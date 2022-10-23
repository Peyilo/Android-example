package com.example.genshin.parse;

import android.app.AlertDialog;

public class RankFiveItem extends Item {

    public int count = -1;      // 用来记录多少发抽出的五星

    public RankFiveItem(String uid, int rank, String itemType, String name, String gachaType, String time) {
        super(uid, rank, itemType, name, gachaType, time);
    }

    public RankFiveItem(Item item) {
        super();
        this.itemType = item.itemType;
        this.name = item.name;
        this.gachaType = item.gachaType;
        this.time = item.time;
        this.rank = item.rank;
        this.uid = item.uid;
    }
}
