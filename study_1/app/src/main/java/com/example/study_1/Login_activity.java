package com.example.study_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class Login_activity extends AppCompatActivity {

    TextView tv_find_id, tv_find_pw, tv_join;
    EditText et_id, et_pw;
    Button btn_login;
    CheckBox cb_keep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tv_join = (TextView) findViewById(R.id.tv_join);
        tv_find_id = (TextView)findViewById(R.id.tv_find_id);
        tv_find_pw = (TextView)findViewById(R.id.tv_find_pw);

        et_id = (EditText)findViewById(R.id.et_id);
        et_pw = (EditText)findViewById(R.id.et_pw);
        btn_login = (Button)findViewById(R.id.btn_login);
        cb_keep = (CheckBox)findViewById(R.id.checkbox);

        tv_join.setOnClickListener(listener_join);
        tv_find_id.setOnClickListener(listener_id);
        tv_find_pw.setOnClickListener(listener_pw);






    }

    //for find id
    View.OnClickListener listener_id = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    //for find pw
    View.OnClickListener listener_pw = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    //for member join
    View.OnClickListener listener_join = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(Login_activity.this, member_join.class);
            startActivity(intent);

        }
    };


}















