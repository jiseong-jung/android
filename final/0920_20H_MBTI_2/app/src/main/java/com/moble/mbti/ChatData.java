package com.moble.mbti;

//DTO
public class ChatData {
    private String msg;
    private String nickname;
    private String nowDate;

    public ChatData() {
    }

    public ChatData(String msg, String nickname, String nowDate) {
        this.msg = msg;
        this.nickname = nickname;
        this.nowDate = nowDate;
    }

    public ChatData(String msg, String nickname) {
        this.msg = msg;
        this.nickname = nickname;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }
}


