package com.sinoautodiagnoseos.entity.Station;

import java.util.List;

/**
 * Created by Lanye on 2017/3/2.
 */

public class Station {
    public int totalCount;
    public List<StationInfo> data;

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setData(List<StationInfo> data) {
        this.data = data;
    }

    public int getTotalCount() {

        return totalCount;
    }

    public List<StationInfo> getData() {
        return data;
    }

    public class StationInfo{
        public String firstChar;
        public String fullName;
        public String id;
        public String name;
        public String stationNo;

        public void setFirstChar(String firstChar) {
            this.firstChar = firstChar;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setStationNo(String stationNo) {
            this.stationNo = stationNo;
        }

        public String getFirstChar() {

            return firstChar;
        }

        public String getFullName() {
            return fullName;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getStationNo() {
            return stationNo;
        }
    }

}
