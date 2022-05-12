package com.example.doccure.entity.health_basicInfo;

public class PhysicalInfo {
    private Integer physical_rate;
    private Integer physical_time;
    private String physical_way;

    public PhysicalInfo() {
        physical_rate = 0;
        physical_time = 0;
        physical_way = "";
    }

    public Integer getPhysical_rate() {
        return physical_rate;
    }

    public void setPhysical_rate(Integer physical_rate) {
        this.physical_rate = physical_rate;
    }

    public Integer getPhysical_time() {
        return physical_time;
    }

    public void setPhysical_time(Integer physical_time) {
        this.physical_time = physical_time;
    }

    public String getPhysical_way() {
        return physical_way;
    }

    public void setPhysical_way(String physical_way) {
        this.physical_way = physical_way;
    }

    public PhysicalInfo(Integer physical_rate, Integer physical_time, String physical_way) {
        this.physical_rate = physical_rate;
        this.physical_time = physical_time;
        this.physical_way = physical_way;
    }
}
