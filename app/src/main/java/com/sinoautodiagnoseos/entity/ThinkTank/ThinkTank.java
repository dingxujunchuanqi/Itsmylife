package com.sinoautodiagnoseos.entity.ThinkTank;

import java.util.List;

/**
 * 智库实体类
 * Created by HQ_Demos on 2017/5/23.
 */

public class ThinkTank {
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

    public List<ResultInfo> getResult() {
        return result;
    }

    public void setResult(List<ResultInfo> result) {
        this.result = result;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    private String errmsg;
    private List<ResultInfo>result;
    private int totalCount;
}
