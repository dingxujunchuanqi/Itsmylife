package com.sinoautodiagnoseos.entity.Experts;

import java.util.List;

/**
 * 评分实体类
 * Created by HQ_Demos on 2017/3/8.
 */

public class ListResult {
    private List<CallHistoriesExpertsDtos> callHistoriesExpertsDtos;

    public List<CallHistoriesExpertsDtos> getCallHistoriesExpertsDtos() {
        return callHistoriesExpertsDtos;
    }

    public void setCallHistoriesExpertsDtos(List<CallHistoriesExpertsDtos> callHistoriesExpertsDtos) {
        this.callHistoriesExpertsDtos = callHistoriesExpertsDtos;
    }

    public String getCallHistoryId() {
        return callHistoryId;
    }

    public void setCallHistoryId(String callHistoryId) {
        this.callHistoryId = callHistoryId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    private String callHistoryId;//呼叫历史ID
    private String result;//解决结果
}
