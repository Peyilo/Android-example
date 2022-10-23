package com.example.genshin.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.genshin.ItemAdapter;
import com.example.genshin.MainActivity;
import com.example.genshin.R;
import com.example.genshin.parse.Item;

import org.litepal.LitePal;

import java.util.List;

public class FragmentTwo extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    ItemAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_two, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        List<Item> list = LitePal.where("gachaType = ?", "角色池")
                .find(Item.class);
        adapter = new ItemAdapter(list);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onStart() {
        super.onStart();
    }
}