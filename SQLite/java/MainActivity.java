package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Book> mBookList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyHelper helper = new MyHelper(this, "myDatabase", null, 1);
        Button create = (Button)findViewById(R.id.create);
        Button add = (Button)findViewById(R.id.add);
        Button update = (Button)findViewById(R.id.update);
        Button delete = (Button)findViewById(R.id.delete);
        Button query = (Button)findViewById(R.id.query);

        BookAdapter adapter = new BookAdapter(this,R.layout.activity_sub,mBookList);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        //创建数据库
        create.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                helper.getReadableDatabase();
            }
        });

        //往数据库添加数据
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ContentValues values = new ContentValues();
                values.put("author","Anvei");
                values.put("price",12.4);
                values.put("pages",431);
                values.put("name","Code");
                SQLiteDatabase db = helper.getReadableDatabase();
                db.insert("Book",null,values);
                //清空values后再次添加数据
                values.clear();
                values.put("author","Anvei");
                values.put("price",23.4);
                values.put("pages",546);
                values.put("name","Programming");
                db.insert("Book",null,values);
                Toast.makeText(MainActivity.this,"添加数据成功",Toast.LENGTH_SHORT).show();
            }
        });

        //更新数据
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put("name","Book name 1");
                values.put("price",47);
                SQLiteDatabase db = helper.getReadableDatabase();
                db.update("Book",values,"id > ?",new String[]{"4"});
                Toast.makeText(MainActivity.this,"Update data successes",Toast.LENGTH_SHORT).show();
            }
        });

        //删除数据
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = helper.getReadableDatabase();
                db.delete("Book","name = ?",new String[]{"Book name 1"});
                Toast.makeText(MainActivity.this,"Delete data successes",Toast.LENGTH_SHORT).show();
            }
        });;

        //查询数据，并且将查询结果显示在ListView中
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = helper.getReadableDatabase();
                Cursor cursor = db.query("Book",null,null,null,null,null,null);
                Book mBook;
                mBookList.clear();
                if(cursor.moveToFirst()){
                    do{
                        @SuppressLint("Range") String author = cursor.getString(cursor.getColumnIndex("author"));
                        @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                        @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        @SuppressLint("Range") int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        mBook = new Book(author,name,price,pages);
                        mBookList.add(mBook);
                    }while(cursor.moveToNext());
                }
                cursor.close();

                //刷新ListView
                adapter.notifyDataSetChanged();
            }
        });;
    }
}