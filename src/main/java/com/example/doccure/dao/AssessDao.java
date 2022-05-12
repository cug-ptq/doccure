package com.example.doccure.dao;

import com.example.doccure.entity.Assess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AssessDao {
    void insert(Assess assess);

    void deleteById(int id);

    Assess getAssessById(int id);

    Assess getAssessByDPE_Time(String doctor_email, String patient_email, String time);

    List<Assess> getAllAssessByPE(String patient_email);

    List<Assess> getAllAssessByPE_OtherDE(String patient_email,String doctor_email);

    List<Assess> getAssessByPE_IsRead(String patient_email, int is_read);

    List<Assess> getAssessByDE(String doctor_email);

    void updateAssessById(int id, @Param("assess") Assess assess);

    void updateIs_Read(int id, int is_read);
}
