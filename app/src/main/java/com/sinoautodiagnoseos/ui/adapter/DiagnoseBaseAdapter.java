package com.sinoautodiagnoseos.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.entity.CallRecord.CallRecord;
import com.sinoautodiagnoseos.entity.Experts.SearchExpertsData;
import com.sinoautodiagnoseos.net.requestApi.HttpRequestApi;
import com.sinoautodiagnoseos.net.requestSubscribers.HttpSubscriber;
import com.sinoautodiagnoseos.net.requestSubscribers.SubscriberOnListener;
import com.sinoautodiagnoseos.openvcall.ui.DirectWaitingActivity;
import com.sinoautodiagnoseos.utils.Constant;
import com.sinoautodiagnoseos.utils.OnMultiClickListener;
import com.sinoautodiagnoseos.utils.PicassoUtils;
import com.sinoautodiagnoseos.utils.SharedPreferences;
import com.sinoautodiagnoseos.utils.ToastUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by HQ_Demos on 2017/5/9.
 */

public class DiagnoseBaseAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<CallRecord.Record> callRecord;
    private List<CallRecord.Record.Expert> experts;

    public DiagnoseBaseAdapter(Context context, List<CallRecord.Record> callRecord, List<CallRecord.Record.Expert> experts) {
        this.context=context;
        this.callRecord=callRecord;
        this.experts=experts;
    }

    //获取组元素数目   
    @Override
    public int getGroupCount() {
        return callRecord.size();
    }

    //获取子元素数目 
    @Override
    public int getChildrenCount(int groupPosition) {
        return callRecord.get(groupPosition).getExpertList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return callRecord.get(groupPosition);
    }

    //获取子元素对象
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //获取子元素Id  
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //加载并显示组元素 
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder =null;
        View groupView=null;
        if (convertView!=null){
            groupView=convertView;
            groupHolder= (GroupHolder) groupView.getTag();
        }else {
            groupView=View.inflate(context, R.layout.item_group,null);
            groupHolder=new GroupHolder();
            groupHolder.time= (TextView) groupView.findViewById(R.id.record_time);
            groupView.setTag(groupHolder);
        }
        String date_time=callRecord.get(groupPosition).getBeginOnUtc();
        String str_time= date_time.substring(0,date_time.indexOf("T"));
        groupHolder.time.setText(str_time);
        return groupView;
    }

    //加载子元素并显示
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View childView=null;
        ChildHolder childHolder =null;
        if (convertView!=null){
            childView=convertView;
            childHolder= (ChildHolder) childView.getTag();
        }else {
            childView=View.inflate(context,R.layout.item_child,null);
            childHolder=new ChildHolder();
            childHolder.name= (TextView) childView.findViewById(R.id.name);
            childHolder.pingpai= (TextView) childView.findViewById(R.id.pingpai);
            childHolder.jineng= (TextView) childView.findViewById(R.id.jineng);
            childHolder.avatar= (CircleImageView) childView.findViewById(R.id.head_avatar);
            childHolder.video_play= (ImageView) childView.findViewById(R.id.video_play);
            childView.setTag(childHolder);
        }
        String nick_name=callRecord.get(groupPosition).getExpertList().get(childPosition).getNickName();
        childHolder.name.setText(nick_name);
        String pingpai=callRecord.get(groupPosition).getExpertList().get(childPosition).getBrandInfo();
        childHolder.pingpai.setText(pingpai);
        String jineng=callRecord.get(groupPosition).getExpertList().get(childPosition).getSkillInfo();
        childHolder.jineng.setText(jineng);
        childHolder.video_play.setImageResource(R.drawable.video_play);
        String avatar_url=callRecord.get(groupPosition).getExpertList().get(childPosition).getAvatar();
        PicassoUtils.loadImageView(context,avatar_url,childHolder.avatar);
        final String expertsId = callRecord.get(groupPosition).getExpertList().get(childPosition).getExpertId();
        final String roomId="";
        childHolder.video_play.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                directCallProfessor(expertsId,roomId);
            }
        });
        return childView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    static class GroupHolder {
        TextView time;
    }

    static class ChildHolder {
        TextView name;
        TextView pingpai;
        TextView jineng;
        CircleImageView avatar;
        ImageView video_play;
    }

    /**
     * 直通专家
     *
     * @param expertsId
     */
    public void directCallProfessor(final String expertsId,final String roomId) {
        System.out.println("专家ID" + expertsId);
        Constant.TOKEN= SharedPreferences.getInstance().getString("checktoken","");
        Constant.REGISTRATION=SharedPreferences.getInstance().getString("RegistrationId","");
        HttpRequestApi.getInstance().getDirectexpert(expertsId,roomId, new HttpSubscriber<SearchExpertsData>(new SubscriberOnListener<SearchExpertsData>() {
            @Override
            public void onSucceed(SearchExpertsData directexpert) {
                Constant.ISFROM = "0";
                Constant.ROOMID = directexpert.getData().getRoomId();
                System.out.println(" Constant.ROOMID=" + Constant.ROOMID);
                if (directexpert.getData().getRoomId().equals("2") || directexpert.getData().getRoomId().equals("3")) {
                    ToastUtils.makeShortText("该专家正在问诊中",context);
                } else {
                    Constant.ROOMID = directexpert.getData().getRoomId();
                    Intent intent = new Intent(context, DirectWaitingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onError(int code, String msg) {
            }
        }, context));
    }
}
