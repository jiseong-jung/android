package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    final static int WHAT_CHECK_MESSAGE_PROGRESS = 1;
    final static int WHAT_CHECK_TOUCH = 2;

    RelativeLayout rl;
    TextView tv;
    ProgressBar pb;
    Button button;
    int count =0;
    int second=0;

    Handler my_handler = new Handler(Looper.myLooper()){

        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == WHAT_CHECK_MESSAGE_PROGRESS){
                if(pb.getProgress() <pb.getMax()){

                    pb.setProgress(pb.getProgress() + 1);
                    second++;
                    sendEmptyMessageDelayed(WHAT_CHECK_MESSAGE_PROGRESS, 10);


                }
                else {
                    Log.i("jiseong", "time over");
                }
            }
            else if(msg.what == WHAT_CHECK_TOUCH){
                tv.setText(""+count++);
            }
            else
                tv.setText("time over");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb = (ProgressBar) findViewById(R.id.progressBar);
        button = (Button) findViewById(R.id.btn1);
        tv = (TextView)findViewById(R.id.tv);
        rl = (RelativeLayout)findViewById(R.id.rl);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                my_handler.sendEmptyMessage(WHAT_CHECK_MESSAGE_PROGRESS);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN){
            if(second >= 60){
                tv.setText("Time Over");
                return false;
            }
            else {
                my_handler.sendEmptyMessage(WHAT_CHECK_TOUCH);
                Random random = new Random();
                int r,g,b;
                r= random.nextInt(256)+1;
                g= random.nextInt(256)+1;
                b= random.nextInt(256)+1;
                rl.setBackgroundColor(Color.rgb(r,g,b));

            }
        }

        return true;
    }
}