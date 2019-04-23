package com.yidankeji.cheng.ebuyhouse.housemodule.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.adapter.PostRoom.PostRoomSelectAddressAdapter;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.mode.PostRoom.PostRoomSelectAddressMode;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.RefreshFinishUtls;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 上传房屋时 验证地理信息 ，选择地址页面
 */
public class PostRoomSelectAddressActivity extends Activity implements View.OnClickListener
        ,PostRoomSelectAddressAdapter.OnItemClickListening{


    private ArrayList<PostRoomSelectAddressMode> arrayList = new ArrayList<>();
    private boolean isFistCityList = true;
    private PostRoomSelectAddressAdapter addressAdapter;

    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private TextView nodata;
    private String tag;
    private String TAG = "PostRoom";
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postroom_selectaddress);

        tag = getIntent().getStringExtra("tag");
        if (tag == null){
            finish();
        }
        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.postroom_selectadd_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.postroom_selectadd_back);
        back.setOnClickListener(this);
        editText = (EditText) findViewById(R.id.postroom_selectadd_edit);
        if (tag.equals("city")){
            editText.setHint("Please enter the city keyword");
            editText.addTextChangedListener(textWatcher);
        }else{
            editText.setHint("The state doesn't need keywords");
        }
        TextView seach = (TextView) findViewById(R.id.postroom_selectadd_seach);
        seach.setVisibility(View.INVISIBLE);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.postroom_selectadd_refreshlayout);
        refreshLayout.autoRefresh();
        refreshLayout.setEnableLoadmore(false);
        recyclerView = (RecyclerView) findViewById(R.id.postroom_selectadd_recyclerview);
        nodata = (TextView) findViewById(R.id.postroom_selectadd_nodata);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                beginRequestHttp();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.postroom_selectadd_back:
                finish();
                break;
        }
    }

    //监听搜索框的变化
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
        @Override
        public void afterTextChanged(Editable s) {
            beginRequestHttp();
        }
    };

    /**
     * 开始请求数据
     *         根据不同的需求
     */
    private void beginRequestHttp(){
        String editTextContent = WindowUtils.getEditTextContent(editText);
        if (editTextContent == null){
            editTextContent = "";
        }
        switch (tag){
            case "city":
                arrayList.clear();
                getCityFormKeywordHttp(editTextContent);
                break;
            case "state":
                arrayList.clear();
                getStateFormHttp();
                break;
        }
    }

    /**
     * 根据城市关键字，搜索城市列表
     */
    private void getCityFormKeywordHttp(String keyword){
        MyApplication.getmMyOkhttp().get()
                .url(Constant.getCityFormKeyword+"str="+keyword)
                .enqueue(new NewRawResponseHandler(PostRoomSelectAddressActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"城市关键字" ,response );
                        RefreshFinishUtls.setFinish(refreshLayout);
                        getJSONData(response);
                    }
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        RefreshFinishUtls.setFinish(refreshLayout);
                        ToastUtils.showToast(PostRoomSelectAddressActivity.this , getString(R.string.net_erro));
                        Log.i(TAG+"城市关键字" ,error_msg );
                    }
                });
    }

    /**
     * 解析城市列表
     */
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
                        PostRoomSelectAddressMode mode = new PostRoomSelectAddressMode();
                        mode.setId(object.getString("id"));
                        mode.setCity(object.getString("city"));
                        mode.setLon(object.getDouble("lon"));
                        mode.setLat(object.getDouble("lat"));
                        mode.setFk_state_id(object.getString("fk_state_id"));
                        mode.setState(object.getString("state"));
                        arrayList.add(mode);
                    }

                    if (arrayList.size() == 0){
                        nodata.setVisibility(View.VISIBLE);
                    }else{
                        nodata.setVisibility(View.GONE);
                    }

                    if (isFistCityList){
                        addressAdapter = new PostRoomSelectAddressAdapter(PostRoomSelectAddressActivity.this , arrayList , tag);
                        addressAdapter.setListening(this);
                        recyclerView.setAdapter(addressAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(PostRoomSelectAddressActivity.this , LinearLayoutManager.VERTICAL , false));
                        isFistCityList = false;
                    }else{
                        addressAdapter.notifyDataSetChanged();
                    }

                }else if(state==703){
                    new LanRenDialog((Activity) PostRoomSelectAddressActivity.this).onlyLogin();

                }else {
                    ToastUtils.showToast( PostRoomSelectAddressActivity.this , jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(PostRoomSelectAddressActivity.this , getString(R.string.json_erro));
        }
    }

    /**
     * 获取州镇列表
     */
    private void getStateFormHttp(){
        MyApplication.getmMyOkhttp().get()
                .url(Constant.getStateFormHttp)
                .enqueue(new NewRawResponseHandler(PostRoomSelectAddressActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"州镇" , response);
                        getJSONDataOfSatate(response);
                        RefreshFinishUtls.setFinish(refreshLayout);
                    }
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        Log.i(TAG+"州镇" , error_msg);
                        RefreshFinishUtls.setFinish(refreshLayout);
                        ToastUtils.showToast(PostRoomSelectAddressActivity.this , getString(R.string.net_erro));
                    }
                });

    }

    /**
     * 解析州镇数据
     */
    private void getJSONDataOfSatate(String json){
        try {
            if (json != null){
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1){
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONArray rows = content.getJSONArray("rows");
                    for (int i = 0; i < rows.length() ; i++) {
                        JSONObject object = rows.getJSONObject(i);
                        PostRoomSelectAddressMode mode = new PostRoomSelectAddressMode();
                        mode.setId(object.getString("id"));
                        mode.setState(object.getString("state"));
                        arrayList.add(mode);
                    }

                    if (arrayList.size() == 0){
                        nodata.setVisibility(View.VISIBLE);
                    }else{
                        nodata.setVisibility(View.GONE);
                    }

                    if (isFistCityList){
                        addressAdapter = new PostRoomSelectAddressAdapter(PostRoomSelectAddressActivity.this , arrayList , tag);
                        addressAdapter.setListening(this);
                        recyclerView.setAdapter(addressAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(PostRoomSelectAddressActivity.this , LinearLayoutManager.VERTICAL , false));
                        isFistCityList = false;
                    }else{
                        addressAdapter.notifyDataSetChanged();
                    }
                }else if(state==703){
                    new LanRenDialog((Activity) PostRoomSelectAddressActivity.this).onlyLogin();

                }else {
                    ToastUtils.showToast( PostRoomSelectAddressActivity.this , jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(PostRoomSelectAddressActivity.this , getString(R.string.json_erro));
        }
    }

    @Override
    public void onItemClickListening(View view, int position) {
        PostRoomSelectAddressMode postRoomSelectAddressMode = arrayList.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("selectAddressMode" , postRoomSelectAddressMode);
        Intent intent = new Intent(PostRoomSelectAddressActivity.this , PostRoomActivity.class);
        intent.putExtras(bundle);
        if (tag.equals("city")){
            setResult(1006 , intent);
        }else{
            setResult(1007 , intent);
        }
        finish();
    }
}
