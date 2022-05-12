package com.example.doccure.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * 用于WangEditor上传图片的JSON回调
 */
public class AssessImg {
    private Integer errno;
    private List<String> data;

    public AssessImg() {
        errno = 0;
    }

    public AssessImg(Integer error, List<String> data) {
        this.errno = error;
        this.data = data;
    }

    public Integer getErrno() {
        return errno;
    }

    public void setErrno(Integer errno) {
        this.errno = errno;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public static String SuccessJson(AssessImg assessImg){
        ObjectMapper objectMapper = new ObjectMapper();
        String data = "";
        try {
            data = objectMapper.writeValueAsString(assessImg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return data;
    }
}
