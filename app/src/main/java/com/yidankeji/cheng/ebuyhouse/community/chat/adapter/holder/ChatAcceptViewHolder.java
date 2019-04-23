package com.yidankeji.cheng.ebuyhouse.community.chat.adapter.holder;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.community.chat.adapter.ChatAdapter;
import com.yidankeji.cheng.ebuyhouse.community.chat.util.Constants;
import com.yidankeji.cheng.ebuyhouse.community.chat.util.Utils;
import com.yidankeji.cheng.ebuyhouse.community.db.MessageInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.github.rockerhieu.emojicon.EmojiconTextView;


/**
 * 作者：Rance on 2016/11/29 10:47
 * 邮箱：rance935@163.com
 */
public class ChatAcceptViewHolder extends BaseViewHolder<MessageInfo> {


    TextView chatItemDate;

    RoundedImageView chatItemHeader;

    ImageView chatItemContentImage;

    ImageView chatItemFail;

    ProgressBar chatItemProgress;

    ImageView chatItemVoice;

    LinearLayout chatItemLayoutContent;
    RelativeLayout rl_voice;
    TextView chatItemVoiceTime;
    ImageView chatItemContentVideo;
    EmojiconTextView mChatItemContentText;
    private ChatAdapter.onItemClickListener onItemClickListener;
    private Handler handler;
    private List<MessageInfo> mList;
    int x = 0;
    int y =0;
    public ChatAcceptViewHolder(List<MessageInfo> list, ViewGroup parent, ChatAdapter.onItemClickListener onItemClickListener, Handler handler) {
        super(parent, R.layout.item_chat_accept);
        initView(itemView);
        this.onItemClickListener = onItemClickListener;
        this.handler = handler;
        mList = list;
    }

    public void initView(View view) {
        chatItemDate = view.findViewById(R.id.chat_item_date);
        chatItemHeader = view.findViewById(R.id.chat_item_header);
        chatItemContentImage = view.findViewById(R.id.chat_item_content_image);
        chatItemVoice = view.findViewById(R.id.chat_item_voice);
        chatItemFail = (ImageView) view.findViewById(R.id.chat_item_fail);
        chatItemLayoutContent = view.findViewById(R.id.chat_item_layout_content);
        chatItemVoiceTime = view.findViewById(R.id.chat_item_voice_time);
        chatItemProgress = (ProgressBar) view.findViewById(R.id.chat_item_progress);
        mChatItemContentText = view.findViewById(R.id.chat_item_content_text);
        chatItemContentVideo = view.findViewById(R.id.chat_item_content_video);
        rl_voice = view.findViewById(R.id.rl_voice);
    }


