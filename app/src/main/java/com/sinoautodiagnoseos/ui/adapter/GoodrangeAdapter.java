package com.sinoautodiagnoseos.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.FaulTranges.FaulTranges;

import java.util.ArrayList;
import java.util.List;

/**
 * 擅长范围的adapter
 * Created by dingxujun on 2017/5/23.
 */

public class GoodrangeAdapter extends RecyclerView.Adapter<GoodrangeAdapter.MyViewHodler> {
    /**
     * 创建一个构造方法 让对方传递我们需要的数据
     *
     * @author dingxujun
     * created at 2017/5/23 17:08
     */
    private List<FaulTranges.Faul> list;
    private LayoutInflater layoutInflater;
    private MyViewHodler.ClickListener clickListener;
    private SparseBooleanArray selectedItems;
    private  Context context;

    public GoodrangeAdapter(Context context, List<FaulTranges.Faul> list, MyViewHodler.ClickListener clickListener) {
        this.list = list;
        layoutInflater = layoutInflater.from(context);//填充器
        this.clickListener = clickListener;
        this.selectedItems = new SparseBooleanArray();
        this.context=context;

    }
    /**
    *判断是否被选中
    *@author dingxujun
    *created at 2017/5/25 16:58
    */
    public boolean isSelected(int position) {
        return getSelectedItems().contains(position);
    }

    public void switchSelectedState(int position) {
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }
/*
* 清空选中的个数
* */
    public void clearSelectedState() {
        List<Integer> selection = getSelectedItems();
        selectedItems.clear();
        for (Integer i : selection) {
            notifyItemChanged(i);
        }
    }
/*
* 获取选中的个数
* */
    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); ++i) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }


    /*
    * 创建一个viewHolder
    * */
    @Override
    public MyViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.goodrang_item, parent, false);
        return new MyViewHodler(view, clickListener);
    }

    /**
     * 绑定一个viewholder
     *
     * @author dingxujun
     * created at 2017/5/23 17:14
     */
    @Override
    public void onBindViewHolder(MyViewHodler holder, final int position) {
        holder.good_tvitem.setText(list.get(position).getText());
        ((MyViewHodler) holder).good_tvitem.setBackgroundResource(isSelected(position) ? R.drawable.petname_shape2 :
                R.color.transparent);
        ((MyViewHodler) holder).good_tvitem.setTextColor(isSelected(position) ?
                context.getResources().getColor(R.color.button_blue):context.getResources().getColor(R.color.text_apha50));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /*
    * 创建自己的Viewholder
    * */
    public static class MyViewHodler extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private TextView good_tvitem;
        private RelativeLayout rl_contentwrapper1;
        private ClickListener listener;

        public MyViewHodler(View itemView,ClickListener listener) {
            super(itemView);
            this.listener = listener;
            good_tvitem = (TextView) itemView.findViewById(R.id.good_tvItem);
            rl_contentwrapper1 = (RelativeLayout) itemView.findViewById(R.id.rl_content_wrapper1);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.onItemClicked(getPosition());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            return listener != null && listener.onItemLongClicked(getPosition());
        }

        public interface ClickListener {  //条目点击回调监听
            void onItemClicked(int position);

            boolean onItemLongClicked(int position);//条目长按回调监听
        }
    }
}

