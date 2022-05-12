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
@Table(name = "health_data")
public class HealthData {
    @Id
    private Integer id;
    private String patient_email;
    private String health_type;
    private String health_data1;
    private String health_data2;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp time;

    public HealthData() {
    }

    public HealthData(String patient_email, String health_type, String health_data1, String health_data2, Timestamp time) {
        this.patient_email = patient_email;
        this.health_type = health_type;
        this.health_data1 = health_data1;
        this.health_data2 = health_data2;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPatient_email() {
        return patient_email;
    }

    public void setPatient_email(String patient_email) {
        this.patient_email = patient_email;
    }

    public String getHealth_type() {
        return health_type;
    }

    public void setHealth_type(String health_type) {
        this.health_type = health_type;
    }

    public String getHealth_data1() {
        return health_data1;
    }

    public void setHealth_data1(String health_data1) {
        this.health_data1 = health_data1;
    }

    public String getHealth_data2() {
        return health_data2;
    }

    public void setHealth_data2(String health_data2) {
        this.health_data2 = health_data2;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "HealthData{" +
                "id=" + id +
                ", patient_email='" + patient_email + '\'' +
                ", health_type='" + health_type + '\'' +
                ", health_data1='" + health_data1 + '\'' +
                ", health_data2='" + health_data2 + '\'' +
                ", time=" + time +
                '}';
    }

    public static String getJsonString(HealthData healthData){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(Constant.timePatternTimeStamp));
        try {
            return objectMapper.writeValueAsString(healthData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
