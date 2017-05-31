package com.sinoautodiagnoseos.entity.CarBrands;

import java.util.List;
import java.util.Map;

/**
 * 智库车辆品牌实体类
 * Created by HQ_Demons on 2017/3/2.
 */

public class ZKCarBrands {
    private int errcode;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    private String errmsg;
    private int totalCount;
    public Map<String, List<Brand>> getData() {
        return result;
    }

    public void setData(Map<String, List<Brand>> data) {
        this.result = data;
    }

    public Map<String,List<Brand>>result;
        public class Brand {
            public String value;
            public String text;

            public String getValue() {
                return value;
            }

            public String getText() {
                return text;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public void setText(String text) {
                this.text = text;
            }
        }
//    }
}
