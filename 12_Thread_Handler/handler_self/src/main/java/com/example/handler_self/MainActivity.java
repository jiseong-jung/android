package com.example.handler_self;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static final int WHAT_HANDLER_MSG_COUNT = 1;

    TextView tv;
    Button btn_start, btn_stop;
    int count;

    Handler my_handler = new Handler(Looper.myLooper()){

        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == WHAT_HANDLER_MSG_COUNT){
                tv.setText(++count+"second");
                sendEmptyMessageDelayed(WHAT_HANDLER_MSG_COUNT,1000);
            }
        }
    } ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView)findViewById(R.id.tv);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_stop = (Button) findViewById(R.id.btn_stop);

        btn_start.setOnClickListener(listener_start);
        btn_stop.setOnClickListener(listener_stop);
    }


    View.OnClickListener listener_start = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            tv.setText("default second"+count);
            my_handler.sendEmptyMessage(WHAT_HANDLER_MSG_COUNT);

        }
    };


    View.OnClickListener listener_stop = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Toast.makeText(MainActivity.this, "your click stop button", Toast.LENGTH_SHORT).show();

        }
    };

}