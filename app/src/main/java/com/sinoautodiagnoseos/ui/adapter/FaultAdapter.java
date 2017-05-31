package com.sinoautodiagnoseos.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.FaulTranges.FaulTranges;

import java.util.List;

/**
 * Created by HQ_Demos on 2017/5/10.
 */

public class FaultAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private List<FaulTranges.Faul> faults;

    final int itemLength = 16;
    private int clickTemp = -1;//标识被选择的item
    private int[] clickedList=new int[itemLength];//这个数组用来存放item的点击状态


    public FaultAdapter(Context context, List<FaulTranges.Faul> faults){
        this.context=context;
        this.faults=faults;
        inflater=LayoutInflater.from(context);
        for (int i =0;i<itemLength;i++){
            clickedList[i]=0;      //初始化item点击状态的数组
        }

    }

    @Override
    public int getCount() {
        return faults.size();
    }

    @Override
    public Object getItem(int position) {
        return faults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FaultHolder holder =null;
        if (convertView==null){
            convertView=inflater.inflate(R.layout.item_fault_layout,null);
            holder=new FaultHolder();
            holder.carbrand_btn= (Button) convertView.findViewById(R.id.fault_btn);
            convertView.setTag(holder);
        }else
        {
            holder= (FaultHolder) convertView.getTag();
        }
        final String fault_txt=faults.get(position).getText();
        final String fault_value=faults.get(position).getValue();
        holder.carbrand_btn.setText(fault_txt);
        if(clickTemp==position){    //根据点击的Item当前状态设置背景
            if (clickedList[position]==0){
                holder.carbrand_btn.setBackgroundResource(R.drawable.btn_selector);
                holder.carbrand_btn.setTextColor(context.getResources().getColor(R.color.btn_blue_normal));
                clickedList[position]=1;
                faults.get(position).setSelect(true);
            }
            else {
                holder.carbrand_btn.setBackgroundResource(R.drawable.btn_unselector);
                holder.carbrand_btn.setTextColor(context.getResources().getColor(R.color.black));
                clickedList[position]=0;
                faults.get(position).setSelect(false);
            }
        }

        return convertView;
    }

    public void setSelection(int selection) {
        this.clickTemp = selection;
    }

    static class FaultHolder {
        Button carbrand_btn;
    }
}
