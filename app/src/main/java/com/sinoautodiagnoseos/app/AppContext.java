package com.sinoautodiagnoseos.app;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import cn.jpush.android.api.JPushInterface;

public class AppContext extends Application {

    private static AppContext app;

    public AppContext() {
        app = this;
    }

    public static synchronized AppContext getInstance() {
        if (app == null) {
            app = new AppContext();
        }
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        registerUncaughtExceptionHandler();

        JPushInterface.setDebugMode(false); 	// 发布正式版本,关闭日志文件
        JPushInterface.init(this);     		// 初始化 JPush
        System.out.println("--------JPush初始化成功----------------");
    }

    // 注册App异常崩溃处理器
    private void registerUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
    }

}