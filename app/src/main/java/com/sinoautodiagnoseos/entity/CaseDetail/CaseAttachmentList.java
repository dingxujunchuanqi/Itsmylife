package com.sinoautodiagnoseos.entity.CaseDetail;

/**
 * Created by HQ_Demos on 2017/5/23.
 */
public class CaseAttachmentList {
    private int attachmentId;

    public int getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getAttachmentSubffix() {
        return attachmentSubffix;
    }

    public void setAttachmentSubffix(String attachmentSubffix) {
        this.attachmentSubffix = attachmentSubffix;
    }

    public int getAttachmentSize() {
        return attachmentSize;
    }

    public void setAttachmentSize(int attachmentSize) {
        this.attachmentSize = attachmentSize;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    private int caseId;
    private String attachmentName;
    private String attachmentSubffix;
    private int attachmentSize;
    private String attachmentUrl;
}
