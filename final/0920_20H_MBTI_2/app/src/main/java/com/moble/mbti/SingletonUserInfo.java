package com.moble.mbti;

import android.net.Uri;

import java.io.File;
import java.util.ArrayList;

public class SingletonUserInfo {

    private String MyUid;                       //자신의 UID
    private String MyNick;                      //자신의 닉네임
    private String MyMbti;                      //자신의 MBTI
    private String MyImgPath;                   //자신의 이미지가 저장된 경로
    private String MyRC_Check;                  //Random Chatting - CheckBox (True or False)
    private ArrayList<DestUserInfo> destList;   //상대방 정보를 담는 클래스
    private File storage;

    private static SingletonUserInfo userInfo = new SingletonUserInfo();            //객체 생성 (선언)

    public static SingletonUserInfo getInstance() {                                 //만들어진 객체를 반환
        if (userInfo == null) {
            userInfo = new SingletonUserInfo();
        }
        return userInfo;
    }

    private SingletonUserInfo() {                                                   //생성자
        destList = new ArrayList<DestUserInfo>();
    }

    public String getMyNick() {
        return MyNick;
    }

    public void setMyNick(String myNick) {
        MyNick = myNick;
    }

    public String getMyUid() {
        return MyUid;
    }

    public void setMyUid(String myUid) {
        MyUid = myUid;
    }

    public String getMyImgPath() {
        return MyImgPath;
    }

    public void setMyImgPath(String myImgPath) {
        MyImgPath = myImgPath;
    }

    public String getMyRC_Check() {
        return MyRC_Check;
    }

    public void setMyRC_Check(String myRC_Check) {
        MyRC_Check = myRC_Check;
    }

    public String getMyMbti() {
        return MyMbti;
    }

    public void setMyMbti(String myMbti) {
        MyMbti = myMbti;
    }

    public ArrayList<DestUserInfo> getDestList() {
        return destList;
    }

    public void setDestList(DestUserInfo destUserInfo) {
        destList.add(destUserInfo);
    }



    public File getStorage() {
        return storage;
    }

    public void setStorage(File storage) {
        this.storage = storage;
    }


}
