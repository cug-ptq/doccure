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

@Entity
@Table(name = "visit")
public class VisitInfo {
    @Id
    private Integer id;
    private String doctor_email;
    private String patient_email;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp time;
    private String visit_result;
    private int is_read;
    public VisitInfo() {
    }

    public VisitInfo(String doctor_email, String patient_email, Timestamp time, String visit_result, int is_read) {
        this.doctor_email = doctor_email;
        this.patient_email = patient_email;
        this.time = time;
        this.visit_result = visit_result;
        this.is_read = is_read;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDoctor_email() {
        return doctor_email;
    }

    public void setDoctor_email(String doctor_email) {
        this.doctor_email = doctor_email;
    }

    public String getPatient_email() {
        return patient_email;
    }

    public void setPatient_email(String patient_email) {
        this.patient_email = patient_email;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getVisit_result() {
        return visit_result;
    }

    public void setVisit_result(String visit_result) {
        this.visit_result = visit_result;
    }

    @Override
    public String toString() {
        return "VisitInfo{" +
                "id=" + id +
                ", doctor_email='" + doctor_email + '\'' +
                ", patient_email='" + patient_email + '\'' +
                ", time=" + time +
                ", visit_result='" + visit_result + '\'' +
                ", is_read=" + is_read +
                '}';
    }

    public static String getJsonString(VisitInfo visitInfo){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(Constant.timePatternTimeStamp));
        try {
            return objectMapper.writeValueAsString(visitInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
