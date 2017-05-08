package com.sinoautodiagnoseos.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.activity.MainActivity;
import com.sinoautodiagnoseos.entity.CallRecord.CallRecord;
import com.sinoautodiagnoseos.entity.CallRecord.Experts_datas;
import com.sinoautodiagnoseos.net.requestApi.HttpRequestApi;
import com.sinoautodiagnoseos.net.requestSubscribers.HttpSubscriber;
import com.sinoautodiagnoseos.net.requestSubscribers.SubscriberOnListener;
import com.sinoautodiagnoseos.ui.adapter.DiagnoseAdapter;
import com.sinoautodiagnoseos.utils.Constant;
import com.sinoautodiagnoseos.utils.OnMultiClickListener;
import com.sinoautodiagnoseos.utils.SharedPreferences;

import java.util.ArrayList;
import java.util.List;



/**
 * 云诊页面
 * Created by HQ_Demos on 2017/4/24.
 */

public class DiagnoseFragment extends Fragment {
    private final String TAG=DiagnoseFragment.class.getSimpleName();
    private MainActivity context;
//    private int pno = 1;
//    private boolean isLoadAll;
    private ListView listView;
//    QuickAdapter<CallRecord.Record.Expert> adapter;
    private DiagnoseAdapter adapter;
    LinearLayout voice_btn,video_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_diagnose_pager, container, false);
        listView= (ListView) view.findViewById(R.id.listView);
        voice_btn= (LinearLayout) view.findViewById(R.id.btn_voice);
        video_btn= (LinearLayout) view.findViewById(R.id.btn_video);
        voice_btn.setOnClickListener(listener);
        voice_btn.setOnClickListener(listener);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context= (MainActivity) getActivity();
        initData();
        loadData();
    }

    OnMultiClickListener listener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            switch (v.getId()){
                case R.id.btn_voice:
                    callServiceTel();
                    break;
                case R.id.btn_video:
                    break;
            }
        }
    };

    private void initData() {
        getCallRecord();
    }
    Experts_datas experts;
    List<Experts_datas>experts_list;
    //获取最新三条记录
    private void getCallRecord() {
        experts_list=new ArrayList<>();
        Constant.TOKEN= SharedPreferences.getInstance().getString("checktoken","");
        Constant.REGISTRATION=SharedPreferences.getInstance().getString("RegistrationId","");
        HttpRequestApi.getInstance().getCallRecord(new HttpSubscriber<CallRecord>(new SubscriberOnListener<CallRecord>() {
            @Override
            public void onSucceed(CallRecord data) {
                for (int i = 0;i<data.getData().size();i++)
                {
                    experts=new Experts_datas();
                    String date_time=data.getData().get(i).getBeginOnUtc();
                    experts.setDate_time(date_time);
//                    experts_list.add(experts);
                    for (int j =0;j<data.getData().get(i).getExpertList().size();j++){
                        String avatar=data.getData().get(i).getExpertList().get(j).getAvatar();
                        String brandInfo=data.getData().get(i).getExpertList().get(j).getBrandInfo();
                        String nickName=data.getData().get(i).getExpertList().get(j).getNickName();
                        String skillInfo=data.getData().get(i).getExpertList().get(j).getSkillInfo();
                        String stationName=data.getData().get(i).getExpertList().get(j).getStationName();
                        experts.setAvatar(avatar);
                        experts.setBrandInfo(brandInfo);
                        experts.setNickName(nickName);
                        experts.setSkillInfo(skillInfo);
                        experts.setStationName(stationName);
                    }
                }
                experts_list.add(experts);
                Log.e(TAG,experts_list.size()+"");
                initView();
            }

            @Override
            public void onError(int code, String msg) {

            }
        }, context));
    }

    private void initView() {
        if (adapter==null){
            adapter=new DiagnoseAdapter(context,experts_list,R.layout.item_record_layout);
            listView.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
//        adapter=new QuickAdapter<CallRecord.Record.Expert>(context,R.layout.item_record_layout){
//
//            @Override
//            protected void convert(BaseAdapterHelper helper, CallRecord.Record.Expert item) {
//                helper.setText(R.id.name,item.getNickName())
//                        .setText(R.id.pingpai,item.getBrandInfo())
//                        .setText(R.id.jineng,item.getSkillInfo())
//                        .setImageUrl(R.id.head_avatar,item.getAvatar());
//
//            }
//        };
//
//        listView.withLoadMoreView();
//        listView.setAdapter(adapter);
//        //下拉刷新
//        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
//            @Override
//            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//                initData();
//                loadData();
//            }
//        });

//        // 加载更多
//        listView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
//            @Override
//            public void onLastItemVisible() {
//                loadData();
//            }
//        });
//        // 点击事件
//        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                UIHelper.showHouseDetailActivity(context);
//            }
//        });
//
//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
//                    Picasso.with(context).pauseTag(context);
//                } else {
//                    Picasso.with(context).resumeTag(context);
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//            }
//        });
    }

    private void loadData() {
//        getCallRecord();
    }


    /**
     * 语音呼叫
     */
    public void callServiceTel() {
//        if (Constant.USERROLE.equals("普通用户")) {
//            Toast.makeText(context, "请先去认证", Toast.LENGTH_SHORT).show();
//        } else {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + 40008 + 10066));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
//        }
    }
}
