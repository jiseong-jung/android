package com.moble.mbti;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


//todo MBTI 두개의 유형을 받아서 DB와 연동 후 값 불러오기

public class Matching extends Fragment {

    Button btn_result;
    TextView textView_result;
    View v;
    Spinner sp_my;
    Spinner sp_other;
    String str_my;
    String str_other;




    ArrayAdapter aa;

    SingletonUserInfo singletonUserInfo = SingletonUserInfo.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();     //db연동

    public Matching() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_matching, container, false);

        //현재 사용자 uid 저장
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference docRef = db.collection("InfoTest").document(userUid);

        //get()을 통해서 해당 문서의 정보를 가져온다.
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @NonNull
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                // 작업이 성공적일때
                if (task.isSuccessful()) {
                    // 문서의 데이터를 담을 DocumentSnapshot 에 작업의 결과를 담는다.
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        // 값 불러오기
                    } else {
                        Log.d("jiseong", "get failed with ", task.getException());
                    }
                }
            }
        });

        setSpinner();
        eventListener();

        return v;

    }

    //비교할 MBTI를 스피너를 통해 값을 가져옴
    public void eventListener() {
        sp_my.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_my = sp_my.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                str_other = sp_other.getSelectedItem().toString();
            }
        });

        sp_other.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_other = sp_other.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //궁합 불러오기
        btn_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String my = sp_my.getSelectedItem().toString(); // 내 mbti
                String other = sp_other.getSelectedItem().toString(); // 상대 mbti

                DocumentReference mbti = db.collection(my).document(other); // Firebase에 저장되어있는 mbti 궁합 찾아가기
                mbti.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                textView_result.setText(document.get("data").toString());
                                Log.i("jiseong", String.valueOf(document.getData()));
                            } else {
                                textView_result.setText("궁합 없음");
                                Log.i("jiseong", "실패");
                            }
                        } else {
                            Log.i("jiseong", String.valueOf(task.getException()));

                        }
                    }
                });
            }
        });
    }

    public void setSpinner() {

        textView_result = (TextView) v.findViewById(R.id.text_result);
        btn_result = (Button) v.findViewById(R.id.btn_result);
        sp_my = v.findViewById(R.id.sp_my);
        sp_other = v.findViewById(R.id.sp_mbti);
        aa = ArrayAdapter.createFromResource(getContext(), R.array.spnnier_mbti, android.R.layout.simple_spinner_dropdown_item);
        sp_my.setAdapter(aa);
        sp_other.setAdapter(aa);

        //나의 MBTI를 스피너에 디폴트 값으로 설정
        for (int i = 0; i < sp_my.getCount(); i++) {
            if (sp_my.getItemAtPosition(i).toString().equals(singletonUserInfo.getMyMbti())) {
                sp_my.setSelection(i);
            }
        }

    }
}