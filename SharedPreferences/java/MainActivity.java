package com.example.sharedpreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText mEditText;
    private Button mButton;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取SharedPreferences实例
        pref = getSharedPreferences("test",MODE_PRIVATE);

        mButton = (Button)findViewById(R.id.save);
        mEditText = (EditText)findViewById(R.id.editText);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edit()获取SharedPreferences.Editor实例，再使用putString()添加数据，最后使用apply()提交
                editor = pref.edit();
                editor.putString("editText", mEditText.getText().toString());
                editor.apply();
            }
        });

        //当活动创建的时候检查时候是否有已经存储的数据，如果有就将数据呈现在EditText实例中
        String str = pref.getString("editText","");
        if(!TextUtils.isEmpty(str)){        //使用TextUtils.isEmpty()判断字符串是否为null或者空字符串
            mEditText.setText(str);
        }
    }
}