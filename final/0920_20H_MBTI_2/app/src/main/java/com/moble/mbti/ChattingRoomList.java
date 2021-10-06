package com.moble.mbti;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChattingRoomList extends Fragment {

    View v;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerAdapter;
    private List<ChatRoom> ChatRoomList;

    private DatabaseReference myRef;  //실시간 데이터베이스
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    // 이미지를 가져오기 위한 설정
    StorageReference pathRef;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    SingletonUserInfo singletonUserInfo = SingletonUserInfo.getInstance();

    // myRecyclerAdapter 액티비티에서 사용하기 위해 static 선언
    static ArrayList<String> myCRList;

    // 싱글톤에서 필요한 정보를 읽기 및 저장하는 변수
    private String MyNick;
    private String MyUid;
    private String MyUid_Nick;
    private File myStorage;
    ArrayList<DestUserInfo> destList;

    private String tempFilePath;

    // myRecyclerAdapter에서 필요한 상대 정보
    static ArrayList<String> destUidList;
    static ArrayList<String> destNickList;

    int count = 0;


    public ChattingRoomList() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_crl, container, false);


        ChatRoomList = new ArrayList<>();
        myCRList = new ArrayList<String>();

        // 리싸이클러뷰 및 어댑터 설정
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        mRecyclerAdapter = new MyRecyclerAdapter(getActivity(), ChatRoomList);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        getSingletonUserInfo();
        setDBListener();

        return v;
    }

    void getSingletonUserInfo() {
        MyUid = singletonUserInfo.getMyUid();
        Log.i("jiseong", MyUid);
        MyNick = singletonUserInfo.getMyNick();

        destList = singletonUserInfo.getDestList();

        // destList 제대로 들어왔는지 확인
        for (int i = 0; i < destList.size(); i++) {
            Log.i("lcs", "getSingletonUserInfo   destList : " + destList.get(i).getDestImgPath());
        }

        myStorage = singletonUserInfo.getStorage();
        Log.i("jiseong", myStorage + "");
    }

    // 실시간 db의 정보를 읽고 수신 대기하는 리스너 메소드
    void setDBListener() {

        destUidList = new ArrayList<String>();
        destNickList = new ArrayList<String>();

        // 저장했던 상대방 정보 초기화
        destList.clear();

        myRef = database.getReference("chatRoomData");

        myRef.child(MyUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                        Log.i("lcs", snapshot1.child("name").getValue().toString());

                    String destUid = snapshot1.child("uid").getValue().toString();
                    String destNick = snapshot1.child("nickname").getValue().toString();

                    Log.i("lcs", "destUid : " + destUid);
                    Log.i("lcs", "destNick : " + destNick);

                    destUidList.add(destUid);
                    destNickList.add(destNick);

                    Log.i("lcs", "onDataChange in destUidList : " + destUidList);
                    Log.i("lcs", "onDataChange in destNickList : " + destNickList);

                }


                //   destUid와 이름이 같은 ImgPath 불러오기
                for (int i = 0; i < destUidList.size(); i++) {

                    String destUid = destUidList.get(i);
                    String destNick = destNickList.get(i);

                    Log.i("lcs", "destUid : " + destUid);
                    Log.i("lcs", "destNick : " + destNick);

                    pathRef = storageRef.child("images/" + destUid + ".jpg");
                    //Log.i("lcs", "pathRef : " + pathRef);
                    String fileName = destUid + ".jpg";
                    //Log.i("lcs", "fileName : " + fileName);
                    File tempFile = new File(myStorage, fileName);

                    try {
                        tempFile.createNewFile();
                        pathRef.getFile(tempFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Log.i("lcs", "local file create success");
                                tempFilePath = tempFile.getAbsolutePath();      //사진이 저장되는 절대경로
                                Log.i("lcs", "CRL filePath : " + tempFilePath);

                                DestUserInfo destUserInfo = new DestUserInfo(destNick, destUid, tempFilePath);
                                Log.i("lcs", "destUserInfo.getDestImgPath() : " + destUserInfo.getDestImgPath());

                                singletonUserInfo.setDestList(destUserInfo);
                                Log.i("lcs", "destList.size() : " + destList.size());

                                count++;

                                // 마지막 이미지까지 로컬파일로 다운되었는지 검사하는 if문
                                if (count == destUidList.size()) {
                                    callAdapter();
                                }

                            }
                        });

                    } catch (FileNotFoundException e) {
                        Log.e("lcs", "FileNotFoundException : " + e.getMessage());
                    } catch (IOException e) {
                        Log.e("lcs", "IOException : " + e.getMessage());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("lcs", String.valueOf(error.toException())); // 에러문 출력
            }
        });


    }

    // 어댑터를 호출하는 리스너
    void callAdapter() {

        Log.i("lcs", "callAdapter Method in");

        myRef.child(MyUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                    Log.i("lcs", "snapshot2.child(\"uid\").getValue() : " + snapshot2.child("uid").getValue().toString());
                    myCRList.add(snapshot2.child("uid").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("lcs", String.valueOf(error.toException())); // 에러문 출력
            }
        });


        myRef.child(MyUid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i("lcs", "addChildEventListener in");
                ChatRoom chatRoom = snapshot.getValue(ChatRoom.class);
                ((MyRecyclerAdapter) mRecyclerAdapter).addChatR(chatRoom);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}