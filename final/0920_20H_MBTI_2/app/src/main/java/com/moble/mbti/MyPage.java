package com.moble.mbti;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class MyPage extends AppCompatActivity {

    //Singleton 값들 불러옴
    SingletonUserInfo singletonUserInfo = SingletonUserInfo.getInstance();
    String MyNick, MyUid, MyImgPath, MyMbti, MyRC_Check;
    //String uid, mbti, nickname, msg;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private DatabaseReference myRef;
    private FirebaseDatabase realDb;

    ImageView img_setting;

    EditText et_nickname;                //닉네임 변경할 EditText
    Spinner mbtiSpinner;                //MBTI 선택할 Spinner
    ArrayAdapter mbtiAdapter;
    Button btn_overlap, btn_setting;

    private String spinner_mbti_result;
    private CheckBox checkBox_match;


    private String checkBox_match_result = "false";
    private String save_str_conversion;
    private String str_conversion;              //EditText 의 값을 String 으로 저장


    InputMethodManager immhide;                 //입력이 다 되면 (포커스를 벗어나면) 키보드 내려가게

    AdapterView.OnItemSelectedListener listener_spinner;
    private View.OnClickListener listener_btn_overlap;
    View.OnClickListener listener_btn_setting, listener_img_setting;
    TextWatcher listener_et_nickname;
    Spinner joinSpinner;
    CompoundButton.OnCheckedChangeListener listener_checkbox_match;
    //이미지 관련
    View.OnClickListener listener_btn_photo_get, listener_btn_photo_upload;
    Uri photoUri;       //내부저장소에 있는 이미지 Uri 주소 저장
    static final int PICK_IMAGE_FROM_ALBUM = 1; // request code
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    Boolean flag_image_change = false;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        Log.i("jiseong", "MyPage");


        init();


        img_setting.setImageURI(Uri.parse(MyImgPath));        //절대 경로에 있는 사진으로 이미지 설정

    }


    void init() {
        get_SingletonUserInfo();
        setElements();
        setMbtiSpinner();
        listener_setting();     //리스너 기능 구현
        listener_link();        //리스너 연결
        checkSelfPermission();
    }


    public void setElements() {
        Log.i("jiseong", "Mypage" + "setElements");
        et_nickname = (EditText) findViewById(R.id.et_setNick);
        btn_overlap = findViewById(R.id.btn_overlap);
        btn_setting = findViewById(R.id.btn_setting);
        joinSpinner = findViewById(R.id.sp_mbti);
        checkBox_match = findViewById(R.id.cb_match);
        img_setting = (ImageView) findViewById(R.id.iv_myPage);
        et_nickname.setText(MyNick);
        img_setting.setEnabled(false);

        Log.i("jiseong", "msg = " + MyRC_Check);

        if (MyRC_Check.equals("false")) {
            checkBox_match.setChecked(false);
        } else {
            checkBox_match.setChecked(true);
        }


    }

    public void setMbtiSpinner() {
        mbtiSpinner = (Spinner) findViewById(R.id.sp_mbti);
        mbtiAdapter = ArrayAdapter.createFromResource(this, R.array.spnnier_mbti, android.R.layout.simple_spinner_item);
        mbtiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mbtiSpinner.setAdapter(mbtiAdapter);
        Log.i("jiseong", "size:" + joinSpinner.getCount() + "");

        //값을 가져온 mbti 값과 spinner 값을 비교 후 default 설정
        for (int i = 0; i < joinSpinner.getCount(); i++) {
            if (joinSpinner.getItemAtPosition(i).toString().equals(MyMbti)) {
                joinSpinner.setSelection(i);
            }
        }
    }

    void listener_link() {
        Log.i("jiseong", "listener_link");
        et_nickname.addTextChangedListener(listener_et_nickname);           //EditText 가 값이 변동될 때
        btn_overlap.setOnClickListener(listener_btn_overlap);
        btn_setting.setOnClickListener(listener_btn_setting);
        checkBox_match.setOnCheckedChangeListener(listener_checkbox_match);
        joinSpinner.setOnItemSelectedListener(listener_spinner);
        img_setting.setOnClickListener(listener_img_setting);               //ImageView 를 클릭했을 때

    }

    void listener_setting() {
        Log.i("jiseong", "listener_setting");

        //이미지 설정
        listener_img_setting = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag_image_change = true;
                Log.i("jiseong", "ImageView click");
                Intent intent = new Intent();
                //기기 기본 갤러리 접근
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                //구글 갤러리 접근
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_FROM_ALBUM);
            }
        };

        //랜덤 채팅 확인 유무
        listener_checkbox_match = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                checkBox_match_result = check + "";
                Log.i("jiseong", check + "");
            }
        };

        //스피너 클릭 리스너
        listener_spinner = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_mbti_result = joinSpinner.getSelectedItem().toString();
                Log.i("jiseong", spinner_mbti_result);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };


        listener_btn_overlap = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i == 0) {
                    immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    i = 1;
                }
                String pattern = "^[a-zA-Z0-9가-힣]*$";
                //닉네임 정규판별식
                boolean flag = Pattern.matches(pattern, str_conversion); //닉네임 규칙 판별

                Log.i("jiseong", flag + "");

                //중복클릭하면 중복 확인 후 토스트메시지 나옴
                //중복 확인을 하면 설정 하기 버튼이 활성화
                if (str_conversion.length() > 8 || str_conversion.length() < 2) {
                    Toast.makeText(MyPage.this, "2글자에서 8자리 사이만 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                } else if (!flag) {
                    Toast.makeText(MyPage.this, "특수문자, 자음, 모음은 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    db.collection("InfoTest").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String nick = document.get("nickname").toString();

                                    if (nick.equals(str_conversion)) {
                                        Log.i("jiseong", "MemberJoin uid = " + nick);
                                        Log.i("jiseong", "same!");
                                        Toast.makeText(MyPage.this, "이미 사용중인 아이디입니다.", Toast.LENGTH_SHORT).show();
                                        btn_setting.setEnabled(false);
                                        return;
                                    }
                                }
                                Toast.makeText(MyPage.this, "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                                btn_setting.setEnabled(true);
                                save_str_conversion = str_conversion;
                                Log.i("jiseong", "not find equal uid");
                            }
                        }
                    });
                }

            }
        };

        //정보 수정 후
        listener_btn_setting = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.i("jiseong", "setting_btn");
                Intent it = new Intent();
                it.setClass(MyPage.this, MainActivity.class);
                setInfo();


                if (flag_image_change) {
                    storageRef = storage.getReference().child("images").child(MyUid + ".jpg");   //uid 로 저장
                    storageRef.putFile(photoUri);
                    //todo photoUri 내부 저장소에 저장
                    try {
                        File localFile = File.createTempFile("images", "jpg");
                        localFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //firebase storage 에 images 파일에 MyUid.jpg 이름으로 사진 저장
                    //StorageReference pathReference = storageRef.child("images").child(MyUid+".jpg");
                    //Log.i("jiseong",pathReference+"");
                    Log.i("jiseong", "upload");
                    singletonUserInfo.setMyImgPath(String.valueOf(photoUri));
                }

                startActivity(it);
                Toast.makeText(MyPage.this, "정보 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }


        };


        listener_et_nickname = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //str_conversion = et_nickname.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence char_str, int i, int i1, int i2) {
                btn_overlap.setEnabled(true);       //별명이 바뀌면 중복확인버튼 활성화
                btn_setting.setEnabled(false);

                //6글자까지 자르기 + String으로 변환
                str_conversion = char_str.toString();

                if (str_conversion.length() > 8 || str_conversion.length() < 2) {
                    //Toast.makeText(MemberJoin.this,"8자리 이상 입력불가",Toast.LENGTH_SHORT).show();
                    Toast.makeText(MyPage.this, str_conversion, Toast.LENGTH_SHORT);
                    Log.i("jiseong", str_conversion);
                    //btn_setting.setEnabled(false);
                    et_nickname.setTextColor(Color.RED);
                } else {
                    et_nickname.setTextColor(Color.BLACK);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

    }

    void setInfo() {
        db = FirebaseFirestore.getInstance();
        CollectionReference info = db.collection("InfoTest");

        //DocumentReference docRef = db.collection("InfoTest").document("nickname");
        db.collection("InfoTest").document(MyUid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i("jiseong", MyNick);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("jiseong", "필드 삭제 실패");
            }
        });

        if (et_nickname.getText().toString().equals(MyNick)) {
            Map<String, Object> data1 = new HashMap<>();
            data1.put("nickname", MyNick);
            data1.put("MBTI", spinner_mbti_result);
            data1.put("UID", MyUid);
            data1.put("msg", checkBox_match_result);
            info.document(MyUid).set(data1);
            singletonUserInfo = SingletonUserInfo.getInstance();

            singletonUserInfo.setMyUid(MyUid);
            singletonUserInfo.setMyRC_Check(checkBox_match_result);
            singletonUserInfo.setMyMbti(spinner_mbti_result);
            return;
        }

        Map<String, Object> data1 = new HashMap<>();
        data1.put("nickname", save_str_conversion);
        data1.put("MBTI", spinner_mbti_result);
        data1.put("UID", MyUid);
        data1.put("msg", checkBox_match_result);
        info.document(MyUid).set(data1);

        updateRealDbNick();

        singletonUserInfo = SingletonUserInfo.getInstance();

        singletonUserInfo.setMyNick(str_conversion);
        singletonUserInfo.setMyUid(MyUid);
        singletonUserInfo.setMyRC_Check(checkBox_match_result);
        singletonUserInfo.setMyMbti(spinner_mbti_result);



        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //btn_photo_get
        if (requestCode == PICK_IMAGE_FROM_ALBUM) {
            if (resultCode == Activity.RESULT_OK) {

                Log.i("jiseong", "사진 정상작동");
                photoUri = data.getData();
                Log.i("jiseong", photoUri + "");
                img_setting.setImageURI(photoUri);


            } else {
                Log.i("jiseong", "!");
            }
        }
    }

    void get_SingletonUserInfo() {
        MyNick = singletonUserInfo.getMyNick();
        MyUid = singletonUserInfo.getMyUid();
        MyMbti = singletonUserInfo.getMyMbti();
        MyImgPath = singletonUserInfo.getMyImgPath();
        MyRC_Check = singletonUserInfo.getMyRC_Check();
        checkBox_match_result = MyRC_Check;


        Log.i("jiseong", MyImgPath);
    }




    public void checkSelfPermission() {

        String temp = "";
        //파일 읽기 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
            Log.i("jiseong","1");
        }

        if (TextUtils.isEmpty(temp) == false) {
            // 권한 요청
            ActivityCompat.requestPermissions(this, temp.trim().split(" "), 1);
            Log.i("jiseong","2");
        } else {
            // 모두 허용 상태
            Log.i("jiseong","3");
            img_setting.setEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_IMAGE_FROM_ALBUM) {
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 동의
                    Log.i("jiseong", "권한 허용 : " + permissions[i]);
                    Toast.makeText(this, "권한 허용", Toast.LENGTH_SHORT).show();
                    img_setting.setEnabled(true);
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.setClass(MyPage.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // 닉네임 변경을 했다면 실시간 디비에 nickname 정보를 변경해주는 메소드
    void updateRealDbNick() {

        realDb = FirebaseDatabase.getInstance();
        myRef = realDb.getReference("chatRoomData");

        myRef = realDb.getReference("chatRoomData");
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {

                    Log.i("lcs", "채팅방 key : " + dataSnapshot.getKey());

                    myRef.child(dataSnapshot.getKey()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            for (DataSnapshot dataSnapshot1 : task.getResult().getChildren()) {
                                Log.i("lcs", "nickname  : " + dataSnapshot1.child("nickname"));

                                Log.i("lcs", "MyNick : " + MyNick);
                                if (dataSnapshot1.child("nickname").getValue().equals(MyNick)) {
                                    Log.i("lcs", "조건에 맞는 key  : " + dataSnapshot1.getKey());

                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("nickname", save_str_conversion);

                                    myRef.child(dataSnapshot.getKey()).child(dataSnapshot1.getKey()).updateChildren(map);
                                }
                            }
                        }
                    });

                }
            }
        });
    }
}