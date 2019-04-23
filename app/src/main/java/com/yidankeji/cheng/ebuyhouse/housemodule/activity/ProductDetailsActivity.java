package com.yidankeji.cheng.ebuyhouse.housemodule.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.makeramen.roundedimageview.RoundedImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.adapter.ProDetail.featureListAdapter;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.community.chat.ui.activity.ChatActivity;
import com.yidankeji.cheng.ebuyhouse.housemodule.adapter.MoreListAdapter;
import com.yidankeji.cheng.ebuyhouse.housemodule.httputils.SubmitRoomHttpUtils;
import com.yidankeji.cheng.ebuyhouse.housemodule.mode.MoreListModel;
import com.yidankeji.cheng.ebuyhouse.loginmodule.activity.LoginActivity;
import com.yidankeji.cheng.ebuyhouse.mode.ProDetail.ProCanShuMode;
import com.yidankeji.cheng.ebuyhouse.mode.ShowListMode;
import com.yidankeji.cheng.ebuyhouse.myinfomodule.activity.MyMessageActivity;
import com.yidankeji.cheng.ebuyhouse.myinfomodule.mode.MyRentRoomModel;
import com.yidankeji.cheng.ebuyhouse.offermodule.ContractActivity;
import com.yidankeji.cheng.ebuyhouse.offermodule.MyZiJinDataActivity;
import com.yidankeji.cheng.ebuyhouse.offermodule.SubmitZiJinActivity;
import com.yidankeji.cheng.ebuyhouse.offermodule.httputils.OfferHttpUtils;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.DrawableCenterTextView;
import com.yidankeji.cheng.ebuyhouse.utils.JumpUtils;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.PhotoView.PhotoViewUtils;
import com.yidankeji.cheng.ebuyhouse.utils.RefreshFinishUtls;
import com.yidankeji.cheng.ebuyhouse.utils.ScrollChangeScrollView;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.TimeUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.TokenLifeUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.interfaceUtils.InterfaceUtils;
import com.yidankeji.cheng.ebuyhouse.utils.lunbotu.CBViewHolderCreator;
import com.yidankeji.cheng.ebuyhouse.utils.lunbotu.ConvenientBanner;
import com.yidankeji.cheng.ebuyhouse.utils.lunbotu.NetworkImageHolderView;
import com.yidankeji.cheng.ebuyhouse.utils.lunbotu.OnItemClickListener;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.CallPhoneUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.ImageLogoUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * 出售房屋详情页
 * 必须传来id
 */
public class ProductDetailsActivity extends SwipeBackActivity implements View.OnClickListener {

    private ArrayList<String> lunboList = new ArrayList<>();
    private ArrayList<ProCanShuMode> featureList = new ArrayList<>();
    private ArrayList<ProCanShuMode> buildingList = new ArrayList<>();
    private ShowListMode mode = new ShowListMode();
    private SmartRefreshLayout refreshLayout;
    private ConvenientBanner convenientBanner;
    private TextView tv_name, tv_address, tv_price,
            tv_bedsNum, tv_bathsNum, tv_kitchenNum, tv_sqftNum;
    private String TAG = "ProductDetails";
    private String prodetail_id;
    private ListView featureListView, buildingListView;
    private ImageView collect;
    private EditText tv_email, tv_phone, tv_mynotes;
    private Activity activity;
    private RoundedImageView touxiang;
    private TextView tv_name1, submitoff, tv_details, tv_img_pos;
    private int imgSize;
    private ImageView mVideoIcon;
    private RelativeLayout mRe;
    private ListView mMoreListView;
    private TextView mIsVis;
    private TextView callphone;
    private String token;
    private TextView mViewNum, mSaveNum;
    DrawableCenterTextView goVideo;
    ScrollChangeScrollView scrollView;
    private LinearLayout mHouseMessageLl;
    private DrawableCenterTextView goMap;
    private int reHight;
    private int mapHight;
    private int llHight;
    private int buildHight;
    private int featureHight;
    private ArrayList<MoreListModel> listModels;
    private int moreHight;
    private TextView mTitle;
    private ImageView mImg01, mImg02, mImg03;
    private ImageView mDeleteImg01, mDeleteImg02, mDeleteImg03;
    private String mStringImg01, mStringImg02, mStringImg03;
    ArrayList<String> mPicList;
    private int mPicPos;
    private ArrayList<String> mSubmitPic;
    private ImageView mShare;
    private ImageView mImgCollect;
    private TextView mCollectNum, mLookNum;
    private String getVideoUrl;
    private TextView mComDetail, mTitleContact;
    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        activity = ProductDetailsActivity.this;
        token = SharedPreferencesUtils.getToken(ProductDetailsActivity.this);
        prodetail_id = getIntent().getStringExtra("prodetail_id");
        //getIntent().getStringExtra("id");

        addLookHouseLog(prodetail_id);
        if (prodetail_id == null) {
            ToastUtils.showToast(this, "Sorry, the data is missing");
            finish();
        }
        initView();
        initActionBar();
    }

    //  页面加载完成得到控件高度
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        reHight = mRe.getHeight();
        mapHight = goMap.getHeight();
        llHight = mHouseMessageLl.getHeight();

    }

    @SuppressLint("NewApi")
    private void initView() {
        convenientBanner = (ConvenientBanner) findViewById(R.id.productdetais_lunbo);
        convenientBanner.setPointViewVisible(false);
        tv_name = (TextView) findViewById(R.id.prodetail_name);
        tv_price = (TextView) findViewById(R.id.prodetail_price);
        tv_img_pos = (TextView) findViewById(R.id.img_pos);
        tv_address = (TextView) findViewById(R.id.prodetail_address);
        mVideoIcon = (ImageView) findViewById(R.id.video_icon);
        mViewNum = (TextView) findViewById(R.id.see_num);
        mSaveNum = (TextView) findViewById(R.id.savenum);
        mTitle = (TextView) findViewById(R.id.title);
        mComDetail = (TextView) findViewById(R.id.com_detail);
        mView = findViewById(R.id.gray_layout);
        mSaveNum.setOnClickListener(this);
        mIsVis = (TextView) findViewById(R.id.isvis);
        mIsVis.setOnClickListener(this);
        mRe = (RelativeLayout) findViewById(R.id.re);
        mHouseMessageLl = (LinearLayout) findViewById(R.id.house_message_ll);
        mShare = (ImageView) findViewById(R.id.share);
        mShare.setOnClickListener(this);
        mImgCollect = (ImageView) findViewById(R.id.collect);
        mImgCollect.setOnClickListener(this);
        mCollectNum = (TextView) findViewById(R.id.collect_num);
        mLookNum = (TextView) findViewById(R.id.watch_num);
        mTitleContact = (TextView) findViewById(R.id.title_contact);
//        mImg01 = (ImageView) findViewById(R.id.img_1);
//        mImg02 = (ImageView) findViewById(R.id.img_2);
//        mImg03 = (ImageView) findViewById(R.id.img_3);
//        mImg01.setOnClickListener(this);
//        mImg02.setOnClickListener(this);
//        mImg03.setOnClickListener(this);
//
//        mDeleteImg01 = (ImageView) findViewById(R.id.item_selectxiangce_delete);
//        mDeleteImg02 = (ImageView) findViewById(R.id.item_selectxiangce_delete01);
//        mDeleteImg03 = (ImageView) findViewById(R.id.item_selectxiangce_delete02);
//        mDeleteImg01.setOnClickListener(this);
//        mDeleteImg02.setOnClickListener(this);
//        mDeleteImg03.setOnClickListener(this);


        mPicList = new ArrayList<>();
        mPicList.add("");
        mPicList.add("");
        mPicList.add("");

        int hight = getScreenHeight(this);
        final LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mRe.getLayoutParams(); //取控件textView当前的布局参数
        linearParams.height = hight / 5 * 2;// 控件的高强制设成20
        mSubmitPic = new ArrayList<>();
        mRe.setLayoutParams(linearParams); //
        goMap = (DrawableCenterTextView) findViewById(R.id.productdetais_gomap);
        goMap.setOnClickListener(this);
        goVideo = (DrawableCenterTextView) findViewById(R.id.productdetais_govideo);
        goVideo.setOnClickListener(this);
        tv_bedsNum = (TextView) findViewById(R.id.prodetail_bedsnum);
        tv_bathsNum = (TextView) findViewById(R.id.prodetail_bathsnum);
        tv_kitchenNum = (TextView) findViewById(R.id.prodetail_kitchennum);
        tv_sqftNum = (TextView) findViewById(R.id.prodetail_sqftnum);
        featureListView = (ListView) findViewById(R.id.item_productdetail_canshu01);
        buildingListView = (ListView) findViewById(R.id.item_productdetail_canshu02);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.prodetais_refreshlayout);
        mMoreListView = (ListView) findViewById(R.id.item_more_product);
        mMoreListView.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(getIntent().getStringExtra("json"))) {
            setMoreList(getIntent().getStringExtra("json"));
        } else {
            mIsVis.setVisibility(View.GONE);
        }

        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initHttp();
            }
        });
        /**/
        tv_details = (TextView) findViewById(R.id.prodetail_details);
        LinearLayout details_layout = (LinearLayout) findViewById(R.id.prodetail_details_layout);
        /**/
