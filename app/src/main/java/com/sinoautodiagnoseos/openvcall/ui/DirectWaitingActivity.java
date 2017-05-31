package com.sinoautodiagnoseos.openvcall.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.propeller.ui.TimeCountDown;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by HQ_Demos on 2017/3/6.
 */
public class DirectWaitingActivity extends BaseActivity implements TimeCountDown.OnTimerCountDownListener {
    private ImageView foundDevice;
    private TextView press_txt;
    private TimeCountDown countDown;
    private ImageView end_call;
    private RippleBackground rippleBackground;
    private Handler handler;
    Timer timer = new Timer();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_experts_waiting);
//        getFlag();
        initView();
    }

    @Override
    protected void initUIandEvent() {

    }

    @Override
    protected void deInitUIandEvent() {

    }


    String finish_activity="";
    //获取标识码
    private void getFlag() {
        finish_activity=getIntent().getStringExtra("finish");
    }

    @Override
    protected void onStop() {
        countDown.cancel();
        super.onStop();
        System.out.println("退出");
        this.finish();
    }

    private void initView() {
        handler = new Handler();
        rippleBackground = (RippleBackground) findViewById(R.id.content);
        foundDevice = (ImageView) findViewById(R.id.foundDevice);
        press_txt = (TextView) findViewById(R.id.press_txt);
        ImageView imageView = (ImageView) findViewById(R.id.centerImage);

        countDown = (TimeCountDown) findViewById(R.id.countDown);
        end_call = (ImageView) findViewById(R.id.end_call);
        countDown.setOnTimerCountDownListener(this);
        countDown.initTimer();

        press_txt.setVisibility(View.GONE);
        rippleBackground.startRippleAnimation();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                foundDevice();
            }
        }, 3000);
        //点击匹配专家传过来的标识码
            press_txt.setVisibility(View.VISIBLE);
            press_txt.setText(getResources().getString(R.string.str_wait));
            countDown.setVisibility(View.VISIBLE);
//        if (finish_activity.equals("is_ok")){
//            countDown.cancel();
//        }
    }


    private void foundDevice() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList = new ArrayList<Animator>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        animatorSet.playTogether(animatorList);
        foundDevice.setVisibility(View.VISIBLE);
        animatorSet.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        rippleBackground.stopRippleAnimation();
        countDown.cancel();
    }


    @Override
    public void onCountDownStart() {

    }

    @Override
    public void onCountDownLoading(int currentCount) {

    }

    @Override
    public void onCountDownError() {

    }

    @Override
    public void onCountDownFinish() {
        Log.e("------Finish--------","---onCountDownFinish----");
//        Toast.makeText(DirectWaitingActivity.this,"该专家没有应答",Toast.LENGTH_SHORT).show();
        finish();
    }
}
