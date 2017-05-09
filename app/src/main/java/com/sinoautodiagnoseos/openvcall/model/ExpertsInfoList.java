package com.sinoautodiagnoseos.openvcall.model;

import java.util.List;

/**
 * 存储专家列表信息实体类
 * Created by HQ_Demos on 2017/3/9.
 */

public class ExpertsInfoList {
    public List<ExpertsData> getData() {
        return data;
    }

    public void setData(List<ExpertsData> data) {
        this.data = data;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    private List<ExpertsData> data;
    private int totalCount;

}
