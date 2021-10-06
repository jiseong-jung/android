package com.example.list_view_dynamic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> al;
    ArrayAdapter aa;

    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        al = new ArrayList<>();
        aa = new ArrayAdapter(this, android.R.layout.simple_list_item_1, al);
        ListView lv = (ListView)findViewById(R.id.list);
        lv.setAdapter(aa);

        et = (EditText) findViewById(R.id.input_name);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, ((TextView)view).getText(), Toast.LENGTH_SHORT).show();
            }
        });


    }





    public void onClickAdd(View v){
        String str = et.getText().toString();
        et.setText("");
        al.add(str);
        aa.notifyDataSetChanged();
        //Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
    }

}