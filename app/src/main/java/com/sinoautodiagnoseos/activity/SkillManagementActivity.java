package com.sinoautodiagnoseos.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.CarBrands.CarBrands;
import com.sinoautodiagnoseos.entity.FaulTranges.FaulTranges;
import com.sinoautodiagnoseos.entity.FaulTranges.MyGoodRrange;
import com.sinoautodiagnoseos.net.requestApi.HttpRequestApi;
import com.sinoautodiagnoseos.net.requestSubscribers.HttpSubscriber;
import com.sinoautodiagnoseos.net.requestSubscribers.SubscriberOnListener;
import com.sinoautodiagnoseos.ui.UIHelper;
import com.sinoautodiagnoseos.ui.adapter.MyrangeAapter;
import com.sinoautodiagnoseos.ui.loginui.SwipeBackActivity;
import com.sinoautodiagnoseos.utils.Constant;
import com.sinoautodiagnoseos.utils.LogUtils;
import com.sinoautodiagnoseos.utils.SharedPreferences;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 技能管理界面
 * Created by dingxujun on 2017/5/21.
 */

public class SkillManagementActivity extends SwipeBackActivity implements View.OnClickListener, MyrangeAapter.onItemClickListener {
    private String TAG = this.getClass().getName();//获得类名
    private RelativeLayout addnewSkill;
    private CarBrands data;
    private ImageView setting_image;
    private TextView transportselectTV;
    private FrameLayout back_click;
    private FaulTranges faulTranges;
    private RecyclerView skillrecyclerview;
    private List<MyGoodRrange> qwlist;
    private MyrangeAapter rangeAapter;
    private MyGoodRrange mRange;
    private Dialog deleteDilog;
    private TextView tv_cancel,tv_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skillmanagement);
        initView();
        initListenerOclick();
        vehicleBrandRequest();
        goodAtRangeRequest();
        EventBus.getDefault().register(this);
    }

    /**
     * 擅长范围的数据请求
     *
     * @author dingxujun
     * created at 2017/5/23 14:28
     */
    private void goodAtRangeRequest() {
        Constant.TOKEN = SharedPreferences.getInstance().getString("checktoken", "");
        Constant.REGISTRATION = SharedPreferences.getInstance().getString("RegistrationId", "");
        HttpRequestApi.getInstance().getFaulTrangesApi(new HttpSubscriber<FaulTranges>(new SubscriberOnListener<FaulTranges>() {
            @Override
            public void onSucceed(FaulTranges faulTrangesData) {
                SkillManagementActivity.this.faulTranges = faulTrangesData;
                if (click && data != null) {
                    UIHelper.showTransportionSelection
                            (SkillManagementActivity.this, data, faulTrangesData);
                    click = false;
                }

                LogUtils.deBug(TAG, "----擅长范围数据请求成功-----");
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.deBug(TAG, "----擅长范围数据请求失败-----");
            }
        }, SkillManagementActivity.this));
    }

    /**
     * 点击事件
     *
     * @author dingxujun
     * created at 2017/5/23 9:46
     */
    private void initListenerOclick() {
        addnewSkill.setOnClickListener(this);
        back_click.setOnClickListener(this);
    }

    /**
     * 控件初始化
     *
     * @author dingxujun
     * created at 2017/5/23 9:44
     */
    private void initView() {
        addnewSkill = (RelativeLayout) findViewById(R.id.add_newSkill);//添加新技能
        setting_image = (ImageView) findViewById(R.id.setting_image);
        setting_image.setVisibility(View.GONE);
        transportselectTV = (TextView) findViewById(R.id.person_tv);
        transportselectTV.setText(R.string.Management_Applied_Personal_Skills_Training);
        back_click = (FrameLayout) findViewById(R.id.back_click);//返回TAb
        skillrecyclerview = (RecyclerView) findViewById(R.id.skillrecyclerview);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE_107&&data!=null) {//获取那边带回来的数据
                mRange = (MyGoodRrange) data.getSerializableExtra("myrange");
            if (qwlist == null)
                qwlist = new ArrayList<>();
            if (mRange != null && mRange.list != null && mRange.list.size() > 0) {
                qwlist.add(mRange);
                EventBus.getDefault().post(qwlist);
                if (rangeAapter == null) {
                    rangeAapter = new MyrangeAapter(this, qwlist);
                    skillrecyclerview.setAdapter(rangeAapter);
                    RecyclerView.LayoutManager layoutManager
                            = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                    skillrecyclerview.setLayoutManager(layoutManager);
                    rangeAapter.setListener(this);
                } else {
                    rangeAapter.list = qwlist;
                    System.out.println("999999999999999==" + qwlist.size());
                    rangeAapter.notifyDataSetChanged();//recyclerview刷新数据adapter数据源的方法 和listiew一样
                    System.out.println("999999999999999==" + rangeAapter.list.size());
                }
            }
        }

    }

    /*
        *  车辆品牌数据请求
        *
        * */
    private void vehicleBrandRequest() {
        Constant.TOKEN = SharedPreferences.getInstance().getString("checktoken", "");
        Constant.REGISTRATION = SharedPreferences.getInstance().getString("RegistrationId", "");
        HttpRequestApi.getInstance().getCarBrands(new HttpSubscriber<CarBrands>(new SubscriberOnListener<CarBrands>() {
            @Override
            public void onSucceed(CarBrands data) {
                SkillManagementActivity.this.data = data;
                if (click && faulTranges != null) {
                    UIHelper.showTransportionSelection
                            (SkillManagementActivity.this, data, faulTranges);
                    click = false;
                }

                LogUtils.deBug(TAG, "----技能管理数据请求成功-----");
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.deBug(TAG, "----技能管理数据请求失败-----");
            }
        }, SkillManagementActivity.this));
    }

    boolean click;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_newSkill:
                if (data != null && faulTranges != null)
                    UIHelper.showTransportionSelection(this, data, faulTranges);
                else {
                    click = true;
                }
                break;
            case R.id.back_click:
                System.out.println("-------我要关闭----");
                finish();
                break;
            default:
                break;
        }
    }

    private String trademarkName;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receive(String trademarkName) {
        this.trademarkName = trademarkName;
        System.out.println("===========我是车辆品牌=====-----、、、、" + trademarkName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /*
    * 删除按钮的接口回调
    * */
    @Override
    public void itemClick(int position) {
        getDialog(position);
        deleteDilog.show();
    }
    private void getDialog(final int position) {
        deleteDilog = new Dialog(this, R.style.DialogTheme);
        View view = LayoutInflater.from(this).inflate(R.layout.delete_dialog, null);
        deleteDilog.setContentView(view);
        getDialogWindow();
        tv_ok = (TextView) view.findViewById(R.id.tv_OK);
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("-----------我是被删除的位置----"+position);
                qwlist.remove(position);
                rangeAapter.list = qwlist;
                rangeAapter.notifyItemRemoved(position);//移除要删除的条目之后刷新适配器
                rangeAapter.notifyItemRangeChanged(0,rangeAapter.getItemCount());//对于被删掉的位置及其后range大小范围内的view进行重新onBindViewHolder
                System.out.println("9999999999999999wo条目被删除了啊");
                deleteDilog.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDilog.dismiss();
            }
        });
    }

    /**
     *dialog 在父布局位置的方法
     *@author dingxujun
     *created at 2017/5/23 16:40
     */
    private void getDialogWindow() {
        Window window = deleteDilog.getWindow();
        window.getDecorView().setPadding(50, 50, 50, 50);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

}
