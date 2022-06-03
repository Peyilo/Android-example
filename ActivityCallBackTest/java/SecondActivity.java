package com.example.activitycallbacktest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    EditText edit;
    Button finish;
    String returnData;

    @Override
    public void onCreate(Bundle savedInstanceStage){
        super.onCreate(savedInstanceStage);
        setContentView(R.layout.activity_second);

        Intent intent = getIntent();
        String data = intent.getStringExtra("data_request");
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();

        edit = (EditText) findViewById(R.id.edit_text);
        finish = (Button) findViewById(R.id.finish);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnData = edit.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("data_return", returnData);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
