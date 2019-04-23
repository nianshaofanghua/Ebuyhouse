package com.yidankeji.cheng.ebuyhouse.servicemodule.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.servicemodule.mode.ServiceTypeMode;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017\12\27 0027.
 */

public class PopServiceTypeAdapter extends BaseAdapter{

    private Activity activity;
    private ArrayList<ServiceTypeMode> list;

    public PopServiceTypeAdapter(Activity activity, ArrayList<ServiceTypeMode> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_pop_servicetype , parent , false);
            holder.logo = ((ImageView) convertView.findViewById(R.id.item_pop_servicetype_logo));
            holder.select = ((ImageView) convertView.findViewById(R.id.item_pop_servicetype_select));
            holder.title = ((TextView) convertView.findViewById(R.id.item_pop_servicetype_title));
            holder.layout = ((LinearLayout) convertView.findViewById(R.id.item_pop_servicetype_layout));
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        ServiceTypeMode mode = list.get(position);
        holder.title.setText(mode.getSub_name());
        Glide.with(activity).load(mode.getSub_img_url()).into(holder.logo);
        if (mode.isSelect()){
            holder.select.setVisibility(View.VISIBLE);
        }else{
            holder.select.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder{
        ImageView logo , select;
        TextView title;
        LinearLayout layout;
    }


}
