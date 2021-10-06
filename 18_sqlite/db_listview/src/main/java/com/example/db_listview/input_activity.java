package com.example.db_listview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class input_activity extends AppCompatActivity {

    EditText et_capital, et_country;
    int _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);


        et_capital = (EditText) findViewById(R.id.et_capital);
        et_country = (EditText) findViewById(R.id.et_country);

        Intent get_intent = getIntent();

        String country = get_intent.getStringExtra("country");
        String capital = get_intent.getStringExtra("capital");

        // 이거 없어도 됨ㅋ from.지식천사
        _id = get_intent.getIntExtra("id",-1);

        et_country.setText(country);
        et_capital.setText(capital);


    }

    public void on_Click_ok(View view) {

        Intent intent = new Intent();
        String capital = et_capital.getText().toString();
        String country = et_country.getText().toString();

        intent.putExtra("capital",capital);
        intent.putExtra("country", country);

        // 이거 없어도 됨ㅋ from.지식천사
        intent.putExtra("_id",_id);

        setResult(RESULT_OK, intent);
        finish();


    }
}











