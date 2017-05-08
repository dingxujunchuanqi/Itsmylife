package com.sinoautodiagnoseos.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.ui.loginui.SwipeBackActivity;

/**
 * Created by dingxujun on 2017/5/4.
 */

public class PersonalInfoActivity extends SwipeBackActivity implements View.OnClickListener {
    private FrameLayout image_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        initView();
        initListenerOclick();
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
}


