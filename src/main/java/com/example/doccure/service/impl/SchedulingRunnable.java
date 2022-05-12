package com.example.doccure.service.impl;

import com.example.doccure.controller.ChatWebSocket;
import com.example.doccure.controller.DataServiceController;
import com.example.doccure.entity.HealthData;
import com.example.doccure.service.PatientInfoService;
import com.example.doccure.service.UserService;
import com.example.doccure.utils.ApplicationContextFactory;
import com.example.doccure.utils.Constant;
import com.example.doccure.utils.Msg;
import com.example.doccure.utils.ServiceUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;


import javax.websocket.Session;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchedulingRunnable implements Runnable{
    private PatientInfoService patientInfoService;
    private ObjectMapper objectMapper;
    private String doctor_email;
    private String patient_email;
    private String healthDataType;
    private String beginTime;

    public SchedulingRunnable() {
    }

    public SchedulingRunnable(String doctor_email, String patient_email, String healthDataType,String beginTime) {
        this.beginTime = ServiceUtil.timeBeforeChat(beginTime,-10000);
        this.doctor_email = doctor_email;
        this.patient_email = patient_email;
        this.healthDataType = healthDataType;
        this.objectMapper = new ObjectMapper();
        ApplicationContext applicationContext = ApplicationContextFactory.getApplicationContext();
        this.patientInfoService = applicationContext.getBean(PatientInfoService.class);
    }

    public String getPatient_email() {
        return patient_email;
    }

    public String getDoctor_email() {
        return doctor_email;
    }

    public void setDoctor_email(String doctor_email) {
        this.doctor_email = doctor_email;
    }

    public void setPatient_email(String patient_email) {
        this.patient_email = patient_email;
    }

    public String getHealthDataType() {
        return healthDataType;
    }

    public void setHealthDataType(String healthDataType) {
        this.healthDataType = healthDataType;
    }

    public void healthDataUpdate (){
        String[] nowTime = ServiceUtil.nowArrayDateTime();
        String endTime = nowTime[0] + " " + nowTime[1];
        Map<String, Session> emailSessionMap = ChatWebSocket.getEmailSessionMap();
        if (emailSessionMap.containsKey(doctor_email)){
            List<HealthData> healthData = patientInfoService.getHealthDataListByPETypeTime(patient_email,
                    healthDataType,beginTime,endTime,1);

            Map<String,String> data = new HashMap<>();
            data.put("text",healthDataType+"变化图");data.put("nameOption",healthDataType+"变化曲线");
            objectMapper.setDateFormat(new SimpleDateFormat(Constant.timePatternTimeStamp));
            try {
                data.put("data",objectMapper.writeValueAsString(healthData));
                Msg msg = new Msg(1,"data",objectMapper.writeValueAsString(data));
                emailSessionMap.get(doctor_email).getAsyncRemote().sendText(Msg.getMsgJsonString(msg));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void run() {
        healthDataUpdate();
    }
}
