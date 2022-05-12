package com.example.doccure.controller;

import com.example.doccure.entity.User;
import com.example.doccure.service.DoccureInfoService;
import com.example.doccure.service.UserService;
import com.example.doccure.utils.Msg;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private DoccureInfoService doccureInfoService;

    @RequestMapping("/admin/doctor/deal-register")
    @ResponseBody
    public String registerSuccess(HttpServletRequest request,String applyInfo){
        if (applyInfo!=null){
            return userService.dealApply(applyInfo);
        }
        return Msg.getMsgJsonCode(-1,"操作失败");
    }

    @RequestMapping("/admin/add-speciality")
    @ResponseBody
    public String addSpeciality(String specialityInfo,MultipartFile file,HttpServletRequest request){
        if (specialityInfo!=null){
            return doccureInfoService.insertSpecialityInfo(specialityInfo,file,request);
        }
        return Msg.getMsgJsonCode(-1,"操作失败");
    }

    @RequestMapping("/admin/update-speciality")
    @ResponseBody
    public String updateSpeciality(String specialityInfo, MultipartFile file,HttpServletRequest request){
        if (specialityInfo!=null){
            return doccureInfoService.updateSpecialityInfo(specialityInfo,file,request);
        }
        return Msg.getMsgJsonCode(-1,"操作失败");
    }

    @RequestMapping("/admin/delete-speciality")
    @ResponseBody
    public String deleteSpeciality(String id){
        if (id!=null){
            doccureInfoService.deleteSpeciality(Integer.parseInt(id));
            return Msg.getMsgJsonCode(1,"删除成功");
        }
        return Msg.getMsgJsonCode(-1,"操作失败");
    }
}
