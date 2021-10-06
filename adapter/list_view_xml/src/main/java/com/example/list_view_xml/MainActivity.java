package com.example.list_view_xml;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    String[] str_items = {"blue","red","green","yellow"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.row, R.id.tv, str_items);
        ListView lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(aa);
    }
}