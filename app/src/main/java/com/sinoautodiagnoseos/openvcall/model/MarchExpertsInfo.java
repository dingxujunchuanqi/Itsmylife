package com.sinoautodiagnoseos.openvcall.model;

import java.util.List;

/**
 * 匹配专家的实体类
 * Created by HQ_Demos on 2017/3/12.
 */

public class MarchExpertsInfo {
    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<MartchInfo> getData() {
        return data;
    }

    public void setData(List<MartchInfo> data) {
        this.data = data;
    }

    private int totalCount;
    private List<MartchInfo> data;
}
