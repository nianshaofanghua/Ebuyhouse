package com.yidankeji.cheng.ebuyhouse.loginmodule.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.loginmodule.mode.PhoneCodeMode;
import com.yidankeji.cheng.ebuyhouse.utils.interfaceUtils.InterfaceUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018\1\10 0010.
 */

public class PhoneCodeAdapter extends RecyclerView.Adapter<PhoneCodeAdapter.ViewHolder>{

    private Activity activity;
    private ArrayList<PhoneCodeMode> list;
    private InterfaceUtils.OnListItemClickListening listening;

    public PhoneCodeAdapter(Activity activity, ArrayList<PhoneCodeMode> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view =  LayoutInflater.from(activity).inflate(R.layout.item_phonecode , parent , false);
       ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        PhoneCodeMode phoneCodeMode = list.get(position);
        String is_hot = phoneCodeMode.getIs_hot();
        if (is_hot.equals("0")){
            holder.title.setVisibility(View.VISIBLE);
        }else{
            holder.title.setVisibility(View.GONE);
        }
        holder.name.setText(phoneCodeMode.getName());
        holder.num.setText("+"+phoneCodeMode.getCode());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listening != null){
                    listening.onListItemClickListening(v , position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title , name , num;
        LinearLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            title = ((TextView) itemView.findViewById(R.id.item_phonecode_title));
            num = ((TextView) itemView.findViewById(R.id.item_phonecode_num));
            name = ((TextView) itemView.findViewById(R.id.item_phonecode_name));
            layout = ((LinearLayout) itemView.findViewById(R.id.item_phonecode_layout));
        }
    }

    public InterfaceUtils.OnListItemClickListening getListening() {
        return listening;
    }

    public void setListening(InterfaceUtils.OnListItemClickListening listening) {
        this.listening = listening;
    }
}
