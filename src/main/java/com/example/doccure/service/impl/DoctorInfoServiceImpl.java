package com.example.doccure.service.impl;

import com.example.doccure.dao.DoctorInfoDao;
import com.example.doccure.dao.UserDao;
import com.example.doccure.entity.*;
import com.example.doccure.entity.doctor_exp_edu.DoctorEdu;
import com.example.doccure.entity.doctor_exp_edu.DoctorExp;
import com.example.doccure.entity.info.DoctorInfo;
import com.example.doccure.service.DoctorInfoService;
import com.example.doccure.utils.Constant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorInfoServiceImpl implements DoctorInfoService {
    @Autowired
    DoctorInfoDao doctorInfoDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    ObjectMapper objectMapper;
    //存储路径
    @Value("${file.upload.path}")
    private String filePath;
    //访问路径前缀
    @Value("${file.access.path}")
    private String accessPath;

    @Override
    public void deleteEduByE(String email) {
        doctorInfoDao.deleteEduByE(email);
    }

    @Override
    public void deleteExpByE(String email){
        doctorInfoDao.deleteExpByE(email);
    }

    @Override
    public void insertEdu(DoctorEdu doctorEdu){
        doctorInfoDao.insertEdu(doctorEdu);
    }

    @Override
    public void insertExp(DoctorExp doctorExp){
        doctorInfoDao.insertExp(doctorExp);
    }

    @Override
    public List<DoctorEdu> getAllDoctorEduByE(String email) {
        return doctorInfoDao.getAllDoctorEduByE(email);
    }

    @Override
    public List<DoctorExp> getAllDoctorExpByE(String email) {
        return doctorInfoDao.getAllDoctorExpByE(email);
    }

    /**
     * 得到所有医生
     * @return 信息
     */
    @Override
    public List<DoctorInfo> getAllDoctor() {
        return doctorInfoDao.getAllDoctorInfo();
    }

    /**
     * @param user 用户  待改
     * @param infoBasic 前端用户修改数据
     * @param image_url 图片上传地址
     */
    @Override
    public DoctorInfo SaveInfo(User user, String infoBasic, String infoEdu, String infoExp,String image_url){
        DoctorInfo doctorInfo = new DoctorInfo();
            try {
                //infoBasic
                objectMapper.setDateFormat(new SimpleDateFormat(Constant.timePatternDate));
                doctorInfo = objectMapper.readValue(infoBasic,DoctorInfo.class);
                user.setUsername(doctorInfo.getUsername());

                doctorInfo.setImage_url(image_url);

                //更新用户表
                userDao.updateUsername(user.getEmail(),user.getPassword(), doctorInfo.getUsername());
                //更新信息表
                if (doctorInfoDao.getDoctorInfoByE(user.getEmail())==null){
                    doctorInfoDao.insertDoctorInfo(doctorInfo);
                }
                else {
                    doctorInfoDao.updateDoctorInfo(user.getEmail(),doctorInfo);
                }

                //教育经历
                JsonNode jsonNode2 = objectMapper.readTree(infoEdu);
                doctorInfoDao.deleteEduByE(user.getEmail());
                for (int i = 0;i < jsonNode2.size();i++){
                    DoctorEdu doctorEdu = new DoctorEdu(user.getEmail(),jsonNode2.get(i).get("degree").asText(),
                            jsonNode2.get(i).get("college").asText(),jsonNode2.get(i).get("year_complete").asText());
                    doctorInfoDao.insertEdu(doctorEdu);
                }
                //工作经历
                JsonNode jsonNode3 = objectMapper.readTree(infoExp);
                doctorInfoDao.deleteExpByE(user.getEmail());
                for (int i = 0;i < jsonNode3.size();i++){
                    DoctorExp doctorExp = new DoctorExp(user.getEmail(),jsonNode3.get(i).get("hospital").asText()
                            ,jsonNode3.get(i).get("from_time").asText(),jsonNode3.get(i).get("to_time").asText(),
                            jsonNode3.get(i).get("designation").asText());
                    doctorInfoDao.insertExp(doctorExp);
                }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return doctorInfo;
    }


    /**
     * 根据邮箱得到信息
     */
    @Override
    public DoctorInfo getDoctorInfoByE(String email) {
        return doctorInfoDao.getDoctorInfoByE(email);
    }

    @Override
    public List<DoctorInfo> getAllDoctorInfo() {
        return doctorInfoDao.getAllDoctorInfo();
    }

    /**
     * 插入信息
     */
    @Override
    public void insertDoctorInfo(DoctorInfo doctorInfo) {
        doctorInfoDao.insertDoctorInfo(doctorInfo);
    }

    /**
     * 根据邮箱和密码修改信息
     */
    @Override
    public void updateDoctorInfo(String email,DoctorInfo doctorInfo) {
        doctorInfoDao.updateDoctorInfo(email,doctorInfo);
    }

    /**
     * 根据医生病人关系 得到医生信息列表
     * @param doctorPatientList 医生列表
     * @return 信息列表
     */
    @Override
    public List<DoctorInfo> getDoctorInfoByDPList(List<DoctorPatient> doctorPatientList) {
        List<DoctorInfo> doctorInfoList = new ArrayList<>();
        for (DoctorPatient doctorPatient: doctorPatientList){
            doctorInfoList.add(doctorInfoDao.getDoctorInfoByE(doctorPatient.getDoctor_email()));
        }
        return doctorInfoList;
    }

    /**
     * 整合医生学历信息
     * @param doctorEduList 教育经历列表
     * @return 学历信息字符串
     */
    @Override
    public String getEdu(List<DoctorEdu> doctorEduList){
        StringBuilder edu = new StringBuilder();
        for (int i = 0;i < doctorEduList.size();i++){
            if (i == doctorEduList.size()-1){
                edu.append(doctorEduList.get(i).getDegree()).append(" ").append(
                        doctorEduList.get(i).getCollege()
                );
                break;
            }
            edu.append(doctorEduList.get(i).getDegree()).append("&");
        }
        return edu.toString();
    }

    /**
     * 根据病人的预约信息得到医生信息
     * @param appointments 预约列表
     * @return 医生信息列表
     */
    @Override
    public List<DoctorInfo> getAllDoctorInfoByAppoint(List<Appointment> appointments){
        List<DoctorInfo> doctorInfos = new ArrayList<>();
        for (Appointment appointment : appointments){
            DoctorInfo doctorInfo = doctorInfoDao.getDoctorInfoByE(appointment.getDoctor_email());
            doctorInfos.add(doctorInfo);
        }
        return doctorInfos;
    }

    @Override
    public List<DoctorInfo> getDoctorInfoByAssesses(List<Assess> assesses){
        List<DoctorInfo> doctorInfos = new ArrayList<>();
        for (Assess assess:assesses){
            doctorInfos.add(doctorInfoDao.getDoctorInfoByE(assess.getDoctor_email()));
        }
        return doctorInfos;
    }

    @Override
    public List<DoctorInfo> getDoctorInfoByVisit(List<VisitInfo> visitInfos) {
        List<DoctorInfo> doctorInfos = new ArrayList<>();
        for (VisitInfo visitInfo:visitInfos){
            doctorInfos.add(doctorInfoDao.getDoctorInfoByE(visitInfo.getDoctor_email()));
        }
        return doctorInfos;
    }
}
