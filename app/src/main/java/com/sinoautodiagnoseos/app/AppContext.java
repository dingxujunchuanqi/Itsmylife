package com.sinoautodiagnoseos.app;

import android.app.Application;

import com.sinoautodiagnoseos.entity.User.Token;
import com.sinoautodiagnoseos.entity.User.UserInfo;
import com.sinoautodiagnoseos.net.requestApi.HttpRequestApi;
import com.sinoautodiagnoseos.net.requestSubscribers.HttpSubscriber;
import com.sinoautodiagnoseos.net.requestSubscribers.SubscriberOnListener;
import com.sinoautodiagnoseos.openvcall.model.CurrentUserSettings;
import com.sinoautodiagnoseos.openvcall.model.WorkerThread;
import com.sinoautodiagnoseos.utils.Constant;
import com.sinoautodiagnoseos.utils.SharedPreferences;
import com.sinoautodiagnoseos.utils.StringUtils;
import com.squareup.leakcanary.LeakCanary;

import cn.jpush.android.api.JPushInterface;

public class AppContext extends Application {

    private static AppContext app;
    //用户信息
    public static UserInfo userInfo;
    public AppContext() {
        app = this;
    }

    public static synchronized AppContext getInstance() {
        if (app == null) {
            app = new AppContext();
        }
        return app;
    }

    private WorkerThread mWorkerThread;

    public synchronized void initWorkerThread() {
        if (mWorkerThread == null) {
            mWorkerThread = new WorkerThread(getApplicationContext());
            mWorkerThread.start();
            mWorkerThread.waitForReady();
        }
    }
    public synchronized WorkerThread getWorkerThread() {
        return mWorkerThread;
    }

    public synchronized void deInitWorkerThread() {
        mWorkerThread.exit();
        try {
            mWorkerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mWorkerThread = null;
    }

    public static final CurrentUserSettings mVideoSettings = new CurrentUserSettings();



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

    /**
     * 自动登录
     *
     */
    boolean AuthLogin = false;
    public boolean AuthLogin() {
        Constant.TOKEN = "Basic " + StringUtils.getBASE64(SharedPreferences.getInstance().getString("account", "")
                + ":" + SharedPreferences.getInstance().getString("password", ""));
        Constant.REGISTRATION=SharedPreferences.getInstance().getString("RegistrationId","");
        HttpRequestApi.getInstance().getToken(new HttpSubscriber<Token>(new SubscriberOnListener<Token>() {
            @Override
            public void onSucceed(Token data) {
                AuthLogin = true;
            }

            @Override
            public void onError(int code, String msg) {
                AuthLogin = false;
            }
        }, AppContext.getInstance()));
        return AuthLogin;
    }

}