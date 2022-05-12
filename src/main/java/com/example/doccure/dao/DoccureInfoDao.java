package com.example.doccure.dao;

import com.example.doccure.entity.info.SpecialityInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DoccureInfoDao {
    //specialty
    List<SpecialityInfo> getAllSpecialityInfo();
    SpecialityInfo getSpecialityInfoById(int id);
    void insertSpecialityInfo(SpecialityInfo specialityInfo);

    void updateSpecialityInfo(@Param("specialityInfo") SpecialityInfo specialityInfo);

    void deleteSpeciality(int id);
}
