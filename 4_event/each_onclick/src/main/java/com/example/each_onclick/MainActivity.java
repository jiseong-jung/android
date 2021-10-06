package com.example.each_onclick;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(listener1);
        
        Button btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {    //2번째 방식
            @Override
            public void onClick(View view) {
                TextView tv = (TextView) findViewById(R.id.tv);
                tv.setText("두번째 버튼 클릭");
            }
        });

        Button btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(this);


    }
    
    //1번째 방식
    View.OnClickListener listener1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView tv = (TextView) findViewById(R.id.tv);
            tv.setText("첫번째 버튼 클릭");
        }
    };

    //3번째 방식
    @Override
    public void onClick(View view) {
        TextView tv = (TextView) findViewById(R.id.tv);
        tv.setText("세번째 버튼 클릭");
    }

    public void xml_Onclick(View v){
        TextView tv = (TextView) findViewById(R.id.tv);
        tv.setText("네번째 버튼 클릭");
    }


}