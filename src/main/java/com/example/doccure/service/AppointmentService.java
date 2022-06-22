package com.example.doccure.service;

import com.example.doccure.entity.Appointment;
import com.example.doccure.entity.User;
import com.example.doccure.utils.Msg;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AppointmentService {
    void insert(Appointment appointment);

    //病人

    List<Appointment> getAptByPE_IsRead(String patient_email,int is_read);

    List<Appointment> getAllByPatientE(String patient_email);

    List<Appointment> getAptByPE_Result_IsRead(String patient_email,
                                           int is_read, String appoint_result);

    void updateIsRead(int id, int is_read);

    List<Appointment> getAllByPEAptType_Time(String patient_email, String appoint_type, String beginTime, String endTime);


    // 医生
    List<Appointment> getAllByDoctorE(String doctor_email);

    List<Appointment> getAllNoRefuseByDPE_Time(String doctor_email,String patient_time,String beginTime,String endTime);

    Msg book(User user, String appointInfo);

    Appointment getAppointmentById(int id);

    void updateAppointResultById(int id, User user, String appoint_type, String result);

    List<Appointment> getAllByDEAptType(String doctor_email, String appoint_type);

    List<Appointment> getAllByDEAptType_Time(String doctor_email,
                                           String appoint_type, String beginTime, String endTime);

    List<Appointment> getAllByDEAptResult_Time(String doctor_email,
                                             String appoint_result, String beginTime, String endTime);
    List<Appointment> getAllByDEAptType_Result(String doctor_email, String appoint_type, String appoint_result);

    List<Appointment> getAllByDEResultType(String doctor_email, String appoint_result);

    List<Appointment> getAllByDPEResultType(String doctor_email, String patient_email, String appoint_result);

    List<Appointment> getAllBeforeTodayByDEResult_Time(String doctor_email, String appoint_result, String endTime);

    List<Appointment> getFutureByDEResultType_BeginTime(String doctor_email, String appoint_result, String beginTime);
}
