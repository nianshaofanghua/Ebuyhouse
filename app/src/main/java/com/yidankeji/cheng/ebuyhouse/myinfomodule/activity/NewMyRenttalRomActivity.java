package com.yidankeji.cheng.ebuyhouse.myinfomodule.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.adapter.ViewPagerFragmentAdapter;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

import java.util.ArrayList;

public class NewMyRenttalRomActivity extends FragmentActivity implements View.OnClickListener {
    private TextView right;

    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private TabLayout tableLayout;
    private static ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_my_renttal_rom);
        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.myrentalroom_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("My Listing");
        right = (TextView) findViewById(R.id.action_bar_right);
        right.setText("Edit");
        right.setOnClickListener(this);
        right.setTextColor(getResources().getColor(R.color.text_heise));
        /**/


        tableLayout = (TabLayout) findViewById(R.id.savelist_tablayout);
        viewPager = (ViewPager) findViewById(R.id.savelist_viewpager);
        initData();
    }

    private void initData() {
        for (int i = 0; i < 5; i++) {
            MyRentRoomFragment fragment = new MyRentRoomFragment();
            fragmentList.add(fragment);
        }
        ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);

        tableLayout.setupWithViewPager(viewPager);

        tableLayout.getTabAt(0).setText("Active listings").setTag("");
        tableLayout.getTabAt(1).setText("Pending").setTag("");
        tableLayout.getTabAt(2).setText("Sold out").setTag("");
        tableLayout.getTabAt(3).setText("Bought").setTag("");
        tableLayout.getTabAt(4).setText("Sold").setTag("");

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0 || position == 2) {
                    right.setVisibility(View.VISIBLE);
                } else {
                    right.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_bar_back:
                finish();
                break;
            case R.id.action_bar_right:
                MyRentRoomFragment fragment = (MyRentRoomFragment) fragmentList.get(viewPager.getCurrentItem());
                fragment.getEditSatate(right);
                break;
            default:
                break;
        }
    }

    public static int getTabLayoutTAG() {
        int currentItem = viewPager.getCurrentItem();

        return currentItem;
    }
}
