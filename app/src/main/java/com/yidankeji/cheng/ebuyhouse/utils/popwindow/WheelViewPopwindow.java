package com.yidankeji.cheng.ebuyhouse.utils.popwindow;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
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
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

import java.util.ArrayList;

/**
 * 滚筒选择器工具类
 */

public class WheelViewPopwindow {

    private Activity activity;
    ArrayList<FilterCanShuMode> list;
    private PopupWindow pop;

    public WheelViewPopwindow(Activity activity , ArrayList<FilterCanShuMode> list) {
        this.activity = activity;
        this.list = list;
    }

    /**
     * 获取两列的
     */
    public void getTwoLinesWheelView(String tag){
        ArrayList<String> listson = new ArrayList<>();
        for (int i = 0; i < list.size() ; i++) {
            listson.add(list.get(i).getKey());
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
            }
        });

        initView(view , listson , listson, tag);
    }

    private void initView(View view , ArrayList<String> listleft , ArrayList<String> listright , final String tag) {
        final WheelView wheelView01 = (WheelView) view.findViewById(R.id.wheelview_twoline_one);
        wheelView01.setWheelAdapter(new ArrayWheelAdapter(activity));
        wheelView01.setLoop(true);
        wheelView01.setWheelData(listleft);
        wheelView01.setSkin(WheelView.Skin.Holo);
        wheelView01.setWheelSize(5);
        final WheelView wheelView02 = (WheelView) view.findViewById(R.id.wheelview_twoline_two);
        wheelView02.setWheelAdapter(new ArrayWheelAdapter(activity));
        wheelView02.setLoop(true);
        wheelView02.setWheelData(listright);
        wheelView02.setSkin(WheelView.Skin.Holo);
        wheelView02.setWheelSize(5);

        Button cancel = (Button) view.findViewById(R.id.wheelview_twoline_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
        Button ok = (Button) view.findViewById(R.id.wheelview_twoline_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item1 = (String) wheelView01.getSelectionItem();
                String item2 = (String) wheelView02.getSelectionItem();

                for (int i = 0; i < list.size() ; i++) {
                    String key = list.get(i).getKey();
                    if (key.equals(item1)){
                        item1 = list.get(i).getValues();
                    }
                    if (key.equals(item2)){
                        item2 = list.get(i).getValues();
                    }
                }
                if (listen != null){
                    listen.getView(tag , item1+" "+item2);
                }
            }
        });
    }

    public interface WheelViewOKClickListen{
        void getView(String tag , String values);
    }
    public static WheelViewOKClickListen listen;

    public static void getListening(WheelViewOKClickListen liste){
        listen = liste;
    }
}
