package com.sinoautodiagnoseos.entity.RoomInfo;

/**
 * Created by HQ_Demos on 2017/3/24.
 */
public class Data {
    private String roomId;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    private long timestamp;
}
