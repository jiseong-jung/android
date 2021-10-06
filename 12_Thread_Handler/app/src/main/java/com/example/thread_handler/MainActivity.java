package com.example.thread_handler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    Button btn_start, btn_stop;
    int count;

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
                        tv.setText("count: " + count);

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




















