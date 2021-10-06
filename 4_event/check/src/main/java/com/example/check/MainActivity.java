package com.example.check;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity  {

    TextView tv ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView)findViewById(R.id.tv);

        Button button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(listener1);

        Button btn_visible = (Button) findViewById(R.id.btn_visible);
        btn_visible.setOnClickListener(listener2);

    }

    private View.OnClickListener listener1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            TextView tv =  (TextView) findViewById(R.id.tv);
            tv.setText("버튼이 눌렸습니다.");
        }
    };

    private View.OnClickListener listener2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            TextView tv =  (TextView) findViewById(R.id.tv);
            if(tv.getVisibility() == View.VISIBLE){
                tv.setVisibility(View.INVISIBLE);
            }
            else
                tv.setVisibility(View.VISIBLE);
        }
    };


    /*@Override
    public void onClick(View view) {
        TextView tv =  (TextView) findViewById(R.id.tv);
        tv.setText("버튼이 눌렸습니다.");
    }*/


}