package com.yidankeji.cheng.ebuyhouse.housemodule.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import com.wevey.selector.dialog.NormalAlertDialog;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.loginmodule.activity.LoginActivity;
import com.yidankeji.cheng.ebuyhouse.adapter.ProDetail.featureListAdapter;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.mode.ProDetail.ProCanShuMode;
import com.yidankeji.cheng.ebuyhouse.mode.ShowListMode;
import com.yidankeji.cheng.ebuyhouse.offermodule.SubmitOfferActivity;
import com.yidankeji.cheng.ebuyhouse.offermodule.SubmitZiJinActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.PhotoView.PhotoViewActivity;
import com.yidankeji.cheng.ebuyhouse.utils.PhotoView.PhotoViewUtils;
import com.yidankeji.cheng.ebuyhouse.utils.RefreshFinishUtls;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.TokenLifeUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.lunbotu.CBViewHolderCreator;
import com.yidankeji.cheng.ebuyhouse.utils.lunbotu.ConvenientBanner;
import com.yidankeji.cheng.ebuyhouse.utils.lunbotu.NetworkImageHolderView;
import com.yidankeji.cheng.ebuyhouse.utils.lunbotu.OnItemClickListener;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.CallPhoneUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

import static com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils.dismiss;

/**
 * 出租房屋详情页
 *         必须传来id
 */

public class ProRentDetailActivity extends SwipeBackActivity implements View.OnClickListener{

    private ArrayList<String> lunboList = new ArrayList<>();
    private ArrayList<ProCanShuMode> featureList = new ArrayList<>();
    private ArrayList<ProCanShuMode> buildingList = new ArrayList<>();
    private ShowListMode mode = new ShowListMode();
    private SmartRefreshLayout refreshLayout;
    private ConvenientBanner convenientBanner;
    private TextView tv_name ,tv_address ,tv_price ,
            tv_bedsNum ,tv_bathsNum ,tv_kitchenNum ,tv_sqftNum;

