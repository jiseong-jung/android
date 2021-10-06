package com.example.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    EditText et;
    Button btn_start, btn_stop;

    Timer timer;
    TimerTask timerTask, stopTask;
    int delay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = (EditText)findViewById(R.id.et);
        tv = (TextView) findViewById(R.id.tv);
        btn_start = (Button)findViewById(R.id.btn_start);
        btn_stop = (Button)findViewById(R.id.btn_stop);

        timer = new Timer();

        btn_start.setOnClickListener(listener);
        btn_stop.setOnClickListener(listener1);




    }

    //start button
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            try {
                timerTask = new TimerTask() {
                    int count =0;

                    @Override
                    public void run() {
                        tv.post(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText(++count + "초");
                            }
                        });
                    }
                };

                //타이머가 끝날떄 호출
                stopTask = new TimerTask() {
                    @Override
                    public void run() {
                        tv.setText("timer 종료");
                        timerTask.cancel();
                        timerTask = null;
                    }
                };
                delay = Integer.valueOf(et.getText().toString());
                delay = delay * 1000; //밀리 세컨드로 시간 맞추기
                timer.schedule(stopTask, delay);
                timer.schedule(timerTask, 0, 1000);
                
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };

    //stop button
    View.OnClickListener listener1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(timerTask != null){
                timerTask.cancel();
                timerTask = null;
            }

            if(stopTask != null){
                stopTask.cancel();
                stopTask = null;
            }

        }
    };





}


