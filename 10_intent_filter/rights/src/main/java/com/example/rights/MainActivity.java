package com.example.rights;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText et_phone;
    Button btn_call;
    static final int REQCODE_PERMISSION_CALLPHONE =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_phone = (EditText) findViewById(R.id.et_phone);
        btn_call = (Button) findViewById(R.id.btn_call);

        btn_call.setOnClickListener(listener);

    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.CALL_PHONE)) {
                    Toast.makeText(MainActivity.this, "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                    Log.i("jiseong","권한이 없습니다.");
                }else{
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQCODE_PERMISSION_CALLPHONE);
                }

            }
            else{
                String str = et_phone.getText().toString();
                if(str.length()>0){
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+str));
                    startActivity(intent);
                }
                else
                    Toast.makeText(MainActivity.this, "아무것도 입력하지 않았습니다.", Toast.LENGTH_SHORT).show();
                    Log.i("jiseong","아무것도 입력하지 않았습니다.");
            }

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if(requestCode == REQCODE_PERMISSION_CALLPHONE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                btn_call.setEnabled(true);
                Toast.makeText(MainActivity.this, "권한을 얻었습니다.\n 버튼을 다시 눌러 통화를 시도하시오", Toast.LENGTH_SHORT).show();
                Log.i("jiseong","권한 get");
            }
        }
        
    }
}