package com.yidankeji.cheng.ebuyhouse.community.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.community.mode.SocialFriendBean;

import java.util.ArrayList;

/**
 * Created by ${syj} on 2018/4/11.
 */

public class FirendListAdapter extends BaseAdapter {
    private ArrayList<SocialFriendBean.ContentBean.RowsBean> mList;
    private Context mContext;
    private int type;

    public FirendListAdapter(ArrayList<SocialFriendBean.ContentBean.RowsBean> list, Context context, int type) {
        mList = list;
        mContext = context;
        this.type = type;
    }


    public void setList(ArrayList<SocialFriendBean.ContentBean.RowsBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void setType(int type) {
        this.type = type;
        notifyDataSetChanged();
    }

    public int getType() {
        return type;
    }

    @Override
    public int getCount() {
        if (type == 0) {
            return mList.size();
        } else {
            return mList.size() + 2;
        }

    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.interest_friend_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position >= mList.size() && mList.size() != 0) {

            if (position == mList.size()) {
                Glide.with(mContext).load(R.mipmap.add_pic).into(viewHolder.iv_user_pic);
                viewHolder.iv_user_pic.setOval(false);
                viewHolder.iv_user_pic.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else {
                Glide.with(mContext).load(R.mipmap.minus_pic).into(viewHolder.iv_user_pic);
                viewHolder.iv_user_pic.setOval(false);
                viewHolder.iv_user_pic.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            }


        } else {

        if(mList.size()!=0){
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.error(R.mipmap.touxiang);
            Glide.with(mContext).load(mList.get(position).getHead_url()).apply(requestOptions).into(viewHolder.iv_user_pic);
            viewHolder.iv_user_pic.setOval(true);
            viewHolder.iv_user_pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        }
        return convertView;
    }


    public interface OnClickItem {
        void onClick(View view, int position);
    }

    private class ViewHolder {
        RoundedImageView iv_user_pic;

        public ViewHolder(View view) {
            iv_user_pic = view.findViewById(R.id.firend_icon);
        }
    }
}
