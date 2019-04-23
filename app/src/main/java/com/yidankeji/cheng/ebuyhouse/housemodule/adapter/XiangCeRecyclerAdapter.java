package com.yidankeji.cheng.ebuyhouse.housemodule.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018\1\16 0016.
 */

public class XiangCeRecyclerAdapter extends RecyclerView.Adapter<XiangCeRecyclerAdapter.ViewHolder>{

    private Activity activity;
    private ArrayList<String> list;

    public XiangCeRecyclerAdapter(Activity activity, ArrayList<String> list) {
        this.activity = activity;
        this.list = list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_xiangce_layout , parent , false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        setHeight(holder.image);
        if (position < list.size()){
            holder.delect.setVisibility(View.VISIBLE);
            Glide.with(activity).load(list.get(position)).apply(MyApplication.getOptions_square()).into( holder.image);
        }else{
            holder.image.setImageResource(R.mipmap.xuqiudan_xiangce);
            holder.delect.setVisibility(View.GONE);
        }

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (listeming != null){
                   listeming.onXiangCeOnItemClickListeming(v , position);
               }
            }
        });
        holder.delect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listeming != null){
                    listeming.onXiangCeOnItemClickListeming(v , position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (list.size())+1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image ,delect;
        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.item_selectxiangce_image);
            delect = (ImageView) itemView.findViewById(R.id.item_selectxiangce_delete);
        }
    }

    public void setHeight(ImageView ima){
        ViewGroup.LayoutParams params = ima.getLayoutParams();
        params.height=(WindowUtils.getWeight(activity)/4)-20;
        params.width =(WindowUtils.getWeight(activity)/4)-20;
        ima.setLayoutParams(params);
    }

    public interface XiangCeOnItemClickListeming{
        void onXiangCeOnItemClickListeming(View view , int position);
    }

    public XiangCeOnItemClickListeming listeming;

    public XiangCeOnItemClickListeming getListeming() {
        return listeming;
    }

    public void setListeming(XiangCeOnItemClickListeming listeming) {
        this.listeming = listeming;
    }
}