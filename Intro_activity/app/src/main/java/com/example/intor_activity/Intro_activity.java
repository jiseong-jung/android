package com.example.intor_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Intro_activity extends AppCompatActivity {

    final static int CHANGE_ACTIVITY = 1;
    ProgressBar pb;
    ImageButton img_btn;
    TextView tv;

    Handler my_handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == CHANGE_ACTIVITY){
                if(pb.getProgress()<pb.getMax()){
                    pb.setProgress(pb.getProgress()+1);
                    sendEmptyMessageDelayed(CHANGE_ACTIVITY,100);
                }
                if(pb.getProgress()>=pb.getMax()){
                    Intent intent = new Intent();
                    intent.setClass(Intro_activity.this, MainActivity.class);
                    intent.putExtra("hit","?");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        tv = (TextView)findViewById(R.id.tv);

        my_handler.sendEmptyMessage(CHANGE_ACTIVITY);

        img_btn = (ImageButton) findViewById(R.id.img_button);


        img_btn.setOnClickListener(listener1);
    }

    View.OnClickListener listener1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            tv.setText("img click");
        }
    };
}






















