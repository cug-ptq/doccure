package com.example.doccure.entity.health_basicInfo;

public class AlcoholInfo {
    private Integer alcohol_rate;
    private Double alcohol_consumption;
    private String begin_time;
    private String endTime;

    public AlcoholInfo() {
        alcohol_rate = 0;
        alcohol_consumption = 0.0;
        begin_time = "";
        endTime = "";
    }

    public AlcoholInfo(Integer alcohol_rate, Double alcohol_consumption, String begin_time, String endTime) {
        this.alcohol_rate = alcohol_rate;
        this.alcohol_consumption = alcohol_consumption;
        this.begin_time = begin_time;
        this.endTime = endTime;
    }

    public Integer getAlcohol_rate() {
        return alcohol_rate;
    }

    public void setAlcohol_rate(Integer alcohol_rate) {
        this.alcohol_rate = alcohol_rate;
    }

    public Double getAlcohol_consumption() {
        return alcohol_consumption;
    }

    public void setAlcohol_consumption(Double alcohol_consumption) {
        this.alcohol_consumption = alcohol_consumption;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
