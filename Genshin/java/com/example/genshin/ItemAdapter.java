package com.example.genshin;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.genshin.parse.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{

    List<Item> list;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView type;
        TextView name;
        TextView time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.recycler_type);
            name = itemView.findViewById(R.id.recycler_name);
            time = itemView.findViewById(R.id.recycler_time);
        }
    }

    public ItemAdapter(List<Item> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = list.get(position);
        holder.type.setText(item.getItemType());
        holder.name.setText(item.getName());
        switch (item.getRank()) {
            case 5:
                holder.name.setTextColor(Color.YELLOW);
                break;
            case 4:
                holder.name.setTextColor(Color.BLUE);
            default:
                break;
        }
        holder.time.setText(item.getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
