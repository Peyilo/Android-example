package com.example.genshin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.genshin.parse.Item;
import com.example.genshin.parse.UrlProcess;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

import java.io.IOException;
import java.util.List;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Connector.getDatabase();
        Button startParse = findViewById(R.id.start_parse);
        EditText editText = findViewById(R.id.input);

        startParse.setOnClickListener(v -> startParse(editText.getText().toString()));
    }

    public void startParse(String url) {
        if (TextUtils.isEmpty(url))
            return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                UrlProcess urlProcess = new UrlProcess(url);
                List<Item> temp;
                putToast("开始解析角色池");
                while (true){
                    try {
                        if ((temp = urlProcess.parse()) == null) break;
                        LitePal.saveAll(temp);
                        Thread.sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                putToast("开始解析武器池");
                urlProcess.reSet(302);
                while (true){
                    try {
                        if ((temp = urlProcess.parse()) == null) break;
                        LitePal.saveAll(temp);
                        Thread.sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                putToast("开始解析常驻池");
                urlProcess.reSet(200);
                while (true){
                    try {
                        if ((temp = urlProcess.parse()) == null) break;
                        LitePal.saveAll(temp);
                        Thread.sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                putToast("开始解析新手池");
                urlProcess.reSet(100);
                while (true){
                    try {
                        if ((temp = urlProcess.parse()) == null) break;
                        LitePal.saveAll(temp);
                        Thread.sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                putToast("解析完成");
            }
        }).start();
    }

    public void putToast(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(StartActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}