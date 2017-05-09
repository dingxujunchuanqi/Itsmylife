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

import com.google.gson.Gson;
import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.User.Skill;
import com.sinoautodiagnoseos.net.requestApi.HttpRequestApi;
import com.sinoautodiagnoseos.net.requestSubscribers.HttpSubscriber;
import com.sinoautodiagnoseos.net.requestSubscribers.SubscriberOnListener;
import com.sinoautodiagnoseos.openvcall.model.Faults;
import com.sinoautodiagnoseos.openvcall.model.ListExpertsSearchDto;
import com.sinoautodiagnoseos.propeller.ui.TimeCountDown;
import com.sinoautodiagnoseos.utils.Constant;
import com.skyfishjy.library.RippleBackground;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import okhttp3.RequestBody;

/**
 * Created by HQ_Demos on 2017/3/6.
 */
public class WaitingActivity extends BaseActivity implements TimeCountDown.OnTimerCountDownListener {
    private ImageView foundDevice;
    private TextView press_txt;
    private TimeCountDown countDown;
    private ImageView end_call;
    private RippleBackground rippleBackground;
    private Handler handler;
    private String flag;//<===来源哪个页面的操作标识
    private String json;//RN封装好的json格式数据
    Timer timer = new Timer();

    @Override
    protected void initUIandEvent() {

    }

    @Override
    protected void deInitUIandEvent() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        //无title
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_experts_waiting);
        getFlag();
        initView();
    }

    @Override
    protected void onStop() {
        countDown.cancel();
        super.onStop();
        System.out.println("退出");
        this.finish();
    }

    //获取标识码
    private void getFlag() {
        flag = getIntent().getStringExtra("from_where");
        json = getIntent().getStringExtra("json");
        System.out.println("intent_json="+json);
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
        countDown.setOnClickListener(listener);
        end_call.setOnClickListener(listener);

        press_txt.setVisibility(View.GONE);
        rippleBackground.startRippleAnimation();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                foundDevice();
            }
        }, 3000);
        //点击匹配专家传过来的标识码
        if (flag.equals("0")) {
            press_txt.setVisibility(View.VISIBLE);
            press_txt.setText(getResources().getString(R.string.str_wait));
            countDown.setVisibility(View.VISIBLE);
            end_call.setVisibility(View.GONE);
            findoneexperts();// 匹配专家接口方法
        }
        //
        else if (flag.equals("1")) {
            countDown.setVisibility(View.GONE);
            end_call.setVisibility(View.GONE);
        }
    }

    /**
     * 匹配专家方法入口
     */
    private void findoneexperts() {
        ReadJSON(json);
        Gson gson = new Gson();
        String json = gson.toJson(les);

        Log.e("JSON", json);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
//        HttpRequestApi.getInstance().findoneexperts(requestBody,
//                new HttpSubscriber<RoomInfo>(new SubscriberOnListener<RoomInfo>() {
//                    @Override
//                    public void onSucceed(RoomInfo data) {
//                        if (data.getData().getRoomId().equals("")){
//                           countDown.cancel();
//                            ToastUtils.showShort(AppContext.getInstance(),"暂时没有空闲专家，请稍后再试！");
//                        }else {
//                            Constant.ROOMID=data.getData().getRoomId();//保存房间号
////                            forwardToRoom();//匹配成功，直接进入创建好房间进入
//                            Constant.SACCOUNT = Constant.USERNAME;
//                            countDown.cancel();
//                        }
//                    }
//                    @Override
//                    public void onError(int code, String msg) {
//                        countDown.cancel();
//                    }
//                }, WaitingActivity.this));
    }

    Faults fault;
    List<Faults> faults = new ArrayList<>();
    ListExpertsSearchDto les ;
    /**
     * 读取前端传过来的数据
     * @param json
     */
    private void ReadJSON(String json){
        les = new ListExpertsSearchDto();
        JSONObject parser = null;
        try {
            parser = new JSONObject(json);
            String carBrandId = parser.get("carBrandId").toString();
            JSONArray array = parser.getJSONArray("faults");
            for (int i = 0;i<array.length();i++){
                JSONObject faultsObject = array.getJSONObject(i);
                fault = new Faults();
                String faults_text=faultsObject.get("text").toString();
                String faults_value=faultsObject.get("value").toString();
                fault.setText(faults_text);fault.setValue(faults_value);
                faults.add(fault);
            }
            les.setCarBrandId(carBrandId);
            les.setFaults(faults);
            Constant.Experts=les;
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.countDown:
                    break;
                case R.id.end_call:
                    countDown.cancel();
//                    forwardToRoom();//跳转进入房间
                    break;
            }
        }
    };


    @Override
    protected void onDestroy() {
        System.out.println("出去了");
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
        showLongToast(getResources().getString(R.string.not_response_from_experts));
        HttpRequestApi.getInstance().getCallStatus(Constant.ROOMID,
                new HttpSubscriber<Skill>(new SubscriberOnListener<Skill>() {
                    @Override
                    public void onSucceed(Skill data) {

                    }

                    @Override
                    public void onError(int code, String msg) {

                    }
                },WaitingActivity.this));
        finish();
    }
}
