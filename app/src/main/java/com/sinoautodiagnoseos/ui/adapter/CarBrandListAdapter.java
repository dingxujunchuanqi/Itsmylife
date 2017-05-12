package com.sinoautodiagnoseos.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.CarBrands.CarInfo;
import com.sinoautodiagnoseos.utils.CommUtil;

import java.util.List;

/**
 * Created by HQ_Demos on 2017/5/11.
 */

public class CarBrandListAdapter extends BaseAdapter {
    private Context mContext;
    private List<CarInfo> brandsList;
    private LayoutInflater inflater;

    public CarBrandListAdapter(Context context,List<CarInfo> brandsList){
        super();
        this.mContext=context;
        this.brandsList=brandsList;
        this.inflater=LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return brandsList.size();
    }

    @Override
    public Object getItem(int position) {
        return brandsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        CarInfo carInfo=brandsList.get(position);
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_carbrand,null);
            viewHolder.headerTv= (TextView) convertView.findViewById(R.id.tv_item_citys_header);
            viewHolder.contentTv = (TextView) convertView.findViewById(R.id.tv_item_citys_context);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        // 如果当前位置为第一次出现该类首字母的位置,则显示headerTv
        if (position == CommUtil.getPositionForSection(position, brandsList)) {
            viewHolder.contentTv.setVisibility(View.VISIBLE);
            viewHolder.headerTv.setVisibility(View.VISIBLE);
            viewHolder.headerTv.setText(carInfo.getLetterName());
        } else {
            viewHolder.headerTv.setVisibility(View.GONE);
            viewHolder.contentTv.setVisibility(View.VISIBLE);
        }
        viewHolder.contentTv.setText(carInfo.getCarName());
//            viewHolder.tab_tag.setText(brandsList.get(position).getCarHeader());
        return convertView;
    }

    static class ViewHolder{
        TextView headerTv;
        TextView contentTv;
    }
}
