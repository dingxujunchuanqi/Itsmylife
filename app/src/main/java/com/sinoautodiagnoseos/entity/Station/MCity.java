package com.sinoautodiagnoseos.entity.Station;

/**
 * Created by dingxujun on 2017/5/17.
 */

public class MCity {

    private String cityPinyin;
    private String cityName;
    private String firstPinYin;


    public String getCityPinyin() {
        return cityPinyin;
    }

    public void setCityPinyin(String cityPinyin) {
        this.cityPinyin = cityPinyin;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }


    public String getFirstPinYin() {
        firstPinYin = cityPinyin.substring(0, 1);
        return firstPinYin;
    }
}

