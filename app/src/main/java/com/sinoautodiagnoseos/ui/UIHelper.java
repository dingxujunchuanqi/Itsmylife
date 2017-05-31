package com.sinoautodiagnoseos.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.sinoautodiagnoseos.activity.CarInfoActivity;
import com.sinoautodiagnoseos.activity.FeedBackActivity;
import com.sinoautodiagnoseos.activity.ForgetPsswordActivity;
import com.sinoautodiagnoseos.activity.LoginActivity;
import com.sinoautodiagnoseos.activity.MainActivity;
import com.sinoautodiagnoseos.activity.PersonalInfoActivity;
import com.sinoautodiagnoseos.activity.ServiceDetailActivity;
import com.sinoautodiagnoseos.activity.SettingActivity;
import com.sinoautodiagnoseos.entity.CarBrands.CarInfo;
import com.sinoautodiagnoseos.entity.User.UserInfo;

import java.io.Serializable;
import java.util.List;

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
    public static void showPersonInfo(Activity context,  UserInfo data){
        Intent mIntent = new Intent(context, PersonalInfoActivity.class);
        Bundle mBundle =new Bundle();
        mBundle.putSerializable("userdata",data);
        mIntent.putExtras(mBundle);
        context.startActivity(mIntent);
//        Intent mIntent = new Intent(context, PersonalInfoActivity.class);
//        context.startActivity(mIntent);

    }
    public static void showLogin(Activity context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
//        ToastMessage(AppContext.getInstance().getApplicationContext(),"此处需要登录");
    }

    /**
     * 个人中心中的设置页面
     * @param context
     */
    public static void showSettingActivity(Activity context){
        Intent intent=new Intent(context,SettingActivity.class);
        context.startActivity(intent);
    }

    /**
     *  修改密码页面
     * @param context
     */
    public static void showFgtPsd(Activity context){
        Intent intent =new Intent(context, ForgetPsswordActivity.class);
        context.startActivity(intent);
    }

    /**
     *  意见反馈页面
     * @param context
     */
    public static void showFeedBack(Activity context){
        Intent intent =new Intent(context, FeedBackActivity.class);
        context.startActivity(intent);
    }

    public static void showServiceDetail(Activity context,int caseId,String title){
        Intent intent =new Intent(context, ServiceDetailActivity.class);
        intent.putExtra("caseId",caseId);
        intent.putExtra("title",title);
        context.startActivity(intent);
    }

    public static void showCarInfoActivity(Activity context, List<CarInfo> carInfoList){
        Intent intent =new Intent(context, CarInfoActivity.class);
        intent.putExtra("carInfoList", (Serializable) carInfoList);
        context.startActivityForResult(intent, 1000);
    }
}
