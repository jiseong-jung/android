package com.moble.mbti;

public class DestUserInfo {

    private String destNick;
    private String destUid;
    private String destImgPath;

    public DestUserInfo() {
    }

    public DestUserInfo(String destNick, String destUid, String destImgPath) {
        this.destNick = destNick;
        this.destUid = destUid;
        this.destImgPath = destImgPath;
    }

    public String getDestNick() {
        return destNick;
    }

    public void setDestNick(String destNick) {
        this.destNick = destNick;
    }

    public String getDestUid() {
        return destUid;
    }

    public void setDestUid(String destUid) {
        this.destUid = destUid;
    }

    public String getDestImgPath() {
        return destImgPath;
    }

    public void setDestImgPath(String destImgPath) {
        this.destImgPath = destImgPath;
    }
}
