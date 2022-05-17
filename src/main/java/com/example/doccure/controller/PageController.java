package com.example.doccure.controller;


import com.example.doccure.entity.*;
import com.example.doccure.entity.doctor_exp_edu.DoctorEdu;
import com.example.doccure.entity.doctor_exp_edu.DoctorExp;
import com.example.doccure.entity.info.DoctorInfo;
import com.example.doccure.entity.info.PatientInfo;
import com.example.doccure.entity.info.SpecialityInfo;
import com.example.doccure.entity.resume.HealthBasicInfo;
import com.example.doccure.entity.resume.HealthExamInfo;
import com.example.doccure.entity.resume.HealthResume;
import com.example.doccure.service.*;
import com.example.doccure.utils.Constant;
import com.example.doccure.utils.Msg;
import com.example.doccure.utils.ServiceUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 完成页面跳转，基本数据渲染，不包括聊天
 */
@Controller
public class PageController {
    @Autowired
    private PatientInfoService patientInfoService;
    @Autowired
    private DoctorInfoService doctorInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private AssessService assessService;
    @Autowired
    private ChatMessageService chatMessageService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ServerProperties serverProperties;
    @Autowired
    private DoccureInfoService doccureInfoService;
    private String message = "";

    @RequestMapping("/blank-page")
    public String blankPage(Model model){
        model.addAttribute("message",message);
        return "blank-page";
    }

    @RequestMapping("/chat/blank-page")
    public String blankPageMsg(Model model, @ModelAttribute("msg") String msg){
        model.addAttribute("message",msg);
        return "blank-page";
    }

