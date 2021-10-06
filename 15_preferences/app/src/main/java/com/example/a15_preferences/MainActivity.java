package com.example.a15_preferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {

    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkBox = (CheckBox) findViewById(R.id.checkbox);

        checkBox.setOnCheckedChangeListener(listener_checkbox);

    }

    //? View랑 다른? 상속받은?
    CompoundButton.OnCheckedChangeListener listener_checkbox = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            SharedPreferences pref = getSharedPreferences(
                    "sharedpreferences", MODE_PRIVATE);     //매개변수1 xml 파일 이름
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("sound", b);
            editor.commit();
        }
    };

    //pref 호출시 실행될 콜백 메서드
    SharedPreferences.OnSharedPreferenceChangeListener listener_pref = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            boolean bSound = sharedPreferences.getBoolean(key, false);
            String str = (bSound)? "설정" : "해제";
            Log.i("jiseong", "sound가 " + str +" 되었습니다." );
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences("sharedpreferences", MODE_PRIVATE);
        boolean bSound = pref.getBoolean("sound",false);

        CheckBox cb = (CheckBox) findViewById(R.id.checkbox);
        cb.setChecked(bSound);

        pref.registerOnSharedPreferenceChangeListener(listener_pref);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences pref = getSharedPreferences("sharedpreferences", MODE_PRIVATE);
        pref.unregisterOnSharedPreferenceChangeListener(listener_pref);
    }
}












