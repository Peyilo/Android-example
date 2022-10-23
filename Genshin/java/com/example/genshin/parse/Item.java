package com.example.genshin.parse;

import androidx.annotation.NonNull;

import org.litepal.crud.LitePalSupport;

public class Item extends LitePalSupport {

    public String uid;          // uid

    public String itemType;     // 武器还是角色

    public int rank;            // 星级

    public String name;         // 名字

    public String gachaType;    // 卡池信息

    public String time;         // 抽卡时间



    protected Item() {}

    public Item(String uid, int rank, String itemType, String name, String gachaType, String time) {
        this.uid = uid;
        this.rank = rank;
        this.itemType = itemType;
        this.name = name;
        this.gachaType = gachaType;
        this.time = time;
    }

    @NonNull
    @Override
    public String toString() {
        return "rank = " + rank +
                "\titemType = " + itemType +
                "\tname = " + name +
                "\tgachaType = " + gachaType +
                "\ttime = " + time;
    }

    public int getRank() {
        return rank;
    }

    public String getItemType() {
        return itemType;
    }

    public String getName() {
        return name;
    }

    public String getGachaType() {
        return gachaType;
    }

    public String getTime() {
        return time;
    }

    public String getUid() {
        return uid;
    }
}
