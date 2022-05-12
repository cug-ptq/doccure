package com.example.doccure.entity.doctor_exp_edu;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "doctor_exp")
public class DoctorExp implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String hospital;
    private String from_time;
    private String to_time;
    private String designation;

    public DoctorExp() {
    }

    public DoctorExp(String email, String hospital, String from_time, String to_time, String designation) {
        this.email = email;
        this.hospital = hospital;
        this.from_time = from_time;
        this.to_time = to_time;
        this.designation = designation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getFrom_time() {
        return from_time;
    }

    public void setFrom_time(String from_time) {
        this.from_time = from_time;
    }

    public String getTo_time() {
        return to_time;
    }

    public void setTo_time(String to_time) {
        this.to_time = to_time;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @Override
    public String toString() {
        return "DoctorExp{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", hospital='" + hospital + '\'' +
                ", from_time='" + from_time + '\'' +
                ", to_time='" + to_time + '\'' +
                ", designation='" + designation + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorExp doctorExp = (DoctorExp) o;
        return id.equals(doctorExp.id) && email.equals(doctorExp.email) && Objects.equals(hospital, doctorExp.hospital) && Objects.equals(from_time, doctorExp.from_time) && Objects.equals(to_time, doctorExp.to_time) && Objects.equals(designation, doctorExp.designation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, hospital, from_time, to_time, designation);
    }
}
