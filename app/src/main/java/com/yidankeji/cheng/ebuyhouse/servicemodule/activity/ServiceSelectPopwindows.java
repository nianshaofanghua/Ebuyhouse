package com.yidankeji.cheng.ebuyhouse.servicemodule.activity;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.servicemodule.adapter.PopServiceTypeAdapter;
import com.yidankeji.cheng.ebuyhouse.servicemodule.mode.ServiceTypeMode;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017\12\26 0026.
 */

public class ServiceSelectPopwindows implements View.OnClickListener{

    private Activity activity;
    private ServiceTypeMode mode;
    private PopupWindow pop;
    private PopServiceTypeAdapter adapter;
    private String str = "";

    public ServiceSelectPopwindows(Activity activity ,ServiceTypeMode mode) {
        this.activity = activity;
        this.mode = mode;
    }

    public void getDialog(final OnPopDismissListening listening){
        str = "";
        View view = LayoutInflater.from(activity).inflate(R.layout.pop_servicetype , null);
        pop = new PopupWindow( view, LinearLayout.LayoutParams.WRAP_CONTENT , LinearLayout.LayoutParams.WRAP_CONTENT , true);
        pop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pop.setAnimationStyle(R.style.dialogWindowAnim);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        WindowUtils.setBg(activity , 0.7f);
        pop.showAtLocation(view , Gravity.CENTER , 0 , 0);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (listening != null){
                    listening.getView(str);
                }
                WindowUtils.setBg(activity , 1f);
            }
        });

        initView(view);
    }

    private void initView(View view) {
        TextView title = (TextView) view.findViewById(R.id.pop_servicetype_title);
        title.setText(mode.getName());
        TextView cancel = (TextView) view.findViewById(R.id.pop_servicetype_cancel);
        cancel.setOnClickListener(this);
        TextView apply = (TextView) view.findViewById(R.id.pop_servicetype_apply);
        apply.setOnClickListener(this);
        ListView recyclerView = (ListView) view.findViewById(R.id.pop_servicetype_recyclerview);
        adapter = new PopServiceTypeAdapter(activity , mode.getList());
        recyclerView.setAdapter(adapter);
        if (mode.getList().size() <= 5){
            WindowUtils.setListViewHeightBasedOnChildren(recyclerView);
        }
        recyclerView.setOnItemClickListener(listener);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pop_servicetype_cancel:
                getData();
                break;
            case R.id.pop_servicetype_apply:
                getData();
                break;
        }
    }

    private void getData(){
        ArrayList<ServiceTypeMode> list = mode.getList();
        for (int i = 0; i < list.size(); i++) {
            ServiceTypeMode mode = list.get(i);
            if (mode.isSelect()){
                if (str.endsWith("")){
                    str =  mode.getSub_id();
                }else{
                    str = str+","+mode.getSub_id();
                }
            }
        }
        pop.dismiss();
    }

    ListView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ArrayList<ServiceTypeMode> list = mode.getList();
            ServiceTypeMode serviceTypeMode = list.get(position);
            if (serviceTypeMode.isSelect()){
                serviceTypeMode.setSelect(false);
            }else{
                serviceTypeMode.setSelect(true);
            }
            adapter.notifyDataSetChanged();
        }
    };

    public interface OnPopDismissListening{
        void getView(String str);
    }
}
