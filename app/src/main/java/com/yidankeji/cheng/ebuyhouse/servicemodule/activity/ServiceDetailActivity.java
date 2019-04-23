package com.yidankeji.cheng.ebuyhouse.servicemodule.activity;

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
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.loginmodule.activity.LoginActivity;
import com.yidankeji.cheng.ebuyhouse.servicemodule.adapter.CommentsAdapter;
import com.yidankeji.cheng.ebuyhouse.servicemodule.adapter.ServiceItemAdapter;
import com.yidankeji.cheng.ebuyhouse.servicemodule.mode.CommentMode;
import com.yidankeji.cheng.ebuyhouse.servicemodule.mode.ServiceListMode;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.RefreshFinishUtls;
import com.yidankeji.cheng.ebuyhouse.utils.ScrollChangeScrollView;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.TokenLifeUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.lunbotu.CBViewHolderCreator;
import com.yidankeji.cheng.ebuyhouse.utils.lunbotu.ConvenientBanner;
import com.yidankeji.cheng.ebuyhouse.utils.lunbotu.NetworkImageHolderView;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class ServiceDetailActivity extends SwipeBackActivity implements View.OnClickListener{

    private ArrayList<String> bannerList = new ArrayList<>();
    private ArrayList<CommentMode> list = new ArrayList<>();
    private ConvenientBanner convenientBanner;
    private ServiceListMode serviceMode;
    private Activity activity;
    private int pageNumber = 1;
    private int pageSize = 20;
    private String TAG = "ServiceDetail";
    private SmartRefreshLayout refreshLayout;
    private CommentsAdapter commentsAdapter;
    private ServiceItemAdapter serviceItemAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        activity = ServiceDetailActivity.this;

        serviceMode = (ServiceListMode)getIntent().getSerializableExtra("serviceMode");
        if (serviceMode == null){
            finish();
        }
        initActionBar();
        initView();
    }

    private void initActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.servicedetail_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back1 = (ImageView) findViewById(R.id.servicedetail_back);
        back1.setOnClickListener(this);
        ImageView edit1 = (ImageView) findViewById(R.id.servicedetail_edit);
        edit1.setOnClickListener(this);
        final LinearLayout actionBarLayout = (LinearLayout) findViewById(R.id.servicedetail_layout);
        actionBarLayout.getBackground().setAlpha(0);
        ScrollChangeScrollView scrollView = (ScrollChangeScrollView) findViewById(R.id.servicedetail_scrollview);
        scrollView.setOnScrollListener(new ScrollChangeScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                if (scrollY < 100){
                    actionBarLayout.getBackground().setAlpha(0);
                }else{
                    actionBarLayout.getBackground().setAlpha(250);
                }
            }
        });
    }

    private void initView() {
        convenientBanner = (ConvenientBanner) findViewById(R.id.servicedetail_banber);
        ArrayList<ServiceListMode> imgUrlslist = serviceMode.getImgUrlslist();
        for (int i = 0; i < imgUrlslist.size(); i++) {
            bannerList.add(imgUrlslist.get(i).getImg_url());
        }
        convenientBanner(bannerList , convenientBanner);
        TextView name = (TextView) findViewById(R.id.servicedetail_name);
        name.setText(serviceMode.getCompany());
        /**/
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.servicedetail_serviceitem);
        serviceItemAdapter = new ServiceItemAdapter(activity , serviceMode.getServiceItemslist());
        recyclerView.setAdapter(serviceItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ServiceDetailActivity.this , LinearLayoutManager.HORIZONTAL , false));
       /**/
        TextView canshu1 = (TextView) findViewById(R.id.servicedetail_canshu1);
        TextView canshu2 = (TextView) findViewById(R.id.servicedetail_canshu2);
        TextView canshu3 = (TextView) findViewById(R.id.servicedetail_canshu3);
        TextView canshu4 = (TextView) findViewById(R.id.servicedetail_canshu4);
        canshu1.setText("Region:"+serviceMode.getCity()+" "+serviceMode.getState());
        canshu2.setText(serviceMode.getAddress());
        canshu3.setText(serviceMode.getContact());
        canshu4.setText(serviceMode.getPhone_number());
        /**/
        listView = (ListView) findViewById(R.id.servicedetail_listview);
        commentsAdapter = new CommentsAdapter(activity , list);
        listView.setAdapter(commentsAdapter);
        /**/
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.servicedetail_refreshlayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNumber = 1;

                getServiceCommentsList();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                ++pageNumber;
                getServiceCommentsList();
            }
        });
        getServiceCommentsList();
    }

    @Override
    public void onResume() {
        super.onResume();
        convenientBanner.startTurning(3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        convenientBanner.stopTurning();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.servicedetail_back:
                finish();
                break;
            case R.id.servicedetail_edit:
                boolean login = SharedPreferencesUtils.isLogin(activity);
                if (login){
                    Intent intent = new Intent(ServiceDetailActivity.this , AddCommentsActivity.class);
                    intent.putExtra("serviceID" , serviceMode.getId());
                    startActivityForResult(intent , 100);
                }else{
                    startActivity(new Intent(activity , LoginActivity.class));
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 1008){
            refreshLayout.autoRefresh();
        }
    }

    public void convenientBanner(ArrayList<String> list, ConvenientBanner Banner) {
        String[] image = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            image[i] = list.get(i);
        }
        List<String> networkImages = Arrays.asList(image);
        Banner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, networkImages).setPageIndicator(new int[] { R.mipmap.ic_page_indicator, R.mipmap.jiahao_xuxian });
    }

    /**
     * 获取评论列表
     */
    private void getServiceCommentsList(){
        String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.evaluates)
                .addHeader("Authorization" , "Bearer "+token)
                .addParam("partner_id" , serviceMode.getId())
                .addParam("pageNumber" , pageNumber+"")
                .addParam("pageSize" , pageSize+"")
                .enqueue(new NewRawResponseHandler(ServiceDetailActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"评论列表" , response);
                        RefreshFinishUtls.setFinish(refreshLayout);
                        boolean token1 = TokenLifeUtils.getToken(activity, response);
                        if (token1){
                            if(pageNumber==1){
                                list.clear();
                            }
                            getJSONData(response);
                        }else{
                            SharedPreferencesUtils.setExit(activity);
                            startActivity(new Intent(activity , LoginActivity.class));
                        }
                    }
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        Log.i(TAG+"评论列表" , error_msg);
                        RefreshFinishUtls.setFinish(refreshLayout);
                        ToastUtils.showToast(activity , getString(R.string.net_erro));
                    }
                });
    }

    private void getJSONData(String json){
        try {
            if (json != null){
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1){
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONArray rows = content.getJSONArray("rows");
                    for (int i = 0; i < rows.length() ; i++) {
                        JSONObject object = rows.getJSONObject(i);
                        CommentMode mode = new CommentMode();
                        mode.setEvaluate(object.getString("evaluate"));
                        mode.setAdd_time(object.getString("add_time"));
                        JSONObject customer = object.getJSONObject("customer");
                        mode.setNickname(customer.getString("nickname"));
                        mode.setHead_url(customer.getString("head_url"));
                        ArrayList<String> imgUrlsList = new ArrayList<>();
                        JSONArray imgUrls = object.getJSONArray("imgUrls");
                        for (int j = 0; j < imgUrls.length() ; j++) {
                            String string = (String)imgUrls.get(j);
                            imgUrlsList.add(string);
                        }
                        mode.setImgUrlsList(imgUrlsList);
                        list.add(mode);
                    }
                    commentsAdapter.notifyDataSetChanged();
                    WindowUtils.setListViewHeightBasedOnChildren(listView);
                }else if(state==703){
                    new LanRenDialog((Activity) activity).onlyLogin();

                }else {
                    ToastUtils.showToast( activity , jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
