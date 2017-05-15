package com.sinoautodiagnoseos.entity.FaulTranges;

import java.util.List;

/**
 * Created by Lanye on 2017/3/1.
 * 故障范围
 */

public class FaulTranges {

    public List<Faul> data;

    public void setData(List<Faul> data) {
        this.data = data;
    }

    public List<Faul> getData() {

        return data;
    }

    public class Faul{
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
