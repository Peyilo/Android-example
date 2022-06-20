package com.example.simulatepositioning;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lljjcoder.citypickerview.widget.CityPicker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    UserDao dao;
    List<Map<String,Object>> list;
    List<String> listCity=new ArrayList<>();
    SimpleAdapter adapter;
//    private ListView listView;
    private LinearLayout bg;
    private Button btn_search;
    private ImageButton likes,likeCityBtn,map,share,bgicon;
    private EditText et_cityname;
    private ImageView iv,iv1,iv2;
    private TextView tv_city, tv_weather, tv_temp, tv_wind, tv_humidity, tv_forcast1, tv_forcast2, tv_forcast3,hint;
    private WeatherInfo weatherInfo;
    String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_search=findViewById(R.id.city);
        initialization();

        Intent intent = getIntent();//getIntent();获取当前intent对象
        if (intent.getStringExtra("city")!=null){
            city = intent.getStringExtra("city");
        }else {
            city = "宁波";
        }


        if (listCity.contains(city)){
            likes.setImageResource(R.drawable.likes);
        }else{
            likes.setImageResource(R.drawable.like);
        }

        dao=new UserDao(this);
        reFreshData();

        getRealWeather();
        getForcastWeather();
        likes.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        likeCityBtn.setOnClickListener(this);
        map.setOnClickListener(this);
        share.setOnClickListener(this);
        bgicon.setOnClickListener(this);
//        btn_search.setOnClickListener(this);
    }

    private void initialization() {
//        listView=findViewById(R.id.listView);
//        btn_search = findViewById(R.id.search);
//        et_cityname = findViewById(R.id.cityname);
        iv = findViewById(R.id.ima);
        iv1 = findViewById(R.id.ima1);
        iv2 = findViewById(R.id.ima2);
        tv_city = findViewById(R.id.city);
        tv_weather = findViewById(R.id.weather);
        tv_temp = findViewById(R.id.temp);
        tv_wind = findViewById(R.id.wind);
        tv_humidity = findViewById(R.id.humidity);
        tv_forcast1 = findViewById(R.id.forcast1);
        map = findViewById(R.id.map);
        share = findViewById(R.id.share);
        tv_forcast2 = findViewById(R.id.forcast2);
        tv_forcast3 = findViewById(R.id.forcast3);
        likes=findViewById(R.id.like);
        bgicon=findViewById(R.id.bgicon);
        hint=findViewById(R.id.hint);
        likeCityBtn=findViewById(R.id.likeCityBtn);
//        listView=findViewById(R.id.listView);
        bg=findViewById(R.id.bg);
    }

    private void reFreshData() { //加载所有的数据,用于初始化，添加，删除，修改后的数据
        List<String> cityList=dao.getAll();
        list=new ArrayList<>();
        listCity=new ArrayList<>();
        if (cityList!=null && cityList.size()!=0){
            for (String c:cityList){
                Map<String,Object> map=new HashMap<>();
                map.put("city",c);
                listCity.add(c);
//                map.put("username",user.getUsername());
//                map.put("password",user.getPassword());
//                map.put("phone",user.getPhone());
                list.add(map);
            }

            if (listCity.contains(city)){
                likes.setImageResource(R.drawable.likes);
            }else{
                likes.setImageResource(R.drawable.like);
            }

            adapter =new SimpleAdapter(this,list,R.layout.item,new String[]{"city"},
                    new int[]{R.id.likeCity});
//            listView.setAdapter(adapter);
        }
    }

    private void insertData() {//添加城市
        dao.insert(city);
        reFreshData();
    }

    private void deleteData() {
        dao.delete(city);
        reFreshData();
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.city){//城市选择
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                selectAddress();//调用CityPicker选取区域

            }
        }else if (view.getId()==R.id.like){//收藏城市
            if (listCity.contains(city)){
                deleteData();
                Toast.makeText(MainActivity.this, "提示：\n取消关注成功！", Toast.LENGTH_SHORT).show();
                likes.setImageResource(R.drawable.like);
            }else{
                insertData();
                Toast.makeText(MainActivity.this, "提示：\n关注成功！", Toast.LENGTH_SHORT).show();
                likes.setImageResource(R.drawable.likes);
            }
        }else if (view.getId()==R.id.likeCityBtn){//进入收藏城市的页面
            Intent intent=new Intent(MainActivity.this,likeCity.class);
            startActivity(intent);
        }else if (view.getId()==R.id.map){
            Intent intent=new Intent(MainActivity.this,test.class);
            startActivity(intent);
        }else if(view.getId()==R.id.share){
            allShare();
        }else if (view.getId()==R.id.bgicon){
            Intent intent=new Intent(MainActivity.this,BgImage.class);
            startActivity(intent);
        }
