package com.moble.mbti;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MemberJoin extends AppCompatActivity {


    SingletonUserInfo singletonUserInfo;
    String MyUid;

    private static final String TAG = "jiseong";
    //private FirebaseFirestore db = null;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    //uid로 변경
    StorageReference imageRef;

    InputMethodManager immhide;

    FirebaseFirestore db = FirebaseFirestore.getInstance();     //DB 정보를 불러옴

    private Button btn_join, btn_overlap;
    private EditText et_nickname;
    private String uid;
    private String str_conversion;      //CharSequence 를 String 으로 받을 값
    private String spinner_mbti_result;
    private CheckBox checkBox_match;

    //값을 계속 갖고 있을것들
    private String checkBox_match_result = "false";
    private String save_str_conversion;     //중복체크를 눌렀을때의 이름 기록

    Spinner joinSpinner;
    ArrayAdapter mbtiAdapter;

    // 리스너들 모음
    AdapterView.OnItemSelectedListener listener_spinner;
    CompoundButton.OnCheckedChangeListener listener_checkbox_match;
    TextWatcher listener_et_nickname;
    View.OnClickListener listener_btn_join, listener_btn_overlap;

    int i=0;            //키보드 토글 flag

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_join);

        singletonUserInfo= SingletonUserInfo.getInstance();

        init();
        Log.i("jiseong","MemberJoin start "+MyUid);
    }

    void init(){
        setElements();
        get_SingletonUserInfo();
        listener_setting();     //리스너 기능 구현
        listener_link();        //리스너 연결
    }

    void listener_link(){
        btn_join.setOnClickListener(listener_btn_join);
        btn_overlap.setOnClickListener(listener_btn_overlap);
        et_nickname.addTextChangedListener(listener_et_nickname);           //EditText 가 값이 변동될 때
        checkBox_match.setOnCheckedChangeListener(listener_checkbox_match);

        mbtiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //기본 제공되는 spinner
        joinSpinner.setAdapter(mbtiAdapter);

        joinSpinner.setOnItemSelectedListener(listener_spinner);
    }

    void setElements(){
        mbtiAdapter = ArrayAdapter.createFromResource(this, R.array.spnnier_mbti, android.R.layout.simple_spinner_item);
        btn_join = (Button) findViewById(R.id.btn_join);
        btn_overlap = (Button) findViewById(R.id.btn_overlap);
        et_nickname = (EditText)findViewById(R.id.et_nickname);
        checkBox_match = (CheckBox)findViewById(R.id.cb_match);
        joinSpinner = (Spinner) findViewById(R.id.sp_mbti);     //정보 입력창 스피너
    }

    void listener_setting(){

        //스피너 클릭 리스너
        listener_spinner = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_mbti_result = joinSpinner.getSelectedItem().toString();
                Log.i("jiseong",spinner_mbti_result);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };


        //랜덤 채팅 확인 유무
        listener_checkbox_match = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                checkBox_match_result= check+"";
                Log.i("jiseong",check+"");
            }
        };

        //EditText 에 변환이 있을 때
        listener_et_nickname = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence str, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence char_str, int i, int i1, int i2) {
                //6글자까지 자르기 + String으로 변환
                str_conversion = char_str.toString();
                btn_overlap.setEnabled(true);
                if(str_conversion.length()>8 || str_conversion.length() < 2){
                    //Toast.makeText(MemberJoin.this,"8자리 이상 입력불가",Toast.LENGTH_SHORT).show();
                    Toast.makeText(MemberJoin.this, str_conversion,Toast.LENGTH_SHORT);
                    Log.i("jiseong",str_conversion);
                    btn_join.setEnabled(false);
                    et_nickname.setTextColor(Color.RED);
                }
                else {
                    et_nickname.setTextColor(Color.BLACK);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        };


        listener_btn_join = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("jiseong","btn_join");

                setInfo();      // UID, MBTI 정보, 닉네임, 문자 수신여부 정보 FireBase 저장

                // 싱글톤 객체에 정보 저장
                singletonUserInfo = SingletonUserInfo.getInstance();

                singletonUserInfo.setMyNick(str_conversion);
                singletonUserInfo.setMyRC_Check(checkBox_match_result);
                singletonUserInfo.setMyMbti(spinner_mbti_result);

                ImageView imageView = (ImageView)findViewById(R.id.im);                 //기본이미지
                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();

                //기본 이미지로 설정
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();     //내부저장소에 저장하기 위해 bitmap 으로 변환
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                Log.i("jiseong",bitmap+"");
                File storage = getCacheDir();           //내부저장소 CacheDir 을 불러옴
                singletonUserInfo.setStorage(storage);
                String fileName = singletonUserInfo.getMyUid() + ".jpg";

                
                File tempFile = new File(storage, fileName);
                singletonUserInfo.setMyImgPath(String.valueOf(tempFile));

                //이미지 저장
                try {
                    // 자동으로 빈 파일을 생성합니다.
                    tempFile.createNewFile();
                    FileOutputStream out = new FileOutputStream(tempFile);
                    // compress 함수를 사용해 스트림에 비트맵을 저장합니다.
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                    // 스트림 사용후 닫아줍니다.
                    out.close();

                } catch (FileNotFoundException e) {
                    Log.e("jiseong","FileNotFoundException : " + e.getMessage());
                } catch (IOException e) {
                    Log.e("jiseong","IOException : " + e.getMessage());
                }
                
                imageRef = storageRef.child("images/"+MyUid+".jpg");                //경로에 지정
                Log.i("jiseong","MemberJoin "+imageRef);
                UploadTask uploadTask = imageRef.putBytes(data);                    //drawble 에 있는 이미지를 byte로 변환 후 저장 

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                        Log.i("jiseong","member join upload success");
                    }
                });



                Intent intent = new Intent(MemberJoin.this, MainActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(MemberJoin.this, "회원가입 완료되었습니다", Toast.LENGTH_SHORT).show();
            }
        };

        listener_btn_overlap = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //포커스 아웃 시 키보드 내려가는 기능
                if(i == 0) {
                    immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    i=1;
                }
                String pattern =  "^[a-zA-Z0-9가-힣]*$";
                //닉네임 정규판별식
                boolean flag = Pattern.matches(pattern,str_conversion); //닉네임 규칙 판별

                Log.i("jiseong",flag+"");

                //중복클릭하면 중복 확인 후 토스트메시지 나옴
                //중복 확인을 하면 설정 하기 버튼이 활성화
                if(str_conversion.length()>8 || str_conversion.length() < 2){
                    Toast.makeText(MemberJoin.this, "2글자에서 8자리 사이만 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                }
                else if(!flag){
                    Toast.makeText(MemberJoin.this, "특수문자, 자음, 모음은 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    db.collection("InfoTest").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String nick = document.get("nickname").toString();

                                    if(nick.equals(str_conversion)){
                                        Log.i("jiseong","same!");
                                        Toast.makeText(MemberJoin.this, "이미 사용중인 아이디입니다.", Toast.LENGTH_SHORT).show();
                                        btn_join.setEnabled(false);
                                        return;
                                    }
                                }
                                Toast.makeText(MemberJoin.this, "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                                btn_join.setEnabled(true);
                                save_str_conversion = str_conversion;
                                Log.i("jiseong","not find equal uid");
                            }
                        }
                    });

                }
            }
        };
    }


    //firebase 에 회원 정보 저장
    void setInfo(){
        db = FirebaseFirestore.getInstance();
        CollectionReference info = db.collection("InfoTest");

        Map<String, Object> data1 = new HashMap<>();
        data1.put("nickname", save_str_conversion);
        data1.put("MBTI", spinner_mbti_result);
        data1.put("UID", MyUid);
        data1.put("msg",checkBox_match_result);
        info.document(MyUid).set(data1);
    }

    void get_SingletonUserInfo(){
        MyUid = singletonUserInfo.getMyUid();
        Log.i("jiseong",MyUid);
    }

}