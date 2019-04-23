package com.yidankeji.cheng.ebuyhouse.myinfomodule.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import com.yidankeji.cheng.ebuyhouse.loginmodule.activity.LoginActivity;
import com.yidankeji.cheng.ebuyhouse.mode.ErrorModel;
import com.yidankeji.cheng.ebuyhouse.mode.NewContractModel;
import com.yidankeji.cheng.ebuyhouse.offermodule.ContractActivity;
import com.yidankeji.cheng.ebuyhouse.offermodule.MyZiJinDataActivity;
import com.yidankeji.cheng.ebuyhouse.offermodule.SubmitZiJinActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.RefreshFinishUtls;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.TokenLifeUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContractMessageActivity extends AppCompatActivity implements View.OnClickListener, ContractMessageAdapter.OnItemClickListening {
    private ListView recyclerView;
    private SmartRefreshLayout refreshLayout;
    private Activity activity;
    private String TAG = "BoardCast";
    private ContractMessageAdapter mBoardCastListAdapter;
    private TextView nodata;
    ArrayList<NewContractModel.ContentBean.RowsBean> mList;
private TextView tv_right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_message);
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
        tv_right = (TextView) findViewById(R.id.action_bar_right);
        tv_right.setOnClickListener(this);
        tv_right.setTextColor(getResources().getColor(R.color.text_red));
        mList = new ArrayList<>();
        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("Contract");
tv_right.setText("empty");
        recyclerView = (ListView) findViewById(R.id.boardcast_recyclerview);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.boardcast_refreshlayout);
        mBoardCastListAdapter = new ContractMessageAdapter(mList, ContractMessageActivity.this);
        recyclerView.setAdapter(mBoardCastListAdapter);
 mBoardCastListAdapter.setListening(this);
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mList.clear();
                getHttp();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mList.clear();
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
        String token = SharedPreferencesUtils.getToken(this);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.contract_intent)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(ContractMessageActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG + "广播列表", response);

                        RefreshFinishUtls.setFinish(refreshLayout);
                        NewContractModel model = new Gson().fromJson(response, NewContractModel.class);
                        if (model.getState() == 1) {
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
                    public void onFailure(Object object,int statusCode, String error_msg) {
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
        String id = mList.get(firstPos).getRadio_msg().get(position).getSource_id();
        switch (mList.get(firstPos).getRadio_msg().get(position).getRadio_type()) {
            case 1: // 消息列表
                Intent intent1 = new Intent(ContractMessageActivity.this, MyMessageActivity.class);

                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                break;
            case 2://off列表
                Intent intent2 = new Intent(ContractMessageActivity.this, ContractActivity.class);

                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                break;
            case 3://系统广播
                Intent intent3 = new Intent(ContractMessageActivity.this, BoardCastActivity.class);

                intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent3);

                break;
            case 4:// 房屋推送
                Log.e("prodetail_id", "房屋推送");
                Intent intent4 = new Intent(ContractMessageActivity.this, ProductDetailsActivity.class);
                intent4.putExtra("prodetail_id", id);
                intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent4);
                break;
            case 5:  //合同推送
                Intent intent5 = new Intent(ContractMessageActivity.this, ContractActivity.class);

                intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent5);
                break;
            case 6:  //资金凭证
                Log.e("推送消息", "推送消息推送消息");
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        getZiJinData(ContractMessageActivity.this);
                    }
                });

                break;
            case 7:  // 合同
                Intent intent7 = new Intent(ContractMessageActivity.this, ContractActivity.class);
                intent7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent7);
                break;
            default:
                break;
        }
    }

    /**
     * 资金验证
     */
    private void getZiJinData(final Context context) {
        String token = SharedPreferencesUtils.getToken(context);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.myfund)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(ContractMessageActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        boolean token = TokenLifeUtils.isToken(context, response);
                        if (token) {
                            getJsonDataZiJin(context, response);
                        } else {
                            SharedPreferencesUtils.setExit(context);
                            context.startActivity(new Intent(context, LoginActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        ToastUtils.showToast(context, context.getString(R.string.net_erro));
                    }
                });


    }

    private void getJsonDataZiJin(final Context context, String json) {
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONObject data = content.getJSONObject("data");
                    boolean isExist = data.getBoolean("isExist");
                    if (isExist) {
                        JSONArray rows = content.getJSONArray("rows");
                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject object = rows.getJSONObject(i);
                            String amount = object.getString("amount");
                            String url = object.getString("url");
                            String check_status = object.getString("check_status");
                            String add_time = object.getString("add_time");
                            String check_msg = object.getString("check_msg");

                            if (check_status == null || check_status.equals("")) {
                                context.startActivity(new Intent(context, SubmitZiJinActivity.class));
                            } else if (check_status.equals("3")) {
                                new LanRenDialog((Activity) context).getSystemHintDialog("Your voucher has been rejected and uploaded again"
                                        , "OK", new LanRenDialog.DialogDismisListening() {
                                            @Override
                                            public void getListening() {
                                                context.startActivity(new Intent(context, SubmitZiJinActivity.class));
                                            }
                                        });
                            } else if (check_status.equals("1") || check_status.equals("2")) {
                                Intent intent = new Intent(context, MyZiJinDataActivity.class);
                                intent.putExtra("amount", amount);
                                intent.putExtra("url", url);
                                intent.putExtra("check_status", check_status);
                                intent.putExtra("add_time", add_time);
                                intent.putExtra("check_msg", check_msg);
                                context.startActivity(intent);
                            }
                        }
                    } else {
                        context.startActivity(new Intent(context, SubmitZiJinActivity.class));
                    }
                } else if (state == 703) {
                    new LanRenDialog((Activity) context).onlyLogin();

                } else {
                    ToastUtils.showToast(context, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void clearMessage() {
        String token = SharedPreferencesUtils.getToken(ContractMessageActivity.this);
        MyApplication.getmMyOkhttp()
                .get()
                .url(Constant.clear_message.replace("message_type", "3"))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(ContractMessageActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        ErrorModel errorModel = new Gson().fromJson(response, ErrorModel.class);
                        if (errorModel.getState() == 1) {
                            mList.clear();
                            getHttp();
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });
    }
}
