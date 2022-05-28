package com.example.doccure.utils;

import com.example.doccure.entity.*;
import com.example.doccure.entity.info.DoctorInfo;
import com.example.doccure.entity.info.PatientInfo;
import com.example.doccure.entity.resume.HealthExamInfo;
import com.example.doccure.entity.resume.HealthResume;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceUtil {
    /**
     * 修改密码 判断填是否正确
     * @param user 病人
     * @param old_password 旧密码
     * @param new_password 新密码
     * @param confirm_password 确认密码
     * @return Msg
     */
    public static Msg changePasswordMsg(User user,String old_password,
                                     String new_password,String confirm_password){
        Msg data = new Msg();
        if (old_password.length()==0 || new_password.length() == 0){
            data.setCode(-1);data.setMessage("请输入旧密码或新密码");
            return data;
        }
        if (old_password.equals(user.getPassword())) {
            if (new_password.equals(confirm_password)){
                data.setCode(1);data.setMessage("修改成功");
            }
            else {
                data.setCode(-1);data.setMessage("密码确认错误");
            }
        }
        else {
            data.setCode(-1);data.setMessage("原密码错误");
        }
        return data;
    }

    public static Msg changePasswordMsg(String new_password,String confirm_password){
        Msg data = new Msg();
        if (new_password.length()==0){
            data.setCode(-1);data.setMessage("请输入新密码");
            return data;
        }
        if (new_password.equals(confirm_password)){
            data.setCode(1);data.setMessage("修改成功");
        }
        else {
            data.setCode(-1);data.setMessage("密码确认错误");
        }
        return data;
    }

    /**
     * appointment记录id与appointment对象封装为map
     * @param appointments appointment列表
     * @return Map<Integer, Appointment>
     */
    public static Map<Integer, Appointment> getMapIdAppointment(List<Appointment> appointments){
        Map<Integer,Appointment> idAppointmentMap = new HashMap<>();
        for (Appointment appointment:appointments){
            idAppointmentMap.put(appointment.getId(),appointment);
        }
        return idAppointmentMap;
    }

    /**
     * appointment记录id与appointment对象封装为map
     * @param assesses appointment列表
     * @return Map<Integer, Appointment>
     */
    public static Map<Integer, Assess> getMapIdAssess(List<Assess> assesses){
        Map<Integer,Assess> idAppointmentMap = new HashMap<>();
        for (Assess assess:assesses){
            idAppointmentMap.put(assess.getId(),assess);
        }
        return idAppointmentMap;
    }

    public static Map<Integer, VisitInfo> getMapIdVisit(List<VisitInfo> visitInfos){
        Map<Integer,VisitInfo> idVisitInfoMap = new HashMap<>();
        for (VisitInfo visitInfo:visitInfos){
            idVisitInfoMap.put(visitInfo.getId(),visitInfo);
        }
        return idVisitInfoMap;
    }

    /**
     * id映射exam
     * @param healthExamInfos list
     * @return id-exam
     */
    public static Map<Integer, HealthExamInfo> getMapIdExamInfo(List<HealthExamInfo> healthExamInfos){
        Map<Integer,HealthExamInfo> examInfoIdMap = new HashMap<>();
        for (HealthExamInfo healthExamInfo:healthExamInfos){
            examInfoIdMap.put(healthExamInfo.getId(),healthExamInfo);
        }
        return examInfoIdMap;
    }

    /**
     * HealthResume 记录id与HealthResume对象封装为map 该map与履历类型封装为map
     * @param healthResumes healthResumes
     * @return Map<String, Map<Integer, HealthResume>>
     */
    public static Map<String, Map<Integer, HealthResume>> getTypeMapIdResume(List<HealthResume> healthResumes){
        if (healthResumes==null){
            return null;
        }
        Map<String, Map<Integer, HealthResume>> type_IdHealthResume = new HashMap<>();
        for (String s:Constant.getResumeType()){
            type_IdHealthResume.put(s,new HashMap<>());
        }
        for (HealthResume healthResume:healthResumes){
            type_IdHealthResume.get(healthResume.getHealth_type()).put(healthResume.getId(),healthResume);
        }
        return type_IdHealthResume;
    }

    public static Map<String, DoctorInfo> getEmailDoctorMap(List<DoctorInfo> doctorInfos){
        Map<String,DoctorInfo> emailDoctorMap = new HashMap<>();
        for (DoctorInfo doctorInfo:doctorInfos){
            emailDoctorMap.put(doctorInfo.getFilterEmail(),doctorInfo);
        }
        return emailDoctorMap;
    }

    public static Map<String, PatientInfo> getEmailPatientMap(List<PatientInfo> patientInfos){
        Map<String,PatientInfo> emailPatientMap = new HashMap<>();
        for (PatientInfo patientInfo:patientInfos){
            emailPatientMap.put(patientInfo.getFilterEmail(),patientInfo);
        }
        return emailPatientMap;
    }

    /**
     * 时间 字符串转换 StringToDate
     * @param date 时间字符串
     * @param pattern 格式字符串
     * @return java.sql.Date
     */
    public static java.sql.Date timeStringToDate(String date, String pattern){
        //时间
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        java.util.Date temp_date = null;
        if (date.length()==0){return null;}
        try {
            temp_date = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (temp_date != null){
            return new Date(temp_date.getTime());
        }
        return null;
    }

    /**
     * 时间 字符串转换 StringToDate
     * @param date 时间字符串
     * @param pattern 格式字符串
     * @return Timestamp
     */
    public static Timestamp timeStringToTimeStamp(String date, String pattern){
        //时间
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        java.util.Date temp_date = null;
        try {
            temp_date = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert temp_date != null;
        return new Timestamp(temp_date.getTime());
    }

    /**
     * 根据当前日期得到 未来或以前是Date
     * @return Date
     */
    public static Date getDateByNowDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + days);
        java.util.Date futureDay = calendar.getTime();
        return new Date(futureDay.getTime());
    }

    /**
     * 根据Date("yyy-MM-dd"),得到想要的时间
     * @param date ("yyy-MM-dd")
     * @param days 天数，正数：未来几天，负数：以前
     * @return ("yyy-MM-dd")
     */
    public static String getDateByTimeDays(String date,int days){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date dateParse = sdf.parse( date, new ParsePosition(0) );
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( dateParse );

        // add 方法中的第二个参数n中, 正数表示该日期后 n 天, 负数表示该日期的前 n 天
        calendar.add( Calendar.DATE, days);
        java.util.Date dateTime = calendar.getTime();
        return sdf.format(dateTime);
    }

    /**
     * 用于聊天
     * 根据时间、秒数，得到时间
     * @param timeString 给出时间
     * @param second 秒数
     * @return 目的时间
     */
    public static String timeBeforeChat(String timeString,int second){
        // long时间戳转换成时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            java.util.Date date = sdf.parse(timeString);
            long newTime = date.getTime() + second;
            java.util.Date date1 = new Date(newTime);
            return new Timestamp(date1.getTime()).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 得到当前时间 array [date("yyyy-MM-dd"),time("HH-mm-ss")]
     */
    public static String[] nowArrayDateTime(){
        String nowDateTime = String.valueOf(LocalDateTime.now()); //appointment信息渲染
        return nowDateTime.split("T");
    }
}
