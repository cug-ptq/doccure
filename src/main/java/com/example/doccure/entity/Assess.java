package com.example.doccure.entity;

import com.example.doccure.utils.Constant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Objects;


@Entity
@Table
public class Assess implements Serializable {
    @Id
    private Integer id;
    private String patient_email;
    private String doctor_email;
    private String description;
    private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp time;
    private int is_read;
    public Assess() {
        is_read = Constant.unread;
    }

    public Assess(String patient_email, String doctor_email, String description, String content, Timestamp time, int is_read) {
        this.patient_email = patient_email;
        this.doctor_email = doctor_email;
        this.description = description;
        this.content = content;
        this.time = time;
        this.is_read = is_read;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatient_email() {
        return patient_email;
    }

    public void setPatient_email(String patient_email) {
        this.patient_email = patient_email;
    }

    public String getDoctor_email() {
        return doctor_email;
    }

    public void setDoctor_email(String doctor_email) {
        this.doctor_email = doctor_email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    @Override
    public String toString() {
        return "Assess{" +
                "id=" + id +
                ", patient_email='" + patient_email + '\'' +
                ", doctor_email='" + doctor_email + '\'' +
                ", description=" + description + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", is_read=" + is_read +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assess assess = (Assess) o;
        return id.equals(assess.id) && patient_email.equals(assess.patient_email) && doctor_email.equals(assess.doctor_email)
                && Objects.equals(description,assess.description)
                && Objects.equals(content, assess.content)
                && Objects.equals(time, assess.time)
                && Objects.equals(is_read,assess.is_read);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patient_email, doctor_email, description, content, time);
    }

    public static String getJson(Assess assess){
        ObjectMapper objectMapper = new ObjectMapper();
        String data = "";
        objectMapper.setDateFormat(new SimpleDateFormat(Constant.timePatternTimeStamp));
        try {
            data = objectMapper.writeValueAsString(assess);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return data;
    }
}
