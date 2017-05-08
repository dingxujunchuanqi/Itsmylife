package com.sinoautodiagnoseos.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.ui.UIHelper;
import com.sinoautodiagnoseos.ui.loginui.SwipeBackActivity;
import com.sinoautodiagnoseos.ui.personcenterui.FlowLayout;
import com.sinoautodiagnoseos.utils.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人中心界面
 * Created by 惊吓了时光 on 2017/5/2.
 */

public class PersonalCenterActivity extends SwipeBackActivity implements View.OnClickListener {

    private FlowLayout flow_layout;
    private List list;
    private ImageView image_user;
    private TextView user_name;
    private TextView user_grade;
    private RatingBar ratingbar;
    private FrameLayout image_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        initView();
        userInfo();
        initListenerOclick();
    }

    private void initListenerOclick() {
        image_back.setOnClickListener(this);
    }

    private void userInfo() {
        String userName = SharedPreferences.getInstance().getString("userName", "");
        String mobile = SharedPreferences.getInstance().getString("mobile", "");
        String starRating = SharedPreferences.getInstance().getString("starRating", "");
        String avatar = SharedPreferences.getInstance().getString("avatar", "");
        user_name.setText(userName);
        user_grade.setText(starRating);
        System.out.println(starRating);
        if (starRating.equals("")){
            starRating="0";
        }
        float star_num=Float.valueOf(starRating);
        ratingbar.setRating(star_num);
    }

    private void initView() {
        list = new ArrayList();
        list.add("专家1");
        list.add("专家2");
        list.add("专家3");
        list.add("专家4");
        flow_layout = (FlowLayout) findViewById(R.id.fl_keyword);
        image_user = (ImageView) findViewById(R.id.image_user);
        user_name = (TextView) findViewById(R.id.user_name);
        user_grade = (TextView) findViewById(R.id.user_grade);
        ratingbar = (RatingBar) findViewById(R.id.ratingbar);
        image_back = (FrameLayout) findViewById(R.id.back_click);
        flow_layout.setFlowLayout(list, new FlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(String content) {
                Toast.makeText(PersonalCenterActivity.this, content, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        UIHelper.showIm(PersonalCenterActivity.this);
    }
}
