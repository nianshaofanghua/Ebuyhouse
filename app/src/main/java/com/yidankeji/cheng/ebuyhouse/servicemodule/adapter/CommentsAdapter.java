package com.yidankeji.cheng.ebuyhouse.servicemodule.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.servicemodule.mode.CommentMode;
import com.yidankeji.cheng.ebuyhouse.utils.TimeUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017\12\27 0027.
 */

public class CommentsAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<CommentMode> list;

    public CommentsAdapter(Activity activity, ArrayList<CommentMode> list) {
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_commentslist , parent , false);
            holder.touxiang = ((ImageView) convertView.findViewById(R.id.item_commentlist_touxiang));
            holder.name = ((TextView) convertView.findViewById(R.id.item_commentlist_name));
            holder.addtime = ((TextView) convertView.findViewById(R.id.item_commentlist_time));
            holder.content = ((TextView) convertView.findViewById(R.id.item_commentlist_content));
            holder.gridView = ((GridView) convertView.findViewById(R.id.item_commentlist_gridview));
            convertView.setTag(holder);
        }else{
            holder = ((ViewHolder) convertView.getTag());
        }

        CommentMode commentMode = list.get(position);
        Glide.with(activity).load(commentMode.getHead_url())
                .apply(MyApplication.getOptions_touxiang()).into(holder.touxiang);
        holder.name.setText(commentMode.getNickname());
        String add_time = commentMode.getAdd_time();
        long aLong = Long.parseLong(add_time);
        String toYears = getSecondToYears(aLong);
        holder.addtime.setText(toYears);
        holder.content.setText(commentMode.getEvaluate());

        CommentsImageAdapter adapter = new CommentsImageAdapter(activity , commentMode.getImgUrlsList());
        holder.gridView.setAdapter(adapter);
        WindowUtils.setGridViewHeightBasedOnChildren(holder.gridView , 4);
        adapter.notifyDataSetChanged();
        return convertView;
    }

    class ViewHolder{
        ImageView touxiang;
        TextView name , addtime , content;
        GridView gridView;
    }
    public static String getSecondToYears(long time){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:ss dd-MM-yyyy");
        Date curDate = new Date(1000*time);//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }
}
