package com.sinoautodiagnoseos.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.Fault.FaultResult;

import java.util.List;

/**
 * Created by HQ_Demos on 2017/5/31.
 */

public class GzAdapter extends BaseAdapter{
    private List<FaultResult>list;
    private Context context;
    private LayoutInflater inflater;
    private int clickTemp = -1;
    public GzAdapter(Context context, List<FaultResult> fault_list) {
        this.list=fault_list;
        this.context=context;
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
        FaultHolder holder =null;
        if (convertView==null){
            convertView=inflater.inflate(R.layout.item_search_fault,null);
            holder=new FaultHolder();
            holder.fault_btn= (Button) convertView.findViewById(R.id.search_fault);
            convertView.setTag(holder);
        }else
        {
            holder= (FaultHolder) convertView.getTag();
        }
        if (clickTemp==position){
            holder.fault_btn.setBackgroundResource(R.drawable.yuanjiao_btn_selector);
            holder.fault_btn.setTextColor(context.getResources().getColor(R.color.white));
        }else {
            holder.fault_btn.setBackgroundResource(R.drawable.yuanjiao_btn_unselector);
            holder.fault_btn.setTextColor(context.getResources().getColor(R.color.black));
        }
        Log.e("fault_btn",list.get(position).getText());
        holder.fault_btn.setText(list.get(position).getText());
        return convertView;
    }

    public void setSeclection(int position) {
        clickTemp = position;
    }

    class FaultHolder{
        Button fault_btn;
    }
}
