package com.sinoautodiagnoseos.entity.Station;

import java.util.List;

/**
 * Created by Lanye on 2017/3/2.
 */

public class StationList {
    public int totalCount;
    public List<Stations> data;

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setData(List<Stations> data) {
        this.data = data;
    }

    public int getTotalCount() {

        return totalCount;
    }

    public List<Stations> getData() {
        return data;
    }

    public class Stations{
        public String id;
        public String name;
        public String firstChar;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getFirstChar() {
            return firstChar;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setFirstChar(String firstChar) {
            this.firstChar = firstChar;
        }
    }
}
