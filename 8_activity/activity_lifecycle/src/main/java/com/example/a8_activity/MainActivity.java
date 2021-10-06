package com.example.a8_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    int mCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState !=null){
            mCount = savedInstanceState.getInt("game score");
        }

        Log.i("jiseong","crate");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("game score", mCount);
        Log.i("jiseong","onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        mCount = savedInstanceState.getInt("game score");
        Log.i("jiseong","onRestoreInstanceState");

        super.onRestoreInstanceState(savedInstanceState);
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
}