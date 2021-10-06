package com.example.worldcup;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class make_8 {

    ArrayList<Integer> index;

    public void make_init(){
        Random random = new Random(); //랜덤 객체 생성(디폴트 시드값 : 현재시간)
        random.setSeed(System.currentTimeMillis()); //시드값 설정을 따로 할수도 있음


        index = new ArrayList<Integer>();

        index.add(R.drawable.char1);
        index.add(R.drawable.char2);
        index.add(R.drawable.char3);
        index.add(R.drawable.char4);
        index.add(R.drawable.char5);
        index.add(R.drawable.char6);
        index.add(R.drawable.char7);
        index.add(R.drawable.char8);
        Log.i("jiseong","크기는 "+index.size());


    }

    public ArrayList<Integer> shuffle(ArrayList<Integer> arrayList) {
        Collections.shuffle(arrayList);



        return arrayList;
    }

}
