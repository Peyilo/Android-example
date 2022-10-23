package com.example.genshin.gson;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class DataItem {

    public String uid;

    public String gacha_type;

    public String item_id;

    public String count;

    public String time;

    public String name;

    public String lang;

    public String item_type;

    public String rank_type;        // 星级

    @SerializedName("id")
    public String end_id;

    @NonNull
    @Override
    public String toString() {
        return "DataItem{" +
                "uid='" + uid + '\'' +
                ", gacha_type='" + gacha_type + '\'' +
                ", item_id='" + item_id + '\'' +
                ", count='" + count + '\'' +
                ", time='" + time + '\'' +
                ", name='" + name + '\'' +
                ", lang='" + lang + '\'' +
                ", item_type='" + item_type + '\'' +
                ", rank_type='" + rank_type + '\'' +
                ", end_id='" + end_id + '\'' +
                '}';
    }
}