    @RequestMapping("/index")
    public String index(Model model, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null){
            model.addAttribute("name",user.getUsername());
            model.addAttribute("role",user.getRoleName());
            String image_url;
            if (user.getRole()==Constant.patientRole){
                image_url = patientInfoService.getPatientInfoByE(user.getEmail()).getImage_url();
                model.addAttribute("isDoctor",false);
            }
            else {
                image_url = doctorInfoService.getDoctorInfoByE(user.getEmail()).getImage_url();
                model.addAttribute("isDoctor",true);
            }
            model.addAttribute("ImgUrl",image_url);
            model.addAttribute("isLogin",true);
        }
        else {
            model.addAttribute("isLogin",false);
        }
        return "index";
    }

    /**
     * 医生列表
     */
    @RequestMapping("/doctors")
    public String doctors(Model model, HttpServletRequest request){
        //获取医生列表
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null){
            model.addAttribute("name",user.getUsername());
            model.addAttribute("role",user.getRoleName());
            String image_url;
            if (user.getRole()==Constant.patientRole){
                image_url = patientInfoService.getPatientInfoByE(user.getEmail()).getImage_url();
            }
            else {
                image_url = doctorInfoService.getDoctorInfoByE(user.getEmail()).getImage_url();
            }
            model.addAttribute("ImgUrl",image_url);
            model.addAttribute("isLogin",true);
        }
        else {
            model.addAttribute("isLogin",false);
        }
        List<DoctorInfo> doctorList = doctorInfoService.getAllDoctor();
        model.addAttribute("doctorList",doctorList);
        return "doctors";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/doctor-change-password")
    public String doctorChangePassword(Model model, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null){
            model.addAttribute("user",user);
            DoctorInfo doctorInfo = doctorInfoService.getDoctorInfoByE(user.getEmail());
            model.addAttribute("doctorInfo",doctorInfo);
        }
        return "doctor-change-password";
    }

    @RequestMapping("/change-password")
    public String changePassword(Model model, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null){
            if (user.getRole()== Constant.patientRole){
                model.addAttribute("user",user);
                PatientInfo patientInfo = patientInfoService.getPatientInfoByE(user.getEmail());
                model.addAttribute("patientInfo",patientInfo);
            }
            else {
                return "redirect:/doctor-change-password";
            }
        }
        return "change-password";
    }

    /**
     * 医生
     */


    @RequestMapping("/doctor-dashboard")
    public String doctorDashboard(Model model, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null){
            model.addAttribute("user",user);
            if (user.getRole()== Constant.doctorRole){
                //基本信息渲染
                DoctorInfo doctorInfo = doctorInfoService.getDoctorInfoByE(user.getEmail());
                model.addAttribute("doctorInfo",doctorInfo);
                List<DoctorEdu> doctorEduList = doctorInfoService.getAllDoctorEduByE(user.getEmail());
                model.addAttribute("edu",doctorInfoService.getEdu(doctorEduList));

                //数据总览
                String[] nowArrayDateTime = ServiceUtil.nowArrayDateTime();
                String todayTimeBegin = nowArrayDateTime[0] + " " + "00:00:00";
                String todayTimeEnd = nowArrayDateTime[0] + " " + "23:59:59";
                model.addAttribute("toDayTime",nowArrayDateTime[0]);

                //今天的appointment数量 就诊，家访-->病人人数
                List<Appointment> todayTreatAppointments = appointmentService.getAllByDEAptType_Time(user.getEmail(),
                        Constant.appoint_typeTreat,todayTimeBegin,todayTimeEnd);
                List<Appointment> todayVisitAppointments = appointmentService.getAllByDEAptType_Time(user.getEmail(),
                        Constant.appoint_typeVisit,todayTimeBegin,todayTimeEnd);
                model.addAttribute("todayAppointmentsNum",todayTreatAppointments.size()+todayVisitAppointments.size());

                //所有完成的appointment数量 截止今天
                List<Appointment> allDealAppointments = appointmentService.getAllBeforeTodayByDEResult_Time(user.getEmail(),
                        Constant.appoint_resultDeal,todayTimeEnd);
                List<Appointment> appointments = appointmentService.getAllByDoctorE(user.getEmail());
                model.addAttribute("allPatientNumTillToday",allDealAppointments.size());
                //截止今天完成的appointment占总的比例
                if (appointments.size()==0){
                    model.addAttribute("allDealPatientNumRate",0);
                }
                else {
                    model.addAttribute("allDealPatientNumRate",allDealAppointments.size()/appointments.size());
                }

                //病人信息 today upcoming

                //today upcoming appointment显示
                todayTreatAppointments.addAll(todayVisitAppointments);
                model.addAttribute("todayAppointmentsIdMap",
                        ServiceUtil.getMapIdAppointment(todayTreatAppointments));
                //upcoming
                List<Appointment> visitAppointments = appointmentService.getAllByDEAptType_Time(user.getEmail(),
                        Constant.appoint_typeVisit, todayTimeEnd,ServiceUtil.getDateByNowDays(7).toString());
                List<Appointment> treatAppointments = appointmentService.getAllByDEAptType_Time(user.getEmail(),
                        Constant.appoint_typeTreat,todayTimeEnd,ServiceUtil.getDateByNowDays(7).toString());
                //汇总
                visitAppointments.addAll(treatAppointments);

                //today upcoming patientInfo
                List<PatientInfo> patientInfos = new ArrayList<>(patientInfoService.getAllPatientInfoByAppoint(todayTreatAppointments));
                patientInfos.addAll(patientInfoService.getAllPatientInfoByAppoint(visitAppointments));
                model.addAttribute("upcomingAppointmentsIdMap",
                        ServiceUtil.getMapIdAppointment(visitAppointments));
                model.addAttribute("patientInfos",patientInfos);
                //未读消息
                model.addAttribute("message",chatMessageService.sumUnReadByFE(user.getEmail()));
                return "doctor-dashboard";
            }
            else {
                message = "您不是医生";
            }
        }

        return "redirect:/blank-page";
    }

    /**
     * 显示预约信息  未完成：页面显示哪些预约，拒绝是否显示等
     * @param request user
     */
    @RequestMapping("/appointments")
    public String appointments(Model model, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null){
            model.addAttribute("user",user);
            if (user.getRole()==Constant.doctorRole){
                //医生信息
                DoctorInfo doctorInfo = doctorInfoService.getDoctorInfoByE(user.getEmail());
                model.addAttribute("doctorInfo",doctorInfo);
                List<DoctorEdu> doctorEduList = doctorInfoService.getAllDoctorEduByE(user.getEmail());
                model.addAttribute("edu",doctorInfoService.getEdu(doctorEduList));
                //预约信息——只显示预约结果不为拒绝的预约信息
                //dealType 根据处理类型查看预约
                List<String> resultType = Constant.getAptDealType();
                model.addAttribute("resultType",resultType);
                //默认显示第一个类型
                List<Appointment> appointmentsNoDeal =
                        appointmentService.getFutureByDEResultType_BeginTime(user.getEmail(),resultType.get(0),
                                ServiceUtil.nowArrayDateTime()[0]+" "+ServiceUtil.nowArrayDateTime()[1]);
                model.addAttribute("idAppointmentsNoDeal", ServiceUtil.getMapIdAppointment(appointmentsNoDeal));
                List<PatientInfo> patientInfoListNoDeal = patientInfoService.getAllPatientInfoByAppoint(appointmentsNoDeal);
                model.addAttribute("patientInfoListNoDeal",patientInfoListNoDeal);
                //未读消息
                model.addAttribute("message",chatMessageService.sumUnReadByFE(user.getEmail()));

                return "appointments";
            }
            else {
                message = "您不是医生";
            }
        }
        return "redirect:/blank-page";
    }

    @RequestMapping("/my-patients") //未完成
    public String patientList(Model model, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null){
            model.addAttribute("user",user);
            if (user.getRole()==Constant.doctorRole){
                DoctorInfo doctorInfo = doctorInfoService.getDoctorInfoByE(user.getEmail());
                List<DoctorPatient> doctorPatientList = userService.getAllPatientByD_E(user.getEmail());
                List<PatientInfo> patientInfoList = patientInfoService.getPatientInfoByDPList(doctorPatientList);
                model.addAttribute("doctorInfo",doctorInfo);
                model.addAttribute("patientInfoList",patientInfoList);
                List<DoctorEdu> doctorEduList = doctorInfoService.getAllDoctorEduByE(user.getEmail());
                model.addAttribute("edu",doctorInfoService.getEdu(doctorEduList));
                //未读消息
                model.addAttribute("message",chatMessageService.sumUnReadByFE(user.getEmail()));
                return "my-patients";
            }
            else {
                message = "您不是医生";
            }
        }
        return "redirect:/blank-page";
    }


    @RequestMapping("/doctor-visit")
    public String healthVisit(Model model,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null) {
            if (user.getRole()==Constant.doctorRole){
                model.addAttribute("user", user);
                model.addAttribute("doctorInfo",doctorInfoService.getDoctorInfoByE(user.getEmail()));
                List<DoctorEdu> doctorEduList = doctorInfoService.getAllDoctorEduByE(user.getEmail());
                model.addAttribute("edu",doctorInfoService.getEdu(doctorEduList));
                model.addAttribute("message",chatMessageService.sumUnReadByFE(user.getEmail()));

                List<Appointment> appointments = appointmentService.getAllByDEAptType_Result(user.getEmail(),
                        Constant.appoint_typeVisit,Constant.appoint_resultAccept);
                List<DoctorPatient> doctorPatients = userService.getAllPatientByD_E(user.getEmail());
                List<PatientInfo> myPatients = new ArrayList<>();
                for (Appointment appointment:appointments){
                    myPatients.add(patientInfoService.getPatientInfoByE(appointment.getPatient_email()));
                }
                for (DoctorPatient doctorPatient:doctorPatients){
                    myPatients.add(patientInfoService.getPatientInfoByE(doctorPatient.getPatient_email()));
                }
                model.addAttribute("myPatients",myPatients);

                if (myPatients.size()==0){
                    model.addAttribute("visitDoctor",null);
                    model.addAttribute("patientInfo",null);
                }
                else {
                    //doctor的家访-添加
                    List<VisitInfo> visitDoctor = patientInfoService.getVisitInfoByDPE(user.getEmail(),myPatients.get(0).getEmail());
                    model.addAttribute("visitDoctor",ServiceUtil.getMapIdVisit(visitDoctor));
                    model.addAttribute("patientInfo",patientInfoService.getPatientInfoByE(myPatients.get(0).getEmail()));
                }

                return "doctor-visit";
            }
            else {
                message = "您不是医生";
            }
        }
        return "redirect:/blank-page";
    }

    @RequestMapping("/doctor-profile-settings")
    public String doctorSetting(Model model, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null){
            model.addAttribute("user",user);
            if (user.getRole()==Constant.doctorRole){
                DoctorInfo doctorInfo = doctorInfoService.getDoctorInfoByE(user.getEmail());
                if (doctorInfo!=null){
                    List<DoctorEdu> doctorEduList = doctorInfoService.getAllDoctorEduByE(user.getEmail());
                    List<DoctorExp> doctorExpList = doctorInfoService.getAllDoctorExpByE(user.getEmail());
                    model.addAttribute("doctorInfo",doctorInfo);
                    model.addAttribute("doctorEduList",doctorEduList);
                    model.addAttribute("doctorExpList",doctorExpList);
                    model.addAttribute("edu",doctorInfoService.getEdu(doctorEduList));
                    //未读消息
                    model.addAttribute("message",chatMessageService.sumUnReadByFE(user.getEmail()));
                }
                return "doctor-profile-settings";
            }
            else {
                message = "您不是医生";
            }
        }
        return "redirect:/blank-page";
    }



    //医生注册
    @RequestMapping("/doctor-register")
    public String doctorRegister(Model model){
        List<SpecialityInfo> specialityInfos = doccureInfoService.getAllSpecialityInfo();
        model.addAttribute("specialityInfos", specialityInfos);
        return "doctor-register";
    }

    @RequestMapping("/health-analysis")
    public String healthAnalysis(Model model,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null){
            model.addAttribute("user",user);
            if (user.getRole()==Constant.doctorRole){
                DoctorInfo doctorInfo = doctorInfoService.getDoctorInfoByE(user.getEmail());
                if (doctorInfo!=null){
                    List<DoctorEdu> doctorEduList = doctorInfoService.getAllDoctorEduByE(user.getEmail());
                    model.addAttribute("doctorInfo",doctorInfo);
                    model.addAttribute("edu",doctorInfoService.getEdu(doctorEduList));
                    //未读消息
                    model.addAttribute("message",chatMessageService.sumUnReadByFE(user.getEmail()));

                    //我的病人/包括未完成的预约的病人
                    List<Appointment> appointments = appointmentService.getAllByDEAptType_Result(user.getEmail(),
                            Constant.appoint_typeVisit,Constant.appoint_resultAccept);
                    List<DoctorPatient> doctorPatients = userService.getAllPatientByD_E(user.getEmail());
                    Map<String,String> myPatients = new HashMap<>();
                    for (Appointment appointment:appointments){
                        String email = appointment.getPatient_email();
                        if (!myPatients.containsKey(email)){
                            myPatients.put(email,patientInfoService.getPatientInfoByE(appointment.getPatient_email()).getUsername());
                        }
                    }
                    for (DoctorPatient doctorPatient:doctorPatients){
                        String email = doctorPatient.getPatient_email();
                        if (!myPatients.containsKey(email)){
                            myPatients.put(email,patientInfoService.getPatientInfoByE(doctorPatient.getPatient_email()).getUsername());
                        }
                    }
                    model.addAttribute("myPatients",myPatients);
                    model.addAttribute("dataType",Constant.getDataType());

                    //预约统计
                    List<String> appointTypeNum = new ArrayList<>();
                    for (String type:Constant.getAptTypeList()){
                        ObjectNode root = objectMapper.createObjectNode();
                        List<Appointment> appointments1 = appointmentService.getAllByDEAptType(user.getEmail(),type);
                        root.put("name",type);
                        root.put("value",appointments1.size());
                        try {
                            appointTypeNum.add(objectMapper.writeValueAsString(root));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }
                    model.addAttribute("appointTypeNum",appointTypeNum);
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
                    List<String> specialties = new ArrayList<>(specialtyNumMap.keySet());
                    List<Integer> specialtyNum = new ArrayList<>(specialtyNumMap.values());
                    model.addAttribute("specialties",specialties);
                    model.addAttribute("specialtyNum",specialtyNum);
                }
                return "health-analysis";
            }
            else {
                message = "您不是医生";
            }
        }
        return "redirect:/blank-page";
    }

    @RequestMapping("/video-call-doctor") //未完成
    public String videoCallDoctor(Model model, HttpServletRequest request,String patient_email){
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null){
            model.addAttribute("user",user);
            if (user.getRole()==Constant.doctorRole){
                DoctorInfo doctorInfo = doctorInfoService.getDoctorInfoByE(user.getEmail());
                model.addAttribute("doctorInfo",doctorInfo);
                model.addAttribute("toUserEmail",patient_email);
                model.addAttribute("toUserInfo",patientInfoService.getPatientInfoByE(patient_email));
                return "video-call-doctor";
            }
        }
        return "redirect:/blank-page";
    }

    @RequestMapping("/video-call-patient") //未完成
    public String videoCallPatient(Model model, HttpServletRequest request, String doctor_email){
        User user = (User) request.getSession().getAttribute("user");
        System.out.println(doctor_email);
        if (user!=null){
            model.addAttribute("user",user);
            if (user.getRole()==Constant.patientRole){
                PatientInfo patientInfo = patientInfoService.getPatientInfoByE(user.getEmail());
                model.addAttribute("patientInfo",patientInfo);
                model.addAttribute("toUserEmail",doctor_email);
                model.addAttribute("toUserInfo",doctorInfoService.getDoctorInfoByE(doctor_email));
                return "video-call-patient";
            }
        }
        return "redirect:/blank-page";
    }

    /**
     * 用户
     */
    @RequestMapping("/patient-dashboard")
    public String patientDashboard(Model model, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null){
            model.addAttribute("user",user);
            if (user.getRole()==Constant.patientRole) {
                //基本信息
                PatientInfo patientInfo = patientInfoService.getPatientInfoByE(user.getEmail());
                model.addAttribute("patientInfo", patientInfo);
                //未读消息
                model.addAttribute("message",chatMessageService.sumUnReadByFE(user.getEmail()));

                String[] nowArrayDateTime = ServiceUtil.nowArrayDateTime();
                String todayTimeBegin = nowArrayDateTime[0] + " " + nowArrayDateTime[1];
                String todayTimeEnd = nowArrayDateTime[0] + " " + "23:59:59";

                //今天的appointment数量 就诊，家访-->病人人数
                List<Appointment> todayTreatAppointments = appointmentService.getAllByPEAptType_Time(user.getEmail(),
                        Constant.appoint_typeTreat,todayTimeBegin,todayTimeEnd);
                List<Appointment> todayVisitAppointments = appointmentService.getAllByPEAptType_Time(user.getEmail(),
                        Constant.appoint_typeVisit,todayTimeBegin,todayTimeEnd);
                todayTreatAppointments.addAll(todayVisitAppointments);

                model.addAttribute("appointments",ServiceUtil.getMapIdAppointment(todayTreatAppointments));
                model.addAttribute("doctorInfosAppoints",doctorInfoService.getAllDoctorInfoByAppoint(todayTreatAppointments));

                //visit
                List<VisitInfo> visitInfos = patientInfoService.getVisitInfoByPE(user.getEmail());
                model.addAttribute("visitInfos",ServiceUtil.getMapIdVisit(visitInfos));
                model.addAttribute("doctorInfosVisits",doctorInfoService.getDoctorInfoByVisit(visitInfos));

                return "patient-dashboard";
            }
            else {
                message = "您不是病人";
            }
        }
        return "redirect:/blank-page";
    }

    //用户注册
    @RequestMapping("/register")
    public String register(Model model){
        Msg message = new Msg();
        model.addAttribute("message",message);
        return "register";
    }

    @RequestMapping("/doctor-profile")
    public String doctorProfile(Model model, HttpServletRequest request,String email){
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null){
            model.addAttribute("user",user);
            String image_url;
            if (user.getRole()==Constant.patientRole){
                image_url = patientInfoService.getPatientInfoByE(user.getEmail()).getImage_url();
            }
            else {
                image_url = doctorInfoService.getDoctorInfoByE(user.getEmail()).getImage_url();
            }
            model.addAttribute("userImgUrl",image_url);
            model.addAttribute("isLogin",true);
        }
        else {
            model.addAttribute("isLogin",false);
        }
        List<DoctorEdu> doctorEduList = doctorInfoService.getAllDoctorEduByE(email);
        model.addAttribute("doctorEduList", doctorEduList);
        model.addAttribute("edu",doctorInfoService.getEdu(doctorEduList));
        List<DoctorExp> doctorExpList = doctorInfoService.getAllDoctorExpByE(email);
        model.addAttribute("doctorExpList", doctorExpList);
        DoctorInfo doctorInfo = doctorInfoService.getDoctorInfoByE(email);
        model.addAttribute("doctorInfo",doctorInfo);

        return "doctor-profile";
    }

    @RequestMapping("/profile-settings")
    public String profileSettings(Model model, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null){
            model.addAttribute("user",user);
            if (user.getRole()==Constant.patientRole){
                PatientInfo patientInfo = patientInfoService.
                        getPatientInfoByE(user.getEmail());
                if (patientInfo!=null){
                    model.addAttribute("patientInfo",patientInfo);
                }
                //未读消息
                model.addAttribute("message",chatMessageService.sumUnReadByFE(user.getEmail()));

                return "profile-settings";
            }
            else {
                message = "您不是病人";
            }
        }
        return "redirect:/blank-page";
    }

    /**
     * 显示已经预约成功、失败的预约
     * @param model 渲染数据
     * @param request user
     * @return 页面
     */
    @RequestMapping("/booking-success")
    public String bookingSuccess(Model model, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null){
            model.addAttribute("user",user);
            if (user.getRole()==Constant.patientRole){
                //基本信息
                PatientInfo patientInfo = patientInfoService.
                        getPatientInfoByE(user.getEmail());
                model.addAttribute("patientInfo",patientInfo);
                //预订成功、失败 预约
                List<Appointment> successAptList = appointmentService.getAptByPE_Result_IsRead(user.getEmail(),
                        Constant.unread,Constant.appoint_resultAccept);
                List<Appointment> failAptList = appointmentService.getAptByPE_Result_IsRead(user.getEmail(),
                        Constant.unread,Constant.appoint_resultRefuse);
                model.addAttribute("successAptList",ServiceUtil.getMapIdAppointment(successAptList));
                model.addAttribute("failAptList",ServiceUtil.getMapIdAppointment(failAptList));
                List<DoctorInfo> doctorInfos = doctorInfoService.getAllDoctorInfoByAppoint(successAptList);
                doctorInfos.addAll(doctorInfoService.getAllDoctorInfoByAppoint(failAptList));
                model.addAttribute("doctorInfos",doctorInfos);
                return "booking-success";
            }
            else {
                message = "您不是病人";
            }
        }
        return "redirect:/blank-page";
    }

    @RequestMapping("/booking")
    public String checkOut(Model model,HttpServletRequest request,String email){
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null){
            model.addAttribute("user",user);
            if (user.getRole()==Constant.patientRole){
                PatientInfo patientInfo = patientInfoService.
                        getPatientInfoByE(user.getEmail());
                DoctorInfo doctorInfo = doctorInfoService.getDoctorInfoByE(email);
                List<DoctorEdu> doctorEduList = doctorInfoService.getAllDoctorEduByE(doctorInfo.getEmail());
                model.addAttribute("edu",doctorInfoService.getEdu(doctorEduList));
                model.addAttribute("doctorInfo",doctorInfo);
                if (patientInfo!=null){
                    model.addAttribute("patientInfo",patientInfo);
                }
                return "booking";
            }
            else {
                message = "您不是病人";
            }
        }
        return "redirect:/blank-page";
    }

    @RequestMapping("/myAssess")
    public String myAssess(HttpServletRequest request, Model model){
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null){
            if (user.getRole()==Constant.patientRole){
                model.addAttribute("patientInfo",patientInfoService.getPatientInfoByE(user.getEmail()));
                model.addAttribute("user",user);

                List<Assess> haveReadIdMap = assessService.getAssessByPE_IsRead(user.getEmail(),Constant.read);
                List<Assess> unReadIdMap = assessService.getAssessByPE_IsRead(user.getEmail(),Constant.unread);
                unReadIdMap.addAll(haveReadIdMap);
                //评估数据
                model.addAttribute("assessIdMap",ServiceUtil.getMapIdAssess(unReadIdMap));

                List<DoctorInfo> doctorInfos = new ArrayList<>();
                for (Assess assess:unReadIdMap){
                    doctorInfos.add(doctorInfoService.getDoctorInfoByE(assess.getDoctor_email()));
                }
                for (Assess assess:haveReadIdMap){
                    doctorInfos.add(doctorInfoService.getDoctorInfoByE(assess.getDoctor_email()));
                }
                model.addAttribute("doctorInfos",doctorInfos);
                //未读消息
                model.addAttribute("message",chatMessageService.sumUnReadByFE(user.getEmail()));

                return "myAssess";
            }
            else {
                message = "您不是病人";
            }
        }
        return "redirect:/blank-page";
    }

    @RequestMapping("/myHealthRecord")
    public String healthRecord(HttpServletRequest request, Model model){
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null){
            if (user.getRole()==Constant.patientRole){
                model.addAttribute("patientInfo",patientInfoService.getPatientInfoByE(user.getEmail()));
                model.addAttribute("user",user);
                //履历信息
                HealthBasicInfo healthBasicInfo = patientInfoService.getHealthBasicInfoByPE(user.getEmail());
                if (healthBasicInfo==null){
                    healthBasicInfo = new HealthBasicInfo();
                }
                model.addAttribute("healthBasicInfo",healthBasicInfo);

                List<HealthResume> healthResumes = patientInfoService.getHealthResumeByPE(user.getEmail());
                model.addAttribute("recordTypeIdMap",ServiceUtil.getTypeMapIdResume(healthResumes));
                model.addAttribute("disease",Constant.RESUME_DISEASE);
                model.addAttribute("injury",Constant.RESUME_INJURY);
                model.addAttribute("operation",Constant.RESUME_OPERATION);

                //体检
                List<HealthExamInfo> healthExamInfos = patientInfoService.getHealthExamInfoByPE(user.getEmail());
                model.addAttribute("examInfosIdMap",ServiceUtil.getMapIdExamInfo(healthExamInfos));
                //未读消息
                model.addAttribute("message",chatMessageService.sumUnReadByFE(user.getEmail()));
                return "patient-health-record";
            }
            else {
                message = "您不是病人";
            }
        }
        return "redirect:/blank-page";
    }


    // admin
    @RequestMapping("/admin/doctor-apply")
    public String doctorApply(Model model,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null) {
            if (user.getRole()==Constant.adminRole) {
                List<DoctorApply> doctorApplies = userService.getDoctorApplyByStatus(Constant.apply_noDeal);
                model.addAttribute("doctorApplies", doctorApplies);
                String staticFileUrl = request.getScheme() + "://" + request.getServerName() + ":"
                        + serverProperties.getPort() + "/img/file.png";
                model.addAttribute("staticFileUrl", staticFileUrl);
            }
            else {
                message = "您不是管理员";
                return "redirect:/blank-page";
            }
        }
        return "admin/doctor-apply";
    }

    @RequestMapping("admin/login")
    public String adminLogin(){
        return "admin/login";
    }

    @RequestMapping("/admin/specialities")
    public String adminSpecialty(Model model,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null) {
            if (user.getRole()==Constant.adminRole) {
                List<SpecialityInfo> specialityInfos = doccureInfoService.getAllSpecialityInfo();
                model.addAttribute("specialityInfos", specialityInfos);
            }
            else {
                message = "您不是管理员";
                return "redirect:/blank-page";
            }
        }
        return "admin/specialities";
    }
}
