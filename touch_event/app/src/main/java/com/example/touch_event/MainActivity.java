package com.example.touch_event;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = (TextView) findViewById(R.id.tv);
        tv.setOnTouchListener(listener);

    }

    View.OnTouchListener listener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            String str_action[] = {"Down", "Up", "Move"};

            int action = motionEvent.getAction();
            float x = motionEvent.getX();
            float y = motionEvent.getY();

            if(action>=0 && action < str_action.length) {
                Log.i("jiseong", "\n" + "onTouch :" + str_action[action] + " x: " + x + " y:" + y + "\n" +
                        "Event_Time :" + motionEvent.getEventTime() + "\n" +
                        "Down_Event_Time :" + motionEvent.getDownTime()
                );

            }
            return true;
        }
    };



}