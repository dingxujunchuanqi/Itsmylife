package com.sinoautodiagnoseos.entity.FaulTranges;

import java.io.Serializable;
import java.util.List;

/**技能擅长范围实体类
 * Created by dingxujun on 2017/5/25.
 */

public class MyGoodRrange implements Serializable{
    public  String name;

    @Override
    public String toString() {
        return "MyGoodRrange{" +
                "list=" + list +
                ", name='" + name + '\'' +
                '}';
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> list;

}
