package com.example.each_touch_event;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String str_motion[] = {"Down", "Up", "move"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = (TextView) findViewById(R.id.tv);
        tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.i("jiseong","TextView: "+ str_motion[motionEvent.getAction()]);
                return false;
            }
        });

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.i("jiseong","Relative: "+ str_motion[motionEvent.getAction()]);
                return true;
            }
        });



    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        Log.i("jiseong","activite: "+ str_motion[motionEvent.getAction()]);
        return true;
    }




}