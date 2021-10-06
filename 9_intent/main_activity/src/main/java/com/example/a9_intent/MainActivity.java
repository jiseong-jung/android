package com.example.a9_intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final static int REQUSET_CODE_START_INPUT = 1;
    ArrayAdapter<String> aa;

    String[] items = {"서울", "부산", "대전", "천안"};
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lv = (ListView) findViewById(R.id.lv);
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, items);
        lv.setAdapter(aa);
        lv.setOnItemClickListener(listener);

    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String item_value = items[i];

            Log.i("jiseong", "check");

            Intent intent = new Intent();
            int index = i;
            intent.putExtra("key_data", item_value);
            intent.putExtra("index",i);

            intent.setClass(MainActivity.this,SecondActivity.class);
            startActivityForResult(intent, REQUSET_CODE_START_INPUT);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUSET_CODE_START_INPUT){
            if(resultCode == RESULT_OK){
                /*TextView tv_result = (TextView) findViewById(R.id.tv_result);
                tv_result.setText(data.getCharSequenceExtra("input") +"을 입력받았습니다");*/
                String str = data.getStringExtra("input");
                int index = data.getIntExtra("index",0);
                items[index] = str;
                aa.notifyDataSetChanged();

            }
        }

    }

    /*public void onClickExplicit(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,SecondActivity.class);
        intent.putExtra("key_data", "value_check");
        startActivity(intent);

    }*/


}