package com.sinoautodiagnoseos.entity.CaseDetail;

import java.util.List;

/**
 * 案例详情实体类
 * Created by HQ_Demos on 2017/5/23.
 */

public class CaseDetail {
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

    public List<CaseDetailResult> getResult() {
        return result;
    }

    public void setResult(List<CaseDetailResult> result) {
        this.result = result;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    private String errmsg;
    private List<CaseDetailResult>result;
    private int totalCount;
}
