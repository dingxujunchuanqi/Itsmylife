package com.sinoautodiagnoseos.entity.SimpleData;

/**
 * Created by HQ_Demos on 2017/5/10.
 */

public class SimpleData {
    int type;
    String string;


    public SimpleData(int type, String string) {
        this.type = type;
        this.string = string;
    }

    @Override
    public String toString() {
        return "SimpleData{" +
                "type=" + type +
                ", string='" + string + '\'' +
                '}';
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
