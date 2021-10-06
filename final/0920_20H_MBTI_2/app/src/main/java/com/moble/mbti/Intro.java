package com.moble.mbti;

import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

//앱 시작시 제일 처음 호출되는 Activity
public class Intro extends AppCompatActivity {

    //fire storage 이미지 불러오기
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    StorageReference pathRef;                                                       //fire storage 에 저장되어 있는 사진의 파일 이름
    String tempFilePath;

    FirebaseFirestore db = FirebaseFirestore.getInstance();                         //firebase DB 정보 불러오기
    static final int PROGRESSBAR_START = 1;
    private ProgressBar pb;
    private String uid = null;
    private Intent intent;


    SingletonUserInfo singletonUserInfo = SingletonUserInfo.getInstance();


    //싱글톤 객체

    String str_mbti;
    String str_nickname;
    String str_msg;

    int flag= 0;


    //값 불러오는 progressbar 로 변경
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (msg.what == PROGRESSBAR_START) {

                //intent 불러오기
                if (pb.getProgress() == 0) {
                    intent = new Intent();
                    FirebaseUser userUid = FirebaseAuth.getInstance().getCurrentUser();

//                    //앱을 처음 받은 사람
//                    if (userUid == null) {
//                        Log.i("jiseong", "userUid is null");
////                        Logged_none_init(intent);
//                        flag = 1;
//                    }
//
//                    //로그인 한 이력이 있으면 Singleton 에 값 저장
//                    else if (userUid != null) {
//                        uid = userUid.getUid();
//                        Logged_past_init(uid);
//                    }

                    if (userUid != null) {
                        uid = userUid.getUid();
                        Logged_past_init(uid);
                    }

                }

                else if (pb.getProgress() == 1) {
                    //uid 는 있으나 MemberJoin 을 거치지 않은 유저
                    if(str_mbti == null){
                        Logged_none_init(intent);
                    }
                    else {
                        //자신 이미지 불러오기
                        Log.i("jiseong", "uid : " + uid);
                        pathRef = storageRef.child("images/" + uid + ".jpg");
                        File storage = getCacheDir();                                   //내부 저장소 Cache Dir 의 경로를 불러옴

                        singletonUserInfo.setStorage(storage);
                        String fileName = uid + ".jpg";                                 //내부 저장소에 저장할 이미지 이름을 uid 로 설정
                        File tempFile = new File(storage, fileName);                    //파일의 경로와 이름을 지정해 줌

                        try {
                            tempFile.createNewFile();
                            pathRef.getFile(tempFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Log.i("jiseong", "local file create success");
                                    tempFilePath = tempFile.getAbsolutePath();      //사진이 저장되는 절대경로
                                    Log.i("jiseong", "Intro filePath" + tempFilePath);

                                    // 싱글톤 객체에 정보 저장
                                    singletonUserInfo.setMyNick(str_nickname);
                                    singletonUserInfo.setMyUid(uid);
                                    singletonUserInfo.setMyImgPath(tempFilePath);
                                    singletonUserInfo.setMyRC_Check(str_msg);
                                    singletonUserInfo.setMyMbti(str_mbti);

                                    intent.setClass(Intro.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            });
                        } catch (FileNotFoundException e) {
                            Log.e("jiseong","FileNotFoundException : " + e.getMessage());
                        } catch (IOException e) {
                            Log.e("jiseong","IOException : " + e.getMessage());
                        }
                    }
                }

                if (pb.getProgress() < pb.getMax()) {
                    Log.i("lcs", "getProgress");
                    pb.setProgress(pb.getProgress() + 1);
                    sendEmptyMessageDelayed(PROGRESSBAR_START, 1000);
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        pb = (ProgressBar) findViewById(R.id.progressBar);

        Thread thread = new Thread("thread") {
            @Override
            public void run() {
                handler.sendEmptyMessage(PROGRESSBAR_START);
                super.run();
            }
        };
        thread.start();
        pb.setProgress(0);


    }

    //현재 로그인 되어 있는 uid 의 fire base 에 저장되어 있는 값들을 불러옴
    void Logged_past_init(String uid) {
        Log.i("jiseong", "Logged_past_init start");
        db.collection("InfoTest").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String str_uid = document.get("UID").toString();

                        if (str_uid.equals(uid)) {
                            Log.i("jiseong", "find equal uid");
                            str_mbti = document.get("MBTI").toString();
                            str_nickname = document.get("nickname").toString();
                            str_msg = document.get("msg").toString();
                            Log.i("jiseong", "userUid not null+ " + uid);
                            Log.i("jiseong",str_msg);
                            return;
                        }
                    }
                    Log.i("jiseong", "not find equal uid");
//                    Logged_none_init(intent);

                }
            }
        });
    }

    //uid 가 없을 때 (로그인 이력이 없을 때)
    void Logged_none_init(Intent intent) {
        Log.i("jiseong", "none_init");
        intent.setClass(Intro.this, Login.class);
        startActivity(intent);
        finish();
        return;
    }

}