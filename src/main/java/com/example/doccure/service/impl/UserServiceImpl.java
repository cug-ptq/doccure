package com.example.doccure.service.impl;

import com.example.doccure.dao.DoctorInfoDao;
import com.example.doccure.dao.PatientInfoDao;
import com.example.doccure.entity.*;
import com.example.doccure.entity.info.DoctorInfo;
import com.example.doccure.entity.info.PatientInfo;
import com.example.doccure.utils.Constant;
import com.example.doccure.utils.FileUtil;
import com.example.doccure.utils.Msg;
import com.example.doccure.dao.UserDao;
import com.example.doccure.service.UserService;
import com.example.doccure.utils.ServiceUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    private ServerProperties serverProperties;
    @Autowired
    private DoctorInfoDao doctorInfoDao;
    @Autowired
    private PatientInfoDao patientInfoDao;
    @Autowired
    private  ObjectMapper objectMapper;

    //存储路径
    @Value("${file.upload.path}")
    private String filePath;
    //访问路径前缀
    @Value("${file.access.path}")
    private String accessPath;
    @Override
    public List<User> getAllDoctorByRole(){
        return userDao.getAllDoctorByRole(Constant.doctorRole);
    }

    @Override
    public List<User> getAllPatientByRole() {
        return userDao.getAllPatientByRole(Constant.patientRole);
    }

    /**
     * 医生病人关系
     */
    @Override
    public void insertDE(DoctorPatient doctorPatient) {
        userDao.insertDE(doctorPatient);
    }

    @Override
    public void deleteByDP_E(String doctor_email, String patient_email) {
        userDao.deleteByDP_E(doctor_email,patient_email);
    }

    @Override
    public DoctorPatient getDoctorPatientByDP_E(String doctor_email, String patient_email) {
        return userDao.getDoctorPatientByDP_E(doctor_email,patient_email);
    }

    @Override
    public List<DoctorPatient> getAllPatientByD_E(String doctor_email) {
        return userDao.getAllPatientByD_E(doctor_email);
    }

    @Override
    public List<DoctorPatient> getAllDoctorByP_E(String patient_email){
        return userDao.getAllDoctorByP_E(patient_email);
    }

    @Override
    public String deleteApplyFail(int id){
        userDao.deleteApplyFail(id);
        return Msg.getMsgJsonCode(1,"操作成功");
    }

    @Override
    public List<DoctorApply> getDoctorApplyByStatus(int status){
        return userDao.getDoctorApplyByStatus(status);
    }


    /**
     * admin 接收医生注册 修改结果 加入user表
     * @param applyInfo 申请记录id等
     * @return 操作结果
     */
    @Override
    public String dealApply(String applyInfo) {
        try {
            DoctorApply temp_doctorApply = objectMapper.readValue(applyInfo,DoctorApply.class);
            DoctorApply doctorApply = userDao.getDoctorApplyById(temp_doctorApply.getId()); // 得到申请信息
            if (temp_doctorApply.getStatus() == 1){
                userDao.updateApplyStatusById(temp_doctorApply.getId(),Constant.apply_accept);
                userDao.registerUser(new User(doctorApply.getUsername(),  // 注册
                        doctorApply.getDoctor_email(),doctorApply.getDoctor_password(),Constant.doctorRole));
                DoctorInfo doctorInfo = new DoctorInfo();
                doctorInfo.setEmail(doctorApply.getDoctor_email());
                doctorInfo.setSpeciality(doctorApply.getSpecialty());
                doctorInfo.setImage_url(doctorApply.getImage_url());
                doctorInfoDao.insertDoctorInfo(doctorInfo);
            }
            else {
                userDao.updateApplyStatusById(temp_doctorApply.getId(),Constant.apply_refuse);
                userDao.deleteUser(doctorApply.getDoctor_email());
                userDao.updateApplyResultById(temp_doctorApply.getId(),temp_doctorApply.getResult());
                doctorInfoDao.deleteDoctorInfo(doctorApply.getDoctor_email());
                doctorInfoDao.deleteExpByE(doctorApply.getDoctor_email());
                doctorInfoDao.deleteEduByE(doctorApply.getDoctor_email());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Msg.getMsgJsonCode(1,"操作成功");
    }

    @Override
    public User getUserByE(String email) {
        return userDao.getUserByE(email);
    }

    @Override
    public User getUserByEmail_Password(String email, String password) {
        return userDao.getUserByEmail_Password(email,password);
    }

    @Override
    public Msg registerPatient(String userInfo, HttpServletRequest request) {
        String msg = "";
        try {
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            User user = objectMapper.readValue(userInfo,User.class);
            if (user.getUsername()!=null&&user.getEmail()!=null&&user.getPassword()!=null){
                //是否注册过
                if (userDao.getUserByE(user.getEmail())!=null){
                    msg = "邮箱已使用";
                }
                else if (userDao.getUserByP(user.getPassword())!=null){
                    msg = "密码已使用";
                }
                else {
                    user.setRole(Constant.patientRole);
                    userDao.registerUser(user);
                    PatientInfo patientInfo = new PatientInfo();
                    patientInfo.setEmail(user.getEmail());patientInfo.setUsername(user.getUsername());
                    patientInfoDao.insertPatientInfo(patientInfo);
                    request.getSession().setAttribute("user",user);
                    return new Msg(1,"","success");
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new Msg(-1,"",msg);
    }

    /**
     * 查询注册结果
     * @param doctor_email 邮箱
     * @return 结果
     */
    @Override
    public String getDoctorApplyByDEP(String doctor_email,String doctor_password){
        DoctorApply doctorApply;
        if (userDao.getDoctorApplyByDEP_Status(doctor_email,doctor_password,Constant.apply_noDeal,1)!=null){
            return Msg.getMsgJsonCode(-1,"您的申请正在被处理，请耐心等待");
        }
        else if ((doctorApply=userDao.getDoctorApplyByDEP_Status(doctor_email,doctor_password,Constant.apply_refuse,1))!=null){
            return Msg.getMsgJsonString(new Msg(-1,"message","<p>您的注册信息认证失败，请重新申请.</p>" + doctorApply.getResult()));
        }
        else if (userDao.getDoctorApplyByDEP_Status(doctor_email,doctor_password,Constant.apply_accept,1)!=null){
            return Msg.getMsgJsonCode(1,"您已经成功注册，请前往登录吧");
        }
        return Msg.getMsgJsonCode(-1,"邮箱输入错误或未注册过");
    }

    /**
     * 注册医生
     * @param userInfo 基本信息
     * @param doctorImg 医生认证照片
     * @param doctorIdentifyFile 医生认证文件
     * @param request IP
     * @return 结果
     */
    @Override
    public Msg registerDoctor(String userInfo, MultipartFile doctorImg,
                              MultipartFile doctorIdentifyFile,HttpServletRequest request) {
        String msg = "";
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(userInfo);
            String username = jsonNode.get("username").asText();
            String email = jsonNode.get("email").asText();
            String password = jsonNode.get("password").asText();
            if (username!=null&&email!=null&&password!=null){
                //是否注册过
                if (userDao.getUserByE(email)!=null){
                    msg = "邮箱已使用";
                }
                else if (userDao.getUserByP(password)!=null){
                    msg = "密码已使用";
                }
                else if (userDao.getDoctorApplyByDE_Status(email,Constant.apply_noDeal,1)!=null) {
                    msg = "该邮箱正在被使用申请注册，请耐心等待";
                }
                else {
                    DoctorApply doctorApply = new DoctorApply();
                    if (doctorImg!=null){
                        String doctorIdentifyImg_url = FileUtil.uploadFile(doctorImg, String.valueOf(serverProperties.getPort()),
                                filePath,accessPath,"",request);
                        doctorApply.setImage_url(doctorIdentifyImg_url);
                    }
                    if (doctorIdentifyFile!=null){
                        String doctorIdentifyFile_url = FileUtil.uploadFile(doctorIdentifyFile, String.valueOf(serverProperties.getPort()),
                                filePath,accessPath,"",request);
                        doctorApply.setFile_url(doctorIdentifyFile_url);
                    }
                    doctorApply.setDoctor_email(email);doctorApply.setUsername(username);
                    doctorApply.setDoctor_password(password);doctorApply.setStatus(Constant.apply_noDeal);
                    doctorApply.setSpecialty(jsonNode.get("specialty").asText());
                    doctorApply.setIs_new(1);
                    String[] nowArrayDateTime = ServiceUtil.nowArrayDateTime();
                    doctorApply.setTime(ServiceUtil.timeStringToTimeStamp(nowArrayDateTime[0]+" "+nowArrayDateTime[1],Constant.timePatternTimeStamp));
                    userDao.updateApplyIsNewById(doctorApply.getDoctor_email());
                    userDao.insertApply(doctorApply);
                    return new Msg(1,"","请等待管理员审核");
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new Msg(-1,"",msg);
    }


    @Override
    public Msg loginUser(String userInfo,HttpServletRequest request) {
        try {
            //解析json字符串信息
            JsonNode jsonNode = objectMapper.readTree(userInfo);
            String email = jsonNode.get("email").asText();
            String password = jsonNode.get("password").asText();
            if (email!=null&&password!=null){
                User user = null;
                //是否注册过
                if ((user=userDao.getUserByEmail_Password(email,password)) !=null){
                    request.getSession().setAttribute("user",user);
                    if (user.getRole()!=Constant.adminRole){
                        return new Msg(1,"","success");
                    }
                    else {
                        return new Msg(1,"admin","success");
                    }
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new Msg(-1,"","邮箱或密码错误");
    }

    @Override
    public Msg changePassword(User user, String info){
        Msg data;
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(info);
            String old_password = jsonNode.get("old_password").asText();
            String new_password = jsonNode.get("new_password").asText();
            String confirm_password = jsonNode.get("confirm_password").asText();
            data = ServiceUtil.changePasswordMsg(user,old_password,new_password,confirm_password);
            if (data.getCode()==1){
                userDao.updatePassword(user.getEmail(),user.getPassword(),new_password);
            }
            return data;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new Msg(-1,"","修改失败");
    }

    @Override
    public void updatePassword(String email, String password, String new_password) {
        userDao.updatePassword(email,password,new_password);
    }

    @Override
    public void updateUsername(String email, String password, String new_username) {
        userDao.updateUsername(email,password,new_username);
    }
}
