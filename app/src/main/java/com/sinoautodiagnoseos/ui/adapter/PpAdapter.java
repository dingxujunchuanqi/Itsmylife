package com.sinoautodiagnoseos.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.Brand.BrandResult;

import java.util.List;

/**
 * Created by HQ_Demos on 2017/5/31.
 */

public class PpAdapter extends BaseAdapter{
    private List<BrandResult> list;
    private Context context;
    private LayoutInflater inflater;
    private int clickTemp = -1;
    public PpAdapter(Context context, List<BrandResult> brand_list) {
        this.context=context;
        this.list=brand_list;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BrandHolder holder =null;
        if (convertView==null){
            convertView=inflater.inflate(R.layout.item_search_brand,null);
            holder=new BrandHolder();
            holder.brand_btn= (Button) convertView.findViewById(R.id.search_brand);
            convertView.setTag(holder);
        }else
        {
            holder= (BrandHolder) convertView.getTag();
        }
        if (clickTemp==position){
            holder.brand_btn.setBackgroundResource(R.drawable.yuanjiao_btn_selector);
            holder.brand_btn.setTextColor(context.getResources().getColor(R.color.white));
        }else {
            holder.brand_btn.setBackgroundResource(R.drawable.yuanjiao_btn_unselector);
            holder.brand_btn.setTextColor(context.getResources().getColor(R.color.black));
        }
        Log.e("brand_btn",list.get(position).getBrandName());
        holder.brand_btn.setText(list.get(position).getBrandName());
        return convertView;
    }

    public void setSeclection(int position) {
        clickTemp = position;
    }

    class BrandHolder{
        Button brand_btn;
    }
}
