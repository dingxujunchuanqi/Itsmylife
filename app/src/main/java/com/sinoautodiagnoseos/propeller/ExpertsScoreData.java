package com.sinoautodiagnoseos.propeller;

/**
 * 评分专家的信息
 * Created by HQ_Demos on 2017/2/20.
 */

public class ExpertsScoreData {
    @Override
    public String toString() {
        return "ExpertsScoreData{" +
                "experts_avatar=" + experts_avatar +
                ", experts_name='" + experts_name + '\'' +
                ", ratingCount=" + ratingCount +
                '}';
    }

    private int experts_avatar;//专家头像
    private String experts_name;//专家姓名
    private float ratingCount;//星星的个数

    public float getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(float ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Integer getExperts_avatar() {
        return experts_avatar;
    }

    public void setExperts_avatar(Integer experts_avatar) {
        this.experts_avatar = experts_avatar;
    }

    public String getExperts_name() {
        return experts_name;
    }

    public void setExperts_name(String experts_name) {
        this.experts_name = experts_name;
    }
}
