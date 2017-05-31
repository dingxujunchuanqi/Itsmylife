package com.sinoautodiagnoseos.entity.FaulTranges;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lanye on 2017/3/1.
 * 擅长故障范围实体类
 */

public class FaulTranges  implements Serializable{

    public List<Faul> data;

    public void setData(List<Faul> data) {
        this.data = data;
    }

    public List<Faul> getData() {

        return data;
    }

    public class Faul implements Serializable{
        @Override
        public String toString() {
            return "Faul{" +
                    "select=" + select +
                    ", text='" + text + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }

        public String text;
        public String value;

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        public boolean select;

        public void setText(String text) {
            this.text = text;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getText() {

            return text;
        }

        public String getValue() {
            return value;
        }

    }
}
