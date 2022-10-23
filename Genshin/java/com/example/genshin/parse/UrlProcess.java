package com.example.genshin.parse;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.genshin.gson.DataItem;
import com.example.genshin.gson.ResponseJson;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// 对给定的url进行处理
public class UrlProcess {

    public static final int SUCCESS = 0;
    public static final int END = 1;
    public static final int ERROR = 2;

    private final String apiUrl;    // 查询API接口

    private  String uid;
    private int gacha_type;  // 新手100，常驻200，角色祈愿301，武器祈愿302
    private String gachaTypeString;
    private String end_id = "0";   // 查询的起始id，0表示从头开始查询
    private int page = 1;
    public final int SIZE = 20;

    // 对抽卡结果进行计数
    private int rankThree = 0;
    private int rankFourWeapons = 0;
    private int rankFourCharactor = 0;
    private int rankFiveWeapons = 0;
    private int rankFiveCharactor = 0;

    private int allCount = 0;       // 总抽卡次数
    private final List<RankFiveItem> rankFiveList = new ArrayList<>();     // 对抽五星所用的次数计数
    private int rankFiveCount = 0;      // 五星计数器
    private int count = 0;              // 已有多少抽未出五星

    // 初始化对象，构造出查询的API地址，默认是查询角色池数据
    public UrlProcess(String url) {
        this(url, 301);
    }

    public UrlProcess(String url, int gacha_type) {
        String prefix = "https://hk4e-api.mihoyo.com/event/gacha_info/api/getGachaLog";
        apiUrl = prefix + url.substring(url.indexOf('?'), url.lastIndexOf('#'));
        this.gacha_type = gacha_type;
        switch (gacha_type) {
            case 100:
                gachaTypeString = "新手池";
                break;
            case 200:
                gachaTypeString = "常驻池";
                break;
            case 301:
                gachaTypeString = "角色池";
                break;
            case 302:
                gachaTypeString = "武器池";
                break;
            default:
        }
    }

    // 构建请求的url地址
    public String getRequestUrl() {
        // page不影响请求结果，size表示每次请求数据的数量，最大为20
        return  apiUrl + "&gacha_type=" + gacha_type + "&page=" + page + "&size=" + SIZE + "&end_id=" + end_id;
    }

    // 发出请求一次
    public List<Item> parse() throws IOException {
        String url = getRequestUrl();
        Document document = Jsoup.connect(url)
                .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36")
                .timeout(10000)
                .ignoreContentType(true)
                .get();
        String jsonData = document.body().text();   // 获取返回的json字符串
        Gson gson = new Gson();                     // 使用Gson进行解析
        ResponseJson responseJson = gson.fromJson(jsonData, ResponseJson.class);
        Log.d("Anvei", jsonData);
        if(responseJson.data == null) return null;
        List<DataItem> list = responseJson.data.list;
        int size = list.size();
        if (size < 1) return null;   // 如果长度为0
        allCount += size;
        if (uid == null) uid = list.get(0).uid;
        List<Item> itemList = new ArrayList<>();
        for (DataItem item: list) {
            itemList.add(new Item(uid, Integer.parseInt(item.rank_type), item.item_type, item.name, gachaTypeString, item.time));
            switch (item.rank_type) {
                case "3":
                    rankThree++;
                    rankFiveCount++;
                    break;
                case "4":
                    if (item.item_type.equals("武器")) {
                        rankFourWeapons++;
                    } else {
                        rankFourCharactor++;
                    }
                    rankFiveCount++;
                    break;
                case "5":
                    if (item.item_type.equals("武器")) {
                        rankFiveWeapons++;
                    } else {
                        rankFiveCharactor++;
                    }
                    if (rankFiveList.isEmpty()) count = rankFiveCount;
                    else rankFiveList.get(rankFiveList.size() - 1).count = ++rankFiveCount;
                    rankFiveList.add(new RankFiveItem(itemList.get(itemList.size() - 1)));
                    rankFiveCount = 0;
                    break;
            }
        }
        end_id = list.get(size - 1).end_id;      // 更新end_id
        page++;
        return itemList;
    }

    // 该方法耗时比较久
    public List<Item> parseAll() throws IOException, InterruptedException {
        List<Item> itemList = new ArrayList<>();
        List<Item> temp;
        while ((temp = parse()) != null) {
            itemList.addAll(temp);
            Thread.sleep(200);      // 避免请求速度过快
        }
        return itemList;
    }

    public boolean reSet(int gacha_type) {
        if (gacha_type != 100 &gacha_type != 200 && gacha_type != 301 && gacha_type != 302 ) {
            return false;
        }
        this.gacha_type = gacha_type;
        switch (gacha_type) {
            case 100:
                gachaTypeString = "新手池";
                break;
            case 200:
                gachaTypeString = "常驻池";
                break;
            case 301:
                gachaTypeString = "角色池";
                break;
            case 302:
                gachaTypeString = "武器池";
                break;
            default:
        }
        end_id = "0";
        page = 1;
        rankFiveCharactor = 0;
        rankFiveWeapons = 0;
        rankFourCharactor = 0;
        rankFourWeapons = 0;
        rankThree = 0;
        rankFiveList.clear();
        rankFiveCount = 0;
        count = 0;
        return true;
    }

    @NonNull
    @Override
    public String toString() {
        return "rankFiveCharactor: " + rankFiveCharactor + "\n" +
                "rankFiveWeapons: " + rankFourWeapons + "\n"  +
                "rankFourCharactor: " + rankFourCharactor + "\n" +
                "rankFourWeapons: " + rankFourWeapons + "\n" +
                "rankThree: " + rankThree + "\n" +
                "allCount: " + allCount + "\n";
    }

    public int getRankThree() {
        return rankThree;
    }

    public int getRankFourWeapons() {
        return rankFourWeapons;
    }

    public int getRankFourCharactor() {
        return rankFourCharactor;
    }

    public int getRankFiveWeapons() {
        return rankFiveWeapons;
    }

    public int getRankFiveCharactor() {
        return rankFiveCharactor;
    }

    public List<RankFiveItem> getRankFiveList() {
        return rankFiveList;
    }

    public int getCount() {
        return count;
    }

    public int getCurrentPage() {
        return page - 1;
    }

    public int getAllCount() {
        return allCount;
    }

    public String getGachaTypeString() {
        return gachaTypeString;
    }
}
