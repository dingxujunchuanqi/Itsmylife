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

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.fragment.CircleFragment;
import com.sinoautodiagnoseos.fragment.DiagnoseFragment;
import com.sinoautodiagnoseos.fragment.ImFragment;
import com.sinoautodiagnoseos.fragment.StudyFragment;
import com.sinoautodiagnoseos.ui.UIHelper;
import com.sinoautodiagnoseos.utils.OnMultiClickListener;

import java.util.ArrayList;
import java.util.Arrays;

import static com.sinoautodiagnoseos.R.id.user;
import static com.sinoautodiagnoseos.R.id.view;

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
            UIHelper.showLogin(MainActivity.this);
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
}
