package com.yidankeji.cheng.ebuyhouse.community.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.d.lib.slidelayout.SlideLayout;
import com.d.lib.slidelayout.SlideManager;
import com.d.lib.xrv.adapter.CommonAdapter;
import com.d.lib.xrv.adapter.CommonHolder;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.community.mode.FriendApplyModel;

import java.util.List;

/**
 * Created by ${syj} on 2018/4/12.
 */

public class NewFriendListAdapter extends CommonAdapter<FriendApplyModel.ContentBean.RowsBean> {

    private SlideManager manager;
    private Context mContext;

    public NewFriendListAdapter(Context context, List<FriendApplyModel.ContentBean.RowsBean> datas, int layoutId) {
        super(context, datas, layoutId);
        mContext = context;
        manager = new SlideManager();

    }

    @Override
    public void setDatas(List<FriendApplyModel.ContentBean.RowsBean> datas) {
        super.setDatas(datas);
        notifyDataSetChanged();
    }

    @Override
    public void convert(final int position, CommonHolder holder, final FriendApplyModel.ContentBean.RowsBean item) {

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
                if (mOnClickItemListen != null) {
                    mOnClickItemListen.onClick(v, position);
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

        RequestOptions requestOptions =new RequestOptions();
        requestOptions.circleCrop();
        requestOptions.error(R.mipmap.touxiang);
        Glide.with(mContext).load(item.getHead_url()).apply(requestOptions).into(viewHolder.iv_user);
        viewHolder.tv_type.setText(item.getSource());
        viewHolder.tv_name.setText(item.getNickname());
        viewHolder.tv_detail.setText("Source:" + item.getSource());

        switch (item.getState()) {
            case 1:
                viewHolder.tv_type.setText("Agree");
                break;
            case 2:
                viewHolder.tv_type.setText("Agree");
                break;
            case 3:
                viewHolder.tv_type.setText("Approved");
                break;
            case 4:
                viewHolder.tv_type.setText("Refuse");
                break;
            default:
                break;
        }


        RelativeLayout.LayoutParams rl;
        if (viewHolder.tv_type.getText().equals("Agree")) {
            rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            viewHolder.tv_type.setBackground(mContext.getResources().getDrawable(R.drawable.shape_nosolid_red));
            viewHolder.tv_type.setTextColor(mContext.getResources().getColor(R.color.text_red));
        } else {
            rl = new RelativeLayout.LayoutParams(220,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            viewHolder.tv_type.setBackground(mContext.getResources().getDrawable(R.drawable.shape_white));
            viewHolder.tv_type.setTextColor(mContext.getResources().getColor(R.color.text_use_gray));
        }


        rl.addRule(RelativeLayout.CENTER_VERTICAL);
        rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rl.setMargins(0, 0, 80, 0);

        viewHolder.tv_type.setLayoutParams(rl);
        viewHolder.tv_type.setGravity(Gravity.CENTER);


        viewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickItemListen != null) {
                    mOnClickItemListen.onClick(v, position);
                }
            }
        });
        viewHolder.tv_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickItemListen != null) {
                    mOnClickItemListen.onClick(v, position);
                }
            }
        });

    }

    OnClickItemListen mOnClickItemListen;

    public void setOnClickItemListen(OnClickItemListen clickItemListen) {
        mOnClickItemListen = clickItemListen;
    }

    public interface OnClickItemListen {
        void onClick(View view, int position);
    }

    private class ViewHolder {
        TextView tv_name, tv_detail, tv_type;
        ImageView iv_user;
        RelativeLayout mLayout;

        public ViewHolder(CommonHolder itemView) {

            tv_name = itemView.getView(R.id.tv_name);
            tv_detail = itemView.getView(R.id.tv_detail);
            tv_type = itemView.getView(R.id.tv_type);
            iv_user = itemView.getView(R.id.iv_user);
            mLayout = itemView.getView(R.id.rl);

        }
    }
}
