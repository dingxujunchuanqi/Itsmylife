package com.sinoautodiagnoseos.entity.User;

/**
 * Created by Lanye on 2017/3/2.
 */

public class UserState {
    public int totalCount;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public class data{
        public int onlineStation;

        public int getOnlineStation() {
            return onlineStation;
        }

        public void setOnlineStation(int onlineStation) {
            this.onlineStation = onlineStation;
        }
    }
}
