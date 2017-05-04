package com.sinoautodiagnoseos.ui.adapter;

import android.content.Context;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.CallRecord.Experts_datas;

import java.util.List;

/**
 * Created by HQ_Demos on 2017/5/4.
 */

public class DiagnoseAdapter extends CommonAdapter<Experts_datas>{
    public DiagnoseAdapter(Context context, List<Experts_datas> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, int position) {
        holder.setText(R.id.name,mData.get(position).getNickName());
        holder.setText(R.id.pingpai,mData.get(position).getBrandInfo());
        holder.setText(R.id.jineng,mData.get(position).getSkillInfo());
        holder.setImageResource(R.id.video_play,R.drawable.video_play);
        holder.setImageUrl(R.id.head_avatar,mData.get(position).getAvatar());
    }
}
