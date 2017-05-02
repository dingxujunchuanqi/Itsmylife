package com.sinoautodiagnoseos.entity.User;

public class User {
    public String userName;
    public String password;
    public String id;
   // public List<String> userName;

    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {

        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }


}
