package com.sinoautodiagnoseos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.github.promeg.pinyinhelper.Pinyin;
import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.Station.MCity;
import com.sinoautodiagnoseos.entity.Station.StationList;
import com.sinoautodiagnoseos.net.requestApi.HttpRequestApi;
import com.sinoautodiagnoseos.net.requestSubscribers.HttpSubscriber;
import com.sinoautodiagnoseos.net.requestSubscribers.SubscriberOnListener;
import com.sinoautodiagnoseos.ui.adapter.CityAdapter;
import com.sinoautodiagnoseos.ui.loginui.SwipeBackActivity;
import com.sinoautodiagnoseos.ui.selectstoresui.CircleTextView;
import com.sinoautodiagnoseos.ui.selectstoresui.DividerDecoration;
import com.sinoautodiagnoseos.ui.selectstoresui.MySlideView;
import com.sinoautodiagnoseos.ui.selectstoresui.StickyDecoration;
import com.sinoautodiagnoseos.utils.Constant;
import com.sinoautodiagnoseos.utils.SharedPreferences;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 选择店铺界面
 * Created by dingxujun on 2017/5/16.
 */

public class SelectStoresActivity extends SwipeBackActivity implements MySlideView.onTouchListener, CityAdapter.onItemClickListener, View.OnClickListener {
    public static final String TYPE_WORK ="typework" ;
    public  List<MCity> cityList = new ArrayList<>();
    private Set<String> firstPinYin = new LinkedHashSet<>();
    public List<String> pinyinList = new ArrayList<>();
    private PinyinComparator pinyinComparator;
    private MySlideView mySlideView;
    private CircleTextView circleTxt;
    private RecyclerView recyclerView;
    private CityAdapter adapter;
    private LinearLayoutManager layoutManager;
    private MCity mCity;
    private FrameLayout back_image;
    private List<StationList.Stations> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_stores);
        initView();
        storeRequest();
        initListenerOclick();
    }
    /**
     *点击事件
     *@author dingxujun
     *created at 2017/5/19 14:05
     */
    private void initListenerOclick() {
        back_image.setOnClickListener(this);
    }

    /*
      初始化控件
    * */
    private void initView() {
        cityList.clear();
        firstPinYin.clear();
        pinyinList.clear();
        pinyinComparator = new PinyinComparator();
        mySlideView = (MySlideView) findViewById(R.id.my_slide_view);
        circleTxt = (CircleTextView) findViewById(R.id.my_circle_view);
        recyclerView = (RecyclerView) findViewById(R.id.rv_sticky_example);
        back_image = (FrameLayout) findViewById(R.id.back_click);
    }
    /*
    * 门店数据网络
    * */
    private void storeRequest() {
        HttpRequestApi.getInstance().getAllStations(new HttpSubscriber<StationList>(new SubscriberOnListener<StationList>() {
            @Override
            public void onSucceed(StationList data) {
                Constant.TOKEN = SharedPreferences.getInstance().getString("checktoken", "");
                Constant.REGISTRATION = SharedPreferences.getInstance().getString("RegistrationId", "");
                System.out.println("-----------我是门店请求成功----------------");
                list = data.getData();
                for (int i = 0; i < list.size(); i++) {
                    mCity = new MCity();
                    mCity.setCityName(list.get(i).getName());
                    mCity.setCityPinyin(transformPinYin(list.get(i).getName()));
                    cityList.add(mCity);
                }
                Collections.sort(cityList, pinyinComparator);
                for (MCity city : cityList) {
                    firstPinYin.add(city.getCityPinyin().substring(0, 1));
                }
                for (String string : firstPinYin) {
                    pinyinList.add(string);
                }
                mySlideView.setArray(pinyinList);
                mySlideView.setListener(SelectStoresActivity.this);
                layoutManager = new LinearLayoutManager(SelectStoresActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                adapter = new CityAdapter(getApplicationContext(), cityList);
                adapter.setListener(SelectStoresActivity.this);
                recyclerView.setAdapter(adapter);
                recyclerView.addItemDecoration(new StickyDecoration(cityList,getApplicationContext()));//粘性控件 悬浮字母条目
                recyclerView.addItemDecoration(new DividerDecoration(SelectStoresActivity.this));//给Recyclerview添加分割线
                System.out.println("=======我是集合大小=========" + cityList.size());
                System.out.println("=======我是门店=========" + mCity.getCityName());
            }
            @Override
            public void onError(int code, String msg) {
                System.out.println("-----------我是门店请求失败----------------");
            }
        }, SelectStoresActivity.this));

    }
    public String transformPinYin(String character) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < character.length(); i++) {
            buffer.append(Pinyin.toPinyin(character.charAt(i)));
        }
        return buffer.toString();
    }
    /*
    * adpter的条目点击事件，根据position确定你点击的是谁
    * */
    @Override
    public void itemClick(int position) {
        String storesid = list.get(position).getId();
        EventBus.getDefault().post(storesid);
        Toast.makeText(getApplicationContext(),
                cityList.get(position).getCityName(), Toast.LENGTH_SHORT).show();
        Intent mIntent =new Intent();
        mIntent.putExtra(TYPE_WORK,cityList.get(position).getCityName());
        //REQUEST_CODE_105结果码
        setResult(Constant.REQUEST_CODE_105,mIntent);
        finish();
    }
    /*
    * MyslideView的接口回调方法，是View给Activity传值
    * */
    @Override
    public void showTextView(String textView, boolean dismiss) {
        if (dismiss) {
            circleTxt.setVisibility(View.GONE);
        } else {
            circleTxt.setVisibility(View.VISIBLE);
            circleTxt.setText(textView);
        }

        int selectPosition = 0;
        for (int i = 0; i < cityList.size(); i++) {
            if (cityList.get(i).getFirstPinYin().equals(textView)) {
                selectPosition = i;
                break;
            }
        }
        scrollPosition(selectPosition);
    }
    private void scrollPosition(int index) {
        int firstPosition = layoutManager.findFirstVisibleItemPosition();
        int lastPosition = layoutManager.findLastVisibleItemPosition();
        if (index <= firstPosition) {
            recyclerView.scrollToPosition(index);
        } else if (index <= lastPosition) {
            int top = recyclerView.getChildAt(index - firstPosition).getTop();
            recyclerView.scrollBy(0, top);
        } else {
            recyclerView.scrollToPosition(index);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.back_click:
                finish();
                break;
            default:
                break;

        }
    }

    public class PinyinComparator implements Comparator<MCity> {
        @Override
        public int compare(MCity cityFirst, MCity citySecond) {
            return cityFirst.getCityPinyin().compareTo(citySecond.getCityPinyin());
        }
    }
}
