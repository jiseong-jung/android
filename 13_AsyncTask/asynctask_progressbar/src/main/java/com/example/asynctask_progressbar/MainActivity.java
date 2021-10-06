package com.example.asynctask_progressbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ProgressBar pb;
    Button btn_start;
    long count;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start = (Button) findViewById(R.id.btn_start);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        tv = (TextView)findViewById(R.id.tv);

        btn_start.setOnClickListener(listener_btn);

    }

    View.OnClickListener listener_btn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Async_make my_async = new Async_make();
            my_async.execute(50);
        }
    };


    class Async_make extends AsyncTask<Integer, Integer, Long>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            count=0;
        }

        @Override
        protected Long doInBackground(Integer... params) {
            Log.i("jiseong","check");
            while(isCancelled() == false){
                if(count<pb.getMax()) {
                    publishProgress((int) count++);
                }
                else
                    break;
            }
            return count;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i("jiseong","update");
            super.onProgressUpdate(values);
            pb.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(Long aLong) {
            tv.setText("END" + aLong);
        }
    }


}






















