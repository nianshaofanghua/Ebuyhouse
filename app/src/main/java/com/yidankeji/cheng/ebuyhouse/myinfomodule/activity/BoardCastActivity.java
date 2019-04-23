package com.yidankeji.cheng.ebuyhouse.myinfomodule.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.housemodule.activity.ProductDetailsActivity;
import com.yidankeji.cheng.ebuyhouse.mode.ErrorModel;
import com.yidankeji.cheng.ebuyhouse.mode.PostRoom.BoardCastModel;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.RefreshFinishUtls;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BoardCastActivity extends AppCompatActivity implements View.OnClickListener, BoardCastListAdapter.OnItemClickListening {

    private ListView recyclerView;
    private SmartRefreshLayout refreshLayout;
    private Activity activity;
    private String TAG = "BoardCast";
    private BoardCastListAdapter mBoardCastListAdapter;
    private TextView nodata;
    ArrayList<BoardCastModel.ContentBean.RowsBean> mList;
    private TextView tv_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_cast);
        activity = BoardCastActivity.this;

        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.boardcast_yincang);
            yincang.setVisibility(View.VISIBLE);
            nodata = (TextView) findViewById(R.id.mymeaage_nodata);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        mList = new ArrayList<>();
        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("Broadcast");
        tv_right = (TextView) findViewById(R.id.action_bar_right);
        tv_right.setOnClickListener(this);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setTextColor(getResources().getColor(R.color.text_red));
        tv_right.setText("empty");
        recyclerView = (ListView) findViewById(R.id.boardcast_recyclerview);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.boardcast_refreshlayout);
        mBoardCastListAdapter = new BoardCastListAdapter(mList, BoardCastActivity.this);
        recyclerView.setAdapter(mBoardCastListAdapter);
        mBoardCastListAdapter.setListening(this);
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                getHttp();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                getHttp();
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
                clearMessage();
                break;
        }
    }


    private void getHttp() {
        String token = SharedPreferencesUtils.getToken(activity);
        Log.e("token", "" + token);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.pages)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(BoardCastActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "广播列表", response);
                        Log.e("board", "" + response);
                        RefreshFinishUtls.setFinish(refreshLayout);
                        BoardCastModel model = new Gson().fromJson(response, BoardCastModel.class);
                        if (model.getState() == 1) {
                            mList.clear();
                            mList.addAll(model.getContent().getRows());
                            if (mList.size() == 0) {
                                nodata.setVisibility(View.VISIBLE);
                            } else {
                                nodata.setVisibility(View.GONE);
                            }
                            mBoardCastListAdapter.setList(mList);
                        } else if (model.getState() == 703) {
                            new LanRenDialog(activity).onlyLogin();
                        }
                        getJsonData(response);
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "广播列表", error_msg);

                        RefreshFinishUtls.setFinish(refreshLayout);
                        ToastUtils.showToast(activity, getString(R.string.net_erro));
                    }
                });
    }

    private void getJsonData(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            int state = jsonObject.getInt("state");
            if (state == 1) {
                JSONObject content = jsonObject.getJSONObject("content");
                JSONArray rows = content.getJSONArray("rows");
                for (int i = 0; i < rows.length(); i++) {

                }
            } else if (state == 703) {
                new LanRenDialog(activity).onlyLogin();
            } else {
                ToastUtils.showToast(activity, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onItemClickListening(int firstPos, int position) {

        if (mList.get(firstPos).getRadio_msg().get(position).getRadio_type() == 4) {
            Intent intent = new Intent(BoardCastActivity.this, ProductDetailsActivity.class);
            intent.putExtra("prodetail_id", mList.get(firstPos).getRadio_msg().get(position).getSource_id());
            startActivity(intent);
        }

    }


    public void clearMessage() {
        String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp()
                .get()
                .url(Constant.clear_message.replace("message_type", "1"))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(BoardCastActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        ErrorModel errorModel = new Gson().fromJson(response, ErrorModel.class);
                        if (errorModel.getState() == 1) {
                            getHttp();
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });
    }
}
