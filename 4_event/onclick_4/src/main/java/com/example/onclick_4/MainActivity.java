package com.example.onclick_4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void Btn_Click(View view){
        Toast.makeText(MainActivity.this, "hi", Toast.LENGTH_SHORT).show();
        TextView tv = (TextView) findViewById(R.id.tv);
        tv.setText("After");

    }

}