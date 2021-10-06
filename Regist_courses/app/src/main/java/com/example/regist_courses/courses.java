package com.example.regist_courses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class courses extends AppCompatActivity {

    String agree;
    EditText et_phone, et_name;

    Student st = new Student();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);

        RadioGroup rg_check = (RadioGroup) findViewById(R.id.check);
        RadioGroup rg_subject = (RadioGroup)findViewById(R.id.subject);
        RadioGroup rg_time = (RadioGroup)findViewById(R.id.time);

        rg_check.setOnCheckedChangeListener(listener1);
        rg_subject.setOnCheckedChangeListener(listener2);
        rg_time.setOnCheckedChangeListener(listener3);

    }

    RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            RelativeLayout rl = (RelativeLayout)findViewById(R.id.rl_subject);
            if(i == R.id.ch_01){
                Toast.makeText(courses.this, "확인", Toast.LENGTH_SHORT).show();
                rl.setVisibility(View.VISIBLE);
            }
            else if(i == R.id.ch_02){
                Toast.makeText(courses.this, "동의를 해야 신청 가능합니다.", Toast.LENGTH_SHORT).show();
                rl.setVisibility(View.INVISIBLE);
            }

        }
    };

    RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if(i == R.id.java){
                st.setSubject("java");
            }
            else if(i == R.id.python){
                st.setSubject("python");
            }

            else if(i == R.id.c){
                st.setSubject("c");
            }

            else if(i == R.id.html){
                st.setSubject("html");
            }
        }
    };

    RadioGroup.OnCheckedChangeListener listener3 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if(i == R.id.day){
                st.setTime("day");
            }
            else if(i == R.id.night){
                st.setTime("night");
            }
        }
    };


    public void on_Click_submit(View view) {
        Intent intent = new Intent();

        String name = et_name.getText().toString();
        String phone = et_phone.getText().toString();
        st.setName(name);
        st.setPhone(phone);
        intent.putExtra("value",st);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void on_Click_cancle(View view) {
        finish();
    }


}

