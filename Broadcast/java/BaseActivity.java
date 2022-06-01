package com.example.broadcast;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


//其他活动都继承BaseActivity
public class BaseActivity extends AppCompatActivity {

    MyBroadcastReceiver mReceiver;

    @Override
    public void onCreate(Bundle savedInstanceStage){
        super.onCreate(savedInstanceStage);
        Collector.add(this);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Collector.remove(this);
    }
    
    //当活动处于栈顶，就注册广播接收器
    @Override
    public void onResume(){
        super.onResume();
        mReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.my_action");
        registerReceiver(mReceiver, filter);
    }
    
    //当活动离开栈顶，就注销广播接收器
    @Override
    public void onPause(){
        super.onPause();
        unregisterReceiver(mReceiver);
    }
    
    //内部类，自定义广播接收器
    class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("This is a AlertDialog");
            builder.setMessage("Please login again");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    Collector.finishAll();
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                }
            });
            builder.show();
        }
    }
    
    //内部类，用于一次性结束全部活动
    static class Collector{

        static List<Activity> list = new ArrayList<>();

        static void add(Activity activity){
            list.add(activity);
        }

        static void remove(Activity activity){
            list.remove(activity);
        }

        static void finishAll(){
            for(Activity activity : list){
                activity.finish();
            }
        }
    }
}
