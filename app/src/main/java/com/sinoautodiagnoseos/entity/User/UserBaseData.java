package com.sinoautodiagnoseos.entity.User;

/**
 * Created by Lanye on 2017/3/14.
 */

public class UserBaseData {
    public String areaId;
    public String areaNames;
    public String avatarId;
    public String avatorUrl;
    public String birthday;
    public String name;

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public void setAreaNames(String areaNames) {
        this.areaNames = areaNames;
    }

    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }

    public void setAvatorUrl(String avatorUrl) {
        this.avatorUrl = avatorUrl;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAreaId() {

        return areaId;
    }

    public String getAreaNames() {
        return areaNames;
    }

    public String getAvatarId() {
        return avatarId;
    }

    public String getAvatorUrl() {
        return avatorUrl;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getName() {
        return name;
    }
}