    private String TAG = "ProRentDetail";
    private String prodetail_id;
    private ListView featureListView ,buildingListView;
    private EditText tv_email ,tv_phone ,tv_mynotes;
    private ImageView collect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prorent_details);

        prodetail_id = getIntent().getStringExtra("prodetail_id");
        if (prodetail_id == null){
            ToastUtils.showToast(this , "Sorry, the data is missing");
            finish();
        }
        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.prodetais_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.prodetais_back);
        back.setOnClickListener(this);
        collect = (ImageView) findViewById(R.id.prodetais_collect);
        collect.setOnClickListener(this);

        convenientBanner = (ConvenientBanner) findViewById(R.id.prorentductdetais_lunbo);
        tv_name = (TextView) findViewById(R.id.prodetail_name);
        tv_price = (TextView) findViewById(R.id.prodetail_price);
        tv_address = (TextView) findViewById(R.id.prodetail_address);
        ImageView goMap = (ImageView) findViewById(R.id.productdetais_gomap);
        goMap.setOnClickListener(this);
        tv_bedsNum = (TextView) findViewById(R.id.prodetail_bedsnum);
        tv_bathsNum = (TextView) findViewById(R.id.prodetail_bathsnum);
        tv_kitchenNum = (TextView) findViewById(R.id.prodetail_kitchennum);
        tv_sqftNum = (TextView) findViewById(R.id.prodetail_sqftnum);
        featureListView = (ListView) findViewById(R.id.item_productdetail_canshu01);
        buildingListView = (ListView) findViewById(R.id.item_productdetail_canshu02);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.prorentdetais_refreshlayout);
        refreshLayout.autoRefresh();
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                lunboList.clear();
                featureList.clear();
                buildingList.clear();
                getHouseMessageByID();
                getHouseCanShuMessage();
            }
        });
        tv_email = (EditText) findViewById(R.id.productdetais_emial);
        tv_phone = (EditText) findViewById(R.id.productdetais_myphone);
        tv_mynotes = (EditText) findViewById(R.id.productdetais_mynotes);
        TextView tv_submit = (TextView) findViewById(R.id.productdetais_submit);
        tv_submit.setOnClickListener(this);
        TextView submitoff = (TextView) findViewById(R.id.productdetais_submitoff);
        submitoff.setOnClickListener(this);
        TextView callphone = (TextView) findViewById(R.id.productdetais_callphone);
        callphone.setOnClickListener(this);

        convenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                PhotoViewUtils.getPhotoView(ProRentDetailActivity.this , lunboList , position);
            }
        });
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
            case R.id.prodetais_back:
                finish();
                break;
            case R.id.prodetais_collect:
                setCollectForProduct();
                break;
            case R.id.productdetais_submit:
                addMyMessage();
                break;
            case R.id.productdetais_submitoff:
                getZiJin();
                break;
            case R.id.productdetais_callphone:
                new CallPhoneUtils(ProRentDetailActivity.this , mode.getCustomer_phone_number()).getDialog();
                break;
        }
    }

    /**
     * 按主键查询房屋所有信息
     */
    private void getHouseMessageByID(){
        String token = SharedPreferencesUtils.getToken(ProRentDetailActivity.this);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.fid+"house_id="+prodetail_id)
                .addHeader("Authorization" , "Bearer "+token)
                .enqueue(new NewRawResponseHandler(ProRentDetailActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.e("logs","fid=="+response);
                        Log.i(TAG+"房屋信息" , response);
                        RefreshFinishUtls.setFinish(refreshLayout);
                        getJSONHouse(response);
                    }
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        Log.i(TAG+"房屋信息" , error_msg);
                        Log.e("logs","fid=="+error_msg);
                        RefreshFinishUtls.setFinish(refreshLayout);
                        ToastUtils.showToast(ProRentDetailActivity.this , getString(R.string.net_erro));
                    }
                });
    }

    /**
     * 解析房屋数据
     */
    private void getJSONHouse(String json){
        try {
            if (json != null){
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1){
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONObject data = content.getJSONObject("data");

                    mode.setCustomer_phone_number(data.getString("customer_phone_number"));//手机号
                    mode.setCategory_name(data.getString("category_name"));//
                    mode.setIs_enable(data.getString("is_enable")+""); // 0初始化 1待审核 2启用
                    mode.setLatitude(data.getString("latitude")+"");//纬度
                    mode.setOrigin(data.getString("origin")); // 来源 pc或app
                    mode.setFk_city_id(data.getString("fk_city_id"));
                    mode.setDescription(data.getString("description"));//描述
                    mode.setRemark(data.getString("remark"));//备注
                    mode.setFk_state_id(data.getString("fk_state_id"));//州id
                    mode.setFk_customer_id(data.getString("fk_customer_id"));
                    mode.setIs_collect(data.getBoolean("is_collect"));//是否收藏
                    mode.setUpdate_time(data.getString("update_time")+"");//更新时间
                    mode.setCity_name(data.getString("city_name"));//城市名字
                    mode.setState_name(data.getString("state_name"));//州名字
                    mode.setPrice(data.getString("price"));//价格
                    mode.setStreet(data.getString("street"));//街道信息
                    mode.setId(data.getString("id"));//房屋id
                    mode.setKitchen(data.getString("kitchen"));//厨房数量
                    mode.setApn(data.getString("apn"));//房屋apn编码
                    mode.setBathroom(data.getString("bathroom"));//浴室数量
                    mode.setLongitude(data.getString("longitude")+"");//经度
                    mode.setProperty_price(data.getString("property_price"));//物业费
                    mode.setZip(data.getString("zip")+"");//邮编
                    mode.setLiving_sqft(data.getString("living_sqft")+"");//使用面积
                    mode.setJoinTime(data.getString("joinTime"));//
                    JSONArray img_code = data.getJSONArray("img_code");
                    for (int i = 0; i < img_code.length(); i++) {
                        String imagepath = (String) img_code.get(i);
                        lunboList.add(imagepath);
                    }
                    mode.setCustomer_head_url(data.getString("customer_head_url"));
                    mode.setYear_build(data.getString("year_build"));//建于哪一年
                    mode.setBedroom(data.getString("bedroom"));//卧室数量
                    mode.setCheck_status(data.getString("check_status"));//审核状态 0:初始化，1请求，2审核通过，3驳回
                    mode.setLot_sqft(data.getString("lot_sqft"));//占地面积
                    mode.setCap_rate(data.getString("cap_rate"));//回报率
                    mode.setFk_category_id(data.getString("fk_category_id"));//用户主键
                    mode.setImg_url(data.getString("img_url"));//
                    mode.setCustomer_nick_name(data.getString("customer_nick_name"));//
                    mode.setCustomer_email(data.getString("customer_email"));
                    mode.setAdd_time(data.getString("add_time")+"");//添加时间

                    setDataForProDetail();
                }else if(state==703){
                    new LanRenDialog((Activity) ProRentDetailActivity.this).onlyLogin();

                }else {
                    ToastUtils.showToast( ProRentDetailActivity.this, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取房屋的参数信息
     */
    private void getHouseCanShuMessage(){
        String token = SharedPreferencesUtils.getToken(ProRentDetailActivity.this);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.hinfo+"house_id="+prodetail_id)
                .addHeader("Authorization" , "Bearer "+token)
                .enqueue(new NewRawResponseHandler(ProRentDetailActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"房屋参数" , response);
                        getJSOnDataCanShu(response);
                    }
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        Log.i(TAG+"房屋参数" , error_msg);
                        ToastUtils.showToast(ProRentDetailActivity.this , getString(R.string.net_erro));
                    }
                });
    }

    /**
     * 解析房屋参数信息
     */
    private void getJSOnDataCanShu(String json){
        try {
            if (json != null){
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1){
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONObject data = content.getJSONObject("data");
                    JSONArray feature = data.getJSONArray("feature");
                    for (int i = 0; i < feature.length() ; i++) {
                        ProCanShuMode mode = new ProCanShuMode();
                        String string = feature.getString(i);
                        mode.setCanshuname(string);
                        featureList.add(mode);
                    }
                    JSONArray building = data.getJSONArray("building");
                    for (int i = 0; i < building.length() ; i++) {
                        ProCanShuMode mode = new ProCanShuMode();
                        String string = building.getString(i);
                        mode.setCanshuname(string);
                        buildingList.add(mode);
                    }

                    if (featureList.size() > 0){
                        featureListView.setVisibility(View.VISIBLE);
                        featureListAdapter adapter = new featureListAdapter(ProRentDetailActivity.this ,featureList  ,"aa" );
                        featureListView.setAdapter(adapter);
                        WindowUtils.setListViewHeightBasedOnChildren(featureListView);
                    }else{
                        featureListView.setVisibility(View.GONE);
                    }

                    if (buildingList.size() > 0){
                        buildingListView.setVisibility(View.VISIBLE);
                        featureListAdapter adapter = new featureListAdapter(ProRentDetailActivity.this ,buildingList , "bb" );
                        buildingListView.setAdapter(adapter);
                        WindowUtils.setListViewHeightBasedOnChildren(buildingListView);
                    }else{
                        buildingListView.setVisibility(View.GONE);
                    }

                }else if(state==703){
                    new LanRenDialog((Activity) ProRentDetailActivity.this).onlyLogin();

                }else {
                    ToastUtils.showToast( ProRentDetailActivity.this , jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(ProRentDetailActivity.this , getString(R.string.json_erro));
        }
    }

    /**
     * 获取到网络数据，更新UI
     */
    private void setDataForProDetail(){
        convenientBanner(lunboList , convenientBanner);
        tv_name.setText(mode.getState_name()+mode.getCity_name());
        tv_price.setText(mode.getPrice());
        tv_address.setText(mode.getStreet());
        tv_bedsNum.setText(mode.getBedroom()+"Beds");
        tv_bathsNum.setText(mode.getBathroom()+"Baths");
        tv_kitchenNum.setText(mode.getKitchen()+"Kitchen");
        tv_sqftNum.setText(mode.getLiving_sqft()+"Sqft");
        if (mode.is_collect()){
            collect.setImageResource(R.mipmap.pro_shoucang_ed);
        }else{
            collect.setImageResource(R.mipmap.pro_shoucang_e);
        }
    }

    /**
     * 加载轮播图
     */
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
     * 是否收藏
     */
    private void setCollectForProduct(){
        collect.setClickable(false);
        String token = SharedPreferencesUtils.getToken(ProRentDetailActivity.this);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.collect+"target_id="+prodetail_id)
                .addHeader("Authorization" , "Bearer "+token)
                .enqueue(new NewRawResponseHandler(ProRentDetailActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"收藏" , response);
                        collect.setClickable(true);
                        boolean token1 = TokenLifeUtils.getToken(ProRentDetailActivity.this, response);
                        if (token1){
                            getJSONCollectData(response);
                        }else{
                            SharedPreferencesUtils.setExit(ProRentDetailActivity.this);
                            startActivity(new Intent(ProRentDetailActivity.this , LoginActivity.class));
                        }
                    }
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        Log.i(TAG+"收藏" , error_msg);
                        collect.setClickable(true);
                        ToastUtils.showToast(ProRentDetailActivity.this , getString(R.string.net_erro));
                    }
                });
    }

    /**
     * 解析收藏json数据
     */
    private void getJSONCollectData(String json){
        try {
            if (json != null){
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 200){
                    String message = jsonObject.getString("message");
                    if (message.equals("success")){
                        collect.setImageResource(R.mipmap.pro_shoucang_ed);
                    }else{
                        collect.setImageResource(R.mipmap.pro_shoucang_e);
                    }
                }else if(state==703){
                    new LanRenDialog((Activity) ProRentDetailActivity.this).onlyLogin();

                }else {
                    ToastUtils.showToast( ProRentDetailActivity.this , jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加我的消息
     */
    private void addMyMessage(){
        String email = WindowUtils.getEditTextContent(tv_email);
        if (email.isEmpty()){
            ToastUtils.showToast(ProRentDetailActivity.this , "Please enter your email");
            return;
        }
        if (!email.contains("@")){
            ToastUtils.showToast(ProRentDetailActivity.this , "Please enter your email");
            return;
        }
        String phone = WindowUtils.getEditTextContent(tv_phone);
        if (phone.isEmpty()){
            ToastUtils.showToast(ProRentDetailActivity.this , "Please enter your phone");
            return;
        }
        if (phone.length() != 11){
            ToastUtils.showToast(ProRentDetailActivity.this , "Please enter your phone");
            return;
        }
        String notes = WindowUtils.getEditTextContent(tv_mynotes);
        if (notes.isEmpty()){
            ToastUtils.showToast(ProRentDetailActivity.this , "Please enter your notes");
            return;
        }

        LoadingUtils.showDialog(ProRentDetailActivity.this);
        String token = SharedPreferencesUtils.getToken(ProRentDetailActivity.this);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.mymessage)
                .addHeader("Authorization" , "Bearer "+token)
                .addParam("fk_seller_id" , mode.getFk_customer_id())
                .addParam("message" , notes)
                .addParam("house_id" , mode.getId())
                .addParam("phoner_number" , phone)
                .addParam("email" , email)
                .enqueue(new NewRawResponseHandler(ProRentDetailActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"添加信息" , response);
                        dismiss();
                        boolean token1 = TokenLifeUtils.getToken(ProRentDetailActivity.this, response);
                        if (token1){
                            getMyMessageJSON(response);
                        }else{
                            SharedPreferencesUtils.setExit(ProRentDetailActivity.this);
                            startActivity(new Intent(ProRentDetailActivity.this , LoginActivity.class));
                        }
                    }
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        Log.i(TAG+"添加信息" , error_msg);
                        dismiss();
                    }
                });
    }

    private void getMyMessageJSON(String json){
        try {
            if (json != null){
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                getSubmitSuccess(state , jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传成功提示
     */
    private void getSubmitSuccess(final int state , String str){
        if(state==1||state==200){
            new NormalAlertDialog.Builder(ProRentDetailActivity.this)
                    .setHeight(0.23f).setWidth(0.65f).setTitleVisible(true)
                    .setTitleText("System hint").setTitleTextColor(R.color.colorPrimary)
                    .setContentText(str).setContentTextColor(R.color.colorPrimaryDark)
                    .setSingleMode(true).setSingleButtonText("Close")
                    .setSingleButtonTextColor(R.color.colorAccent).setCanceledOnTouchOutside(true)
                    .setSingleListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tv_email.setText("");
                            tv_phone.setText("");
                            tv_mynotes.setText("");
                            dismiss();
                        }
                    }).build().show();
        }else if(state==703){
            new LanRenDialog((Activity) ProRentDetailActivity.this).onlyLogin();

        }else {
            ToastUtils.showToast( ProRentDetailActivity.this , str);
        }

    }

    /**
     * 判断资金凭证
     */
    private void getZiJin(){
        String token = SharedPreferencesUtils.getToken(ProRentDetailActivity.this);
        LoadingUtils.showDialog(ProRentDetailActivity.this);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.isupfunds)
                .addHeader("Authorization" , "Bearer "+token)
                .enqueue(new NewRawResponseHandler(ProRentDetailActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"资金凭证" , response);
                        LoadingUtils.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.getInt("state");
                            if (state == 1){
                                JSONObject content = jsonObject.getJSONObject("content");
                                JSONObject data = content.getJSONObject("data");
                                boolean isUpFunds = data.getBoolean("isUpFunds");
                                if (isUpFunds){
                                    Intent intent = new Intent(ProRentDetailActivity.this , SubmitOfferActivity.class);
                                    intent.putExtra("apn" , prodetail_id);
                                    startActivity(intent);
                                }else{
                                    Intent intent = new Intent(ProRentDetailActivity.this , SubmitZiJinActivity.class);
                                    intent.putExtra("apn" , prodetail_id);
                                    startActivity(intent);
                                }
                            }else if(state==703){
                                new LanRenDialog((Activity) ProRentDetailActivity.this).onlyLogin();

                            }else {
                                ToastUtils.showToast( ProRentDetailActivity.this , jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        Log.i(TAG+"资金凭证" , error_msg);
                        LoadingUtils.dismiss();
                    }
                });
    }
}
