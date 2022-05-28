package com.example.doccure.utils;

import java.util.ArrayList;
import java.util.List;

public class Constant {
    public static String theme = "家庭医生管理系统";
    public static int doctorRole = 1;
    public static int patientRole = -1;
    public static int adminRole = 0;
    //预约
    public static String appoint_resultNoDeal = "未处理";
    public static String appoint_resultDeal = "已完成";
    public static String appoint_resultAccept = "已接收";
    public static String appoint_resultRefuse = "拒绝";
    public static String appoint_typeSign = "签约";
    public static String appoint_typeVisit = "家访";
    public static String appoint_typeTreat = "就诊";
    public static int unread = 0;
    public static int read = 1;
    public static String timePatternTimeStamp = "yyyy-MM-dd HH:mm:ss";
    public static String timePatternDate = "yyyy-MM-dd";
    //申请
    public static int apply_accept = 1;
    public static int apply_noDeal = 0;
    public static int apply_refuse = -1;
    //履历类型
    public static String RESUME_DISEASE = "disease";
    public static String RESUME_INJURY = "injury";
    public static String RESUME_OPERATION = "operation";

    //健康数据类型
    public static String heart_rate = "心率";
    public static String blood_pressure = "血压";
    public static String blood_oxygen = "血氧饱和度";
    public static String temperature = "体温";

    //消息
    public static int NOT_CHAT = 0;
    public static int CHAT = 1;
    public static int LAST_MESSAGE = 1;
    public static int NOT_LAST_MESSAGE = 0;

    public static String MSG_VISIT = "visit";
    public static String MSG_RESUME = "resume";
    public static String MSG_ASSESS = "assess";
    public static String MSG_EXAM = "exam";
    public static String MSG_CHAT = "chat";
    public static String MSG_VIDEO = "video";
    /**
     * 处理类型->List
     * appoint_resultNoDeal;
     * appoint_resultDeal;
     * appoint_resultAccept;
     * @return List
     */
    public static List<String> getAptDealType(){
        List<String> dealTypes = new ArrayList<>();
        dealTypes.add(appoint_resultNoDeal);
        dealTypes.add(appoint_resultDeal);
        dealTypes.add(appoint_resultAccept);
        return dealTypes;
    }

    public static List<String> getResumeType(){
        List<String> resumeTypes = new ArrayList<>();
        resumeTypes.add(RESUME_DISEASE);
        resumeTypes.add(RESUME_INJURY);
        resumeTypes.add(RESUME_OPERATION);
        return resumeTypes;
    }

    public static List<String> getDataType(){
        List<String> dataTypes = new ArrayList<>();
        dataTypes.add(heart_rate);
        dataTypes.add(blood_oxygen);
        dataTypes.add(blood_pressure);
        dataTypes.add(temperature);
        return dataTypes;
    }

    public static List<String> getAptTypeList(){
        List<String> aptTypeList = new ArrayList<>();
        aptTypeList.add(appoint_typeSign);
        aptTypeList.add(appoint_typeVisit);
        aptTypeList.add(appoint_typeTreat);
        return aptTypeList;
    }
}
