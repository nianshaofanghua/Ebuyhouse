package com.yidankeji.cheng.ebuyhouse.filtermodule.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.filtermodule.adapter.TypeAdapter;
import com.yidankeji.cheng.ebuyhouse.filtermodule.mode.TypeMode;
import com.yidankeji.cheng.ebuyhouse.mainmodule.activity.BasePageFragment;
import com.yidankeji.cheng.ebuyhouse.mainmodule.activity.MainFragment;
import com.yidankeji.cheng.ebuyhouse.mode.FilterCanShuMode;
import com.yidankeji.cheng.ebuyhouse.mode.FilterMode;
import com.yidankeji.cheng.ebuyhouse.mode.MainFilterMode;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.interfaceUtils.InterfaceUtils;

import java.util.ArrayList;

/**
 *
 */
public class FilterForRentFragment extends BasePageFragment implements View.OnClickListener
        , InterfaceUtils.OnListItemClickListening {

    private String[] TypeTitle = {"Price", "Days", "HOA_fee", "Year_build", "Lot_sqft", "Living_sqft"};
    private String[] TypeValues = {"Any", "Any", "Any", "Any", "Any", "Any"};
    private ArrayList<TypeMode> typeList = new ArrayList<>();
    private String TAG = "FilterForRent";
    private TextView tv_properytypes;
    private ListView listView;
    private RadioGroup beds, baths, kitchens;
    private TypeAdapter adapter;
    private ScrollView scrollView;
    private MainFilterMode mainFilterMode;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_for_rent, container, false);

        initView(view);
        return view;
    }

    @Override
    public boolean prepareFetchData(boolean forceUpdate) {
        return super.prepareFetchData(true);
    }

    @Override
    public void fetchData() {
        mainFilterMode = MainFragment.getMainFilterMode();
        int viewPagerCurrentItem = FilterActivity.getViewPagerCurrentItem();
        if (viewPagerCurrentItem == 0) {
            mainFilterMode.setRelease_type("rent");
        } else {
            mainFilterMode.setRelease_type("sale");
        }
        initData();
    }

    /**
     * 初始化成功
     */
    private void initData() {
        /**/
        String fk_category_id = mainFilterMode.getFk_category_id();
        if (fk_category_id == null || fk_category_id.equals("")) {
            tv_properytypes.setText("Any");
        } else {
            tv_properytypes.setText(mainFilterMode.getHouseName());
        }
        /**/
        String price = mainFilterMode.getPrice();
        if (price == null || price.equals("")) {
            typeList.get(0).setValues("Any");
        } else {
            typeList.get(0).setValues(mainFilterMode.getPriceName());
        }
        /**/
        String days = mainFilterMode.getDays();
        if (days == null || days.equals("")) {
            typeList.get(1).setValues("Any");
        } else {
            typeList.get(1).setValues(mainFilterMode.getDayName());
        }
        /**/
        String property_price = mainFilterMode.getProperty_price();
        if (property_price == null || property_price.equals("")) {
            typeList.get(2).setValues("Any");
        } else {
            typeList.get(2).setValues(mainFilterMode.getProperty_priceName());
        }
        /**/
        String year_build = mainFilterMode.getYear_build();
        if (year_build == null || year_build.equals("")) {
            typeList.get(3).setValues("Any");
        } else {
            typeList.get(3).setValues(mainFilterMode.getYear_buildName());
        }
        /**/
        String lot_sqft = mainFilterMode.getLot_sqft();
        if (lot_sqft == null || lot_sqft.equals("")) {
            typeList.get(4).setValues("Any");
        } else {
            typeList.get(4).setValues(mainFilterMode.getLot_sqftName());
        }
        /**/
        String living_sqft = mainFilterMode.getLiving_sqft();
        if (living_sqft == null || living_sqft.equals("")) {
            typeList.get(5).setValues("Any");
        } else {
            typeList.get(5).setValues(mainFilterMode.getLiving_sqftName());
        }
        adapter.notifyDataSetChanged();

        int bedroom = Integer.parseInt(mainFilterMode.getBedroom());
        switch (bedroom) {
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
        switch (bathroom) {
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
        switch (kitchen) {
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

    private void initView(View view) {
        TextView tv_reset = (TextView) view.findViewById(R.id.filte_reset);
        tv_reset.setOnClickListener(this);
        TextView tv_seach = (TextView) view.findViewById(R.id.filter_seach);
        tv_seach.setOnClickListener(this);
        scrollView = (ScrollView) view.findViewById(R.id.filterent_scrollview);
        tv_properytypes = (TextView) view.findViewById(R.id.filter_text_properytypes);
        tv_properytypes.setOnClickListener(this);
        beds = (RadioGroup) view.findViewById(R.id.filter_radiogroup_beds);
        beds.setOnCheckedChangeListener(bedsListening);
        baths = (RadioGroup) view.findViewById(R.id.filter_radiogroup_baths);
        baths.setOnCheckedChangeListener(bathsListening);
        kitchens = (RadioGroup) view.findViewById(R.id.filter_radiogroup_kitchens);
        kitchens.setOnCheckedChangeListener(kitchensListening);
        listView = ((ListView) view.findViewById(R.id.filterent_listview));
        for (int i = 0; i < TypeTitle.length; i++) {
            TypeMode mode = new TypeMode();
            mode.setTitle(TypeTitle[i]);
            mode.setValues(TypeValues[i]);
            typeList.add(mode);
        }
        adapter = new TypeAdapter(getActivity(), typeList);
        adapter.setListening(this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.filter_text_properytypes:
                Intent intent = new Intent(getActivity(), FilerHouseTypeActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.filte_reset:
                setClearData();
                break;
            case R.id.filter_seach:
                int viewPagerCurrentItem = FilterActivity.getViewPagerCurrentItem();
                if (viewPagerCurrentItem == 0) {
                    mainFilterMode.setRelease_type("rent");
                } else {
                    mainFilterMode.setRelease_type("sale");
                }
                mainFilterMode.setRefresh_list(true);
                mainFilterMode.setRefresh_map(true);
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 1005) {
            FilterMode houseTypemode = (FilterMode) data.getSerializableExtra("houseType");
            Log.i(TAG + "data", houseTypemode.getType());
            tv_properytypes.setText(houseTypemode.getType());
            mainFilterMode.setFk_category_id(houseTypemode.getId());
            mainFilterMode.setHouseName(houseTypemode.getType());
        }
    }

    @Override
    public void onListItemClickListening(View view, int position) {
        TypeMode typeMode = typeList.get(position);
        String title = typeMode.getTitle();
        Log.e("点击item","点击item点击item点击item"+title);
//        switch (title) {
//            case "Price":
//                showSelectDialog(FilterActivity.priceList, position);
//                break;
//            case "Days":
//                showSelectDialog(FilterActivity.daysList, position);
//                break;
//            case "HOA Fee":
//                Log.e("点击item","点击item点击item点击item"+title+"-----"+FilterActivity.propertyPriceList.size());
//                showSelectDialog(FilterActivity.propertyPriceList, position);
//                break;
//            case "Year_build":
//                showSelectDialog(FilterActivity.yearBuildList, position);
//                break;
//            case "Lot_sqft":
//                showSelectDialog(FilterActivity.lotSqftList, position);
//                break;
//            case "Living_sqft":
//                showSelectDialog(FilterActivity.livingsqftList, position);
//                break;
//        }
        switch (position){
            case 0:
                showSelectDialog(FilterActivity.priceList, position);
                break;
            case 1:
                showSelectDialog(FilterActivity.daysList, position);
                break;
            case 2:
                showSelectDialog(FilterActivity.propertyPriceList, position);
                break;
            case 3:
                showSelectDialog(FilterActivity.yearBuildList, position);
                break;
            case 4:
                showSelectDialog(FilterActivity.lotSqftList, position);
                break;
            case 5:
                showSelectDialog(FilterActivity.livingsqftList, position);
                break;
            default:
                break;
        }

    }

    private void showSelectDialog(ArrayList<FilterCanShuMode> list, final int position) {
        new FilterPopwindows(getActivity(), list).getDialog(new FilterPopwindows.FilterPopListening() {
            @Override
            public void getFilterPopListening(String key, String values) {
                if (!TextUtils.isEmpty(values)) {
                    switch (position) {
                        case 0:
                            typeList.get(position).setValues(values);
                            mainFilterMode.setPriceName(values);
                            mainFilterMode.setPrice(key);
                            adapter.notifyDataSetChanged();
                            break;
                        case 1:
                            typeList.get(position).setValues(values);
                            mainFilterMode.setDayName(values);
                            mainFilterMode.setDays(key);
                            adapter.notifyDataSetChanged();
                            break;
                        case 2:
                            typeList.get(position).setValues(values);
                            mainFilterMode.setProperty_priceName(values);
                            mainFilterMode.setProperty_price(key);
                            adapter.notifyDataSetChanged();
                            break;
                        case 3:
                            typeList.get(position).setValues(values);
                            mainFilterMode.setYear_buildName(values);
                            mainFilterMode.setYear_build(key);
                            adapter.notifyDataSetChanged();
                            break;
                        case 4:
                            typeList.get(position).setValues(values);
                            mainFilterMode.setLot_sqftName(values);
                            mainFilterMode.setLot_sqft(key);
                            adapter.notifyDataSetChanged();
                            break;
                        case 5:
                            typeList.get(position).setValues(values);
                            mainFilterMode.setLiving_sqftName(values);
                            mainFilterMode.setLiving_sqft(key);
                            adapter.notifyDataSetChanged();
                            break;
                    }
                }
            }
        });
    }

    RadioGroup.OnCheckedChangeListener bedsListening = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
            String tag = (String) radioButton.getTag();
            mainFilterMode.setBedroom(tag);
        }
    };

    RadioGroup.OnCheckedChangeListener bathsListening = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
            String tag = (String) radioButton.getTag();
            mainFilterMode.setBathroom(tag);
        }
    };

    RadioGroup.OnCheckedChangeListener kitchensListening = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
            String tag = (String) radioButton.getTag();
            mainFilterMode.setKitchen(tag);
        }
    };


    public void setClearData() {
        mainFilterMode.setRelease_type("");
        /**/
        tv_properytypes.setText("Any");
        mainFilterMode.setFk_category_id("");
        mainFilterMode.setHouseName("Any");
        /**/
        beds.check(R.id.filter_radiogroup_beds00);
        baths.check(R.id.filter_radiogroup_baths00);
        kitchens.check(R.id.filter_radiogroup_kitchens00);
        mainFilterMode.setBedroom("-1");
        mainFilterMode.setBathroom("-1");
        mainFilterMode.setKitchen("-1");
        /**/
        for (int i = 0; i < typeList.size(); i++) {
            typeList.get(i).setValues("Any");
        }
        /**/
        mainFilterMode.setPriceName("Any");
        mainFilterMode.setPrice("");
        mainFilterMode.setDayName("Any");
        mainFilterMode.setDays("");
        mainFilterMode.setProperty_priceName("Any");
        mainFilterMode.setProperty_price("");
        mainFilterMode.setYear_buildName("Any");
        mainFilterMode.setYear_build("");
        mainFilterMode.setLot_sqftName("Any");
        mainFilterMode.setLot_sqft("");
        mainFilterMode.setLiving_sqftName("Any");
        mainFilterMode.setLiving_sqft("");
        adapter.notifyDataSetChanged();
    }

    public void setChange() {
        typeList.get(0).setTitle("Rent/month");;
    }

    public void setTypeTitle(ArrayList<String> list) {
        for (int i = 0; i <list.size() ; i++) {
            typeList.get(i).setTitle(list.get(i));
        }

        adapter.notifyDataSetChanged();
    }

}
