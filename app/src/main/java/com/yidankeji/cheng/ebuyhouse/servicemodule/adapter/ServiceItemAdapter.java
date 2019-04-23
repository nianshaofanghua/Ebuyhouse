package com.yidankeji.cheng.ebuyhouse.servicemodule.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.servicemodule.mode.ServiceListMode;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017\12\27 0027.
 */

public class ServiceItemAdapter extends RecyclerView.Adapter<ServiceItemAdapter.ViewHolder>{

    private Activity activity;
    private ArrayList<ServiceListMode> list;

    public ServiceItemAdapter(Activity activity, ArrayList<ServiceListMode> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_serviceitem, parent , false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ServiceListMode mode = list.get(position);
        Glide.with(activity).load(mode.getImg_url()).into(holder.imageView);
        holder.textView.setText(mode.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = ((ImageView) itemView.findViewById(R.id.item_serviceitem_image));
            textView = ((TextView) itemView.findViewById(R.id.item_serviceitem_text));
        }
    }
}
