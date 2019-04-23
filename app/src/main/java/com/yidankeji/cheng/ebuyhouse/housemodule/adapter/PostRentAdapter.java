package com.yidankeji.cheng.ebuyhouse.housemodule.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.adapter.FilterHouseTypeAdapter;
import com.yidankeji.cheng.ebuyhouse.housemodule.mode.PostRentListModel;
import com.yidankeji.cheng.ebuyhouse.myinfomodule.activity.ContractMessageAdapter;

import java.util.ArrayList;

/**
 * Created by ${syj} on 2018/3/14.
 */

public class PostRentAdapter extends RecyclerView.Adapter<PostRentAdapter.ViewHolder> {
    ArrayList<PostRentListModel.ContentBean.RowsBean.AttrValueListBean> mList;
    Context mContext;

    public PostRentAdapter(ArrayList<PostRentListModel.ContentBean.RowsBean.AttrValueListBean> list, Context context) {
        mList = list;
        mContext = context;

    }

    public void setList(ArrayList<PostRentListModel.ContentBean.RowsBean.AttrValueListBean> list) {
        mList = list;
        notifyDataSetChanged();
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View  convertView = LayoutInflater.from(mContext).inflate(R.layout.salerentfilter_list_item, parent,false);
        ViewHolder viewHolder = new ViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTitle.setText(mList.get(position).getValue_name());
      Log.e("adapter","logzz"+position+"----"+mList.get(position).isChose());
        if(mList.get(position).isChose()){
            holder.mChose.setVisibility(View.VISIBLE);
        }else {
            holder.mChose.setVisibility(View.GONE);
        }
        holder.mRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListening.onItemClickListening(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public static  class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTitle;
        ImageView mChose;
        RelativeLayout mRl;


        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.name);
            mChose = (ImageView) itemView.findViewById(R.id.chose);
            mRl = (RelativeLayout) itemView.findViewById(R.id.rl);
        }
    }

    public interface OnItemClickListening {
        void onItemClickListening(View view, int position);
    }

   public OnItemClickListening mOnItemClickListening ;

     OnItemClickListening getOnItemClickListening() {
        return mOnItemClickListening;
    }

    public void setOnItemClickListening(OnItemClickListening onItemClickListening) {
        mOnItemClickListening = onItemClickListening;
    }
}
