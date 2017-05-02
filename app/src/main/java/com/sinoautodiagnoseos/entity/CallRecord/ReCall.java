package com.sinoautodiagnoseos.entity.CallRecord;

/**
 * Created by Lanye on 2017/3/20.
 */

public class ReCall {
    public String beginOnUtc;
    public String callhistoryId;
    public String endOnUtc;
    public String expertId;
    public String expertNum;
    public String memberId;
    public int callSource;
    public int callStatus;
    public boolean invited;

    public void setBeginOnUtc(String beginOnUtc) {
        this.beginOnUtc = beginOnUtc;
    }

    public void setCallhistoryId(String callhistoryId) {
        this.callhistoryId = callhistoryId;
    }

    public void setEndOnUtc(String endOnUtc) {
        this.endOnUtc = endOnUtc;
    }

    public void setExpertId(String expertId) {
        this.expertId = expertId;
    }

    public void setExpertNum(String expertNum) {
        this.expertNum = expertNum;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void setCallSource(int callSource) {
        this.callSource = callSource;
    }

    public void setCallStatus(int callStatus) {
        this.callStatus = callStatus;
    }

    public void setInvited(boolean invited) {
        this.invited = invited;
    }

    public String getBeginOnUtc() {

        return beginOnUtc;
    }

    public String getCallhistoryId() {
        return callhistoryId;
    }

    public String getEndOnUtc() {
        return endOnUtc;
    }

    public String getExpertId() {
        return expertId;
    }

    public String getExpertNum() {
        return expertNum;
    }

    public String getMemberId() {
        return memberId;
    }

    public int getCallSource() {
        return callSource;
    }

    public int getCallStatus() {
        return callStatus;
    }

    public boolean isInvited() {
        return invited;
    }
}
