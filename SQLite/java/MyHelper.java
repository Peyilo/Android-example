package com.example.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyHelper extends SQLiteOpenHelper {

    //用来传入Context对象，用于显示一个Toast消息
    Context mContext;

    //SQL语句，创建表
    private final String CREATE_BOOK = "create table Book("
            + "id integer primary key autoincrement, "
            + "author text ,"
            + "price real ,"
            + "pages integer ,"
            + "name text )";
    private String CREATE_CATEGORY = "create table Category( "
            + "id integer primary key autoincrement ,"
            + "category_name text ,"
            + "category_code )";

    //构造方法，第三个参数一般填null
    public MyHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_BOOK);
        db.execSQL(CREATE_CATEGORY);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oleVersion, int newVersion){
        //先删除原有的表格再调用一次onCreate()重新更新
        db.execSQL("drop table if exists Book");
        db.execSQL("drop table if exists Category");
        onCreate(db);
    }
}
