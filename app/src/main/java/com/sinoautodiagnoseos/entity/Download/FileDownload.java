package com.sinoautodiagnoseos.entity.Download;

/**
 * Created by HQ_Demos on 2017/4/12.
 */

public class FileDownload {
    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private String fileUrl;
    private String fileName;
}
