package com.yidankeji.cheng.ebuyhouse.community.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.community.mode.SocialDetailListModel;

import java.util.ArrayList;

/**
 * Created by ${syj} on 2018/4/9.
 */

public class SocailDetailAdapter extends BaseAdapter {
    private ArrayList<SocialDetailListModel> mList;
    private Context mContext;

    public SocailDetailAdapter(ArrayList<SocialDetailListModel> detailModels, Context context) {
        mList = detailModels;
        mContext = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.social_detail_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(mList.get(position).getTitle());
        viewHolder.detail.setText(mList.get(position).getDetail());


        return convertView;
    }

    private class ViewHolder {
        private TextView title,detail;

        public ViewHolder(View view) {
            title =view.findViewById(R.id.title);
            detail = view.findViewById(R.id.detail);
        }
    }
}
