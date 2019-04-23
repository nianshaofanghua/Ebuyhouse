package com.yidankeji.cheng.ebuyhouse.filtermodule.activity;

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

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.adapter.FilterHouseTypeAdapter;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.mode.FilterMode;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.RefreshFinishUtls;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class FilerHouseTypeActivity extends SwipeBackActivity implements View.OnClickListener
                , FilterHouseTypeAdapter.OnItemClickListening{

    private ArrayList<FilterMode> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView nodata;
    private String TAG = "HouseType";
    private SmartRefreshLayout refreshLayout;
    private boolean isFist = true;
    private FilterHouseTypeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filer_house_type);

        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.housetype_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("Propery Types");
        recyclerView = (RecyclerView) findViewById(R.id.filter_houstype_recyclerview);
        nodata = (TextView) findViewById(R.id.filter_houstype_nodata);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.filter_houstype_refreshlayout);
        refreshLayout.autoRefresh();
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                list.clear();
                getHouseTypeHttp();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.action_bar_back:
                finish();
                break;
            default:
                break;
        }
    }

    private void getHouseTypeHttp(){
        MyApplication.getmMyOkhttp().get()
                .url(Constant.category)
                .enqueue(new NewRawResponseHandler(FilerHouseTypeActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"获取房屋类型" , response);
                        RefreshFinishUtls.setFinish(refreshLayout);
                        getJSONData(response);
                    }
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        Log.i(TAG+"获取房屋类型" , error_msg);
                        RefreshFinishUtls.setFinish(refreshLayout);
                        ToastUtils.showToast(FilerHouseTypeActivity.this , getString(R.string.net_erro));
                    }
                });
    }

    /**
     * 对数据进行解析
     */
    private void getJSONData(String json){
        try {
            if (json != null){
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1){
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONArray rows = content.getJSONArray("rows");
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject object = rows.getJSONObject(i);
                        FilterMode mode = new FilterMode();
                        mode.setId(object.getString("id"));
                        mode.setImg_url(object.getString("img_url"));
                        mode.setType(object.getString("type"));
                        list.add(mode);
                    }

                }else if(state==703){
                    new LanRenDialog(FilerHouseTypeActivity.this).onlyLogin();

                }else {
                    ToastUtils.showToast(FilerHouseTypeActivity.this , jsonObject.getString("message"));
                }


                if (list.size() > 0){
                    nodata.setVisibility(View.GONE);
                }else{
                    nodata.setVisibility(View.VISIBLE);
                }
                if(isFist){
                    adapter = new FilterHouseTypeAdapter(FilerHouseTypeActivity.this , list);
                    adapter.setListening(this);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(FilerHouseTypeActivity.this , LinearLayoutManager.VERTICAL , false));
                    isFist = false;
                } else{
                    adapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(FilerHouseTypeActivity.this , getString(R.string.json_erro));
        }
    }

    @Override
    public void onItemClickListening(View view, int position) {
        FilterMode mode = list.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("houseType" , mode);
        Intent intent = new Intent(FilerHouseTypeActivity.this , FilterActivity.class);
        intent.putExtras(bundle);
        setResult(1005 , intent);
        finish();
    }
}
