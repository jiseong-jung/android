package com.example.preferences_name;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button button;

    TextView tv;

    @Override
    protected void onResume() {
        super.onResume();
        readUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(btn_listener);


    }

    View.OnClickListener btn_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

            dialog.setTitle("이름 변경");
            dialog.setMessage("사용자의 이름을 입력합니다.");

            EditText et = new EditText(MainActivity.this);
            dialog.setView(et);

            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String str = et.getText().toString();
                    writeUser(str);
                    readUser();
                }
            });

            dialog.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //writeUser("unknown");
                    readUser();
                }
            });


            dialog.show();
        }
    };


    void readUser(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String name = pref.getString("user_name","unknown");
        tv.setText(name);
    }

    void writeUser(String name){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("user_name", name);
        editor.commit();
    }



}
















