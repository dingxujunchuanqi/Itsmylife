package com.sinoautodiagnoseos.openvcall.model;

import java.io.Serializable;

/**
 * Created by HQ_Demos on 2017/3/7.
 */
public class Faults implements Serializable{
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        private String text;
        private String value;
    public Faults(int position,String text,String value){
        this.position=position;
        this.text=text;
        this.value=value;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private int position;
}
