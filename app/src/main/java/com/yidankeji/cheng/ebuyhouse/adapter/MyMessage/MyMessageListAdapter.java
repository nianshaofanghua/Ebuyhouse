package com.yidankeji.cheng.ebuyhouse.adapter.MyMessage;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.mode.MyMessageMode;
import com.yidankeji.cheng.ebuyhouse.utils.NoMoveListView;
import com.yidankeji.cheng.ebuyhouse.utils.TimeUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

import java.util.ArrayList;

import static com.yidankeji.cheng.ebuyhouse.R.id.item_mymessagelist_gopro;

/**
 * Created by Administrator on 2017\12\25 0025.
 */

public class MyMessageListAdapter extends RecyclerView.Adapter<MyMessageListAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<MyMessageMode> datalist;

    public MyMessageListAdapter(Activity activity, ArrayList<MyMessageMode> datalist) {
        this.activity = activity;
        this.datalist = datalist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_mymessage_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        MyMessageMode myMessageMode = datalist.get(position);
        ArrayList<MyMessageMode> list = myMessageMode.getList();
        MyMeaageSonAdapter adapter = new MyMeaageSonAdapter(activity, list);
        holder.listview.setAdapter(adapter);
        if (list.size() >= 2) {
            setListViewHeight(holder.listview);
        } else {
            WindowUtils.setListViewHeightBasedOnChildren(holder.listview);
        }
        holder.listNum.setText(list.size() + "");
        String head_url = myMessageMode.getHead_url();
        Glide.with(activity).load(head_url).apply(MyApplication.getOptions_touxiang()).into(holder.touxiang);
        holder.name.setText(myMessageMode.getNickname());
        String add_time = myMessageMode.getAdd_time();
        long time = Long.parseLong(add_time);
        String secondToYears = TimeUtils.getSecondToYears(time);
        holder.time.setText(secondToYears);
        /**/
        holder.bianhao.setText(myMessageMode.getPhone_number());
        holder.content.setText(myMessageMode.getMessage());
        holder.email.setText(myMessageMode.getEmail());


        if (myMessageMode.getHas_new() == 1) {
            holder.isNew.setVisibility(View.VISIBLE);
        } else {
            holder.isNew.setVisibility(View.GONE);
        }

        holder.gopro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listening != null) {
                    listening.getView(v, position);
                }
            }
        });
        holder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listening != null) {
                    listening.getView(v, position);
                }
            }
        });
        holder.listNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listening != null) {
                    listening.getView(v, position);
                }
            }
        });
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listening != null) {
                    listening.getView(v, position);
                }
            }
        });
        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listening != null) {
                    listening.getView(v, position);
                }
            }
        });
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listening != null) {
                    listening.getView(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView touxiang;
        TextView name, time, reply, bianhao, content, gopro, email, listNum, isNew;
        NoMoveListView listview;
        LinearLayout mLinearLayout;
        RelativeLayout mRelativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            touxiang = ((ImageView) itemView.findViewById(R.id.item_mymessagelist_touxiang));
            time = ((TextView) itemView.findViewById(R.id.item_mymessagelist_time));
            reply = ((TextView) itemView.findViewById(R.id.item_mymessagelist_reply));
            name = ((TextView) itemView.findViewById(R.id.item_mymessagelist_name));
            bianhao = ((TextView) itemView.findViewById(R.id.item_mymessagelist_bianhao));
            content = ((TextView) itemView.findViewById(R.id.item_mymessagelist_content));
            email = ((TextView) itemView.findViewById(R.id.item_mymessagelist_email));
            gopro = ((TextView) itemView.findViewById(item_mymessagelist_gopro));
            listview = ((NoMoveListView) itemView.findViewById(R.id.item_mymessagelist_listview));
            listNum = (TextView) itemView.findViewById(R.id.item_mymessagelist_listview_num);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.item_ll);
            mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.item_rl);
            isNew = itemView.findViewById(R.id.is_new);
        }
    }

    public interface OnItemClickListening {
        void getView(View view, int position);
    }

    public OnItemClickListening listening;

    public OnItemClickListening getListening() {
        return listening;
    }

    public void setListening(OnItemClickListening listening) {
        this.listening = listening;
    }

    public void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < 2; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (2 - 1));
        listView.setLayoutParams(params);
    }
}
