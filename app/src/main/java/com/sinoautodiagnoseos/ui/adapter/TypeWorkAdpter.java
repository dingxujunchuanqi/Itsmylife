package com.sinoautodiagnoseos.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.User.Skill;

import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.sinoautodiagnoseos.R.id.view;

/**
 *  选择工种的Adapter
 * Created by dingxujun on 2017/5/18.
 */

public class TypeWorkAdpter extends BaseAdapter {
    private Context context;
    private  List<Skill.DataBean> list;
    private final LayoutInflater inflater;

    public TypeWorkAdpter(Context context, List<Skill.DataBean>list) {
        this.context=context;
        this.list=list;
        inflater = LayoutInflater.from(context);
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        TypeWkViewHolder typeWkViewHolder =null;
        if (convertView==null){
            convertView= inflater.inflate(R.layout.typewk_gridview_item, null);
            typeWkViewHolder=new TypeWkViewHolder();
            typeWkViewHolder.type_wktv= (TextView) convertView.findViewById(R.id.type_wktv);
            convertView.setTag(typeWkViewHolder);
        }else {
            typeWkViewHolder = (TypeWkViewHolder) convertView.getTag();
        }
        String typewk = list.get(position).getText();
        typeWkViewHolder.type_wktv.setText(typewk);
        return convertView;
    }
 /**
 *条目的viewholder
 *@author dingxujun
 *created at 2017/5/18 17:04
 */
    public static class TypeWkViewHolder{
    private TextView type_wktv;
 }
}
