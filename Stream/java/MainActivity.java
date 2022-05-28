package com.example.stream;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private Button mSave;
    private Button mLoad;
    private TextView mTextView;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSave = (Button)findViewById(R.id.save);
        mLoad = (Button)findViewById(R.id.load);
        mTextView = (TextView)findViewById(R.id.textView);
        mEditText = (EditText)findViewById(R.id.editText);

        mSave.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                String str = mEditText.getText().toString();
                save(str);
                mTextView.setText("文件内的内容 : " + str);
            }
        });
        mLoad.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                mEditText.setText(load());
            }
        });

        String str;
        if ((str = load()) != null){
            mTextView.setText("文件内的内容 : " + str);
        }
    }

    //写入文件
    public void save(String str){

        FileOutputStream out;
        BufferedWriter writer = null;

        try{
            out = openFileOutput("stream", MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(str);
        }catch (IOException ex){
            ex.printStackTrace();
        }finally {
            try{
                if (writer != null)
                    writer.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    //读取文件
    public String load(){
        StringBuilder builder = new StringBuilder();
        FileInputStream in;
        BufferedReader reader = null;
        try{
            in = openFileInput("stream");
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while((line = reader.readLine()) != null){
                builder.append(line);
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }finally {
            try{
                if(reader != null)
                    reader.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
        return builder.toString();
    }
}
