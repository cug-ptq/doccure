package com.example.doccure.dao;

import com.example.doccure.entity.doctor_exp_edu.DoctorEdu;
import com.example.doccure.entity.doctor_exp_edu.DoctorExp;
import com.example.doccure.entity.info.DoctorInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DoctorInfoDao {
    void deleteDoctorInfo(String email);
    void insertDoctorInfo(DoctorInfo doctorInfo);
    void updateDoctorInfo(@Param("email") String email,
                          @Param("doctorInfo") DoctorInfo doctorInfo);
    DoctorInfo getDoctorInfoByE(String email);

    List<DoctorInfo> getAllDoctorInfo();

    //教育 工作经历信息
    void deleteEduByE(String email);
    void deleteExpByE(String email);
    void insertEdu(DoctorEdu doctorEdu);
    void insertExp(DoctorExp doctorExp);
    List<DoctorEdu> getAllDoctorEduByE(String email);
    List<DoctorExp> getAllDoctorExpByE(String email);

}
