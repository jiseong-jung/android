package com.example.study_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class intro_activity extends AppCompatActivity {

    int next;
    ProgressBar pb;
    ImageButton imageButton;
    TextView tip;
    String[] tip_list = {"dog is poll", "human is Poop", "Cat is God"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        setTitle("?");

        imageButton = (ImageButton) findViewById(R.id.image);
        tip = (TextView)findViewById(R.id.tip);
        pb = (ProgressBar)findViewById(R.id.progressBar);

        next= 0;


        Thread th_count = new Thread("count thread"){


            @Override
            public void run() {

                for(int i=1; i<6; i++){
                    try {
                        Thread.sleep(100);//to do 1000
                        pb.setProgress(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setClass(intro_activity.this, Login_activity.class);
                startActivity(intent);
            }



        };
        th_count.start();

        imageButton.setOnClickListener(btn_listener);

    }

    View.OnClickListener btn_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            tip.setText(tip_list[next++]);
            if(next == 2){
                next=0;
            }
        }
    };


}























