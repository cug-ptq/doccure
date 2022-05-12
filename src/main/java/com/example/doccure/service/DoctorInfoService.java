package com.example.doccure.service;

import com.example.doccure.entity.*;
import com.example.doccure.entity.doctor_exp_edu.DoctorEdu;
import com.example.doccure.entity.doctor_exp_edu.DoctorExp;
import com.example.doccure.entity.info.DoctorInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DoctorInfoService {

    //基本信息设置
    void deleteEduByE(String email);
    void deleteExpByE(String email);
    void insertEdu(DoctorEdu doctorEdu);
    void insertExp(DoctorExp doctorExp);
    List<DoctorEdu> getAllDoctorEduByE(String email);
    List<DoctorExp> getAllDoctorExpByE(String email);

    List<DoctorInfo> getAllDoctor();

    DoctorInfo SaveInfo(User user, String infoBasic, String infoEdu, String infoExp,String image_url);

    DoctorInfo getDoctorInfoByE(String email);

    List<DoctorInfo> getAllDoctorInfo();

    void insertDoctorInfo(DoctorInfo doctorInfo);

    void updateDoctorInfo(String email, DoctorInfo doctorInfo);

    /**
     * 医生病人
     */
    List<DoctorInfo> getDoctorInfoByDPList(List<DoctorPatient> doctorPatientList);

    String getEdu(List<DoctorEdu> doctorEduList);

    List<DoctorInfo> getAllDoctorInfoByAppoint(List<Appointment> appointments);

    List<DoctorInfo> getDoctorInfoByAssesses(List<Assess> assesses);

    List<DoctorInfo> getDoctorInfoByVisit(List<VisitInfo> visitInfos);
    //健康档案信息
    //.....
}
