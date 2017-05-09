package com.sinoautodiagnoseos.openvcall.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.openvcall.model.CallHistoriesExpertsDtos;
import com.sinoautodiagnoseos.propeller.ui.RatingBar;
import com.sinoautodiagnoseos.utils.PicassoUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sinoautodiagnoseos.R.id.rb;


/**
 * Created by HQ_Demos on 2017/2/20.
 */
public class ListViewAdapter extends BaseAdapter {
    View[] itemViews;
    Context context;
    private LayoutInflater inflater = null;
    private List<CallHistoriesExpertsDtos> list;
    private RatingClickListener ratingClickListener;

    public ListViewAdapter(List<CallHistoriesExpertsDtos> list, Context context) {
        Log.e("Adapter",list.size()+"");
        this.context=context;
        inflater = LayoutInflater.from(context);
        this.list=list;
    }

    public interface RatingClickListener{
        void OnRatingClickListener(float ratingCount);
    }

    public void setRatingClickListener(RatingClickListener ratingClickListener){
            this.ratingClickListener=ratingClickListener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
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
            view=inflater.inflate(R.layout.item_score_layout,null);
            holder.avatar= (CircleImageView) view.findViewById(R.id.avatar);
            holder.name= (TextView) view.findViewById(R.id.user_name);
            holder.rb= (RatingBar) view.findViewById(rb);
            holder.rb.setClickable(true);//<==设置可否点击
            holder.rb.setStar(0);
            holder.rb.setStepSize(RatingBar.StepSize.Full);//设置每次点击增加一颗星
            view.setTag(holder);
        }else {
            holder= (ViewsHolder) view.getTag();
        }
        holder.rb.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float ratingCount) {
                Log.e("RatingBar","Counts="+ratingCount);
                list.get(i).setStarRating((int) ratingCount);
//                if (ratingClickListener!=null){
//                    ratingClickListener.OnRatingClickListener(ratingCount);
//                }
            }
        });
        PicassoUtils.loadImageView(context,list.get(i).getAvatar(),holder.avatar);
        holder.name.setText(list.get(i).getName());
        return view;
    }

    public static class ViewsHolder {
        CircleImageView avatar;
        TextView name;
        RatingBar rb;
    }

}
