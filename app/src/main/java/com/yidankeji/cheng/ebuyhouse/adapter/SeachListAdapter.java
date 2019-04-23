package com.yidankeji.cheng.ebuyhouse.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.mode.SeachMode;

import java.util.ArrayList;

/**
 * Created by cheng on 2017\11\29 0029.
 *
 *  搜索：联想词
 */

public class SeachListAdapter extends RecyclerView.Adapter<SeachListAdapter.ViewHolder>{

    private Context context;
    private ArrayList<SeachMode> seachList;

    public SeachListAdapter(Context context, ArrayList<SeachMode> seachList) {
        this.context = context;
        this.seachList = seachList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_seachlist , parent , false);
        ViewHolder holder= new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        SeachMode seachMode = seachList.get(position);
        String type = seachMode.getType();
        switch (type){
            case "city":
                holder.image.setImageResource(R.mipmap.seach_city);
                break;
            case "state":
                holder.image.setImageResource(R.mipmap.seach_state);
                break;
            case "street":
                holder.image.setImageResource(R.mipmap.seach_street);
                break;
        }
        holder.content.setText(seachMode.getKeyword());
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickFromSeach != null){
                    onItemClickFromSeach.OnItemClickFromSeach(v , position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return seachList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView content;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            content = (TextView)itemView.findViewById(R.id.item_seachlist_content);
            image = ((ImageView) itemView.findViewById(R.id.item_seachlist_image));
        }
    }

    public interface OnItemClickFromSeach{
        void OnItemClickFromSeach(View view , int position);
    }
    public OnItemClickFromSeach onItemClickFromSeach;

    public OnItemClickFromSeach getOnItemClickFromSeach() {
        return onItemClickFromSeach;
    }

    public void setOnItemClickFromSeach(OnItemClickFromSeach onItemClickFromSeach) {
        this.onItemClickFromSeach = onItemClickFromSeach;
    }
}
