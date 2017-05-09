package com.sinoautodiagnoseos.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.sinoautodiagnoseos.activity.LoginActivity;
import com.sinoautodiagnoseos.activity.MainActivity;
import com.sinoautodiagnoseos.activity.PersonalInfoActivity;
import com.sinoautodiagnoseos.entity.User.UserInfo;

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
       /*
        * 跳到个人信息界面并传递数据
        *
        */
    public static void showPersonInfo(Activity context ,UserInfo data ){
        Intent mIntent = new Intent(context, PersonalInfoActivity.class);
        Bundle mBundle =new Bundle();
        mBundle.putSerializable("userdata",data);
        mIntent.putExtras(mBundle);
        context.startActivity(mIntent);
    }
    public static void showLogin(Activity context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
//        ToastMessage(AppContext.getInstance().getApplicationContext(),"此处需要登录");
    }
}
