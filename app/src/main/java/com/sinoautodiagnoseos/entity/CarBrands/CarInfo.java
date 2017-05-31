package com.sinoautodiagnoseos.entity.CarBrands;

import java.io.Serializable;

/**
 * Created by HQ_Demos on 2017/5/12.
 */

public class CarInfo implements SideBase,Serializable{
    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarValue() {
        return carValue;
    }

    public void setCarValue(String carValue) {
        this.carValue = carValue;
    }

    public String getCarHeader() {
        return carHeader;
    }

    public void setCarHeader(String carHeader) {
        this.carHeader = carHeader;
    }

    private String carName;//车名
    private String carValue;//车id
    private String carHeader;//首字母

    public CarInfo(String carName,String carHeader,String carValue){
        this.carName=carName;
        this.carHeader=carHeader;
        this.carValue=carValue;
    }

    /**
     * 获取索引
     *
     * @return
     */
    @Override
    public String getLetterName() {
        return carHeader;
    }
}
