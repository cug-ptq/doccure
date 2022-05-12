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
@Table(name = "appointment")
public class Appointment implements Serializable {
    @Id
    private Integer id;
    private String doctor_email;
    private String patient_email;
    private String appoint_content;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp appoint_time;
    private String appoint_result;
    private String appoint_type;
    private int is_read;

    public Appointment() {}

    public Appointment(String doctor_email, String patient_email,
                       String appoint_content, Timestamp appoint_time,
                       String appoint_result, String appoint_type, int is_read) {
        this.doctor_email = doctor_email;
        this.patient_email = patient_email;
        this.appoint_content = appoint_content;
        this.appoint_time = appoint_time;
        this.appoint_result = appoint_result;
        this.appoint_type = appoint_type;
        this.is_read = is_read;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public String getAppoint_type() {
        return appoint_type;
    }

    public void setAppoint_type(String appoint_type) {
        this.appoint_type = appoint_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getAppoint_content() {
        return appoint_content;
    }

    public void setAppoint_content(String appoint_content) {
        this.appoint_content = appoint_content;
    }

    public Timestamp getAppoint_time() {
        return appoint_time;
    }

    public void setAppoint_time(Timestamp appoint_time) {
        this.appoint_time = appoint_time;
    }

    public String getAppoint_result() {
        return appoint_result;
    }

    public void setAppoint_result(String appoint_result) {
        this.appoint_result = appoint_result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return appoint_result.equals(that.appoint_result) && id.equals(that.id) && doctor_email.equals(that.doctor_email)
                && patient_email.equals(that.patient_email)
                && Objects.equals(appoint_content, that.appoint_content)
                && Objects.equals(appoint_time, that.appoint_time)
                && Objects.equals(appoint_type,that.appoint_type)
                && Objects.equals(is_read,that.is_read);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, doctor_email, patient_email, appoint_content, appoint_time, appoint_result, appoint_type, is_read);
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", doctor_email='" + doctor_email + '\'' +
                ", patient_email='" + patient_email + '\'' +
                ", appoint_content='" + appoint_content + '\'' +
                ", appoint_time='" + appoint_time + '\'' +
                ", appoint_result=" + appoint_result +
                ", appoint_type="+ appoint_type +
                ", is_read="+ is_read +
                '}';
    }

    public static String getJson(Appointment appointment){
        ObjectMapper objectMapper = new ObjectMapper();
        String data = "";
        objectMapper.setDateFormat(new SimpleDateFormat(Constant.timePatternTimeStamp));
        try {
            data = objectMapper.writeValueAsString(appointment);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return data;
    }
}
