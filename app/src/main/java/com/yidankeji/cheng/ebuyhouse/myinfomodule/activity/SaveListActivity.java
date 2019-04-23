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

public class SaveListActivity extends FragmentActivity implements View.OnClickListener{

    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private TabLayout tableLayout;
    private static ViewPager viewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_list);

        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.savelist_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("Save List");

        tableLayout = (TabLayout) findViewById(R.id.savelist_tablayout);
        viewPager = (ViewPager) findViewById(R.id.savelist_viewpager);
        initData();
    }

    private void initData() {
        for (int i = 0; i < 2; i++) {
            CollectListFragment fragment = new CollectListFragment();
            fragmentList.add(fragment);
        }
CollectListFragment fragment01 = (CollectListFragment) fragmentList.get(0);
        fragment01.setTag("rent");
        CollectListFragment fragment02 = (CollectListFragment) fragmentList.get(1);
        fragment02.setTag("sale");
        ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager() ,fragmentList );
        viewPager.setAdapter(adapter);

        tableLayout.setupWithViewPager(viewPager);

        tableLayout.getTabAt(0).setText("Rental").setTag("rent");
        tableLayout.getTabAt(1).setText("Sale").setTag("sale");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.action_bar_back:
                finish();
                break;
            default:
                break;
        }
    }

    public static String getTabLayoutTAG(){
        int currentItem = viewPager.getCurrentItem();
        if (currentItem == 0){
            return "rent";
        }else{
            return "sale";
        }
    }
}
