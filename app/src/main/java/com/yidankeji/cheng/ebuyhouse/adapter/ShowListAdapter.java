package com.yidankeji.cheng.ebuyhouse.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.mode.ShowListMode;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Administrator on 2017\12\5 0005.
 */

public class ShowListAdapter extends RecyclerView.Adapter<ShowListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ShowListMode> list;

    public ShowListAdapter(Context context, ArrayList<ShowListMode> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(ArrayList<ShowListMode> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_showlist_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        setHeight(holder.imageView);
        ShowListMode showListMode = list.get(position);
        Glide.with(context).load(showListMode.getImg_url()).apply(MyApplication.getOptions_rectangles()).into(holder.imageView);
        boolean collect = showListMode.is_collect();

        Drawable drawable01 = context.getResources().getDrawable(R.mipmap.pro_shoucang_ed);
        drawable01.setBounds(0, 0, drawable01.getIntrinsicWidth(), drawable01.getIntrinsicHeight());
        Drawable drawable = context.getResources().getDrawable(R.mipmap.pro_shoucang_e);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        if (collect) {
            holder.shoucang.setImageResource(R.mipmap.pro_shoucang_ed);
            holder.saveNum.setCompoundDrawables(drawable01, null, null, null);
            holder.saveNum.setText((Integer.valueOf(holder.saveNum.getText().toString())+1)+"");
        } else {
            holder.shoucang.setImageResource(R.mipmap.pro_shoucang_e);
            holder.saveNum.setCompoundDrawables(drawable, null, null, null);
            holder.saveNum.setText((Integer.valueOf(holder.saveNum.getText().toString())-1)+"");
        }
        holder.jishi.setText(showListMode.getBedroom() + "beds " + showListMode.getBathroom() + "baths " + showListMode.getLiving_sqft() + "sqft");

        String price = showListMode.getSimple_price();
        if (!TextUtils.isEmpty(price)) {
            if (!price.contains("$")) {
                price = "$" + price;
            }
            holder.money.setText(price);
        } else {
            price = showListMode.getPrice();
            if (!price.contains("$")) {
                price = "$" + price;
            }
            holder.money.setText(price);
        }

        if (showListMode.getRelease_type().equals("rent")) {

            holder.money.setText(price + "/MO");
        }
        holder.money.setTextSize(18f);
        holder.canshu1.setText(showListMode.getCity_name() + " " + showListMode.getState_name());
//        holder.viewNum.setText(showListMode.getViewnum() + "");
//        holder.saveNum.setText(showListMode.getSavenum() + "");
        holder.collect_num.setText(showListMode.getSavenum() + "");
        holder.lookNum.setText(showListMode.getViewnum() + "");
holder.saveNum.setText(showListMode.getSavenum()+"");
        holder.canshu2.setText(showListMode.getStreet());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showListOnItemClickListening != null) {
                    showListOnItemClickListening.OnItemClickListening(v, position);
                }
            }
        });
        holder.shoucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showListOnItemClickListening != null) {
                    showListOnItemClickListening.OnItemClickListening(v, position);
                }
            }
        });
        holder.checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showListOnItemClickListening != null) {
                    showListOnItemClickListening.OnItemClickListening(v, position);
                }
            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showListOnItemClickListening != null) {
                    showListOnItemClickListening.OnItemClickListening(v, position);
                }
            }
        });
        holder.saveNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showListOnItemClickListening != null) {
                    showListOnItemClickListening.OnItemClickListening(v, position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView, shoucang;
        TextView money, canshu1, canshu2, checked, jishi, viewNum,saveNum,lookNum,collect_num;
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = ((ImageView) itemView.findViewById(R.id.item_showlist_image));
            shoucang = ((ImageView) itemView.findViewById(R.id.item_showlist_shoucang));
            money = ((TextView) itemView.findViewById(R.id.item_showlist_moaney));
            canshu1 = ((TextView) itemView.findViewById(R.id.item_showlist_canshu1));
            canshu2 = ((TextView) itemView.findViewById(R.id.item_showlist_canshu2));
            checked = ((TextView) itemView.findViewById(R.id.item_showlist_checked));
            jishi = (TextView) itemView.findViewById(R.id.item_showlist_jishi);
            layout = ((LinearLayout) itemView.findViewById(R.id.item_showlist_son));
            viewNum = (TextView) itemView.findViewById(R.id.see_num);
            saveNum = (TextView) itemView.findViewById(R.id.savenum);
            lookNum  = (TextView) itemView.findViewById(R.id.watch_num);
            collect_num = (TextView) itemView.findViewById(R.id.collect_num);
        }
    }

    int MIN_CLICK_DELAY_TIME = 1000;
    long lastClickTime = 0;

    public interface ShowListOnItemClickListening {

        long currentTime = Calendar.getInstance().getTimeInMillis();

        //        default void  get(){
//            if(currentTime -lastClickTime>MIN_CLICK_DELAY_TIME){
//                lastClickTime = currentTime;
//
//            }



        void OnItemClickListening(View view, int position);
    }

    public void setHeight(ImageView ima) {
        ViewGroup.LayoutParams params = ima.getLayoutParams();
        params.height = MyApplication.Equipment_width / 2;
        params.width = MyApplication.Equipment_width;
        ima.setLayoutParams(params);
    }


    public ShowListOnItemClickListening showListOnItemClickListening;

    public ShowListOnItemClickListening getShowListOnItemClickListening() {
        return showListOnItemClickListening;
    }

    public void setShowListOnItemClickListening(ShowListOnItemClickListening showListOnItemClickListening) {
        this.showListOnItemClickListening = showListOnItemClickListening;
    }
}


