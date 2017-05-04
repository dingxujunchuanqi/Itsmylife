package com.sinoautodiagnoseos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.app.AppContext;
import com.sinoautodiagnoseos.entity.User.Skill;
import com.sinoautodiagnoseos.entity.User.Token;
import com.sinoautodiagnoseos.entity.User.UserInfo;
import com.sinoautodiagnoseos.net.requestApi.HttpRequestApi;
import com.sinoautodiagnoseos.net.requestSubscribers.HttpSubscriber;
import com.sinoautodiagnoseos.net.requestSubscribers.SubscriberOnListener;
import com.sinoautodiagnoseos.ui.UIHelper;
import com.sinoautodiagnoseos.ui.loginui.SwipeBackActivity;
import com.sinoautodiagnoseos.utils.Constant;
import com.sinoautodiagnoseos.utils.LogUtils;
import com.sinoautodiagnoseos.utils.OnMultiClickListener;
import com.sinoautodiagnoseos.utils.RegexUtils;
import com.sinoautodiagnoseos.utils.SharedPreferences;
import com.sinoautodiagnoseos.utils.StringUtils;
import com.sinoautodiagnoseos.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.sinoautodiagnoseos.ui.UIHelper.ToastMessage;

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
        int mode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
        getWindow().setSoftInputMode(mode);
        initView();
//        registerMessageReceiver();
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

//    private MyReceiver myReceiver;
//    public static final String MESSAGE_RECEIVED_ACTION = "com.sinoautodiagnoseos.MESSAGE_RECEIVED_ACTION";
//    public void registerMessageReceiver() {
//        myReceiver = new MyReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
//        filter.addAction(MESSAGE_RECEIVED_ACTION);
//        registerReceiver(myReceiver, filter);
//    }

    private void onClickLongin() {
        final String phonenumber = phone_edit.getText().toString();
        final String password = password_edit.getText().toString();
//        String rid = JPushInterface.getRegistrationID(AppContext.getInstance());
//        if (!rid.isEmpty()) {
//            SharedPreferences.getInstance().putString("RegistrationId",rid);
//        } else {
//            ToastMessage(AppContext.getInstance(),"始化失败，请退出应用程序重新登录");
//        }
        if (checkInput(phonenumber, password)) {

            // TODO: 请求服务器登录账号

            if (true) {
                Constant.TOKEN = "Basic " + StringUtils.getBASE64(phonenumber + ":" + password);
                SharedPreferences.getInstance().putString("token",Constant.TOKEN);
                System.out.println("token=" + Constant.TOKEN);
                HttpRequestApi.getInstance().getToken(new HttpSubscriber<Token>(new SubscriberOnListener<Token>() {
                    @Override
                    public void onSucceed(Token data) {
                        ToastMessage(AppContext.getInstance(),"登陆成功");
                        SharedPreferences.getInstance().putString("checktoken", "Bearer " + data.getToken());
                        saveRegistion();

                        SharedPreferences.getInstance().putString("account",phonenumber);
                        SharedPreferences.getInstance().putString("password",password);
                        UIHelper.showDiagnose(LoginActivity.this);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        ToastMessage(AppContext.getInstance(),"账号或密码有误");
                    }
                },LoginActivity.this));
//                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                startActivity(intent);
//                finish();
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

    public void getUserInfo(){
        Constant.TOKEN=SharedPreferences.getInstance().getString("checktoken","");
        Constant.REGISTRATION=SharedPreferences.getInstance().getString("RegistrationId","");
        HttpRequestApi.getInstance().getUserInfo(new HttpSubscriber<UserInfo>(new SubscriberOnListener<UserInfo>() {
            @Override
            public void onSucceed(UserInfo data) {
                /**
                 * 获取用户信息
                 */
              // data.getData().getName();
                System.out.println("--------请求用户信息成功-----------");
                Constant.USERROLE = data.getData().getRoleName();
                Constant.MEMBERID = data.getData().getMemberId();
                SharedPreferences.getInstance().putString("userId", data.getData().getUserId());
                SharedPreferences.getInstance().putString("userName",data.getData().getName());
                SharedPreferences.getInstance().putString("mobile",data.getData().getMobile());
                SharedPreferences.getInstance().putString("starRating",data.getData().getOtherInfo().getStarRating());
                SharedPreferences.getInstance().putString("avatar",data.getData().getAvatar());
                System.out.println("用户ID=======" + data.getData().getUserId());
                System.out.println("用户权限-" + data.getData().getRoleName());
                /**
                 * 更新用户在线状态
                 */
                ChageUserState(1);
            }

            @Override
            public void onError(int code, String msg) {
                if (Constant.RESPONSECODE == 500) {
                    Toast.makeText(LoginActivity.this, "服务器异常请稍后再试！", Toast.LENGTH_SHORT).show();
                } else if (Constant.RESPONSECODE == 417) {
                }
            }
        }, LoginActivity.this));
    }

    /**
     * 更新用户状态
     *
     * @param state 1-在线 2-占线 3-离线
     */
    public void ChageUserState(int state) {
        Constant.TOKEN = SharedPreferences.getInstance().getString("checktoken", "");
        Constant.REGISTRATION=SharedPreferences.getInstance().getString("RegistrationId","");
        HttpRequestApi.getInstance().onLine(new HttpSubscriber<Skill>(new SubscriberOnListener<Skill>() {
            @Override
            public void onSucceed(Skill data) {
            }

            @Override
            public void onError(int code, String msg) {

            }
        }, LoginActivity.this));
    }


    /**
     * 保存唯一识别码
     * 用于校验唯一登录
     */
    private void saveRegistion() {
        Constant.TOKEN=SharedPreferences.getInstance().getString("checktoken","");
        Constant.REGISTRATION=SharedPreferences.getInstance().getString("RegistrationId","");
        final Map<String, Object> map = new HashMap<>();
        map.put("deviceType", "Android");
        map.put("registrationID",Constant.REGISTRATION );
        Gson gson = new Gson();
        String loginJson = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), loginJson);
        HttpRequestApi.getInstance().postId(requestBody, new HttpSubscriber<UserInfo>(new SubscriberOnListener<UserInfo>() {

            @Override
            public void onSucceed(UserInfo data) {
                System.out.println("------执行了-----onSucceed---");
                getUserInfo();
            }

            @Override
            public void onError(int code, String msg) {
                System.out.println("-------onError----------" + msg);
                if (code == -1003) {
                    getUserInfo();
                }
            }
        }, AppContext.getInstance()));

    }


}

