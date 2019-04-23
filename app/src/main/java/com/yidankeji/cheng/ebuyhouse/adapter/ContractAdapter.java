package com.yidankeji.cheng.ebuyhouse.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.offermodule.InformationActivity;
import com.yidankeji.cheng.ebuyhouse.mode.ContractMode;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017\12\5 0005.
 */

public class ContractAdapter extends RecyclerView.Adapter<ContractAdapter.ViewHolder>{

    private Context context;
    private ArrayList<ContractMode> list;

    public ContractAdapter(Context context, ArrayList<ContractMode> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contractlayout ,parent , false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ContractMode contractMode = list.get(position);
        holder.name.setText(contractMode.getName());
        holder.time.setText(contractMode.getTime());
        holder.content01.setText(contractMode.getContent01());
        holder.content02.setText(contractMode.getContent02());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context , InformationActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name , time , content01 , content02 , view ;

        public ViewHolder(View itemView) {
            super(itemView);
            name = ((TextView) itemView.findViewById(R.id.item_contractlayout_name));
            time = ((TextView) itemView.findViewById(R.id.item_contractlayout_time));
            content01 = ((TextView) itemView.findViewById(R.id.item_contractlayout_content01));
            content02 = ((TextView) itemView.findViewById(R.id.item_contractlayout_content02));
            view = ((TextView) itemView.findViewById(R.id.item_contractlayout_viewbut));
        }
    }
}
