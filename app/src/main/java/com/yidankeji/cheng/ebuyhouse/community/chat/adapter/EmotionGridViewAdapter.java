package com.yidankeji.cheng.ebuyhouse.community.chat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yidankeji.cheng.ebuyhouse.R;

import java.util.List;

import io.github.rockerhieu.emojicon.EmojiconTextView;
import io.github.rockerhieu.emojicon.emoji.Emojicon;

/**
 * 作者：Rance on 2016/11/29 10:47
 * 邮箱：rance935@163.com
 */
public class EmotionGridViewAdapter extends BaseAdapter {

    private Context context;
    private List<Emojicon> emotionNames;
    private int itemWidth;

    public EmotionGridViewAdapter(Context context, List<Emojicon> emotionNames, int itemWidth) {
        this.context = context;
        this.emotionNames = emotionNames;
        this.itemWidth = itemWidth;
    }

    @Override
    public int getCount() {
        // +1 最后一个为删除按钮
        return emotionNames.size() + 1;
    }

    @Override
    public Emojicon getItem(int position) {
        return emotionNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EmojiconTextView iv_emotion = null;
        ImageView iv_delete = null;
        if (position == emotionNames.size()) {
            // 设置内边距
            iv_delete = new ImageView(context);
            iv_delete.setImageResource(R.drawable.compose_emotion_delete);
            iv_delete.setPadding(itemWidth / 8, 0, itemWidth / 8, 0);


            LayoutParams params = new LayoutParams(itemWidth, itemWidth);

            iv_delete.setLayoutParams(params);
            return iv_delete;
        } else {
            iv_emotion = new EmojiconTextView(context);
            iv_emotion.setUseSystemDefault(false);
            // 设置内边距
            iv_emotion.setPadding(itemWidth / 8, itemWidth / 8, itemWidth / 8, itemWidth / 8);
            LayoutParams params = new LayoutParams(itemWidth, itemWidth);
            iv_emotion.setLayoutParams(params);
            iv_emotion.setGravity(Gravity.CENTER_VERTICAL);
            Emojicon emotionName = emotionNames.get(position);
            iv_emotion.setText(emotionName.getEmoji());

            iv_emotion.setEmojiconSize(itemWidth / 4 * 3);
            return iv_emotion;
        }


    }

}
