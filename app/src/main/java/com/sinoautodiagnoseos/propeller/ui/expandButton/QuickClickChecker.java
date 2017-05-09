package com.sinoautodiagnoseos.propeller.ui.expandButton;

/**
 * Created by zhaodan on 2017/3/13.
 */
public class QuickClickChecker {
    private int threshold;
    private long lastClickTime = 0;

    public QuickClickChecker(int threshold) {
        this.threshold = threshold;
    }

    public boolean isQuick() {
        boolean isQuick = System.currentTimeMillis() - lastClickTime <= threshold;
        lastClickTime = System.currentTimeMillis();
        return isQuick;
    }
}
