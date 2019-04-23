package com.yidankeji.cheng.ebuyhouse.loginmodule.activity;

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

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.loginmodule.adapter.PhoneCodeAdapter;
import com.yidankeji.cheng.ebuyhouse.loginmodule.mode.PhoneCodeMode;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.RefreshFinishUtls;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.interfaceUtils.InterfaceUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PhoneAreaCodeActivity extends Activity implements View.OnClickListener
        , InterfaceUtils.OnListItemClickListening{

    private ArrayList<PhoneCodeMode> list = new ArrayList<>();
    private String TAG = "PhoneAreaCode";
    private Activity activity;
    private RecyclerView recyclerView;
    private PhoneCodeAdapter adapter;
    private int aa = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_area_code);
        activity = PhoneAreaCodeActivity.this;

        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.phoneareacode_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("Select Country");
        recyclerView = (RecyclerView) findViewById(R.id.phoneareacode_recyclerview);
        adapter = new PhoneCodeAdapter(activity , list);
        adapter.setListening(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity , LinearLayoutManager.VERTICAL, false));
        getCodeHttp();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.action_bar_back:
                finish();
                break;
        }
    }

    private void getCodeHttp(){
        MyApplication.getmMyOkhttp().get()
                .url(Constant.areacode)
                .enqueue(new NewRawResponseHandler(PhoneAreaCodeActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"城市" , response);
                        getJsonData(response);
                    }
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        Log.i(TAG+"城市" , error_msg);
                        ToastUtils.showToast(activity , getString(R.string.net_erro));
                    }
                });
    }

    private void getJsonData(String json){
        try {
            if (json != null){
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1){
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONArray rows = content.getJSONArray("rows");
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject object = rows.getJSONObject(i);
                        PhoneCodeMode mode = new PhoneCodeMode();
                        mode.setId(object.getString("id"));
                        mode.setCode(object.getString("code"));
                        mode.setName(object.getString("country_name"));
                        String is_hot = object.getString("is_hot");
                        if (is_hot.equals("1")){
                            mode.setIs_hot(object.getString("is_hot"));
                        }else{
                            if (aa == 0){
                                mode.setIs_hot("0");
                            }else{
                                mode.setIs_hot("3");
                            }
                            aa = aa + 1;
                        }
                        list.add(mode);
                    }

                    adapter.notifyDataSetChanged();
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

    @Override
    public void onListItemClickListening(View view, int position) {
        PhoneCodeMode mode = list.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("mode" , mode);
        Intent intent = new Intent(activity , RegisteredPhoneActivity.class);
        intent.putExtras(bundle);
        setResult(1009 , intent);
        finish();
    }
}
