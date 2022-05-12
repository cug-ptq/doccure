package com.example.doccure.entity.health_basicInfo;

/**
 * 一般信息
 */
public class NormalHealthyInfo {
    private String high;
    private String weight;
    private String left_sight;
    private String right_sight;

    public NormalHealthyInfo() {
        high = "";
        weight = "";
        left_sight = "";
        right_sight = "";
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getLeft_sight() {
        return left_sight;
    }

    public void setLeft_sight(String left_sight) {
        this.left_sight = left_sight;
    }

    public String getRight_sight() {
        return right_sight;
    }

    public void setRight_sight(String right_sight) {
        this.right_sight = right_sight;
    }

    public NormalHealthyInfo(String high, String weight, String left_sight, String right_sight) {
        this.high = high;
        this.weight = weight;
        this.left_sight = left_sight;
        this.right_sight = right_sight;
    }
}
