package com.example.doccure.controller;

import com.example.doccure.entity.*;
import com.example.doccure.entity.info.PatientInfo;
import com.example.doccure.entity.resume.HealthBasicInfo;
import com.example.doccure.entity.resume.HealthExamInfo;
import com.example.doccure.entity.resume.HealthResume;
import com.example.doccure.service.*;
import com.example.doccure.utils.Constant;
import com.example.doccure.utils.FileUtil;
import com.example.doccure.utils.Msg;
import com.example.doccure.utils.ServiceUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DoctorController {
    //存储路径
    @Value("${file.upload.path}")
    private String filePath;
    //访问路径前缀
    @Value("${file.access.path}")
    private String accessPath;
    @Autowired
    private DoctorInfoService doctorInfoService;
    @Autowired
    private PatientInfoService patientInfoService;
    @Autowired
    private ServerProperties serverProperties;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private AssessService assessService;


    /** Setting
     * 修改医生信息
     * @param userImg 图片
     * @param infoBasic 基本信息
     * @param infoEdu 教育经历
     * @param infoExp 工作经历
     * @param request session
     * @return 图片路径
     */
    @RequestMapping("/doctor/saveSettingInfoImg")
    @ResponseBody
    public String saveSettingInfoImg(@RequestParam("userImg")MultipartFile userImg,String infoBasic,
                                     String infoEdu,String infoExp,
                                     HttpServletRequest request){
        // 用户信息
        User user = (User) request.getSession().getAttribute("user");

        String url = doctorInfoService.getDoctorInfoByE(user.getEmail()).
                getImage_url();
        String old_filename = "";
        if (url!=null){
            old_filename = url.substring(url.lastIndexOf("/"));//旧图片名
        }
        // 文件上传
        if (userImg!=null){
            url = FileUtil.uploadFile(userImg, String.valueOf(serverProperties.getPort()),filePath,accessPath,old_filename,request);
        }
        doctorInfoService.SaveInfo(user,infoBasic,infoEdu,infoExp,url);
        Map<String,String> data = new HashMap<>();
        String result = null; // 返回前端显示图片
        data.put("url",url);
        try {
            result = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // 返回图片访问路径URL
        return result;
    }

    /** Setting
     * 医生未上传图片时，Ajax请求路径，更新信息
     * @param infoBasic 基本信息
     * @param infoEdu 教育信息
     * @param infoExp 工作经历
     * @param request user
     * @return 更新情况
     */
    @RequestMapping("/doctor/saveSettingInfo")
    @ResponseBody
    public String saveSettingInfo(String infoBasic,String infoEdu,String infoExp,
                                  HttpServletRequest request){
        // 用户信息
        User user = (User) request.getSession().getAttribute("user");
        String url = doctorInfoService.getDoctorInfoByE(user.getEmail()).
                getImage_url();
        doctorInfoService.SaveInfo(user,infoBasic,infoEdu,infoExp,url);
        return infoBasic;
    }

    /** Appointments
     * 更新预约状态
     * @param request user
     * @param appointResult 医生操作得到信息 预约id、处理状态、预约类型
     * @return 是否更新成功
     */
    @RequestMapping("/doctor/changeAppointStatus")
    @ResponseBody
    public String changeAppointStatus(HttpServletRequest request,String appointResult){
        // 用户信息
        User user = (User) request.getSession().getAttribute("user");
        try {
            JsonNode jsonNode = objectMapper.readTree(appointResult);
            appointmentService.updateAppointResultById(jsonNode.get("id").asInt(),user,jsonNode.get("appoint_type").asText(),
                    jsonNode.get("appoint_result").asText());
            if (jsonNode.get("appoint_result").asText().equals(Constant.appoint_resultAccept)){
                appointmentService.updateIsRead(jsonNode.get("id").asInt(),Constant.unread);
            }
            else if (jsonNode.get("appoint_result").asText().equals(Constant.appoint_resultRefuse)){
                appointmentService.updateIsRead(jsonNode.get("id").asInt(),Constant.unread);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Msg.getMsgJsonCode(1,"修改成功");
    }

    /**
     * 根据前端选择的预约结果类型得到appointment信息
     * @param request user
     * @param resultType appoint_result类型
     * @return 病人信息、appointment信息，Map的JSON字符串
     */
    @RequestMapping("/doctor/appointmentByAptResult")
    @ResponseBody
    public String getAppointmentByAptType(HttpServletRequest request,String resultType){
        // 用户信息
        User user = (User) request.getSession().getAttribute("user");
        List<Appointment> appointments;
//        if (resultType.equals(Constant.appoint_resultAccept)){
//            appointments = appointmentService.getAllByDEResultType(user.getEmail(),Constant.appoint_resultAccept);
//        }
//        else {
//            appointments = appointmentService.getFutureByDEResultType_BeginTime(user.getEmail(),
//                    resultType, ServiceUtil.nowArrayDateTime()[0]+" "+ServiceUtil.nowArrayDateTime()[1]);
//        }
        objectMapper.setDateFormat(new SimpleDateFormat(Constant.timePatternTimeStamp));
        appointments = appointmentService.getAllByDEResultType(user.getEmail(),resultType);
        String idAppointmentMap = "";
        try {
            idAppointmentMap = objectMapper.writeValueAsString(appointments);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String patientInfoList = "";
        try {
            patientInfoList = objectMapper.writeValueAsString(patientInfoService.getAllPatientInfoByAppoint(appointments));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Map<String ,String> data = new HashMap<>();
        data.put("idAppointmentMap",idAppointmentMap);data.put("patientInfoList",patientInfoList);
        try {
            objectMapper.setDateFormat(new SimpleDateFormat(Constant.timePatternTimeStamp));
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Msg.getMsgJsonCode(-1,"获取失败");
    }

    /**
     * 解约
     * @param request user
     * @param email 病人邮箱
     * @return 解约操作结果
     */
    @RequestMapping("/doctor/rescind")
    @ResponseBody
    public String rescind(HttpServletRequest request,String email){
        // 用户信息
        User user = (User) request.getSession().getAttribute("user");
        if (email!=null){
            userService.deleteByDP_E(user.getEmail(),email);
            return Msg.getMsgJsonCode(1,"解约失败");
        }
        return Msg.getMsgJsonCode(-1,"解约失败");
    }


    /**
     * 显示病人信息 - 健康档案
     * @param model 数据渲染
     * @param email 病人email
     * @param request user
     * @return 页面
     */
    @RequestMapping(value = {"/patient-profile","/patient-profile/health-record"})
    public String healthRecord(Model model, String email, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null) {
            model.addAttribute("user", user);
            model.addAttribute("doctorInfo",doctorInfoService.getDoctorInfoByE(user.getEmail()));
            //病人信息
            PatientInfo patientInfo = patientInfoService.getPatientInfoByE(email);
            model.addAttribute("patientInfo",patientInfo);
            //健康档案
            //基本信息
            HealthBasicInfo healthBasicInfo = patientInfoService.getHealthBasicInfoByPE(email);
            if (healthBasicInfo == null){ healthBasicInfo = new HealthBasicInfo();}
            model.addAttribute("healthBasicInfo",healthBasicInfo);

            //履历
            List<HealthResume> healthResumes = patientInfoService.getHealthResumeByPE(email);
            if (healthResumes == null){ healthResumes = new ArrayList<>();}
            model.addAttribute("recordTypeIdMap",ServiceUtil.getTypeMapIdResume(healthResumes));
            model.addAttribute("disease",Constant.RESUME_DISEASE);
            model.addAttribute("injury",Constant.RESUME_INJURY);
            model.addAttribute("operation",Constant.RESUME_OPERATION);

            //体检
            List<HealthExamInfo> healthExamInfos = patientInfoService.getHealthExamInfoByPE(email);
            model.addAttribute("examInfosIdMap",ServiceUtil.getMapIdExamInfo(healthExamInfos));
        }
        return "patient-record";
    }

    /**
     * 显示病人信息 - 评估(先进行) 前端
     * @param model 数据渲染
     * @param email 病人email
     * @param request user
     * @return 页面
     */
    @RequestMapping("/patient-profile/health-assess")
    public String healthAssess(Model model, String email, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null) {
            model.addAttribute("user", user);
            //病人信息
            PatientInfo patientInfo = patientInfoService.getPatientInfoByE(email);
            model.addAttribute("patientInfo",patientInfo);
            //评估-病人的历史评估
            List<Assess> assessesPOthers = assessService.getAllAssessByPE_OtherDE(email,user.getEmail());
            model.addAttribute("assessesPOthers",ServiceUtil.getMapIdAssess(assessesPOthers));
            //doctor的评估-添加
            List<Assess> assessesDoctor = assessService.getAssessByDE(user.getEmail());
            model.addAttribute("assessesDoctor",ServiceUtil.getMapIdAssess(assessesDoctor));

            model.addAttribute("doctorInfo",doctorInfoService.getDoctorInfoByE(user.getEmail()));
            model.addAttribute("doctorInfosOthers",doctorInfoService.getDoctorInfoByAssesses(assessesPOthers));
        }
        return "patient-assess";
    }

    /**
     * 添加评估
     * @param request user
     * @param assessInfo assess
     * @return assess对象
     */
    @RequestMapping("/doctor/addAssess")
    @ResponseBody
    public String addAssess(HttpServletRequest request,String assessInfo){
        User user = (User) request.getSession().getAttribute("user");
        return assessService.addAssess(user,assessInfo);
    }

    /**
     * 修改评估
     * @param request user
     * @param assessInfo 评估信息-id、description、content
     * @return 修改结果
     */
    @RequestMapping("/doctor/updateAssess")
    @ResponseBody
    public String updateAssess(HttpServletRequest request,String assessInfo){
        User user = (User) request.getSession().getAttribute("user");
        return assessService.updateAssess(user,assessInfo);
    }

    @RequestMapping("/doctor/deleteAssess")
    @ResponseBody
    public String deleteAssess(HttpServletRequest request,String id){
        if (id!=null){
            return assessService.deleteById(Integer.parseInt(id));
        }
        return Msg.getMsgJsonCode(-1,"删除失败");
    }

    @RequestMapping("/doctor/addVisit")
    @ResponseBody
    public String addVisit(HttpServletRequest request,String visitInfo){
        User user = (User) request.getSession().getAttribute("user");
        if (visitInfo!=null){
            return patientInfoService.insertVisit(user,visitInfo);
        }
        return Msg.getMsgJsonCode(-1,"添加失败");
    }

    @RequestMapping("/doctor/updateVisit")
    @ResponseBody
    public String updateVisit(HttpServletRequest request,String visitInfo){
        if (visitInfo!=null){
            return patientInfoService.updateVisitInfoByIdInfo(visitInfo);
        }
        return Msg.getMsgJsonCode(-1,"更新失败");
    }

    @RequestMapping("/doctor/deleteVisit")
    @ResponseBody
    public String deleteVisit(String id){
        if (id!=null){
            patientInfoService.deleteVisitInfoById(Integer.parseInt(id));
            return Msg.getMsgJsonCode(1,"删除成功");
        }
        return Msg.getMsgJsonCode(-1,"删除失败");
    }

    @RequestMapping("/doctor/changePatientVisit")
    @ResponseBody
    public String changePatientVisit(HttpServletRequest request, String patient_email){
        User user = (User) request.getSession().getAttribute("user");

        if (patient_email!=null){
            List<VisitInfo> visitInfos = patientInfoService.getVisitInfoByDPE(user.getEmail(),patient_email);
            try {
                objectMapper.setDateFormat(new SimpleDateFormat(Constant.timePatternTimeStamp));
                return Msg.getMsgJsonCode(1,objectMapper.writeValueAsString(ServiceUtil.getMapIdVisit(visitInfos)));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return Msg.getMsgJsonCode(-1,"请求失败");
    }

}
