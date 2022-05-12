package com.example.doccure.entity.info;

import com.example.doccure.utils.Constant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Table(name = "doctor_info")
public class DoctorInfo{
    @Id
    private String email;
    private String telephone;
    private String gender;
    private String image_url;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth;
    private String address;
    private String services;
    private String username;
    private String about_me;
    private String speciality;

    public DoctorInfo(){}

    public DoctorInfo(String email, String telephone, String gender, String image_url, Date birth,
                      String about_me, String address, String services, String username, String speciality) {
        this.email = email;
        this.telephone = telephone;
        this.gender = gender;
        this.image_url = image_url;
        this.birth = birth;
        this.address = address;
        this.services = services;
        this.username = username;
        this.about_me = about_me;
        this.speciality = speciality;
    }

    public String getAbout_me() {
        return about_me;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFilterEmail(){
        String regEx="[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(this.email);
        return matcher.replaceAll("").trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorInfo that = (DoctorInfo) o;
        return email.equals(that.email) && Objects.equals(telephone, that.telephone) && Objects.equals(gender, that.gender) && Objects.equals(image_url, that.image_url) && Objects.equals(birth, that.birth)
                && Objects.equals(address, that.address) && services.equals(that.services) && Objects.equals(username, that.username) && Objects.equals(speciality, that.speciality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, telephone, gender, image_url, birth, address, services, username, speciality);
    }

    @Override
    public String toString() {
        return "DoctorInfo{" +
                "email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", gender='" + gender + '\'' +
                ", image_url='" + image_url + '\'' +
                ", birth='" + birth + '\'' +
                ", address='" + address + '\'' +
                ", services='" + services + '\'' +
                ", username='" + username + '\'' +
                ", speciality='" + speciality + '\'' +
                '}';
    }

    public static String getJson(DoctorInfo doctorInfo){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(Constant.timePatternDate));
        String data = "";
        try {
            data = objectMapper.writeValueAsString(doctorInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return data;
    }
}
