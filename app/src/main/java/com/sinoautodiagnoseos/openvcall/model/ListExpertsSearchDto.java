package com.sinoautodiagnoseos.openvcall.model;

import java.util.List;

/**
 * 匹配专家实体类
 * Created by HQ_Demos on 2017/3/7.
 */

public class ListExpertsSearchDto {
    public String getCarBrandId() {
        return carBrandId;
    }

    public void setCarBrandId(String carBrandId) {
        this.carBrandId = carBrandId;
    }

    public List<Faults> getFaults() {
        return faults;
    }

    public void setFaults(List<Faults> faults) {
        this.faults = faults;
    }

    private String carBrandId;
    private List<Faults> faults;

}
