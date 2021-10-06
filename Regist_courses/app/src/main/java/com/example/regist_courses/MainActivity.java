package com.example.regist_courses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.sql.Struct;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final static int REQUEST_CODE_START_INPUT = 1;
    int i =0;
    row_Adapter aa;
    ListView lv;


    ArrayList<Student> al;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        al = new ArrayList();
        aa = new row_Adapter(this, R.layout.row, R.id.tv, al);
        lv = (ListView) findViewById(R.id.list_view);
        lv.setAdapter(aa);

        Button request_btn = (Button) findViewById(R.id.request);
        request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, courses.class);
                startActivityForResult(intent, REQUEST_CODE_START_INPUT);
            }
        });

        lv.setOnItemClickListener(info_listener);
    }
    
    @Override       //courses에서의 콜백 함수
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_START_INPUT){
            if(resultCode == RESULT_OK){
                TextView tv = (TextView)findViewById(R.id.count);
                //객체 값을 넘겨 받음
                Student st = (Student) data.getSerializableExtra("value");
                tv.setText((i+1)+"명");
                al.add(st);
                aa.notifyDataSetChanged();
                i++;
            }
        }

    }

    AdapterView.OnItemClickListener info_listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent();
            intent.putExtra("student_info",al.get(i));
            intent.setClass(MainActivity.this, student_info.class);
            startActivity(intent);

        }
    };
    
}

class row_Adapter extends ArrayAdapter<String>{

    LayoutInflater inflater;
    ArrayList<Student> st;
    int i=0;

    public row_Adapter(@NonNull Context context, int resource, int ResourceID, ArrayList students) {
        super(context, resource, ResourceID, students);
        inflater = LayoutInflater.from(context);
        st = students;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.row, null);
        }

        TextView tv = (TextView)view.findViewById(R.id.tv);
        ImageView img = (ImageView) view.findViewById(R.id.image);

        String name = st.get(position).getName();
        String subject = st.get(position).getSubject();

        if(subject.equals("java")){
            img.setImageResource(R.drawable.java);
        }
        else if (subject.equals("python")){
            img.setImageResource(R.drawable.python);
        }
        else if (subject.equals("c")){
            img.setImageResource(R.drawable.c);
        }
        else if (subject.equals("html")){
            img.setImageResource(R.drawable.html);
        }


        tv.setText(name);

        return view;
    }



}









