package com.moble.mbti;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    SingletonUserInfo singletonUserInfo = SingletonUserInfo.getInstance();
    String MyNick, MyUid, MyMbti, MyImgPath;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    //firestorage 이미지 불러오기
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();       //firestorage
    StorageReference pathRef;

    private String CR_Id;
    private String destMbti;  // 다이얼로그 view에서 선택한 매칭하고 싶은 상대의 mbti 정보

    private ArrayList<String> destUid_Nick;

    private ArrayList<String> runningUid;

    String tempFilePath;
    Button btn_home, btn_chat, btn_compare, btn_match;      //하단 선택 메뉴

    private ImageView ivMenu;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private TextView tv_mbti;
    private TextView tv_nickname;
    private DrawerLayout drawer;

    private TextView tv_MyMbtiInfo;
    private TextView tv_MyMbtiInfotitle;

    CircleImageView view1;

    TextView appbar_title;

    Button btn_logout;


    View nav_header_view;           //navigation 안에 있는 값들 사용시 header.xml 참조하여 사용
    View toolbar_chat;              //toolbar 안에 있는 값들 사용 시 toolbar_main.xml 참조하여 사용

    //0919_lcs
    ArrayList<DestUserInfo> destList;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("jiseong", "mainActivity start");

        init();
    }


    void init() {
        get_SingletonUserInfo();
        setElements();
        listener_link();
        setBtn();

        if (getIntent().getIntExtra("ChatBackPressed", -1) == 1) {
            chatRoomListCall();
        } else {
            homeCall();
        }
    }

    void setElements() {

        drawerLayout = findViewById(R.id.drawer);           //메인(drawer)
        ivMenu = findViewById(R.id.iv_menu);
        toolbar = findViewById(R.id.toolbar);               //툴바 생성(drawer 밑)
        setSupportActionBar(toolbar);
        btn_logout = findViewById(R.id.btn_logout1);
        toolbar_chat = (View) findViewById(R.id.included_layout);
        drawer = findViewById(R.id.drawer);


        navigationView = findViewById(R.id.navigation);     //네비게이션
        navigationView.setNavigationItemSelectedListener(listener_itemselected);
        nav_header_view = navigationView.getHeaderView(0);

        //둥근 이미지
        view1 = (CircleImageView) nav_header_view.findViewById(R.id.img_nav);

        tv_mbti = (TextView) nav_header_view.findViewById(R.id.tv_mbti_output);
        tv_nickname = (TextView) nav_header_view.findViewById(R.id.tv_nickname_output);
        appbar_title = (TextView) toolbar_chat.findViewById(R.id.tv_logo);

        tv_MyMbtiInfotitle = (TextView) findViewById(R.id.tv_MyMbtiInfoTitle);
        tv_MyMbtiInfo = (TextView) findViewById(R.id.tv_MyMbtiInfoData);

        view1.setImageURI(Uri.parse(MyImgPath));
    }

    void listener_link() {
        ivMenu.setOnClickListener(listener_drawer);         //햄버거 버튼(사이드 바) 클릭시
        btn_logout.setOnClickListener(listener_btn_logout);
    }


    NavigationView.OnNavigationItemSelectedListener listener_itemselected = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();

            //NavigationView 마이페이지 클릭
            if (id == R.id.menu_item1) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MyPage.class);

                startActivity(intent);
                finish();

            }
            //NavigationView 내 MBTI 정보 보기 클릭
            else if (id == R.id.menu_item2) {
                firestore.collection("MbtiInfo").document(MyMbti).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                tv_MyMbtiInfotitle.setText(task.getResult().get("title").toString());
                                tv_MyMbtiInfotitle.setVisibility(View.VISIBLE);

                                tv_MyMbtiInfo.setText(task.getResult().get("data").toString());
                                tv_MyMbtiInfo.setVisibility(View.VISIBLE);
                            } else {
                                Log.i("lcs", "No Data");
                            }
                        } else {
                            Log.d("lcs", "get failed with ", task.getException());
                        }
                    }
                });
            }

            return true;
        }
    };

    //햄버거 버튼(사이드 바) 클릭시
    View.OnClickListener listener_drawer = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            drawerLayout.openDrawer(Gravity.RIGHT);
        }
    };

    public void setBtn() {
        btn_home = (Button) findViewById(R.id.btn_home);
        btn_chat = (Button) findViewById(R.id.btn_chat);
        btn_compare = (Button) findViewById(R.id.btn_compare);
        btn_match = (Button) findViewById(R.id.btn_matching);

        //홈 프래그먼트
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Home fragment1 = new Home();
                appbar_title.setText("HOME");
                transaction.replace(R.id.frame, fragment1);
                transaction.commit();
            }
        });

        //채팅방 프래그먼트
        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                ChattingRoomList fragment2 = new ChattingRoomList();
                appbar_title.setText("Chatting");
                transaction.replace(R.id.frame, fragment2);
                transaction.commit();
            }
        });

        //비교 프래그먼트
        btn_compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Matching fragment3 = new Matching();
                appbar_title.setText("Compare");
                transaction.replace(R.id.frame, fragment3);
                transaction.commit();
            }
        });

        //매칭 버튼
        btn_match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matchDialog();
            }
        });
    }

    //매칭 버튼 클릭 시 띄울 다이얼로그 창
    public void matchDialog() {
        AlertDialog.Builder dialMatch = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.match_dialog, null);


        dialMatch.setTitle("랜덤 매칭");
        dialMatch.setMessage("나의 MBTI\n " + MyMbti);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                destMbti = spinner.getSelectedItem().toString();    //비교할 상대방 MBTI
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.spnnier_mbti, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        dialMatch.setPositiveButton("매칭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setDestNick();
            }
        });

        dialMatch.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialMatch.setView(view);
        AlertDialog alertDialog = dialMatch.create();
        alertDialog.show();

        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT); //다이얼로그 창 크기 조절
    }


    //사이드바 열려있을때 뒤로가기를 누를때 앱 종료가 아닌 사이드바가 닫힘
    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            exit();
        }
    }

    View.OnClickListener listener_btn_logout = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("jiseong", "MainActivity 에서 Logout");
            FirebaseAuth.getInstance().signOut();       //구글 계정 로그아웃

            Intent intent = new Intent();
            intent.setClass(MainActivity.this, Login.class);
            startActivity(intent);

            // 저장했던 상대방 정보 초기화
            destList.clear();

        }
    };


    //켰을 때 fragment home_Activity 가 되게 설정
    void homeCall() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Home fragment1 = new Home();
        transaction.replace(R.id.frame, fragment1);
        transaction.commit();
    }

    // 채팅방에서 뒤로가기 누를 시 ChatiingRoomList 띄우기
    void chatRoomListCall() {

        appbar_title.setText("Chatting");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ChattingRoomList fragment2 = new ChattingRoomList();
        transaction.replace(R.id.frame, fragment2);
        transaction.commit();
    }

    // 현재 액티비티에 필요한 현재 사용자 정보를 싱글톤 객체에서 읽기 및 저장
    void get_SingletonUserInfo() {
        MyNick = singletonUserInfo.getMyNick();
        MyUid = singletonUserInfo.getMyUid();
        MyMbti = singletonUserInfo.getMyMbti();
        MyImgPath = singletonUserInfo.getMyImgPath();

        destList = singletonUserInfo.getDestList();

        Log.i("jiseong", "main + " + MyImgPath);

    }

    // setDestNick()에서 찾은 적절한 상대방정보로 채팅방 정보 생성 및 chatActicity 로 넘어가기
    void setParticipant() {         //참가자

        // realtimebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chatRoomData");

        //선택한 MBTI의 유저가 더 없을 때 (이미 채팅방에 연결된 유저 제외)
        if (destUid_Nick.size() == 0) {
            Toast.makeText(MainActivity.this, "연결할 새로운 친구가 더 이상 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        Collections.shuffle(destUid_Nick);

        // '_' 를 기준으로 Uid와 Nickname 분별 (채팅방 분별이 uid_nickname 형식이라)
        String destUid = destUid_Nick.get(0).substring(0, destUid_Nick.get(0).indexOf("_"));
        String destNick = destUid_Nick.get(0).substring(destUid_Nick.get(0).indexOf("_") + 1);

        Log.i("lcs", "destUid : " + destUid);
        Log.i("lcs", "destNick : " + destNick);

        CR_Id = MyUid + "," + destUid;

        ChatRoom cr = new ChatRoom(destUid, destNick, "");
        myRef.child(MyUid).push().setValue(cr);

        cr = new ChatRoom(MyUid, MyNick, "");
        myRef.child(destUid).push().setValue(cr);

        database.getReference("ChatData").child(CR_Id).setValue("");   // 실시간 데이터 베이스에 채팅방 생성

        pathRef = storageRef.child("images/" + destUid + ".jpg");
        Log.i("lcs", "pathRef : " + pathRef);
        File storage = getCacheDir();
        String fileName = destUid + ".jpg";
        Log.i("lcs", "fileName : " + fileName);
        File tempFile = new File(storage, fileName);

        try {
            tempFile.createNewFile();
            pathRef.getFile(tempFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.i("jiseong", "local file create success");
                    tempFilePath = tempFile.getAbsolutePath();      //사진이 저장되는 절대경로
                    Log.i("jiseong", "Intro filePath" + tempFilePath);

                    DestUserInfo destUserInfo = new DestUserInfo(destNick, destUid, tempFilePath);
                    singletonUserInfo.setDestList(destUserInfo);

                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, ChatActivity.class)
                            .putExtra("CR_Id", CR_Id);
                    startActivity(intent);
                }
            });
        } catch (FileNotFoundException e) {
            Log.e("jiseong", "FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("jiseong", "IOException : " + e.getMessage());

        }
    }

    // 랜덤 매칭 상대uid_상대nick 찾기
    void setDestNick() {

        destUid_Nick = new ArrayList<String>();
        runningUid = new ArrayList<String>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chatRoomData");

        firestore = FirebaseFirestore.getInstance();  // firestore

        myRef = database.getReference("ChatData");

        // realtimebase DB에서 자신과 현재 채팅창이 만들어져있는 상대유저Uid을 runningCR_Uid에 저장
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {

                        // 자신과 채팅방이 만들어진 유저 uid 저장
                        String UidThatExists = snapshot.getKey().replace(MyUid, "").replace(",", "");
                        Log.i("lcs", "채팅방이 만들어진 상대 유저 uid : " + UidThatExists);

                        runningUid.add(UidThatExists);
                    }

                    // firestore DB에서 현재 채팅방이 생성된 상대와 자신을 제외하고 선택한 MBTI와의 검사 및 랜덤채팅 유무 검사 진행하여 적절한 랜덤 상대를 읽기 및 저장
                    firestore.collection("InfoTest").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.i("lcs", "user : " + document.getId());
                                    Log.i("lcs", "user : " + document.get("MBTI"));

                                    if (!MyUid.equals(document.getId().toString()) && document.get("msg").equals("true")
                                            && !runningUid.contains(document.getId()) && destMbti.equals(document.get("MBTI"))) {

                                        destUid_Nick.add(document.getId() + "_" + document.get("nickname"));
                                        Log.i("lcs", "조건에 부합하는 상대 유저 uid_nick: " + document.getId() + "_" + document.get("nickname"));
                                    }
                                }
                                setParticipant();
                            } else {
                                Log.d("lcs", "get failed with ", task.getException());
                            }
                        }
                    });
                } else {
                    Log.d("lcs", "get failed with ", task.getException());
                }
            }
        });
    }

    // 뒤로가기 버튼 다이얼로그 메소드
    public void exit() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("정말 종료하시겠습니까?");
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //메인이 다시 호출됐을 때 바뀐 유저정보 업데이트
    @Override
    protected void onResume() {
        super.onResume();

        MyNick = singletonUserInfo.getMyNick();
        MyUid = singletonUserInfo.getMyUid();
        MyMbti = singletonUserInfo.getMyMbti();
        MyImgPath = singletonUserInfo.getMyImgPath();
        Log.i("jiseong", "main onResume + " + MyImgPath);
        tv_nickname.setText(MyNick);
        tv_mbti.setText(MyMbti);
    }
}