package com.sinoautodiagnoseos.openvcall.ui;

import android.content.Context;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.openvcall.model.ExpertsData;
import com.sinoautodiagnoseos.ui.adapter.CommonAdapter;

import java.util.List;

/**
 * Created by HQ_Demos on 2017/2/22.
 */
public class SearchAdapter extends CommonAdapter<ExpertsData> {
    public SearchAdapter(Context context, List<ExpertsData> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(com.sinoautodiagnoseos.ui.adapter.ViewHolder holder, int position) {
        holder
//                .setImageResource(R.id.experts_avatar,mData.get(position).getExperts_avatar())
                .setText(R.id.experts_name,mData.get(position).getName());
    }

}
