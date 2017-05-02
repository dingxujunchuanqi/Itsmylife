package com.sinoautodiagnoseos.entity.CallRecord;

import java.util.List;

/**
 * Created by Lanye on 2017/3/1.
 */

public class CallRecord {
    public List<Record> data;
    public int totalCount;

    public List<Record> getData() {
        return data;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setData(List<Record> data) {
        this.data = data;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public class Record{
        public String beginOnUtc;
        public String carBrandInfo;
        public String faultRangeInfo;
        public String inviteUserId;
        public List<Expert> expertList;

        public void setBeginOnUtc(String beginOnUtc) {
            this.beginOnUtc = beginOnUtc;
        }

        public void setCarBrandInfo(String carBrandInfo) {
            this.carBrandInfo = carBrandInfo;
        }

        public void setFaultRangeInfo(String faultRangeInfo) {
            this.faultRangeInfo = faultRangeInfo;
        }

        public void setInviteUserId(String inviteUserId) {
            this.inviteUserId = inviteUserId;
        }

        public void setExpertList(List<Expert> expertList) {
            this.expertList = expertList;
        }

        public String getBeginOnUtc() {

            return beginOnUtc;
        }

        public String getCarBrandInfo() {
            return carBrandInfo;
        }

        public String getFaultRangeInfo() {
            return faultRangeInfo;
        }

        public List<Expert> getExpertList() {
            return expertList;
        }

        public String getInviteUserId() {
            return inviteUserId;
        }

        public class Expert{
            public String avatar;
            public String brandInfo;
            public String nickName;
            public String skillInfo;
            public String stationName;
            public String userId;

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public void setBrandInfo(String brandInfo) {
                this.brandInfo = brandInfo;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public void setSkillInfo(String skillInfo) {
                this.skillInfo = skillInfo;
            }

            public void setStationName(String stationName) {
                this.stationName = stationName;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getAvatar() {

                return avatar;
            }

            public String getBrandInfo() {
                return brandInfo;
            }

            public String getNickName() {
                return nickName;
            }

            public String getSkillInfo() {
                return skillInfo;
            }

            public String getStationName() {
                return stationName;
            }

            public String getUserId() {
                return userId;
            }
        }
    }

}
