package com.moble.mbti;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Home extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //lcs
    View v;
    TextView mbti_textview;
    ImageView mbti_imageview;



    private ArrayList<String> destNick;
    private ArrayList<String> runningCR_Nick;
    private String CR_Id;

    //날짜 가져오기
    long now = System.currentTimeMillis();
    Date mDate=new Date(now);
    SimpleDateFormat simpleDate = new SimpleDateFormat("dd");
    String getTime = simpleDate.format(mDate);

    int int_getTime = Integer.parseInt(getTime);



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.activity_home,container,false);


        mbti_textview = (TextView)v.findViewById(R.id.mbti_text);
        mbti_imageview=(ImageView)v.findViewById(R.id.mbti_image);

        DocumentReference mbti = db.collection("MbtiInfo").document("TODAY"); // Firebase에 저장되어있는 mbti 궁합 찾아가기
        mbti.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        int num=1;

                        //16일이 지났을 경우
                        if(int_getTime>16){
                            int_getTime-=16;
                        }

                        switch (int_getTime) {
                            case 1:
                                mbti_imageview.setImageResource(R.drawable.istj);
                                break;
                            case 2:
                                num = 2;
                                mbti_imageview.setImageResource(R.drawable.isfj);
                                break;
                            case 3:
                                num = 3;
                                mbti_imageview.setImageResource(R.drawable.infj);
                                break;
                            case 4:
                                num = 4;
                                mbti_imageview.setImageResource(R.drawable.intj);
                                break;
                            case 5:
                                num = 5;
                                mbti_imageview.setImageResource(R.drawable.istp);
                                break;
                            case 6:
                                num = 6;
                                mbti_imageview.setImageResource(R.drawable.isfp);
                                break;
                            case 7:
                                num = 7;
                                mbti_imageview.setImageResource(R.drawable.infp);
                                break;
                            case 8:
                                num = 8;
                                mbti_imageview.setImageResource(R.drawable.intp);
                                break;
                            case 9:
                                num = 9;
                                mbti_imageview.setImageResource(R.drawable.estp);
                                break;
                            case 10:
                                num = 10;
                                mbti_imageview.setImageResource(R.drawable.enfp);
                                break;
                            case 11:
                                num = 11;
                                mbti_imageview.setImageResource(R.drawable.estj);
                                break;
                            case 12:
                                num = 12;
                                mbti_imageview.setImageResource(R.drawable.esfj);
                                break;
                            case 13:
                                num = 13;
                                mbti_imageview.setImageResource(R.drawable.entp);
                                break;
                            case 14:
                                num = 14;
                                mbti_imageview.setImageResource(R.drawable.enfj);
                                break;
                            case 15:
                                num = 15;
                                mbti_imageview.setImageResource(R.drawable.entj);
                                break;
                            case 16:
                                num = 16;
                                mbti_imageview.setImageResource(R.drawable.esfp);
                                break;

                        }

                        mbti_textview.setText(document.get("data"+num).toString()+"\n"+document.get("data"+num+"_info") + "\n \n 직업 추천\n"+document.get("data"+num+"_job"));
                        Log.i("firebaase", String.valueOf(document.getData()));

                    } else {
                        mbti_textview.setText("실패");
                        Log.i("firebaase", "실패");
                    }
                } else {
                    Log.i("firebaase", String.valueOf(task.getException()));

                }
            }
        });

        return v;

    }

}