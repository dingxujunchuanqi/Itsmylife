package com.sinoautodiagnoseos.openvcall.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 *  搜索自定义dialog
 * Created by HQ_Demos on 2017/5/25.
 */

public class Search_Dialog extends Dialog{

    private Window window=null;

    public Search_Dialog(Context context) {
        super(context);
    }

    public Search_Dialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected Search_Dialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setView(View view){
        setContentView(view);
    }

    public void setViewId(int id){
        setContentView(id);
    }

    public void setProperty(){
        window=getWindow();
        window.getDecorView().setPadding(0,130,0,300);//<==关键这句话
        WindowManager.LayoutParams wl=window.getAttributes();
        wl.width= WindowManager.LayoutParams.MATCH_PARENT;
        wl.height= WindowManager.LayoutParams.MATCH_PARENT;
//        wl.x=x;
//        wl.y=y;
//        wl.width=w;
//        wl.height=h;
        wl.alpha=1f;// 设置对话框的透明度,1f不透明 
        wl.gravity= Gravity.TOP;
        window.setAttributes(wl);
    }
}
