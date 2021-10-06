package com.example.phone_list;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final static int REQUEST_CODE_START_INPUT = 1;
    final static int REQCODE_PERMISSION_CALLPHONE = 1;

    Button btn_add;
    ListView lv;
    int count;

    ArrayList<String> al = new ArrayList<>();               //이름 번호 원본 상태의 값
    ArrayList<String> name = new ArrayList<String>();       //이름만 저장   
    ArrayList<String> phone = new ArrayList<>();            //번호만 저장

    ArrayAdapter<String> aa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.listview);
        btn_add = (Button) findViewById(R.id.btn_add);

        /*al.add("jiseong/01088055636");

        int index;
        index = al.get(0).indexOf("/");
        Log.i("jiseong",index+"번째");

        phone.add(al.get(0).substring(index+1));
        name.add(al.get(0).substring(0,index));


        Log.i("jiseong",name.get(0));
        Log.i("jiseong",phone.get(0));*/

        /* name.add(al.get(0).split("/",0)); */
        //arrayAdapter 설정

        aa = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, name);

        //리스트뷰에 arrayAdapter 연결
        lv.setAdapter(aa);  

        //리스트뷰 아이템 클릭시 발생할 리스너
        lv.setOnItemClickListener(listener_item);
        btn_add.setOnClickListener(listener_btn);


        init();
        

    }

    View.OnClickListener listener_btn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, add_list.class);
            startActivityForResult(intent, REQUEST_CODE_START_INPUT);

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_START_INPUT){
            if(resultCode == RESULT_OK){
                Log.i("jiseong","add_list return intent(data)");
                int index = 0;
                //int count = 0; //위에 테스트용 값이 있어서 1부터 시작한다.
                //size 변경 필요
                
                
                //객체 값을 넘겨 받음
                String st = data.getStringExtra("people");

                al.add(st);

                Log.i("jiseong",st);
                //Log.i("jiseong", "방금 들어온 값"+al.get(count));
                //Log.i("jiseong", "방금 들어온 값"+al.get(count+1));
                index = al.get(count).indexOf("/");     // "/" 이름 번호로 나뉘는 값의 위치

                name.add(al.get(count).substring(0,index));         // "/" 전에 있는 값을 이름 Arraylist에
                phone.add(al.get(count).substring(index+1));        // "/" 뒤로 있는 값을 휴대전화 Arraylist로 저장

                writeUser(st);      // 프리퍼리티에 저장

                aa.notifyDataSetChanged();      //aa의 값이 변경되었을때 변경 내용 반영

                Log.i("jiseong",al.get(count));
                count++;
            }
        }


    }

    AdapterView.OnItemClickListener listener_item = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.CALL_PHONE)) {
                    Toast.makeText(MainActivity.this, "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                    Log.i("jiseong","권한이 없습니다.");
                }else{
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQCODE_PERMISSION_CALLPHONE);
                }

            }
            else{
                String phone_number = phone.get(i);
                if(phone_number.length()>0){
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+phone_number));
                    startActivity(intent);
                }
                else
                    Toast.makeText(MainActivity.this, "아무것도 입력하지 않았습니다.", Toast.LENGTH_SHORT).show();
                Log.i("jiseong","아무것도 입력하지 않았습니다.");
            }


        }
    };

    void writeUser(String name){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(count+"", name);
        editor.commit();
    }

    void init(){
        int index = 0;

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);

        //불러오는 사이즈 크기 확인

        for(int i =0; i<100; i++) {

            String base_data = pref.getString(i+"","no");

            if (base_data.equals("no")) {
                Log.i("jiseong", base_data);
            } else {
                al.add(base_data);

                Log.i("jiseong", base_data);

                index = al.get(count).indexOf("/");
                Log.i("jiseong", "onResume");

                name.add(al.get(count).substring(0,index));
                phone.add(al.get(count).substring(index+1));        // "/" 뒤로 있는 값을 휴대전화 Arraylist로 저장
                aa.notifyDataSetChanged();      //aa의 값이 변경되었을때 변경 내용 반영
                count++;
            }
        }
    }
}
































