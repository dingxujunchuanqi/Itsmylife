package com.sinoautodiagnoseos.entity.Fault;

import java.util.List;

/**
 * 故障范围
 * Created by HQ_Demos on 2017/5/25.
 */

public class Fault {
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

    public List<FaultResult> getResult() {
        return result;
    }

    public void setResult(List<FaultResult> result) {
        this.result = result;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    private String errmsg;
    private List<FaultResult>result;
    private int totalCount;
}
