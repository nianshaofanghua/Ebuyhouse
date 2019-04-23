package com.yidankeji.cheng.ebuyhouse.servicemodule.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.utils.PhotoView.PhotoViewUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017\12\27 0027.
 */

public class CommentsImageAdapter extends BaseAdapter{

    private Activity activity;
    private ArrayList<String> list;

    public CommentsImageAdapter(Activity activity, ArrayList<String> list) {
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
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_comments_image , parent , false);
            holder.image = ((ImageView) convertView.findViewById(R.id.item_comment_image));
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        Glide.with(activity).load(list.get(position))
                .apply(MyApplication.getOptions_square()).into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoViewUtils.getPhotoView(activity ,list , 0 );
            }
        });
        return convertView;
    }

    class ViewHolder{
        ImageView image;
    }
}
