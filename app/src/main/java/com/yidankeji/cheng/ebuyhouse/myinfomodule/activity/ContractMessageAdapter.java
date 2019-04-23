package com.yidankeji.cheng.ebuyhouse.myinfomodule.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.adapter.MyRentalRoomAdapter;
import com.yidankeji.cheng.ebuyhouse.mode.NewContractModel;
import com.yidankeji.cheng.ebuyhouse.mode.PostRoom.BoardCastModel;
import com.yidankeji.cheng.ebuyhouse.myinfomodule.utils.CustomListView;

import java.util.ArrayList;

/**
 * Created by ${syj} on 2018/2/26.
 */

public class ContractMessageAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<NewContractModel.ContentBean.RowsBean> mList;

    public ContractMessageAdapter(ArrayList<NewContractModel.ContentBean.RowsBean> list, Context context) {
        mList = list;
        mContext = context;
    }

    public void setList(ArrayList<NewContractModel.ContentBean.RowsBean> list) {
        mList = list;
        notifyDataSetChanged();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_boardcast, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mDate.setText(mList.get(position).getDay_time());
        ContractListItemAdapter boardCastListItemAdapter = new ContractListItemAdapter((ArrayList<NewContractModel.ContentBean.RowsBean.RadioMsgBean>) mList.get(position).getRadio_msg(), mContext);
        viewHolder.mCustomListView.setAdapter(boardCastListItemAdapter);
        viewHolder.mCustomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                listening.onItemClickListening(position , i);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView mDate;
        CustomListView mCustomListView;

        public ViewHolder(View view) {
            mDate = (TextView) view.findViewById(R.id.date);
            mCustomListView = (CustomListView) view.findViewById(R.id.list_item);
        }
    }
    public interface OnItemClickListening{
        void onItemClickListening(int firstPos , int position);
    }
    public OnItemClickListening listening;

    public OnItemClickListening getListening() {
        return listening;
    }

    public void setListening(OnItemClickListening listening) {
        this.listening = listening;
    }

}
