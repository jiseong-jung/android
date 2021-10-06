package com.example.list_view_getview;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    String str_items[] = {"red", "blue", "green", "yellow",
            "red", "blue", "green", "yellow",
            "red", "blue", "green", "yellow",
            "red", "blue", "green", "yellow",
            "red", "blue", "green", "yellow",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyAdapter aa = new MyAdapter(MainActivity.this, R.layout.row, R.id.tv, str_items);
        ListView lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(aa);
    }
}



class MyAdapter extends ArrayAdapter<String> {

        LayoutInflater inflater;
        String[] mltems;

        public MyAdapter(Context context, int resource, int textViewResourceId , String[] objects) {

            super(context, resource, textViewResourceId, objects);
            mltems = objects;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;

            if (view == null) {
                view = inflater.inflate(R.layout.row, null);
            }

            ImageView iv = (ImageView)view.findViewById(R.id.image);

            int nResId = R.drawable.heart;

            if (position % 2 == 0)
                nResId = R.drawable.star;

            iv.setImageResource(nResId);

            TextView tv = (TextView) view.findViewById(R.id.tv);
            tv.setText(mltems[position]);

            return view;

        }

    }



