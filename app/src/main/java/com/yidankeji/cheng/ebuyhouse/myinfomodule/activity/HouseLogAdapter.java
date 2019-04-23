package com.yidankeji.cheng.ebuyhouse.myinfomodule.activity;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.d.lib.slidelayout.SlideLayout;
import com.d.lib.slidelayout.SlideManager;
import com.d.lib.xrv.adapter.CommonAdapter;
import com.d.lib.xrv.adapter.CommonHolder;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.myinfomodule.mode.HistoryLogListModel;

import java.util.ArrayList;

/**
 * Created by ${syj} on 2018/3/20.
 */

public class HouseLogAdapter extends CommonAdapter<HistoryLogListModel.ContentBean.RowsBean> {
    private SlideManager manager;
    private Context mContext;

    public HouseLogAdapter(Context context, ArrayList<HistoryLogListModel.ContentBean.RowsBean> datas, int layoutId) {
        super(context, datas, layoutId);
        manager = new SlideManager();
        mContext = context;
    }

    @Override
    public void convert(final int position, CommonHolder holder, final HistoryLogListModel.ContentBean.RowsBean item) {
        ViewHolder viewHolder = new ViewHolder(holder);
        final SlideLayout slSlide = holder.getView(R.id.sl_slide);
        slSlide.setOpen(item.isOpen(), false);
        slSlide.setOnStateChangeListener(new SlideLayout.OnStateChangeListener() {
            @Override
            public void onChange(SlideLayout layout, boolean isOpen) {

                item.setOpen(isOpen);
                manager.onChange(layout, isOpen);
            }

            @Override
            public boolean closeAll(SlideLayout layout) {
                return manager.closeAll(layout);
            }
        });

        holder.setViewOnClickListener(R.id.tv_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slSlide.close();
                if (showListOnItemClickListening != null) {
                    showListOnItemClickListening.OnItemClickListening(v, position);
                }
            }
        });
        holder.setViewOnClickListener(R.id.sl_slide, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slSlide.isOpen()) {
                    slSlide.close();
                    return;
                }

            }
        });



        setHeight(viewHolder.imageView);
        ViewGroup.LayoutParams ll_hight = viewHolder.ll_hight.getLayoutParams();
        ll_hight.height = (MyApplication.Equipment_width / 2)+350;
        viewHolder.ll_hight.setLayoutParams(ll_hight)
        ;
        HistoryLogListModel.ContentBean.RowsBean showListMode = item;
        Glide.with(mContext).load(showListMode.getImg_url()).apply(MyApplication.getOptions_rectangles()).into(viewHolder.imageView);
        boolean collect = showListMode.is_collect();
        if (collect) {
            viewHolder.shoucang.setImageResource(R.mipmap.pro_shoucang_ed);
        } else {
            viewHolder.shoucang.setImageResource(R.mipmap.pro_shoucang_e);
        }
        viewHolder.jishi.setText(showListMode.getBedroom() + "beds " + showListMode.getBathroom() + "baths " + showListMode.getLiving_sqft() + "sqft");

        String price = showListMode.getSimple_price();
        if (!TextUtils.isEmpty(price)) {
            if (!price.contains("$")) {
                price = "$" + price;
            }
            viewHolder.money.setText(price);
        } else {
            price = showListMode.getPrice();
            if (!price.contains("$")) {
                price = "$" + price;
            }
            viewHolder.money.setText(price);
        }

        if (showListMode.getRelease_type().equals("rent")) {

            viewHolder.money.setText(price + "/MO");
        }
        viewHolder.money.setTextSize(18f);
        viewHolder.canshu1.setText(showListMode.getCity_name() + " " + showListMode.getState_name());
        viewHolder.viewNum.setText(showListMode.getViewNum()+"");
        viewHolder.saveNum.setText(showListMode.getSaveNum()+"");

        viewHolder.watchNum.setText(showListMode.getSaveNum()+"");
        viewHolder.collectNum.setText(showListMode.getSaveNum()+"");


        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.watchNum.getLayoutParams();
        params.setMargins(0, (MyApplication.Equipment_width / 2)-140, 0, 0);
        params.rightMargin = 10;
        viewHolder.watchNum.setLayoutParams(params);


        RelativeLayout.LayoutParams param01 = (RelativeLayout.LayoutParams) viewHolder.collectNum.getLayoutParams();
        param01.setMargins(0, (MyApplication.Equipment_width / 2)-140, 0, 0);
        viewHolder.collectNum.setLayoutParams(param01);


        viewHolder.canshu2.setText(showListMode.getStreet());
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showListOnItemClickListening != null) {
                    showListOnItemClickListening.OnItemClickListening(v, position);
                }
            }
        });
        viewHolder.shoucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showListOnItemClickListening != null) {
                    showListOnItemClickListening.OnItemClickListening(v, position);
                }
            }
        });
        viewHolder.checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showListOnItemClickListening != null) {
                    showListOnItemClickListening.OnItemClickListening(v, position);
                }
            }
        });
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showListOnItemClickListening != null) {
                    showListOnItemClickListening.OnItemClickListening(v, position);
                }
            }
        });
        viewHolder.saveNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showListOnItemClickListening != null) {
                    showListOnItemClickListening.OnItemClickListening(v, position);
                }
            }
        });


    }

    public static class ViewHolder {

        ImageView imageView, shoucang;
        TextView money, canshu1, canshu2, checked, jishi,viewNum,saveNum,watchNum,collectNum;
        LinearLayout layout,ll_hight;

        public ViewHolder(CommonHolder itemView) {

            imageView = ((ImageView) itemView.getView(R.id.item_showlist_image));
            shoucang = ((ImageView) itemView.getView(R.id.item_showlist_shoucang));
            money = ((TextView) itemView.getView(R.id.item_showlist_moaney));
            canshu1 = ((TextView) itemView.getView(R.id.item_showlist_canshu1));
            canshu2 = ((TextView) itemView.getView(R.id.item_showlist_canshu2));
            checked = ((TextView) itemView.getView(R.id.item_showlist_checked));
            jishi = (TextView) itemView.getView(R.id.item_showlist_jishi);
            layout = ((LinearLayout) itemView.getView(R.id.item_showlist_son));
            viewNum = itemView.getView(R.id.see_num);
            saveNum = itemView.getView(R.id.savenum);
            watchNum = itemView.getView(R.id.watch_num);
            collectNum = itemView.getView(R.id.collect_num);
            ll_hight = itemView.getView(R.id.ll_hight);

        }
    }

    public interface ShowListOnItemClickListening {
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
