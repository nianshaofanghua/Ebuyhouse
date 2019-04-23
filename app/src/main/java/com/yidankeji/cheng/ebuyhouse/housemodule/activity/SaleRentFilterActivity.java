package com.yidankeji.cheng.ebuyhouse.housemodule.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.housemodule.adapter.PostRentAdapter;
import com.yidankeji.cheng.ebuyhouse.housemodule.mode.PostRentListModel;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.RefreshFinishUtls;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

import java.util.ArrayList;

public class SaleRentFilterActivity extends Activity implements View.OnClickListener, PostRentAdapter.OnItemClickListening {
    private ArrayList<PostRentListModel.ContentBean.RowsBean> list = new ArrayList<>();
    private ArrayList<PostRentListModel.ContentBean.RowsBean.AttrValueListBean> mSonList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView nodata;
    private String TAG = "HouseType";
    private SmartRefreshLayout refreshLayout;
    private PostRentAdapter mAdapter;
    private boolean isFist = true;
    private int mIntentPosition;
    private TextView mSubmit;
    private String[] value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_rent_filter);
        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.housetype_yincang);
            mSubmit = (TextView) findViewById(R.id.action_bar_right);
            mSubmit.setText("Ok");
            mSubmit.setOnClickListener(this);
            mSubmit.setTextColor(getResources().getColor(R.color.text_red));
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.action_bar_title);
        Intent intent = getIntent();
        String stringTitle = intent.getStringExtra("title");
        mIntentPosition = intent.getIntExtra("position", 0);
        String temp = intent.getStringExtra("chose");

        if(!TextUtils.isEmpty(temp)){
            value = temp.split(",");
        }
        title.setText(stringTitle);
        recyclerView = (RecyclerView) findViewById(R.id.filter_houstype_recyclerview);
        nodata = (TextView) findViewById(R.id.filter_houstype_nodata);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.filter_houstype_refreshlayout);
        refreshLayout.autoRefresh();
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mSonList.clear();
                getHouseTypeHttp();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_bar_back:
                finish();
                break;
            case R.id.action_bar_right:
                Intent intent = getIntent();
                intent.putExtra("key", list.get(mIntentPosition).getAttr_key_id());
                String value = "";
                String name = "";
                for (PostRentListModel.ContentBean.RowsBean.AttrValueListBean bean :
                        mSonList) {
                    if (bean.isChose()) {
                        if (TextUtils.isEmpty(value)) {
                            value = bean.getValue_id();
                            name = bean.getValue_name();
                        } else {
                            value = value + "," + bean.getValue_id();
                            name = name + "," + bean.getValue_name();
                        }
                    }

                }
                intent.putExtra("value", value);
                intent.putExtra("name", name);
                setResult(1000, intent);
                finish();
                break;
            default:
                break;
        }
    }

    private void getHouseTypeHttp() {
        String token = SharedPreferencesUtils.getToken(SaleRentFilterActivity.this);
        MyApplication.getmMyOkhttp().get()
                .addHeader("Authorization", "Bearer " + token)
                .url(Constant.postroomfilter)
                .enqueue(new NewRawResponseHandler(SaleRentFilterActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "获取房屋类型", response);
                        RefreshFinishUtls.setFinish(refreshLayout);
                        PostRentListModel model = new Gson().fromJson(response, PostRentListModel.class);
                        if (model.getState() == 1) {
                            mSonList = (ArrayList<PostRentListModel.ContentBean.RowsBean.AttrValueListBean>) model.getContent().getRows().get(mIntentPosition).getAttr_value_list();
                            mAdapter = new PostRentAdapter(mSonList, SaleRentFilterActivity.this);
                            recyclerView.setLayoutManager(new LinearLayoutManager(SaleRentFilterActivity.this, LinearLayoutManager.VERTICAL, false));
                            mAdapter.setOnItemClickListening(SaleRentFilterActivity.this);
                            Log.e("logzz", "mSonList" + mSonList.size());
list = (ArrayList<PostRentListModel.ContentBean.RowsBean>) model.getContent().getRows();
                          if(value!=null&&value.length!=0){
                              for (String key:
                                      value) {
                                  for (PostRentListModel.ContentBean.RowsBean.AttrValueListBean row:
                                          mSonList) {
                                      if(key.equals(row.getValue_id())){
                                          row.setChose(true);
                                      }
                                  }
                              }
                          }
                            recyclerView.setAdapter(mAdapter);

                        }

                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "获取房屋类型", error_msg);
                        RefreshFinishUtls.setFinish(refreshLayout);
                        ToastUtils.showToast(SaleRentFilterActivity.this, getString(R.string.net_erro));
                    }
                });
    }

    @Override
    public void onItemClickListening(View view, int position) {
        if (list.get(mIntentPosition).getAttr_is_multiple() == 1) {
            if (mSonList.get(position).isChose()) {
                mSonList.get(position).setChose(false);
            } else {
                mSonList.get(position).setChose(true);
            }
        } else {
            for (PostRentListModel.ContentBean.RowsBean.AttrValueListBean bean :
                    mSonList) {
                bean.setChose(false);
            }
            mSonList.get(position).setChose(true);
        }

        mAdapter.notifyDataSetChanged();
    }


}
