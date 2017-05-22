package com.sinoautodiagnoseos.entity.User;

import java.io.Serializable;
import java.util.List;

/**
 *  技师工种实体类
 * Created by Lanye on 2017/3/14.
 */

public class Skill implements Serializable {
    private List<DataBean> data ;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * value : 4
         * text : 美容
         */

        private String value;
        private String text;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
