package com.sinoautodiagnoseos.entity.ExceptionBean;

import java.util.List;

/**
 * Created by Lanye on 2017/2/23.
 */

public class ErrMessage {
    public List<String> code;
    public List<String> passWord;
    public List<String> oldPassword;
    public String content;
    public List<String> userName;
    public String error;

    public void setCode(List<String> code) {
        this.code = code;
    }

    public List<String> getCode() {

        return code;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {

        return content;
    }

    public void setUserName(List<String> userName) {
        this.userName = userName;
    }

    public void setPassWord(List<String> passWord) {
        this.passWord = passWord;
    }

    public List<String> getUserName() {

        return userName;
    }

    public List<String> getPassWord() {
        return passWord;
    }

    public void setOldPassword(List<String> oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void setNewPassword(List<String> newPassword) {
        this.newPassword = newPassword;
    }

    public List<String> getNewPassword() {

        return newPassword;
    }

    public List<String> getOldPassword() {
        return oldPassword;
    }

    public List<String> newPassword;

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {

        return error;
    }
}
