package com.sinoautodiagnoseos.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.CarBrands.Alphabet;
import com.sinoautodiagnoseos.entity.CarBrands.CarBrands;
import com.sinoautodiagnoseos.entity.FaulTranges.FaulTranges;
import com.sinoautodiagnoseos.entity.FaulTranges.MyGoodRrange;
import com.sinoautodiagnoseos.ui.adapter.GoodrangeAdapter;
import com.sinoautodiagnoseos.ui.adapter.VehicleBrandAdapter;
import com.sinoautodiagnoseos.ui.loginui.SwipeBackActivity;
import com.sinoautodiagnoseos.ui.selectstoresui.CircleTextView;
import com.sinoautodiagnoseos.ui.selectstoresui.MySlideView;
import com.sinoautodiagnoseos.utils.Constant;
import com.sinoautodiagnoseos.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.sinoautodiagnoseos.R.id.view;
import static com.sinoautodiagnoseos.R.style.dialog;

/**
 * 车辆选择界面  有车辆数据 dialog的数据
 * Created by dingxujun on 2017/5/23.
 */

public class TransportationSelectionActivity extends SwipeBackActivity   implements MySlideView.onTouchListener,
        VehicleBrandAdapter.onItemClickListener, View.OnClickListener ,GoodrangeAdapter.MyViewHodler.ClickListener{

    private CarBrands carBrands;
    RecyclerView mRecyclerView;
    private List<CarBrands.Brand> brands;
    private VehicleBrandAdapter adpter;
    private CircleTextView circleTxt;
    private MySlideView mySlideView;
    private List<String> listzimu;
    private GridLayoutManager manager;
    private FrameLayout back_click;
    private FaulTranges fauTrangesData;
    private Dialog goodAtrangeDialog;
    private RecyclerView trad_recyclerview;
    private GoodrangeAdapter dialogAdapter;
    private TextView tv_ok;
    private TextView tv_cancel;
    private MyGoodRrange myrange;
    private TextView pingpai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_selection);
        initView();
        getTranspSelectInfo();
        initListenerOclick();
    }
    /**
     *控件点击事件
     *@author dingxujun
     *created at 2017/5/23 13:17
     */
    private void initListenerOclick() {
        back_click.setOnClickListener(this);
    }

    /**
     *初始化控件
     *@author dingxujun
     *created at 2017/5/23 12:52
     */
    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mySlideView = (MySlideView) findViewById(R.id.my_slide_view);//右侧索引栏
        circleTxt = (CircleTextView) findViewById(R.id.my_circle_view);//悬浮的小方框字母
        back_click = (FrameLayout) findViewById(R.id.back_click);//返回按钮
    }

    /**
     *获取SkillManagementActivity那边intent传过来的数据
     *@author dingxujun
     *created at 2017/5/23 10:48
     */
    private void getTranspSelectInfo() {
        Intent intent =getIntent();
        Bundle bundleExtras = intent.getExtras();
        carBrands = (CarBrands) bundleExtras.getSerializable("carBrands");//车辆品牌数据
        fauTrangesData = (FaulTranges) bundleExtras.getSerializable("faulTranges");//擅长范围数据
        if (fauTrangesData!=null) {
            List<FaulTranges.Faul> data = fauTrangesData.getData();
            System.out.println("我是======擅长范围数据" + data.toString());
        }
        stepGetInfo();
    }
    /*
    * 取出map集合中所需要的数据
    * */
    private void stepGetInfo() {
        Set<String> keys = carBrands.getData().keySet();
        listzimu = new ArrayList<String>();
        List<String> list1 =new ArrayList<String>();
        Alphabet ziMU =new Alphabet();
        for (String keyt:keys) {
            ziMU .setLetter(keyt);
            listzimu.add(keyt);
            list1.add(keyt);
            List<CarBrands.Brand> brands = carBrands.getData().get(keyt);
            for (int i = 0; i <brands.size() ; i++) {
                String text = carBrands.getData().get(keyt).get(i).getText();
                listzimu.add(text);
            }
        }
        mySlideView.setArray(list1);
        mySlideView.setListener(TransportationSelectionActivity.this);
        for (int i = 0; i < listzimu.size(); i++) {//遍历listzimu集合
            String s = listzimu.get(i);
            System.out.println("-------我是-----"+s);
        }
        System.out.println("--------------我是字母键集合大小"+list1.size());
        System.out.println("--------------我是字母集合大小"+ listzimu.size());
        System.out.println("--------------我是字母"+ listzimu.get(6));
        if (listzimu!=null) {
            adpter = new VehicleBrandAdapter(TransportationSelectionActivity.this, listzimu);
        }
        adpter.setListener(TransportationSelectionActivity.this);
        manager = new GridLayoutManager(TransportationSelectionActivity.this, 4);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adpter.getItemViewType(position) == adpter.VIEW_TYPE_TITLE) {
                    return 4;
                }
                return 1;
            }
        });
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adpter);
    }

    /*
    * 悬浮字母和RecyclerView联动的滑动的监听
    *
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
        for (int i = 0; i <listzimu .size(); i++) {
            if (listzimu.get(i).equals(textView)) {
                selectPosition = i;
                break;
            }
        }
        scrollPosition(selectPosition);
    }
    /**
     *RecyclerView位置滑动的监听
     *@author dingxujun
     *created at 2017/5/23 12:59
     */
    private void scrollPosition(int index) {
        int firstPosition = manager.findFirstVisibleItemPosition();
        int lastPosition = manager.findLastVisibleItemPosition();
        if (index <= firstPosition) {
            mRecyclerView.scrollToPosition(index);
        } else if (index <= lastPosition) {
            int top = mRecyclerView.getChildAt(index - firstPosition).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(index);
        }
    }
    /*
    * 自定义的RecyclerView 条目监听回调方法
    * */
    @Override
    public void itemClick(int position) {
        ToastUtils.showShort(getApplicationContext(),"你点击了"+
                listzimu.get(position));
        EventBus.getDefault().post( listzimu.get(position));//把所点击的数据传过去
        myrange=new MyGoodRrange();
        myrange.name= listzimu.get(position);
        showGoodAtrangeDialog();
    }
    /**
     *显示擅长范围的dialog
     *@author dingxujun
     *created at 2017/5/23 16:32
     */
    private void showGoodAtrangeDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.good_range_dialog, null);
        goodAtrangeDialog = new Dialog(this, R.style.DialogTheme);
        goodAtrangeDialog.setContentView(view);
       if (adpter.check(myrange.getName())){//选择的如果是字母就直接return
           return;
       }else {
           goodAtrangeDialog.show();//否则显示dialog
       }
        trad_recyclerview = (RecyclerView) view.findViewById(R.id.trad_recyclerview);
        getDialogWindow();
       dialogAdapter = new GoodrangeAdapter(this,fauTrangesData.getData(),this);
        trad_recyclerview.setAdapter(dialogAdapter);
        GridLayoutManager layoutManager =new GridLayoutManager(this,4);//让Recyclerview显示Gridview样式的布局，呈4列显示
        trad_recyclerview.setLayoutManager(layoutManager);
        pingpai = (TextView) view.findViewById(R.id.pinpai_my);
        pingpai.setText(myrange.getName());
        tv_ok = (TextView) view.findViewById(R.id.tv_OK);
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmoClick();
            }
        });
        cancleClick();
    }
    /*
      * 确定点击事件
      *
      * */
    private void confirmoClick() {
        if (myrange.list!=null&&myrange.list.size()!=0) {
            goodAtrangeDialog.dismiss();
            Intent intent = new Intent();
            intent.putExtra("myrange", myrange);
            setResult(Constant.REQUEST_CODE_108, intent);
            finish();
        }else {
            ToastUtils.showShort(this,"还没有选择哦");
        }
    }

    /*
    * 取消点击事件
    * */
    private void cancleClick() {
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goodAtrangeDialog.dismiss();
                myrange=null;
            }
        });
    }

    /**
     *dialog 在父布局位置的方法
     *@author dingxujun
     *created at 2017/5/23 16:40
     */
    private void getDialogWindow() {
        Window window = goodAtrangeDialog.getWindow();
        window.getDecorView().setPadding(50, 50, 50, 50);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
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
    /*
    * dialog条目点击回调
    *
    *
    * */
    @Override
    public void onItemClicked(int position) {
        dialogAdapter.switchSelectedState(position);
        System.out.println("---------我被点击了条目shang------"+position);
        if (myrange.list==null)
            myrange.list = new ArrayList<String>();
        if (myrange.list.contains(fauTrangesData.getData().get(position).getText())) {
            System.out.println("---------移除------"+fauTrangesData.getData().get(position).getText());
            myrange.list.remove(fauTrangesData.getData().get(position).getText());
        } else {
            System.out.println("---------我被点击了条目shang------"+fauTrangesData.getData().get(position).getText());
            myrange.list.add(fauTrangesData.getData().get(position).getText());
        }

        System.out.println("---------我被点击了条目-xia-----"+position);
    }
    /*
    *
    * dialog条目长按点击回调
    * */
    @Override
    public boolean onItemLongClicked(int position) {
        return true;
    }
}
