package com.example.doccure.service.impl;

import com.example.doccure.dao.PatientInfoDao;
import com.example.doccure.dao.UserDao;
import com.example.doccure.entity.*;
import com.example.doccure.entity.health_basicInfo.AlcoholInfo;
import com.example.doccure.entity.health_basicInfo.NormalHealthyInfo;
import com.example.doccure.entity.health_basicInfo.PhysicalInfo;
import com.example.doccure.entity.health_basicInfo.SmokeInfo;
import com.example.doccure.entity.info.PatientInfo;
import com.example.doccure.entity.resume.HealthBasicInfo;
import com.example.doccure.entity.resume.HealthExamInfo;
import com.example.doccure.entity.resume.HealthResume;
import com.example.doccure.service.PatientInfoService;
import com.example.doccure.utils.Constant;
import com.example.doccure.utils.FileUtil;
import com.example.doccure.utils.Msg;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PatientInfoServiceImpl implements PatientInfoService {
    @Autowired
    PatientInfoDao patientInfoDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    //存储路径
    @Value("${file.upload.path}")
    private String filePath;
    //访问路径前缀
    @Value("${file.access.path}")
    private String accessPath;

    /**
     * @param user 用户
     * @param info 前端用户修改数据
     * @param image_url 图片上传地址
     */
    @Override
    public PatientInfo SaveInfo(User user, String info, String image_url){
        PatientInfo patientInfo = new PatientInfo();
        try {
            objectMapper.setDateFormat(new SimpleDateFormat(Constant.timePatternDate));
            patientInfo = objectMapper.readValue(info,PatientInfo.class);

            patientInfo.setImage_url(image_url);
            //更新用户表
            userDao.updateUsername(user.getEmail(),user.getPassword(), patientInfo.getUsername());
            //更新信息表
            if (patientInfoDao.getPatientInfoByE(user.getEmail())==null){
                patientInfoDao.insertPatientInfo(patientInfo);
            }
            else {
                patientInfoDao.updatePatientInfo((user.getEmail()),patientInfo);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return patientInfo;
    }

    @Override
    public PatientInfo getPatientInfoByE(String email) {
        return patientInfoDao.getPatientInfoByE(email);
    }

    /**
     * 插入信息
     */
    @Override
    public void insertPatientInfo(PatientInfo patientInfo) {
        patientInfoDao.insertPatientInfo(patientInfo);
    }

    /**
     * 根据邮箱和密码修改
     */
    @Override
    public void updatePatientInfo(String email, PatientInfo patientInfo) {
        patientInfoDao.updatePatientInfo(email,patientInfo);
    }


    @Override
    public List<PatientInfo> getAllPatientInfo() {
        return patientInfoDao.getAllPatientInfo();
    }

    /**
     * 医生病人
     */
    @Override
    public List<PatientInfo> getPatientInfoByDPList(List<DoctorPatient> doctorPatientList) {
        List<PatientInfo> patientInfoList = new ArrayList<>();
        for (DoctorPatient doctorPatient: doctorPatientList){
            patientInfoList.add(patientInfoDao.getPatientInfoByE(doctorPatient.getPatient_email()));
        }
        return patientInfoList;
    }

    /**
     * 根据医生的预约申请列表得到病人信息
     * @param appointments 预约列表
     * @return 病人信息列表
     */
    @Override
    public List<PatientInfo> getAllPatientInfoByAppoint(List<Appointment> appointments){
        List<PatientInfo> patientInfoList = new ArrayList<>();
        for (Appointment appointment : appointments){
            PatientInfo patientInfo = patientInfoDao.getPatientInfoByE(appointment.getPatient_email());
            patientInfoList.add(patientInfo);
        }
        return patientInfoList;
    }

    @Override
    public List<PatientInfo> getAllPatientInfoByVisit(List<VisitInfo> visitInfos) {
        List<PatientInfo> patientInfoList = new ArrayList<>();
        for (VisitInfo visitInfo : visitInfos){
            PatientInfo patientInfo = patientInfoDao.getPatientInfoByE(visitInfo.getPatient_email());
            patientInfoList.add(patientInfo);
        }
        return patientInfoList;
    }

    /**
     * 得到信息
     * @param patient_email patient
     * @return 信息对象
     */
    @Override
    public HealthBasicInfo getHealthBasicInfoByPE(String patient_email) {
        return patientInfoDao.getHealthBasicInfoByPE(patient_email);
    }

    /**
     * 更新信息
     * @param patient_email patient
     * @param healthBasicInfo 信息JSON
     * @return 操作结果
     */
    @Override
    public String updateHealthBasicInfoByPE_Info(String patient_email, String healthBasicInfo) {
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(healthBasicInfo);
            NormalHealthyInfo normalHealthyInfo = objectMapper.readValue(jsonNode.get("normal").asText(),NormalHealthyInfo.class);
            SmokeInfo smokeInfo = objectMapper.readValue(jsonNode.get("smoke").asText(),SmokeInfo.class);
            AlcoholInfo alcoholInfo = objectMapper.readValue(jsonNode.get("alcohol").asText(),AlcoholInfo.class);
            PhysicalInfo physicalInfo = objectMapper.readValue(jsonNode.get("physical").asText(),PhysicalInfo.class);
            HealthBasicInfo healthBasicInfo1 = new HealthBasicInfo(patient_email,normalHealthyInfo,smokeInfo,alcoholInfo,physicalInfo);
            if (patientInfoDao.getHealthBasicInfoByPE(patient_email)!=null){
                patientInfoDao.updateHealthBasicInfoByPE_Info(patient_email,healthBasicInfo1);
            }
            else {
                patientInfoDao.insertBasicInfo(healthBasicInfo1);
            }
            return Msg.getMsgJsonCode(1,"保存成功");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Msg.getMsgJsonCode(-1,"保存失败");
    }

    /**
     * 删除
     * @param patient_email patient
     */
    @Override
    public void deleteHealthBasicInfoByPE(String patient_email) {
        patientInfoDao.deleteHealthBasicInfoByPE(patient_email);
    }

    @Override
    public String insertResume(User user, String healthResumeInfo) {
        try {
            objectMapper.setDateFormat(new SimpleDateFormat(Constant.timePatternDate));
            HealthResume healthResume = objectMapper.readValue(healthResumeInfo,HealthResume.class);
            healthResume.setPatient_email(user.getEmail());
            patientInfoDao.insertResume(healthResume);
            return Msg.getMsgJsonCode(1,HealthResume.getJsonString(healthResume));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Msg.getMsgJsonCode(-1,"添加失败");
    }

    @Override
    public HealthResume getHealthResumeById(int id) {
        return patientInfoDao.getHealthResumeById(id);
    }

    @Override
    public List<HealthResume> getHealthResumeByPE(String patient_email) {
        return patientInfoDao.getHealthResumeByPE(patient_email);
    }

    @Override
    public String updateHealthResumeBy(int id, String healthResumeInfo) {
        try {
            objectMapper.setDateFormat(new SimpleDateFormat(Constant.timePatternDate));
            HealthResume healthResume = objectMapper.readValue(healthResumeInfo,HealthResume.class);
            patientInfoDao.updateHealthResumeBy(id,healthResume);
            healthResume.setId(id);
            return Msg.getMsgJsonCode(1,HealthResume.getJsonString(healthResume));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Msg.getMsgJsonCode(-1,"更新失败");
    }

    @Override
    public void deleteHealthResume(int id) {
        patientInfoDao.deleteHealthResume(id);
    }

    @Override
    public String insertExam(User user,int port, String examInfo,MultipartFile image, MultipartFile otherFile,HttpServletRequest request) {
        try {
            String image_url = "";String otherFile_url = "";
            if (image!=null){
                image_url = FileUtil.uploadFile(image, String.valueOf(port),filePath,accessPath,"",request);
            }
            if (otherFile!=null){
                otherFile_url = FileUtil.uploadFile(otherFile, String.valueOf(port),filePath,accessPath,"",request);
            }
            objectMapper.setDateFormat(new SimpleDateFormat(Constant.timePatternTimeStamp));
            System.out.println(examInfo);
            HealthExamInfo healthExamInfo = objectMapper.readValue(examInfo,HealthExamInfo.class);
            healthExamInfo.setPatient_email(user.getEmail());healthExamInfo.setImage_url(image_url);
            healthExamInfo.setFile_url(otherFile_url);
            patientInfoDao.insertExam(healthExamInfo);
            return Msg.getMsgJsonCode(1,HealthExamInfo.getJsonString(healthExamInfo));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return Msg.getMsgJsonCode(-1,"添加失败");
    }

    @Override
    public HealthExamInfo getHealthExamInfoById(int id) {
        return patientInfoDao.getHealthExamInfoById(id);
    }

    @Override
    public List<HealthExamInfo> getHealthExamInfoByPE(String patient_email) {
        return patientInfoDao.getHealthExamInfoByPE(patient_email);
    }

    @Override
    public String updateHealthExamInfoBy(int id, int port, String examInfo, MultipartFile image, MultipartFile otherFile,HttpServletRequest request) {
        try {
            String image_url = "";String otherFile_url = "";

            objectMapper.setDateFormat(new SimpleDateFormat(Constant.timePatternTimeStamp));
            HealthExamInfo healthExamInfo = objectMapper.readValue(examInfo,HealthExamInfo.class);
            if (image!=null){
                image_url = FileUtil.uploadFile(image, String.valueOf(port),filePath,accessPath,"",request);
            }
            else {
                image_url = healthExamInfo.getImage_url();
            }
            if (otherFile!=null){
                otherFile_url = FileUtil.uploadFile(otherFile, String.valueOf(port),filePath,accessPath,"",request);
            }
            else {
                otherFile_url = healthExamInfo.getFile_url();
            }
            healthExamInfo.setFile_url(otherFile_url);healthExamInfo.setImage_url(image_url);
            patientInfoDao.updateHealthExamInfoByIdInfo(id,healthExamInfo);
            healthExamInfo.setId(id);
            return Msg.getMsgJsonCode(1,HealthExamInfo.getJsonString(healthExamInfo));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Msg.getMsgJsonCode(-1,"修改失败");
    }

    @Override
    public void deleteHealthExamInfoById(int id) {
        patientInfoDao.deleteHealthExamInfoById(id);
    }

    //健康数据
    @Override
    public String insertData(String healthDataInfo) {
        objectMapper.setDateFormat(new SimpleDateFormat(Constant.timePatternTimeStamp));
        try {
            HealthData healthData = objectMapper.readValue(healthDataInfo,HealthData.class);
            patientInfoDao.insertData(healthData);
            return Msg.getMsgJsonCode(1,"插入成功");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Msg.getMsgJsonCode(-1,"插入失败");
    }

    @Override
    public HealthData getHealthDataById(int id) {
        return patientInfoDao.getHealthDataById(id);
    }

    @Override
    public List<HealthData> getHealthDataListByPE(String patient_email) {
        return patientInfoDao.getHealthDataListByPE(patient_email);
    }

    @Override
    public List<HealthData> getHealthDataListByPETypeTime(String patient_email, String health_type,String beginTime, String endTime,int internal) {
        return patientInfoDao.getHealthDataListByPETypeTime(patient_email,health_type,beginTime,endTime,internal);
    }

    @Override
    public Map<Timestamp, String> getSingleDataByPETypeTime(String patient_email, String health_type, String beginTime, String endTime) {
        return patientInfoDao.getSingleDataByPETypeTime(patient_email, health_type, beginTime, endTime);
    }

    //家访
    @Override
    public String insertVisit(User user, String visitInfo) {
        try {
            objectMapper.setDateFormat(new SimpleDateFormat(Constant.timePatternTimeStamp));
            VisitInfo visitInfo1 = objectMapper.readValue(visitInfo,VisitInfo.class);
            visitInfo1.setDoctor_email(user.getEmail());
            visitInfo1.setIs_read(Constant.unread);
            patientInfoDao.insertVisit(visitInfo1);
            return Msg.getMsgJsonCode(1,VisitInfo.getJsonString(visitInfo1));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Msg.getMsgJsonCode(-1,"添加失败");
    }

    @Override
    public VisitInfo getVisitInfoById(int id) {
        return patientInfoDao.getVisitInfoById(id);
    }

    @Override
    public List<VisitInfo> getVisitInfoByPE(String patient_email) {
        return patientInfoDao.getVisitInfoByPE(patient_email);
    }

    @Override
    public List<VisitInfo> getVisitInfoByDPE(String doctor_email, String patient_email) {
        return patientInfoDao.getVisitInfoByDPE(doctor_email, patient_email);
    }

    @Override
    public String updateVisitInfoByIdInfo(String visitInfo) {
        try {
            objectMapper.setDateFormat(new SimpleDateFormat(Constant.timePatternTimeStamp));
            VisitInfo visitInfo1 = objectMapper.readValue(visitInfo,VisitInfo.class);
            visitInfo1.setIs_read(Constant.unread);
            patientInfoDao.updateVisitInfoByIdInfo(visitInfo1.getId(),visitInfo1);
            System.out.println(visitInfo1);
            return Msg.getMsgJsonCode(1,VisitInfo.getJsonString(visitInfo1));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void deleteVisitInfoById(int id) {
        patientInfoDao.deleteVisitInfoById(id);
    }

    @Override
    public List<VisitInfo> getAllVisitByPE_OtherDE(String patient_email, String doctor_email) {
        return patientInfoDao.getAllVisitByPE_OtherDE(patient_email, doctor_email);
    }

    @Override
    public void updateVisitInfoIsReadByIdIsRead(int id, int is_read) {
        patientInfoDao.updateVisitInfoIsReadByIdIsRead(id,is_read);
    }
}
