package com.sinoautodiagnoseos.ui.selectstoresui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.View;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.Station.MCity;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangyangkai on 2016/12/27.
 */

public class StickyDecoration extends RecyclerView.ItemDecoration {


    private TextPaint textPaint;
    private Paint paint;
    private int topHeight;
    private List<MCity> cityList = new ArrayList<>();

    public StickyDecoration(List<MCity> cityList, Context context) {
        Resources res = context.getResources();
        this.cityList=cityList;
        paint = new Paint();
        paint.setColor(res.getColor(R.color.gray));//字母条目的颜色
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(45);//悬浮字母的大小
        textPaint.setColor(Color.WHITE);//悬浮字母的颜色
        topHeight = res.getDimensionPixelSize(R.dimen.top);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        if (isFirstInGroup(position)) {
            outRect.top = topHeight;
        } else {
            outRect.top = 0;
        }

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            String textLine = this.cityList.get(position).getFirstPinYin();
            if (isFirstInGroup(position)) {
                float top = view.getTop() - topHeight;
                float bottom = view.getTop();
                c.drawRect(left, top, right, bottom, paint);//绘制红色矩形
                c.drawText(textLine, left + 30, bottom - 30, textPaint);//绘制文本
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int position = ((LinearLayoutManager) (parent.getLayoutManager())).findFirstVisibleItemPosition();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        c.drawRect(left, 0, right, topHeight, paint);//绘制红色矩形
        String text = this.cityList.get(position).getFirstPinYin();
        c.drawText(text, 30, topHeight - 30, textPaint);//绘制文本
    }

    private boolean isFirstInGroup(int position) {
        boolean isFirst;
        if (position == 0) {
            isFirst = true;
        } else {
            if (this.cityList.get(position).getFirstPinYin().
                    equals(this.cityList.get(position - 1).getFirstPinYin())) {
                isFirst = false;
            } else {
                isFirst = true;
            }
        }
        return isFirst;
    }
}
