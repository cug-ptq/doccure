package com.example.doccure.service;

import com.example.doccure.entity.Assess;
import com.example.doccure.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AssessService {
    void insert(Assess assess);

    String deleteById(int id);

    Assess getAssessById(int id);

    Assess getAssessByDPE_Time(String doctor_email, String patient_email, String time);

    List<Assess> getAllAssessByPE(String patient_email);

    List<Assess> getAllAssessByPE_OtherDE(String patient_email,String doctor_email);

    List<Assess> getAssessByPE_IsRead(String patient_email, int is_read);

    List<Assess> getAssessByDE(String doctor_email);

    void updateIs_Read(int id, int is_read);

    String addAssess(User user, String assessInfo);

    String updateAssess(User user, String assessInfo);
}
