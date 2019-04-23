package com.yidankeji.cheng.ebuyhouse.housemodule.httputils;

import android.app.Activity;
import android.view.View;

import com.wevey.selector.dialog.DialogOnClickListener;
import com.wevey.selector.dialog.NormalAlertDialog;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.housemodule.mode.SubmitRoomTypeMode;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018\1\3 0003.
 */

public class SecondaryUtils {

    private Activity activity;

    public SecondaryUtils(Activity activity) {
        this.activity = activity;
    }

    //--------------------------------------------------------------------------------
    /**
     * 放弃上传房屋信息
     */
    private NormalAlertDialog giveupSubmitDialog;

    public void getGiveUpSubmitDialog() {
        giveupSubmitDialog = new NormalAlertDialog.Builder(activity)
                .setHeight(0.23f).setWidth(0.65f).setTitleVisible(true)
                .setTitleText("System hint").setTitleTextColor(R.color.text_heise)
                .setContentText("Will you abandon this upload")
                .setContentTextColor(R.color.text_heise)
                .setLeftButtonText("Cancel").setLeftButtonTextColor(R.color.text_hei)
                .setRightButtonText("Determine")
                .setRightButtonTextColor(R.color.text_heise)
                .setOnclickListener(new DialogOnClickListener() {
                    @Override
                    public void clickLeftButton(View view) {
                        giveupSubmitDialog.dismiss();
                    }

                    @Override
                    public void clickRightButton(View view) {
                        giveupSubmitDialog.dismiss();
                        activity.finish();
                    }
                }).build();
        giveupSubmitDialog.show();
    }


    //-------------------------------------------------------------------------------------
    /**
     * 初始化值
     */
    private String[] titleArray = {"Price", "Beds", "Baths", "Garages", "Kitchens", "Hoa/master plan/ month", "Living-sqft", "Year-Built", "Lot-sqft", "Parcel#", "MLS#"};
    private String[] hintTitleArray = {"$/mo", "0", "0", "0", "0", "$/mo", "0/sqft", "1992", "0/sqft", "111-11-111-111", "1234567"};
    private String[] tagArray = {"price", "bedroom", "bathroom", "garage", "kitchen", "property_price", "living_sqft", "year_build", "lot_sqft", "apn", "mls"};


    private String[] titleArray_rent =    {"Price" ,"Deposit", "Bedrooms" , "Bathrooms" , "Garages", "Kitchens",  "HOA" , "Living-sqft" , "Year-Built" , "Lot-sqft", "Parcel#" , "MLS#"};
    private String[] hintTitleArray_rent={"$/Mo",     "0",    "0",  "0",       "0",       "0","$/mo","0/sqft", "1992","0/sqft","5-12 digits","5-12 digits"};
    private String[] tagArray_rent =      {"price" ,"deposit", "bedroom" , "bathroom" , "garage", "kitchen", "property_price" , "living_sqft" , "year_build" , "lot_sqft"  , "apn" , "mls"};


    private String[] titleArray_sale =    {"Price" , "Bedrooms" , "Bathrooms" , "Garages", "Kitchens",  "Hoa/master plan/ month" , "Living-sqft" , "Year-Built" , "Lot-sqft", "Parcel#" , "MLS#"};
    private String[] hintTitleArray_sale={"$/mo",     "0",      "0",       "0",       "0","$/mo","0/sqft", "1992","0/sqft","5-12 digits","5-12 digits"};
    private String[] tagArray_sale =      {"price" , "bedroom" , "bathroom" , "garage", "kitchen", "property_price" , "living_sqft" , "year_build" , "lot_sqft"  , "apn" , "mls"};


    private String[] titleArray_rent_edit =    {"Price" ,"Deposit", "Bedrooms" , "Bathrooms" , "Garages", "Kitchens",  "HOA" , "Living-sqft" , "Year-Built" , "Lot-sqft" };
    private String[] hintTitleArray_rent_edit={"$/Mo",     "0",    "0",  "0",       "0",       "0","$/mo","0/sqft", "1992","0/sqft",};
    private String[] tagArray_rent_edit =      {"price" ,"deposit", "bedroom" , "bathroom" , "garage", "kitchen", "property_price" , "living_sqft" , "year_build" , "lot_sqft"  };

    private String[] titleArray_sale_edit =    {"Price" , "Bedrooms" , "Bathrooms" , "Garages", "Kitchens",  "Hoa/master plan/ month" , "Living-sqft" , "Year-Built" , "Lot-sqft" };
    private String[] hintTitleArray_sale_edit={"$/mo",     "0",      "0",       "0",       "0","$/mo","0/sqft", "1992","0/sqft"};
    private String[] tagArray_sale_edit =      {"price" , "bedroom" , "bathroom" , "garage", "kitchen", "property_price" , "living_sqft" , "year_build" , "lot_sqft"  };

    private ArrayList<SubmitRoomTypeMode> submitTypeList = new ArrayList<>();

    public ArrayList<SubmitRoomTypeMode> setTypeDataOfListView() {
        submitTypeList.clear();
        for (int i = 0; i < titleArray.length; i++) {
            SubmitRoomTypeMode mode = new SubmitRoomTypeMode();
            mode.setTitle(titleArray[i]);
            mode.setTag(tagArray[i]);

            mode.setHint(hintTitleArray[i]);
            submitTypeList.add(mode);
        }
        return submitTypeList;
    }
    public ArrayList<SubmitRoomTypeMode> setTypeDataOfListViewForRent() {
        submitTypeList.clear();
        for (int i = 0; i < titleArray_rent.length; i++) {
            SubmitRoomTypeMode mode = new SubmitRoomTypeMode();
            mode.setTitle(titleArray_rent[i]);
            mode.setTag(tagArray_rent[i]);

            mode.setHint(hintTitleArray_rent[i]);
            submitTypeList.add(mode);
        }
        return submitTypeList;
    }
    public ArrayList<SubmitRoomTypeMode> setTypeDataOfListViewForSale() {
        submitTypeList.clear();
        for (int i = 0; i < titleArray_sale.length; i++) {
            SubmitRoomTypeMode mode = new SubmitRoomTypeMode();
            mode.setTitle(titleArray_sale[i]);
            mode.setTag(tagArray_sale[i]);

            mode.setHint(hintTitleArray_sale[i]);
            submitTypeList.add(mode);
        }
        return submitTypeList;
    }

    public ArrayList<SubmitRoomTypeMode> setTypeDataOfListViewForRentEdit() {
        submitTypeList.clear();
        for (int i = 0; i < titleArray_rent_edit.length; i++) {
            SubmitRoomTypeMode mode = new SubmitRoomTypeMode();
            mode.setTitle(titleArray_rent_edit[i]);
            mode.setTag(tagArray_rent_edit[i]);

            mode.setHint(hintTitleArray_rent_edit[i]);
            submitTypeList.add(mode);
        }
        return submitTypeList;
    }
    public ArrayList<SubmitRoomTypeMode> setTypeDataOfListViewForSaleEdit() {
        submitTypeList.clear();
        for (int i = 0; i < titleArray_sale_edit.length; i++) {
            SubmitRoomTypeMode mode = new SubmitRoomTypeMode();
            mode.setTitle(titleArray_sale_edit[i]);
            mode.setTag(tagArray_sale_edit[i]);

            mode.setHint(hintTitleArray_sale_edit[i]);
            submitTypeList.add(mode);
        }
        return submitTypeList;
    }
}
