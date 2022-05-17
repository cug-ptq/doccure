package com.example.doccure.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MsgVideo {
    private String type;
    private String toUser;
    private String fromUser;
    private String msg;
    private String sdp;
    private String iceCandidate;

    public MsgVideo() {
    }

    public MsgVideo(String type, String toUser, String fromUser, String msg,String sdp,String iceCandidate) {
        this.type = type;
        this.toUser = toUser;
        this.fromUser = fromUser;
        this.msg = msg;
        this.sdp = sdp;
        this.iceCandidate = iceCandidate;
    }

    public String getIceCandidate() {
        return iceCandidate;
    }

    public void setIceCandidate(String iceCandidate) {
        this.iceCandidate = iceCandidate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSdp() {
        return sdp;
    }

    public void setSdp(String sdp) {
        this.sdp = sdp;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static String getJsonString(MsgVideo msgVideo){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(msgVideo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String toString() {
        return "MsgVideo{" +
                "type='" + type + '\'' +
                ", toUser='" + toUser + '\'' +
                ", fromUser='" + fromUser + '\'' +
                ", msg='" + msg + '\'' +
                ", sdp='" + sdp + '\'' +
                ", iceCandidate='" + iceCandidate + '\'' +
                '}';
    }
}
