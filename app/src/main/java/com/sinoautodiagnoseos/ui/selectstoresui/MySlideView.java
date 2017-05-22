package com.sinoautodiagnoseos.ui.selectstoresui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import com.sinoautodiagnoseos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangyangkai on 16/7/26.
 */
public class MySlideView extends View {

    List<String> array = new ArrayList<>();

    public List<String> getArray() {
        return array;
    }

    public void setArray(List<String> array) {
        this.array = array;
        requestLayout();
    }

    public interface onTouchListener {
        void showTextView(String textView, boolean dismiss);
    }

    private onTouchListener listener;

    public void setListener(onTouchListener listener) {
        this.listener = listener;
    }

    private int mWidth, mHeight, mTextHeight, position;
    private Paint paint;
    private Rect mBound;
    private int backgroundColor;
    private int yDown, yMove, mTouchSlop;
    private boolean isSlide;
    private String selectTxt;

    public MySlideView(Context context) {
        this(context, null);
    }

    public MySlideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }


    private void initView() {
        paint = new Paint();
        paint.setAntiAlias(true);
        mBound = new Rect();
        backgroundColor = getResources().getColor(R.color.gray);//侧边所引颜色
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(backgroundColor);
        canvas.drawRect(0, 0, (float) mWidth, mHeight, paint);
        for (int i = 0; i < array.size(); i++) {
            String textView = array.get(i);
            if (i == position - 1) {
                paint.setColor(getResources().getColor(R.color.gray1));
                selectTxt = array.get(i);
                listener.showTextView(selectTxt, false);
            } else {
                paint.setColor(getResources().getColor(R.color.text_apha50));
            }
            paint.setTextSize(30);//索引字体大小
            paint.getTextBounds(textView, 0, textView.length(), mBound);
            canvas.drawText(textView, (mWidth - mBound.width()) * 1 / 2, mTextHeight - mBound.height(), paint);
            mTextHeight += mHeight / array.size();

        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        int action = ev.getAction();
        int y = (int) ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                yDown = y;
                break;
            case MotionEvent.ACTION_MOVE:
                yMove = y;
                int dy = yMove - yDown;
                //如果是竖直方向滑动
                if (Math.abs(dy) > mTouchSlop) {
                    isSlide = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int y = (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                backgroundColor = getResources().getColor(R.color.gray1);//
                mTextHeight = mHeight / array.size();
                position = y / (mHeight / (array.size() + 1));
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE://滑动时的侧边栏颜色
                if (isSlide) {
                    backgroundColor = getResources().getColor(R.color.gray);
                    mTextHeight = mHeight / array.size();
                    position = y / (mHeight / array.size() + 1) + 1;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                backgroundColor = getResources().getColor(R.color.gray1);
                mTextHeight = mHeight / array.size();
                position = 0;
                invalidate();
                listener.showTextView(selectTxt, true);
                break;
        }
        return true;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = widthSize * 1 / 2;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = heightSize * 1 / 2;
        }
        mWidth = width;
        mHeight = height;
        if (array.size() != 0)
            mTextHeight = mHeight / array.size();
        setMeasuredDimension(width, height);
        System.out.println("----------------" + array.size());
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility != VISIBLE) {
            mTextHeight = mHeight / array.size();
        }
    }
}
