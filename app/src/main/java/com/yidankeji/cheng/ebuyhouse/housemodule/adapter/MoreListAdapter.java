package com.yidankeji.cheng.ebuyhouse.housemodule.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.housemodule.mode.MoreListModel;

import java.util.ArrayList;

/**
 * Created by ${syj} on 2018/3/14.
 */

public class MoreListAdapter extends BaseAdapter {

    ArrayList<MoreListModel> mList;
    Context mContext;

    public MoreListAdapter(ArrayList<MoreListModel> list, Context context) {
        mList = list;
        mContext = context;
    }

    public void setList(ArrayList<MoreListModel> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.more_item_morelist, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(mList.get(position).getTitle()==null||mList.get(position).getTitle().isEmpty()){
            viewHolder.mTitle.setVisibility(View.GONE);
            viewHolder.mItem.setText(mList.get(position).getItem());
        }else {
            viewHolder.mTitle.setVisibility(View.VISIBLE);
            viewHolder.mTitle.setTextSize(20f);
            viewHolder.mTitle.setText(mList.get(position).getTitle());
            viewHolder.mItem.setVisibility(View.GONE);
        }
        if (position % 2 == 0){
            viewHolder.mLl.setBackgroundColor(Color.parseColor("#F2F2F2"));
        }else{
            viewHolder.mLl.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        Log.e("hight","highthighthight"+convertView.getHeight());
        return convertView;
    }

    private class ViewHolder {
        private View mView;
        TextView mTitle, mItem;
LinearLayout mLl;
        public ViewHolder(View view) {

            mView = view;
            mTitle = (TextView) mView.findViewById(R.id.title);
            mItem = (TextView) mView.findViewById(R.id.item);
            mLl = (LinearLayout) mView.findViewById(R.id.layout_bag);
        }

    }
}
