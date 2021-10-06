package com.example.thread_handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    static final int WHAT_HANDLER_MSG_COUNT = 1;

    TextView tv;
    Button btn_start, btn_stop;
    int count;

    Handler myHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == WHAT_HANDLER_MSG_COUNT){
                tv.setText(msg.arg1 + "second");
                Toast.makeText(MainActivity.this,msg.arg2+"",Toast.LENGTH_SHORT).show();
            }
        }
    };

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

            count =0;
            tv.setText(count+"second");

            Thread th_count = new Thread("count Thread"){
                @Override
                public void run() {
                    for (int i = 0; i < 5; i++) {

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        count++;
                        Message message = myHandler.obtainMessage(WHAT_HANDLER_MSG_COUNT, count, 0);
                        myHandler.sendMessage(message);
                        Log.i("jiseong", "count:" + count);

                    }
                }
            };
            th_count.start();
        }
    };

    View.OnClickListener listener_stop = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Toast.makeText(MainActivity.this, "your click stop button", Toast.LENGTH_SHORT).show();

        }
    };




}




















