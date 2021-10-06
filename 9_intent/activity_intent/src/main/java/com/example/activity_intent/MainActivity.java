package com.example.activity_intent;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickWeb(View view) {
        EditText et = (EditText) findViewById(R.id.et_url);
        String str = et.getText().toString();

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        //intent.setData(Uri.parse("http://www.google.com"));
        intent.setData(Uri.parse("http://"+str));
        startActivity(intent);
    }

    public void onClickWebSearch(View view) {
        EditText et = (EditText) findViewById(R.id.et_search);
        String str = et.getText().toString();

        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH)
                .putExtra(SearchManager.QUERY, str);
        startActivity(intent);


    }

    public void onClickAlarm(View view) {
        EditText et_h = (EditText) findViewById(R.id.et_alarm_h);
        String str_h = et_h.getText().toString();

        EditText et_m = (EditText) findViewById(R.id.et_alarm_m);
        String str_m = et_m.getText().toString();

        Intent intent = new Intent();
        intent.setAction(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_HOUR, Integer.valueOf(str_h));
        intent.putExtra(AlarmClock.EXTRA_MINUTES, Integer.valueOf(str_m));
        startActivity(intent);

    }

    /*public void onClickAlarm(View view) {
        EditText et_h = (EditText) findViewById(R.id.et_alarm_h);
        String str_h = et_h.getText().toString();

        EditText et_m = (EditText) findViewById(R.id.et_alarm_m);
        String str_m = et_m.getText().toString();

        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_HOUR, Integer.valueOf(str_h))
                .putExtra(AlarmClock.EXTRA_MINUTES, Integer.valueOf(str_m));
        startActivity(intent);
    }*/


    public void onClickDial(View view) {
        EditText et = (EditText) findViewById(R.id.et_dial);
        String str = et.getText().toString();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+str));
        startActivity(intent);

    }
}
















