package com.yidankeji.cheng.ebuyhouse.myinfomodule.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.mode.NewContractModel;
import com.yidankeji.cheng.ebuyhouse.mode.PostRoom.BoardCastModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${syj} on 2018/2/26.
 */

public class ContractListItemAdapter extends BaseAdapter{
    ArrayList<NewContractModel.ContentBean.RowsBean.RadioMsgBean> mList;
    Context mContext;

    public ContractListItemAdapter(ArrayList<NewContractModel.ContentBean.RowsBean.RadioMsgBean> list, Context context) {
        mList = list;
        mContext = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_boardcast_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mTitle.setText(mList.get(position).getMsg_title());


        viewHolder.mDetail.setText(mList.get(position).getMsg_val());

        return convertView;
    }

    private class ViewHolder {
        TextView mTitle,mDetail;

        public ViewHolder(View view) {
            mTitle = (TextView) view.findViewById(R.id.title);
            mDetail = (TextView) view.findViewById(R.id.detail);
        }
    }

}
