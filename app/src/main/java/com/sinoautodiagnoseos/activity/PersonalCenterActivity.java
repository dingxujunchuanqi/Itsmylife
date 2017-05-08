package com.sinoautodiagnoseos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.app.AppContext;
import com.sinoautodiagnoseos.entity.User.UserInfo;
import com.sinoautodiagnoseos.ui.UIHelper;
import com.sinoautodiagnoseos.ui.loginui.SwipeBackActivity;
import com.sinoautodiagnoseos.ui.personcenterui.FlowLayout;
import com.sinoautodiagnoseos.utils.LogUtils;
import com.sinoautodiagnoseos.utils.PicassoUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.data;


/**
 * 个人中心界面
 * Created by dingxujun on 2017/5/2.
 */

public class PersonalCenterActivity extends SwipeBackActivity implements View.OnClickListener {

    private static  String USER_DATA ="user";
    private FlowLayout flow_layout;
    private List list;
    private CircleImageView image_user;
    private TextView user_name, user_grade;
    private RatingBar ratingbar;
    private FrameLayout image_back;
    private TextView mobile_number, car_shop, person_tv;
    private ImageView setting_image;
    private String name, starRating, mobile, avatar;
    private String stationName;
    private RelativeLayout rl_goin;
    private String roleName;
    private ImageView right_arrow;
    private ImageView right_arrow1;
    private UserInfo userData;

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
        rl_goin.setOnClickListener(this);
    }

    private void userInfo() {
        userData = AppContext.userInfo;
        System.out.println(userData.getData().getRoleName()+"--------------");
        if (userData != null) {
            if (userData.getData() != null) {
                mobile = userData.getData().getMobile();
                stationName = userData.getData().getStationName();
                roleName = userData.getData().getRoleName();
                avatar = userData.getData().getAvatar();
                name = userData.getData().getName();
                if (userData.getData().getOtherInfo() != null) {
                    starRating = userData.getData().getOtherInfo().getStarRating();
                }
            }
        }

        if (mobile != null && roleName.equals("普通用户")) {
            mobile_number.setText(mobile);
            flow_layout.setVisibility(View.GONE);
            ratingbar.setVisibility(View.GONE);
            user_grade.setVisibility(View.GONE);
        } else {
            shoWexperTauthority();
        }
//            String userName = SharedPreferences.getInstance().getString("userName", "");
//            String mobile = SharedPreferences.getInstance().getString("mobile", "");
//            String starRating = SharedPreferences.getInstance().getString("starRating", "");
//            String avatar = SharedPreferences.getInstance().getString("avatar", "");
//            String stationName = SharedPreferences.getInstance().getString("stationName", "");
        setting_image.setImageResource(R.drawable.setting);
        person_tv.setText(R.string.personal_centter);

    }

    private void shoWexperTauthority() {
        if (name != null) {
            user_name.setText(name);
        }
        if (mobile != null) {
            mobile_number.setText(mobile);
        }
        if (stationName != null) {
            car_shop.setText(stationName);
            car_shop.setVisibility(View.VISIBLE);
        }

        if (avatar != null && !TextUtils.isEmpty(avatar)) {
            PicassoUtils.loadImageView(this, avatar, image_user);
        }
        if (starRating != null && !TextUtils.isEmpty(starRating)) {

            ratingbar.setRating(Float.parseFloat(starRating));
            user_grade.setText(starRating);
        }
        right_arrow.setVisibility(View.VISIBLE);
        right_arrow1.setVisibility(View.GONE);
    }

    private void initView() {
        list = new ArrayList();
        list.add("专家1");
        list.add("专家2");
        list.add("专家3");
        list.add("专家4");
        flow_layout = (FlowLayout) findViewById(R.id.fl_keyword);
        image_user = (CircleImageView) findViewById(R.id.image_user);
//        Picasso.with(this).load(url);
//                .placeholder(R.drawable.default_image)
//                .error(R.drawable.default_image)
//                .tag(this)
//                .into(image_user);
        user_name = (TextView) findViewById(R.id.user_name);
        user_grade = (TextView) findViewById(R.id.user_grade);
        ratingbar = (RatingBar) findViewById(R.id.ratingbar);
        image_back = (FrameLayout) findViewById(R.id.back_click);
        mobile_number = (TextView) findViewById(R.id.mobile_number);
        car_shop = (TextView) findViewById(R.id.car_shop);
        person_tv = (TextView) findViewById(R.id.person_tv);
        setting_image = (ImageView) findViewById(R.id.setting_image);
        rl_goin = (RelativeLayout) findViewById(R.id.re_layout);
        right_arrow = (ImageView) findViewById(R.id.arrow_right);
        right_arrow1 = (ImageView) findViewById(R.id.arrow_right1);
        flow_layout.setFlowLayout(list, new FlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(String content) {
                Toast.makeText(PersonalCenterActivity.this, content, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_click:
                UIHelper.showIm(this);
                break;
            case R.id.re_layout:
                UIHelper.showPersonInfo(this,userData);
                break;
            default:
                break;
        }
    }
}

