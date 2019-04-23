package com.yidankeji.cheng.ebuyhouse.community.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.community.mode.SearchSocailModel;

import java.util.ArrayList;

/**
 * Created by ${syj} on 2018/4/9.
 */

public class SearchListAdapter extends BaseAdapter {
    ArrayList<SearchSocailModel.ContentBean.RowsBean> mList;
    Context mContext;

    public SearchListAdapter(ArrayList<SearchSocailModel.ContentBean.RowsBean> listModels, Context context) {
        mList = listModels;
        mContext = context;

    }

    public void setList(ArrayList<SearchSocailModel.ContentBean.RowsBean> list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_search_social, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(mList.get(position).getName());
        viewHolder.address.setText(mList.get(position).getAddress());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.mipmap.touxiang);
        Glide.with(mContext).load(mList.get(position).getHead_url()).apply(requestOptions).into(viewHolder.pic);

        return convertView;
    }


    private class ViewHolder {
        TextView name, address;
        ImageView pic;

        public ViewHolder(View view) {
            name = view.findViewById(R.id.name);
            pic = view.findViewById(R.id.pic);
            address = view.findViewById(R.id.address);
        }
    }
}
