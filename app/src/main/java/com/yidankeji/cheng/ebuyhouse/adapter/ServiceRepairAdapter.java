package com.yidankeji.cheng.ebuyhouse.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.mode.RepairMode;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017\12\6 0006.
 */

public class ServiceRepairAdapter extends RecyclerView.Adapter<ServiceRepairAdapter.ViewHolder>{

    private Context context;
    private ArrayList<RepairMode> list;

    public ServiceRepairAdapter(Context context, ArrayList<RepairMode> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_servicerepair_layout , parent , false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText("Love apartment");
        if (position % 3 == 0){
            holder.iamge.setImageResource(R.mipmap.bbb01);
        }else if (position % 3 == 1){
            holder.iamge.setImageResource(R.mipmap.bbb02);
        }else if (position % 3 == 2){
            holder.iamge.setImageResource(R.mipmap.bbbb03);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name ;
        ImageView iamge;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.item_servicerepair_name);
            iamge = ((ImageView) itemView.findViewById(R.id.item_servicerepair_iamge));
        }
    }
}
