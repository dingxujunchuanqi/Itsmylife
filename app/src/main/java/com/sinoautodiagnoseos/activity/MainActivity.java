package com.sinoautodiagnoseos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.app.AppContext;
import com.sinoautodiagnoseos.entity.User.Skill;
import com.sinoautodiagnoseos.entity.User.UserInfo;
import com.sinoautodiagnoseos.fragment.CircleFragment;
import com.sinoautodiagnoseos.fragment.DiagnoseFragment;
import com.sinoautodiagnoseos.fragment.ImFragment;
import com.sinoautodiagnoseos.fragment.StudyFragment;
import com.sinoautodiagnoseos.net.requestApi.HttpRequestApi;
import com.sinoautodiagnoseos.net.requestSubscribers.HttpSubscriber;
import com.sinoautodiagnoseos.net.requestSubscribers.SubscriberOnListener;
import com.sinoautodiagnoseos.ui.UIHelper;
import com.sinoautodiagnoseos.utils.Constant;
import com.sinoautodiagnoseos.utils.OnMultiClickListener;
import com.sinoautodiagnoseos.utils.SharedPreferences;
import com.sinoautodiagnoseos.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MainActivity extends BaseFragmentActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String CURR_INDEX = "currIndex";
    private static int currIndex = 0;

    private RadioGroup group;
    private ArrayList<String> fragmentTags;
    private FragmentManager fragmentManager;
    private RelativeLayout user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        initData(savedInstanceState);
        initView();
        initListenerOclick();
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURR_INDEX,currIndex);
    }

    private void initData(Bundle savedInstanceState){
        fragmentTags = new ArrayList<>(Arrays.asList("CircleFragment","DiagnoseFragment","ImFragment","StudyFragment"));
        currIndex=0;
        if (savedInstanceState!=null){
            currIndex=savedInstanceState.getInt(CURR_INDEX);
            hideSavedFragment();
        }
    }

    private void hideSavedFragment() {
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTags.get(currIndex));
        if (fragment!=null){
            fragmentManager.beginTransaction().hide(fragment).commit();//隐藏fragment
        }
    }

    private TextView textHeadTitle;
    private void initView() {
        user = (RelativeLayout)findViewById(R.id.user);
        textHeadTitle= (TextView) findViewById(R.id.textHeadTitle);
        textHeadTitle.setText("圈子");
        group = (RadioGroup) findViewById(R.id.group);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.foot_bar_circle:
                        currIndex=0;
                        textHeadTitle.setText("圈子");
                        user.setVisibility(View.GONE);
                        break;
                    case R.id.foot_bar_diagnose:
                        currIndex=1;
                        textHeadTitle.setText("云诊");
                        user.setVisibility(View.VISIBLE);
                        break;
                    case R.id.foot_bar_im:
                        currIndex=2;
                        textHeadTitle.setText("消息");
                        user.setVisibility(View.GONE);
                        break;
                    case R.id.main_footbar_study:
                        currIndex=3;
                        textHeadTitle.setText("学习");
                        user.setVisibility(View.GONE);
                        break;
                    default:break;
                }
                showFragment();
            }
        });
        showFragment();
    }
    private void initListenerOclick() {
        user.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                Intent intent = new Intent(MainActivity.this, PersonalCenterActivity.class);
                startActivity(intent);
            }
        });
    }
    private void showFragment() {
//        //跳转到圈子页面 需登录
//        if (currIndex==0){
//            UIHelper.showCircle(MainActivity.this);
//        }
        //跳转到云诊页面 可浏览 操作需登录
        if (currIndex==1){
            Log.e("--------",AppContext.getInstance().AuthLogin()+"");
            if (AppContext.getInstance().AuthLogin()){
                saveRegistion();
            }else {
                UIHelper.showLogin(MainActivity.this);
            }
        }
//        //跳转到IM页面 需登录
//        if (currIndex==2){
//            UIHelper.showIm(MainActivity.this);
//        }
//        //跳转到学习页面 需登录
//        if (currIndex==3){
//            UIHelper.showStudy(MainActivity.this);
//        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTags.get(currIndex));
        if (fragment==null){
            fragment=instantFragment(currIndex);
        }
        for (int i = 0;i<fragmentTags.size();i++){
            Fragment f = fragmentManager.findFragmentByTag(fragmentTags.get(i));
            if (f!=null&&f.isAdded()){
                fragmentTransaction.hide(f);
            }
        }
        if (fragment.isAdded()){
            fragmentTransaction.show(fragment);
        }else {
            fragmentTransaction.add(R.id.fragment_container,fragment,fragmentTags.get(currIndex));
        }
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();

    }

    private Fragment instantFragment(int currIndex) {
        Log.e(TAG,currIndex+"");
        switch (currIndex){
            //圈子
            case 0: return new CircleFragment();
            //云诊
            case 1: return new DiagnoseFragment();
            //消息
            case 2: return new ImFragment();
            //学习
            case 3: return new StudyFragment();

            default: return null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 保存唯一识别码
     * 用于校验唯一登录
     */
    private void saveRegistion() {
        Constant.TOKEN= SharedPreferences.getInstance().getString("token","");
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

    //用户个人信息接口
    public void getUserInfo(){
        Constant.TOKEN=SharedPreferences.getInstance().getString("token","");
        Constant.REGISTRATION=SharedPreferences.getInstance().getString("RegistrationId","");
        HttpRequestApi.getInstance().getUserInfo(new HttpSubscriber<UserInfo>(new SubscriberOnListener<UserInfo>() {
            @Override
            public void onSucceed(UserInfo data) {
                /**
                 * 获取用户信息
                 */
                System.out.println("--------请求用户信息成功-----------");
                Constant.USERROLE = data.getData().getRoleName();
                Constant.MEMBERID = data.getData().getMemberId();

                SharedPreferences.getInstance().putString("userId", data.getData().getUserId());

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
                    ToastUtils.makeShortText( "服务器异常请稍后再试！",MainActivity.this);
                } else if (Constant.RESPONSECODE == 417) {
                }
            }
        }, MainActivity.this));
    }

    /**
     * 更新用户状态
     *
     * @param state 1-在线 2-占线 3-离线
     */
    public void ChageUserState(int state) {
        Constant.TOKEN = SharedPreferences.getInstance().getString("token", "");
        Constant.REGISTRATION=SharedPreferences.getInstance().getString("RegistrationId","");
        HttpRequestApi.getInstance().onLine(new HttpSubscriber<Skill>(new SubscriberOnListener<Skill>() {
            @Override
            public void onSucceed(Skill data) {
            }

            @Override
            public void onError(int code, String msg) {

            }
        }, MainActivity.this));
    }

}
