package com.sinoautodiagnoseos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.CarBrands.CarInfo;
import com.sinoautodiagnoseos.ui.adapter.CarBrandListAdapter;
import com.sinoautodiagnoseos.ui.loginui.SwipeBackActivity;
import com.sinoautodiagnoseos.ui.sidebar.SideBar;
import com.sinoautodiagnoseos.utils.CommUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *  智库车辆品牌
 * Created by HQ_Demos on 2017/5/30.
 */

public class CarInfoActivity extends SwipeBackActivity {
    private FrameLayout back_click;
    private TextView person_tv;
    private ImageView setting_image;
    private ListView carbrand_gridview;
    private SideBar sideBar;
    List<CarInfo> carInfoList=new ArrayList<>();
    private CarBrandListAdapter cb_adapter;
    private String carValue="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carinfo_layout);
        initData();
    }

    private void initData() {
        carInfoList= (List<CarInfo>) getIntent().getExtras().get("carInfoList");
        System.out.println("carInfoList---"+carInfoList.size());
        initView();
    }

    private void initView() {
        back_click= (FrameLayout) findViewById(R.id.car_back_click);
        person_tv= (TextView) findViewById(R.id.choise_tv);
        setting_image= (ImageView) findViewById(R.id.setting_image);
        person_tv.setText("请选择");
        setting_image.setVisibility(View.GONE);
        back_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("carValue", carValue);
                setResult(1001, intent);
                finish();
            }
        });
        carbrand_gridview= (ListView) findViewById(R.id.zkcarbrand_gridview);
        cb_adapter=new CarBrandListAdapter(CarInfoActivity.this,carInfoList);
        carbrand_gridview.setAdapter(cb_adapter);
        // 设置需要显示的索引栏内容
        sideBar= (SideBar) findViewById(R.id.zksb_city);
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
                carValue=carInfoList.get(position).getCarValue();
                System.out.println(carName+"\n"+carValue);
                cb_adapter.setSeclection(position);
                cb_adapter.notifyDataSetChanged();
            }
        });
    }
}
