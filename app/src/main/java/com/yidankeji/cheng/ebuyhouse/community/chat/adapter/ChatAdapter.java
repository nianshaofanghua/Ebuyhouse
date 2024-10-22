package com.yidankeji.cheng.ebuyhouse.community.chat.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.yidankeji.cheng.ebuyhouse.community.chat.adapter.holder.ChatAcceptViewHolder;
import com.yidankeji.cheng.ebuyhouse.community.chat.adapter.holder.ChatSendViewHolder;
import com.yidankeji.cheng.ebuyhouse.community.chat.util.Constants;
import com.yidankeji.cheng.ebuyhouse.community.db.MessageInfo;


/**
 * 作者：Rance on 2016/11/29 10:46
 * 邮箱：rance935@163.com
 */
public class ChatAdapter extends RecyclerArrayAdapter<MessageInfo> {

    private onItemClickListener onItemClickListener;
    public Handler handler;

    public ChatAdapter(Context context) {
        super(context);
        handler = new Handler();
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        switch (viewType) {
            case Constants.CHAT_ITEM_TYPE_LEFT:
                viewHolder = new ChatAcceptViewHolder(mObjects,parent, onItemClickListener, handler);
                break;
            case Constants.CHAT_ITEM_TYPE_RIGHT:
                viewHolder = new ChatSendViewHolder(mObjects,parent, onItemClickListener, handler);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getViewType(int position) {
        return getAllData().get(position).getType();
    }

    public void addItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface onItemClickListener {
        void onHeaderClick(int position);

        void onImageClick(View view, int position);

        void onVoiceClick(ImageView imageView, int position);
        void onMessageDeal(int x,int y,int position,View view);
    }
}
