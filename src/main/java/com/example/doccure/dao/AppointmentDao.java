package com.example.doccure.dao;

import com.example.doccure.entity.Appointment;
import com.example.doccure.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AppointmentDao {
    void insert(Appointment appointment);

    List<Appointment> getAllByPatientE(@Param("doctor_email") String doctor_email);

    Appointment getAppointmentById(@Param("id") int id);

    List<Appointment> getAllByDoctorE(@Param("doctor_email") String doctor_email);

    //病人
    List<Appointment> getAptByPE_IsRead(@Param("patient_email") String patient_email,int is_read);

    List<Appointment> getAptByPE_Result_IsRead(@Param("patient_email") String patient_email,
                                           int is_read, String appoint_result);

    void updateIsRead(@Param("id") int id, @Param("is_read") int is_read);

    /**
     * 时间段 类型得到appointment
     */
    List<Appointment> getAllByPEAptType_Time(@Param("patient_email") String patient_email, String appoint_type, String beginTime, String endTime);


    //医生

    /**
     * 查找预约结果不为拒绝的预约记录
     * @param doctor_email doctor email
     * @param patient_time patient email
     * @return appointment列表
     */
    List<Appointment> getAllNoRefuseByDPE_Time(String doctor_email,String patient_time,String beginTime,String endTime);

    /**
     * 类型得到appointment
     */
    List<Appointment> getAllByDEAptType(@Param("doctor_email") String doctor_email, String appoint_type);

    /**
     * 时间段 类型得到appointment
     */
    List<Appointment> getAllByDEAptType_Time(@Param("doctor_email") String doctor_email, String appoint_type, String beginTime, String endTime);

    List<Appointment> getAllByDEAptType_Result(@Param("doctor_email") String doctor_email, String appoint_type, String appoint_result);

    /**
     * 时间段 处理结果得到appointment
     */
    List<Appointment> getAllByDEAptResult_Time(@Param("doctor_email") String doctor_email, String appoint_result, String beginTime, String endTime);

    /**
     * 截止今天服务的appointment数量
     */
    List<Appointment> getAllBeforeTodayByDEResult_Time(@Param("doctor_email") String doctor_email, String appoint_result, String endTime);

    /**
     * 根据处理类型和开始时间得到appointment
     */
    List<Appointment> getFutureByDEResultType_BeginTime(@Param("doctor_email") String doctor_email, String appoint_result, String beginTime);

    List<Appointment> getAllByDEResultType(@Param("doctor_email") String doctor_email, String appoint_result);

    List<Appointment> getAllByDPEResultType(@Param("doctor_email") String doctor_email, String patient_email, String appoint_result);

    /**
     * 更新
     */
    void updateAppointResultByDE(@Param("doctor_email") String doctor_email,@Param("result") String result);

    void updateAppointResultById(@Param("id") int id,@Param("result") String result);
}
