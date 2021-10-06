package com.example.a9_intent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class SecondActivity extends AppCompatActivity {

    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sceond);
        EditText et = (EditText) findViewById(R.id.et);
        Intent intent1 = getIntent();
        String str = intent1.getStringExtra("key_data");

        index = intent1.getIntExtra("index",0);

        et.setText(str);

    }

    public void onClick_ok(View view) {

        Intent intent = new Intent();
        EditText et = (EditText) findViewById(R.id.et);
        intent.putExtra("input",et.getText().toString());
        intent.putExtra("index",index);
        setResult(RESULT_OK, intent);

        finish();
    }

    public void onClick_cancle(View view){
        setResult(RESULT_CANCELED);
        finish();
    }

}