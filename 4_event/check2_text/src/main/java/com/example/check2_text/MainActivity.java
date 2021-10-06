package com.example.check2_text;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_ok = (Button) findViewById(R.id.ok);
        button_ok.setOnClickListener(listener_ok);
        Button button_can = (Button) findViewById(R.id.cancle);
        button_can.setOnClickListener(listener_can);

    }

    private View.OnClickListener listener_ok = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EditText et = (EditText)findViewById(R.id.name);
            String str = et.getText().toString();

            TextView input = (TextView) findViewById(R.id.input);
            input.append(str+"\n");

            et.setText("");

        }
    };

    private View.OnClickListener listener_can = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            EditText et = (EditText) findViewById(R.id.name);
            et.setText("");
        }
    };
}