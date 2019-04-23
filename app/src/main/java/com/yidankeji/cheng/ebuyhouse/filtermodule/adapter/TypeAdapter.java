package com.yidankeji.cheng.ebuyhouse.filtermodule.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.filtermodule.mode.TypeMode;
import com.yidankeji.cheng.ebuyhouse.utils.interfaceUtils.InterfaceUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018\1\10 0010.
 */

public class TypeAdapter extends BaseAdapter{

    private Activity activity;
    private ArrayList<TypeMode> list;
    private InterfaceUtils.OnListItemClickListening listening;

    public TypeAdapter(Activity activity, ArrayList<TypeMode> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_filtertype , parent , false);
            holder.title = (TextView) convertView.findViewById(R.id.item_filtertype_title);
            holder.values = (TextView) convertView.findViewById(R.id.item_filtertype_values);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        TypeMode typeMode = list.get(position);
        holder.title.setText(typeMode.getTitle());
        holder.values.setText(typeMode.getValues());

        holder.values.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listening != null){
                    listening.onListItemClickListening(v , position);
                }
            }
        });
        return convertView;
    }

    class ViewHolder{
        TextView title  , values;
    }

    public InterfaceUtils.OnListItemClickListening getListening() {
        return listening;
    }

    public void setListening(InterfaceUtils.OnListItemClickListening listening) {
        this.listening = listening;
    }
}
