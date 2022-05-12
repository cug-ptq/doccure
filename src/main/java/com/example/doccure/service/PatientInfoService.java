package com.example.doccure.service;

import com.example.doccure.entity.*;
import com.example.doccure.entity.info.PatientInfo;
import com.example.doccure.entity.resume.HealthBasicInfo;
import com.example.doccure.entity.resume.HealthExamInfo;
import com.example.doccure.entity.resume.HealthResume;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
public interface PatientInfoService {
    PatientInfo SaveInfo(User user, String info, String image_url);

    PatientInfo getPatientInfoByE(String email);

    void insertPatientInfo(PatientInfo patientInfo);

    void updatePatientInfo(String email,PatientInfo patientInfo);

    List<PatientInfo> getAllPatientInfo();

    /**
     * 医生病人
     */
    List<PatientInfo> getPatientInfoByDPList(List<DoctorPatient> doctorPatientList);

    List<PatientInfo> getAllPatientInfoByAppoint(List<Appointment> appointments);

    List<PatientInfo> getAllPatientInfoByVisit(List<VisitInfo> visitInfos);
    //健康档案信息
    //基本信息
    HealthBasicInfo getHealthBasicInfoByPE(String patient_email);
    String updateHealthBasicInfoByPE_Info(String patient_email,String healthBasicInfo);
    void deleteHealthBasicInfoByPE(String patient_email);

    //履历
    String insertResume(User user,String healthResumeInfo);
    HealthResume getHealthResumeById(int id);
    List<HealthResume> getHealthResumeByPE(String patient_email);
    String updateHealthResumeBy(int id,String healthResumeInfo);
    void deleteHealthResume(int id);

    //体检
    HealthExamInfo getHealthExamInfoById(int id);
    List<HealthExamInfo> getHealthExamInfoByPE(String patient_email);
    void deleteHealthExamInfoById(int id);
    String updateHealthExamInfoBy(int parseInt, int port, String examInfo, MultipartFile image, MultipartFile otherFile,HttpServletRequest request);
    String insertExam(User user,int port, String examInfo,MultipartFile image, MultipartFile otherFile,HttpServletRequest request);

    //家访信息
    String insertVisit(User user, String visitInfo);
    VisitInfo getVisitInfoById(int id);
    List<VisitInfo> getVisitInfoByPE(String patient_email);
    List<VisitInfo> getVisitInfoByDPE(String doctor_email,String patient_email);
    String updateVisitInfoByIdInfo(String visitInfo);
    void deleteVisitInfoById(int id);
    List<VisitInfo> getAllVisitByPE_OtherDE(String patient_email, String doctor_email);
    void updateVisitInfoIsReadByIdIsRead(int id,int is_read);

    //健康数据
    String insertData(String healthDataInfo);
    HealthData getHealthDataById(int id);
    List<HealthData> getHealthDataListByPE(String patient_email);
    List<HealthData> getHealthDataListByPETypeTime(String patient_email,String health_type,String beginTime,String endTime,int internal);
    Map<Timestamp,String> getSingleDataByPETypeTime(String patient_email, String health_type, String beginTime, String endTime);

}