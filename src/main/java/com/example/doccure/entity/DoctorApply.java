package com.example.doccure.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "doctor_apply")
public class DoctorApply implements Serializable{
    @Id
    private Integer id;
    private String doctor_email;
    private String doctor_password;
    private String username;
    private String image_url;
    private String file_url;
    @DateTimeFormat(pattern = "yyyy-MM-ss HH:mm:ss")
    private Timestamp time;
    private int status;
    private String result;
    private String specialty;
    private int is_new;
    public DoctorApply() {
    }

    public DoctorApply(String username, String doctor_email, String doctor_password,int status,Timestamp time,
                       String image_url, String file_url, String result, String specialty,int is_new) {
        this.doctor_email = doctor_email;
        this.time = time;
        this.doctor_password = doctor_password;
        this.image_url = image_url;
        this.file_url = file_url;
        this.result = result;
        this.username = username;
        this.specialty = specialty;
        this.status = status;
        this.is_new = is_new;
    }

    public int getIs_new() {
        return is_new;
    }

    public void setIs_new(int is_new) {
        this.is_new = is_new;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getDoctor_password() {
        return doctor_password;
    }

    public void setDoctor_password(String doctor_password) {
        this.doctor_password = doctor_password;
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
    public String toString() {
        return "DoctorApply{" +
                "id=" + id +
                ", doctor_email='" + doctor_email + '\'' +
                ", doctor_password='" + doctor_password + '\'' +
                ", username='" + username + '\'' +
                ", image_url='" + image_url + '\'' +
                ", file_url='" + file_url + '\'' +
                ", time=" + time +
                ", status=" + status +
                ", result='" + result + '\'' +
                ", specialty='" + specialty + '\'' +
                ", is_new=" + is_new +
                '}';
    }
}
