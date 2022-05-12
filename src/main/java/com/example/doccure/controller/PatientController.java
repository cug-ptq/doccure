package com.example.doccure.controller;

import com.example.doccure.entity.Assess;
import com.example.doccure.entity.VisitInfo;
import com.example.doccure.entity.info.DoctorInfo;
import com.example.doccure.entity.User;
import com.example.doccure.service.*;
import com.example.doccure.utils.Constant;
import com.example.doccure.utils.FileUtil;
import com.example.doccure.utils.Msg;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 病人的信息controller
 */
@Controller
public class PatientController {
    //存储路径
    @Value("${file.upload.path}")
    private String filePath;
    //访问路径前缀
    @Value("${file.access.path}")
    private String accessPath;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PatientInfoService patientInfoService;
    @Autowired
    private DoctorInfoService doctorInfoService;
    @Autowired
    private ServerProperties serverProperties;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private AssessService assessService;

    /**
     * 病人基本信息设置
     * @param userImg 前端图片文件 (用户上传了图片的情况下)
     * @param info 其他信息
     * @return 图片url
     */
    @RequestMapping("/patient/saveSettingInfoImg")
    @ResponseBody
    public String saveSettingInfoImg(@RequestParam("userImg")MultipartFile userImg,String info,
                                 HttpServletRequest request){
        // 用户信息
        User user = (User) request.getSession().getAttribute("user");

        String url = patientInfoService.getPatientInfoByE(user.getEmail()).
                getImage_url();
        String old_filename = "";
        if (url!=null){
            old_filename = url.substring(url.lastIndexOf("/"));//旧图片名
        }
        // 文件上传
        if (userImg!=null){
            url = FileUtil.uploadFile(userImg, String.valueOf(serverProperties.getPort()),filePath,accessPath,old_filename,request);
        }
        patientInfoService.SaveInfo(user,info,url);
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

    @RequestMapping("/patient/saveSettingInfo")
    @ResponseBody
    public String saveSettingInfo(String info,
                                  HttpServletRequest request){
        // 用户信息
        User user = (User) request.getSession().getAttribute("user");

        String url = patientInfoService.getPatientInfoByE(user.getEmail()).
                getImage_url();
        patientInfoService.SaveInfo(user,info,url);
        return info;
    }

    @RequestMapping("/patient/booking")
    @ResponseBody
    public String book(Model model,HttpServletRequest request,String appointInfo){
        User user = (User) request.getSession().getAttribute("user");
        Msg msg = new Msg();
        if (appointInfo!=null){
            msg = appointmentService.book(user,appointInfo);
        }
        String data = "";
        try {
            data = objectMapper.writeValueAsString(new Msg(-1,"","提交失败"));
            return objectMapper.writeValueAsString(msg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 已读
     * @param id appointment id
     * @return 操作结果
     */
    @RequestMapping("/patient/readApt")
    @ResponseBody
    public String readApt(int id){
        appointmentService.updateIsRead(id, Constant.read);
        return Msg.getMsgJsonCode(1,"操作成功");
    }

    @RequestMapping("/patient/readAssess")
    @ResponseBody
    public String readAssess(String assess){
        try {
            objectMapper.setDateFormat(new SimpleDateFormat(Constant.timePatternTimeStamp));
            Assess assess1 = objectMapper.readValue(assess, Assess.class);
            assessService.updateIs_Read(assess1.getId(), Constant.read);
            return Msg.getMsgJsonCode(1,DoctorInfo.getJson(doctorInfoService.getDoctorInfoByE(assess1.getDoctor_email())));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Msg.getMsgJsonCode(-1,"操作失败");
    }

    @RequestMapping("/patient/getAssess")
    @ResponseBody
    public String getAssess(String id){
        if (id!=null){
            assessService.updateIs_Read(Integer.parseInt(id),Constant.unread);
            return Msg.getMsgJsonCode(1,Assess.getJson(assessService.getAssessById(Integer.parseInt(id))));
        }
        return Msg.getMsgJsonCode(-1,"更新失败");
    }

    /**
     * 保存基本健康信息
     * @param request user
     * @param healthBasicInfo 基本信息
     * @return 操作结果
     */
    @RequestMapping("/patient/saveHealthBasicInfo")
    @ResponseBody
    public String saveHealthBasicInfo(HttpServletRequest request, String healthBasicInfo){
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null){
            return patientInfoService.updateHealthBasicInfoByPE_Info(user.getEmail(),healthBasicInfo);
        }
        return Msg.getMsgJsonCode(-1,"您未登录");
    }

    /**
     * 添加resume
     * @param request user
     * @param resumeInfo 信息
     * @return 操作结果/resume
     */
    @RequestMapping("/patient/addResume")
    @ResponseBody
    public String addDisease(HttpServletRequest request,String resumeInfo){
        User user = (User) request.getSession().getAttribute("user");
        if (resumeInfo!=null){
            return patientInfoService.insertResume(user,resumeInfo);
        }
        return Msg.getMsgJsonCode(-1,"添加失败");
    }

    /**
     * 更新履历
     * @param request user
     * @param id resume id
     * @param resumeInfo 信息
     * @return 操作结果
     */
    @RequestMapping("/patient/updateResume")
    @ResponseBody
    public String updateDisease(HttpServletRequest request,String id,String resumeInfo){
        if (id!=null&&resumeInfo!=null){
            return patientInfoService.updateHealthResumeBy(Integer.parseInt(id),resumeInfo);
        }
        return Msg.getMsgJsonCode(-1,"添加失败");
    }

    @RequestMapping("/patient/deleteResume")
    @ResponseBody
    public String deleteDisease(HttpServletRequest request,String id){
        if (id!=null){
            patientInfoService.deleteHealthResume(Integer.parseInt(id));
            return Msg.getMsgJsonCode(1,"删除成功");
        }
        return Msg.getMsgJsonCode(-1,"添加失败");
    }

    @RequestMapping("/patient/addExam")
    @ResponseBody
    public String addExam(HttpServletRequest request,String examInfo,MultipartFile image, MultipartFile otherFile){
        User user = (User) request.getSession().getAttribute("user");
        if (examInfo!=null){
            return patientInfoService.insertExam(user,serverProperties.getPort(),examInfo,image, otherFile,request);
        }
        return Msg.getMsgJsonCode(-1,"添加失败");
    }

    @RequestMapping("/patient/updateExam")
    @ResponseBody
    public String updateExam(HttpServletRequest request,String id,String examInfo,MultipartFile image,MultipartFile otherFile){
        if (id!=null&&examInfo!=null){
            return patientInfoService.updateHealthExamInfoBy(Integer.parseInt(id),serverProperties.getPort(),examInfo,image,otherFile,request);
        }
        return Msg.getMsgJsonCode(-1,"添加失败");
    }

    @RequestMapping("/patient/deleteExam")
    @ResponseBody
    public String deleteExam(String id){
        if (id!=null){
            patientInfoService.deleteHealthExamInfoById(Integer.parseInt(id));
            return Msg.getMsgJsonCode(1,"删除成功");
        }
        return Msg.getMsgJsonCode(-1,"添加失败");
    }

    @RequestMapping("/patient/updateVisitIsRead")
    @ResponseBody
    public String updateVisitIsRead(String id,String is_read){
        if (id!=null&&is_read!=null){
            patientInfoService.updateVisitInfoIsReadByIdIsRead(Integer.parseInt(id), Integer.parseInt(is_read));
            return Msg.getMsgJsonCode(1,"更新成功");
        }
        return Msg.getMsgJsonCode(-1,"更新失败");
    }

    @RequestMapping("/patient/getVisit")
    @ResponseBody
    public String getVisit(String id){
        if (id!=null){
            patientInfoService.getVisitInfoById(Integer.parseInt(id));
            return Msg.getMsgJsonCode(1, VisitInfo.getJsonString(patientInfoService.getVisitInfoById(Integer.parseInt(id))));
        }
        return Msg.getMsgJsonCode(-1,"更新失败");
    }
}
