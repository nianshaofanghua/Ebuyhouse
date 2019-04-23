package com.yidankeji.cheng.ebuyhouse.servicemodule.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.servicemodule.mode.ServiceListMode;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017\12\26 0026.
 */

public class SonServiceListAdapter extends RecyclerView.Adapter<SonServiceListAdapter.ViewHolder>{

    private Activity activity;
    private ArrayList<ServiceListMode> list;

    public SonServiceListAdapter(Activity activity, ArrayList<ServiceListMode> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_sonservicelist , parent , false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String img_url = list.get(position).getImg_url();
        Glide.with(activity).load(img_url).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            image = ((ImageView) itemView.findViewById(R.id.item_sonservicelist));
        }
    }
}
