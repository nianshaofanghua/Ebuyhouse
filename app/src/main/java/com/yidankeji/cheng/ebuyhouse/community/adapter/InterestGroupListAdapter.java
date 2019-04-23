package com.yidankeji.cheng.ebuyhouse.community.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.community.mode.SocialFriendBean;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;

import java.util.ArrayList;

/**
 * Created by ${syj} on 2018/4/12.
 */

public class InterestGroupListAdapter extends BaseAdapter implements SectionIndexer {
    private ArrayList<SocialFriendBean.ContentBean.RowsBean> mList;
    private Context mContext;

    public InterestGroupListAdapter(ArrayList<SocialFriendBean.ContentBean.RowsBean> list, Context context) {
        mList = list;
        mContext = context;

    }

    public void setList(ArrayList<SocialFriendBean.ContentBean.RowsBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public ArrayList<SocialFriendBean.ContentBean.RowsBean> getList() {
        return mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 3; i < getCount(); i++) {
            String sortStr = mList.get(i).getFirstWord();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return mList.get(position).getFirstWord().charAt(0);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.interest_group_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == 0 || position == 1 || position == 2) {
            switch (position) {
                case 0:
                    viewHolder.tv_firstWord.setVisibility(View.GONE);
                    viewHolder.tv_userName.setText(mList.get(position).getCustomer_name());
                    viewHolder.iv_user.setImageResource(R.mipmap.new_friend_icon);
                    viewHolder.iv_user.setOval(false);

                    break;
                case 1:
                    viewHolder.tv_firstWord.setVisibility(View.GONE);
                    viewHolder.tv_userName.setText(mList.get(position).getCustomer_name());
                    viewHolder.iv_user.setImageResource(R.mipmap.community_icon);
                    viewHolder.iv_user.setOval(false);

                    break;
                case 2:
                    viewHolder.tv_firstWord.setVisibility(View.GONE);
                    viewHolder.tv_userName.setText(mList.get(position).getCustomer_name());
                    viewHolder.iv_user.setImageResource(R.mipmap.interest_groups_icon);
                    viewHolder.iv_user.setOval(false);

                    break;
                default:
                    break;
            }



            if (SharedPreferencesUtils.isLogin(mContext)) {
                viewHolder.tv_messageNum.setText(mList.get(position).getMessage() + "");
                if(mList.get(position).getMessage()!=0){
                    viewHolder.tv_messageNum.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.tv_messageNum.setVisibility(View.GONE);
                }
            }


        } else {
            viewHolder.tv_messageNum.setVisibility(View.GONE);
            if (position != 0 && !mList.get(position).getFirstWord().equals(mList.get(position - 1).getFirstWord())) {
                viewHolder.tv_firstWord.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tv_firstWord.setVisibility(View.GONE);
            }
            if (position == 0) {
                viewHolder.tv_firstWord.setVisibility(View.VISIBLE);
            }
            viewHolder.tv_firstWord.setText(mList.get(position).getFirstWord());
            viewHolder.tv_userName.setText(mList.get(position).getNick_name());

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.error(R.mipmap.touxiang);
            Glide.with(mContext).load(mList.get(position).getHead_url()).apply(requestOptions).into(viewHolder.iv_user);

        }


        return convertView;
    }


    private class ViewHolder {
        TextView tv_firstWord, tv_userName;
        RoundedImageView iv_user;
        TextView tv_messageNum;

        public ViewHolder(View view) {
            iv_user = view.findViewById(R.id.iv_user);
            tv_firstWord = view.findViewById(R.id.tv_first_word);
            tv_userName = view.findViewById(R.id.tv_user);
            tv_messageNum = view.findViewById(R.id.tv_message);
        }
    }


    OnClicking mOnClicking;

    public void setOnClickListen(OnClicking onClickListen) {
        mOnClicking = onClickListen;
    }

    public interface OnClicking {
        void onClickListening(View view, int position);
    }

}
