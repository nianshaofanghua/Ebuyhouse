package com.yidankeji.cheng.ebuyhouse.filtermodule.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.adapter.ViewPagerFragmentAdapter;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.mainmodule.activity.MainFragment;
import com.yidankeji.cheng.ebuyhouse.mode.FilterCanShuMode;
import com.yidankeji.cheng.ebuyhouse.mode.FilterParentModel;
import com.yidankeji.cheng.ebuyhouse.mode.MainFilterMode;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class FilterActivity extends FragmentActivity implements View.OnClickListener {

    public static ArrayList<FilterCanShuMode> propertyPriceList = new ArrayList();
    public static ArrayList<FilterCanShuMode> priceList = new ArrayList();
    public static ArrayList<FilterCanShuMode> livingsqftList = new ArrayList();
    public static ArrayList<FilterCanShuMode> daysList = new ArrayList();
    public static ArrayList<FilterCanShuMode> yearBuildList = new ArrayList();
    public static ArrayList<FilterCanShuMode> lotSqftList = new ArrayList();
    public static ArrayList<FilterParentModel> parentList = new ArrayList<>();
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private TabLayout tableLayout;
    private static ViewPager viewPager;
    private String TAG = "Filter";
    FilterForRentFragment fragment;
    FilterForRentFragment fragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.filter_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("Filter");
        tableLayout = (TabLayout) findViewById(R.id.filter_tablayout);
        viewPager = (ViewPager) findViewById(R.id.filter_viewpager);
        setData();
        getFilterDataHttp();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_bar_back:
                MainFilterMode mainFilterMode = MainFragment.getMainFilterMode();
                mainFilterMode.setRefresh_list(true);
                mainFilterMode.setRefresh_map(true);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            MainFilterMode mainFilterMode = MainFragment.getMainFilterMode();
            mainFilterMode.setRefresh_list(true);
            mainFilterMode.setRefresh_map(true);
            finish();
        }
        return true;
    }

    private void setData() {
        fragment = new FilterForRentFragment();
        fragment1 = new FilterForRentFragment();

        fragmentList.add(fragment);
        fragmentList.add(fragment1);
        ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPager);
        tableLayout.getTabAt(0).setText("For Rent");
        tableLayout.getTabAt(1).setText("For Sale");

        MainFilterMode mainFilterMode = MainFragment.getMainFilterMode();
        String release_type = mainFilterMode.getRelease_type();
        if (release_type.equals("") || release_type.equals("sale")) {
            viewPager.setCurrentItem(1);
        } else {
            viewPager.setCurrentItem(0);
        }
    }

    public static int getViewPagerCurrentItem() {
        return viewPager.getCurrentItem();
    }

    /**
     * 获取筛选默认值
     */
    private void getFilterDataHttp() {
        LoadingUtils.showDialog(FilterActivity.this);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.filterArg)
                .enqueue(new NewRawResponseHandler(FilterActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG + "筛选默认数据", response);
                        LoadingUtils.dismiss();
                        //getJSONData(response);
                        //getJson(response);
                        getJson02(response);
                    }

                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        ;
                        Log.i(TAG + "筛选默认数据", error_msg);
                        LoadingUtils.dismiss();
                    }
                });
    }

    /**
     * 解析筛选默认值
     */
    private void getJSONData(String json) {
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONObject data = content.getJSONObject("data");
                    String filter_arg = data.getString("filter_arg");

                    JSONArray jsonArray = new JSONArray(filter_arg);
                    JSONArray price = jsonArray.getJSONObject(0).getJSONArray("rent/month");
                    for (int i = 0; i < price.length(); i++) {
                        JSONObject object = price.getJSONObject(i);
                        FilterCanShuMode mode = new FilterCanShuMode();
                        mode.setKey(object.getString("key"));
                        mode.setValues(object.getString("value"));
                        priceList.add(mode);
                    }
                    JSONArray days = jsonArray.getJSONObject(1).getJSONArray("days");
                    for (int i = 0; i < days.length(); i++) {
                        JSONObject object = days.getJSONObject(i);
                        FilterCanShuMode mode = new FilterCanShuMode();
                        mode.setKey(object.getString("key"));
                        mode.setValues(object.getString("value"));
                        daysList.add(mode);
                    }
                    JSONArray property_price = jsonArray.getJSONObject(2).getJSONArray("HOA_Fee");
                    for (int i = 0; i < property_price.length(); i++) {
                        JSONObject object = property_price.getJSONObject(i);
                        FilterCanShuMode mode = new FilterCanShuMode();
                        mode.setKey(object.getString("key"));
                        mode.setValues(object.getString("value"));
                        propertyPriceList.add(mode);
                    }
                    JSONArray year_build = jsonArray.getJSONObject(3).getJSONArray("year_build");
                    for (int i = 0; i < year_build.length(); i++) {
                        JSONObject object = year_build.getJSONObject(i);
                        FilterCanShuMode mode = new FilterCanShuMode();
                        mode.setKey(object.getString("key"));
                        mode.setValues(object.getString("value"));
                        yearBuildList.add(mode);
                    }
                    JSONArray lot_sqft = jsonArray.getJSONObject(4).getJSONArray("lot_sqft");
                    for (int i = 0; i < lot_sqft.length(); i++) {
                        JSONObject object = lot_sqft.getJSONObject(i);
                        FilterCanShuMode mode = new FilterCanShuMode();
                        mode.setKey(object.getString("key"));
                        mode.setValues(object.getString("value"));
                        lotSqftList.add(mode);
                    }
                    JSONArray living_sqft = jsonArray.getJSONObject(5).getJSONArray("living_sqft");
                    for (int i = 0; i < living_sqft.length(); i++) {
                        JSONObject object = living_sqft.getJSONObject(i);
                        FilterCanShuMode mode = new FilterCanShuMode();
                        mode.setKey(object.getString("key"));
                        mode.setValues(object.getString("value"));
                        livingsqftList.add(mode);
                    }
                } else {
                    ToastUtils.showToast(FilterActivity.this, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

            ToastUtils.showToast(FilterActivity.this, getString(R.string.json_erro));
        }
    }


    /**
     * 解析筛选默认值
     */
    private void getJson(String json) {
        parentList.clear();
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONObject data = content.getJSONObject("data");
                    String filter_arg = data.getString("filter_arg");
                    Log.e("keys", "----------" + filter_arg);
                    JSONArray jsonArray = new JSONArray(filter_arg);
                    Log.e("keys", "-----jsonArray-----" + jsonArray.length());

                    ArrayList<String> strings = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject countName = jsonArray.getJSONObject(i);
                        Iterator<String> keys = countName.keys();

                        while (keys.hasNext()) {

                            String name = (String) keys.next();
                            if (name != null) {
                                strings.add(name);
                            }
                        }
                    }


                    for (int z = 0; z < jsonArray.length(); z++) {
                        ArrayList<FilterCanShuMode> list = new ArrayList<>();
                        FilterParentModel model = new FilterParentModel();
                        JSONObject countName = jsonArray.getJSONObject(z);
                        JSONArray jsonArray02 = countName.getJSONArray(strings.get(z));


                        for (int i = 0; i < jsonArray02.length(); i++) {
                            JSONObject object = jsonArray02.getJSONObject(i);
                            FilterCanShuMode mode = new FilterCanShuMode();
                            mode.setKey(object.getString("key"));
                            mode.setValues(object.getString("value"));
                            list.add(mode);
                        }

                        model.setName(strings.get(z));
                        model.setList(list);
                        parentList.add(model);


                    }
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {


                        }
                    });


                } else {
                    ToastUtils.showToast(FilterActivity.this, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("keys", "" + e.toString());
            ToastUtils.showToast(FilterActivity.this, getString(R.string.json_erro));
        }
    }


    public void getJson02(String json) {

        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");

                if (state == 1) {
                    ArrayList<String> listString = new ArrayList<>();
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONObject data = content.getJSONObject("data");
                    String filter_arg = data.getString("filter_arg");

                    JSONArray jsonArray = new JSONArray(filter_arg);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        listString.add(jsonArray.getJSONObject(i).getString("value"));
                    }
                    fragment.setTypeTitle(listString);

                    fragment1.setTypeTitle(listString);
                    fragment.setChange();

                    JSONArray price = jsonArray.getJSONObject(0).getJSONArray("price");
                    for (int i = 0; i < price.length(); i++) {
                        JSONObject object = price.getJSONObject(i);
                        FilterCanShuMode mode = new FilterCanShuMode();
                        mode.setKey(object.getString("key"));
                        mode.setValues(object.getString("value"));
                        priceList.add(mode);
                    }
                    JSONArray days = jsonArray.getJSONObject(1).getJSONArray("days");
                    for (int i = 0; i < days.length(); i++) {
                        JSONObject object = days.getJSONObject(i);
                        FilterCanShuMode mode = new FilterCanShuMode();
                        mode.setKey(object.getString("key"));
                        mode.setValues(object.getString("value"));
                        daysList.add(mode);
                    }
                    JSONArray property_price = jsonArray.getJSONObject(2).getJSONArray("HOA_Fee");
                    for (int i = 0; i < property_price.length(); i++) {
                        JSONObject object = property_price.getJSONObject(i);
                        FilterCanShuMode mode = new FilterCanShuMode();
                        mode.setKey(object.getString("key"));
                        mode.setValues(object.getString("value"));
                        propertyPriceList.add(mode);
                    }
                    JSONArray year_build = jsonArray.getJSONObject(3).getJSONArray("year_build");
                    for (int i = 0; i < year_build.length(); i++) {
                        JSONObject object = year_build.getJSONObject(i);
                        FilterCanShuMode mode = new FilterCanShuMode();
                        mode.setKey(object.getString("key"));
                        mode.setValues(object.getString("value"));
                        yearBuildList.add(mode);
                    }
                    JSONArray lot_sqft = jsonArray.getJSONObject(4).getJSONArray("lot_sqft");
                    for (int i = 0; i < lot_sqft.length(); i++) {
                        JSONObject object = lot_sqft.getJSONObject(i);
                        FilterCanShuMode mode = new FilterCanShuMode();
                        mode.setKey(object.getString("key"));
                        mode.setValues(object.getString("value"));
                        lotSqftList.add(mode);
                    }
                    JSONArray living_sqft = jsonArray.getJSONObject(5).getJSONArray("living_sqft");
                    for (int i = 0; i < living_sqft.length(); i++) {
                        JSONObject object = living_sqft.getJSONObject(i);
                        FilterCanShuMode mode = new FilterCanShuMode();
                        mode.setKey(object.getString("key"));
                        mode.setValues(object.getString("value"));
                        livingsqftList.add(mode);
                    }
                } else if (state == 703) {
                    new LanRenDialog(FilterActivity.this).onlyLogin();
                } else {
                    ToastUtils.showToast(FilterActivity.this, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(FilterActivity.this, getString(R.string.json_erro));
        }

    }
}
