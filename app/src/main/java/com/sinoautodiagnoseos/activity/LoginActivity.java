package com.sinoautodiagnoseos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.ui.UIHelper;
import com.sinoautodiagnoseos.ui.loginui.SwipeBackActivity;
import com.sinoautodiagnoseos.utils.LogUtils;
import com.sinoautodiagnoseos.utils.OnMultiClickListener;
import com.sinoautodiagnoseos.utils.RegexUtils;
import com.sinoautodiagnoseos.utils.ToastUtils;

/**
 * Created by HQ_Demos on 2017/4/27.
 */
public class LoginActivity extends SwipeBackActivity implements View.OnClickListener {
    private Button bt_login;
    private TextView forget_pwd, register;
    private EditText phone_edit, password_edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        int mode= WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
        getWindow().setSoftInputMode(mode);
        initView();
        initListenerOclick();
    }

    public void initView() {
        bt_login = (Button) findViewById(R.id.login_button);
        register = (TextView) findViewById(R.id.register);
        forget_pwd = (TextView) findViewById(R.id.forget_password);
        phone_edit = (EditText) findViewById(R.id.phone_edit);
        password_edit = (EditText) findViewById(R.id.password_edit);
    }

    private void initListenerOclick() {
        forget_pwd.setOnClickListener(this);
        register.setOnClickListener(this);
        bt_login.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                onClickLongin();
                LogUtils.deBug("login", "------------------");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forget_password:
                Intent intent_pwd = new Intent(this, ForgetPsswordActivity.class);
                startActivity(intent_pwd);
                break;
            case R.id.register:
                Intent intent_rg = new Intent(this, RegisterActivity.class);
                startActivity(intent_rg);
                break;
            default:
                break;
        }
    }

    private void onClickLongin() {
        String phonenumber = phone_edit.getText().toString();
        String password = password_edit.getText().toString();
        if (checkInput(phonenumber, password)) {

            // TODO: 请求服务器登录账号

            if (true) {
//                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                startActivity(intent);
//                finish();
                UIHelper.showStudy(this);

            } else {
                ToastUtils.showShort(this, R.string.username_orpassword_error);
            }
        }


    }

    /**
     * 检查注册输入的内容
     */
    public boolean checkInput(String phone, String password) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort(this, R.string.tip_phone_can_not_be_empty);
        } else if (!RegexUtils.checkMobile(phone)) {
            ToastUtils.showShort(this, R.string.tip_phone_regex_not_right);

        } else if (password.length() < 6 || password.length() > 32 || TextUtils.isEmpty(password)) {
            ToastUtils.showShort(this, R.string.tip_please_input_6_32_password);
        } else {
            return true;
        }

        return false;
    }
}

