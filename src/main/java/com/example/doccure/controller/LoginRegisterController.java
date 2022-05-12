package com.example.doccure.controller;

import com.example.doccure.entity.User;
import com.example.doccure.service.DoctorInfoService;
import com.example.doccure.service.PatientInfoService;
import com.example.doccure.utils.Msg;
import com.example.doccure.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginRegisterController {
    @Autowired
    private UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 登录
     * @param userInfo json字符串得到登录信息
     * @return 返回登录结果信息(是否登陆成功)
     */
    @RequestMapping(value = "/login/auth")
    @ResponseBody
    public String loginAuth(Model model, HttpServletRequest request,String userInfo){
        Msg msg = userService.loginUser(userInfo,request);
        return Msg.getMsgJsonString(msg);
    }

    /**
     * @param userInfo 注册信息
     * @return 注册结果
     */
    @RequestMapping(value = "/register/doctor")
    @ResponseBody
    public String registerDoctor(HttpServletRequest request,
                                 String userInfo, MultipartFile doctorImg,MultipartFile doctorIdentifyFile){
        Msg msg = userService.registerDoctor(userInfo,doctorImg,doctorIdentifyFile,request);
        return Msg.getMsgJsonString(msg);
    }

    @RequestMapping(value = "/query/registerResult")
    @ResponseBody
    public String registerResult(String doctor_email, String doctor_password){
        if (doctor_email!=null&&doctor_password!=null){
            return userService.getDoctorApplyByDEP(doctor_email,doctor_password);
        }
        return Msg.getMsgJsonCode(-1,"未输入邮箱或者密码");
    }

    /**
     * @param userInfo 注册信息
     * @return 注册结果
     */
    @RequestMapping(value = "/register/patient")
    @ResponseBody
    public String registerPatient(String userInfo,HttpServletRequest request){
        Msg msg = userService.registerPatient(userInfo,request);
        return Msg.getMsgJsonString(msg);
    }

    @RequestMapping("/changePassword")
    @ResponseBody
    public String changePassword(HttpServletRequest request,String info){
        Msg msg = new Msg();
        User user = (User) request.getSession().getAttribute("user");
        if (user.getRole()==1){
            msg = userService.changePassword(user,info);
        }
        else if (user.getRole()==-1){
            msg = userService.changePassword(user,info);
        }
        return Msg.getMsgJsonString(msg);
    }

    /**
     * session失效
     */
    @RequestMapping("/logout")
    public String logout(Model model, HttpServletRequest request){
        request.getSession().invalidate();
        return "index";
    }

}
