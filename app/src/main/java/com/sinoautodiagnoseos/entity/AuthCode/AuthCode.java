package com.sinoautodiagnoseos.entity.AuthCode;

/**
 * Created by Lanye on 2017/2/24.
 */

public class AuthCode {
    public String Account;
    public String Key;
    public String TemplateId;
    public String ClientId;


    public void setAccount(String account) {
        Account = account;
    }

    public void setKey(String key) {
        Key = key;
    }

    public void setTemplateId(String templateId) {
        TemplateId = templateId;
    }

    public void setClientId(String clientId) {
        ClientId = clientId;
    }
}
