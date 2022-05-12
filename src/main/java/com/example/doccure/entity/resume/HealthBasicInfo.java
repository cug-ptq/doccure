package com.example.doccure.entity.resume;

import com.example.doccure.entity.health_basicInfo.AlcoholInfo;
import com.example.doccure.entity.health_basicInfo.NormalHealthyInfo;
import com.example.doccure.entity.health_basicInfo.PhysicalInfo;
import com.example.doccure.entity.health_basicInfo.SmokeInfo;

import java.util.Objects;

/**
 * 基本健康信息
 */

public class HealthBasicInfo {
    private String patient_email;
    private NormalHealthyInfo normal_info;
    private SmokeInfo smoke_info;
    private AlcoholInfo alcohol_info;
    private PhysicalInfo physical_info;

    public HealthBasicInfo() {
        normal_info = new NormalHealthyInfo();
        smoke_info = new SmokeInfo();
        alcohol_info = new AlcoholInfo();
        physical_info = new PhysicalInfo();
    }

    public HealthBasicInfo(String patient_email, NormalHealthyInfo normal_info, SmokeInfo smoke_info, AlcoholInfo alcohol_info, PhysicalInfo physical_info) {
        this.patient_email = patient_email;
        this.normal_info = normal_info;
        this.smoke_info = smoke_info;
        this.alcohol_info = alcohol_info;
        this.physical_info = physical_info;
    }

    public String getPatient_email() {
        return patient_email;
    }

    public void setPatient_email(String patient_email) {
        this.patient_email = patient_email;
    }

    public NormalHealthyInfo getNormal_info() {
        return normal_info;
    }

    public void setNormal_info(NormalHealthyInfo normal_info) {
        this.normal_info = normal_info;
    }

    public SmokeInfo getSmoke_info() {
        return smoke_info;
    }

    public void setSmoke_info(SmokeInfo smoke_info) {
        this.smoke_info = smoke_info;
    }

    public AlcoholInfo getAlcohol_info() {
        return alcohol_info;
    }

    public void setAlcohol_info(AlcoholInfo alcohol_info) {
        this.alcohol_info = alcohol_info;
    }

    public PhysicalInfo getPhysical_info() {
        return physical_info;
    }

    public void setPhysical_info(PhysicalInfo physical_info) {
        this.physical_info = physical_info;
    }

    @Override
    public String toString() {
        return "HealthBasicInfo{" +
                "patient_email='" + patient_email + '\'' +
                ", normal_info=" + normal_info +
                ", smoke_info=" + smoke_info +
                ", alcohol_info=" + alcohol_info +
                ", physical_info=" + physical_info +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HealthBasicInfo that = (HealthBasicInfo) o;
        return patient_email.equals(that.patient_email) && Objects.equals(normal_info, that.normal_info) && Objects.equals(smoke_info, that.smoke_info) && Objects.equals(alcohol_info, that.alcohol_info) && Objects.equals(physical_info, that.physical_info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patient_email, normal_info, smoke_info, alcohol_info, physical_info);
    }
}