//        city="温州";
//        getRealWeather();
//        getForcastWeather();
    }

    private void selectAddress() {
        CityPicker cityPicker = new CityPicker.Builder(MainActivity.this)
                .textSize(14)
                .title("地址选择")
                .titleBackgroundColor("#FFFFFF")
                .confirTextColor("#696969")
                .cancelTextColor("#696969")
                .province("浙江省")
                .city("温州市")
                .district("龙港市")
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();
        cityPicker.show();
        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];
                //城市
//                city = citySelected[1];

                //区县（如果设定了两级联动，那么该项返回空）
                city = citySelected[2];
                if (!listCity.contains(city)){
                    likes.setImageResource(R.drawable.like);
                }else{
                    likes.setImageResource(R.drawable.likes);
                }
                getRealWeather();
                getForcastWeather();
                //邮编
                String code = citySelected[3];
                //为TextView赋值
            }
        });
    }

    private void getForcastWeather() {
        String url1 = "https://restapi.amap.com/v3/weather/weatherInfo?city=CITY&key=d72b046349b274bf3d90c1ba900407fc&extensions=all";
        url1 = url1.replace("CITY", city);
//        String url1="";
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder().url(url1).build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {

            @Override

            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "city:"+city, Toast.LENGTH_SHORT).show();
                    }
                });

            }


            @Override

            public void onResponse(Call call, Response response) throws IOException {

                String json = response.body().string();

                try {

                    JSONObject object = new JSONObject(json);

                    String status = object.getString("status");

                    if (status.equals("1")) {

                        JSONArray array = object.getJSONArray("forecasts");

                        JSONObject obj2 = (JSONObject) array.get(0);

                        JSONArray array2 = obj2.getJSONArray("casts");

                        final List<ForeCast> list = new ArrayList<>();

                        for (int i = 0; i < array2.length(); i++) {

                            JSONObject for_obj = (JSONObject) array2.get(i);
                            String date = for_obj.getString("date");
                            String week = for_obj.getString("week");
                            String dayweather = for_obj.getString("dayweather");
                            String nightweather = for_obj.getString("nightweather");
                            String daytemp = for_obj.getString("daytemp");
                            String nighttemp = for_obj.getString("nighttemp");
                            String daywind = for_obj.getString("daywind");
                            String nightwind = for_obj.getString("nightwind");
                            String daypower = for_obj.getString("daypower");
                            String nightpower = for_obj.getString("nightpower");
                            ForeCast fc = new ForeCast(date, week, dayweather, nightweather, daytemp, nighttemp, daywind, nightwind, daypower, nightpower);
                            list.add(fc);

                        }

                        runOnUiThread(

                                new Runnable() {

                                    @Override

                                    public void run() {

                                        System.out.println(list);

                                        ForeCast fc1 = list.get(0);

                                        StringBuffer sb1 = new StringBuffer();

                                        sb1.append("今天 · ");
                                        String we=fc1.getDayweather()+"转"+fc1.getNightweather();
                                        sb1.append(we).append("  ");
                                        for (int i = we.length(); i <30 ; i++) {
                                            sb1.append(" ");
                                        }
                                        iv.setImageResource(getWeather(we));
                                        sb1.append(fc1.getDaytemp()+"°").append("/").append(fc1.getNighttemp()+"°");
//                                        sb1.append("白天:").append(fc1.getDaywind()).append("风").append(fc1.getDaypower()).append("级\n");
//                                        sb1.append("晚上:").append(fc1.getNightwind()).append("风").append(fc1.getNightpower()).append("级");
                                        tv_forcast1.setText(sb1.toString());

//                                        轮播走马灯提示语句state
                                        String hintText="";
                                        if (Math.abs(Integer.parseInt(fc1.getDaytemp())-Integer.parseInt(fc1.getNighttemp()))>=8){
                                            hintText+="今日昼夜温差较大，晚归请携带外套出门，防止感冒";
                                        }else if (Integer.parseInt(fc1.getDaytemp())>30){
                                            hintText+="今天明天温度较高，高温徘徊暑气难消，注意防暑";
                                        }else if (Integer.parseInt(fc1.getDaytemp())>18){
                                            hintText+="今天温度凉爽，慎防流感，请特别注意增减衣服保暖防寒";
                                        }

                                        if (fc1.getNightweather().indexOf("雨")!=-1){
                                            hintText+="  行走时，请不要在光面大理石上停留，避免滑倒，尽量走有安全防范措施的通道";
                                        }else if (fc1.getNightweather().indexOf("云")!=-1){
                                            hintText+="  悠悠的云里有淡淡的诗，淡淡的诗里有绵绵的喜悦，绵绵的喜悦里有我轻轻的问候，气候即将转冷，请注意身体！";
                                        }else if (fc1.getNightweather().indexOf("雪")!=-1){
                                            hintText+="  老年人、小孩尽量少出门以避免不必要伤害，雪天路滑，出行时请您注意防滑";
                                        }else if (fc1.getNightweather().indexOf("雾")!=-1){
                                            hintText+="  大雾/雾霾 天气请减少外出";
                                        }else if (fc1.getNightweather().indexOf("风")!=-1){
                                            hintText+="  勿在玻璃门、窗、广告牌，大树附近停留,对容易坠落之物体（如空调、空调架、广告牌）进行检查，确保不会坠落，及时收取阳台晾晒的衣物和物品，以免发生丢失或掉落砸伤他人";
                                        }

                                        hint.setText(hintText);

//                                        轮播走马灯提示语句stop

                                        ForeCast fc2 = list.get(1);

                                        StringBuffer sb2 = new StringBuffer();

                                        sb2.append("明天 · ");
                                        we=fc2.getDayweather()+"转"+fc2.getNightweather();
                                        sb2.append(we).append("  ");
                                        for (int i = we.length(); i <30 ; i++) {
                                            sb2.append(" ");
                                        }
                                        iv1.setImageResource(getWeather(we));
                                        sb2.append(fc2.getDaytemp()+"°").append("/").append(fc2.getNighttemp()+"°");
//                                        sb2.append("白天:").append(fc2.getDaywind()).append("风").append(fc2.getDaypower()).append("级\n");
//                                        sb2.append("晚上:").append(fc2.getNightwind()).append("风").append(fc2.getNightpower()).append("级");
                                        tv_forcast2.setText(sb2.toString());

                                        ForeCast fc3 = list.get(2);

                                        StringBuffer sb3 = new StringBuffer();

                                        sb3.append("后天 · ");
                                        we=fc3.getDayweather()+"转"+fc3.getNightweather();
                                        sb3.append(we).append("  ");
                                        for (int i = we.length(); i <30 ; i++) {
                                            sb3.append(" ");
                                        }
                                        iv2.setImageResource(getWeather(we));
                                        sb3.append(fc3.getDaytemp()+"°").append("/").append(fc3.getNighttemp()+"°");
//                                        sb3.append("白天:").append(fc3.getDaywind()).append("风").append(fc3.getDaypower()).append("级\n");
//                                        sb3.append("晚上:").append(fc3.getNightwind()).append("风").append(fc3.getNightpower()).append("级");
                                        tv_forcast3.setText(sb3.toString());

                                    }

                                }

                        );

                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

            }

        });
    }



    public static Integer getWeather(String weather){
        if (weather.indexOf("晴")!=-1){
            return R.drawable.a0;
        }else if (weather.indexOf("多云")!=-1){
            return R.drawable.a4;
        }else if (weather.indexOf("阴")!=-1){
            return R.drawable.a9;
        }else if (weather.indexOf("雪")!=-1){
            return R.drawable.a17;
        }else if (weather.indexOf("雨")!=-1){
            return R.drawable.a14;
        }
        return R.drawable.a0;
    }

    //当前天气
    private void getRealWeather() {
        String realUrl = "https://restapi.amap.com/v3/weather/weatherInfo?city=CITY&key=d72b046349b274bf3d90c1ba900407fc&extensions=base";
        realUrl = realUrl.replace("CITY", city);
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(realUrl).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("错误信息："+e);
                        Toast.makeText(MainActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    JSONObject object = new JSONObject(json);
                    String status = object.getString("status");
                    if (status.equals("1")) {
                        JSONArray array = object.getJSONArray("lives");
                        JSONObject real_obj = (JSONObject) array.get(0);
                        String city = real_obj.getString("city");
                        final String weather = real_obj.getString("weather");
                        int temp = real_obj.getInt("temperature");
                        String winddirection = real_obj.getString("winddirection");
                        String windpower = real_obj.getString("windpower");
                        int hunidity = real_obj.getInt("humidity");
                        String reporttime = real_obj.getString("reporttime");
                        weatherInfo = new WeatherInfo(city, weather, temp, winddirection, windpower, hunidity, reporttime);
                        runOnUiThread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void run() {
                                if (weatherInfo.getWeather().indexOf("雨")!=-1){
                                    musicMp3(R.raw.yu);
                                }else{
                                    musicMp3(R.raw.qing);
                                }
                                tv_city.setText(weatherInfo.getCity());
                                tv_weather.setText(weatherInfo.getWeather());
                                iv.setImageResource(Util.getPicByWeather(weatherInfo.getWeather()));
                                tv_temp.setText(weatherInfo.getTemperature()+"");

                                Intent intent = getIntent();//getIntent();获取当前intent对象
                                if (intent.getStringExtra("bgimage")!=null && !intent.getStringExtra("bgimage").equals("")){
                                    bg.setBackgroundResource(Integer.parseInt(intent.getStringExtra("bgimage")));
                                }else{
                                    if (intent.getParcelableExtra("bgimageBitmap")!=null){
//                                        Bundle b=intent.getExtras();
                                        Bitmap bmp=(Bitmap) intent.getParcelableExtra("bgimageBitmap");
                                        BitmapDrawable bd=new BitmapDrawable(bmp);
                                        bg.setBackground(bd);
                                    }else{
                                        bg.setBackgroundResource(getBgImage(weatherInfo.getWeather()));
                                    }

                                }

                                tv_wind.setText(weatherInfo.getWinddirection() + weatherInfo.getWindpower() + "级");
                                tv_humidity.setText("湿度" + weatherInfo.getHumidity());
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private MediaPlayer mp;
    private String path;
    private void musicMp3(Integer str) {
//        path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/qing.mp3";
//        mp= new MediaPlayer();
        try {
            mp= MediaPlayer.create(this,str);
//            mp.setDataSource(path);
//            mp.prepare();
            mp.setLooping(true);
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Integer getBgImage(String we) {
        if (we.indexOf("晴")!=-1){
            return R.drawable.qing;
        }else if (we.indexOf("多云")!=-1){
            return R.drawable.yu;
        }else if (we.indexOf("阴")!=-1){
            return R.drawable.ying;
        }else if (we.indexOf("雨")!=-1){
            return R.drawable.yu;
        }
        return R.drawable.qing;
    }

    /**
     * Android原生分享功能
     * 默认选取手机所有可以分享的APP
     */
    public void allShare(){
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
        share_intent.setType("text/plain");//设置分享内容的类型
        share_intent.putExtra(Intent.EXTRA_SUBJECT, "我在"+city);//添加分享内容标题
        share_intent.putExtra(Intent.EXTRA_TEXT, "我在"+city+"天气为："+tv_weather.getText()+",湿度："+tv_humidity.getText()+",当前气温："+tv_temp.getText()+"°");//添加分享内容
        //创建分享的Dialog
        share_intent = Intent.createChooser(share_intent, "share");
        startActivity(share_intent);
    }
    /**
     * Android原生分享功能
     * @param appName:要分享的应用程序名称
     */
    private void share(String appName) {
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);
        share_intent.setType("text/plain");
        share_intent.putExtra(Intent.EXTRA_SUBJECT, "我在"+city);//添加分享内容标题
        share_intent.putExtra(Intent.EXTRA_TEXT, "我在"+city+"天气为："+tv_weather+",湿度："+tv_humidity+",当前气温："+tv_temp+"°");//添加分享内容
        //创建分享的Dialog
        share_intent = Intent.createChooser(share_intent, "share");
        startActivity(share_intent);
    }
}
