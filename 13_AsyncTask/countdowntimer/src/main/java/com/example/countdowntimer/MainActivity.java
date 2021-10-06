package com.example.countdowntimer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    EditText et;
    Button btn_start;
    MyCountDown_Timer timer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);
        et = (EditText) findViewById(R.id.et);
        btn_start = (Button) findViewById(R.id.btn_start);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sec = Integer.valueOf(et.getText().toString());
                int millisInFuture = sec*1000;
                timer = new MyCountDown_Timer(millisInFuture,1000);
                timer.start();
            }
        });



    }

    class MyCountDown_Timer extends CountDownTimer{

        public MyCountDown_Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            tv.setText(l/1000 +"초");
        }

        @Override
        public void onFinish() {
            tv.setText("timer END");
        }
    }



}