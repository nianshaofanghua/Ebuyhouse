package com.yidankeji.cheng.ebuyhouse.utils.popwindow;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.housemodule.mode.StateMode;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018\1\19 0019.
 */

public class SelectStatePop implements View.OnClickListener{

    private Activity activity;
    private ArrayList<StateMode> list;
    public OnButClickListening listening;
    private PopupWindow pop;
    private WheelView wheelView;

    public SelectStatePop(Activity activity ,ArrayList<StateMode> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.selectstate_cancel:
                pop.dismiss();
                break;
            case R.id.selectstate_ok:
                pop.dismiss();
                String itemString = (String) wheelView.getSelectionItem();
                for (int i = 0; i < list.size() ; i++) {
                    StateMode stateMode = list.get(i);
                    if (stateMode.getState().equals(itemString)){
                        listening.onButClickListening(true , stateMode);
                        break;
                    }
                }
                break;
        }
    }

    public interface OnButClickListening{
        void onButClickListening(boolean tag , StateMode mode);
    }

    public void getPop(OnButClickListening listen){
        listening = listen;
        View view = LayoutInflater.from(activity).inflate(R.layout.pop_selectstate, null);
        pop = new PopupWindow( view, LinearLayout.LayoutParams.MATCH_PARENT , LinearLayout.LayoutParams.WRAP_CONTENT , true);
        pop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pop.setAnimationStyle(R.style.dialogWindowAnim);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
   //    WindowUtils.setBg(activity , 0.7f);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.showAtLocation(view , Gravity.BOTTOM , 0 , 0);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowUtils.setBg(activity , 1f);
            }
        });

        initData(view);
    }

    private void initData(View view) {
        ArrayList<String> listson = new ArrayList<>();
        for (int i = 0; i < list.size() ; i++) {
            listson.add(list.get(i).getState());
        }
        TextView tv_cancel = (TextView) view.findViewById(R.id.selectstate_cancel);
        tv_cancel.setOnClickListener(this);
        TextView tv_ok = (TextView) view.findViewById(R.id.selectstate_ok);
        tv_ok.setOnClickListener(this);
        wheelView = (WheelView) view.findViewById(R.id.selectstate_wheelview);
        wheelView.setWheelAdapter(new ArrayWheelAdapter(activity));
        wheelView.setLoop(false);
        wheelView.setWheelData(listson);
        wheelView.setSkin(WheelView.Skin.Holo);
        wheelView.setWheelSize(5);
    }
}
