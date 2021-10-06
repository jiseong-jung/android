package com.example.activity_restart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("jiseong","create");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("jiseong","start");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("jiseong","restart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("jiseong","resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("jiseong","pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("jiseong","stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("jiseong","destroy");
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            Log.i("jiseong","Landscape");
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Log.i("jiseong","Portrait");
        }
    }
}