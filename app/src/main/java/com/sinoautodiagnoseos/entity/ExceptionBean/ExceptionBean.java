package com.sinoautodiagnoseos.entity.ExceptionBean;
public class ExceptionBean extends RuntimeException {
    private int code;
    private String msg;
    public ExceptionBean(int resultCode, String msg) {
        this.code = resultCode;
        this.msg = msg;
    }
    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}
