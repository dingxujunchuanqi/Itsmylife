package com.sinoautodiagnoseos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.ui.loginui.SwipeBackActivity;
import com.sinoautodiagnoseos.utils.OnMultiClickListener;
import com.sinoautodiagnoseos.utils.ToastUtils;


/**
 * Created by 惊吓了时光 on 2017/4/24.
 */

public class ForgetPswSecondActivity extends SwipeBackActivity implements View.OnClickListener {

    private EditText password;
    private EditText confirmpwd;
    private Button commit_bt;
    private RelativeLayout image_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetpsw_second_activity);
        int mode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
        getWindow().setSoftInputMode(mode);
        initView();
        initListenerOclick();
    }

    private void initListenerOclick() {
        image_back.setOnClickListener(this);
        commit_bt.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                commit();
            }
        });

    }


    private void initView() {
        password = (EditText) findViewById(R.id.password_edit);
        confirmpwd = (EditText) findViewById(R.id.confirm_password);
        commit_bt = (Button) findViewById(R.id.commit_button);
        image_back = (RelativeLayout) findViewById(R.id.back_click);

    }

    /**
     * 检查注册输入的内容
     */
    public boolean checkInput(String password, String confpwd) {
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showShort(this, R.string.register_pasword);
        } else if (password.length() < 6 || password.length() > 32 || TextUtils.isEmpty(password)) {
            ToastUtils.showShort(this, R.string.tip_please_input_6_32_password);

        } else if (TextUtils.isEmpty(confpwd)) {
            ToastUtils.showShort(this, R.string.again_register_pasword);
        } else if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(confpwd)) {
            if (!password.equals(confpwd)) {
                ToastUtils.showShort(this, "两次密码不一致啊");
            } else {
                return true;
            }
        } else {
            return true;
        }

        return false;
    }

    private void commit() {
        String pwd = password.getText().toString().trim();
        String confpwd = confirmpwd.getText().toString();
        if (checkInput(pwd, confpwd)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            ToastUtils.showShort(this, R.string.modify_successfully);
            finish();
        }
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
