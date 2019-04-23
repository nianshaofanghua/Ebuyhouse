package com.yidankeji.cheng.ebuyhouse.housemodule.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.d.lib.slidelayout.SlideLayout;
import com.d.lib.slidelayout.SlideManager;
import com.d.lib.xrv.adapter.CommonAdapter;
import com.d.lib.xrv.adapter.CommonHolder;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.community.db.AlertModel;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;

import java.util.List;

/**
 * Created by ${syj} on 2018/5/11.
 */

public class AlertsListAdapter extends CommonAdapter<AlertModel> {
    private SlideManager manager;
    private boolean isShow = true;
    private Context mContext;
    private String userID;

    public AlertsListAdapter(Context context, List<AlertModel> datas, int layoutId) {
        super(context, datas, layoutId);
        manager = new SlideManager();
        mContext = context;

    }

    @Override
    public void setDatas(List<AlertModel> datas) {
        super.setDatas(datas);
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setShow(boolean show) {
        isShow = show;
        notifyDataSetChanged();
    }

    @Override
    public void convert(final int position, CommonHolder holder, final AlertModel item) {
        userID = (String) SharedPreferencesUtils.getParam(mContext, "userID", "");

        final SlideLayout slSlide = holder.getView(R.id.sl_slide);
        if (position == 0 || position == 1) {
            slSlide.isShow(false);
        } else {
            slSlide.isShow(true);
        }


        slSlide.setOpen(item.isOpen(), false);
        final ViewHolder viewHolder = new ViewHolder(holder);
        slSlide.setOnStateChangeListener(new SlideLayout.OnStateChangeListener() {
            @Override
            public void onChange(SlideLayout layout, boolean isOpen) {
                if (position != 0 || position != 1) {
                    item.setOpen(isOpen);
                    manager.onChange(layout, isOpen);
                } else {

                }

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
                if (position != 0 || position != 1) {
                    if (slSlide.isOpen()) {
                        slSlide.close();
                        return;
                    }
                }


            }
        });
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.mipmap.touxiang);
        requestOptions.circleCrop();
        switch (position) {
            case 0:
                viewHolder.tv_title.setText("Broadcast");
                viewHolder.iv_head.setImageResource(R.mipmap.cc01);
                viewHolder.tv_detail.setVisibility(View.GONE);

                break;
            case 1:
                viewHolder.tv_title.setText("Contract");
                viewHolder.tv_detail.setVisibility(View.GONE);
                viewHolder.iv_head.setImageResource(R.mipmap.cc03);

                break;
            default:

                String head = "";
                if (userID.equals(item.getUser_id())) {
                    head = item.getShow_head();
                    viewHolder.tv_title.setText(item.getShow_name());
                } else {
                    head = item.getUser_head();
                    viewHolder.tv_title.setText(item.getInter_name());
                }
                String name = "";

                if (item.getTarget_type() == 1 || item.getTarget_type() == 2) {
                    head = item.getShow_head();
                    viewHolder.tv_title.setText(item.getShow_name());
                    name = item.getInter_name() + ":";
                } else {

                }


                switch (item.getContentType()) {
                    case 1:

                        Log.e("tv_title", viewHolder.tv_title.getText().toString() + "---" + item.getInter_name());
                        viewHolder.tv_detail.setText(name + item.getContent());

                        Glide.with(mContext).load(head).apply(requestOptions).into(viewHolder.iv_head);
                        break;
                    case 2:

                        viewHolder.tv_detail.setText(name + "[Voice]");
                        Glide.with(mContext).load(head).apply(requestOptions).into(viewHolder.iv_head);
                        break;
                    case 3:

                        viewHolder.tv_detail.setText(name + "[Picture]");
                        Glide.with(mContext).load(head).apply(requestOptions).into(viewHolder.iv_head);
                        break;
                    case 4:

                        viewHolder.tv_detail.setText(name + "[Video]");
                        Glide.with(mContext).load(head).apply(requestOptions).into(viewHolder.iv_head);
                        break;

                    default:
                        break;

                }

                break;
        }


        viewHolder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showListOnItemClickListening != null) {
                    showListOnItemClickListening.OnItemClickListening(viewHolder.ll_item, position);
                }

            }
        });
        Log.e("消息数量"+position, "数量为" + item.getUnread_number());

            if (item.getUnread_number() != 0) {
                viewHolder.tv_num.setVisibility(View.VISIBLE);
            } else {
           //     viewHolder.tv_num.setVisibility(View.GONE);
            }


        if (item.getUnread_number() != 0) {
            if (item.getUnread_number() > 99) {
                viewHolder.tv_num.setText("99+");
            } else {
                viewHolder.tv_num.setText(item.getUnread_number() + "");
            }
        }else {
            viewHolder.tv_num.setVisibility(View.GONE);
        }
    }

    private static class ViewHolder {
        TextView tv_title, tv_num, tv_time, tv_detail;
        ImageView iv_head;
        LinearLayout ll_item;

        public ViewHolder(CommonHolder itemView) {
            iv_head = itemView.getView(R.id.iv_head);
            tv_title = itemView.getView(R.id.tv_title);
            tv_num = itemView.getView(R.id.message_num);
            tv_detail = itemView.getView(R.id.tv_detail);
            ll_item = itemView.getView(R.id.ll_item);
        }
    }

    public ShowListOnItemClickListening showListOnItemClickListening;


    public void setShowListOnItemClickListening(ShowListOnItemClickListening showListOnItemClickListening) {
        this.showListOnItemClickListening = showListOnItemClickListening;
    }

    public interface ShowListOnItemClickListening {
        void OnItemClickListening(View view, int position);
    }
}
