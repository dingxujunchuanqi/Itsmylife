package com.sinoautodiagnoseos.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.ui.UIHelper;
import com.sinoautodiagnoseos.ui.loginui.SwipeBackActivity;
import com.sinoautodiagnoseos.utils.OnMultiClickListener;

/**
 * Created by HQ_Demos on 2017/5/19.
 */
public class SettingActivity extends SwipeBackActivity{
    private RelativeLayout back_btn,edit_psd,version_checked,feedback;
    private Button quit_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        back_btn= (RelativeLayout) findViewById(R.id.setting_back_click);
        edit_psd= (RelativeLayout) findViewById(R.id.login_psw);
        version_checked= (RelativeLayout) findViewById(R.id.r2_layout);
        feedback= (RelativeLayout) findViewById(R.id.r3_layout);
        quit_login= (Button) findViewById(R.id.quit_button);

        back_btn.setOnClickListener(listener);
        edit_psd.setOnClickListener(listener);
        version_checked.setOnClickListener(listener);
        feedback.setOnClickListener(listener);
        quit_login.setOnClickListener(listener);
    }

    View.OnClickListener listener=new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            switch (v.getId()){
                case R.id.setting_back_click:
                    finish();
                    break;
                case R.id.login_psw:
                    UIHelper.showFgtPsd(SettingActivity.this);
                    break;
                case R.id.r2_layout:
                    break;
                case R.id.r3_layout:
                    UIHelper.showFeedBack(SettingActivity.this);
                    break;
                case R.id.quit_button:
                    UIHelper.showLogin(SettingActivity.this);
                    break;
            }
        }
    };
}
