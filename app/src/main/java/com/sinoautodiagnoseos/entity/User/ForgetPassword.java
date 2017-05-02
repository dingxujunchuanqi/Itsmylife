package com.sinoautodiagnoseos.entity.User;

/**
 * Created by Lanye on 2017/3/3.
 */

public class ForgetPassword {
    public String userName;
    public String newPassword;
    public String verificationCode;

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getVerificationCode() {

        return verificationCode;
    }

    public String getUserName() {
        return userName;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
