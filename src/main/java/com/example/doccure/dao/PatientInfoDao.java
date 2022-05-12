package com.example.doccure.dao;

import com.example.doccure.entity.HealthData;
import com.example.doccure.entity.VisitInfo;
import com.example.doccure.entity.info.PatientInfo;
import com.example.doccure.entity.resume.HealthBasicInfo;
import com.example.doccure.entity.resume.HealthExamInfo;
import com.example.doccure.entity.resume.HealthResume;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Mapper
public interface PatientInfoDao {
    void insertPatientInfo(PatientInfo patientInfo);
    void updatePatientInfo(@Param("email") String email, @Param("patientInfo") PatientInfo patientInfo);
    PatientInfo getPatientInfoByE(String email);
    List<PatientInfo> getAllPatientInfo();

    //健康基本信息
    void insertBasicInfo(HealthBasicInfo healthBasicInfo);
    HealthBasicInfo getHealthBasicInfoByPE(String patient_email);
    void updateHealthBasicInfoByPE_Info(String patient_email,HealthBasicInfo healthBasicInfo);
    void deleteHealthBasicInfoByPE(String patient_email);

    //履历信息
    void insertResume(HealthResume healthResume);
    HealthResume getHealthResumeById(int id);
    List<HealthResume> getHealthResumeByPE(String patient_email);
    void updateHealthResumeBy(int id,HealthResume healthResume);
    void deleteHealthResume(int id);

    //体检信息
    void insertExam(HealthExamInfo healthExamInfo);
    HealthExamInfo getHealthExamInfoById(int id);
    List<HealthExamInfo> getHealthExamInfoByPE(String patient_email);
    void updateHealthExamInfoByIdInfo(int id,HealthExamInfo healthExamInfo);
    void deleteHealthExamInfoById(int id);

    //家访数据
    void insertVisit(VisitInfo visitInfo);
    VisitInfo getVisitInfoById(int id);
    List<VisitInfo> getVisitInfoByPE(String patient_email);
    List<VisitInfo> getVisitInfoByDPE(String doctor_email,String patient_email);
    void updateVisitInfoByIdInfo(int id,VisitInfo visitInfo);
    void deleteVisitInfoById(int id);
    List<VisitInfo> getAllVisitByPE_OtherDE(String patient_email, String doctor_email);
    void updateVisitInfoIsReadByIdIsRead(int id,int is_read);
    //健康数据
    void insertData(HealthData healthData);
    HealthData getHealthDataById(int id);
    List<HealthData> getHealthDataListByPE(String patient_email);
    List<HealthData> getHealthDataListByPETypeTime(String patient_email,String health_type,String beginTime,String endTime,int internal);
    Map<Timestamp,String> getSingleDataByPETypeTime(String patient_email,String health_type,String beginTime,String endTime);

    //病人信息统计

}
