package com.sinoautodiagnoseos.entity.User;

/**
 * Created by Lanye on 2017/3/2.
 */

public class UserInfo {

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    private int code;
    private String error;

    public int totalCount;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public UserData data;

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }

    public class UserData{
        public String areaNames;
        public String avatar;
        public String birthday;
        public String mobile;
        public String name;
        public String roleName;
        public String stationName;
        public String userId;
        public String userName;
        public String memberId;

        public void setAreaNames(String areaNames) {
            this.areaNames = areaNames;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setStationName(String stationName) {
            this.stationName = stationName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getAreaNames() {

            return areaNames;
        }

        public String getAvatar() {
            return avatar;
        }

        public String getBirthday() {
            return birthday;
        }

        public String getMobile() {
            return mobile;
        }

        public String getName() {
            return name;
        }

        public String getRoleName() {
            return roleName;
        }

        public String getStationName() {
            return stationName;
        }

        public String getUserId() {
            return userId;
        }

        public String getUserName() {
            return userName;
        }

        public OtherInfo otherInfo;

        public OtherInfo getOtherInfo() {
            return otherInfo;
        }

        public void setOtherInfo(OtherInfo otherInfo) {
            this.otherInfo = otherInfo;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getMemberId() {

            return memberId;
        }

        public class OtherInfo{
            public String carBrandName;
            public String skillInfo;
            public String skillRangeName;
            public String starRating;
            public String workYears;

            public String getCarBrandName() {
                return carBrandName;
            }

            public String getSkillInfo() {
                return skillInfo;
            }

            public String getStarRating() {
                return starRating;
            }

            public String getWorkYears() {
                return workYears;
            }

            public String getSkillRangeName() {
                return skillRangeName;
            }

            public void setCarBrandName(String carBrandName) {
                this.carBrandName = carBrandName;
            }

            public void setSkillInfo(String skillInfo) {
                this.skillInfo = skillInfo;
            }

            public void setSkillRangeName(String skillRangeName) {
                this.skillRangeName = skillRangeName;
            }

            public void setStarRating(String starRating) {
                this.starRating = starRating;
            }

            public void setWorkYears(String workYears) {
                this.workYears = workYears;
            }
        }

    }
}
