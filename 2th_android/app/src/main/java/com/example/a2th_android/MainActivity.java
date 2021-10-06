package com.example.a2th_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    final static int PROGRESS_START = 0;
    final static int PROGRESS_STOP = 1;

    static int right_answer;
    static int right_count = 0;
    TextView process;
    Button btn_start, btn_stop;
    ProgressBar pb;

    Handler my_handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {

            if (msg.what == PROGRESS_START) {
                if (pb.getProgress() < pb.getMax()) {
                    pb.setProgress(pb.getProgress() + 1);
                    sendEmptyMessageDelayed(PROGRESS_START, 100);
                }
                else if(pb.getProgress() == pb.getMax()){
                    process.setText("END");
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.times_table);

        TextView question = (TextView) findViewById(R.id.question);
        btn_start = (Button)findViewById(R.id.btn_start);
        btn_stop = (Button)findViewById(R.id.btn_stop);
        pb = (ProgressBar)findViewById(R.id.progress);
        process = (TextView)findViewById(R.id.process);

        Random rand = new Random();

        int rand_val1 = rand.nextInt(9)+1;
        int rand_val2 = rand.nextInt(9)+1;
        right_answer = rand_val1 * rand_val2;


        String val1_s = Integer.toString(rand_val1);
        String val2_s = Integer.toString(rand_val2);

        //문제 textview설정
        question.setText(val1_s + "*" + val2_s + "=");

        btn_start.setOnClickListener(start_listener);
        btn_stop.setOnClickListener(stop_listener);

    }

    View.OnClickListener start_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            process.setText("START");
            my_handler.sendEmptyMessage(PROGRESS_START);
        }
    };

    View.OnClickListener stop_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            process.setText("STOP");
            my_handler.removeMessages(PROGRESS_START);
        }
    };


    public void btn_click(View view){

        if(pb.getProgress()<pb.getMax() && pb.getProgress() != 0) {

            TextView answer = (TextView) findViewById(R.id.answer);
            TextView result = (TextView) findViewById(R.id.result);
            TextView count = (TextView) findViewById(R.id.count);
            TextView question = (TextView) findViewById(R.id.question);
            //int answer2 = Integer.parseInt(answer1);

            //정답 확인용
            Toast.makeText(MainActivity.this, right_answer + "", Toast.LENGTH_SHORT).show();

            String str = (String) ((Button) view).getText(); //버튼 text 추가

            if (str.equals("확인")) {
                int answer2 = Integer.parseInt(answer.getText().toString());
                if (right_answer == answer2) {
                    result.setText("Success");
                    count.setText((++right_count) + "");


                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Random rand = new Random();

                    int rand_val1 = rand.nextInt(9) + 1;
                    int rand_val2 = rand.nextInt(9) + 1;
                    right_answer = rand_val1 * rand_val2;

                    String val1_s = Integer.toString(rand_val1);
                    String val2_s = Integer.toString(rand_val2);

                    question.setText(val1_s + "*" + val2_s + "=");
                    /* result.setText(" ");*/
                    answer.setText("");

                } else {
                    result.setText("false");
                }
            } else if (str.equals("취소")) {
                answer.setText("");
            } else {
                answer.append(str);
            }

        }
        else{
            process.setText("END!");
        }


    }

}