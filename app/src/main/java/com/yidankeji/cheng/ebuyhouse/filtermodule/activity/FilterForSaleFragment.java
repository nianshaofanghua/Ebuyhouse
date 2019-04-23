package com.yidankeji.cheng.ebuyhouse.filtermodule.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.mainmodule.activity.BasePageFragment;
import com.yidankeji.cheng.ebuyhouse.mainmodule.activity.MainFragment;
import com.yidankeji.cheng.ebuyhouse.mode.FilterCanShuMode;
import com.yidankeji.cheng.ebuyhouse.mode.FilterMode;
import com.yidankeji.cheng.ebuyhouse.mode.MainFilterMode;

import java.util.ArrayList;

/**
 * 筛选
 *    出售
 */
public class FilterForSaleFragment extends BasePageFragment implements View.OnClickListener{

    private FilterMode mode = new FilterMode();
    private String TAG = "FilterForSale";
    private TextView tv_properytypes , tv_squarefeet ,tv_yearbuild ,tv_lotsize ,tv_daysonebuyhouse ,tv_price;
    private RadioGroup beds ,baths ,kitchens;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_for_sale, container, false);

        initView(view);
        return view;
    }

    @Override
    public boolean prepareFetchData(boolean forceUpdate) {
        return super.prepareFetchData(true);
    }

    @Override
    public void fetchData() {
        MainFragment.getMainFilterMode().setRelease_type("sale");
        setData();
    }

    private void initView(View view) {
        tv_properytypes = (TextView) view.findViewById(R.id.filter_text_properytypes);
        tv_properytypes.setOnClickListener(this);
        tv_price = (TextView) view.findViewById(R.id.filter_text_price);
        tv_price.setOnClickListener(this);
        beds = (RadioGroup) view.findViewById(R.id.filter_radiogroup_beds);
        beds.setOnCheckedChangeListener(bedsListening);
        baths = (RadioGroup) view.findViewById(R.id.filter_radiogroup_baths);
        baths.setOnCheckedChangeListener(bathsListening);
        kitchens = (RadioGroup) view.findViewById(R.id.filter_radiogroup_kitchens);
        kitchens.setOnCheckedChangeListener(kitchensListening);
        tv_squarefeet = (TextView) view.findViewById(R.id.filter_text_squarefeet);
        tv_squarefeet.setOnClickListener(this);
        tv_yearbuild = (TextView) view.findViewById(R.id.filter_text_yearbuild);
        tv_yearbuild.setOnClickListener(this);
        tv_lotsize = (TextView) view.findViewById(R.id.filter_text_lotsize);
        tv_lotsize.setOnClickListener(this);
        tv_daysonebuyhouse = (TextView) view.findViewById(R.id.filter_text_daysonebuyhouse);
        tv_daysonebuyhouse.setOnClickListener(this);
        TextView saleResert = (TextView) view.findViewById(R.id.filtersale_reset);
        saleResert.setOnClickListener(this);
        TextView saleSeach = (TextView) view.findViewById(R.id.filtersale_seach);
        saleSeach.setOnClickListener(this);
    }

    /**
     * 初始化值
     */
    private void setData(){
        MainFilterMode mainFilterMode = MainFragment.getMainFilterMode();
        tv_properytypes.setText(mainFilterMode.getFk_category_id());
        tv_price.setText(mainFilterMode.getPrice());
        tv_squarefeet.setText(mainFilterMode.getLiving_sqft());
        tv_yearbuild.setText(mainFilterMode.getYear_build());
        tv_lotsize.setText(mainFilterMode.getLot_sqft());
        int bedroom = Integer.parseInt(mainFilterMode.getBedroom());
        switch (bedroom){
            case -1:
                beds.check(R.id.filter_radiogroup_beds00);
                break;
            case 1:
                beds.check(R.id.filter_radiogroup_beds01);
                break;
            case 2:
                beds.check(R.id.filter_radiogroup_beds02);
                break;
            case 3:
                beds.check(R.id.filter_radiogroup_beds03);
                break;
            case 4:
                beds.check(R.id.filter_radiogroup_beds04);
                break;
            case 5:
                beds.check(R.id.filter_radiogroup_beds05);
                break;
        }
        int bathroom = Integer.parseInt(mainFilterMode.getBathroom());
        switch (bathroom){
            case -1:
                baths.check(R.id.filter_radiogroup_baths00);
                break;
            case 1:
                baths.check(R.id.filter_radiogroup_baths01);
                break;
            case 2:
                baths.check(R.id.filter_radiogroup_baths02);
                break;
            case 3:
                baths.check(R.id.filter_radiogroup_baths03);
                break;
            case 4:
                baths.check(R.id.filter_radiogroup_baths04);
                break;
            case 5:
                baths.check(R.id.filter_radiogroup_baths05);
                break;
        }
        int kitchen = Integer.parseInt(mainFilterMode.getKitchen());
        switch (kitchen){
            case -1:
                kitchens.check(R.id.filter_radiogroup_kitchens00);
                break;
            case 1:
                kitchens.check(R.id.filter_radiogroup_kitchens01);
                break;
            case 2:
                kitchens.check(R.id.filter_radiogroup_kitchens02);
                break;
            case 3:
                kitchens.check(R.id.filter_radiogroup_kitchens03);
                break;
            case 4:
                kitchens.check(R.id.filter_radiogroup_kitchens04);
                break;
            case 5:
                kitchens.check(R.id.filter_radiogroup_kitchens05);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.filter_text_properytypes://房屋类型
                Intent intent = new Intent(getActivity() , FilerHouseTypeActivity.class);
                startActivityForResult(intent , 100);
                break;
            case R.id.filter_text_price://价格区间
                showSelectDialog(getActivity() , FilterActivity.priceList , "price");
                break;
            case R.id.filter_text_squarefeet://房屋面积
                showSelectDialog(getActivity() , FilterActivity.livingsqftList , "SquareFeet");
                break;
            case R.id.filter_text_yearbuild://房屋年龄
                showSelectDialog(getActivity() , FilterActivity.yearBuildList , "yearBuild");
                break;
            case R.id.filter_text_lotsize:
                showSelectDialog(getActivity() , FilterActivity.lotSqftList , "lotSqft");
                break;
            case R.id.filter_text_daysonebuyhouse:
                showSelectDialog(getActivity() , FilterActivity.daysList , "days");
                break;
            case R.id.filtersale_reset:
                setReset();
                break;
            case R.id.filtersale_seach:
                setSeach();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 1005){//城市类型
            FilterMode houseTypemode = (FilterMode) data.getSerializableExtra("houseType");
            Log.i(TAG+"data" , houseTypemode.getType());
            tv_properytypes.setText(houseTypemode.getType());
            mode.setFilter_HouseName(houseTypemode.getType());
            mode.setFilter_HouseID(houseTypemode.getId());
        }
    }

    /**
     * 卧室筛选
     */
    RadioGroup.OnCheckedChangeListener bedsListening = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
            String tag = (String) radioButton.getTag();
            mode.setFilter_Beds(tag);
        }
    };

    /**
     * 浴室筛选
     */
    RadioGroup.OnCheckedChangeListener bathsListening = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
            String tag = (String) radioButton.getTag();
            mode.setFilter_Baths(tag);
        }
    };

    /**
     * 厨房筛选
     */
    RadioGroup.OnCheckedChangeListener kitchensListening = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
            String tag = (String) radioButton.getTag();
            mode.setFilter_Kitchens(tag);
        }
    };

    /**
     * 弹出框
     * @param tag 判断是为哪一个选项弹出的对话框
     */
    private void showSelectDialog(Activity activity , ArrayList<FilterCanShuMode> list , final String tag){
        new FilterPopwindows(activity , list).getDialog(new FilterPopwindows.FilterPopListening() {
            @Override
            public void getFilterPopListening(String key, String values) {
                switch (tag){
                    case "price":
                        tv_price.setText(values);
                        mode.setFilter_Price(key);
                        break;
                    case "SquareFeet":
                        tv_squarefeet.setText(values);
                        mode.setFilter_SquareFeet(key);
                        break;
                    case "yearBuild":
                        tv_yearbuild.setText(values);
                        mode.setFilter_YearBuild(key);
                        break;
                    case "lotSqft":
                        tv_lotsize.setText(values);
                        mode.setFilter_LotSize(key);
                        break;
                    case "days":
                        tv_daysonebuyhouse.setText(values);
                        mode.setFilter_Days(key);
                        break;
                }
            }
        });
    }

    /**
     * resert
     */
    private void setReset(){
        tv_price.setText("");
        tv_squarefeet.setText("");
        tv_yearbuild.setText("");
        tv_lotsize.setText("");
        tv_daysonebuyhouse.setText("");
        tv_properytypes.setText("");
        beds.check(R.id.filter_radiogroup_beds00);
        baths.check(R.id.filter_radiogroup_baths00);
        kitchens.check(R.id.filter_radiogroup_kitchens00);
        MainFragment.setMainFilterModeClient();
        MainFilterMode mainFilterMode = MainFragment.getMainFilterMode();
        mainFilterMode.setRefresh_list(true);
        mainFilterMode.setRefresh_map(true);
        mainFilterMode.setRelease_type("sale");
        mode.setClient();
    }
    /**
     * seach
     */
    private void setSeach(){
        MainFilterMode mainFilterMode = MainFragment.getMainFilterMode();
        mainFilterMode.setRelease_type("sale");
        mainFilterMode.setFk_category_id(mode.getFilter_HouseID());
        mainFilterMode.setPrice(mode.getFilter_Price());
        mainFilterMode.setBedroom(mode.getFilter_Beds());
        mainFilterMode.setBathroom(mode.getFilter_Baths());
        mainFilterMode.setKitchen(mode.getFilter_Kitchens());
        mainFilterMode.setLot_sqft(mode.getFilter_LotSize());
        mainFilterMode.setLiving_sqft(mode.getFilter_SquareFeet());
        mainFilterMode.setYear_build(mode.getFilter_YearBuild());
        mainFilterMode.setDays(mode.getFilter_Days());
        mainFilterMode.setRefresh_list(true);
        mainFilterMode.setRefresh_map(true);
        getActivity().finish();
    }
}
