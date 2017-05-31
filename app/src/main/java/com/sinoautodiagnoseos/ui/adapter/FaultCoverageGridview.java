package com.sinoautodiagnoseos.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 技能选择的gridview
 * Created by dingxujun on 2017/5/25.
 */

public class FaultCoverageGridview extends GridView{
    public FaultCoverageGridview(Context context) {
        super(context);
    }

    public FaultCoverageGridview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FaultCoverageGridview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FaultCoverageGridview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
