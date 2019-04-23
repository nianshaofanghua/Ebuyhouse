package com.yidankeji.cheng.ebuyhouse.community.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.mode.PostRoom.PostRoomMode;

import java.util.ArrayList;

/**
 * Created by ${syj} on 2018/4/13.
 */

public class HintListAdapter extends BaseAdapter {

   private ArrayList<PostRoomMode> mList;
    private Context mContext;

    public HintListAdapter(ArrayList<PostRoomMode> list, Context context) {
        mList = list;
        mContext = context;

    }

    public void setList(ArrayList<PostRoomMode> list) {
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
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_postroom,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.name.setText(mList.get(position).getName());

        return convertView;
    }

    private class ViewHolder{
        TextView name;

        public ViewHolder(View view) {
            name =view.findViewById(R.id.item_postroom_name);
        }
    }
}
