package com.yidankeji.cheng.ebuyhouse.adapter.MyMessage;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.mode.MyMessageMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2017\12\25 0025.
 */

public class MyMeaageSonAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<MyMessageMode> datalist;
    private boolean isSingleLine = true;

    public MyMeaageSonAdapter(Activity activity, ArrayList<MyMessageMode> datalist) {
        this.activity = activity;
        this.datalist = datalist;
    }

    public void setDatalist(ArrayList<MyMessageMode> datalist) {
        this.datalist = datalist;
        notifyDataSetChanged();
    }

    public void setSingleLine(boolean singleLine) {
        isSingleLine = singleLine;
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return datalist.get(position);
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_mymessagesonlist , null);
            holder.image = ((ImageView) convertView.findViewById(R.id.item_mymessageson_image));
            holder.image01= (ImageView) convertView.findViewById(R.id.item_mymessageson_image2);
            holder.content = ((TextView) convertView.findViewById(R.id.item_mymessageson_content));
            holder.mSendTime = (TextView) convertView.findViewById(R.id.send_time);
            convertView.setTag(holder);
        }else{
            holder = ((ViewHolder) convertView.getTag());
        }

        MyMessageMode myMessageMode = datalist.get(position);
        String message = myMessageMode.getMessage();
        Log.e("time","time=="+myMessageMode.getAdd_time());
        holder.mSendTime.setText(getStrTime((myMessageMode.getAdd_time())));
        if (message == null || message.equals("")){
            holder.content.setText(myMessageMode.getReply());
            holder.image.setImageResource(R.mipmap.mymessage_a);
            holder.image01.setImageResource(R.mipmap.mymessage_a);
            holder.image.setVisibility(View.GONE);
            holder.image01.setVisibility(View.VISIBLE);
            holder.content.setGravity(Gravity.RIGHT);
        }else{
            holder.content.setText(message);
            holder.image.setImageResource(R.mipmap.mymessage_q);
            holder.image01.setImageResource(R.mipmap.mymessage_q);
            holder.image.setVisibility(View.VISIBLE);
            holder.image01.setVisibility(View.GONE);
            holder.content.setGravity(Gravity.LEFT);
        }
        holder.content.setSingleLine(isSingleLine);
        return convertView;
    }

    class ViewHolder{
        TextView content;
        ImageView image , image01;
        TextView mSendTime;
    }
    public static String getStrTime(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("ss:mm:HH dd-MM");
// 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;

    }
}
