package com.sinoautodiagnoseos.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.CarBrands.CarBrands;
import com.sinoautodiagnoseos.entity.CarBrands.CarInfo;
import com.sinoautodiagnoseos.entity.FaulTranges.FaulTranges;
import com.sinoautodiagnoseos.net.requestApi.HttpRequestApi;
import com.sinoautodiagnoseos.net.requestSubscribers.HttpSubscriber;
import com.sinoautodiagnoseos.net.requestSubscribers.SubscriberOnListener;
import com.sinoautodiagnoseos.ui.adapter.CarBrandListAdapter;
import com.sinoautodiagnoseos.ui.adapter.FaultAdapter;
import com.sinoautodiagnoseos.ui.loginui.SwipeBackActivity;
import com.sinoautodiagnoseos.ui.sidebar.SideBar;
import com.sinoautodiagnoseos.utils.CommUtil;
import com.sinoautodiagnoseos.utils.Constant;
import com.sinoautodiagnoseos.utils.SharedPreferences;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * 视频求助页面
 * Created by HQ_Demos on 2017/5/10.
 */

public class VideoHelpActivity extends SwipeBackActivity{
    private FrameLayout image_back;
    private ImageView setting_image;
    private TextView person_tv;
    private GridView guzhang_gridview;
    private ListView carbrand_gridview;
    private SideBar sideBar;
    private FaultAdapter f_adapter;
    private CarBrandListAdapter cb_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videohelper);
        initData();
        initView();
    }

    private void initData() {
        getFaulTranges();
        getAllCarBrands();
    }

    private void initView() {
        person_tv = (TextView) findViewById(R.id.person_tv);
        setting_image = (ImageView) findViewById(R.id.setting_image);
        image_back = (FrameLayout) findViewById(R.id.back_click);
        person_tv.setText("请选择");
        setting_image.setVisibility(View.GONE);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        sideBar= (SideBar) findViewById(R.id.sb_city);

    }

    private List<FaulTranges.Faul> faults=new ArrayList<>();
    /**
     * 获取故障范围
     */
    int faul_flag = 0;
    public void getFaulTranges() {
        Constant.TOKEN= SharedPreferences.getInstance().getString("checktoken","");
        Constant.REGISTRATION=SharedPreferences.getInstance().getString("RegistrationId","");
        HttpRequestApi.getInstance().getFaulTrangesApi(new HttpSubscriber<FaulTranges>(new SubscriberOnListener<FaulTranges>() {
            @Override
            public void onSucceed(final FaulTranges data) {
                faults=data.getData();
                guzhang_gridview= (GridView) findViewById(R.id.guzhang_gridview);
                f_adapter=new FaultAdapter(VideoHelpActivity.this,faults
//                        , new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (view.getId()==R.id.fault_btn){
//                            switch (faul_flag){
//                                case 0:
//                                    faul_flag = 1;
//                                    view.setBackgroundResource(R.drawable.btn_selector);
//                            break;
//                                case 1:
//                                    view.setBackgroundResource(R.drawable.btn_unselector);
//                                    faul_flag = 0;
//                                break;
//                             }
//                        }
//                    }
//                }
                );
                guzhang_gridview.setAdapter(f_adapter);
            }

            @Override
            public void onError(int code, String msg) {

            }
        },VideoHelpActivity.this));
    }

    String[] array={"A","B","C","D","E","F","G"
            ,"H","I","J","K","L","M","N"
            ,"O","P","Q","R","S","T"
            ,"U","V","W","X","Y","Z"};

    private Map<String, List<CarBrands.Brand>> carbrands_list;
    private List<CarInfo>carInfoList=new ArrayList<>();
    private CarInfo carInfo=null;
    /**
     * 获取车辆品牌
     */
    public void getAllCarBrands() {
        Constant.TOKEN= SharedPreferences.getInstance().getString("checktoken","");
        Constant.REGISTRATION=SharedPreferences.getInstance().getString("RegistrationId","");
        HttpRequestApi.getInstance().getCarBrands(new HttpSubscriber<CarBrands>(new SubscriberOnListener<CarBrands>() {
            @Override
            public void onSucceed(CarBrands data) {
                Log.e("onSucceed","OKOKOKOKOKOKOK");
                if (data.getData().size()==0){
                    carbrands_list=new Hashtable<String, List<CarBrands.Brand>>();
                }else {
                    carbrands_list=data.getData();
                    for (int i=0;i<array.length;i++){
                        List<CarBrands.Brand> carList=carbrands_list.get(array[i]);
                        if (carList!=null&&carList.size()>0){
                            for (CarBrands.Brand b :carList){
                                carInfo=new CarInfo(b.getText(),array[i],b.getValue());
                                carInfoList.add(carInfo);
                                System.out.println(array[i]+"-----"+b.getText());
                            }
                        }
                    }
                    System.out.println("==="+carInfoList.size());
                }
                carbrand_gridview= (ListView) findViewById(R.id.carbrand_gridview);
                cb_adapter=new CarBrandListAdapter(VideoHelpActivity.this,carInfoList);
                carbrand_gridview.setAdapter(cb_adapter);
                // 设置需要显示的索引栏内容
                sideBar.setLetter(CommUtil.getLetters(carInfoList));
                sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

                    @Override
                    public void onTouchingLetterChanged(String s) {
                        int position = CommUtil.getLetterPosition(carInfoList, s);
                        if (position != -1) {
                            carbrand_gridview.setSelection(position);
                        }
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
            }
        },VideoHelpActivity.this));
    }
}
