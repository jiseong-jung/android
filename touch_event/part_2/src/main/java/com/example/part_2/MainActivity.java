package com.example.part_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
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
        return super.onTouchEvent(motionEvent);
    }
}
