package com.example.doccure.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;


//主键 from to 确定双方有无聊天关系
//插入是只要用户email存在这两个中，就不需要插入
@Table(name = "chat_list")
@Entity
public class ChatListInfo implements Serializable {
    @Id
    private String from_email;
    @Id
    private String to_email;
    private Integer status;
    private Integer unread_num;

    public ChatListInfo() {
    }

    public ChatListInfo(String from_email, String to_email, Integer status, Integer unread_num) {
        this.from_email = from_email;
        this.to_email = to_email;
        this.status = status;
        this.unread_num = unread_num;
    }

    public String getFrom_email() {
        return from_email;
    }

    public void setFrom_email(String from_email) {
        this.from_email = from_email;
    }

    public String getTo_email() {
        return to_email;
    }

    public void setTo_email(String to_email) {
        this.to_email = to_email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUnread_num() {
        return unread_num;
    }

    public void setUnread_num(Integer unread_num) {
        this.unread_num = unread_num;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatListInfo that = (ChatListInfo) o;
        return from_email.equals(that.from_email) && to_email.equals(that.to_email) && Objects.equals(status, that.status) && Objects.equals(unread_num, that.unread_num);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from_email, to_email, status, unread_num);
    }

    public static String getJsonString(ChatListInfo chatListInfo){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(chatListInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
