package com.sinoautodiagnoseos.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.CallRecord.CallRecord;
import com.sinoautodiagnoseos.entity.Experts.SearchExpertsData;
import com.sinoautodiagnoseos.net.requestApi.HttpRequestApi;
import com.sinoautodiagnoseos.net.requestSubscribers.HttpSubscriber;
import com.sinoautodiagnoseos.net.requestSubscribers.SubscriberOnListener;
import com.sinoautodiagnoseos.openvcall.ui.DirectWaitingActivity;
import com.sinoautodiagnoseos.utils.Constant;
import com.sinoautodiagnoseos.utils.OnMultiClickListener;
import com.sinoautodiagnoseos.utils.SharedPreferences;
import com.sinoautodiagnoseos.utils.ToastUtils;

import java.util.List;

/**
 * Created by HQ_Demos on 2017/5/4.
 */

public class DiagnoseAdapter extends CommonAdapter<CallRecord.Record>{
    public DiagnoseAdapter(Context context, List<CallRecord.Record> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, final int position) {
        String data_time = mData.get(position).getBeginOnUtc();
        String str_time=data_time.substring(0,data_time.indexOf("T"));
        holder.setText(R.id.record_time,str_time);
//        holder.setText(R.id.name,mData.get(position).getNickName());
//        holder.setText(R.id.pingpai,mData.get(position).getBrandInfo());
//        holder.setText(R.id.jineng,mData.get(position).getSkillInfo());
//        holder.setImageResource(R.id.video_play,R.drawable.video_play);
//        holder.setImageUrl(R.id.head_avatar,mData.get(position).getAvatar());

        //处理局部点击事件
        holder.getView(R.id.video_play).setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
//                String expertsId = mData.get(position).getExperts_id();
//                String roomId = "";
//                directCallProfessor(expertsId,roomId);
            }
        });
    }

    /**
     * 直通专家
     *
     * @param expertsId
     */
    public void directCallProfessor(final String expertsId,final String roomId) {
        System.out.println("专家ID" + expertsId);
        Constant.TOKEN= SharedPreferences.getInstance().getString("checktoken","");
        Constant.REGISTRATION=SharedPreferences.getInstance().getString("RegistrationId","");
        HttpRequestApi.getInstance().getDirectexpert(expertsId,roomId, new HttpSubscriber<SearchExpertsData>(new SubscriberOnListener<SearchExpertsData>() {
            @Override
            public void onSucceed(SearchExpertsData directexpert) {
                Constant.ISFROM = "0";
                Constant.ROOMID = directexpert.getData().getRoomId();
                System.out.println(" Constant.ROOMID=" + Constant.ROOMID);
                if (directexpert.getData().getRoomId().equals("2") || directexpert.getData().getRoomId().equals("3")) {
                    ToastUtils.makeShortText("该专家正在问诊中",mContext);
                } else {
                    Constant.ROOMID = directexpert.getData().getRoomId();
                    Intent intent = new Intent(mContext, DirectWaitingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            }

            @Override
            public void onError(int code, String msg) {
            }
        }, mContext));
    }
}
