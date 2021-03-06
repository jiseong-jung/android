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

    //firestorage ????????? ????????????
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();       //firestorage
    StorageReference pathRef;

    private String CR_Id;
    private String destMbti;  // ??????????????? view?????? ????????? ???????????? ?????? ????????? mbti ??????

    private ArrayList<String> destUid_Nick;

    private ArrayList<String> runningUid;

    String tempFilePath;
    Button btn_home, btn_chat, btn_compare, btn_match;      //?????? ?????? ??????

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


    View nav_header_view;           //navigation ?????? ?????? ?????? ????????? header.xml ???????????? ??????
    View toolbar_chat;              //toolbar ?????? ?????? ?????? ?????? ??? toolbar_main.xml ???????????? ??????

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

        drawerLayout = findViewById(R.id.drawer);           //??????(drawer)
        ivMenu = findViewById(R.id.iv_menu);
        toolbar = findViewById(R.id.toolbar);               //?????? ??????(drawer ???)
        setSupportActionBar(toolbar);
        btn_logout = findViewById(R.id.btn_logout1);
        toolbar_chat = (View) findViewById(R.id.included_layout);
        drawer = findViewById(R.id.drawer);


        navigationView = findViewById(R.id.navigation);     //???????????????
        navigationView.setNavigationItemSelectedListener(listener_itemselected);
        nav_header_view = navigationView.getHeaderView(0);

        //?????? ?????????
        view1 = (CircleImageView) nav_header_view.findViewById(R.id.img_nav);

        tv_mbti = (TextView) nav_header_view.findViewById(R.id.tv_mbti_output);
        tv_nickname = (TextView) nav_header_view.findViewById(R.id.tv_nickname_output);
        appbar_title = (TextView) toolbar_chat.findViewById(R.id.tv_logo);

        tv_MyMbtiInfotitle = (TextView) findViewById(R.id.tv_MyMbtiInfoTitle);
        tv_MyMbtiInfo = (TextView) findViewById(R.id.tv_MyMbtiInfoData);

        view1.setImageURI(Uri.parse(MyImgPath));
    }

    void listener_link() {
        ivMenu.setOnClickListener(listener_drawer);         //????????? ??????(????????? ???) ?????????
        btn_logout.setOnClickListener(listener_btn_logout);
    }


    NavigationView.OnNavigationItemSelectedListener listener_itemselected = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();

            //NavigationView ??????????????? ??????
            if (id == R.id.menu_item1) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MyPage.class);

                startActivity(intent);
                finish();

            }
            //NavigationView ??? MBTI ?????? ?????? ??????
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

    //????????? ??????(????????? ???) ?????????
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

        //??? ???????????????
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

        //????????? ???????????????
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

        //?????? ???????????????
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

        //?????? ??????
        btn_match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matchDialog();
            }
        });
    }

    //?????? ?????? ?????? ??? ?????? ??????????????? ???
    public void matchDialog() {
        AlertDialog.Builder dialMatch = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.match_dialog, null);


        dialMatch.setTitle("?????? ??????");
        dialMatch.setMessage("?????? MBTI\n " + MyMbti);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                destMbti = spinner.getSelectedItem().toString();    //????????? ????????? MBTI
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.spnnier_mbti, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        dialMatch.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setDestNick();
            }
        });

        dialMatch.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialMatch.setView(view);
        AlertDialog alertDialog = dialMatch.create();
        alertDialog.show();

        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT); //??????????????? ??? ?????? ??????
    }


    //???????????? ??????????????? ??????????????? ????????? ??? ????????? ?????? ??????????????? ??????
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
            Log.i("jiseong", "MainActivity ?????? Logout");
            FirebaseAuth.getInstance().signOut();       //?????? ?????? ????????????

            Intent intent = new Intent();
            intent.setClass(MainActivity.this, Login.class);
            startActivity(intent);

            // ???????????? ????????? ?????? ?????????
            destList.clear();

        }
    };


    //?????? ??? fragment home_Activity ??? ?????? ??????
    void homeCall() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Home fragment1 = new Home();
        transaction.replace(R.id.frame, fragment1);
        transaction.commit();
    }

    // ??????????????? ???????????? ?????? ??? ChatiingRoomList ?????????
    void chatRoomListCall() {

        appbar_title.setText("Chatting");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ChattingRoomList fragment2 = new ChattingRoomList();
        transaction.replace(R.id.frame, fragment2);
        transaction.commit();
    }

    // ?????? ??????????????? ????????? ?????? ????????? ????????? ????????? ???????????? ?????? ??? ??????
    void get_SingletonUserInfo() {
        MyNick = singletonUserInfo.getMyNick();
        MyUid = singletonUserInfo.getMyUid();
        MyMbti = singletonUserInfo.getMyMbti();
        MyImgPath = singletonUserInfo.getMyImgPath();

        destList = singletonUserInfo.getDestList();

        Log.i("jiseong", "main + " + MyImgPath);

    }

    // setDestNick()?????? ?????? ????????? ?????????????????? ????????? ?????? ?????? ??? chatActicity ??? ????????????
    void setParticipant() {         //?????????

        // realtimebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chatRoomData");

        //????????? MBTI??? ????????? ??? ?????? ??? (?????? ???????????? ????????? ?????? ??????)
        if (destUid_Nick.size() == 0) {
            Toast.makeText(MainActivity.this, "????????? ????????? ????????? ??? ?????? ????????????.", Toast.LENGTH_SHORT).show();
            return;
        }

        Collections.shuffle(destUid_Nick);

        // '_' ??? ???????????? Uid??? Nickname ?????? (????????? ????????? uid_nickname ????????????)
        String destUid = destUid_Nick.get(0).substring(0, destUid_Nick.get(0).indexOf("_"));
        String destNick = destUid_Nick.get(0).substring(destUid_Nick.get(0).indexOf("_") + 1);

        Log.i("lcs", "destUid : " + destUid);
        Log.i("lcs", "destNick : " + destNick);

        CR_Id = MyUid + "," + destUid;

        ChatRoom cr = new ChatRoom(destUid, destNick, "");
        myRef.child(MyUid).push().setValue(cr);

        cr = new ChatRoom(MyUid, MyNick, "");
        myRef.child(destUid).push().setValue(cr);

        database.getReference("ChatData").child(CR_Id).setValue("");   // ????????? ????????? ???????????? ????????? ??????

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
                    tempFilePath = tempFile.getAbsolutePath();      //????????? ???????????? ????????????
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

    // ?????? ?????? ??????uid_??????nick ??????
    void setDestNick() {

        destUid_Nick = new ArrayList<String>();
        runningUid = new ArrayList<String>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chatRoomData");

        firestore = FirebaseFirestore.getInstance();  // firestore

        myRef = database.getReference("ChatData");

        // realtimebase DB?????? ????????? ?????? ???????????? ?????????????????? ????????????Uid??? runningCR_Uid??? ??????
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {

                        // ????????? ???????????? ???????????? ?????? uid ??????
                        String UidThatExists = snapshot.getKey().replace(MyUid, "").replace(",", "");
                        Log.i("lcs", "???????????? ???????????? ?????? ?????? uid : " + UidThatExists);

                        runningUid.add(UidThatExists);
                    }

                    // firestore DB?????? ?????? ???????????? ????????? ????????? ????????? ???????????? ????????? MBTI?????? ?????? ??? ???????????? ?????? ?????? ???????????? ????????? ?????? ????????? ?????? ??? ??????
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
                                        Log.i("lcs", "????????? ???????????? ?????? ?????? uid_nick: " + document.getId() + "_" + document.get("nickname"));
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

    // ???????????? ?????? ??????????????? ?????????
    public void exit() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("?????? ?????????????????????????");
        builder.setNegativeButton("?????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("???", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //????????? ?????? ???????????? ??? ?????? ???????????? ????????????
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