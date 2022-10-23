package com.example.genshin;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class TitleLayout extends RelativeLayout implements View.OnClickListener{

    protected CircleImageView circleImageView;

    protected ImageButton btn_add;

    protected DrawerLayout drawerLayout;
    public TitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.title_layout, this);

        drawerLayout = findViewById(R.id.drawer);

        circleImageView = view.findViewById(R.id.avatar);
        btn_add = view.findViewById(R.id.title_add);

        circleImageView.setOnClickListener(this);
        btn_add.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.avatar:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.title_add:
                Intent intent = new Intent(getContext(), MainActivity.class);
                getContext().startActivity(intent);
            default:
                break;
        }
    }

}
