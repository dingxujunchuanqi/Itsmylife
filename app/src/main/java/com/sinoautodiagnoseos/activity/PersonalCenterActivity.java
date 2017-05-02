package com.sinoautodiagnoseos.activity;

import android.os.Bundle;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.ui.loginui.SwipeBackActivity;

/**
 * 个人中心界面
 * Created by 惊吓了时光 on 2017/5/2.
 */

public class PersonalCenterActivity extends SwipeBackActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
    }
}
