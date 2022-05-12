package com.example.doccure.service;

import com.example.doccure.entity.DoctorApply;
import com.example.doccure.entity.DoctorPatient;
import com.example.doccure.utils.Msg;
import com.example.doccure.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public interface UserService {
    User getUserByE(String email);
    User getUserByEmail_Password(String email, String password);
    Msg registerPatient(String userInfo, HttpServletRequest request);

    Msg registerDoctor(String userInfo, MultipartFile doctorImg, MultipartFile doctorIdentifyFile
            , HttpServletRequest request);
    Msg loginUser(String userInfo, HttpServletRequest request);

    Msg changePassword(User user, String info);

    void updatePassword(String old_email, String password, String new_password);
    void updateUsername(String email, String password, String new_username);
    List<User> getAllDoctorByRole();
    List<User> getAllPatientByRole();
    /**
     * 医生病人关系
     */
    void insertDE(DoctorPatient doctorPatient);
    void deleteByDP_E(String doctor_email,String patient_email);
    DoctorPatient getDoctorPatientByDP_E(String doctor_email,String patient_email);
    List<DoctorPatient> getAllPatientByD_E(String doctor_email);
    List<DoctorPatient> getAllDoctorByP_E(String patient_email);

    String deleteApplyFail(int id);

    //doctor apply
    List<DoctorApply> getDoctorApplyByStatus(int status);

    String dealApply(String applyInfo);
    String getDoctorApplyByDEP(String doctor_email,String doctor_password);
}
