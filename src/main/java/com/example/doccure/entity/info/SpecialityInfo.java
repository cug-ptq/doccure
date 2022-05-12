package com.example.doccure.entity.info;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "speciality_info")
public class SpecialityInfo {
    @Id
    private Integer id;
    private String speciality;
    private String image_url;
    public SpecialityInfo() {
    }

    public SpecialityInfo(Integer id, String specialty, String image_url) {
        this.id = id;
        this.speciality = specialty;
        this.image_url = image_url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public static String getJsonString(SpecialityInfo specialityInfo){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(specialityInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
