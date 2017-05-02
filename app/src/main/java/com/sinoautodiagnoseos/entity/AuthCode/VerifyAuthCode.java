package com.sinoautodiagnoseos.entity.AuthCode;

/**
 * Created by Lanye on 2017/2/26.
 */

public class VerifyAuthCode {
    public void setAccount(String account) {
        this.account = account;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAccount() {
        return account;
    }

    public String getTemplateId() {
        return templateId;
    }

    public String getCode() {
        return code;
    }

    public String account;
    public String templateId;
    public String code;
}