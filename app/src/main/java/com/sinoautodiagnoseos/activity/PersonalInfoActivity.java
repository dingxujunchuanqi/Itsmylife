package com.sinoautodiagnoseos.activity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.User.UserInfo;
import com.sinoautodiagnoseos.ui.loginui.SwipeBackActivity;
import com.sinoautodiagnoseos.utils.LogUtils;

import java.io.Serializable;

/**
 * Created by dingxujun on 2017/5/4.
 */

public class PersonalInfoActivity extends SwipeBackActivity implements View.OnClickListener {
    private static final String TAG = "PersonalInfoActivity";
    private FrameLayout image_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        initView();
        initListenerOclick();
        getUserInfo();
    }


    private void initView() {
        image_back = (FrameLayout) findViewById(R.id.back_click);
    }

    private void initListenerOclick() {
        image_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_click:
                finish();
                break;
            default:
                break;
        }
    }
/**
*接收传过来的对象
*@author dingxujun
*created at 2017/5/9 9:40
*/
    private void getUserInfo() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        UserInfo userdata = (UserInfo) bundle.getSerializable("userdata");
        LogUtils.deBug(TAG,"=================="+userdata.getData().getRoleName());
    }
}


