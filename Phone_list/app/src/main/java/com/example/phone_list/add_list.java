package com.example.phone_list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class add_list extends AppCompatActivity {

    EditText et_name, et_phone;
    Button btn_ok, btn_cancle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list2);

        et_name =(EditText) findViewById(R.id.et_name);
        et_phone =(EditText) findViewById(R.id.et_phone);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_cancle = (Button) findViewById(R.id.btn_cancle);

        btn_ok.setOnClickListener(listener_btn_ok);











    }

    View.OnClickListener listener_btn_ok = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String name = et_name.getText().toString();
            String phone = et_phone.getText().toString();

            String people_value = name+"/"+phone;

            Intent intent = new Intent();
            intent.putExtra("people",people_value);

            setResult(RESULT_OK, intent);
            finish();

        }
    };



}
























