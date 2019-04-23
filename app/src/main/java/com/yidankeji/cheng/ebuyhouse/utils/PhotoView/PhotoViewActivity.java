package com.yidankeji.cheng.ebuyhouse.utils.PhotoView;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;

import java.io.Serializable;
import java.util.ArrayList;

public class PhotoViewActivity extends Activity {

    private ArrayList<String> imageList;
    private int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        imageList = (ArrayList<String>) getIntent().getSerializableExtra("ImageList");
        state = getIntent().getIntExtra("state" , 0);
        if (imageList.size() == 0){
            return;
        }
        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        ViewPager viewPager = (ViewPager) findViewById(R.id.photoview_viewpager);
        ViewPageAdapter adapter = new ViewPageAdapter(this , imageList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(state);

        final TextView state = (TextView) findViewById(R.id.photoview_state);
        state.setTextSize(20f);
        state.setText(1 +"/"+imageList.size());

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                state.setText((position+1) +"/"+imageList.size());
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
