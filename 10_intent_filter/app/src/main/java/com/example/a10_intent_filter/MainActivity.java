package com.example.a10_intent_filter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void on_Btn_click(View view) {
        //묵시적 인텐트
        Intent intent = new Intent();
        intent.setType("text/plain");
        intent.setAction("com.example.a10_intent_filter");
        startActivity(intent);
    }

}