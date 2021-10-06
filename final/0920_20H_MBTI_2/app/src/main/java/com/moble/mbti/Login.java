package com.moble.mbti;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

//로그인 되어 있는 정보가 없을때 로그인을 진행할 액티비티
public class Login extends AppCompatActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    StorageReference pathRef;

    private DatabaseReference myRef;            //실시간 데이터 베이스
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ArrayList<DestUserInfo> destList;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    Intent intent = new Intent();
    private SignInButton btn_login;
    private static final int RC_SIGN_IN = 9001;
    private String str_mbti, str_nickname, str_msg;
    private String uid = null;
    private TextView tv_MbtiLink;           //외부 연결


    SingletonUserInfo singletonUserInfo = SingletonUserInfo.getInstance();
    String MyNick, MyUid, MyMbti, MyImgPath;
    String tempFilePath;

    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.i("jiseong", "Login Start");

        init();

        // googleSingInOptions 객체 구성할때 requestIdToken 호출함
        //로그인 후 다음 어플리케이션을 켰을때 바로 다음 화면으로 넘어가기?
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);        //기존 로그인 이력 확인

        mGoogleSignInClient.signOut();  //재로그인시 자동 로그인 방지


    }

    void init() {
//        get_SingletonUserInfo();
        setElements();
    }

    void setElements() {
        mAuth = FirebaseAuth.getInstance();     //firebaseAuth 객체의 공유 인스턴스를 가져옴
        btn_login = findViewById(R.id.btn_login);
        tv_MbtiLink = (TextView) findViewById(R.id.tv_MbtiLink);
        tv_MbtiLink.setOnClickListener(tv_MbtiLink_listener);

        btn_login.setOnClickListener(btn_login_listener);
    }


    View.OnClickListener btn_login_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //로그인 dialog
            signIn();

        }
    };

    View.OnClickListener tv_MbtiLink_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.16personalities.com/ko/%EB%AC%B4%EB%A3%8C-%EC%84%B1%EA%B2%A9-%EC%9C%A0%ED%98%95-%EA%B2%80%EC%82%AC"));
            startActivity(intent);
        }
    };


    //로그인
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    //signIn()함수 startActivityForResult의 결과
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.i("jiseong", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("jiseong", "Google sign in failed", e);
            }
        }

        //login();
    }

    //정상적으로 로그인하였는지 토큰 확인
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("jiseong", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            String uid = user.getUid();
                            singletonUserInfo.setMyUid(uid);
                            Log.i("jiseong", "Login + " + uid);


                            db.collection("InfoTest").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        Log.i("jiseong", "login activity success");
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String str_uid = document.get("UID").toString();
                                            Log.i("jiseong", "Intro str uid = " + str_uid);
                                            if (str_uid.equals(uid)) {

                                                flag++;

                                                Log.i("jiseong", "find equal uid");
                                                String str_mbti = document.get("MBTI").toString();
                                                String str_nickname = document.get("nickname").toString();
                                                String str_msg = document.get("msg").toString();


                                                pathRef = storageRef.child("images/" + uid + ".jpg");
                                                File storage = getCacheDir();
                                                singletonUserInfo.setStorage(storage);
                                                String fileName = uid + ".jpg";
                                                File tempFile = new File(storage, fileName);

                                                try {
                                                    tempFile.createNewFile();
                                                    pathRef.getFile(tempFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                            Log.i("jiseong", "local file create success");
                                                            tempFilePath = tempFile.getAbsolutePath();      //사진이 저장되는 절대경로
                                                            Log.i("jiseong", "Intro filePath" + tempFilePath);
                                                            singletonUserInfo.setMyImgPath(tempFilePath);
                                                            singletonUserInfo.setMyNick(str_nickname);
                                                            singletonUserInfo.setMyMbti(str_mbti);
                                                            singletonUserInfo.setMyRC_Check(str_msg);

                                                            // todo

                                                            String MyUid_Nick = uid + "_" + str_nickname;
                                                            destList = new ArrayList<DestUserInfo>();

                                                            myRef = database.getReference("chatRoomData");
                                                            myRef.child(MyUid_Nick).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        for (DataSnapshot snapshot : task.getResult().getChildren()) {
//                                                                            Log.i("lcs", snapshot.child("name").getValue().toString());

                                                                            String destUid_Nick = snapshot.child("name").getValue().toString();
                                                                            String destUid = destUid_Nick.substring(0, destUid_Nick.indexOf("_"));
                                                                            String destNick = destUid_Nick.substring(destUid_Nick.indexOf("_") + 1);

                                                                            Log.i("lcs", "destUid_Nick : " + destUid_Nick);
                                                                            Log.i("lcs", "destUid : " + destUid);
                                                                            Log.i("lcs", "destNick : " + destNick);

                                                                            //todo   destUid와 이름이 같은 ImgPath 불러오기

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
                                                                                        Log.i("jiseong", " Login dest size = " + singletonUserInfo.getDestList().size());
                                                                                    }
                                                                                });
                                                                            } catch (FileNotFoundException e) {
                                                                                Log.e("jiseong", "FileNotFoundException : " + e.getMessage());
                                                                            } catch (IOException e) {
                                                                                Log.e("jiseong", "IOException : " + e.getMessage());

                                                                            }

                                                                        }

                                                                    } else
                                                                        Log.d("lcs", "get failed with ", task.getException());
                                                                }
                                                            });


                                                            intent.setClass(Login.this, MainActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                            return;
                                                        }
                                                    });
                                                } catch (FileNotFoundException e) {
                                                    Log.e("jiseong", "FileNotFoundException : " + e.getMessage());
                                                } catch (IOException e) {
                                                    Log.e("jiseong", "IOException : " + e.getMessage());
                                                }
                                            }

                                        }
                                        Log.i("jiseong", "signInWithCredential:failure", task.getException());

                                        // fireStore에 현재 사용자 uid와 일치하는것이 없다면 MemberJoin 액티비티로 이동
                                        if (flag == 0) {
                                            intent.setClass(Login.this, MemberJoin.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    } else
                                        Log.i("jiseong", "login activity fail");
                                }
                            });

                        }
                    }
                });
    }
}