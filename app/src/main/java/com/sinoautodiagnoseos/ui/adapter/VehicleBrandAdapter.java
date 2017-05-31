package com.sinoautodiagnoseos.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinoautodiagnoseos.R;

import java.util.ArrayList;
import java.util.List;

/** 车辆选择的Adapter
 * Created by dingxujun on 2017/5/21.
 */

public class VehicleBrandAdapter extends RecyclerView.Adapter<VehicleBrandAdapter.ItemViewHolder> {
    public static final int VIEW_TYPE_TITLE = 0;
    public static final int VIEW_TYPE_ITEM = 1;
    private final LayoutInflater layoutInflater;
    private Context context;
    List<String> list = new ArrayList();
    private onItemClickListener listener;

    public VehicleBrandAdapter(Context context, List<String> list) {
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

    /*
    * 创建viewholder的方法
    *
    * */
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext()//创建打气筒
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (viewType == VIEW_TYPE_TITLE) {
            View view = inflater.inflate(R.layout.item_title, parent, false);
            return new ItemViewHolder(view, VIEW_TYPE_TITLE);
        } else {
            View view = inflater.inflate(R.layout.item_data, parent, false);
            return new ItemViewHolder(view, VIEW_TYPE_ITEM);
        }
    }

    /*
    * 绑定viewholder的方法
    * */
    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {

        holder.bind(list.get(position), getItemViewType(position));
        /*
        * RecyclerView点击监听 加条目自定义回调
        * */
        holder.rlContentWrapper1.setOnClickListener(new View.OnClickListener() {
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

    //返回的条目的类型

    @Override
    public int getItemViewType(int position) {
        String item = list.get(position);
        if (item.equals("擅长品牌") || check(item)) {
            return VIEW_TYPE_TITLE;
        }
        return VIEW_TYPE_ITEM;
    }

    /*
    * 判断条目类型
    *
    * */
    public boolean check(String string) {
        char c = string.charAt(0);//返回字符串第一个索引的char类型
        if ((c >= 'A' && c <= 'Z') && string.length() == 1) {
            return true;
        } else {
            return false;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        private TextView mItem;
        RelativeLayout rlContentWrapper1;

        public ItemViewHolder(View itemView, int type) {
            super(itemView);
            if (type == VIEW_TYPE_TITLE) {
                mTitle = (TextView) itemView.findViewById(R.id.tvTitle);
                rlContentWrapper1 = (RelativeLayout) itemView.findViewById(R.id.rl_content_wrapper1);
            } else {
                mItem = (TextView) itemView.findViewById(R.id.tvItem);
                rlContentWrapper1 = (RelativeLayout) itemView.findViewById(R.id.rl_content_wrapper1);
            }

        }

        public void bind(String item, int type) {
            if (type == VIEW_TYPE_TITLE) {
                mTitle.setText(item);
            } else {
                mItem.setText(item);
            }
        }
    }
}

