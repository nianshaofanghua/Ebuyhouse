package com.yidankeji.cheng.ebuyhouse.community.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.community.mode.AddLabelListModel;

import java.util.ArrayList;

/**
 * Created by ${syj} on 2018/4/16.
 */

public class LabelListAdapter extends BaseAdapter {
    ArrayList<AddLabelListModel> mList;
    Context mContext;


    public LabelListAdapter(ArrayList<AddLabelListModel> list, Context context) {
        mList = list;
        mContext = context;

    }

    public void setList(ArrayList<AddLabelListModel> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.socail_friend_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.tv_userName.setText(mList.get(position).getLabel());

        viewHolder.iv_user.setImageResource(R.mipmap.remave_label);
        viewHolder.iv_user.setOval(false);
        viewHolder.iv_user.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        viewHolder.iv_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListen != null) {
                    mOnClickListen.OnClick(v, position);
                }
            }
        });
        viewHolder.tv_firstWord.setVisibility(View.GONE);
        return convertView;
    }

    private class ViewHolder {
        TextView tv_firstWord, tv_userName;
        RoundedImageView iv_user;

        public ViewHolder(View view) {
            iv_user = view.findViewById(R.id.iv_user);
            tv_firstWord = view.findViewById(R.id.tv_first_word);
            tv_userName = view.findViewById(R.id.tv_user);
        }
    }

    OnClickListen mOnClickListen;

    public void setOnClickListen(OnClickListen onClickListen) {
        mOnClickListen = onClickListen;
    }

    public interface OnClickListen {
        void OnClick(View view, int position);

    }

}
