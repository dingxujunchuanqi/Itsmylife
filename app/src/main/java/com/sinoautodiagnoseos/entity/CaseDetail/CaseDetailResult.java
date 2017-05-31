package com.sinoautodiagnoseos.entity.CaseDetail;

import java.util.List;

/**
 * Created by HQ_Demos on 2017/5/23.
 */
public class CaseDetailResult {
    private int caseCategory;//类型
    private short caseType;
    private int carYear;
    private int caseId;
    private String title;
    private int contentId;

    public int getCaseCategory() {
        return caseCategory;
    }

    public void setCaseCategory(int caseCategory) {
        this.caseCategory = caseCategory;
    }

    public short getCaseType() {
        return caseType;
    }

    public void setCaseType(short caseType) {
        this.caseType = caseType;
    }

    public int getCarYear() {
        return carYear;
    }

    public void setCarYear(int carYear) {
        this.carYear = carYear;
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getFaultId() {
        return faultId;
    }

    public void setFaultId(int faultId) {
        this.faultId = faultId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getDmlFlag() {
        return dmlFlag;
    }

    public void setDmlFlag(int dmlFlag) {
        this.dmlFlag = dmlFlag;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getFaultName() {
        return faultName;
    }

    public void setFaultName(String faultName) {
        this.faultName = faultName;
    }

    public String getCaseCategoryName() {
        return caseCategoryName;
    }

    public void setCaseCategoryName(String caseCategoryName) {
        this.caseCategoryName = caseCategoryName;
    }

    public String getCaseTypeName() {
        return caseTypeName;
    }

    public void setCaseTypeName(String caseTypeName) {
        this.caseTypeName = caseTypeName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<CaseAttachmentList> getCaseAttachmentList() {
        return caseAttachmentList;
    }

    public void setCaseAttachmentList(List<CaseAttachmentList> caseAttachmentList) {
        this.caseAttachmentList = caseAttachmentList;
    }

    private int brandId;
    private int faultId;
    private String keyword;
    private boolean isEnable;
    private int memberId;
    private String creator;
    private String createTime;
    private int dmlFlag;
    private String brandName;
    private String faultName;
    private String caseCategoryName;
    private String caseTypeName;
    private String content;
    private List<CaseAttachmentList> caseAttachmentList;
}
