package com.sinoautodiagnoseos.propeller.ui.expandButton;

/**
 * Created by zhaodan on 2017/3/13.
 */
public interface ButtonEventListener {
    /**
     * @param index button index, count from startAngle to endAngle, value is 1 to expandButtonCount
     */
    void onButtonClicked(int index);

    void onExpand();
    void onCollapse();
}
