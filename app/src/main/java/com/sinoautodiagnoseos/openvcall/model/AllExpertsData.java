package com.sinoautodiagnoseos.openvcall.model;

/**
 * 评分专家的信息
 * Created by HQ_Demos on 2017/2/20.
 */

public class AllExpertsData {
    private int experts_avatar;//专家头像
    private String experts_name;//专家姓名

    public AllExpertsData(int avatar, String name) {
        this.experts_avatar=avatar;
        this.experts_name=name;
    }

    @Override
    public String toString() {
        return "ExpertsScoreData{" +
                "experts_avatar='" + experts_avatar + '\'' +
                ", experts_name='" + experts_name + '\'' +
                '}';
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
