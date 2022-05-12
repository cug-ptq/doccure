package com.example.doccure.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class Msg {
    private int code;
    private String type;
    private String message;
    public Msg() {}

    public Msg(int code, String type, String message) {
        this.code = code;
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static String getMsgJsonCode(int code, String msg){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(new Msg(code,"",msg));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getMsgJsonType(String type, String msg){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(new Msg(1,type,msg));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getMsgJsonString(Msg msg){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(msg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String toString() {
        return "Msg{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
