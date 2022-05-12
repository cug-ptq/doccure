package com.example.doccure.entity.doctor_exp_edu;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "doctor_edu")
public class DoctorEdu implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String degree;
    private String college;
    private String year_complete;

    public DoctorEdu() {
    }

    public DoctorEdu(String email, String degree, String college, String year_complete) {
        this.email = email;
        this.degree = degree;
        this.college = college;
        this.year_complete = year_complete;
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

    @Override
    public String toString() {
        return "DoctorEdu{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", degree='" + degree + '\'' +
                ", college='" + college + '\'' +
                ", year_complete='" + year_complete + '\'' +
                '}';
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getYear_complete() {
        return year_complete;
    }

    public void setYear_complete(String year_complete) {
        this.year_complete = year_complete;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorEdu doctorEdu = (DoctorEdu) o;
        return id.equals(doctorEdu.id) && email.equals(doctorEdu.email) && degree.equals(doctorEdu.degree) && college.equals(doctorEdu.college) && year_complete.equals(doctorEdu.year_complete);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, degree, college, year_complete);
    }
}
