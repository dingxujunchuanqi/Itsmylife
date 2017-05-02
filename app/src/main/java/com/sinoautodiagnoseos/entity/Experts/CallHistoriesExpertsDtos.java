package com.sinoautodiagnoseos.entity.Experts;

/**
 * Created by HQ_Demos on 2017/3/8.
 */
public class CallHistoriesExpertsDtos {
    private String beginOnUtc;//接通时间

    public String getBeginOnUtc() {
        return beginOnUtc;
    }

    public void setBeginOnUtc(String beginOnUtc) {
        this.beginOnUtc = beginOnUtc;
    }

    public int getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(int callStatus) {
        this.callStatus = callStatus;
    }

    public String getCallhistoryId() {
        return callhistoryId;
    }

    public void setCallhistoryId(String callhistoryId) {
        this.callhistoryId = callhistoryId;
    }

    public String getEndOnUtc() {
        return endOnUtc;
    }

    public void setEndOnUtc(String endOnUtc) {
        this.endOnUtc = endOnUtc;
    }

    public String getExpertId() {
        return expertId;
    }

    public void setExpertId(String expertId) {
        this.expertId = expertId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) {
        this.starRating = starRating;
    }

    private int callStatus;//本次专家接听状态 1-接听 2-挂断 3-30s未接听
    private String callhistoryId;//呼叫记录ID
    private String endOnUtc;//挂断时间
    private String expertId;//专家Id

    public String getExpertNum() {
        return expertNum;
    }

    public void setExpertNum(String expertNum) {
        this.expertNum = expertNum;
    }

    private String expertNum;//专家账号（registionsId）
    private String memberId;//用户ID
    private int starRating;//评星值

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    private String name;
    private String avatar;
}
