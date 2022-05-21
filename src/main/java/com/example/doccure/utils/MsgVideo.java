package com.example.doccure.utils;

public class MsgVideo {
    private String msg;
    private String type;
    private String toUser;
    private String fromUser;
    private String iceCandidate;
    private String sdp;

    public MsgVideo() {
    }

    public MsgVideo(String msg, String type, String toUser, String fromUser, String iceCandidate,String sdp) {
        this.msg = msg;
        this.type = type;
        this.toUser = toUser;
        this.fromUser = fromUser;
        this.iceCandidate = iceCandidate;
        this.sdp = sdp;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSdp() {
        return sdp;
    }

    public void setSdp(String sdp) {
        this.sdp = sdp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getIceCandidate() {
        return iceCandidate;
    }

    public void setIceCandidate(String iceCandidate) {
        this.iceCandidate = iceCandidate;
    }

    @Override
    public String toString() {
        return "MsgVideo{" +
                "msg='" + msg + '\'' +
                ", type='" + type + '\'' +
                ", toUser='" + toUser + '\'' +
                ", fromUser='" + fromUser + '\'' +
                ", iceCandidate='" + iceCandidate + '\'' +
                ", sdp='" + sdp + '\'' +
                '}';
    }
}
