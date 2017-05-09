package com.sinoautodiagnoseos.openvcall.model;

/**
 * Created by HQ_Demos on 2017/3/24.
 */

public class UploadDatas {
    private String callHistoryId;

    public String getCallHistoryId() {
        return callHistoryId;
    }

    public void setCallHistoryId(String callHistoryId) {
        this.callHistoryId = callHistoryId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    private String fileId;
    private String fileName;

    public double getFileSize() {
        return fileSize;
    }

    public void setFileSize(double fileSize) {
        this.fileSize = fileSize;
    }

    private double fileSize;
    private String fileUrl;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    private String contentType;
}
