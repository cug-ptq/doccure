package com.example.doccure.service;

import com.example.doccure.entity.info.SpecialityInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public interface DoccureInfoService {
    List<SpecialityInfo> getAllSpecialityInfo();
    String insertSpecialityInfo(String specialtyInfo, MultipartFile file, HttpServletRequest request);

    String updateSpecialityInfo(String specialtyInfo, MultipartFile file,HttpServletRequest request);

    void deleteSpeciality(int id);
}
