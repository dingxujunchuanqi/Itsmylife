package com.sinoautodiagnoseos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.propeller.ui.MDDialog;
import com.sinoautodiagnoseos.ui.loginui.SwipeBackActivity;
import com.sinoautodiagnoseos.utils.OnMultiClickListener;
import com.sinoautodiagnoseos.utils.RegexUtils;
import com.sinoautodiagnoseos.utils.SharedPreferences;
import com.sinoautodiagnoseos.utils.StringUtils;
import com.sinoautodiagnoseos.utils.ToastUtils;

/**
 * 注册的页面
 * Created by dingxujun on 2017/4/20.
 */

public class RegisterActivity extends SwipeBackActivity implements View.OnClickListener {
    private EditText phone_edit, password_edit, verifi_edit;
    private TextView verifi_send;
    private Button register_andlogin;
    private RelativeLayout image_back;
    private CheckBox agree_xieyi;
    private TextView xieyi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        //防止按钮上弹
        int mode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
        getWindow().setSoftInputMode(mode);
        initView();//初始化数据
        initListenerOclick();//按钮点击监听
    }

    private void initListenerOclick() {
        image_back.setOnClickListener(this);
        xieyi.setOnClickListener(this);
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
        phone_edit = (EditText) findViewById(R.id.phone_edit);
        password_edit = (EditText) findViewById(R.id.password_edit);
        verifi_edit = (EditText) findViewById(R.id.verifi_edit);
        verifi_send = (TextView) findViewById(R.id.verifi_send);
        image_back = (RelativeLayout) findViewById(R.id.back_click);
        register_andlogin = (Button) findViewById(R.id.register_andlogin);
        agree_xieyi= (CheckBox) findViewById(R.id.agree_xieyi);
        xieyi= (TextView) findViewById(R.id.xieyi);
    }


    //注册提交数据
    public void commit() {
        String phone = phone_edit.getText().toString().trim();
        String password = password_edit.getText().toString().trim();
        String verification = verifi_edit.getText().toString().trim();
        if (checkInput(phone, password, verification)) {
            //TODO：验证数据成功后，请求服务端注册账号,并提交注册数据
            SharedPreferences.getInstance().putString("account",phone);
            SharedPreferences.getInstance().putString("password",password);
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
        } else if(agree_xieyi.isChecked()==false){
            ToastUtils.showShort(this,R.string.agree_xieyi);
        }
        else {
            return true;
        }

        return false;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_click:
                finish();
                break;
            case R.id.xieyi:
                showXiyiDialog();
            default:
                break;
        }
    }

    MDDialog.Builder dialog;
    TextView xieyi_content;
    //显示协议文字内容
    private void showXiyiDialog() {
        dialog= new MDDialog.Builder(this);
        dialog .setContentView(R.layout.xieyi_dialog)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {
                        xieyi_content= (TextView) contentView.findViewById(R.id.content_txt);
                        String txt= StringUtils.readAssetsTxt(RegisterActivity.this,"xieyi");
                        xieyi_content.setText(txt);
                    }
                }) .setTitle("蓝领驿家用户协议")
                .setNegativeButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setPositiveButtonMultiListener(new MDDialog.OnMultiClickListener() {
                    @Override
                    public void onClick(View clickedView, View contentView) {
                    }
                })
                .setOnItemClickListener(new MDDialog.OnItemClickListener() {
                    @Override
                    public void onItemClicked(int index) {
                    }
                })
                .setWidthMaxDp(600)
                .setShowButtons(true)
                .create()
                .show();
    }

}
