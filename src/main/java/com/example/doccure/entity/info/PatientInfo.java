package com.example.doccure.entity.info;

import com.example.doccure.utils.Constant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Table(name = "patient_info")
public class PatientInfo{
    @Id
    private String email;
    private String telephone;
    private String gender;
    private String image_url;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth;
    private String address;
    private String username;

    public PatientInfo() {}

    public PatientInfo(String email, String telephone, String gender,
                       String image_url, Date birth, String address, String username) {
        this.email = email;
        this.telephone = telephone;
        this.gender = gender;
        this.image_url = image_url;
        this.birth = birth;
        this.address = address;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientInfo that = (PatientInfo) o;
        return email.equals(that.email) && Objects.equals(telephone, that.telephone) && Objects.equals(gender, that.gender) && Objects.equals(image_url, that.image_url) && Objects.equals(birth, that.birth) && Objects.equals(address, that.address) && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, telephone, gender, image_url, birth, address, username);
    }

    @Override
    public String toString() {
        return "PatientInfo{" +
                "email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", gender='" + gender + '\'' +
                ", image_url='" + image_url + '\'' +
                ", birth=" + birth +
                ", address='" + address + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public static String getJson(PatientInfo patientInfo){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(Constant.timePatternDate));
        String data = "";
        try {
            data = objectMapper.writeValueAsString(patientInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return data;
    }

    public String getFilterEmail(){
        String regEx="[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(this.email);
        return matcher.replaceAll("").trim();
    }
}
