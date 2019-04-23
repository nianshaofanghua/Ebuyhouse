package com.yidankeji.cheng.ebuyhouse.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.servicemodule.mode.ServiceTabMode;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017\11\30 0030.
 */

public class MoreIconAdapter extends RecyclerView.Adapter<MoreIconAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ServiceTabMode> iconList;

    public MoreIconAdapter(Context context, ArrayList<ServiceTabMode> iconList) {
        this.context = context;
        this.iconList = iconList;
    }

    public void setIconList(ArrayList<ServiceTabMode> iconList) {
        this.iconList = iconList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_moreicon, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ServiceTabMode serviceTabMode = iconList.get(position);
        holder.text.setText(serviceTabMode.getTitle());
        holder.image.setImageResource(serviceTabMode.getLogo());

        Log.e("more","serviceTa"+serviceTabMode.getNum());
        if (position == 5) {
            Log.e("more","serviceTabMode"+serviceTabMode.getNum());
            if (serviceTabMode.getNum() != 0) {
                if(serviceTabMode.getNum()>99){
                  //  holder.mMessageNum.setText( "99+");
                }else {
                //    holder.mMessageNum.setText(serviceTabMode.getNum() + "");
                }
                holder.mMessageNum.setVisibility(View.VISIBLE);

            }else {
                holder.mMessageNum.setVisibility(View.GONE);
            }

        }else {
            holder.mMessageNum.setVisibility(View.GONE);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListening.OnItemClickListening(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return iconList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout layout;
        ImageView image;
        TextView text;
        TextView mMessageNum;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = ((RelativeLayout) itemView.findViewById(R.id.item_moreicon_layout));
            image = ((ImageView) itemView.findViewById(R.id.item_moreicon_iamge));
            text = (TextView) itemView.findViewById(R.id.item_moreicon_title);
            mMessageNum = (TextView) itemView.findViewById(R.id.message_num);
        }
    }

    public interface OnItemClickListening {
        void OnItemClickListening(View view, int position);
    }

    public OnItemClickListening onItemClickListening;

    public OnItemClickListening getOnItemClickListening() {
        return onItemClickListening;
    }

    public void setOnItemClickListening(OnItemClickListening onItemClickListening) {
        this.onItemClickListening = onItemClickListening;
    }
}
