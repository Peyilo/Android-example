package com.example.review_material;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context context;

    private final List<Picture> list;

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView pic;

        TextView picName;

        public ViewHolder(View view) {
            super(view);
            pic = view.findViewById(R.id.recy_item_image);
            picName = view.findViewById(R.id.recy_item_name);
        }
    }

    public ImageAdapter(List<Picture> list){
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picture picture = list.get(position);
        holder.picName.setText(picture.getName());
        Glide.with(context).load(picture.getImageId()).into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
