package com.example.doccure.entity.resume;

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

@Table(name = "health_exam")
@Entity
public class HealthExamInfo {
    @Id
    private Integer id;
    private String patient_email;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp time;
    private String hospital;
    private String option_exam;
    private String value_exam;
    private String description;
    private String image_url;
    private String file_url;

    public HealthExamInfo() {
    }

    public HealthExamInfo(String patient_email, Timestamp time, String hospital, String option_exam, String value_exam, String description, String image_url, String file_url) {
        this.patient_email = patient_email;
        this.time = time;
        this.hospital = hospital;
        this.option_exam = option_exam;
        this.value_exam = value_exam;
        this.description = description;
        this.image_url = image_url;
        this.file_url = file_url;
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

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getOption_exam() {
        return option_exam;
    }

    public void setOption_exam(String option_exam) {
        this.option_exam = option_exam;
    }

    public String getValue_exam() {
        return value_exam;
    }

    public void setValue_exam(String value_exam) {
        this.value_exam = value_exam;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HealthExamInfo that = (HealthExamInfo) o;
        return id.equals(that.id) && patient_email.equals(that.patient_email) && Objects.equals(time, that.time) && Objects.equals(hospital, that.hospital) && Objects.equals(option_exam, that.option_exam) && Objects.equals(value_exam, that.value_exam) && Objects.equals(description, that.description) && Objects.equals(image_url, that.image_url) && Objects.equals(file_url, that.file_url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patient_email, time, hospital, option_exam, value_exam, description, image_url, file_url);
    }

    public static String getJsonString(HealthExamInfo healthExamInfo){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(Constant.timePatternTimeStamp));
        try {
            return objectMapper.writeValueAsString(healthExamInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
