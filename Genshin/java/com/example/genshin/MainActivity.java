package com.example.genshin;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.litepal.tablemanager.Connector;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    DrawerLayout drawerLayout;
    CircleImageView titleCircleImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Connector.getDatabase().delete("item", null, null);

        drawerLayout = findViewById(R.id.drawer);
        titleCircleImage = findViewById(R.id.avatar);
        titleCircleImage.setOnClickListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avatar:
                drawerLayout.openDrawer(GravityCompat.START);
        }
    }
}