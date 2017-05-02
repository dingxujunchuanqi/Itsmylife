package com.sinoautodiagnoseos.entity.Push;

import java.util.List;

/**
 * Created by Lanye on 2017/3/6.
 */

public class Msg {
    public String Account;
    public List<String> Data;
    public String TemplateId;
    public String clientid;
    public MyMessage Extension;

    public void setAccount(String account) {
        Account = account;
    }

    public void setData(List<String> data) {
        Data = data;
    }

    public void setTemplateId(String templateId) {
        TemplateId = templateId;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public void setExtension(MyMessage extension) {
        Extension = extension;
    }

    public String getAccount() {

        return Account;
    }

    public List<String> getData() {
        return Data;
    }

    public String getTemplateId() {
        return TemplateId;
    }

    public String getClientid() {
        return clientid;
    }

    public MyMessage getExtension() {
        return Extension;
    }
}