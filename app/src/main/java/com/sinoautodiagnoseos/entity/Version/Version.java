package com.sinoautodiagnoseos.entity.Version;

/**
 * Created by Lanye on 2017/3/7.
 */

public class Version {
    public String appId;
    public String platform;
    public String versionNo;
    public String forceUpdate;
    public String build;

    public String getAppId() {
        return appId;
    }

    public String getPlatform() {
        return platform;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public String getForceUpdate() {
        return forceUpdate;
    }

    public String getBuild() {
        return build;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public void setForceUpdate(String forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public void setBuild(String build) {
        this.build = build;
    }
}
