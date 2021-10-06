package com.example.filter2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent get_intent = getIntent();
        String str = get_intent.getStringExtra(Intent.EXTRA_TEXT);

        TextView tv = (TextView) findViewById(R.id.tv_second);
        tv.setText(str);

    }
}