//        tv_email = (EditText) findViewById(R.id.productdetais_emial);
//        tv_phone = (EditText) findViewById(R.id.productdetais_myphone);
//        if (SharedPreferencesUtils.isLogin(ProductDetailsActivity.this)) {
//            if (SharedPreferencesUtils.getParam(activity, "type", "").equals("email")) {
//                tv_email.setText((String) SharedPreferencesUtils.getParam(activity, "email_phone", ""));
//            } else {
//                tv_phone.setText((String) SharedPreferencesUtils.getParam(activity, "email_phone", ""));
//            }
//        } else {
//
//        }

        tv_mynotes = (EditText) findViewById(R.id.productdetais_mynotes);
        TextView tv_submit = (TextView) findViewById(R.id.productdetais_submit);
        tv_submit.setText("Talk to the homeowner");
        tv_submit.setOnClickListener(this);
        submitoff = (TextView) findViewById(R.id.productdetais_submitoff);
        submitoff.setOnClickListener(this);
        callphone = (TextView) findViewById(R.id.productdetais_callphone);
        callphone.setOnClickListener(this);
        touxiang = (RoundedImageView) findViewById(R.id.productdetais_touxiang);
        tv_name1 = (TextView) findViewById(R.id.productdetais_name);


        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.prodetais_yincanglayout);
        layout.getBackground().setAlpha(0);
        int bannerHeight = mRe.getLayoutParams().height;
        final int height = bannerHeight - 200;

        scrollView = (ScrollChangeScrollView) findViewById(R.id.prodetais_scrollview);
        //  标题悬浮
        scrollView.setOnScrollListener(new ScrollChangeScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {

                if (scrollY < height) {
                    mTitle.setVisibility(View.GONE);
                    layout.getBackground().setAlpha(0);
                } else {
                    mTitle.setVisibility(View.VISIBLE);
                    layout.getBackground().setAlpha(250);
                }

                if ((scrollY > reHight + mapHight + llHight) && reHight != 0) {
                    mTitle.setText("Feature");
                }
                if ((scrollY > reHight + mapHight + llHight + buildHight) && reHight != 0) {
                    mTitle.setText("Building");

                }
                if (listModels != null) {
                    if ((scrollY > reHight + mapHight + llHight + buildHight + featureHight) && reHight != 0) {
                        for (int i = 0; i < listModels.size(); i++) {
                            if (mMoreListView.getVisibility() == View.VISIBLE) {
                                if (listModels.get(i).getTitle() != null && !TextUtils.isEmpty(listModels.get(i).getTitle())) {
                                    if (scrollY > reHight + mapHight + llHight + buildHight + featureHight + (moreHight / listModels.size() * i)) {
                                        mTitle.setText(listModels.get(i).getTitle());
                                    }

                                }
                            }

                        }

                    }
                }
                Log.e("hight", scrollY + "----" + (reHight + mapHight + llHight + buildHight + featureHight + moreHight) + "---" + moreHight + "---" + mComDetail.getHeight());
                if (mMoreListView.getVisibility() == View.GONE) {
                    moreHight = 0;
                }
                if (scrollY > (reHight + mapHight + llHight + buildHight + featureHight + moreHight + mComDetail.getHeight())) {
                    mTitle.setText(mComDetail.getText());
                    if (scrollY > (reHight + mapHight + llHight + buildHight + featureHight + moreHight + mComDetail.getHeight()) + tv_details.getHeight() + mTitleContact.getHeight()) {
                        mTitle.setText(mTitleContact.getText());
                    }

                }


            }
        });

    }

    private void initActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.prodetais_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }

        ImageView back = (ImageView) findViewById(R.id.prodetais_back);
        back.setOnClickListener(this);
        collect = (ImageView) findViewById(R.id.prodetais_collect);
        collect.setOnClickListener(this);

        convenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                if (position == 0) {
                    if (getVideoUrl != null) {
                        getVideoUrl(getVideoUrl);
                    }
                } else {
                    PhotoViewUtils.getPhotoView(ProductDetailsActivity.this, lunboList, position);
                }
            }
        });
        convenientBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                tv_img_pos.setText(position + 1 + "/" + imgSize);
                if (!TextUtils.isEmpty(getVideoUrl)) {

                    if (position == 0) {
                        goVideo.setVisibility(View.VISIBLE);
                        mVideoIcon.setVisibility(View.VISIBLE);
                    } else {
                        mVideoIcon.setVisibility(View.GONE);

                    }

                } else {
                    mVideoIcon.setVisibility(View.GONE);

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initHttp();
    }

    private void initHttp() {
        lunboList.clear();
        featureList.clear();
        buildingList.clear();
        //  LoadingUtils.showDialog(ProductDetailsActivity.this);
        getHouseMessageByID();
        getHouseCanShuMessage();
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
        String a = mPicList.get(0);
        String b = mPicList.get(1);
        String c = mPicList.get(2);

        switch (v.getId()) {
            case R.id.prodetais_back:
                finish();
                break;
            case R.id.prodetais_collect:
                boolean login3 = SharedPreferencesUtils.isLogin(activity);
                if (login3) {
                    setCollectForProduct();
                } else {
                    startActivity(new Intent(activity, LoginActivity.class));
                }
                break;
            case R.id.productdetais_gomap:
                String longitude = mode.getLongitude();
                String latitude = mode.getLatitude();
                Intent intent1 = new Intent(ProductDetailsActivity.this, Map02Activity.class);
                intent1.putExtra("longitude", longitude);
                intent1.putExtra("latitude", latitude);
                startActivity(intent1);
                break;
            case R.id.productdetais_govideo:
                String post_videopath = mode.getPost_videopath();

                if (getVideoUrl != null) {
                    getVideoUrl(getVideoUrl);
                }


                break;
            case R.id.productdetais_submit:


                boolean login2 = SharedPreferencesUtils.isLogin(activity);
                if (login2) {

                    getRoomId(mode.getFk_customer_id(),mode.getCustomer_nick_name());
//                    ArrayList<LocalMedia> list = new ArrayList<>();
//                    for (String path :
//                            mPicList) {
//                        if (!TextUtils.isEmpty(path)) {
//                            LocalMedia media = new LocalMedia();
//                            media.setCompressPath(path);
//                            list.add(media);
//                        }
//
//                    }
//                    String fk_category_id = mode.getFk_customer_id();
//                    String userID = (String) SharedPreferencesUtils.getParam(activity, "userID", "");
//                    if (fk_category_id.equals(userID)) {
//
//                        ToastUtils.showToast(activity, "This is your own house");
//                        return;
//                    }
//                    String email = WindowUtils.getEditTextContent(tv_email);
//                    if (email == null) {
//                        email = "";
//                    }
//
//                    String phone = WindowUtils.getEditTextContent(tv_phone);
//                    if (phone.isEmpty()) {
//
//                        ToastUtils.showToast(ProductDetailsActivity.this, "Please enter your phone");
//                        return;
//                    }
//
//                    String notes = WindowUtils.getEditTextContent(tv_mynotes);
//                    if (notes.isEmpty()) {
//
//                        ToastUtils.showToast(ProductDetailsActivity.this, "Please enter your notes");
//                        return;
//                    }
//
//                    if (list.size() != 0) {
//                        submitImagr(list);
//                    } else {
//                        addMyMessage();
//                    }
//

                } else {
                    startActivity(new Intent(activity, LoginActivity.class));
                }
                break;
            case R.id.productdetais_submitoff:
                boolean login1 = SharedPreferencesUtils.isLogin(activity);
                if (login1) {
                    getZiJin();

                } else {
                    startActivity(new Intent(activity, LoginActivity.class));
                }
                break;
            case R.id.productdetais_callphone:
                boolean login = SharedPreferencesUtils.isLogin(activity);
                if (login) {
                    if (TextUtils.isEmpty(mode.getCustomer_phone_number()) && !mode.getCustomer_email().isEmpty()) {

                        sendEmail(activity, mode.getCustomer_email());
                    } else {
                        new CallPhoneUtils(ProductDetailsActivity.this, mode.getCustomer_phone_number()).getDialog();
                    }


                } else {
                    startActivity(new Intent(activity, LoginActivity.class));
                }
                break;
            case R.id.isvis:

                Drawable drawableLeft_nochose = getResources().getDrawable(
                        R.mipmap.red_arrow_down);
                Drawable drawableLeft_chose = getResources().getDrawable(
                        R.mipmap.red_arrow_up);
                drawableLeft_nochose.setBounds(0, 0, drawableLeft_nochose.getIntrinsicWidth(), drawableLeft_nochose.getIntrinsicHeight());
                drawableLeft_chose.setBounds(0, 0, drawableLeft_chose.getIntrinsicWidth(), drawableLeft_chose.getIntrinsicHeight());
                if (mIsVis.getText().equals("More")) {
                    mMoreListView.setVisibility(View.VISIBLE);
                    mIsVis.setText("Less");
                    mIsVis.setCompoundDrawables(null, null, drawableLeft_chose, null);

                } else {
                    mMoreListView.setVisibility(View.GONE);
                    mIsVis.setText("More");
                    mIsVis.setCompoundDrawables(null, null, drawableLeft_nochose, null);
                }
                break;
            case R.id.savenum:
                boolean login4 = SharedPreferencesUtils.isLogin(activity);
                if (login4) {
                    setCollectForProduct();
                } else {
                    startActivity(new Intent(activity, LoginActivity.class));
                }
                break;
            case R.id.img_1:
                mPicPos = 0;

                if (a.equals("")) {
                    new ImageLogoUtils(activity).getImageLogoDialog(3);
                }


                switch (mPicList.size()) {
                    case 0:
                        new ImageLogoUtils(activity).getImageLogoDialog(3);
                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    default:
                        break;
                }
                break;
            case R.id.img_2:
                mPicPos = 1;

                if (b.equals("")) {
                    new ImageLogoUtils(activity).getImageLogoDialog(2);
                }

                switch (mPicList.size()) {
                    case 0:
                        break;
                    case 1:
                        new ImageLogoUtils(activity).getImageLogoDialog(2);
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }
                break;
            case R.id.img_3:

                Log.e("", "");
                if (c.equals("")) {
                    new ImageLogoUtils(activity).getImageLogoDialog(1);
                }

                mPicPos = 2;
                switch (mPicList.size()) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        new ImageLogoUtils(activity).getImageLogoDialog(1);
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }
                break;
            case R.id.item_selectxiangce_delete:


                if (mStringImg02 != null && mStringImg03 != null) {
                    mPicList.set(0, mStringImg02);
                    mPicList.set(1, mStringImg03);
                    mPicList.set(2, "");
                    mStringImg01 = mStringImg02;
                    mStringImg02 = mStringImg03;
                    mStringImg03 = null;

                    Glide.with(activity).load(mPicList.get(0)).into(mImg01);
                    Glide.with(activity).load(mPicList.get(1)).into(mImg02);

                    mDeleteImg03.setVisibility(View.GONE);
                    mImg03.setImageResource(R.mipmap.xuqiudan_xiangce);
                } else if (mStringImg02 != null) {
                    mPicList.set(0, mStringImg02);
                    mPicList.set(1, "");
                    mStringImg01 = mStringImg02;
                    mStringImg02 = null;
                    Glide.with(activity).load(mPicList.get(0)).into(mImg01);
                    mDeleteImg03.setVisibility(View.GONE);
                    mImg03.setVisibility(View.GONE);
                    mImg02.setImageResource(R.mipmap.xuqiudan_xiangce);
                    mDeleteImg02.setVisibility(View.GONE);
                } else {
                    mPicList.set(0, "");
                    mStringImg01 = null;
                    mImg01.setImageResource(R.mipmap.xuqiudan_xiangce);
                    mDeleteImg01.setVisibility(View.GONE);
                    mImg02.setVisibility(View.GONE);


                }


                break;
            case R.id.item_selectxiangce_delete01:

                if (mStringImg03 != null) {

                    mPicList.set(1, mStringImg03);
                    mPicList.set(2, "");
                    Log.e("message", "" + mPicList.get(2));
                    mStringImg02 = mStringImg03;
                    mStringImg03 = null;
                    Glide.with(activity).load(mPicList.get(1)).into(mImg02);
                    mDeleteImg03.setVisibility(View.GONE);
                    mImg03.setImageResource(R.mipmap.xuqiudan_xiangce);


                } else {

                    mPicList.set(1, "");
                    mPicList.set(2, "");
                    mStringImg02 = null;
                    mImg02.setImageResource(R.mipmap.xuqiudan_xiangce);
                    mDeleteImg02.setVisibility(View.GONE);
                    mImg03.setVisibility(View.GONE);


                }

                break;
            case R.id.item_selectxiangce_delete02:

                if (mStringImg02 != null) {

                    mPicList.set(1, mStringImg03);
                    mPicList.set(2, "");
                    mStringImg02 = mStringImg03;
                    mStringImg03 = null;
                    Glide.with(activity).load(mPicList.get(1)).into(mImg02);

                    mDeleteImg03.setVisibility(View.GONE);
                    mImg03.setImageResource(R.mipmap.xuqiudan_xiangce);
                }

                break;
            case R.id.share:
                /// new  ImageLogoUtils(ProductDetailsActivity.this).getToShare(prodetail_id);

                new ImageLogoUtils(ProductDetailsActivity.this).showShare(mShare, prodetail_id, mView);
                break;
            case R.id.collect:
                boolean login5 = SharedPreferencesUtils.isLogin(activity);
                if (login5) {
                    setCollectForProduct();
                } else {
                    startActivity(new Intent(activity, LoginActivity.class));
                }
                break;

            default:
                break;
        }
    }

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
     * 按主键查询房屋所有信息
     */
    private void getHouseMessageByID() {
        String token = SharedPreferencesUtils.getToken(ProductDetailsActivity.this);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.fid + "house_id=" + prodetail_id)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(ProductDetailsActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("logs", "response==" + response);
                        Log.i(TAG + "房屋信息", response);
                        RefreshFinishUtls.setFinish(refreshLayout);
                        getJSONHouse(response);
                        //LoadingUtils.dismiss();
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "房屋信息", error_msg);
                        RefreshFinishUtls.setFinish(refreshLayout);
                        // LoadingUtils.dismiss();
                        ToastUtils.showToast(ProductDetailsActivity.this, getString(R.string.net_erro));
                    }
                });
    }

    /**
     * 解析房屋数据
     */
    private void getJSONHouse(String json) {
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONObject data = content.getJSONObject("data");
                    mode.setCustomer_phone_number(data.getString("customer_phone_number"));//手机号
                    mode.setCategory_name(data.getString("category_name"));//
                    mode.setIs_enable(data.getString("is_enable") + ""); // 0初始化 1待审核 2启用
                    mode.setLatitude(data.getString("latitude") + "");//纬度
                    mode.setOrigin(data.getString("origin")); // 来源 pc或app
                    mode.setFk_city_id(data.getString("fk_city_id"));
                    mode.setDescription(data.getString("description"));//描述
                    mode.setRemark(data.getString("remark"));//备注
                    mode.setFk_state_id(data.getString("fk_state_id"));//州id
                    mode.setFk_customer_id(data.getString("fk_customer_id"));
                    mode.setIs_collect(data.getBoolean("is_collect"));//是否收藏
                    mode.setUpdate_time(data.getString("update_time") + "");//更新时间
                    mode.setCity_name(data.getString("city_name"));//城市名字
                    mode.setState_name(data.getString("state_name"));//州名字
                    mode.setPrice(data.getString("price"));//价格
                    mode.setStreet(data.getString("street"));//街道信息
                    mode.setId(data.getString("id"));//房屋id
                    mode.setVideo_first_pic(data.getString("video_first_pic"));
                    mode.setKitchen(data.getString("kitchen") + "");//厨房数量
                    mode.setApn(data.getString("apn"));//房屋apn编码
                    mode.setBathroom(data.getString("bathroom") + "");//浴室数量
                    mode.setLongitude(data.getString("longitude") + "");//经度
                    mode.setProperty_price(data.getString("property_price"));//物业费
                    mode.setZip(data.getString("zip") + "");//邮编
                    mode.setLiving_sqft(data.getString("living_sqft") + "");//使用面积
                    mode.setJoinTime(data.getString("joinTime"));//
                    mode.setRelease_type(data.getString("release_type"));
                    mode.setGarage(data.optString("garage"));
                    mode.setViewnum(data.getInt("viewNum"));
                    mode.setSavenum(data.getInt("saveNum"));
                    JSONArray img_code = data.getJSONArray("img_code");
                    if (!TextUtils.isEmpty(mode.getVideo_first_pic())) {
                        if (mode.getVideo_first_pic().contains("null")) {
                            mVideoIcon.setVisibility(View.GONE);
                            goVideo.setVisibility(View.GONE);

                        } else {
                            lunboList.add(mode.getVideo_first_pic());
                        }

                    }
                    for (int i = 0; i < img_code.length(); i++) {
                        String imagepath = (String) img_code.get(i);
                        lunboList.add(imagepath);
                    }
                    mode.setCustomer_head_url(data.getString("customer_head_url"));
                    mode.setYear_build(data.getString("year_build") + "");//建于哪一年
                    mode.setBedroom(data.getString("bedroom") + "");//卧室数量
                    mode.setCheck_status(data.getString("check_status") + "");//审核状态 0:初始化，1请求，2审核通过，3驳回
                    mode.setLot_sqft(data.getString("lot_sqft") + "");//占地面积
                    mode.setCap_rate(data.getString("cap_rate") + "");//回报率
                    mode.setFk_category_id(data.getString("fk_category_id"));//用户主键
                    mode.setImg_url(data.getString("img_url"));//
                    mode.setCustomer_nick_name(data.getString("customer_nick_name"));//
                    mode.setCustomer_email(data.getString("customer_email"));
                    mode.setAdd_time(data.getString("add_time") + "");//添加时间
                    mode.setCustomer_head_url(data.getString("customer_head_url"));

                    if (data.getString("video_url") != null && !data.getString("video_url").equals("")) {
                        goVideo.setVisibility(View.VISIBLE);
                        mVideoIcon.setVisibility(View.VISIBLE);
                        getVideoUrl = data.getString("video_url");
                        Log.e("video", "" + getVideoUrl);
                    }


                    if (TextUtils.isEmpty(mode.getCustomer_phone_number()) && !mode.getCustomer_email().isEmpty()) {
                        callphone.setText("Email");
                    }
                    setDataForProDetail();
                } else if (state == 703) {
                    new LanRenDialog((Activity) ProductDetailsActivity.this).onlyLogin();

                } else {
                    ToastUtils.showToast(ProductDetailsActivity.this, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取房屋的参数信息
     */
    private void getHouseCanShuMessage() {
        String token = SharedPreferencesUtils.getToken(ProductDetailsActivity.this);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.hinfo + "house_id=" + prodetail_id)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(ProductDetailsActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "房屋参数", response);
                        //   getJSOnDataCanShu(response);
                        getJson(response);
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "房屋参数", error_msg);
                        ToastUtils.showToast(ProductDetailsActivity.this, getString(R.string.net_erro));
                    }
                });
    }


    /**
     * 添加数据
     */
    private void setDataForProDetail() {
        convenientBanner(lunboList, convenientBanner);
        tv_name.setText(mode.getStreet());//mode.getStreet()
        tv_price.setText("$" + mode.getPrice());

        if (mode.getRelease_type().equals("rent")) {
            tv_price.setText("$" + mode.getPrice() + "/MO");
        }
        mSaveNum.setText("" + mode.getSavenum());
        mCollectNum.setText("" + mode.getSavenum());

        mViewNum.setText("" + mode.getViewnum());
        mLookNum.setText("" + mode.getViewnum());

        tv_address.setText(mode.getCity_name());///mode.getState_name()+
        tv_bedsNum.setText(mode.getBedroom() + "");
        tv_bathsNum.setText(mode.getBathroom() + "");
        tv_kitchenNum.setText(mode.getGarage() + "");
        tv_sqftNum.setText(mode.getLiving_sqft() + "");
        tv_details.setText(mode.getDescription());
        Glide.with(activity).load(mode.getCustomer_head_url())
                .apply(MyApplication.getOptions_touxiang()).into(touxiang);
        tv_name1.setText(mode.getCustomer_nick_name());


        Drawable drawable01 = getResources().getDrawable(R.mipmap.pro_shoucang_ed);
        drawable01.setBounds(0, 0, drawable01.getIntrinsicWidth(), drawable01.getIntrinsicHeight());
        Drawable drawable = getResources().getDrawable(R.mipmap.pro_shoucang_e);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        if (mode.is_collect()) {
            mImgCollect.setImageResource(R.mipmap.pro_shoucang_ed);
            mSaveNum.setCompoundDrawables(drawable01, null, null, null);

        } else {
            mImgCollect.setImageResource(R.mipmap.pro_shoucang_e);
            mSaveNum.setCompoundDrawables(drawable, null, null, null);
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
        imgSize = list.size();
        if (imgSize == 1) {
            Banner.stopTurning();
        }
        List<String> networkImages = Arrays.asList(image);
        Banner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, networkImages).setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.jiahao_xuxian});
    }

    /**
     * 是否收藏
     */
    private void setCollectForProduct() {
        collect.setClickable(false);
        mImgCollect.setClickable(false);
        String token = SharedPreferencesUtils.getToken(ProductDetailsActivity.this);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.collect + "target_id=" + prodetail_id)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(ProductDetailsActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "收藏", response);
                        collect.setClickable(true);
                        mImgCollect.setClickable(true);
                        boolean token1 = TokenLifeUtils.getToken(ProductDetailsActivity.this, response);
                        if (token1) {
                            getJSONCollectData(response);
                        } else {
                            SharedPreferencesUtils.setExit(ProductDetailsActivity.this);
                            startActivity(new Intent(ProductDetailsActivity.this, LoginActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "收藏", error_msg);
                        collect.setClickable(true);
                        mImgCollect.setClickable(true);
                        ToastUtils.showToast(ProductDetailsActivity.this, getString(R.string.net_erro));
                    }
                });
    }

    /**
     * 解析收藏json数据
     */
    private void getJSONCollectData(String json) {

        Drawable drawable01 = getResources().getDrawable(R.mipmap.pro_shoucang_ed);
        drawable01.setBounds(0, 0, drawable01.getIntrinsicWidth(), drawable01.getIntrinsicHeight());
        Drawable drawable = getResources().getDrawable(R.mipmap.pro_shoucang_e);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        if (mode.is_collect()) {
//            collect.setImageResource(R.mipmap.pro_shoucang_ed);
//            mSaveNum.setCompoundDrawables(drawable01, null, null, null);
            mImgCollect.setImageResource(R.mipmap.pro_shoucang_ed);
        } else {
//            collect.setImageResource(R.mipmap.pro_shoucang_e);
//            mImgCollect.setImageResource(R.mipmap.pro_shoucang_e);
            mSaveNum.setCompoundDrawables(drawable, null, null, null);
        }
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 200) {

                    String message = jsonObject.getString("message");
                    ToastUtils.showToast(ProductDetailsActivity.this, jsonObject.getString("message"));
                    if (message.equals("Success")) {

//                        collect.setImageResource(R.mipmap.pro_shoucang_ed);
//                        mSaveNum.setCompoundDrawables(drawable01, null, null, null);
//                        mSaveNum.setText((Integer.valueOf(mSaveNum.getText().toString()) + 1) + "");
                        mCollectNum.setText((Integer.valueOf(mCollectNum.getText().toString()) + 1) + "");
                        mImgCollect.setImageResource(R.mipmap.pro_shoucang_ed);
                    } else {
//                        collect.setImageResource(R.mipmap.pro_shoucang_e);
//                        mSaveNum.setCompoundDrawables(drawable, null, null, null);
//                        mSaveNum.setText((Integer.valueOf(mSaveNum.getText().toString()) - 1) + "");
                        mImgCollect.setImageResource(R.mipmap.pro_shoucang_e);
                        mCollectNum.setText((Integer.valueOf(mCollectNum.getText().toString()) - 1) + "");
                    }
                } else if (state == 703) {
                    new LanRenDialog((Activity) ProductDetailsActivity.this).onlyLogin();

                } else {
                    ToastUtils.showToast(ProductDetailsActivity.this, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加我的消息
     */
    private void addMyMessage() {


        String picPath = "";
        for (String pic :
                mSubmitPic) {

            if (!pic.equals("")) {
                if (picPath.equals("")) {
                    picPath = pic;
                } else {
                    picPath = picPath + "," + pic;
                }
            }

        }

        String email = WindowUtils.getEditTextContent(tv_email);

        String phone = WindowUtils.getEditTextContent(tv_phone);


        String notes = WindowUtils.getEditTextContent(tv_mynotes);


        if (TextUtils.isEmpty(picPath)) {
            LoadingUtils.showDialog(this);
        }

        String token = SharedPreferencesUtils.getToken(ProductDetailsActivity.this);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.mymessage)
                .addHeader("Authorization", "Bearer " + token)
                .addParam("fk_seller_id", mode.getFk_customer_id())
                .addParam("message", notes)
                .addParam("house_id", mode.getId())
                .addParam("phoner_number", phone)
                .addParam("email", email + "")
                .addParam("capitals", picPath)
                .enqueue(new NewRawResponseHandler(ProductDetailsActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "添加信息", response);
                        LoadingUtils.dismiss();
                        boolean token1 = TokenLifeUtils.getToken(ProductDetailsActivity.this, response);
                        if (token1) {
                            getMyMessageJSON(response);
                        } else {
                            SharedPreferencesUtils.setExit(ProductDetailsActivity.this);
                            startActivity(new Intent(ProductDetailsActivity.this, LoginActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "添加信息", error_msg);
                        LoadingUtils.dismiss();
                    }
                });
    }

    private void getMyMessageJSON(String json) {
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    new LanRenDialog(activity).getSystemHintDialog("Uploaded successfully"
                            , "OK", new LanRenDialog.DialogDismisListening() {
                                @Override
                                public void getListening() {
                                    tv_email.setText("");
                                    tv_phone.setText("");
                                    tv_mynotes.setText("");

                                    String currentTime = TimeUtils.getCurrentTime();
                                    if (SharedPreferencesUtils.isLogin(ProductDetailsActivity.this)) {
                                        SharedPreferencesUtils.setParam(ProductDetailsActivity.this, "me_time", currentTime);
                                        startActivity(new Intent(ProductDetailsActivity.this, MyMessageActivity.class));
                                    } else {
                                        startActivity(new Intent(ProductDetailsActivity.this, LoginActivity.class));
                                    }


                                }
                            });
                } else if (state == 703) {
                    new LanRenDialog((Activity) ProductDetailsActivity.this).onlyLogin();

                } else if (state == 200) {
                    //   ToastUtils.showToast(ProductDetailsActivity.this, "Uploaded successfully");
                    Toast.makeText(ProductDetailsActivity.this, "Uploaded successfully", Toast.LENGTH_LONG).show();
                } else {
                    ToastUtils.showToast(ProductDetailsActivity.this, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断资金凭证
     */
    private void getZiJin() {
        String token = SharedPreferencesUtils.getToken(ProductDetailsActivity.this);
        LoadingUtils.showDialog(ProductDetailsActivity.this);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.aaverify)
                .addHeader("Authorization", "Bearer " + token)
                .addParam("apn", mode.getApn())
                .enqueue(new NewRawResponseHandler(ProductDetailsActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "资金凭证", response);
                        LoadingUtils.dismiss();
                        boolean token1 = TokenLifeUtils.getToken(activity, response);
                        if (token1) {
                            getJsonPingZhengData(response);
                        } else {
                            SharedPreferencesUtils.setExit(activity);
                            startActivity(new Intent(activity, LoginActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "资金凭证", error_msg);
                        LoadingUtils.dismiss();
                    }
                });
    }

    // 解析凭证
    private void getJsonPingZhengData(String json) {
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONObject data = content.getJSONObject("data");
                    int anInt = data.getInt("state");
                    if (anInt == 5) {
                        new LanRenDialog(activity).getZiJinDialog(new LanRenDialog.DialogDismisListening() {
                            @Override
                            public void getListening() {
//                                Intent intent = new Intent(ProductDetailsActivity.this, SubmitZiJinActivity.class);
//                                intent.putExtra("apn", prodetail_id);
//                                startActivity(intent);
                                getZiJinData();
                            }
                        });
                    } else if (anInt == 1) {
                        Intent intent = new Intent(ProductDetailsActivity.this, ContractActivity.class);
                        intent.putExtra("apn", mode.getApn());
                        startActivity(intent);
                    } else {
                        ToastUtils.showToast(ProductDetailsActivity.this, data.getString("message"));
                    }
                } else if (state == 703) {
                    new LanRenDialog((Activity) ProductDetailsActivity.this).onlyLogin();

                } else {
                    ToastUtils.showToast(ProductDetailsActivity.this, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 解析房屋信息
    public void getJson(String json) {
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                ProCanShuMode mode0 = new ProCanShuMode();

                if (state == 1) {

                    JSONObject content = jsonObject.getJSONObject("content");

                    JSONObject data = content.getJSONObject("data");
                    Iterator<String> keys = data.keys();
                    //    Iterator iterator = keys.iterator();
                    int count = 0;
                    while (keys.hasNext()) {

                        String key = (String) keys.next();

                        if (count == 0) {
                            ProCanShuMode build = new ProCanShuMode();
                            build.setCanshuname(key);
                            buildingList.add(build);
                            JSONArray building = data.getJSONArray(key);
                            for (int i = 0; i < building.length(); i++) {
                                ProCanShuMode mode = new ProCanShuMode();
                                String string = building.getString(i);
                                mode.setCanshuname(string);
                                buildingList.add(mode);
                            }
                        }
                        if (count == 1) {
                            ProCanShuMode features = new ProCanShuMode();
                            features.setCanshuname(key);
                            featureList.add(features);
                            JSONArray feature = data.getJSONArray(key);
                            for (int i = 0; i < feature.length(); i++) {
                                ProCanShuMode mode = new ProCanShuMode();
                                Log.e("tag", "tag==" + feature.optString(i));
                                String string = feature.optString(i);
                                mode.setCanshuname(string);
                                featureList.add(mode);
                            }
                            Log.e("tag", "key1: " + key);
                        }
                        count++;
                    }


                    if (featureList.size() > 0) {
                        featureListView.setVisibility(View.VISIBLE);
                        featureListAdapter adapter = new featureListAdapter(ProductDetailsActivity.this, featureList, "aa");
                        featureListView.setAdapter(adapter);


                        buildHight = WindowUtils.setListViewHeightBasedOnChildren01(featureListView);
                    } else {
                        featureListView.setVisibility(View.GONE);
                    }

                    if (buildingList.size() > 0) {
                        buildingListView.setVisibility(View.VISIBLE);
                        featureListAdapter adapter = new featureListAdapter(ProductDetailsActivity.this, buildingList, "bb");
                        buildingListView.setAdapter(adapter);
                        featureHight = WindowUtils.setListViewHeightBasedOnChildren01(buildingListView);


                        //   buildHight = buildingListView.getHeight();
                    } else {
                        buildingListView.setVisibility(View.GONE);
                    }

                } else if (state == 703) {
                    new LanRenDialog((Activity) ProductDetailsActivity.this).onlyLogin();

                } else {
                    ToastUtils.showToast(ProductDetailsActivity.this, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            Log.e("tag", "" + e.toString());
            e.printStackTrace();

            ToastUtils.showToast(ProductDetailsActivity.this, getString(R.string.json_erro));
        }
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 资金验证
     */
    private void getZiJinData() {
        LoadingUtils.showDialog(activity);
        new OfferHttpUtils(activity).getZinJiCallBack(new InterfaceUtils.MyOkHttpCallBack() {
            @Override
            public void getHttpResultListening(String state, String json) {
                Log.i(state, json);
                LoadingUtils.dismiss();
                boolean token = TokenLifeUtils.getToken(activity, json);
                if (token) {
                    getJsonDataZiJin(json);
                } else {
                    SharedPreferencesUtils.setExit(activity);
                    startActivity(new Intent(activity, LoginActivity.class));
                }
            }

            @Override
            public void onFailure(String state, String json) {
                LoadingUtils.dismiss();
                ToastUtils.showToast(activity, getString(R.string.net_erro));
            }
        });
    }

    // 解析是否有凭证
    private void getJsonDataZiJin(String json) {
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
                                startActivity(new Intent(activity, SubmitZiJinActivity.class));
                            } else if (check_status.equals("3")) {
                                new LanRenDialog(activity).getSystemHintDialog("Your voucher has been rejected and uploaded again"
                                        , "OK", new LanRenDialog.DialogDismisListening() {
                                            @Override
                                            public void getListening() {
                                                startActivity(new Intent(activity, SubmitZiJinActivity.class));
                                            }
                                        });
                            } else if (check_status.equals("1") || check_status.equals("2")) {
                                Intent intent = new Intent(activity, MyZiJinDataActivity.class);
                                intent.putExtra("amount", amount);
                                intent.putExtra("url", url);
                                intent.putExtra("check_status", check_status);
                                intent.putExtra("add_time", add_time);
                                intent.putExtra("check_msg", check_msg);
                                startActivity(intent);
                            }
                        }
                    } else {
                        startActivity(new Intent(activity, SubmitZiJinActivity.class));
                    }
                } else if (state == 703) {
                    new LanRenDialog((Activity) activity).onlyLogin();

                } else {
                    ToastUtils.showToast(activity, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  跳转系统邮箱 发送邮件
    public static void sendEmail(Context context, String email) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        try {
            if (!(context instanceof Activity)) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "未安装邮箱应用！", Toast.LENGTH_SHORT).show();
        }
    }

    //  more列表填充
    public void setMoreList(String json) {
        listModels = new ArrayList<>();
        MyRentRoomModel.ContentBean.RowsBean rowsBean = new Gson().fromJson(json, MyRentRoomModel.ContentBean.RowsBean.class);
        ArrayList<MyRentRoomModel.ContentBean.RowsBean.ExtAttrBean> extList = (ArrayList<MyRentRoomModel.ContentBean.RowsBean.ExtAttrBean>) rowsBean.getExtAttr();

        for (int i = 0; i < extList.size(); i++) {
            for (int z = 0; z < extList.get(i).getAttr_value_list().size(); z++) {
                MoreListModel moreListModel = new MoreListModel();
                if (z == 0) {
                    MoreListModel titleModel = new MoreListModel();
                    titleModel.setTitle(extList.get(i).getAttr_key_name());
                    listModels.add(titleModel);
                    moreListModel.setItem(extList.get(i).getAttr_value_list().get(z).getValue_name());
                } else {
                    moreListModel.setTitle("");
                    moreListModel.setItem(extList.get(i).getAttr_value_list().get(z).getValue_name());
                }
                listModels.add(moreListModel);
            }
        }
        if (extList.size() == 0) {
            mIsVis.setVisibility(View.GONE);
        }

        MoreListAdapter moreListAdapter = new MoreListAdapter(listModels, ProductDetailsActivity.this);
        mMoreListView.setAdapter(moreListAdapter);
        moreHight = WindowUtils.setListViewHeightBasedOnChildren01(mMoreListView);
        WindowUtils.setListViewHeightBasedOnChildren(mMoreListView);
    }

    //  添加浏览记录
    public void addLookHouseLog(String houseId) {

        MyApplication.getmMyOkhttp().get()
                .url(Constant.add_history_log)
                .addParam("houseId", houseId)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(ProductDetailsActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("addlog", "" + response);
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.e("addlog", error_msg);
                        ToastUtils.showToast(activity, getString(R.string.net_erro));
                    }
                });
    }


    public void getVideoUrl(final String url) {
        //  LoadingUtils.showDialog(ProductDetailsActivity.this);
//        Intent intent = new Intent(activity, VideoActivity.class);
//        Log.e("url",""+url);
//        intent.putExtra("videoUrl", url);
//        startActivity(intent);


        JumpUtils.goToVideoPlayer(ProductDetailsActivity.this, goVideo, url);

//        if (url != null && !url.isEmpty()) {
//            final Handler handler = new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    super.handleMessage(msg);
//                    LoadingUtils.dismiss();
//                    Intent intent = new Intent(activity, VideoActivity.class);
//                    String url = msg.obj.toString();
//                    Log.e("url",""+url);
//                    intent.putExtra("videoUrl", url);
//                    startActivity(intent);
//
//                }
//            };
//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        String a = HttpJoddHolder.get(url);
//                        mode.setPost_videopath(a);
//
//                        Message message = new Message();
//                        message.obj = a;
//                        handler.sendMessage(message);
//
//                    } catch (Exception e) {
//
//                    }
//
//                }
//            }).start();


//        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Handler handler = new Handler();


        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);

                    switch (selectList.size()) {
                        case 0:
                            break;
                        case 1:
                            if (mStringImg01 == null) {
                                mDeleteImg01.setVisibility(View.VISIBLE);
                                mStringImg01 = selectList.get(0).getCompressPath();
                                mPicList.set(0, selectList.get(0).getCompressPath());
                                Glide.with(ProductDetailsActivity.this).load(selectList.get(0).getCompressPath()).into(mImg01);
                                mImg02.setVisibility(View.VISIBLE);
                                mDeleteImg02.setVisibility(View.GONE);
                                mDeleteImg03.setVisibility(View.GONE);
                            } else if (mStringImg02 == null) {
                                mDeleteImg02.setVisibility(View.VISIBLE);
                                mStringImg02 = selectList.get(0).getCompressPath();
                                mPicList.set(1, selectList.get(0).getCompressPath());
                                Glide.with(ProductDetailsActivity.this).load(selectList.get(0).getCompressPath()).into(mImg02);
                                mImg03.setVisibility(View.VISIBLE);
                                mDeleteImg03.setVisibility(View.GONE);
                            } else if (mStringImg03 == null) {
                                mStringImg03 = selectList.get(0).getCompressPath();
                                mPicList.set(2, selectList.get(0).getCompressPath());
                                mDeleteImg03.setVisibility(View.VISIBLE);
                                mImg03.setVisibility(View.VISIBLE);
                                Glide.with(ProductDetailsActivity.this).load(selectList.get(0).getCompressPath()).into(mImg03);

                            }


                            switch (mPicList.size()) {
                                case 0:
                                    mStringImg01 = selectList.get(0).getCompressPath();
                                    mPicList.set(0, selectList.get(0).getCompressPath());
                                    Glide.with(ProductDetailsActivity.this).load(selectList.get(0).getCompressPath()).into(mImg01);
                                    mImg02.setVisibility(View.VISIBLE);
                                    mDeleteImg02.setVisibility(View.VISIBLE);
                                    break;
                                case 1:
                                    mStringImg02 = selectList.get(0).getCompressPath();
                                    mPicList.set(1, selectList.get(0).getCompressPath());
                                    Glide.with(ProductDetailsActivity.this).load(selectList.get(0).getCompressPath()).into(mImg02);
                                    mImg03.setVisibility(View.VISIBLE);
                                    mDeleteImg03.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    mStringImg03 = selectList.get(0).getCompressPath();
                                    mPicList.set(2, selectList.get(0).getCompressPath());
                                    Glide.with(ProductDetailsActivity.this).load(selectList.get(0).getCompressPath()).into(mImg03);

                                    break;
                            }

                            break;
                        case 2:
                            if (mStringImg01 == null) {
                                mStringImg01 = selectList.get(0).getCompressPath();
                                mStringImg02 = selectList.get(1).getCompressPath();
                                mPicList.set(0, selectList.get(0).getCompressPath());
                                mPicList.set(1, selectList.get(1).getCompressPath());
                                Glide.with(ProductDetailsActivity.this).load(selectList.get(0).getCompressPath()).into(mImg01);
                                Glide.with(ProductDetailsActivity.this).load(selectList.get(1).getCompressPath()).into(mImg02);
                                mImg03.setVisibility(View.VISIBLE);
                                mDeleteImg03.setVisibility(View.VISIBLE);
                            } else if (mStringImg02 == null) {
                                mStringImg02 = selectList.get(0).getCompressPath();
                                mStringImg03 = selectList.get(1).getCompressPath();
                                mPicList.set(1, selectList.get(0).getCompressPath());
                                mPicList.set(2, selectList.get(1).getCompressPath());
                                mImg03.setVisibility(View.VISIBLE);
                                mDeleteImg03.setVisibility(View.VISIBLE);
                                mDeleteImg02.setVisibility(View.VISIBLE);
                                Glide.with(ProductDetailsActivity.this).load(selectList.get(1).getCompressPath()).into(mImg03);
                                Glide.with(ProductDetailsActivity.this).load(selectList.get(0).getCompressPath()).into(mImg02);
                            } else if (mStringImg03 == null) {


                            }

                            switch (mPicList.size()) {
                                case 0:
                                    mStringImg01 = selectList.get(0).getCompressPath();
                                    mStringImg02 = selectList.get(1).getCompressPath();
                                    mPicList.set(0, selectList.get(0).getCompressPath());
                                    mPicList.set(1, selectList.get(1).getCompressPath());
                                    Glide.with(ProductDetailsActivity.this).load(selectList.get(0).getCompressPath()).into(mImg01);
                                    Glide.with(ProductDetailsActivity.this).load(selectList.get(1).getCompressPath()).into(mImg02);
                                    mImg03.setVisibility(View.VISIBLE);
                                    mDeleteImg03.setVisibility(View.VISIBLE);
                                    break;
                                case 1:
                                    mStringImg02 = selectList.get(0).getCompressPath();
                                    mStringImg03 = selectList.get(1).getCompressPath();
                                    mPicList.set(1, selectList.get(0).getCompressPath());
                                    mPicList.set(2, selectList.get(1).getCompressPath());
                                    mImg03.setVisibility(View.VISIBLE);
                                    mDeleteImg03.setVisibility(View.VISIBLE);
                                    Glide.with(ProductDetailsActivity.this).load(selectList.get(1).getCompressPath()).into(mImg03);
                                    Glide.with(ProductDetailsActivity.this).load(selectList.get(0).getCompressPath()).into(mImg02);
                                    break;
                                case 2:
                                    break;
                            }
                            break;
                        case 3:


                            mStringImg01 = selectList.get(0).getCompressPath();
                            mStringImg02 = selectList.get(1).getCompressPath();
                            mStringImg03 = selectList.get(2).getCompressPath();
                            mPicList.set(0, selectList.get(0).getCompressPath());
                            mPicList.set(1, selectList.get(1).getCompressPath());
                            mPicList.set(2, selectList.get(2).getCompressPath());
                            Glide.with(ProductDetailsActivity.this).load(selectList.get(0).getCompressPath()).into(mImg01);
                            Glide.with(ProductDetailsActivity.this).load(selectList.get(1).getCompressPath()).into(mImg02);
                            Glide.with(ProductDetailsActivity.this).load(selectList.get(2).getCompressPath()).into(mImg03);
                            mImg02.setVisibility(View.VISIBLE);
                            mDeleteImg02.setVisibility(View.VISIBLE);
                            mImg03.setVisibility(View.VISIBLE);
                            mDeleteImg03.setVisibility(View.VISIBLE);
                            mDeleteImg01.setVisibility(View.VISIBLE);

                            switch (mPicList.size()) {
                                case 0:
                                    mStringImg01 = selectList.get(0).getCompressPath();
                                    mStringImg02 = selectList.get(1).getCompressPath();
                                    mStringImg03 = selectList.get(2).getCompressPath();
                                    mPicList.set(0, selectList.get(0).getCompressPath());
                                    mPicList.set(1, selectList.get(1).getCompressPath());
                                    mPicList.set(2, selectList.get(2).getCompressPath());
                                    Glide.with(ProductDetailsActivity.this).load(selectList.get(0).getCompressPath()).into(mImg01);
                                    Glide.with(ProductDetailsActivity.this).load(selectList.get(1).getCompressPath()).into(mImg02);
                                    Glide.with(ProductDetailsActivity.this).load(selectList.get(2).getCompressPath()).into(mImg03);
                                    mImg02.setVisibility(View.VISIBLE);
                                    mDeleteImg02.setVisibility(View.VISIBLE);
                                    mImg03.setVisibility(View.VISIBLE);
                                    mDeleteImg03.setVisibility(View.VISIBLE);
                                    break;
                                case 1:
                                    break;
                                case 2:
                                    break;
                                default:
                                    break;
                            }
                            break;
                        default:
                            break;
                    }


                    break;
                case PictureConfig.TYPE_VIDEO:


                    break;
                default:
                    break;
            }
        }

    }

    private void submitImagr(final List<LocalMedia> selectList) {
        LoadingUtils.showDialog(this);
        new SubmitRoomHttpUtils(activity).compressedImage(selectList, new SubmitRoomHttpUtils.SubmitRoomImageListening() {
            @Override
            public void getSubmitRoomImageListening(String state, String json) {
                Log.i(TAG + "_" + state, json);
                if (state.equals("onSuccess")) {
                    try {
                        if (json != null || !json.equals("")) {
                            JSONObject jsonObject = new JSONObject(json);
                            int state1 = jsonObject.getInt("state");
                            if (state1 == 1) {
                                JSONObject content = jsonObject.getJSONObject("content");
                                JSONObject data = content.getJSONObject("data");
                                String img_url = data.getString("img_url");
                                mSubmitPic.add(img_url);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (state.equals("onFinished")) {
                    addMyMessage();
                }
            }
        });
    }

    public void getRoomId(final String id, final String name) {
        String token = SharedPreferencesUtils.getToken(ProductDetailsActivity.this);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.get_room + "/" + id)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(ProductDetailsActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("room", "" + response);
                        String userId = (String) SharedPreferencesUtils.getParam(ProductDetailsActivity.this, "userID", "");
                        try {

                            if (response != null) {
                                JSONObject jsonObject = new JSONObject(response);
                                int state = jsonObject.getInt("state");
                                if (state == 1) {
                                    JSONObject content = jsonObject.getJSONObject("content");
                                    JSONObject data = content.getJSONObject("data");
                                    String roomId = data.optString("roomId");
                                    Intent intent = new Intent(ProductDetailsActivity.this, ChatActivity.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("sourceid", userId);
                                    intent.putExtra("name", name);
                                    intent.putExtra("type", 3);
                                    intent.putExtra("roomid", roomId);
                                    startActivity(intent);
                                }
                            }
                        } catch (Exception e) {


                        }


                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });
    }

}


