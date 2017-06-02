package com.sinoautodiagnoseos.fragment;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sinoautodiagnoseos.R;
import com.sinoautodiagnoseos.activity.MainActivity;

import java.lang.reflect.Field;

/**
 * 智库
 * Created by HQ_Demos on 2017/4/26.
 */

public class StudyFragment extends Fragment{
    private String[] tabTitle = {"维修案例","技术通报","汽车资料"};

    private TabLayout mTabLayout;
    private ViewPager pager;
    public MainActivity activity;
    private ServiceFragment serviceFragment;
    private CarInfoFragment carInfoFragment;

    public void setThis(MainActivity activity){
        this.activity=activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_study_pager,container,false);
        initViews(view);
        initData();
        serviceFragment = new ServiceFragment();
        carInfoFragment = new CarInfoFragment();
        carInfoFragment.setThis(activity);
        serviceFragment.setThis(activity);
        return view;
    }

    private void initViews(View view){
        pager = (ViewPager) view.findViewById(R.id.mViewPager1);
        mTabLayout = (TabLayout) view.findViewById(R.id.mTabLayout);
    }

    @Override
    public void onStart() {
        super.onStart();
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(mTabLayout,30,30);
            }
        });
    }

    private void initData() {
        for (int i=0; i<tabTitle.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(tabTitle[i]));
        }
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#ff33b5e5"));
        mTabLayout.setTabTextColors(Color.GRAY,Color.parseColor("#000000"));
        mTabLayout.setSelectedTabIndicatorHeight(10);

        pager.setAdapter(new MyFragmentStatePagerAdapter(getChildFragmentManager(),tabTitle));
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println("------我是位置条目----------"+tab.getPosition());
                System.out.println("------我是位置----------"+pager);
                if (pager!=null) {
                    pager.setCurrentItem(tab.getPosition());
                }
                System.out.println("------我是位置下----------"+pager);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter{
        private String[] tabTilte;

        public MyFragmentStatePagerAdapter(FragmentManager fm,String[] tabTitle) {
            super(fm);
            this.tabTilte = tabTitle;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    System.out.println("----ServiceFragment----"+position);
                    return serviceFragment;
                case 1:
                    System.out.println("----TechnicalFragment----"+position);
                    return new TechnicalFragment();
                case 2:
                    System.out.println("----CarInfoFragment----"+position);
                    return carInfoFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabTilte.length;
        }
    }

    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }


    }
}
