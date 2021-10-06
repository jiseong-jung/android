package com.moble.mbti;

import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// 리사이클러뷰를 사용하기 위해서는 반드시 개발자가 어댑터를 직접 구현해야 한다.
// 그리고 이 때 새로 만드는 어댑터는 RecyclerView.Adapter를 상속하여 구현해야 한다.
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<ChatData> chatList;
    private String my_uid;

    private String MyUid;
    private String destUid;
    private String destNick;
    private String destTempFilePath;

    SingletonUserInfo singletonUserInfo = SingletonUserInfo.getInstance();

    // 싱글톤의 destList정보를 담는 멤버변수
    ArrayList<DestUserInfo> destList;


    public ChatAdapter(List<ChatData> chatList, String my_uid) {
        this.chatList = chatList;
        this.my_uid = my_uid;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {      // viewType 형태의 아이템 뷰를 위한 뷰홀더 객체 생성.
        // LayoutInflater.from(parent.getContext()) -> 부모의 컨텐츠를 받는 LayoutInflater를 생성
        // 레이아웃 XML파일을 View객체로 만들기 위해서는 LayoutInflater내의 inflater 메서드를 사용.
        // attachToRoot가 false일 경우에는 LayoutParams값을 설정해주기 위한 상위 뷰
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_chat, parent, false);

        destList = singletonUserInfo.getDestList();


        Log.i("jiseong", "ChatAdapter start");
        Log.i("jiseong", destList.get(0).getDestImgPath());

        MyViewHolder myViewHolder = new MyViewHolder(linearLayout);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {         // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
        ChatData chat = chatList.get(position);

        get_SingletonUserInfo();
        FindDestTempFilePath();

//        // chat에 저장되어있는 닉네임 형식은 Uid_Nick이기 때문에 잘라서 Nick만 저장
//        String nickname = chat.getNickname();

        // 상대방의 Nick, 가장 최근 메시지, 메세지 전송 시간, 상대방의 img를 setting

        holder.msgText.setText(chat.getMsg());
        holder.dateText.setText(chat.getNowDate());

        Log.i("jiseong", "destTempFilePath" + destTempFilePath);
        holder.img_View.setImageURI(Uri.parse(destTempFilePath));

        // 만약에 사용자이름과 데이터베이스에 저장된 이름이 같을 시 오른쪽 정렬하고 아닐 시 왼쪽으로 정렬하여
        // 자신이 보낸 채팅과 남이 보낸 채팅을 구별한다.
        if (chat.getNickname().equals(this.my_uid)) {
            holder.nameText.setText(MyUid);

            holder.nameText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            holder.msgText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            holder.dateText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

            holder.msgLinear.setGravity(Gravity.RIGHT);
            holder.img_View.setVisibility(View.GONE);

        } else {
            holder.nameText.setText(destNick);

            holder.nameText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            holder.msgText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            holder.dateText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);

            holder.msgLinear.setGravity(Gravity.LEFT);
            holder.img_View.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return chatList == null ? 0 : chatList.size();
    }      // 전체 아이템 갯수 리턴.

    public void addChat(ChatData chat) {
        chatList.add(chat);

        // position의 위치에 새로 삽입된 아이템이 있음을 옵저버에 알려 반영한다.
        // 알리게 되면 ChatAdapter에서 해당 view item을 그린다.
        notifyItemInserted(chatList.size() - 1);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        public TextView msgText;
        public TextView dateText;
        public CircleImageView img_View;
        public LinearLayout msgLinear;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.TextView_nickname);
            msgText = itemView.findViewById(R.id.TextView_msg);
            dateText = itemView.findViewById(R.id.TextView_date);
            img_View = itemView.findViewById(R.id.img_View_civ);
            msgLinear = itemView.findViewById(R.id.msgLinear);

            itemView.setEnabled(true);
            itemView.setClickable(true);
        }
    }

    // 싱글톤 객체에서 필요한 정보 불러오기
    void get_SingletonUserInfo() {

        MyUid = singletonUserInfo.getMyNick();
        destList = singletonUserInfo.getDestList();
    }

    // 싱글톤 destList에 저장된 알맞은 상대방 TempFilePath 정보 가져오기
    void FindDestTempFilePath() {

        // CR_Id(채팅방 ID)에서 검출한 상대방 Uid 및 Nick
        destUid = ChatActivity.destUid;
        Log.i("lcs", "destUid chatAdapter: " + destUid);


        // 상대방의 정보가 담긴 destList의 Nick과 Uid를 CR_Id(채팅방 ID)에서 검출한 상대방 정보와 비교해서 같으면 상대방 이미지를 가져온다.
        for (int i = 0; i < destList.size(); i++) {
            if (destList.get(i).getDestUid().equals(destUid)) {
                Log.i("lcs", "for문 in");
                destTempFilePath = destList.get(i).getDestImgPath();
                destNick = destList.get(i).getDestNick();
                break;
            }
        }
    }
}