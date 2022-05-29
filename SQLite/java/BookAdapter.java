package com.example.sqlite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

    int mResourceId;

    public BookAdapter(Context context, int textViewResourceId, List<Book> books){
        super(context,textViewResourceId,books);
        mResourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Book book = getItem(position);
        View view;
        MyHolder holder;

        //提高ListView性能1
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(mResourceId, parent, false);
            holder = new MyHolder();
            holder.author = (TextView) view.findViewById(R.id.author);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.price = (TextView) view.findViewById(R.id.price);
            holder.pages = (TextView) view.findViewById(R.id.pages);
            view.setTag(holder);
        }else{
            //convertView中保存着已经加载过的子视图
            view = convertView;
            holder = (MyHolder) view.getTag();
        }
        holder.author.setText(book.getAuthor());
        holder.name.setText(book.getName());
        holder.price.setText(String.valueOf(book.getPrice()));
        holder.pages.setText(String.valueOf(book.getPages()));
        return view;
    }

    //提高ListView性能2
    private class MyHolder{
        TextView author;
        TextView name;
        TextView price;
        TextView pages;
    }
}
