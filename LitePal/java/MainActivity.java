package com.example.litepal;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import org.litepal.tablemanager.Connector;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Book> mBookList = new ArrayList<>();
    private BookAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button create = (Button)findViewById(R.id.Create);
        Button dataAdd = (Button)findViewById(R.id.add);
        Button dataUpdate = (Button)findViewById(R.id.update);
        Button dataDelete = (Button)findViewById(R.id.delete);
        Button dataQuery = (Button)findViewById(R.id.query);

        create.setOnClickListener(this);
        dataAdd.setOnClickListener(this);
        dataDelete.setOnClickListener(this);
        dataQuery.setOnClickListener(this);
        dataUpdate.setOnClickListener(this);

        mAdapter = new BookAdapter(this, R.layout.sub,mBookList);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(mAdapter);

        initialList();
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.Create:
                LitePal.initialize(this);
                Toast.makeText(this, "Succeed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.add:
                Book book = new Book("Anvei","Code",45, 564);
                book.save();
                mBookList.add(book);
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.delete:
                LitePal.deleteAll(Book.class, "id > ?", "4");
                initialList();
                break;
            case R.id.update:
                Book book1 = new Book();
                book1.setAuthor("Execution");
                book1.setName("Programming");
                book1.updateAll("name = ? and author = ?", "Code", "Anvei");
                initialList();
                break;
            case R.id.query:
                initialList();
                break;
            default:
                break;
        }
    }

    public void initialList(){
        if(!mBookList.isEmpty()){
            mBookList.clear();
        }
        List<Book> tempList = LitePal.findAll(Book.class);
        for (Book book: tempList){
            mBookList.add(book);
        }
        mAdapter.notifyDataSetChanged();
    }
}