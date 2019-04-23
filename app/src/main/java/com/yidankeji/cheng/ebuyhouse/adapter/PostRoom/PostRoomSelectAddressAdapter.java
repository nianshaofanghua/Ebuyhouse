package com.yidankeji.cheng.ebuyhouse.adapter.PostRoom;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.mode.PostRoom.PostRoomSelectAddressMode;

import java.util.ArrayList;

/**
 * 上传房屋信息
 *      地址筛选
 *      item view复用的PostRoomActivity的adapter
 */

public class PostRoomSelectAddressAdapter extends RecyclerView.Adapter<PostRoomSelectAddressAdapter.ViewHolder>{

    private Activity activity;
    private ArrayList<PostRoomSelectAddressMode> list;
    private String tag;

    public PostRoomSelectAddressAdapter(Activity activity, ArrayList<PostRoomSelectAddressMode> list, String tag) {
        this.activity = activity;
        this.list = list;
        this.tag = tag;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_postroom , parent , false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (tag.equals("city")){
            String city = list.get(position).getCity();
            if (city == null){
                city = "";
            }
            holder.name.setText(list.get(position).getCity());
        }else{
            String state = list.get(position).getState();
            if (state == null){
                state = "";
            }
            holder.name.setText(list.get(position).getState());
        }
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listening != null){
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
