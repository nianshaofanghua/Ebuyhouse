package com.yidankeji.cheng.ebuyhouse.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017\12\15 0015.
 */

public class XiangCeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> list;

    public XiangCeAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return position!=list.size()?list.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
//        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_xiangce_image , parent , false);
            holder.image = (ImageView) convertView.findViewById(R.id.item_xiangce_image);
            holder.delect = (ImageView) convertView.findViewById(R.id.item_xiangce_delete);
            holder.layout = (RelativeLayout)convertView.findViewById(R.id.item_xiangce_layout);
        convertView.setTag(holder);
//        }else{
//            holder = (ViewHolder) convertView.getTag();
//        }

        setHeight(holder.layout);

        if (position < list.size()){
            holder.delect.setVisibility(View.VISIBLE);
            Glide.with(context).load(list.get(position)).apply(MyApplication.getOptions_square()).into( holder.image);
        }else{
            holder.image.setImageResource(R.mipmap.xuqiudan_xiangce);
            holder.delect.setVisibility(View.GONE);
            if (position == 9) {
                holder.image.setVisibility(View.GONE);
            }
        }
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listening != null){
                    listening.onitemClickListening(v , position);
                }
            }
        });
        holder.delect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listening != null){
                    listening.onitemClickListening(v , position);
                }
            }
        });
        return convertView;
    }

    class ViewHolder{
        ImageView image;
        ImageView delect;
        RelativeLayout layout;
    }

    public void setHeight(RelativeLayout ima){
        ViewGroup.LayoutParams params = ima.getLayoutParams();
        params.height=(WindowUtils.getWeight(context)/4)-20;
        params.width =(WindowUtils.getWeight(context)/4)-20;
        ima.setLayoutParams(params);
    }

    public interface OnItemClickListening{
        void onitemClickListening(View view , int position);
    }
    public OnItemClickListening listening;

    public OnItemClickListening getListening() {
        return listening;
    }

    public void setListening(OnItemClickListening listening) {
        this.listening = listening;
    }
}
