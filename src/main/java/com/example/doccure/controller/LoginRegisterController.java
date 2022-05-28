package com.example.doccure.controller;

import com.example.doccure.entity.User;
import com.example.doccure.service.DoctorInfoService;
import com.example.doccure.service.MailService;
import com.example.doccure.service.PatientInfoService;
import com.example.doccure.utils.Constant;
import com.example.doccure.utils.Msg;
import com.example.doccure.service.UserService;
import com.example.doccure.utils.ServiceUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

@Controller
public class LoginRegisterController {
    @Autowired
    private MailService mailService;
    @Autowired
    private UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping("/identity-code")
    public String identityCode(){
        return "identity-code";
    }

    @RequestMapping("/forgot-password")
    public String forgotPassword(){
        return "forgot-password";
    }

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
        Msg msg;
        User user = (User) request.getSession().getAttribute("user");
        msg = userService.changePassword(user,info);
        return Msg.getMsgJsonString(msg);
    }

    @RequestMapping("/getIdentityCode")
    @ResponseBody
    public String getIdentityCode(String email,String telephone, HttpServletRequest request){
        String content = "您的验证码: ";
        if (email.length() != 0){
            String code = setIdentityCode();
            request.getSession().setAttribute("code",code);
            return mailService.sendSimpleMailMessage(email, Constant.theme, content + code + "。请在5分钟内使用");
        }
        else if (telephone.length() != 0){
            String code = setIdentityCode();
            request.getSession().setAttribute("code",code);
            return Msg.getMsgJsonCode(1,"请注意查收短信");
        }
        else {
            return Msg.getMsgJsonCode(-1,"请输入邮箱或者电话号码");
        }
    }

    public String setIdentityCode(){
        StringBuilder sRand = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand.append(rand);
        }
        return String.valueOf(sRand);
    }


    @RequestMapping("/confirmIdentityCode")
    @ResponseBody
    public String confirmIdentityCode(String code, HttpServletRequest request){
        if (code.length() != 0){
            if (code.equals(request.getSession().getAttribute("code"))){
                return Msg.getMsgJsonCode(1,"验证成功");
            }
            else {
                return Msg.getMsgJsonCode(-1,"验证码错误");
            }
        }
        else {
            return Msg.getMsgJsonCode(-1,"请输入验证码");
        }
    }

    @RequestMapping("/modify-password")
    @ResponseBody
    public String modifyPassword(String email,String password,String confirm){
        Msg msg = ServiceUtil.changePasswordMsg(password,confirm);
        if (msg.getCode()==1){
            if (email.length()!=0){
                userService.updatePassword(email,password);
            }
            else {
                return Msg.getMsgJsonCode(-1,"请填写邮箱");
            }
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
