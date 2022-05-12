package com.example.doccure.service.impl;

import com.example.doccure.dao.DoccureInfoDao;
import com.example.doccure.entity.info.SpecialityInfo;
import com.example.doccure.service.DoccureInfoService;
import com.example.doccure.utils.FileUtil;
import com.example.doccure.utils.Msg;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class DoccureInfoServiceImpl implements DoccureInfoService {
    @Autowired
    private DoccureInfoDao doccureInfoDao;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ServerProperties serverProperties;
    @Value("${file.upload.path}")
    private String filePath;
    //访问路径前缀
    @Value("${file.access.path}")
    private String accessPath;

    @Override
    public List<SpecialityInfo> getAllSpecialityInfo() {
        return doccureInfoDao.getAllSpecialityInfo();
    }

    @Override
    public String insertSpecialityInfo(String specialtyInfo, MultipartFile file, HttpServletRequest request) {
        SpecialityInfo specialityInfo1;
        try {
            specialityInfo1 = objectMapper.readValue(specialtyInfo, SpecialityInfo.class);
            if (file!=null){
                specialityInfo1.setImage_url(FileUtil.uploadFile(file,
                        String.valueOf(serverProperties.getPort()),filePath,accessPath,"",request));
            }
            doccureInfoDao.insertSpecialityInfo(specialityInfo1);
            return Msg.getMsgJsonCode(1, SpecialityInfo.getJsonString(specialityInfo1));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String updateSpecialityInfo(String specialtyInfo,MultipartFile file,HttpServletRequest request) {
        SpecialityInfo specialityInfo1;
        try {
            specialityInfo1 = objectMapper.readValue(specialtyInfo, SpecialityInfo.class);
            if (file!=null){
                specialityInfo1.setImage_url(FileUtil.uploadFile(file,
                        String.valueOf(serverProperties.getPort()),filePath,accessPath,"",request));
            }
            else {
                specialityInfo1.setImage_url(doccureInfoDao.getSpecialityInfoById(specialityInfo1.getId()).getImage_url());
            }
            doccureInfoDao.updateSpecialityInfo(specialityInfo1);
            return Msg.getMsgJsonCode(1, SpecialityInfo.getJsonString(specialityInfo1));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void deleteSpeciality(int id) {
        doccureInfoDao.deleteSpeciality(id);
    }
}
