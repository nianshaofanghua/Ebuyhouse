package com.yidankeji.cheng.ebuyhouse.servicemodule.adapter;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.servicemodule.mode.ServiceListMode;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017\12\26 0026.
 */

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ViewHolder>{

    private Activity activity;
    private ArrayList<ServiceListMode> list;

    public ServiceListAdapter(Activity activity, ArrayList<ServiceListMode> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_servicelist , parent , false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        ServiceListMode mode = list.get(position);
        holder.name.setText(mode.getCompany());
        holder.type.setText("Region:"+mode.getCity()+" "+mode.getState());
        holder.phone.setText(mode.getPhone_number());
        holder.address.setText(mode.getAddress());
        Glide.with(activity).load(mode.getImg_url()).apply(MyApplication.getOptions_square()).into(holder.iamge);

        SonServiceListAdapter adapter = new SonServiceListAdapter(activity , mode.getServiceItemslist());
        holder.recyclerView.setAdapter(adapter);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(activity , LinearLayoutManager.HORIZONTAL , false));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listening != null){
                    listening.onItemClickListening(v , position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iamge;
        TextView name , type , phone , address;
        RecyclerView recyclerView;
        LinearLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            iamge = ((ImageView) itemView.findViewById(R.id.item_servicelist_image));
            name = ((TextView) itemView.findViewById(R.id.item_servicelist_name));
            type = ((TextView) itemView.findViewById(R.id.item_servicelist_type));
            phone = ((TextView) itemView.findViewById(R.id.item_servicelist_phone));
            address = ((TextView) itemView.findViewById(R.id.item_servicelist_address));
            recyclerView = ((RecyclerView) itemView.findViewById(R.id.item_servicelist_recycler));
            layout = ((LinearLayout) itemView.findViewById(R.id.item_servicelist_layout));
        }
    }

    public interface OnItemClickListening{
        void onItemClickListening(View view, int position);
    }
    public OnItemClickListening listening;

    public OnItemClickListening getListening() {
        return listening;
    }

    public void setListening(OnItemClickListening listening) {
        this.listening = listening;
    }
}
