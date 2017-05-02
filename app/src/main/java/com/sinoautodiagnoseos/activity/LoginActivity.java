package com.sinoautodiagnoseos.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.ui.loginui.SwipeBackActivity;
import com.sinoautodiagnoseos.utils.OnMultiClickListener;

/**
 * 登录页
 * Created by HQ_Demos on 2017/4/27.
 */
public class LoginActivity extends SwipeBackActivity{
    private Button bt_login;
    private TextView fgp_psd,rgt_psd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        bt_login= (Button) findViewById(R.id.login_button);

        bt_login.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {

            }
        });
    }
}
