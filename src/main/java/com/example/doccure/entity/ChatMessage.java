package com.example.doccure.entity;

import com.example.doccure.utils.Constant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Objects;

@Entity
@Table(name = "chat_data")
public class ChatMessage {
    @Id
    private Integer id;
    private String from_email;
    private String to_email;
    private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp time;
    private Integer is_last;

    public ChatMessage() {
    }

    public ChatMessage(String from_email, String to_email, String content, Timestamp time, Integer is_last) {
        this.from_email = from_email;
        this.to_email = to_email;
        this.content = content;
        this.time = time;
        this.is_last = is_last;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Integer getIs_last() {
        return is_last;
    }

    public void setIs_last(Integer is_last) {
        this.is_last = is_last;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage that = (ChatMessage) o;
        return id.equals(that.id) && from_email.equals(that.from_email) && to_email.equals(that.to_email) && Objects.equals(content, that.content) && Objects.equals(time, that.time) && Objects.equals(is_last, that.is_last);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, from_email, to_email, content, time, is_last);
    }

    public static String getJsonString(ChatMessage chatMessage){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(Constant.timePatternTimeStamp));
        try {
            return objectMapper.writeValueAsString(chatMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "id=" + id +
                ", from_email='" + from_email + '\'' +
                ", to_email='" + to_email + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", is_last=" + is_last +
                '}';
    }
}
