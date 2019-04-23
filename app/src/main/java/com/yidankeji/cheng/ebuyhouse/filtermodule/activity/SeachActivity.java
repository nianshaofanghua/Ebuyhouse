package com.yidankeji.cheng.ebuyhouse.filtermodule.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.adapter.SeachListAdapter;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.housemodule.activity.ProductDetailsActivity;
import com.yidankeji.cheng.ebuyhouse.mainmodule.activity.MainActivity;
import com.yidankeji.cheng.ebuyhouse.mainmodule.activity.MainFragment;
import com.yidankeji.cheng.ebuyhouse.mode.MainFilterMode;
import com.yidankeji.cheng.ebuyhouse.mode.SeachMode;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.RefreshFinishUtls;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 首页搜索
 */
public class SeachActivity extends Activity implements SeachListAdapter.OnItemClickFromSeach
                ,View.OnClickListener{

    private ArrayList<SeachMode> seachList = new ArrayList<>();//联想词的集合
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private EditText editText;
    private String TAG = "SeachActivity";
    private TextView nodata;
    private SeachListAdapter adapter;
    private String keywordstring = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach);

        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.seach_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.seach_back);
        back.setOnClickListener(this);
        editText = (EditText) findViewById(R.id.seach_edit);
        editText.addTextChangedListener(textWatcher);
        nodata = (TextView) findViewById(R.id.seach_nodata);
        TextView seach = (TextView) findViewById(R.id.seach_seach);
        seach.setOnClickListener(this);
        /**/
        recyclerView = (RecyclerView) findViewById(R.id.seach_recyclerview);
        adapter = new SeachListAdapter(SeachActivity.this , seachList);
        adapter.setOnItemClickFromSeach(SeachActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(SeachActivity.this , LinearLayoutManager.VERTICAL , false));
       /**/
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.seach_refreshlayout);
        refreshLayout.autoRefresh();
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                getSeachDataHttp();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.seach_back:
                finish();
                break;
            case R.id.seach_seach:
                String srach_content = WindowUtils.getEditTextContent(editText);
                if (srach_content.isEmpty()){
                    return;
                }

                keywordstring = srach_content;
                getSeachDataHttp();
                break;
        }
    }

    /**
     * 点击其他区域  键盘消失
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (WindowUtils.isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 监听搜索框的变化
     */
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
        @Override
        public void afterTextChanged(Editable s) {

            String srach_content = editText.getText().toString().trim();
            keywordstring = srach_content;
            getSeachDataHttp();
        }
    };

    /**
     *   搜索的联想词
     */
    public void getSeachDataHttp(){
        Log.i(TAG+"联想词" , Constant.fcitystate+ "keyword="+ keywordstring);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.fcitystate+ "keyword="+ keywordstring)
                .enqueue(new NewRawResponseHandler(SeachActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        RefreshFinishUtls.setFinish(refreshLayout);
                        Log.i(TAG+"联想词" , response);
                        seachList.clear();
                        getJSONData(response);
                    }
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        RefreshFinishUtls.setFinish(refreshLayout);
                        Log.i(TAG+"联想词" , error_msg);
                    }});
    }

    /**
     * 解析数据
     */
    private void getJSONData(String json){
        try {
            if (json != null){
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");

                if(state==1){

                }

                else if(state==703){
                    new LanRenDialog((Activity)SeachActivity.this).onlyLogin();

                }else {
                    ToastUtils.showToast( SeachActivity.this , jsonObject.getString("message"));
                }
                JSONObject content = jsonObject.getJSONObject("content");
                JSONArray rows = content.getJSONArray("rows");
                for (int i = 0; i < rows.length() ; i++) {
                    JSONObject object = rows.getJSONObject(i);
                    SeachMode mode = new SeachMode();
                    mode.setKeyword(object.getString("keyword"));
                    mode.setId(object.getString("id"));
                    mode.setType(object.getString("type"));
                    seachList.add(mode);
                }

                if (seachList.size() > 0 ){
                    nodata.setVisibility(View.GONE);
                }else{
                    nodata.setVisibility(View.VISIBLE);
                }

                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param view
     * @param position
     */
    @Override
    public void OnItemClickFromSeach(View view, int position) {
        SeachMode seachMode = seachList.get(position);
        String type = seachMode.getType();
        if (type == null){
            return;
        }
        if (type.equals("city") || type.equals("state")){
            MainFilterMode mode = MainFragment.getMainFilterMode();
            mode.setName(seachMode.getKeyword());
            mode.setType(seachMode.getType());
            mode.setId(seachMode.getId());
            mode.setRefresh_list(true);
            mode.setRefresh_map(true);
            Intent intent = new Intent(SeachActivity.this , MainActivity.class);
            setResult(1001 , intent);
            finish();
        }else if (type.endsWith("street")){
            String prodetailID = seachMode.getId();
            if (prodetailID != null){
                Intent intent = new Intent(SeachActivity.this , ProductDetailsActivity.class);
                intent.putExtra("prodetail_id" ,prodetailID)  ;
                startActivity(intent);
                finish();
            }
        }
    }
}
