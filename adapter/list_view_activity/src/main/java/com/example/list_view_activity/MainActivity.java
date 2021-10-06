package com.example.list_view_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

    String str_items[] = {"red", "blue", "green", "yellow"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayAdapter<String> aa;
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, str_items);

        setListAdapter(aa);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(this, position + 1 + "번째 아이템이 클릭됨", Toast.LENGTH_SHORT).show();
        super.onListItemClick(l, v, position, id);
    }

    //    @Override
//    protected void OnListItemClick(ListView i, View v, int position, long id){
//        Toast.makeText(this, position+1+"번째 아이템이 클릭됨", Toast.LENGTH_SHORT).show();
//        super.onListItemClick(i,v,position,id);
//    }

}