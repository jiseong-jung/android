package com.example.a16_file_input_output;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    Button btn1, btn2;
    EditText et;
    TextView tv;

    String str = "file test";
    String File_Name = "file.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1= (Button) findViewById(R.id.button);
        btn2= (Button) findViewById(R.id.button2);
        tv = (TextView)findViewById(R.id.tv);
        et = (EditText)findViewById(R.id.et);

        btn1.setOnClickListener(listener1);
        btn2.setOnClickListener(listener2);

    }

    //저장하기
    View.OnClickListener listener1 = new View.OnClickListener() {

        FileOutputStream fos = null;

        @Override
        public void onClick(View view) {
            try {
                String et_input = et.getText().toString();
                fos = openFileOutput(File_Name,MODE_PRIVATE);
                fos.write(et_input.getBytes(StandardCharsets.UTF_8));
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    //불러오기
    View.OnClickListener listener2 = new View.OnClickListener() {

        int len= 0;
        FileInputStream fis = null;

        @Override
        public void onClick(View view) {


            int result = 0;

            try {
                fis = openFileInput(File_Name);
                len = fis.available();
                
                if(len > 0) {
                    byte[] buff = new byte[len];

                    while (result > -1) {       //문자의 끝은 -1을 반환
                        result = fis.read(buff);
                    }

                    tv.setText(new String(buff));
                    fis.close();
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    };



}






















