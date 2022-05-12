package com.example.doccure.dao;

import com.example.doccure.entity.DoctorApply;
import com.example.doccure.entity.DoctorPatient;
import com.example.doccure.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {
    User getUserByEmail_Password(String email,String password);

    User getUserByE(String email);

    User getUserByP(String password);

    void registerUser(User user);

    void updatePassword(String email,String old_password,String new_password);

    void updateUsername(String email, String password, String new_username);

    List<User> getAllDoctorByRole(int role);

    List<User> getAllPatientByRole(int role);

    void deleteUser(String email);

    /**
     * 医生病人关系
     */
    void insertDE(DoctorPatient doctorPatient);
    void deleteByDP_E(String doctor_email,String patient_email);
    DoctorPatient getDoctorPatientByDP_E(String doctor_email,String patient_email);
    List<DoctorPatient> getAllPatientByD_E(String doctor_email);
    List<DoctorPatient> getAllDoctorByP_E(String patient_email);

    //doctor 申请
    void insertApply(DoctorApply doctorApply);
    void deleteApplyFail(int id);
    List<DoctorApply> getDoctorApplyByStatus(int status);
    DoctorApply getDoctorApplyById(int id);
    void updateApplyStatusById(int id,int status);
    void updateApplyResultById(int id,String result);
    DoctorApply getDoctorApplyByDEP_Status(String doctor_email, String doctor_password, int status,int is_new);
    DoctorApply getDoctorApplyByDE_Status(String doctor_email, int status,int is_new);
    void updateApplyIsNewById(String doctor_email);
}
