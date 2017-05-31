package com.sinoautodiagnoseos.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sinoautodiagnoseos.R;

import java.util.List;


/**
 * 技能管理选择后的gridviewadapter
 * Created by dingxujun on 2017/5/25.
 */

public class RangeGridviewAdapter extends BaseAdapter {
    private List<String> list;
    private Context context;
    private LayoutInflater inflater;

    public RangeGridviewAdapter(Context context, List<String> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyGridHold holder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.range_gridview_item, null);
            holder = new MyGridHold();
            holder.textView = (TextView) view.findViewById(R.id.range_gridview);
            view.setTag(holder);
        } else {
            holder = (MyGridHold) view.getTag();
        }
        holder.textView.setText(list.get(i));
        holder.textView.setTextColor(context.getResources().getColor(R.color.button_blue));//给字体设置颜色
        return view;
    }

    /*
    * 我的Viewholder
    * */
    public static class MyGridHold {
        private TextView textView;
    }

}
