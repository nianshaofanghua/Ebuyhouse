package com.yidankeji.cheng.ebuyhouse.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.mode.PostRoom.PostRoomMode;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017\12\15 0015.
 */

public class PostRoomAdapter extends RecyclerView.Adapter<PostRoomAdapter.ViewHolder>{

    private Context context;
    private ArrayList<PostRoomMode> list;

    public PostRoomAdapter(Context context, ArrayList<PostRoomMode> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(ArrayList<PostRoomMode> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_postroom , parent , false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.name.setText(list.get(position).getName());
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listening != null){
                    listening.onItemClickListening(v , position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = ((TextView) itemView.findViewById(R.id.item_postroom_name));
        }
    }

    public interface OnItemClickListening{
        void onItemClickListening(View view , int position);
    }
    public OnItemClickListening listening;

    public OnItemClickListening getListening() {
        return listening;
    }

    public void setListening(OnItemClickListening listening) {
        this.listening = listening;
    }
}
