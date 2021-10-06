package com.moble.mbti;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

// chattingRoomList 프레그먼트의 view를 그려주는 adapter
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    private List<ChatRoom> ChatRoomList;
    private Context mContext;

    // 싱글톤에서 필요한 정보를 읽기 및 저장
    private String MyNick;
    private String MyUid;
    private String MyUid_Nick;
    private String destTempFilePath;
    ArrayList<DestUserInfo> destList;


    public MyRecyclerAdapter(Context context, List<ChatRoom> chatRoomList) {
        ChatRoomList = chatRoomList;
        mContext = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false);

        getSingletonUserInfo();


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ChatRoom chatRoom = ChatRoomList.get(position);

        // chatRoom에 저장되어있는 닉네임 형식은 Uid_Nick이기 때문에 잘라서 Nick만 저장
        String nickname = chatRoom.getNickname();


        FindDestTempFilePath(position);


        // 채팅 리스트 정보 넣어주는 부분
        holder.profile.setImageURI(Uri.parse(destTempFilePath));
        holder.name.setText(chatRoom.getNickname());
        holder.message.setText(chatRoom.getMessage());
    }

    @Override
    public int getItemCount() {
        return ChatRoomList == null ? 0 : ChatRoomList.size();
    }

    public void addChatR(ChatRoom chatRoom) {
        ChatRoomList.add(chatRoom);

        // position의 위치에 새로 삽입된 아이템이 있음을 옵저버에 알려 반영한다.
        // 알리게 되면 ChatAdapter에서 해당 view item을 그린다.
        notifyItemInserted(ChatRoomList.size() - 1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name;
        TextView message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile = (ImageView) itemView.findViewById(R.id.profile);
            name = (TextView) itemView.findViewById(R.id.name);
            message = (TextView) itemView.findViewById(R.id.message);

            // 클릭한 채팅방List에 맞는 채팅방으로 이동
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();        //채팅창 선택

                    Log.i("lcs", "position : " + position);
                    Log.i("lcs", "myNick : " + MyNick);

                    for (int i = 0; i < ChattingRoomList.myCRList.size(); i++) {
                        Log.i("lcs", "myCRList(" + i + ") : " + ChattingRoomList.myCRList.get(i));
                    }

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("ChatData");

                    // myCRList.get(position)[상대방uid]와 MyUid가 포함되어있는 CR_Id(채팅방 id) 검색
                    myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {

                                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                                    Log.i("lcs", "채팅방 id 로그 : " + snapshot.getKey());
                                    Log.i("lcs", "ChattingRoomList.myCRList : " + ChattingRoomList.myCRList);

                                    if (snapshot.getKey().contains(MyUid)
                                            && snapshot.getKey().contains(ChattingRoomList.myCRList.get(position))) {

                                        // 조건에 부합하는 CR_Id를 찾으면 저장 후 인텐트에 담아 채팅액티비티로 넘김.
                                        String CR_Id = snapshot.getKey();
                                        Log.i("lcs", "클릭한 채팅 리스트에 부합하는 채팅방 ID : " + CR_Id);

                                        Intent intent = new Intent();
                                        intent.setClass(mContext, ChatActivity.class)
                                                .putExtra("CR_Id", CR_Id);

                                        mContext.startActivity(intent);
                                        ((Activity) mContext).finish();

                                    } else {
                                        Log.i("lcs", "클릭한 채팅 리스트에 부합하는 채팅방이 없습니다");
                                    }
                                }

                            }else {
                                Log.d("lcs", "get failed with ", task.getException());
                            }

                        }
                    });
                }
            });
        }
    }

    void getSingletonUserInfo() {

        SingletonUserInfo singletonUserInfo = SingletonUserInfo.getInstance();


        MyUid = singletonUserInfo.getMyUid();
        MyNick = singletonUserInfo.getMyNick();
        destList = singletonUserInfo.getDestList();

        MyUid_Nick = MyUid + "_" + MyNick;

        // myCRList 제대로 저장 되었는지 확인
        for (int i = 0; i < ChattingRoomList.myCRList.size(); i++) {
            Log.i("lcs", "myRecyclerAdapter   myCRList : " + ChattingRoomList.myCRList.get(i));
        }

        // destList 제대로 저장 되었는지 확인
        for (int i = 0; i < destList.size(); i++) {
            Log.i("lcs", "myRecyclerAdapter   destList : " + destList.get(i).getDestImgPath());
        }

    }

    // 싱글톤 destList에 저장된 알맞은 상대방 TempFilePath 정보 가져오기
    void FindDestTempFilePath(int position) {

        String destUid = ChattingRoomList.destUidList.get(position);
        String destNick = ChattingRoomList.destNickList.get(position);


        // 상대방의 정보가 담긴 destList의 Nick과 Uid를 CR_Id(채팅방 ID)에서 검출한 상대방 정보와 비교해서 같으면 상대방 이미지를 가져온다.
        for (int j = 0; j < destList.size(); j++) {
            if (destList.get(j).getDestNick().equals(destNick)
                    && destList.get(j).getDestUid().equals(destUid)) {

                destTempFilePath = destList.get(j).getDestImgPath();
                break;
            }
        }


    }
}