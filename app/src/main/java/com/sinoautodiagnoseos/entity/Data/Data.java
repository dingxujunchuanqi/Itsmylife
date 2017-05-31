package com.sinoautodiagnoseos.entity.Data;

/**
 * 资料
 * Created by HQ_Demos on 2017/5/25.
 */

public class Data {

    public Data(String text,String value){
        this.text=text;
        this.value=value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private String value;
    private String text;
}
