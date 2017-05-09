package com.sinoautodiagnoseos.openvcall.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.openvcall.model.ExpertsData;
import com.sinoautodiagnoseos.utils.PicassoUtils;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by HQ_Demos on 2017/2/22.
 */
public class RequestExpertsAdapter extends BaseAdapter {

    private List<ExpertsData> data;
    private Context context;
    // 用来导入布局 
    private LayoutInflater inflater = null;
    private Map<Integer,Boolean> isSelected;
    private List beSelectedData;

    public RequestExpertsAdapter(Context context, List<ExpertsData> dbData, Map<Integer, Boolean> isSelected, List beSelectedData) {
        this.context=context;
        this.data=dbData;
        this.isSelected=isSelected;
        this.beSelectedData=beSelectedData;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewsHolder holder=null;
        if (view==null){
            holder=new ViewsHolder();
            view=inflater.inflate(R.layout.item_experts_list,null);
            holder.image= (CircleImageView) view.findViewById(R.id.experts_avatar);
            holder.tv= (TextView) view.findViewById(R.id.experts_name);
            holder.cb= (CheckBox) view.findViewById(R.id.checked_btn);
            view.setTag(holder);
        }else {
            holder= (ViewsHolder) view.getTag();
        }
        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 当前点击的checkBox
                boolean checkbox = !isSelected.get(i);
                //  先将所有值设为false
                for (Integer p:isSelected.keySet()){
                    isSelected.put(p,false);
                }
                //  再记录当前选择的checkbox实际状态
                isSelected.put(i,checkbox);
                Log.e("!!!!!!!!!",isSelected+"\n"+i+"\n"+checkbox);
                notifyDataSetChanged();
                beSelectedData.clear();
                if (checkbox) beSelectedData.add(data.get(i));
            }
        });
        String url = data.get(i).getAvatorUrl();
        PicassoUtils.loadImageView(context,url,holder.image);
        holder.tv.setText(data.get(i).getName());
        holder.cb.setChecked(isSelected.get(i));
        return view ;
    }

    public static class ViewsHolder {
        CircleImageView image;
        TextView tv;
        CheckBox cb;
      }
}
