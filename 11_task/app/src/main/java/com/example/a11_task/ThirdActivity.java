package com.example.a11_task;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ThirdActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    Button button_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);


        radioGroup = findViewById(R.id.radio_group);
        button_result =findViewById(R.id.result);

        button_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ThirdActivity.this, MainActivity.class);
                Toast.makeText(ThirdActivity.this, "Third", Toast.LENGTH_SHORT).show();
                intent.putExtra("result", "스트레스 점수"+10);
                startActivity(intent);
                Log.i("jiseong","Third");
            }
        });

    }


    /*@Override
    protected void onNewIntent(Intent intent) {
        Log.i("jiseong","onNewIntent");
        super.onNewIntent(intent);
    }*/
}