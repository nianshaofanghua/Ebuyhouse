package com.yidankeji.cheng.ebuyhouse.community.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;

import java.util.ArrayList;

/**
 * Created by ${syj} on 2018/4/12.
 */

public class ConstellAdapter extends BaseAdapter {

    private ArrayList<String> mList;
    private Context mContext;

    public ConstellAdapter(ArrayList<String> list, Context context) {
        mList = list;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.constell_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(mList.get(position));
        return convertView;
    }

    private class ViewHolder {
        TextView tv_name;

        public ViewHolder(View view) {
            tv_name = view.findViewById(R.id.tv_name);
        }
    }
}
