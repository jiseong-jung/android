package com.moble.mbti;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class
ChatActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    // RecyclerView를 사용하면서 상하로 리스트를 보여줄 것인지, 좌우로 리스트를 보여줄것인지
    // Grid형식으로 리스트를 보여줄 것인지에 대한 타입을 지정해준다
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChatData> chatList;

    //appbar 관련된 선언
    private DrawerLayout drawer_chat;      //chat.xml 의 가장 큰 layout
    private ImageView imageView_overflow;
    private NavigationView navigationView;
    View nav_header_view;

    private EditText EditText_chat;
    private Button Button_send;
    private Button Button_chatExit;
    private DatabaseReference myRef, myRef_del, myRef_update;
    private FirebaseDatabase database;
    private TextView tv_mbti;
    private TextView tv_nickname;
    private CircleImageView circleImageView;

    // 싱글톤 객체 정보를 저장할 변수
    private String MyUid;
    private String MyMbti;
    private String MyImgPath;
    private String MyNick;
    private String CR_Id;     // ChatRoom_Id
    private String nowDate;

    private ArrayList<DestUserInfo> destList;
    private String destNick;

    // chatAdapter 액티비티에서 필요하기 때문에 static 선언
    static String destUid;

    TextView appbar_title;
    View toolbar_chat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();


    }

    void init() {
        get_SingletonUserInfo();
        set_nowDate();
        setElements();
        listener_link();
        set_Adapter();

    }

    void setElements() {
        //appbar 관련
        drawer_chat = findViewById(R.id.drawer_chat);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        imageView_overflow = (ImageView) findViewById(R.id.imageview_overflow);
        navigationView = findViewById(R.id.navigation);

        Button_send = findViewById(R.id.Button_send);
        Button_chatExit = findViewById(R.id.btn_chatExit);
        EditText_chat = findViewById(R.id.EditText_chat);
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);                    //동일한 크기의 list(item) 유지
                                                                //ture 를 함으로서 변경이 허락되지 않음을 명시

        nav_header_view = navigationView.getHeaderView(0);

        tv_mbti = (TextView) nav_header_view.findViewById(R.id.tv_mbti_output);
        tv_nickname = (TextView) nav_header_view.findViewById(R.id.tv_nickname_output);
        circleImageView = (CircleImageView) nav_header_view.findViewById(R.id.img_nav);

        toolbar_chat = (View) findViewById(R.id.included_layout);
        appbar_title = (TextView) toolbar_chat.findViewById(R.id.tv_logo);

        appbar_title.setText(destNick);

        // 사이드바 정보 변경
        tv_nickname.setText(MyNick);
        tv_mbti.setText(MyMbti);
        circleImageView.setImageURI(Uri.parse(MyImgPath));
    }

    void set_nowDate() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm"); //원하는 데이터 포맷 지정
        nowDate = simpleDateFormat.format(date); //지정한 포맷으로 변환
    }

    void listener_link() {

        imageView_overflow.setOnClickListener(listener_overflow);
        Button_send.setOnClickListener(listener_btn_send);
        Button_chatExit.setOnClickListener(listener_btn_chatExit);

    }

    void set_Adapter() {
        chatList = new ArrayList<>();

        // LinearLayoutManager : 수평,수직으로 배치시켜주는 레이아웃 매니저.
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ChatAdapter(chatList, MyUid);
        mRecyclerView.setAdapter(mAdapter);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("ChatData").child(CR_Id);

        // 정보가 바뀌었을때 호출, 경로의 전체 내용에 대한 변경 사항을 읽고 수신 대기한다.
        myRef.addChildEventListener(new ChildEventListener() {

            // data가 추가되었을때 호출
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatData chat = dataSnapshot.getValue(ChatData.class);
                ((ChatAdapter) mAdapter).addChat(chat);
                mRecyclerView.scrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // 싱글톤에서 필요한 정보 읽기 및 저장
    void get_SingletonUserInfo() {

        SingletonUserInfo singletonUserInfo = SingletonUserInfo.getInstance();

        MyMbti = singletonUserInfo.getMyMbti();
        MyImgPath = singletonUserInfo.getMyImgPath();
        MyUid = singletonUserInfo.getMyUid();
        MyNick = singletonUserInfo.getMyNick();

        CR_Id = getIntent().getStringExtra("CR_Id");


        // 현재 CR_Id(채팅방 ID)에서 destUid(상대방Uid) 검출
        destUid = CR_Id.replace(MyUid, "").replace(",", "");

        destList = singletonUserInfo.getDestList();

        for (int i = 0; i < destList.size(); i++) {
            if (destList.get(i).getDestUid().equals(destUid)) {
                destNick = destList.get(i).getDestNick();
            }
        }
    }

    // 채팅창 사이드 바 리스너
    View.OnClickListener listener_overflow = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            drawer_chat.openDrawer(Gravity.RIGHT);
        }
    };

    // 메세지 보내기 버튼 리스너
    View.OnClickListener listener_btn_send = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String msg = EditText_chat.getText().toString();

            if (msg != null) {
                ChatData chat = new ChatData(msg, MyUid, nowDate);
                myRef.push().setValue(chat);  // db에 값을 넣어줌
                EditText_chat.setText("");
            }

            // 채팅방에 속해있는 유저의 message 필드에 가장 최근 메세지를 update 해주기위한 코드
            database = FirebaseDatabase.getInstance();
            myRef_update = database.getReference("chatRoomData");
            myRef_update.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {

                        for (DataSnapshot snapshot : task.getResult().child(MyUid).getChildren()) {
                            if (snapshot.child("uid").getValue().equals(destUid)) {

                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("message", msg);

                                myRef_update.child(MyUid).child(snapshot.getKey()).updateChildren(map);
                            }
                        }

                        for (DataSnapshot snapshot : task.getResult().child(destUid).getChildren()) {
                            if (snapshot.child("uid").getValue().equals(MyUid)) {

                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("message", msg);

                                myRef_update.child(destUid).child(snapshot.getKey()).updateChildren(map);
                            }
                        }

                    } else {
                        Log.d("lcs", "get failed with ", task.getException());

                    }
                }
            });

        }
    };

    // 채팅방 나가기 버튼 리스너
    View.OnClickListener listener_btn_chatExit = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            // 해당 채팅 데이터 삭제
            database = FirebaseDatabase.getInstance();
            myRef_del = database.getReference("chatRoomData");
            myRef_del.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {

                        // 1. MyUid 노드안에 destUid이 포함된 자식노드를 삭제
                        for (DataSnapshot mySnapshot : task.getResult().child(MyUid).getChildren()) {

                            if (mySnapshot.child("uid").getValue().equals(destUid)) {
                                Log.i("lcs", "mySnapshot : " + mySnapshot.getKey());
                                myRef_del.child(MyUid).child(mySnapshot.getKey()).removeValue();
                            }
                        }

                        int flag = 0; // if문 내용이 실행 되었는지를 확인하는 변수

                        // 2. destUid 노드안에도 MyUid이 포함된 자식노드가 존재 하지 않는다면
                        //    채팅방을 둘 다 나간것임으로 chatRoomData의 CR_Id에 맞는 채팅 데이터도 지워버림
                        for (DataSnapshot destSnapshot : task.getResult().child(destUid).getChildren()) {
                            if (destSnapshot.child("uid").getValue().equals(MyUid)) {
                                Log.i("lcs", "destSnapshot : " + destSnapshot.getKey());
                                flag = 0;
                                break;
                            } else {
                                flag = 1;
                            }
                        }

                        if (flag == 1) {
                            myRef.removeValue();
                        }

                    } else {
                        Log.d("lcs", "get failed with ", task.getException());
                    }

                    Intent intent = new Intent();
                    intent.setClass(ChatActivity.this, MainActivity.class)
                            .putExtra("ChatBackPressed", 1);

                    startActivity(intent);
                    finish();
                }
            });
        }

    };

    // 뒤로가기를 누르면 chattingRoomList 프레그먼트로 이동하기 위한 설정
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ChatActivity.this, MainActivity.class)
                .putExtra("ChatBackPressed", 1);
        startActivity(intent);
        finish();

    }
}


