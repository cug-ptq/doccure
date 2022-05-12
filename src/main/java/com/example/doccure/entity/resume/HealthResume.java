package com.example.doccure.entity.resume;

import com.example.doccure.utils.Constant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.Objects;

@Table(name = "health_record")
@Entity
public class HealthResume {
    @Id
    private Integer id;
    private String patient_email;
    private String health_type;
    private String health_describe;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date time;
    private String status;

    public HealthResume() {
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

    public String getHealth_describe() {
        return health_describe;
    }

    public void setHealth_describe(String health_describe) {
        this.health_describe = health_describe;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "HealthResume{" +
                "id=" + id +
                ", patient_email='" + patient_email + '\'' +
                ", health_type='" + health_type + '\'' +
                ", health_describe='" + health_describe + '\'' +
                ", time=" + time +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HealthResume that = (HealthResume) o;
        return id.equals(that.id) && patient_email.equals(that.patient_email) && Objects.equals(health_type, that.health_type) && Objects.equals(health_describe, that.health_describe) && Objects.equals(time, that.time) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patient_email, health_type, health_describe, time, status);
    }

    public static String getJsonString(HealthResume healthResume){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(Constant.timePatternDate));
        try {
            return objectMapper.writeValueAsString(healthResume);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
