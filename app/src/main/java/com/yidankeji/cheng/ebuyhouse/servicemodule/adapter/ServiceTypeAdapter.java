package com.yidankeji.cheng.ebuyhouse.servicemodule.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.servicemodule.mode.ServiceTypeMode;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;

import java.util.ArrayList;

/**
 *
 */

public class ServiceTypeAdapter extends RecyclerView.Adapter<ServiceTypeAdapter.ViewHolder>{

    private Activity activity;
    private ArrayList<ServiceTypeMode> list;
    private int tag;

    public ServiceTypeAdapter(Activity activity, ArrayList<ServiceTypeMode> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_servicetype , parent , false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        setHeight(holder.textView);
        holder.textView.setText(list.get(position).getName());
        if (tag == position){
            holder.textView.setTextColor(Color.parseColor("#f85252"));
        }else{
            holder.textView.setTextColor(Color.parseColor("#333333"));
        }
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listening != null){
                    listening.getView(v , position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_servicetype_text);
        }
    }

    public void setHeight(TextView textView){
        ViewGroup.LayoutParams params = textView.getLayoutParams();
        params.width = (MyApplication.Equipment_width)/4;
        textView.setLayoutParams(params);
    }
    public void setTag(int tag1){
        tag = tag1;
    }

    public interface OnItemClickListening{
        void getView(View view , int position);
    }
    public OnItemClickListening listening;

    public OnItemClickListening getListening() {
        return listening;
    }

    public void setListening(OnItemClickListening listening) {
        this.listening = listening;
    }
}
