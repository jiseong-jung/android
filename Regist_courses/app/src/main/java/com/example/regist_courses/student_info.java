package com.example.regist_courses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class student_info extends AppCompatActivity {

    TextView name, phone, subject, time;
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);

        name = (TextView) findViewById(R.id.s_name);
        phone = (TextView) findViewById(R.id.s_phone);
        subject = (TextView) findViewById(R.id.s_subject);
        time = (TextView) findViewById(R.id.s_time);

        Intent intent = getIntent();

        student = (Student) intent.getSerializableExtra("student_info");

        name.setText(student.getName());
        phone.setText(student.getPhone());
        subject.setText(student.getSubject());
        time.setText(student.getTime());
        String time = student.getTime();
    }



}