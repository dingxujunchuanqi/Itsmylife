package com.sinoautodiagnoseos.activity;

import android.os.Bundle;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.CarBrands.CarBrands;
import com.sinoautodiagnoseos.net.requestApi.HttpRequestApi;
import com.sinoautodiagnoseos.net.requestSubscribers.HttpSubscriber;
import com.sinoautodiagnoseos.net.requestSubscribers.SubscriberOnListener;
import com.sinoautodiagnoseos.ui.loginui.SwipeBackActivity;
import com.sinoautodiagnoseos.utils.Constant;
import com.sinoautodiagnoseos.utils.LogUtils;
import com.sinoautodiagnoseos.utils.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 技能管理界面
 * Created by dingxujun on 2017/5/21.
 */

public class SkillManagementActivity extends SwipeBackActivity {
    private String TAG = this.getClass().getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skillmanagement);
        vehicleBrandRequest();
    }
/*
*  车辆品牌数据请求
*
* */
    private void vehicleBrandRequest() {
        Constant.TOKEN= SharedPreferences.getInstance().getString("checktoken","");
        Constant.REGISTRATION= SharedPreferences.getInstance().getString("RegistrationId","");
        HttpRequestApi.getInstance().getCarBrands( new HttpSubscriber<CarBrands>(new SubscriberOnListener<CarBrands>() {
            @Override
            public void onSucceed(CarBrands data) {
               List<CarBrands.Brand> list1 ;
                Set<String> keys = data.getData().keySet();
                List<String> list =new ArrayList<String>();

                for (String key:keys) {
                    list.add(key);
                    list1 = data.getData().get(key);
               //     VehicleBrandAdapter adpter =new VehicleBrandAdapter(SkillManagementActivity.this,list,list1);
                    for (int i = 0; i < list1.size() ; i++) {
                        CarBrands.Brand brand = list1.get(i);
                        System.out.println("-----我是对象值------"+brand.getText());
                    }
                    System.out.println("======我是键========="+key);
                }
                LogUtils.deBug(TAG,"=======我是技能管理======"+data);
            }
            @Override
            public void onError(int code, String msg) {

            }
        },SkillManagementActivity.this));
    }
}
