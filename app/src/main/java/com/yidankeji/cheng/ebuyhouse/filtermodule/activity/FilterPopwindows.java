package com.yidankeji.cheng.ebuyhouse.filtermodule.activity;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.mode.FilterCanShuMode;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017\12\28 0028.
 */

public class FilterPopwindows implements View.OnClickListener{

    private Activity activity;
    private ArrayList<FilterCanShuMode> list;
    private PopupWindow pop;
    private WheelView wheelViewleft ,wheelViewright;
    private String TAG = "FilterPop";
    private String key , values;

    public FilterPopwindows(Activity activity, ArrayList<FilterCanShuMode> list) {
        this.activity = activity;
        this.list = list;
    }


    public void getDialog(final FilterPopListening listening){
        if (list.size() == 0){
            ToastUtils.showToast(activity , activity.getString(R.string.no_data));
            return;
        }
        View view = LayoutInflater.from(activity).inflate(R.layout.pop_wheelview, null);
        pop = new PopupWindow( view, LinearLayout.LayoutParams.MATCH_PARENT , LinearLayout.LayoutParams.WRAP_CONTENT , true);
        pop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pop.setAnimationStyle(R.style.dialogWindowAnim);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        WindowUtils.setBg(activity , 0.7f);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.showAtLocation(view , Gravity.BOTTOM , 0 , 0);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowUtils.setBg(activity , 1f);
                listening.getFilterPopListening(key , values);
            }
        });

        initView(view);
    }

    private void initView(View view){
        ArrayList<String> listson = new ArrayList<>();
        for (int i = 0; i < list.size() ; i++) {
            listson.add(list.get(i).getKey());
        }
        wheelViewleft = (WheelView) view.findViewById(R.id.wheelview_twoline_one);
        setWheelViewStyle(wheelViewleft, listson);
        wheelViewright = (WheelView) view.findViewById(R.id.wheelview_twoline_two);
        setWheelViewStyle(wheelViewright, listson);
        Button cancel = (Button) view.findViewById(R.id.wheelview_twoline_cancel);
        cancel.setOnClickListener(this);
        Button ok = (Button) view.findViewById(R.id.wheelview_twoline_ok);
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wheelview_twoline_cancel:
                pop.dismiss();
                break;
            case R.id.wheelview_twoline_ok:
                getData();
                break;
        }
    }

    private void getData(){
        String lkey = "";
        String rkey = "";
        String leftItem = (String) wheelViewleft.getSelectionItem();
        String rightItem = (String) wheelViewright.getSelectionItem();
        for (int i = 0; i < list.size() ; i++) {
            String key = list.get(i).getKey();
            if (key.equals(leftItem)){
                lkey = list.get(i).getValues();
            }
            if (key.equals(rightItem)){
                rkey = list.get(i).getValues();
            }
        }
        Log.i(TAG+"结果" , "左:"+leftItem+"右:"+rightItem);
        int leftnum = Integer.parseInt(lkey);
        int rightnum = Integer.parseInt(rkey);
        if (leftnum > rightnum){
            ToastUtils.showToast(activity , "So we're going to have to be less than the number on the left");
            return;
        }
        key = lkey+"-"+rkey;
        values = leftItem+"-"+rightItem;

        pop.dismiss();
    }
    private void setWheelViewStyle(WheelView wheelView , ArrayList<String> listson){
        wheelView.setWheelAdapter(new ArrayWheelAdapter(activity));
        wheelView.setLoop(false);
        wheelView.setWheelData(listson);
        wheelView.setSkin(WheelView.Skin.Holo);
        wheelView.setWheelSize(5);
    }

    public interface FilterPopListening{
        void getFilterPopListening(String key , String values);
    }
}
