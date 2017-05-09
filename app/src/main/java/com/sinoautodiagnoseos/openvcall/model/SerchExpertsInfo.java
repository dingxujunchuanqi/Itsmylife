package com.sinoautodiagnoseos.openvcall.model;

/**
 * Created by HQ_Demos on 2017/3/27.
 */
public class SerchExpertsInfo {
    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    private String roomId;
    private long timestamp;
}
