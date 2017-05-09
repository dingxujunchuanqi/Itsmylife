package com.sinoautodiagnoseos.openvcall.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by HQ_Demos on 2017/2/17.
 */

public class MyDialog extends Dialog {
    public MyDialog(Context context, int width, int height, View layout, int style) {
        super(context,style);

        setContentView(layout);
        //设置dialog的宽高
        Window window=getWindow();
        window.getDecorView().setPadding(100,300,100,300);//<==关键这句话
        WindowManager.LayoutParams params = window.getAttributes();
        params.width= WindowManager.LayoutParams.MATCH_PARENT;
        params.height= WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }
}
