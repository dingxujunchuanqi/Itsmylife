package com.sinoautodiagnoseos.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.sinoautodiagnoseos.activity.LoginActivity;
import com.sinoautodiagnoseos.activity.MainActivity;
import com.sinoautodiagnoseos.app.AppContext;
import com.sinoautodiagnoseos.entity.User.Token;
import com.sinoautodiagnoseos.net.requestApi.HttpRequestApi;
import com.sinoautodiagnoseos.net.requestSubscribers.HttpSubscriber;
import com.sinoautodiagnoseos.net.requestSubscribers.SubscriberOnListener;
import com.sinoautodiagnoseos.utils.Constant;
import com.sinoautodiagnoseos.utils.StringUtils;

/**
 * 应用程序UI工具包：封装UI相关的一些操作
 * Created by HQ_Demos on 2017/4/24.
 */

public class UIHelper {
    public final static String TAG = "UIHelper";

    public final static int RESULT_OK = 0x00;
    public final static int REQUEST_CODE = 0x01;

    public static void ToastMessage(Context cont,String msg){
        if (cont==null||msg==null){
            return;
        }
        Toast.makeText(cont,msg,Toast.LENGTH_SHORT).show();
    }

    public static void ToastMessage(Context cont, int msg) {
        if(cont == null || msg <= 0) {
            return;
        }
        Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
    }

    public static void ToastMessage(Context cont, String msg, int time) {
        if(cont == null || msg == null) {
            return;
        }
        Toast.makeText(cont, msg, time).show();
    }

    public static void showDiagnose(Activity context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void showCircle(Activity context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void showIm(Activity context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void showStudy(Activity context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void showLogin(Activity context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
//        ToastMessage(AppContext.getInstance().getApplicationContext(),"此处需要登录");
    }
    static boolean is_login;
    public static boolean isLogin(String username,String password){
        Constant.TOKEN = "Basic " + StringUtils.getBASE64(username + ":" + password);
        System.out.println("token=" + Constant.TOKEN);
        HttpRequestApi.getInstance().getToken(new HttpSubscriber<Token>(new SubscriberOnListener<Token>() {
            @Override
            public void onSucceed(Token data) {
                ToastMessage(AppContext.getInstance(),"登陆成功");
                is_login=true;
            }

            @Override
            public void onError(int code, String msg) {
                is_login=false;
            }
        },AppContext.getInstance()));
        return is_login;
    }
}
