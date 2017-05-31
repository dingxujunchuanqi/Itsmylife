package com.sinoautodiagnoseos.entity.Brand;

import java.util.List;

/**
 * 车辆品牌
 * Created by HQ_Demos on 2017/5/25.
 */

public class Brand {
    private int errcode;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public List<BrandResult> getResult() {
        return result;
    }

    public void setResult(List<BrandResult> result) {
        this.result = result;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    private String errmsg;
    private List<BrandResult>result;
    private int totalCount;
}
