package com.yidankeji.cheng.ebuyhouse.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.mode.FilterMode;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017\12\14 0014.
 */

public class FilterHouseTypeAdapter extends RecyclerView.Adapter<FilterHouseTypeAdapter.ViewHolder> {

    private Context context;
    private ArrayList<FilterMode> list;

    public FilterHouseTypeAdapter(Context context, ArrayList<FilterMode> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_filterhousetype, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.name.setText(list.get(position).getType());
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listening != null) {
                    listening.onItemClickListening(v, position);
                }
            }
        });
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            name = ((TextView) itemView.findViewById(R.id.item_housetype_content));
            img = (ImageView) itemView.findViewById(R.id.img_pic);
        }
    }

    public interface OnItemClickListening {
        void onItemClickListening(View view, int position);
    }

    public OnItemClickListening listening;

    public OnItemClickListening getListening() {
        return listening;
    }

    public void setListening(OnItemClickListening listening) {
        this.listening = listening;
    }
}
