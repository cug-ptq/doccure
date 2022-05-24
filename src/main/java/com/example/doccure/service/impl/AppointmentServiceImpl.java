package com.example.doccure.service.impl;

import com.example.doccure.dao.AppointmentDao;
import com.example.doccure.entity.Appointment;
import com.example.doccure.entity.info.DoctorInfo;
import com.example.doccure.entity.DoctorPatient;
import com.example.doccure.entity.User;
import com.example.doccure.service.AppointmentService;
import com.example.doccure.service.DoctorInfoService;
import com.example.doccure.service.PatientInfoService;
import com.example.doccure.service.UserService;
import com.example.doccure.utils.Constant;
import com.example.doccure.utils.Msg;
import com.example.doccure.utils.ServiceUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    AppointmentDao appointmentDao;
    @Autowired
    private PatientInfoService patientInfoService;
    @Autowired
    private DoctorInfoService doctorInfoService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;


    @Override
    public void insert(Appointment appointment) {
        appointmentDao.insert(appointment);
    }

    /**
     * 得到未读消息-预约
     * @param patient_email email
     * @param is_read 是否read
     * @return 预约列表
     */
    @Override
    public List<Appointment> getAptByPE_IsRead(String patient_email, int is_read) {
        return appointmentDao.getAptByPE_IsRead(patient_email,is_read);
    }

    @Override
    public void updateIsRead(int id, int is_read){
        appointmentDao.updateIsRead(id, is_read);
    }

    @Override
    public List<Appointment> getAllByPEAptType_Time(String patient_email, String appoint_type, String beginTime, String endTime) {
        return appointmentDao.getAllByPEAptType_Time(patient_email,appoint_type,beginTime,endTime);
    }

    /**
     * 根据医生的额邮箱得到所有预约
     * @param doctor_email 医生邮箱
     * @return 返回预约信息列表
     */
    @Override
    public List<Appointment> getAllByDoctorE(String doctor_email) {
        return appointmentDao.getAllByDoctorE(doctor_email);
    }

    @Override
    public List<Appointment> getAllByPatientE(String patient_email) {
        return appointmentDao.getAllByPatientE(patient_email);
    }

    /**
     * 根据处理结果 得到未读/已读消息
     * @param patient_email email
     * @param is_read 未读/已读
     * @param appoint_result 已接收
     * @return 列表
     */
    @Override
    public List<Appointment> getAptByPE_Result_IsRead(String patient_email, int is_read, String appoint_result) {
        return appointmentDao.getAptByPE_Result_IsRead(patient_email,is_read,appoint_result);
    }

    /**
     * 得到医生未处理预约
     * @param doctor_email 医生邮箱
     * @return 预约列表
     */
    @Override
    public List<Appointment> getAllNoRefuseByDPE_Time(String doctor_email,String patient_time,String beginTime,String endTime){
        return appointmentDao.getAllNoRefuseByDPE_Time(doctor_email,patient_time,beginTime,endTime);
    }

    /**
     * 预约
     * @param user 病人
     * @param appointInfo 预约信息
     * @return 预约结果
     */
    @Override
    public Msg book(User user, String appointInfo) {
        try {
            objectMapper.setDateFormat(new SimpleDateFormat(Constant.timePatternTimeStamp));
            Appointment appointment = objectMapper.readValue(appointInfo,Appointment.class);
            DoctorInfo doctorInfo = doctorInfoService.getDoctorInfoByE(appointment.getDoctor_email());

            //是否已经签约该医生
            if (userService.getDoctorPatientByDP_E(doctorInfo.getEmail(),user.getEmail())!=null
                    && appointment.getAppoint_type().equals(Constant.appoint_typeSign)){
                return new Msg(-1,"","您已经签约过");
            }

            appointment.setIs_read(Constant.read);
            appointment.setPatient_email(user.getEmail());
            appointment.setAppoint_result(Constant.appoint_resultNoDeal);
            appointmentDao.insert(appointment);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new Msg(1,"","请等待医生审核");
    }

    @Override
    public Appointment getAppointmentById(@Param("id") int id){
        return appointmentDao.getAppointmentById(id);
    }

    /**
     * 根据id更改预约状态 同时判断预约类型(签约，则插入记录进医生病人数据表)
     * @param id appointment id
     */
    @Override
    public void updateAppointResultById(int id, User user,String appoint_type, String result){
        Appointment appointment = appointmentDao.getAppointmentById(id);
        if (appoint_type.equals(Constant.appoint_typeSign) &&
                (result.equals(Constant.appoint_resultAccept) || result.equals(Constant.appoint_resultDeal))){
            if (userService.getDoctorPatientByDP_E(user.getEmail(),appointment.getPatient_email())==null){
                userService.insertDE(new DoctorPatient(user.getEmail(),appointment.getPatient_email()));
            }
        }
        appointmentDao.updateAppointResultById(id,result);
        appointmentDao.updateIsRead(id, Constant.unread);
    }

    /**
     * 类型得到appointment
     */
    @Override
    public List<Appointment> getAllByDEAptType(String doctor_email, String appoint_type){
        return appointmentDao.getAllByDEAptType(doctor_email,appoint_type);
    }

    /**
     * 时间段 类型得到appointment
     */
    @Override
    public List<Appointment> getAllByDEAptType_Time(String doctor_email,
                                                  String appoint_type, String beginTime, String endTime){
        return appointmentDao.getAllByDEAptType_Time(doctor_email,appoint_type,beginTime,endTime);
    }

    @Override
    public List<Appointment> getAllByDEAptResult_Time(String doctor_email, String appoint_result, String beginTime, String endTime) {
        return appointmentDao.getAllByDEAptResult_Time(doctor_email,appoint_result,beginTime,endTime);
    }

    @Override
    public List<Appointment> getAllByDEAptType_Result(String doctor_email, String appoint_type, String appoint_result) {
        return appointmentDao.getAllByDEAptType_Result(doctor_email, appoint_type, appoint_result);
    }

    @Override
    public List<Appointment> getAllByDEResultType(String doctor_email, String appoint_result) {
        return appointmentDao.getAllByDEResultType(doctor_email,appoint_result);
    }

    @Override
    public List<Appointment> getAllByDPEResultType(String doctor_email, String patient_email, String appoint_result) {
        return appointmentDao.getAllByDPEResultType(doctor_email,patient_email,appoint_result);
    }

    /**
     * 截止今天服务的appointment数量
     */
    @Override
    public List<Appointment> getAllBeforeTodayByDEResult_Time(String doctor_email, String appoint_result, String endTime){
        return appointmentDao.getAllBeforeTodayByDEResult_Time(doctor_email,appoint_result,endTime);
    }

    /**
     * 根据处理类型和开始时间得到appointment
     * @param doctor_email doctor email
     * @param appoint_result 处理类型
     * @param beginTime 开始时间
     */
    @Override
    public List<Appointment> getFutureByDEResultType_BeginTime(String doctor_email, String appoint_result, String beginTime) {
        return appointmentDao.getFutureByDEResultType_BeginTime(doctor_email,appoint_result,beginTime);
    }

}
