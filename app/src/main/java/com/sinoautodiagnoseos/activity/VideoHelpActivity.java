package com.sinoautodiagnoseos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.sinoautodiagnoseos.openvcall.model.Faults;
import com.sinoautodiagnoseos.openvcall.model.ListExpertsSearchDto;
import com.sinoautodiagnoseos.openvcall.ui.WaitingActivity;
import com.sinoautodiagnoseos.ui.adapter.CarBrandListAdapter;
import com.sinoautodiagnoseos.ui.adapter.FaultAdapter;
import com.sinoautodiagnoseos.ui.loginui.SwipeBackActivity;
import com.sinoautodiagnoseos.ui.sidebar.SideBar;
import com.sinoautodiagnoseos.utils.CommUtil;
import com.sinoautodiagnoseos.utils.Constant;
import com.sinoautodiagnoseos.utils.OnMultiClickListener;
import com.sinoautodiagnoseos.utils.SharedPreferences;
import com.sinoautodiagnoseos.utils.ToastUtils;

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
    private Button search_for_help;
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
        search_for_help= (Button) findViewById(R.id.search_for_help);
        person_tv.setText("请选择");
        setting_image.setVisibility(View.GONE);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        sideBar= (SideBar) findViewById(R.id.sb_city);

        search_for_help.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (searchDto==null){
                    ToastUtils.showShort(VideoHelpActivity.this,"请选择");
                }else {
                    if(searchDto.getCarBrandId()==null){
                        ToastUtils.showShort(VideoHelpActivity.this,"请选择车辆品牌");
                    }else if (searchDto.getFaults()==null){
                        ToastUtils.showShort(VideoHelpActivity.this,"请选择故障范围");
                    }else {
                        getHelpData();
                    }
                }
            }
        });

    }

    private void getHelpData() {
        System.out.println("--------"+searchDto.getCarBrandId());
        Intent to_waitActivity=new Intent(VideoHelpActivity.this, WaitingActivity.class);
        to_waitActivity.putExtra("data",searchDto);
        to_waitActivity.putExtra("from_where","0");
        to_waitActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(to_waitActivity);
    }

    private List<FaulTranges.Faul> faults=new ArrayList<>();
    /**
     * 获取故障范围
     */
    List<Faults>fault_list=new ArrayList<>();
    List<Faults>remove_list=new ArrayList<>();
    Faults faults_bean=null;
    public void getFaulTranges() {
        Constant.TOKEN= SharedPreferences.getInstance().getString("checktoken","");
        Constant.REGISTRATION=SharedPreferences.getInstance().getString("RegistrationId","");
        HttpRequestApi.getInstance().getFaulTrangesApi(new HttpSubscriber<FaulTranges>(new SubscriberOnListener<FaulTranges>() {
            @Override
            public void onSucceed(final FaulTranges data) {
                faults=data.getData();
                guzhang_gridview= (GridView) findViewById(R.id.guzhang_gridview);
                f_adapter=new FaultAdapter(VideoHelpActivity.this,faults);
                guzhang_gridview.setAdapter(f_adapter);
                guzhang_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        for (int i=0;i<faults.size();i++){
//                            if (position==i){//当前选中的Item改变背景颜色
//                                faults.get(i).setSelect(true);
//                            }else {
//                                faults.get(i).setSelect(false);
//                            }
//                        }
//                        f_adapter.notifyDataSetChanged();
                        f_adapter.setSelection(position);
                        f_adapter.notifyDataSetChanged();
                        if (faults.get(position).isSelect()==false){
                            String txt=faults.get(position).getText();
                            String value=faults.get(position).getValue();
                            faults_bean=new Faults(position,txt,value);
//                            faults_bean.setText(txt);
//                            faults_bean.setValue(value);
//                            faults_bean.setPosition(position);
                            fault_list.add(faults_bean);
                            System.out.println("选择了-"+position+"\n"+faults.get(position).getText());
                        }else{
                            for (Faults fault:fault_list){
                                if (fault.getPosition()==position){
                                    fault_list.remove(fault);
                                    System.out.println("移除了-"+position+"\n"+faults.get(position).getText());
                                }
                            }
                        }
                        searchDto.setFaults(fault_list);
                        System.out.println("------------"+fault_list.size());
                    }
                });
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
    ListExpertsSearchDto searchDto=new ListExpertsSearchDto();
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
                carbrand_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String carName=carInfoList.get(position).getCarName();
                        String carValue=carInfoList.get(position).getCarValue();
                        searchDto.setCarBrandId(carValue);
                        System.out.println(carName+"\n"+carValue);
                        cb_adapter.setSeclection(position);
                        cb_adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
            }
        },VideoHelpActivity.this));
    }
}
