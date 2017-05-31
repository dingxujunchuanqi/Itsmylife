package com.sinoautodiagnoseos.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.FaulTranges.MyGoodRrange;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

/**
 * 技能选择后的adapter
 * Created by dingxujun on 2017/5/25.
 */

public class MyrangeAapter extends RecyclerView.Adapter<MyrangeAapter.MyViewHolder> {
    private Context context;
    public List<MyGoodRrange> list = new ArrayList();
    private LayoutInflater layoutInflater;
    private onItemClickListener listener;

    public MyrangeAapter(Context context, List<MyGoodRrange> list) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }

    /*
    * 接口回调条目监听方法
    * */
    public interface onItemClickListener {
        void itemClick(int position);
    }

    /*
    * 使用出需要设置的方法
    * */
    public void setListener(onItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_range, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        System.out.println("我是bindview33333" + list.toString());
        System.out.println("----我是bindview上-----");
        System.out.println(list.get(position).name);
        holder.trademark.setText(list.get(position).name);
        holder.delet.setText("删除");
        RangeGridviewAdapter adapter = new RangeGridviewAdapter(context, list.get(position).list);
        holder.qwgridview.setAdapter(adapter);
        holder.qwgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));//去除条目选中的颜色，xml里面设置不知道为何不起作用
        System.out.println("----我是bindview下-----");
        holder.delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView trademark, delet;
        private GridView qwgridview;

        public MyViewHolder(View itemView) {
            super(itemView);
            trademark = (TextView) itemView.findViewById(R.id.trademark);
            delet = (TextView) itemView.findViewById(R.id.delete);
            qwgridview = (GridView) itemView.findViewById(R.id.qwgridview);

        }
    }
}