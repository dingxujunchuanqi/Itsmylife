package com.sinoautodiagnoseos.activity;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.sinoautodiagnoseos.app.AppManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by HQ_Demos on 2017/4/24.
 */

public abstract class BaseFragmentActivity extends FragmentActivity {
    private final static Logger log = LoggerFactory.getLogger(BaseFragmentActivity.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);

    }


    @Override
    protected void onDestroy() {
        //结束Activity 从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
        super.onDestroy();
    }

    @TargetApi(19)
    private void setTranslucentStatus() {
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        );
    }
}
