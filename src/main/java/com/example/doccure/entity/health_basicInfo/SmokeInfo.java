package com.example.doccure.entity.health_basicInfo;

public class SmokeInfo {
    private Integer smoke_rate;
    private String beginTime;
    private String endTime;

    public SmokeInfo() {
        smoke_rate = 0;
        beginTime = "";
        endTime = "";
    }

    public SmokeInfo(Integer smoke_rate, String beginTime, String endTime) {
        this.smoke_rate = smoke_rate;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    public Integer getSmoke_rate() {
        return smoke_rate;
    }

    public void setSmoke_rate(Integer smoke_rate) {
        this.smoke_rate = smoke_rate;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
