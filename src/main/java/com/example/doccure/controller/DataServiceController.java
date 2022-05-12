package com.example.doccure.controller;

import com.example.doccure.entity.Appointment;
import com.example.doccure.entity.AssessImg;
import com.example.doccure.entity.HealthData;
import com.example.doccure.entity.User;
import com.example.doccure.entity.info.DoctorInfo;
import com.example.doccure.entity.info.SpecialityInfo;
import com.example.doccure.service.*;
import com.example.doccure.service.impl.SchedulingRunnable;
import com.example.doccure.utils.Constant;
import com.example.doccure.utils.FileUtil;
import com.example.doccure.utils.Msg;
import com.example.doccure.utils.ServiceUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.Session;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Component
@Controller
public class DataServiceController {
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private UserService userService;
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    @Autowired
    private ServerProperties serverProperties;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PatientInfoService patientInfoService;
    @Autowired
    private DoctorInfoService doctorInfoService;
    @Autowired
    private DoccureInfoService doccureInfoService;
    //存储路径
    @Value("${file.upload.path}")
    private String filePath;
    //访问路径前缀
    @Value("${file.access.path}")
    private String accessPath;

    private static final Map<String, ScheduledFuture<?>> futureMap = new HashMap<>();

    /**
     * WangEditor的图片上传
     * @param files 多个文件对象
     * @param request user
     * @return AssessImg对象的JSON字符串
     */
    @RequestMapping("/uploadAssess/img")
    @ResponseBody
    public String uploadAssessImg(MultipartFile[] files, HttpServletRequest request){
        AssessImg assessImg = new AssessImg();
        List<String> assessImgList = new ArrayList<>();
        for (MultipartFile file:files){
            String imgUrl =  FileUtil.uploadFile(file,serverProperties.getPort().toString(),filePath,accessPath,"",request);
            assessImgList.add(imgUrl);
        }
        assessImg.setData(assessImgList);
        return AssessImg.SuccessJson(assessImg);
    }

    @RequestMapping("/healthData/update")
    @ResponseBody
    public String healthDataSchedule(HttpServletRequest request,
                                     String email, String healthDateType){
        User user = (User) request.getSession().getAttribute("user");

        String[] nowTime = ServiceUtil.nowArrayDateTime();
        String endTime = nowTime[0] + " " + nowTime[1];
        String beginTime = ServiceUtil.timeBeforeChat(nowTime[0] + " " + nowTime[1],-10000);
        if (patientInfoService.getHealthDataListByPETypeTime(email,
                healthDateType,beginTime,endTime,1).size()==0){
            if (futureMap.containsKey(user.getEmail())){
                futureMap.get(user.getEmail()).cancel(true);
            }
            return Msg.getMsgJsonCode(-1,"该病人没有此数据项的实时数据");
        }
        ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(
                new SchedulingRunnable(user.getEmail(), email,healthDateType,ServiceUtil.nowArrayDateTime()[0] +
                        " " + ServiceUtil.nowArrayDateTime()[1]),
                new CronTrigger("0/5 * * * * *")
        );
        if (futureMap.containsKey(user.getEmail())){
            futureMap.get(user.getEmail()).cancel(true);
        }
        else {
            futureMap.put(user.getEmail(),future);
        }
        return Msg.getMsgJsonCode(1,"请求成功");
    }

    @RequestMapping("/healthData/cancel")
    @ResponseBody
    public String healthDataScheduleKill(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (futureMap.containsKey(user.getEmail())){
            futureMap.get(user.getEmail()).cancel(true);
            futureMap.remove(user.getEmail());
        }
        return Msg.getMsgJsonCode(-1,"停止成功");
    }

    @RequestMapping("/healthData/getHistory")
    @ResponseBody
    public String getHistory(HttpServletRequest request,String email,
                             String healthDataType,String beginTime,String endTime,String internal){
        Map<String,String> data = new HashMap<>();
        data.put("text",healthDataType+"变化图");data.put("nameOption",healthDataType+"变化曲线");
        List<HealthData> healthData = patientInfoService.getHealthDataListByPETypeTime(email,healthDataType,
                beginTime,endTime, Integer.parseInt(internal));
        objectMapper.setDateFormat(new SimpleDateFormat(Constant.timePatternTimeStamp));
        try {
            data.put("data",objectMapper.writeValueAsString(healthData));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            return Msg.getMsgJsonCode(1,objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Msg.getMsgJsonCode(-1,"获取失败");
    }

    @Scheduled(fixedRate = 5000)
    public void appointStatistics(){
        Map<String, Session> emailSessionMap = ChatWebSocket.getEmailSessionMap();
        for (String email:emailSessionMap.keySet()){
            User user = userService.getUserByE(email);
            if (user.getRole()==Constant.doctorRole){
                try {
                    //预约统计
                    List<String> appointTypeNum = new ArrayList<>();
                    for (String type:Constant.getAptTypeList()){
                        ObjectNode root = objectMapper.createObjectNode();
                        List<Appointment> appointments1 = appointmentService.getAllByDEAptType(user.getEmail(),type);
                        root.put("name",type);
                        root.put("value",appointments1.size());
                        appointTypeNum.add(objectMapper.writeValueAsString(root));
                    }
                    ObjectNode root = objectMapper.createObjectNode();
                    ArrayNode appointTypeNumJson = root.putArray("appointTypeNum");
                    for (String typeNum:appointTypeNum){
                        appointTypeNumJson.add(typeNum);
                    }
                    List<DoctorInfo> doctorInfos = doctorInfoService.getAllDoctor();
                    Map<String,Integer> specialtyNumMap = new HashMap<>();
                    List<SpecialityInfo> specialityInfos = doccureInfoService.getAllSpecialityInfo();
                    for (SpecialityInfo specialityInfo : specialityInfos){
                        specialtyNumMap.put(specialityInfo.getSpeciality(),0);
                    }
                    for (DoctorInfo doctorInfo1:doctorInfos){
                        int num = specialtyNumMap.get(doctorInfo1.getSpeciality()) + appointmentService.getAllByDoctorE(doctorInfo1.getEmail()).size();
                        specialtyNumMap.replace(doctorInfo1.getSpeciality(),num);
                    }

                    ArrayNode specialties = root.putArray("specialties");
                    ArrayNode specialtyNum = root.putArray("specialtyNum");
                    for (String key:specialtyNumMap.keySet()){
                        specialties.add(key);
                        specialtyNum.add(specialtyNumMap.get(key));
                    }
                    Session session = emailSessionMap.get(email);
                    if (session!=null){
                        session.getAsyncRemote().sendText(Msg.getMsgJsonType("other",objectMapper.writeValueAsString(root)));
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
