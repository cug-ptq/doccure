package com.example.doccure.service.impl;

import com.example.doccure.dao.AssessDao;
import com.example.doccure.dao.DoctorInfoDao;
import com.example.doccure.entity.Assess;
import com.example.doccure.entity.info.DoctorInfo;
import com.example.doccure.entity.User;
import com.example.doccure.service.AssessService;
import com.example.doccure.utils.Constant;
import com.example.doccure.utils.Msg;
import com.example.doccure.utils.ServiceUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AssessServiceImpl implements AssessService {
    @Autowired
    private AssessDao assessDao;
    @Autowired
    private DoctorInfoDao doctorInfoDao;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void insert(Assess assess) {
        assessDao.insert(assess);
    }

    @Override
    public String deleteById(int id) {
        assessDao.deleteById(id);
        return Msg.getMsgJsonCode(1,"删除成功");
    }

    @Override
    public Assess getAssessById(int id){
        return assessDao.getAssessById(id);
    }

    @Override
    public Assess getAssessByDPE_Time(String doctor_email, String patient_email, String time) {
        return assessDao.getAssessByDPE_Time(doctor_email,patient_email,time);
    }

    @Override
    public List<Assess> getAllAssessByPE(String patient_email){
        return assessDao.getAllAssessByPE(patient_email);
    }

    @Override
    public List<Assess> getAllAssessByPE_OtherDE(String patient_email, String doctor_email) {
        return assessDao.getAllAssessByPE_OtherDE(patient_email,doctor_email);
    }

    @Override
    public List<Assess> getAssessByPE_IsRead(String patient_email, int is_read) {
        return assessDao.getAssessByPE_IsRead(patient_email,is_read);
    }

    @Override
    public List<Assess> getAssessByDE(String doctor_email) {
        return assessDao.getAssessByDE(doctor_email);
    }

    @Override
    public void updateIs_Read(int id, int is_read) {
        assessDao.updateIs_Read(id,is_read);
    }

    /**
     * 添加评论
     * @param user doctor
     * @param assessInfo 评估信息
     * @return assess doctorInfo
     */
    @Override
    public String addAssess(User user, String assessInfo){
        try {
            Assess assess = objectMapper.readValue(assessInfo,Assess.class);
            assess.setDoctor_email(user.getEmail());
            String[] nowArrayDateTime = ServiceUtil.nowArrayDateTime();
            assess.setTime(ServiceUtil.timeStringToTimeStamp(nowArrayDateTime[0]+" "+
                    nowArrayDateTime[1], Constant.timePatternTimeStamp));
            assessDao.insert(assess);
            Map<String,String> result = new HashMap<>();
            result.put("assess",Assess.getJson(assess));
            result.put("doctorInfo", DoctorInfo.getJson(doctorInfoDao.getDoctorInfoByE(assess.getDoctor_email())));
            result.put("message","操作成功");
            String data = "";
            data = objectMapper.writeValueAsString(result);
            return data;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Msg.getMsgJsonCode(-1,"数据错误");
    }

    /**
     * 根据id修改评估内容
     * @param user doctor
     * @param assessInfo 评估信息
     */
    @Override
    public String updateAssess(User user, String assessInfo){
        try {
            Assess assess = objectMapper.readValue(assessInfo,Assess.class);
            assess.setDoctor_email(user.getEmail());
            assess.setIs_read(Constant.unread);
            String[] nowArrayDateTime = ServiceUtil.nowArrayDateTime();
            assess.setTime(ServiceUtil.timeStringToTimeStamp(nowArrayDateTime[0]+" "+
                    nowArrayDateTime[1], Constant.timePatternTimeStamp));
            assessDao.updateAssessById(assess.getId(),assess);
            return Msg.getMsgJsonCode(1,Assess.getJson(assess));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Msg.getMsgJsonCode(-1,"修改失败");
    }
}
