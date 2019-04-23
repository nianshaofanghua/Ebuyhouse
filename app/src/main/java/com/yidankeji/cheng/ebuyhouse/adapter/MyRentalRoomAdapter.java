package com.yidankeji.cheng.ebuyhouse.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.mode.ShowListMode;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017\12\5 0005.
 */

public class MyRentalRoomAdapter extends RecyclerView.Adapter<MyRentalRoomAdapter.ViewHolder>{

    private Context context;
    private ArrayList<ShowListMode> list;

    public MyRentalRoomAdapter(Context context, ArrayList<ShowListMode> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_myrentalroomlayout , parent , false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        ShowListMode mode = list.get(position);
        Glide.with(context).load(mode.getImg_url()).apply(MyApplication.getOptions_square()).into(holder.imageView);
        boolean isEdit = mode.isEdit();
        if (isEdit){
            boolean select = mode.isSelect();
            if (select){
                holder.state.setImageResource(R.mipmap.house_selected);
            }else{
                holder.state.setImageResource(R.mipmap.house_select);
            }
        }else{
            holder.state.setImageResource(R.mipmap.jiantou_right);
        }
        if(mode.getRelease_type().equals("rent")){
            holder.money.setText("$"+mode.getPrice()+"/MO");

        }else {
            holder.money.setText("$"+mode.getPrice());

        }

        holder.canshu1.setText(mode.getBedroom()+"beds."+mode.getBathroom()+"baths."+mode.getPost_livesqft()+"sqft");
        holder.canshu2.setText(mode.getStreet());
        holder.canshu3.setText(mode.getCity_name()+" "+mode.getState_name());

        holder.layout.setOnClickListener(new View.OnClickListener() {
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

        LinearLayout layout;
        ImageView imageView , state;
        TextView canshu1 , canshu2 , money ,canshu3;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = ((LinearLayout) itemView.findViewById(R.id.item_myrentalroom_layout));
            imageView = ((ImageView) itemView.findViewById(R.id.item_myrentalroom_image));
            money = ((TextView) itemView.findViewById(R.id.item_myrentalroom_money));
            canshu1 = ((TextView) itemView.findViewById(R.id.item_myrentalroom_canshu1));
            canshu2 = ((TextView) itemView.findViewById(R.id.item_myrentalroom_canshu2));
            canshu3 = ((TextView) itemView.findViewById(R.id.item_myrentalroom_canshu3));
            state = (ImageView)itemView.findViewById(R.id.item_myrentalroom_jiantou);
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
