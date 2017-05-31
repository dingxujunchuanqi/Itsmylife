package com.sinoautodiagnoseos.entity.Brand;

/**
 * Created by HQ_Demos on 2017/5/25.
 */
public class BrandResult {
    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDmlTime() {
        return dmlTime;
    }

    public void setDmlTime(String dmlTime) {
        this.dmlTime = dmlTime;
    }

    public int getDmlFlag() {
        return dmlFlag;
    }

    public void setDmlFlag(int dmlFlag) {
        this.dmlFlag = dmlFlag;
    }

    private int brandId;
    private String brandName;
    private String enName;
    private String firstChar;
    private String logoUrl;
    private String createTime;
    private String dmlTime;
    private int dmlFlag;
}
