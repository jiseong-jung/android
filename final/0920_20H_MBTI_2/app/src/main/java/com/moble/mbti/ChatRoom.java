package com.moble.mbti;

public class ChatRoom {
    String uid;
    String nickname;
    String message;

    public ChatRoom() {
    }

    public ChatRoom(String uid, String nickname, String message) {
        this.uid = uid;
        this.nickname = nickname;
        this.message = message;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
