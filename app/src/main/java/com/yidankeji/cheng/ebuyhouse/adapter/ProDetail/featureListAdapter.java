package com.yidankeji.cheng.ebuyhouse.adapter.ProDetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.mode.ProDetail.ProCanShuMode;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017\12\21 0021.
 */

public class featureListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<ProCanShuMode> list;
    private String TAG;

    public featureListAdapter(Activity activity, ArrayList<ProCanShuMode> list, String tag) {
        this.activity = activity;
        this.list = list;
        this.TAG = tag;
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_prodetail_canshu, null);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.item_productdetail_canshu_layout);
            holder.name = ((TextView) convertView.findViewById(R.id.item_productdetail_canshu_name));
            holder.values = ((TextView) convertView.findViewById(R.id.item_productdetail_canshu_values));

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == 0) {
            if (TAG.equals("aa")) {
                holder.name.setText(list.get(position).getCanshuname());
            } else {
                holder.name.setText(list.get(position).getCanshuname());
            }
            holder.name.setTextSize(20f);
            holder.layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            if (position % 2 == 0) {
                holder.layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            } else {
                holder.layout.setBackgroundColor(Color.parseColor("#F2F2F2"));
            }
            String canshuname = list.get(position).getCanshuname();
            if (canshuname.contains("=")) {
                String[] split = canshuname.split("=");
                if(split.length==1){
                    holder.name.setText(split[0]);
                    holder.values.setText("");
                }else {
                    holder.name.setText(split[0]);
                    holder.values.setText(split[1]);
                }

            }
        }
        if (holder.name.getText().toString().equals("Kitchen")) {
            holder.name.setText("Kitchens");
        }
        return convertView;
    }

    class ViewHolder {
        TextView name, values;
        LinearLayout layout;
    }
}
