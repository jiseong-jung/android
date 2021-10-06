package com.example.time_lamp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    final static int LIGHT_ON_BUTTON = 1;
    final static int LIGHT_OFF_BUTTON = 0;
    int r,g,b;
    ProgressBar pb;
    LinearLayout ll;
    Button btn;
    int light_current;

    Handler my_handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == LIGHT_ON_BUTTON){
                Log.i("jiseong","off");
                if(pb.getProgress()<pb.getMax()){
                    pb.setProgress(pb.getProgress()+1);

                    r= r-3;
                    ll.setBackgroundColor(Color.rgb(r,g,b));

                    sendEmptyMessageDelayed(LIGHT_ON_BUTTON, 50);
                }
            }
            else if(msg.what == LIGHT_OFF_BUTTON){
                Log.i("jiseong","on");
                if(pb.getProgress()>0){
                    pb.setProgress(pb.getProgress()-1);

                    r= r+3;
                    ll.setBackgroundColor(Color.rgb(r,g,b));

                    sendEmptyMessageDelayed(LIGHT_OFF_BUTTON, 50);
                }
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        r= 255;
        g = 0;
        b = 0;
        light_current = 1;

        pb = (ProgressBar) findViewById(R.id.progressBar);
        ll = (LinearLayout) findViewById(R.id.ll);
        btn = (Button) findViewById(R.id.btn);

        ll.setBackgroundColor(Color.rgb(r,g,b));

        btn.setOnClickListener(btn_listener);
    }

    View.OnClickListener btn_listener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if(light_current == 0){
                light_current =0;
                Log.i("jiseong","on click");
                btn.setText("OFF");
                my_handler.removeMessages(LIGHT_ON_BUTTON);
                my_handler.sendEmptyMessage(LIGHT_OFF_BUTTON);
                light_current =1;
            }
            else if(light_current == 1) {
                light_current =1;
                Log.i("jiseong","off click");
                btn.setText("ON");
                my_handler.removeMessages(LIGHT_OFF_BUTTON);
                my_handler.sendEmptyMessage(LIGHT_ON_BUTTON);
                light_current =0;
            }
        }
    };

}




















