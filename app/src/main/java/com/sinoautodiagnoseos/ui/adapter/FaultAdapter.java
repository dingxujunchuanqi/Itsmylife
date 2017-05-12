package com.sinoautodiagnoseos.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.FaulTranges.FaulTranges;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HQ_Demos on 2017/5/10.
 */

public class FaultAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private List<FaulTranges.Faul> faults;
//    View.OnClickListener onClickListener;
    int flag = 0;

    public FaultAdapter(Context context, List<FaulTranges.Faul> faults){
        this.context=context;
        this.faults=faults;
        inflater=LayoutInflater.from(context);
//        this.onClickListener=onClickListener;
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
        final FaultHolder finalHolder = holder;
        final List<String>car_txt_list=new ArrayList<>();
//        holder.carbrand_btn.setOnClickListener(onClickListener);
        holder.carbrand_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (flag){
                    case 0:
                        flag = 1;
                        car_txt_list.add(fault_txt);
                        System.out.println("选择了："+fault_txt);
                        finalHolder.carbrand_btn.setBackgroundResource(R.drawable.btn_selector);
                        System.out.println("剩余的："+car_txt_list);
                        break;
                    case 1:
                        car_txt_list.remove(fault_txt);
                        System.out.println("删除了："+fault_txt);
                        finalHolder.carbrand_btn.setBackgroundResource(R.drawable.btn_unselector);
                        flag = 0;
                        System.out.println("剩余的："+car_txt_list);
                        break;
                }
            }
        });
        return convertView;
    }

    static class FaultHolder {
        Button carbrand_btn;
    }
}
