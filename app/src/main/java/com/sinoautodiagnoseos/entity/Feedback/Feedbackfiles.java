package com.sinoautodiagnoseos.entity.Feedback;

/**
 * Created by Lanye on 2017/3/9.
 */

public class Feedbackfiles {
    public String fileName;
    public String contentType;
    public String fileId;
    public String fileUrl;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {

        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileId() {
        return fileId;
    }

    public String getFileUrl() {
        return fileUrl;
    }
}
