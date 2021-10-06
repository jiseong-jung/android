package com.example.a13_asynctask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btn_update;
    TextView tv;
    TextView tv1;
    long value = 0;
    long check = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_update = (Button)findViewById(R.id.btn_update);
        tv =(TextView)findViewById(R.id.tv);
        tv1 = (TextView)findViewById(R.id.tv1);

        btn_update.setOnClickListener(listener_btn);
    }

    View.OnClickListener listener_btn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AccumulateTask myTask = new AccumulateTask();
            myTask.execute(50);
        }
    };


    class AccumulateTask extends AsyncTask<Integer, String, Long>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            value=0;
        }

        @Override
        protected Long doInBackground(Integer... params) {
            while(isCancelled() == false){
                value++;
                check++;
                if (value < params[0]){
                    publishProgress(value +"", check+"");
                }else {
                    break;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return value;
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            tv.setText(progress[0]);
            tv1.setText(progress[1]);

        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            tv.setText("sucess" + result);
            Log.i("jiseong", "반환값" + result + "입니다.");
        }
    }

}