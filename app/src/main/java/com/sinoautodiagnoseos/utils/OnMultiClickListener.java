package com.sinoautodiagnoseos.utils;

import android.view.View;

/**
 * 按钮重复点击处理类
 * Created by HQ_Demos on 2017/4/26.
 */

public abstract class OnMultiClickListener implements View.OnClickListener {
    // 两次点击按钮之间的点击间隔不能少于5秒
    private static final int MIN_CLICK_DELAY_TIME = 500;
    private static long lastClickTime;

    public abstract void onMultiClick(View v);

    @Override
    public void onClick(View v) {
        long curClickTime = System.currentTimeMillis();
        if((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME){
            // 超过点击间隔后再将lastClickTime重置为当前点击时间
            lastClickTime = curClickTime;
            onMultiClick(v);
        }
    }
}