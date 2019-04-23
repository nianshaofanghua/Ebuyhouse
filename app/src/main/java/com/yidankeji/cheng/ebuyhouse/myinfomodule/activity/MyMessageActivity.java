package com.yidankeji.cheng.ebuyhouse.myinfomodule.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.adapter.MyMessage.MyMessageListAdapter;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.housemodule.activity.ProductDetailsActivity;
import com.yidankeji.cheng.ebuyhouse.loginmodule.activity.LoginActivity;
import com.yidankeji.cheng.ebuyhouse.mode.ErrorModel;
import com.yidankeji.cheng.ebuyhouse.mode.MyHouseListMode;
import com.yidankeji.cheng.ebuyhouse.mode.MyMessageMode;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.RefreshFinishUtls;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.TokenLifeUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.MyMessagePopwindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyMessageActivity extends Activity implements View.OnClickListener
        , MyMessageListAdapter.OnItemClickListening {

    private ArrayList<MyMessageMode> datalist = new ArrayList<>();
    private int pageNumber = 1;
    private int pageSize = 20;
    private String TAG = "MyMessage";
    private SmartRefreshLayout refreshLayout;
    private TextView nodata;
    private RecyclerView recyclerView;
    private MyMessageListAdapter adapter;
    private FrameLayout yincang;
    private MyMessagePopwindow mMyMessagePopwindow;
    private TextView tv_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);

        initView();
    }

    private void initView() {
        EventBus.getDefault().register(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            yincang = (FrameLayout) findViewById(R.id.mymessage_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();

            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        tv_right = findViewById(R.id.action_bar_right);
        tv_right.setOnClickListener(this);
        tv_right.setText("empty");
        tv_right.setTextColor(getResources().getColor(R.color.text_red));
        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("My Message");
        recyclerView = (RecyclerView) findViewById(R.id.mymeaage_recycler);
        adapter = new MyMessageListAdapter(MyMessageActivity.this, datalist);
        adapter.setListening(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyMessageActivity.this, LinearLayoutManager.VERTICAL, false));
        nodata = (TextView) findViewById(R.id.mymeaage_nodata);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.mymeaage_refreshlayout);
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                pageNumber = 1;
                getHttpData();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                ++pageNumber;
                getHttpData();
            }
        });

        Intent intent = getIntent();
        String id = intent.getStringExtra("offerId");
        String messageCode = intent.getStringExtra("messagecode");
        if (id != null) {
            addMessage(id);
        }
        if (messageCode != null) {
            Log.e("logzz", "" + messageCode);
            jpushGetMessage(messageCode);
        }


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
            default:
                break;
        }
    }

    /**
     * 网络请求数据
     */
    private void getHttpData() {
        String token = SharedPreferencesUtils.getToken(MyMessageActivity.this);
        Log.i(TAG + "我的消息列表", Constant.mymessagelist + "pageNumber=" + pageNumber + "&pageSize=" + pageSize);
        Log.e("token", "" + token);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.mymessagelist)
                .addParam("pageNumber", pageNumber + "")
                .addParam("pageSize", pageSize + "")
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(MyMessageActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        RefreshFinishUtls.setFinish(refreshLayout);
                        Log.i(TAG + "我的消息列表", response);

                        boolean token1 = TokenLifeUtils.getToken(MyMessageActivity.this, response);
                        if (token1) {
                            if (pageNumber == 1) {
                                datalist.clear();
                            }
                            getJSONData(response);
                        } else {
                            SharedPreferencesUtils.setExit(MyMessageActivity.this);
                            startActivity(new Intent(MyMessageActivity.this, LoginActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "我的消息列表", error_msg);
                        RefreshFinishUtls.setFinish(refreshLayout);
                        //                  ToastUtils.showToast(MyMessageActivity.this, getString(R.string.net_erro));
                    }
                });
    }

    /**
     * 解析网络数据
     */
    private void getJSONData(String json) {
        String code = null;
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONArray rows = content.getJSONArray("rows");
                    Log.e("size", "" + rows.length());
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject object = rows.getJSONObject(i);
                        MyMessageMode mode = new MyMessageMode();
                        mode.setMessage(object.getString("message"));
                        mode.setEmail(object.getString("email"));
                        mode.setPhone_number(object.getString("phone_number"));
                        mode.setMessage_code(object.getString("message_code"));
                        mode.setFk_buyer_id(object.getString("fk_buyer_id"));
                        mode.setConsulte_id(object.getString("consulte_id"));
                        mode.setAdd_time(object.getString("add_time"));
                        code = mode.getMessage_code();
                        mode.setNickname(object.optString("nickname"));
                        mode.setHead_url(object.optString("head_url"));
                        mode.setHas_new(object.optInt("has_new"));
                        Log.e("error", mode.getHead_url() + "-----" + mode.getNickname());
                        mode.setHouse_type(object.optString("house_type"));

                        ArrayList<MyMessageMode> list = new ArrayList<>();
                        JSONArray replies = object.getJSONArray("replies");
                        for (int j = 0; j < replies.length(); j++) {
                            JSONObject object1 = replies.getJSONObject(j);
                            MyMessageMode mode1 = new MyMessageMode();
                            mode1.setMessage(object1.getString("message"));
                            mode1.setReply(object1.getString("reply"));
                            mode1.setAdd_time(object1.getString("add_time"));
                            Log.e("time", "" + mode1.getAdd_time());
                            list.add(mode1);
                        }

                        mode.setList(list);

                        datalist.add(mode);

                    }

                    //   getMessage(datalist.get(0).getMessage_code());
                    if (datalist.size() > 0) {
                        nodata.setVisibility(View.GONE);
                    } else {
                        nodata.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
//                    recyclerView.scrollToPosition(0);
//                    LinearLayoutManager mLayoutManager =
//                            (LinearLayoutManager) recyclerView.getLayoutManager();
//                    mLayoutManager.scrollToPositionWithOffset(0, 0);


//                    Handler handler = new Handler();
//
//                    Runnable runnable = new Runnable() {
//
//                        @Override
//                        public void run() {
//                            recyclerView.scrollToPosition(0);// 改变滚动条的位置
//                        }
//                    };
//                    handler.postDelayed(runnable, 50);

                } else if (state == 703) {
                    new LanRenDialog((Activity) MyMessageActivity.this).onlyLogin();

                } else {
                    ToastUtils.showToast(MyMessageActivity.this, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            Log.e("error", "" + code);

            e.printStackTrace();
        }
    }

    @Override
    public void getView(View view, int position) {
        MyMessageMode mode = datalist.get(position);
        switch (view.getId()) {
            case R.id.item_mymessagelist_reply:

                mMyMessagePopwindow = new MyMessagePopwindow(MyMessageActivity.this, mode, position);
                mMyMessagePopwindow.getPopwinsow(new MyMessagePopwindow.OnDismissListening() {
                    @Override
                    public void setDismiss() {
                        pageNumber = 1;
                        getHttpData();
                    }
                });


//                new MyMessagePopwindow(MyMessageActivity.this, mode, position).getPopwinsow(new MyMessagePopwindow.OnDismissListening() {
//                    @Override
//                    public void setDismiss() {
//
//                        pageNumber = 1;
//                        getHttpData();
//                    }
//                });
                break;
            case R.id.item_mymessagelist_gopro:

                String fk_buyer_id = mode.getFk_buyer_id();
                if (fk_buyer_id != null) {
                    Intent intent = new Intent(MyMessageActivity.this, ProductDetailsActivity.class);
                    intent.putExtra("prodetail_id", mode.getConsulte_id());
                    startActivity(intent);
                }
                break;
            case R.id.item_mymessagelist_listview_num:
                mMyMessagePopwindow = new MyMessagePopwindow(MyMessageActivity.this, mode, position);
                mMyMessagePopwindow.getPopwinsow(new MyMessagePopwindow.OnDismissListening() {
                    @Override
                    public void setDismiss() {
                        pageNumber = 1;
                        getHttpData();
                    }
                });
//                new MyMessagePopwindow(MyMessageActivity.this, mode, position).getPopwinsow(new MyMessagePopwindow.OnDismissListening() {
//                    @Override
//                    public void setDismiss() {
//
//                        pageNumber = 1;
//                        getHttpData();
//                    }
//                });
                break;
            case R.id.item_ll:
                mMyMessagePopwindow = new MyMessagePopwindow(MyMessageActivity.this, mode, position);
                mMyMessagePopwindow.getPopwinsow(new MyMessagePopwindow.OnDismissListening() {
                    @Override
                    public void setDismiss() {
                        pageNumber = 1;
                        getHttpData();
                    }
                });
//                new MyMessagePopwindow(MyMessageActivity.this, mode, position).getPopwinsow(new MyMessagePopwindow.OnDismissListening() {
//                    @Override
//                    public void setDismiss() {
//
//                        pageNumber = 1;
//                        getHttpData();
//                    }
//                });
                break;
            case R.id.item_rl:
                mMyMessagePopwindow = new MyMessagePopwindow(MyMessageActivity.this, mode, position);
                mMyMessagePopwindow.getPopwinsow(new MyMessagePopwindow.OnDismissListening() {
                    @Override
                    public void setDismiss() {
                        pageNumber = 1;
                        getHttpData();
                    }
                });
//                new MyMessagePopwindow(MyMessageActivity.this, mode, position).getPopwinsow(new MyMessagePopwindow.OnDismissListening() {
//                    @Override
//                    public void setDismiss() {
//
//                        pageNumber = 1;
//                        getHttpData();
//                    }
//                });
                break;
            case R.id.item_mymessagelist_content:
                mMyMessagePopwindow = new MyMessagePopwindow(MyMessageActivity.this, mode, position);
                mMyMessagePopwindow.getPopwinsow(new MyMessagePopwindow.OnDismissListening() {
                    @Override
                    public void setDismiss() {
                        pageNumber = 1;
                        getHttpData();
                    }
                });
//                new MyMessagePopwindow(MyMessageActivity.this, mode, position).getPopwinsow(new MyMessagePopwindow.OnDismissListening() {
//                    @Override
//                    public void setDismiss() {
//
//                        pageNumber = 1;
//                        getHttpData();
//                    }
//                });
                break;
            default:
                break;
        }
    }

    // offer页面跳转请求数据
    public void addMessage(String id) {
        String token = SharedPreferencesUtils.getToken(MyMessageActivity.this);
        MyApplication.getmMyOkhttp().get().
                url(Constant.contract_message.replace("offerid", id))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(MyMessageActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("log", "" + response);
                        new MyMessagePopwindow(MyMessageActivity.this, getJSONData01(response).get(0), 1).getPopwinsow(new MyMessagePopwindow.OnDismissListening() {
                            @Override
                            public void setDismiss() {
                                pageNumber = 1;
                                getHttpData();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.e("log", "" + error_msg);
                        ToastUtils.showToast(MyMessageActivity.this, getString(R.string.net_erro));
                    }
                });
    }


    /**
     * 解析网络数据
     */
    private ArrayList<MyMessageMode> getJSONData01(String json) {
        ArrayList<MyMessageMode> list01 = new ArrayList<>();
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    JSONObject object01 = jsonObject.getJSONObject("content");
                    JSONObject object = object01.getJSONObject("data");
                    MyMessageMode mode = new MyMessageMode();
                    mode.setMessage(object.getString("message"));
                    mode.setEmail(object.getString("email"));
                    mode.setPhone_number(object.getString("phone_number"));
                    mode.setMessage_code(object.getString("message_code"));
                    mode.setFk_buyer_id(object.getString("fk_buyer_id"));
                    mode.setConsulte_id(object.getString("consulte_id"));
                    mode.setAdd_time(object.getString("add_time"));
                    mode.setNickname(object.getString("nickname"));
                    mode.setHead_url(object.getString("head_url"));
                    mode.setHouse_type(object.getString("house_type"));

                    ArrayList<MyMessageMode> list = new ArrayList<>();
                    JSONArray replies = object.getJSONArray("replies");
                    for (int j = 0; j < replies.length(); j++) {
                        JSONObject object1 = replies.getJSONObject(j);
                        MyMessageMode mode1 = new MyMessageMode();
                        mode1.setMessage(object1.getString("message"));
                        mode1.setReply(object1.getString("reply"));
                        mode1.setAdd_time(object1.getString("add_time"));
                        Log.e("time", "" + mode1.getAdd_time());
                        list.add(mode1);
                    }

                    mode.setList(list);
                    list01.add(mode);


                } else if (state == 703) {
                    new LanRenDialog((Activity) MyMessageActivity.this).onlyLogin();

                } else {
                    ToastUtils.showToast(MyMessageActivity.this, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list01;
    }

    //极光推送跳转
    public void jpushGetMessage(String id) {
        String token = SharedPreferencesUtils.getToken(MyMessageActivity.this);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.jpush_message.replace("message_code", id))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(MyMessageActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("logzz", "" + response);


                        mMyMessagePopwindow = new MyMessagePopwindow(MyMessageActivity.this, getJSONData01(response).get(0), 1);
                        mMyMessagePopwindow.getPopwinsow(new MyMessagePopwindow.OnDismissListening() {
                            @Override
                            public void setDismiss() {
                                pageNumber = 1;
                                getHttpData();
                            }
                        });


//
//                        new MyMessagePopwindow(MyMessageActivity.this, getJSONData01(response).get(0), 1).getPopwinsow(new MyMessagePopwindow.OnDismissListening() {
//                            @Override
//                            public void setDismiss() {
//                                pageNumber = 1;
//                                getHttpData();
//                            }
//                        });
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                        //           ToastUtils.showToast(MyMessageActivity.this, getString(R.string.net_erro));
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void resh(MyHouseListMode mode) {
        if (mMyMessagePopwindow != null) {
            mMyMessagePopwindow.refresh(new MyMessageMode());
        }

    }


    public void getMessage(String code) {
        String token = SharedPreferencesUtils.getToken(MyMessageActivity.this);
        MyApplication.getmMyOkhttp().get()
                .addHeader("Authorization", "Bearer " + token)
                .url(Constant.jpush_message.replace("message_code", code))

                .enqueue(new NewRawResponseHandler(MyMessageActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("logzz", "" + response);
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });
    }

    public void clearMessage() {
        String token = SharedPreferencesUtils.getToken(MyMessageActivity.this);
        MyApplication.getmMyOkhttp()
                .get()
                .url(Constant.clear_message.replace("message_type", "2"))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(MyMessageActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        ErrorModel errorModel = new Gson().fromJson(response, ErrorModel.class);
                        if (errorModel.getState() == 1) {
                            pageNumber = 1;
                            getHttpData();
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });
    }
}
