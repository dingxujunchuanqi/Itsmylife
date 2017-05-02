package com.sinoautodiagnoseos.entity.Area;

import java.util.List;

/**
 * Created by Lanye on 2017/4/1.
 */

public class Area {
    public List<Procince> data;

    public void setData(List<Procince> data) {
        this.data = data;
    }

    public List<Procince> getData() {
        return data;
    }

    public class Procince {
        public String id;
        public String parentId;
        public String areaCode;
        public String name;
        public String shortName;
        public String pinyin;
        public String cityCode;
        public String zipCode;
        public String firstChar;
        public int levelType;

        public String getId() {
            return id;
        }

        public String getParentId() {
            return parentId;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public String getName() {
            return name;
        }

        public String getPinyin() {
            return pinyin;
        }

        public String getShortName() {
            return shortName;
        }

        public String getFirstChar() {
            return firstChar;
        }

        public int getLevelType() {
            return levelType;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public void setFirstChar(String firstChar) {
            this.firstChar = firstChar;
        }

        public void setLevelType(int levelType) {
            this.levelType = levelType;
        }

        public String getCityCode() {
            return cityCode;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }
    }
}
