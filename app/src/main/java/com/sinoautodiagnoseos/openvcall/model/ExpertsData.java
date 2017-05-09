package com.sinoautodiagnoseos.openvcall.model;

import java.io.Serializable;

/**
 * Created by HQ_Demos on 2017/3/9.
 */
public class ExpertsData implements Serializable {
    private String id;//专家的Id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStarRating() {
        return starRating;
    }

    public void setStarRating(String starRating) {
        this.starRating = starRating;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getAvatorUrl() {
        return avatorUrl;
    }

    public void setAvatorUrl(String avatorUrl) {
        this.avatorUrl = avatorUrl;
    }

    private String userId;//专家的用户Id
    private String name;
    private String mobile;
    private String starRating;
    private String orderQuantity;
    private String avatorUrl;

    public String getCallHistoryId() {
        return callHistoryId;
    }

    public void setCallHistoryId(String callHistoryId) {
        this.callHistoryId = callHistoryId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    private String callHistoryId;//呼叫记录历史Id
    private String memberId;//专家的memberId
    private String roomId;

}
