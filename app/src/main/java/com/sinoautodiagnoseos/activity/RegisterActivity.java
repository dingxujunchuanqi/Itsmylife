package com.sinoautodiagnoseos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.ui.loginui.SwipeBackActivity;
import com.sinoautodiagnoseos.utils.OnMultiClickListener;
import com.sinoautodiagnoseos.utils.RegexUtils;
import com.sinoautodiagnoseos.utils.ToastUtils;

/**
 * 注册的页面
 * Created by dingxujun on 2017/4/20.
 */

public class RegisterActivity extends SwipeBackActivity implements View.OnClickListener {

    private ImageView image_back;
    private EditText phone_edit, password_edit, verifi_edit;
    private TextView verifi_send;
    private Button register_andlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        int mode= WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
        getWindow().setSoftInputMode(mode);
        initView();//初始化数据
        initListenerOclick();//按钮点击监听
    }

    private void initListenerOclick() {
        image_back.setOnClickListener(this);
        verifi_send.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                //TODO:请求三方接口发送验证码
                //发送之前对号码进行验证
                // verifyCodeManager.getVerifyCode(VerifyCodeManager.REGISTER);
            }
        });
        register_andlogin.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                commit();//提交注册的数据
            }
        });

        password_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                //点击虚拟键盘现的效果,右下角是完成或开始
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO) {
                    commit();
                }
                return false;
            }
        });
    }

    private void initView() {
        image_back = (ImageView) findViewById(R.id.image_back);
        phone_edit = (EditText) findViewById(R.id.phone_edit);
        password_edit = (EditText) findViewById(R.id.password_edit);
        verifi_edit = (EditText) findViewById(R.id.verifi_edit);
        verifi_send = (TextView) findViewById(R.id.verifi_send);
        register_andlogin = (Button) findViewById(R.id.register_andlogin);
    }


    //注册提交数据
    public void commit() {
        String phone = phone_edit.getText().toString().trim();
        String password = password_edit.getText().toString().trim();
        String verification = verifi_edit.getText().toString().trim();
        if (checkInput(phone, password, verification)) {
            //TODO：验证数据成功后，请求服务端注册账号,并提交注册数据
            //SMSSDK.submitVerificationCode("86", phone, verification);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            ToastUtils.showShort(this, "注册成功，赶快登录吧");
        }

    }

    /**
     * 检查注册输入的内容
     */
    public boolean checkInput(String phone, String password, String verification) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort(this, R.string.tip_phone_can_not_be_empty);
        } else if (!RegexUtils.checkMobile(phone)) {
            ToastUtils.showShort(this, R.string.tip_phone_regex_not_right);

        } else if (TextUtils.isEmpty(verification)) {//验证码不能为空
            ToastUtils.showShort(this, R.string.tip_please_input_code);
        } else if (password.length() < 6 || password.length() > 32 || TextUtils.isEmpty(password)) {
            ToastUtils.showShort(this, R.string.tip_please_input_6_32_password);
        } else {
            return true;
        }

        return false;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            default:
                break;
        }
    }
}
