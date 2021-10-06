package com.example.a17_sdcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    static final String FILENAME = "myFile.txt";

    EditText et_input;
    TextView tv_data;
    static final int REQCODE_PERMISSION_WRITE_EXTERNAL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_input = (EditText) findViewById(R.id.et);
        tv_data = (TextView) findViewById(R.id.tv);
        checkPermssion();


    }

    public void onClickSave(View view) {
        String strData = et_input.getText().toString();
        File sdpath = getExternalFilesDir(null);

        try {
            FileOutputStream fos = new FileOutputStream(sdpath.getAbsolutePath()+ "/" + FILENAME);
            fos.write(strData.getBytes());
            Toast.makeText(this, "파일을 저장했습니다.",Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setVisibilityViews(View.INVISIBLE, View.INVISIBLE);

        Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
    }
    public void onClickRead(View v) {
        int len = 0;
        int result = 0;

        try {
            File sdpath = getExternalFilesDir(null);
            FileInputStream fis = new FileInputStream(sdpath.getAbsolutePath() + "/" + FILENAME);
            len = fis.available();
            if(len > 0) {
                byte buff[] = new byte[len];
                while(result > -1)
                    result = fis.read(buff);

                tv_data.setText(new String(buff));
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setVisibilityViews(View.VISIBLE, View.INVISIBLE);
    }

    public void onClickInput(View v) {
        setVisibilityViews(View.INVISIBLE, View.VISIBLE);
    }

    void setVisibilityViews(int output, int input) {
        tv_data.setVisibility(output);
        et_input.setVisibility(input);
    }

    void checkPermssion() {
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "권한이 없습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        else {
            Toast.makeText(this, "afd", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQCODE_PERMISSION_WRITE_EXTERNAL) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SD카드 쓰기 가능", Toast.LENGTH_SHORT).show();
            }
        }
    }
}











