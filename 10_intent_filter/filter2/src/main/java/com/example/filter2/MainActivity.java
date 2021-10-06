package com.example.filter2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = (EditText) findViewById(R.id.et);

    }

    public void onClick(View view) {
        Intent intent = new Intent();
        String str = et.getText().toString();

        intent.setAction(Intent.ACTION_SEND);
        //intent.setAction("com.example.filter2");
        intent.putExtra(Intent.EXTRA_TEXT, str);
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, "어떤 액티비티를 실행할까요?"));

    }
}