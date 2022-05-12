package com.example.doccure.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * 医生-病人关系
 */
@Entity
@Table(name = "doctor_patients")
public class DoctorPatient implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String doctor_email;
    private String patient_email;

    public DoctorPatient() {
    }

    public DoctorPatient(String doctor_email, String patient_email) {
        this.doctor_email = doctor_email;
        this.patient_email = patient_email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @Override
    public String toString() {
        return "DoctorPatient{" +
                "id=" + id +
                ", doctor_email='" + doctor_email + '\'' +
                ", patient_email='" + patient_email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorPatient that = (DoctorPatient) o;
        return id.equals(that.id) && doctor_email.equals(that.doctor_email) && Objects.equals(patient_email, that.patient_email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, doctor_email, patient_email);
    }
}