    @Override
    public void setData(MessageInfo data) {
        if (getDataPosition() != 0) {
            if (mList.get(getDataPosition()).getSend_time() - mList.get(getDataPosition() - 1).getSend_time() > 60) {
                chatItemDate.setText(getTime(data.getSend_time()));
                chatItemDate.setVisibility(View.VISIBLE);
            }else {
                chatItemDate.setVisibility(View.GONE);
            }
        }
        RequestOptions requestOptionsHead = new RequestOptions();
        requestOptionsHead.error(R.mipmap.touxiang);
      //  requestOptionsHead.optionalTransform(new RoundedCorners(10));
        Glide.with(getContext()).load(data.getUser_head()).apply(requestOptionsHead).into(chatItemHeader);
        chatItemHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onHeaderClick(getDataPosition());
            }
        });

        switch (data.getContentType()) {
            case 1:
                int screenWidth = MyApplication.screenWidth;
                int spacing = Utils.dp2px(getContext(), 12);
                int itemWidth = (screenWidth - spacing * 8) / 7;
                rl_voice.setVisibility(View.GONE);
                mChatItemContentText.setEmojiconSize(itemWidth / 4 * 3);
                mChatItemContentText.setText(data.getContent());
                chatItemVoice.setVisibility(View.GONE);
                mChatItemContentText.setVisibility(View.VISIBLE);
                chatItemContentVideo.setVisibility(View.GONE);
                chatItemLayoutContent.setVisibility(View.VISIBLE);
                chatItemVoiceTime.setVisibility(View.GONE);
                chatItemContentImage.setVisibility(View.GONE);
                chatItemLayoutContent.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        x = (int) event.getX();
                        y = (int) event.getY();
                        return false;
                    }
                });
                chatItemLayoutContent.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onItemClickListener.onMessageDeal(x,y,getDataPosition(),v);
                        return false;
                    }
                });
                break;
            case 2:
                rl_voice.setVisibility(View.VISIBLE);
                chatItemVoice.setVisibility(View.VISIBLE);
                chatItemLayoutContent.setVisibility(View.VISIBLE);
                mChatItemContentText.setVisibility(View.GONE);
                chatItemContentVideo.setVisibility(View.GONE);
                chatItemVoiceTime.setVisibility(View.VISIBLE);
                chatItemContentImage.setVisibility(View.GONE);
                //    chatItemVoiceTime.setText(Utils.formatTime(data.getVoiceTime()));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) setVoiceWinth(data.getVoiceTime() / 1000), ViewGroup.LayoutParams.WRAP_CONTENT);
                rl_voice.setLayoutParams(params);

                chatItemVoiceTime.setText(data.getVoiceLength() + "″");
                chatItemLayoutContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onVoiceClick(chatItemVoice, getDataPosition());
                    }
                });
                break;
            case 3:
                rl_voice.setVisibility(View.GONE);
                chatItemVoice.setVisibility(View.GONE);
                chatItemLayoutContent.setVisibility(View.GONE);
                chatItemVoiceTime.setVisibility(View.GONE);
                mChatItemContentText.setVisibility(View.GONE);
                chatItemContentImage.setVisibility(View.VISIBLE);
                chatItemContentVideo.setVisibility(View.GONE);
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.override((int) px2dp(getContext(), data.getContentWidth()), px2dp(getContext(), data.getContentHeight()));
            requestOptions.optionalTransform(new RoundedCorners(10));
                requestOptions.placeholder(R.mipmap.erro_iamge_fang);
                Glide.with(getContext()).load(data.getContent()).apply(requestOptions).into(chatItemContentImage);
                chatItemContentImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onImageClick(chatItemContentImage, getDataPosition());
                    }
                });
                chatItemContentImage.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        x = (int) event.getX();
                        y = (int) event.getY();
                        return false;
                    }
                });
                chatItemContentImage.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onItemClickListener.onMessageDeal(x, y, getDataPosition(), v);
                        return false;
                    }
                });
                break;
            case 4:
                rl_voice.setVisibility(View.GONE);
                chatItemContentVideo.setVisibility(View.VISIBLE);
                chatItemVoice.setVisibility(View.GONE);
                chatItemLayoutContent.setVisibility(View.GONE);
                chatItemVoiceTime.setVisibility(View.GONE);
                mChatItemContentText.setVisibility(View.GONE);
                chatItemContentImage.setVisibility(View.VISIBLE);
                RequestOptions requestOptionss = new RequestOptions();
                requestOptionss.override((int) px2dp(getContext(), data.getContentWidth()), px2dp(getContext(), data.getContentHeight()));
                requestOptionss.optionalTransform(new RoundedCorners(10));
                Glide.with(getContext()).load(data.getVideo_img()).apply(requestOptionss).into(chatItemContentImage);


                chatItemContentImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onImageClick(chatItemContentImage, getDataPosition());
                    }
                });
                chatItemContentImage.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        x = (int) event.getX();
                        y = (int) event.getY();
                        return false;
                    }
                });
                chatItemContentImage.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onItemClickListener.onMessageDeal(x, y, getDataPosition(), v);
                        return false;
                    }
                });
                break;
            default:
                break;

        }
        switch (data.getSendState()) {
            case Constants.CHAT_ITEM_SENDING:
                chatItemProgress.setVisibility(View.VISIBLE);
                chatItemFail.setVisibility(View.GONE);
                break;
            case Constants.CHAT_ITEM_SEND_ERROR:
                chatItemProgress.setVisibility(View.GONE);
                chatItemFail.setVisibility(View.VISIBLE);
                break;
            case Constants.CHAT_ITEM_SEND_SUCCESS:
                chatItemProgress.setVisibility(View.GONE);
                chatItemFail.setVisibility(View.GONE);
                break;
            default:
                break;
        }

    }

    /**
     * px转换成dp
     */
    private int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue * (scale + 0.5f));
    }

    public double setVoiceWinth(long VoicePlayTimes) {
        double Lmin = MyApplication.screenWidth * 0.2f;
        double Lmax = MyApplication.screenWidth * 0.5f;
        double barLen = 0;
        double barCanChangeLen = Lmax - Lmin;

        if (VoicePlayTimes > 11) {
            barLen = Lmin + VoicePlayTimes * 0.05 * barCanChangeLen; // VoicePlayTimes 为10秒时，正好为可变长度的一半
        } else {
            barLen = Lmin + 0.4 * barCanChangeLen + (VoicePlayTimes - 10) * 0.04 * barCanChangeLen;
        }


        return barLen;

    }
    public String getTime(long timeStamp) {
        Log.e("time",""+timeStamp);
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");

        Date date = new Date(timeStamp*1000);
        res = simpleDateFormat.format(date);


        return res;
    }
}
