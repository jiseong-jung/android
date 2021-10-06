package com.example.adapter;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String[] str_item = {"blue","red","green","yellow"};
    int[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW};

    int[] imgs = {R.drawable.jax,R.drawable.moregana,R.drawable.pangtae};



    RelativeLayout rl;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //어뎁터 생성
        ArrayAdapter<String> aa;
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, str_item);

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(aa);

        listView.setOnItemClickListener(listener);

        rl = findViewById(R.id.liner);
        imageView = findViewById(R.id.img);
    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Toast.makeText(MainActivity.this, position+1 +"번째" +" itemClick", Toast.LENGTH_SHORT).show();
            rl.setBackgroundColor(colors[position]);

            imageView.setBackgroundResource(imgs[position]);


        }
    };

}