package com.yidankeji.cheng.ebuyhouse.housemodule.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
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
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.activity.EditContentActivity;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.filtermodule.activity.FilerHouseTypeActivity;
import com.yidankeji.cheng.ebuyhouse.housemodule.adapter.SubmitTypeAapter;
import com.yidankeji.cheng.ebuyhouse.housemodule.adapter.XiangCeRecyclerAdapter;
import com.yidankeji.cheng.ebuyhouse.housemodule.httputils.CompressListener;
import com.yidankeji.cheng.ebuyhouse.housemodule.httputils.Compressor;
import com.yidankeji.cheng.ebuyhouse.housemodule.httputils.InitListener;
import com.yidankeji.cheng.ebuyhouse.housemodule.httputils.SecondaryUtils;
import com.yidankeji.cheng.ebuyhouse.housemodule.httputils.SubmitRoomHttpUtils;
import com.yidankeji.cheng.ebuyhouse.housemodule.mode.MoreListModel;
import com.yidankeji.cheng.ebuyhouse.housemodule.mode.SubmitRoomTypeMode;
import com.yidankeji.cheng.ebuyhouse.mode.FilterMode;
import com.yidankeji.cheng.ebuyhouse.mode.ShowListMode;
import com.yidankeji.cheng.ebuyhouse.myinfomodule.mode.MyRentRoomModel;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.CustomScrollow;
import com.yidankeji.cheng.ebuyhouse.utils.FileProviderUtils.FileUtils;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.PhotoView.PhotoViewUtils;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.permissionsUtils.MyPermissions;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.ImageLogoUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.makeramen.roundedimageview.RoundedImageView.TAG;

/**
 * 与我的房屋列表页面解析的参数紧密结合
 */
public class EditRoomActivity extends Activity implements View.OnClickListener
        , XiangCeRecyclerAdapter.XiangCeOnItemClickListeming {

    private ArrayList<String> pictureList = new ArrayList<>();
    private ArrayList<String> submitImageList = new ArrayList<>();
    private ArrayList<SubmitRoomTypeMode> submitTypeList = new ArrayList<>();
    private String[] titleArray = {"Price", "Beds", "Baths", "Garage", "Kitchen",
            "HOA/master plan", "Living-sqft", "Year-Built", "Lot-sqft"};
    private String[] tagArray = {"price", "bedroom", "bathroom", "garage", "kitchen",
            "property_price", "living_sqft", "year_build", "lot_sqft"};
    private Activity activity;
    private ShowListMode mode;
    private ListView typeListView;
    private SubmitTypeAapter submitTypeAapter;
    private TextView tv_properytypes, comdetails, submitButton;
    private EditText ed_phone, ed_name;
    private ImageView im_video;
    private RecyclerView xiangceRecycler;
    private XiangCeRecyclerAdapter xiangCeRecyclerAdapter;
    private TextView mPicNum;
    private TextView mTvPersonal_sales, mTvEntrustment, mTvMonth, mTvHalfYear, mTvYear;
    private TextView tv_floor, tv_internal, tv_feature, tv_decoration, tv_building, tv_community;
    private EditText mEmail;
    private String currentOutputVideoPath = "/mnt/sdcard/EBuyHouse/video/" + "out" + ".mp4";
    private String currentInputVideoPath = "";
    String cmd = "-y -i " + currentInputVideoPath + " -strict -2 -vcodec libx264 -preset ultrafast " +
            "-crf 24 -acodec aac -ar 44100 -ac 2 -b:a 96k -s 640x480 -aspect 16:9 " + currentOutputVideoPath;
    private ArrayList<SubmitRoomTypeMode> submitRoomTypeList;
    private Compressor mCompressor;
    private CustomScrollow scrollView;
    private HashMap<String, String> mFilterHashMap = new HashMap<>();
    private int mIntentPosition;
    private TextView tv_floor_detail, tv_internal_detail, tv_feature_detail, tv_decoration_detail, tv_building_detail, tv_community_detail;
    private RelativeLayout rl_floor, rl_internal, rl_feature, rl_decoration, rl_building, rl_community;
    private HashMap<String, String> mFilterNameMap = new HashMap<>();
    private int mPos;
    private int mDetailPos;
private boolean isSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_prodetail);
        activity = EditRoomActivity.this;
        mode = (ShowListMode) getIntent().getSerializableExtra("peodetailmode");
        if (mode == null) {
            ToastUtils.showToast(this, "Sorry, data is lost");
            finish();
        }

        initView();
        initPermsion();
    }

    private void initPermsion() {
        new MyPermissions(activity).getStorageCameraPermissions();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.editprodetail_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        isSubmit =true;
        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("Edit House Info");
        scrollView = (CustomScrollow) findViewById(R.id.editroom_scrollview);
        mPicNum = (TextView) findViewById(R.id.pic_num);
        tv_properytypes = (TextView) findViewById(R.id.filter_text_properytypes);
        tv_properytypes.setOnClickListener(this);
        comdetails = (TextView) findViewById(R.id.filter_text_comdetails);
        comdetails.setOnClickListener(this);
        submitButton = (TextView) findViewById(R.id.editroom_submit);
        submitButton.setOnClickListener(this);
        im_video = (ImageView) findViewById(R.id.item_submitroom_video);
        im_video.setOnClickListener(this);
        mTvPersonal_sales = (TextView) findViewById(R.id.tv_sales);
        mTvEntrustment = (TextView) findViewById(R.id.tv_entrustment);
        mTvMonth = (TextView) findViewById(R.id.tv_month);
        mTvHalfYear = (TextView) findViewById(R.id.tv_half_year);
        mTvYear = (TextView) findViewById(R.id.tv_year);
        mEmail = (EditText) findViewById(R.id.filter_lianxi_email);
        tv_floor = (TextView) findViewById(R.id.tv_floor);
        tv_internal = (TextView) findViewById(R.id.tv_internal);
        tv_feature = (TextView) findViewById(R.id.tv_features);
        tv_decoration = (TextView) findViewById(R.id.tv_decoration);
        tv_building = (TextView) findViewById(R.id.tv_build_amenties);
        tv_community = (TextView) findViewById(R.id.tv_community);

        tv_floor_detail = (TextView) findViewById(R.id.tv_floor_detail);
        tv_internal_detail = (TextView) findViewById(R.id.tv_internal_detail);
        tv_feature_detail = (TextView) findViewById(R.id.tv_features_detail);
        tv_decoration_detail = (TextView) findViewById(R.id.tv_decoration_detail);
        tv_building_detail = (TextView) findViewById(R.id.tv_build_amenties_detail);
        tv_community_detail = (TextView) findViewById(R.id.tv_community_detail);

        rl_floor = (RelativeLayout) findViewById(R.id.rl_floor);
        rl_internal = (RelativeLayout) findViewById(R.id.rl_internal);
        rl_feature = (RelativeLayout) findViewById(R.id.rl_features);
        rl_decoration = (RelativeLayout) findViewById(R.id.rl_decoration);
        rl_building = (RelativeLayout) findViewById(R.id.rl_build_amenties);
        rl_community = (RelativeLayout) findViewById(R.id.rl_community);


        rl_floor.setOnClickListener(this);
        rl_internal.setOnClickListener(this);
        rl_feature.setOnClickListener(this);
        rl_decoration.setOnClickListener(this);
        rl_building.setOnClickListener(this);
        rl_community.setOnClickListener(this);

        mTvPersonal_sales.setOnClickListener(this);
        mTvEntrustment.setOnClickListener(this);
        mTvMonth.setOnClickListener(this);
        mTvHalfYear.setOnClickListener(this);
        mTvYear.setOnClickListener(this);
        /**/
        typeListView = (ListView) findViewById(R.id.editroom_listview);
        xiangceRecycler = (RecyclerView) findViewById(R.id.item_submitroom_xiangce);
        /**/
        ed_name = (EditText) findViewById(R.id.filter_lianxi_name);
        ed_phone = (EditText) findViewById(R.id.filter_lianxi_phone);
        mEmail = (EditText) findViewById(R.id.filter_lianxi_email);
        mCompressor = new Compressor(this);
        mCompressor.loadBinary(new InitListener() {
            @Override
            public void onLoadSuccess() {
                Log.v("mCompressor", "load library succeed");

            }

            @Override
            public void onLoadFail(String reason) {
                Log.i("mCompressor", "load library fail:" + reason);

            }
        });
        setInitData();
    }

    private void setInitData() {
        //   tv_properytypes.setText(mode.getCategory_name()+"");
        comdetails.setText(mode.getPost_details());
        ed_name.setText(mode.getPost_name());
        ed_phone.setText(mode.getPost_phone());


        submitRoomTypeList = new SecondaryUtils(activity).setTypeDataOfListViewForRentEdit();
        submitTypeAapter = new SubmitTypeAapter(activity, submitRoomTypeList, mode.getRelease_type());


        submitTypeAapter.setOnClickListening(new SubmitTypeAapter.OnClickListening() {
            @Override
            public void OnClick(int postion, String text) {
                submitTypeAapter.updataView(1, typeListView, text);
            }
        });


        typeListView.setAdapter(submitTypeAapter);
        WindowUtils.setListViewHeightBasedOnChildren(typeListView);
        setData(getIntent().getStringExtra("json"));
        scrollView.smoothScrollTo(0, 0);

        scrollView.setScrollViewListener(new CustomScrollow.ScrollViewListener() {
            @Override
            public void onScrollChanged(CustomScrollow scrollView, int x, int y, int oldx, int oldy) {
                mPos = y;
            }
        });

//        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                Log.e("location", scrollX + "----" + scrollY);
//                mPos = scrollY;
//            }
//        });
        if (!TextUtils.isEmpty(mode.getVideo_first_pic()) && mode.getVideo_first_pic().contains("http")) {
            Glide.with(this).load(mode.getVideo_first_pic()).into(im_video);
        }
        ArrayList<String> xiangceList = mode.getXiangceList();
        for (int i = 0; i < xiangceList.size(); i++) {
            submitImageList.add(xiangceList.get(i));
            pictureList.add(xiangceList.get(i));
        }
        if (pictureList.size() != 0 || pictureList.size() != 1) {
            mPicNum.setText(pictureList.size() + "/50photos");
        } else {
            mPicNum.setText(pictureList.size() + "/50photo");
        }

        xiangCeRecyclerAdapter = new XiangCeRecyclerAdapter(activity, pictureList);
        xiangCeRecyclerAdapter.setListeming(this);
        xiangceRecycler.setAdapter(xiangCeRecyclerAdapter);
        xiangceRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
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

    @Override
    public void onClick(View v) {
        mDetailPos = mPos;
        Drawable drawableLeft_nochose = getResources().getDrawable(
                R.mipmap.radio_group_notchose);
        Drawable drawableLeft_chose = getResources().getDrawable(
                R.mipmap.group_chose);
        drawableLeft_nochose.setBounds(0, 0, drawableLeft_nochose.getIntrinsicWidth(), drawableLeft_nochose.getIntrinsicHeight());
        drawableLeft_chose.setBounds(0, 0, drawableLeft_chose.getIntrinsicWidth(), drawableLeft_chose.getIntrinsicHeight());
        Intent intentFilter;
        switch (v.getId()) {
            case R.id.action_bar_back:
                GiveupSubmit();
                break;
            case R.id.filter_text_properytypes:
                Intent intent = new Intent(activity, FilerHouseTypeActivity.class);
                startActivityForResult(intent, 101);
                break;
            case R.id.filter_text_comdetails:
                String str = comdetails.getText().toString().trim();
                Intent intent1 = new Intent(activity, EditContentActivity.class);
                intent1.putExtra("content", str);
                startActivityForResult(intent1, 101);
                break;
            case R.id.item_submitroom_video:
                new ImageLogoUtils(activity).getVideoLogoDialog(mode.getPost_videopath());
                break;
            case R.id.editroom_submit:
                getSubmitButton();
                break;
            case R.id.tv_sales:
                mTvPersonal_sales.setTextColor(getResources().getColor(R.color.zhutise));
                mTvPersonal_sales.setCompoundDrawables(drawableLeft_chose, null, null, null);
                mTvEntrustment.setTextColor(getResources().getColor(R.color.black));
                mTvEntrustment.setCompoundDrawables(drawableLeft_nochose, null, null, null);
                mode.setTranscation_contract("Personal sales");
                break;
            case R.id.tv_entrustment:

                mTvPersonal_sales.setTextColor(getResources().getColor(R.color.black));
                mTvPersonal_sales.setCompoundDrawables(drawableLeft_nochose, null, null, null);
                mTvEntrustment.setTextColor(getResources().getColor(R.color.zhutise));
                mTvEntrustment.setCompoundDrawables(drawableLeft_chose, null, null, null);
                mode.setTranscation_contract("Entrustment Ebuyhouse");
                break;
            case R.id.tv_month:
                mTvMonth.setTextColor(getResources().getColor(R.color.zhutise));
                mTvMonth.setCompoundDrawables(drawableLeft_chose, null, null, null);
                mTvHalfYear.setTextColor(getResources().getColor(R.color.black));
                mTvHalfYear.setCompoundDrawables(drawableLeft_nochose, null, null, null);
                mTvYear.setTextColor(getResources().getColor(R.color.black));
                mTvYear.setCompoundDrawables(drawableLeft_nochose, null, null, null);
//                submitTypeAapter.changeFirstTitle(mTvMonth.getText().toString());
                mode.setRent_payment("Month");
                scrollView.smoothScrollTo(0, 0);
                break;
            case R.id.tv_half_year:
                mTvHalfYear.setTextColor(getResources().getColor(R.color.zhutise));
                mTvHalfYear.setCompoundDrawables(drawableLeft_chose, null, null, null);

                mTvMonth.setTextColor(getResources().getColor(R.color.black));
                mTvMonth.setCompoundDrawables(drawableLeft_nochose, null, null, null);

                mTvYear.setTextColor(getResources().getColor(R.color.black));
                mTvYear.setCompoundDrawables(drawableLeft_nochose, null, null, null);

                //  submitTypeAapter.changeFirstTitle(mTvHalfYear.getText().toString());
                scrollView.smoothScrollTo(0, 0);
                mode.setRent_payment("Half year");
                break;
            case R.id.tv_year:
                mTvYear.setTextColor(getResources().getColor(R.color.zhutise));
                mTvYear.setCompoundDrawables(drawableLeft_chose, null, null, null);

                mTvMonth.setTextColor(getResources().getColor(R.color.black));
                mTvMonth.setCompoundDrawables(drawableLeft_nochose, null, null, null);

                mTvHalfYear.setTextColor(getResources().getColor(R.color.black));
                mTvHalfYear.setCompoundDrawables(drawableLeft_nochose, null, null, null);
                //  submitTypeAapter.changeFirstTitle(mTvYear.getText().toString());
                scrollView.smoothScrollTo(0, 0);
                mode.setRent_payment("Year");
                break;
            case R.id.rl_floor:
                mIntentPosition = 0;

                intentFilter = new Intent(activity, SaleRentFilterActivity.class);
                intentFilter.putExtra("title", "Flooring");
                intentFilter.putExtra("position", 0);
                String floor = tv_floor_detail.getText().toString();
                if (!TextUtils.isEmpty(floor)) {
                    intentFilter.putExtra("chose", mFilterHashMap.get(mFilterNameMap.get(floor)));
                }
                ;

                startActivityForResult(intentFilter, 10010);
                break;
            case R.id.rl_internal:
                mIntentPosition = 1;
                intentFilter = new Intent(activity, SaleRentFilterActivity.class);
                String internal = tv_internal_detail.getText().toString();
                if (!TextUtils.isEmpty(internal)) {
                    intentFilter.putExtra("chose", mFilterHashMap.get(mFilterNameMap.get(internal)));
                }
                ;
                intentFilter.putExtra("title", "Intermal facilities");
                intentFilter.putExtra("position", 1);
                startActivityForResult(intentFilter, 10010);
                break;
            case R.id.rl_features:
                mIntentPosition = 2;
                intentFilter = new Intent(activity, SaleRentFilterActivity.class);
                String features = tv_feature_detail.getText().toString();
                if (!TextUtils.isEmpty(features)) {
                    intentFilter.putExtra("chose", mFilterHashMap.get(mFilterNameMap.get(features)));
                }
                ;
                intentFilter.putExtra("title", "Features");
                intentFilter.putExtra("position", 2);
                startActivityForResult(intentFilter, 10010);
                break;
            case R.id.rl_decoration:
                mIntentPosition = 3;
                intentFilter = new Intent(activity, SaleRentFilterActivity.class);
                String decoration = tv_decoration_detail.getText().toString();
                if (!TextUtils.isEmpty(decoration)) {
                    intentFilter.putExtra("chose", mFilterHashMap.get(mFilterNameMap.get(decoration)));
                }
                ;
                intentFilter.putExtra("title", "Decoration");
                intentFilter.putExtra("position", 3);
                startActivityForResult(intentFilter, 10010);
                break;
            case R.id.rl_build_amenties:
                mIntentPosition = 4;
                intentFilter = new Intent(activity, SaleRentFilterActivity.class);
                String build = tv_building_detail.getText().toString();
                if (!TextUtils.isEmpty(build)) {
                    intentFilter.putExtra("chose", mFilterHashMap.get(mFilterNameMap.get(build)));
                }
                ;
                intentFilter.putExtra("title", "Building Amenities");
                intentFilter.putExtra("position", 4);
                startActivityForResult(intentFilter, 10010);
                break;
            case R.id.rl_community:
                mIntentPosition = 5;
                intentFilter = new Intent(activity, SaleRentFilterActivity.class);
                String community = tv_community_detail.getText().toString();
                if (!TextUtils.isEmpty(community)) {
                    intentFilter.putExtra("chose", mFilterHashMap.get(mFilterNameMap.get(community)));
                }
                ;
                intentFilter.putExtra("title", "Community and neighborhood");
                intentFilter.putExtra("position", 5);
                startActivityForResult(intentFilter, 10010);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Handler handler = new Handler();

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                scrollView.smoothScrollTo(0, mDetailPos);// 改变滚动条的位置
            }
        };
        handler.postDelayed(runnable, 50);
        if (requestCode == 101 && resultCode == 1005) {//城市类型
            FilterMode houseTypemode = (FilterMode) data.getSerializableExtra("houseType");
            Log.i(TAG + "data", houseTypemode.getType());
            tv_properytypes.setText(houseTypemode.getType());
            mode.setPost_housetypeid(houseTypemode.getId());
            mode.setPost_housetypename(houseTypemode.getType());
        }
        if (requestCode == 101 && resultCode == 1006) { //详情信息
            String content = data.getStringExtra("content");
            if (content == null) {
                content = "";
            }
            comdetails.setText(content);
            mode.setPost_details(content);
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    for (int i = 0; i < selectList.size(); i++) {
                        String path = selectList.get(i).getCompressPath();
                        pictureList.add(path);
                    }
                    xiangCeRecyclerAdapter.notifyDataSetChanged();
                    if (pictureList.size() != 0 || pictureList.size() != 1) {
                        mPicNum.setText(pictureList.size() + "/50photos");
                    } else {
                        mPicNum.setText(pictureList.size() + "/50photo");
                    }
                    submitImagr(selectList);
                    break;
                case PictureConfig.TYPE_VIDEO:
                    List<LocalMedia> videoList = PictureSelector.obtainMultipleResult(data);
                    if (videoList.size() != 0) {
                        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                        mmr.setDataSource(videoList.get(0).getPath());
                        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                        if (Integer.valueOf(duration) / 1000 > 30) {
                            Toast.makeText(EditRoomActivity.this, "Video time is too long", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    for (int i = 0; i < videoList.size(); i++) {
                        String videoPath = videoList.get(i).getPath();
                        Log.i(TAG + "拍摄", videoPath + "");
                        mode.setPost_videopath(videoPath);
                        Bitmap videoBitmap = WindowUtils.getVideoThumbnail(videoPath);
                        im_video.setImageBitmap(videoBitmap);
                        compressMyHeadImageHttp(saveBitmapFile(videoBitmap));
                        submitVideo(videoPath);
                    }
                    break;
                default:
                    break;

            }
        }
        if (requestCode == 10010 && data != null) {
            String key = data.getStringExtra("key");
            String value = data.getStringExtra("value");
            if (!TextUtils.isEmpty(value)) {

                mFilterHashMap.put(key, value);
                mFilterNameMap.put(data.getStringExtra("name"), key);
            }
            if (data.getStringExtra("name") != null) {
                switch (mIntentPosition) {
                    case 0:
                        tv_floor_detail.setText(data.getStringExtra("name") + "");
                        break;
                    case 1:
                        tv_internal_detail.setText(data.getStringExtra("name") + "");
                        break;
                    case 2:
                        tv_feature_detail.setText(data.getStringExtra("name") + "");
                        break;
                    case 3:
                        tv_decoration_detail.setText(data.getStringExtra("name") + "");
                        break;
                    case 4:
                        tv_building_detail.setText(data.getStringExtra("name") + "");
                        break;
                    case 5:
                        tv_community_detail.setText(data.getStringExtra("name") + "");
                        break;
                    default:
                        break;
                }
            }

        }
    }

    @Override
    public void onXiangCeOnItemClickListeming(View view, int position) {
        Log.e("相册", position + "相册点击" + view.getId());
        switch (view.getId()) {
            case R.id.item_selectxiangce_image:
                if (pictureList.size() == position) {
                    if (pictureList.size() <= 50) {
                        int num = 50 - pictureList.size();
                        Log.i(TAG + "相册选择", num + "");
                        new ImageLogoUtils(activity).getImageLogoDialog(num);
                    }
                } else {
                    PhotoViewUtils.getPhotoView(activity, pictureList, 0);
                }
                break;
            case R.id.item_selectxiangce_delete:
                String string = submitButton.getText().toString();
                if (string.equals("Submit")) {
                    if (submitImageList.size() - 1 >= position) {
                        submitImageList.remove(position);
                        pictureList.remove(position);
                        xiangCeRecyclerAdapter.notifyDataSetChanged();

                        if (pictureList.size() != 0 || pictureList.size() != 1) {
                            mPicNum.setText(pictureList.size() + "/50photos");
                        } else {
                            mPicNum.setText(pictureList.size() + "/50photo");
                        }
                    }
                } else {
                    ToastUtils.showToast(activity, "Upload, cannot delete");
                }
                break;
            default:
                break;
        }

    }

    /**
     * 提示是否要放弃本次上传
     */
    private void GiveupSubmit() {
        setResult(1007, new Intent());
        new SecondaryUtils(activity).getGiveUpSubmitDialog();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            GiveupSubmit();
        }
        return true;
    }

    /**
     * 上传视频
     */
    private void submitVideo(String path) {
        setSubmitButtonSatte(false);

        Log.i("videoUrl", "submitVideo");
        currentInputVideoPath = path;
        currentOutputVideoPath = "/mnt/sdcard/EBuyHouse/video/" + System.currentTimeMillis() + ".mp4";
        String cmd = "-y -i " + currentInputVideoPath + " -strict -2 -vcodec libx264 -preset ultrafast " +
                "-crf 24 -acodec aac -ar 44100 -ac 2 -b:a 96k -s 640x480 -aspect 16:9 " + currentOutputVideoPath;
        Log.e("mCompressor", currentInputVideoPath + "----" + cmd);
        LoadingUtils.showDialog(EditRoomActivity.this);
        execCommand(cmd);

    }

    private void execCommand(String cmd) {
        File mFile = new File(currentOutputVideoPath);
        if (mFile.exists()) {
            mFile.delete();
        }
        mCompressor.execCommand(cmd, new CompressListener() {
            @Override
            public void onExecSuccess(String message) {
                Log.i("mCompressor", "陈工 " + message);

                new SubmitRoomHttpUtils(activity).postVedioToService(currentOutputVideoPath, new SubmitRoomHttpUtils.SubmitRoomImageListening() {
                    @Override
                    public void getSubmitRoomImageListening(final String state, final String json) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (state.equals("onProgress")) {
                                    submitButton.setText("Upload video " + json + "%");
                                } else {
                                    Log.i(TAG + "state", json);
                                    setSubmitButtonSatte(true);
                                    getVideoJsonData(json);
                                }
                            }
                        });
                    }
                });

            }

            @Override
            public void onExecFail(String reason) {
                Log.i("mCompressor", "fail " + reason);

            }

            @Override
            public void onExecProgress(String message) {
                Log.i("mCompressor", "progress " + message);


            }
        });
    }

    private void getVideoJsonData(String json) {
        LoadingUtils.dismiss();
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONObject data = content.getJSONObject("data");
                    String video_url = data.getString("img_url");
                    mode.setPost_videopath(video_url);
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

    /**
     * 上传图片
     */
    private void submitImagr(final List<LocalMedia> selectList) {
        setSubmitButtonSatte(false);
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
                                submitImageList.add(img_url);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (state.equals("onFinished")) {
                    setSubmitButtonSatte(true);
                }
            }
        });
    }

    /**
     * 设置提交按钮状态
     */
    private void setSubmitButtonSatte(boolean state) {
        if (state) {
            submitButton.setClickable(true);
            submitButton.setBackgroundResource(R.drawable.shape_text_bg_zhutise);
            submitButton.setText("Submit");
        } else {
            submitButton.setClickable(false);
            submitButton.setBackgroundColor(getResources().getColor(R.color.text_hei));
            submitButton.setText("Submission File");
        }
    }

    /**
     * 点击提交按钮
     */
    private void getSubmitButton() {
        final String post_housetypeid = mode.getPost_housetypeid();
        if (post_housetypeid == null || post_housetypeid.equals("")) {
            ToastUtils.showToast(activity, "Please select the type of house");
            return;
        }
        HashMap<String, String> typeHashMap = submitTypeAapter.getHashMap();

        String price = typeHashMap.get("price");
        if (price == null || price.equals("")) {
            ToastUtils.showToast(activity, "Please fill in the price of the house");
            return;
        }
        mode.setPrice(price);

        String bedroom = typeHashMap.get("bedroom");
        if (bedroom == null || bedroom.equals("")) {
            ToastUtils.showToast(activity, "There are some bedroom in your house");
            return;
        }
        mode.setBedroom(bedroom);

        String bathroom = typeHashMap.get("bathroom");
        if (bathroom == null || bathroom.equals("")) {
            ToastUtils.showToast(activity, "There are some bathroom in your house");
            return;
        }
        mode.setBathroom(bathroom);

        String kitchen = typeHashMap.get("kitchen");
        if (kitchen == null || kitchen.equals("")) {
            ToastUtils.showToast(activity, "There are some kitchen in your house");
            return;
        }
        mode.setKitchen(kitchen);

        String garage = typeHashMap.get("garage");
        if (garage == null || garage.equals("")) {
            //       ToastUtils.showToast(activity, "Please fill out your property fee");
//            return;
            garage = "";
        }
        mode.setGarage(garage);

        String property_price = typeHashMap.get("property_price");
        if (property_price == null || property_price.equals("")) {
            ToastUtils.showToast(activity, "Please fill out your property fee");
            return;
        }
        mode.setPost_wuyefei(property_price);

        String living_sqft = typeHashMap.get("living_sqft");
        if (living_sqft == null || living_sqft.equals("")) {
            ToastUtils.showToast(activity, "Please fill in your room area");
            return;
        }
        mode.setPost_livesqft(living_sqft);

        String year_build = typeHashMap.get("year_build");
        if (year_build == null || year_build.equals("")) {
            ToastUtils.showToast(activity, "Please fill in your room year_build");
            return;
        }
        mode.setPost_yearbuilder(year_build);

        String lot_sqft = typeHashMap.get("lot_sqft");
        if (lot_sqft == null || lot_sqft.equals("")) {
            ToastUtils.showToast(activity, "Please fill in your room lot_sqft");
            return;
        }
        mode.setPost_lotsize(lot_sqft);

        String post_details = mode.getPost_details();
        if (post_details == null || post_details.equals("")) {
           post_details = "";

        }
        mode.setPost_details(post_details);

        if (submitImageList.size() == 0) {
            ToastUtils.showToast(activity, "Please select several indoor photos");
            return;
        }
        String name = WindowUtils.getEditTextContent(ed_name);
        if (name == null || name.equals("")) {
            ToastUtils.showToast(activity, "Please fill in your name");
            return;
        }
        mode.setPost_name(name);

        String phone = WindowUtils.getEditTextContent(ed_phone);
        String email = WindowUtils.getEditTextContent(mEmail);
        if (TextUtils.isEmpty(email + phone)) {
            ToastUtils.showToast(activity, "Please fill in your phone number or email");
            return;
        }
        if (!TextUtils.isEmpty(email) && !email.contains("@")) {
            ToastUtils.showToast(activity, "email Format is wrong");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            email = "";
        }
        if (TextUtils.isEmpty(phone)) {
            phone = "";
        }

        mode.setEmail(email);
        mode.setPost_phone(phone);


        String filterKey = "";
        String filterValue = "";
        Iterator iter = mFilterHashMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            String val = (String) entry.getValue();
            if (TextUtils.isEmpty(filterKey)) {
                filterKey = key;
                filterValue = val;
            } else {
                filterKey = filterKey + "-" + key;
                filterValue = filterValue + "-" + val;
            }
        }
        mode.setExt_key(filterKey);
        mode.setExt_value(filterValue);

        Log.e("filter", filterKey + "---" + filterValue);

        String deposit = typeHashMap.get("deposit");
        if (TextUtils.isEmpty(deposit)) {
            ToastUtils.showToast(activity, "Please fill in your deposit");
            return;
        }
        mode.setDeposit(deposit);

        if (TextUtils.isEmpty(mode.getRent_payment())) {
            ToastUtils.showToast(activity, "Please fill in your Rent Payment");
            return;
        }
        if (TextUtils.isEmpty(mode.getTranscation_contract())) {
            ToastUtils.showToast(activity, "Please fill in your Transcation contract");
            return;
        }

        if(isSubmit){
            beginSubmitDataToService();
            isSubmit = false;
        }

    }

    private void beginSubmitDataToService() {
        LoadingUtils.showDialog(activity);
        String imageURL = "";
        String imagezhutu = "";
        for (int i = 0; i < submitImageList.size(); i++) {
            if (i == 0) {
                imagezhutu = submitImageList.get(i);
                imageURL = submitImageList.get(i);
            } else {
                imageURL = imageURL + "," + submitImageList.get(i);
            }
        }
        String p = mode.getPrice();
        String replace_p = p.replace(",", "");
        Double price = Double.valueOf(replace_p);
        long longPrice = (long) (price * 100);

        String w = mode.getPost_wuyefei();
        String replace_w = w.replace(",", "");
        Double wuyefei = Double.valueOf(replace_w);
        long longWuyefei = (long) (wuyefei * 100);


        String token = SharedPreferencesUtils.getToken(activity);
        String deposit = (Double.valueOf(mode.getDeposit()) * 100) + "";
        if (deposit.contains(".0")) {
            deposit = deposit.split("\\.")[0];
        }
        MyApplication.getmMyOkhttp().post()
                .url(Constant.addHouseMessage)
                .addHeader("Authorization", "Bearer " + token)
                .addParam("id", mode.getId())
                .addParam("fk_city_id", mode.getFk_city_id())
                .addParam("fk_state_id", mode.getFk_state_id())
                .addParam("price", longPrice + "")
                .addParam("property_price", longWuyefei + "")
                .addParam("fk_category_id", mode.getPost_housetypeid())
                .addParam("street", mode.getPost_name())
                .addParam("zip", mode.getZip())
                .addParam("bedroom", mode.getBedroom())
                .addParam("bathroom", mode.getBathroom())
                .addParam("garage", mode.getGarage())
                .addParam("kitchen", mode.getKitchen())
                .addParam("apn", mode.getApn())
                .addParam("mls", mode.getMls())
                .addParam("lot_sqft", mode.getPost_lotsize())
                .addParam("living_sqft", mode.getPost_livesqft())
                .addParam("latitude", mode.getLatitude() + "")
                .addParam("longitude", mode.getLongitude() + "")
                .addParam("year_build", mode.getPost_yearbuilder())
                .addParam("img_url", imagezhutu)
                .addParam("img_code", imageURL)
                .addParam("video_url", mode.getPost_videopath() + "")
                .addParam("release_type", mode.getRelease_type())
                .addParam("description", mode.getPost_details() + "")
                .addParam("name", mode.getPost_name())
                .addParam("phone_number", mode.getPost_phone())
                .addParam("video_first_pic", mode.getVideo_first_pic() + "")
                .addParam("email", mode.getEmail() + "")
                .addParam("ext_key", mode.getExt_key() + "")
                .addParam("ext_value", mode.getExt_value() + "")
                .addParam("rent_payment", mode.getRent_payment())
                .addParam("deposit", deposit)
                .addParam("release_method", mode.getTranscation_contract())
                .enqueue(new NewRawResponseHandler(EditRoomActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "上传", response);
                        isSubmit = true;
                        LoadingUtils.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.getInt("state");
                            if (state == 1) {
                                new LanRenDialog(activity).getSubmitSuccessDialog(new LanRenDialog.DialogDismisListening() {
                                    @Override
                                    public void getListening() {
                                        setResult(1007, new Intent());
                                        finish();
                                    }
                                });
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
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.e("submit", "xczxczczxcz" + error_msg);
                        LoadingUtils.dismiss();
                        isSubmit = true;
                        ToastUtils.showToast(activity, getString(R.string.net_erro));
                    }
                });
    }

    public void compressMyHeadImageHttp(String path) {
        ArrayList<LocalMedia> list = new ArrayList<>();
        LocalMedia media = new LocalMedia();
        media.setCompressPath(path);
        list.add(media);
        LoadingUtils.showDialog(EditRoomActivity.this);
        new SubmitRoomHttpUtils(activity).compressedImage(list, new SubmitRoomHttpUtils.SubmitRoomImageListening() {
            @Override
            public void getSubmitRoomImageListening(String state, String json) {
                Log.i("上传头像" + "上传头像", json);

                if (state.equals("onSuccess")) {
                    getJSOnData(json);
                } else if (state.equals("onFinished")) {

                }
            }
        });
    }

    private void getJSOnData(String json) {
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONObject data = content.getJSONObject("data");
                    String img_url = data.getString("img_url");
                    mode.setVideo_first_pic(img_url);

                } else {
                    ToastUtils.showToast(EditRoomActivity.this, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // bitmap 转file
    public String saveBitmapFile(Bitmap bitmap) {
        File file = new File(FileUtils.picturePath + System.currentTimeMillis() + ".jpg");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return file.getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getPath();
    }

    public void setData(String json) {
        MyRentRoomModel.ContentBean.RowsBean rowsBean = new Gson().fromJson(json, MyRentRoomModel.ContentBean.RowsBean.class);
        Drawable drawableLeft_nochose = getResources().getDrawable(
                R.mipmap.radio_group_notchose);
        Drawable drawableLeft_chose = getResources().getDrawable(
                R.mipmap.group_chose);
        drawableLeft_nochose.setBounds(0, 0, drawableLeft_nochose.getIntrinsicWidth(), drawableLeft_nochose.getIntrinsicHeight());
        drawableLeft_chose.setBounds(0, 0, drawableLeft_chose.getIntrinsicWidth(), drawableLeft_chose.getIntrinsicHeight());

        //    property type 回显
        mode.setPost_housetypeid(rowsBean.getFk_category_id());
        // transcation contract 回显
        if (rowsBean.getRelease_method().contains("Personal")) {
            mTvPersonal_sales.setTextColor(getResources().getColor(R.color.zhutise));
            mTvPersonal_sales.setCompoundDrawables(drawableLeft_chose, null, null, null);
            mTvEntrustment.setTextColor(getResources().getColor(R.color.black));
            mTvEntrustment.setCompoundDrawables(drawableLeft_nochose, null, null, null);
            mode.setTranscation_contract("Personal sales");
        } else {
            mTvPersonal_sales.setTextColor(getResources().getColor(R.color.black));
            mTvPersonal_sales.setCompoundDrawables(drawableLeft_nochose, null, null, null);
            mTvEntrustment.setTextColor(getResources().getColor(R.color.zhutise));
            mTvEntrustment.setCompoundDrawables(drawableLeft_chose, null, null, null);
            mode.setTranscation_contract("Entrustment Ebuyhouse");
        }
        // Rent payment 回显

        switch (rowsBean.getRent_payment()) {
            case "Month":
                mTvMonth.setTextColor(getResources().getColor(R.color.zhutise));
                mTvMonth.setCompoundDrawables(drawableLeft_chose, null, null, null);
                mTvHalfYear.setTextColor(getResources().getColor(R.color.black));
                mTvHalfYear.setCompoundDrawables(drawableLeft_nochose, null, null, null);
                mTvYear.setTextColor(getResources().getColor(R.color.black));
                mTvYear.setCompoundDrawables(drawableLeft_nochose, null, null, null);
                submitTypeAapter.changeFirstTitle(mTvMonth.getText().toString());
                mode.setRent_payment("Month");
                break;
            case "Half year":
                mTvHalfYear.setTextColor(getResources().getColor(R.color.zhutise));
                mTvHalfYear.setCompoundDrawables(drawableLeft_chose, null, null, null);

                mTvMonth.setTextColor(getResources().getColor(R.color.black));
                mTvMonth.setCompoundDrawables(drawableLeft_nochose, null, null, null);

                mTvYear.setTextColor(getResources().getColor(R.color.black));
                mTvYear.setCompoundDrawables(drawableLeft_nochose, null, null, null);

                submitTypeAapter.changeFirstTitle(mTvHalfYear.getText().toString());

                mode.setRent_payment("Half year");
                break;
            case "Year":
                mTvYear.setTextColor(getResources().getColor(R.color.zhutise));
                mTvYear.setCompoundDrawables(drawableLeft_chose, null, null, null);

                mTvMonth.setTextColor(getResources().getColor(R.color.black));
                mTvMonth.setCompoundDrawables(drawableLeft_nochose, null, null, null);

                mTvHalfYear.setTextColor(getResources().getColor(R.color.black));
                mTvHalfYear.setCompoundDrawables(drawableLeft_nochose, null, null, null);
                submitTypeAapter.changeFirstTitle(mTvYear.getText().toString());

                mode.setRent_payment("Year");
                break;
            default:
                mode.setRent_payment("Month");
                break;
        }

        mode.setApn(rowsBean.getApn());
        mode.setMls(rowsBean.getMls());



//        //  monthly rent 回显
//        submitRoomTypeList.get(0).setScannerEdit(rowsBean.getPrice() + "");
//        if (rowsBean.getPrice().contains(".00")) {
//            rowsBean.getPrice().replace(".00", "");
//        }

        try {

            if (!TextUtils.isEmpty(rowsBean.getPrice())) {
                if (rowsBean.getPrice().contains(",")) {
                    rowsBean.setPrice(rowsBean.getPrice().replace(",", ""));
                }
                Double price = Double.valueOf(rowsBean.getPrice());

                String temp = price + "";
                if (temp.contains(".0")) {
                    temp = temp.replace(".0", "");
                }

                mode.setPrice(temp + "");
                submitRoomTypeList.get(0).setScannerEdit(temp + "");
            }
        } catch (Exception e) {
            Log.e("double", "" + e.toString());
        }

        try {

            if (!TextUtils.isEmpty(rowsBean.getDeposit())) {
                if (rowsBean.getDeposit().contains(",")) {
                    rowsBean.setDeposit(rowsBean.getDeposit().replace(",", ""));
                }
                Double price = Double.valueOf(rowsBean.getDeposit());
                Log.e("price", "" + price);
                String temp = price + "";
                if (temp.contains(".0")) {
                    temp = temp.replace(".0", "");
                }
                mode.setDeposit(temp + "");

                submitRoomTypeList.get(1).setScannerEdit(temp + "");

            }
        } catch (Exception e) {
            Log.e("double", "" + e.toString());
        }
        try {


            if (!TextUtils.isEmpty(rowsBean.getBedroom())) {
                if (rowsBean.getBedroom().contains(",")) {
                    rowsBean.setBedroom(rowsBean.getBedroom().replace(",", ""));
                }
                Double price = Double.valueOf(rowsBean.getBedroom());
                Log.e("price", "" + price);
                String temp = price + "";
                if (temp.contains(".0")) {
                    temp = temp.replace(".0", "");
                }
                mode.setBedroom(temp + "");
                submitRoomTypeList.get(2).setScannerEdit(temp + "");
            }
        } catch (Exception e) {
            Log.e("double", "" + e.toString());
        }
        try {

            if (!TextUtils.isEmpty(rowsBean.getBathroom())) {
                if (rowsBean.getBathroom().contains(",")) {
                    rowsBean.setBathroom(rowsBean.getBathroom().replace(",", ""));
                }
                Double price = Double.valueOf(rowsBean.getBathroom());
                Log.e("price", "" + price);
                String temp = price + "";
                if (temp.contains(".0")) {
                    temp = temp.replace(".0", "");
                }
                mode.setBathroom(temp + "");
                submitRoomTypeList.get(3).setScannerEdit(temp + "");
            }
        } catch (Exception e) {
            Log.e("double", "" + e.toString());
        }
        try {

            if (!TextUtils.isEmpty(rowsBean.getGarage())) {
                if (rowsBean.getGarage().contains(",")) {
                    rowsBean.setGarage(rowsBean.getGarage().replace(",", ""));
                }
                Double price = Double.valueOf(rowsBean.getGarage());
                Log.e("price", "" + price);
                String temp = price + "";
                if (temp.contains(".0")) {
                    temp = temp.replace(".0", "");
                }
                mode.setGarage(temp + "");
                submitRoomTypeList.get(4).setScannerEdit(temp + "");
            }
        } catch (Exception e) {
            Log.e("double", "" + e.toString());
        }
        try {

            if (!TextUtils.isEmpty(rowsBean.getKitchen())) {
                if (rowsBean.getKitchen().contains(",")) {
                    rowsBean.setKitchen(rowsBean.getKitchen().replace(",", ""));
                    ;
                }
                Double price = Double.valueOf(rowsBean.getKitchen());
                Log.e("price", "" + price);
                String temp = price + "";
                if (temp.contains(".0")) {
                    temp = temp.replace(".0", "");
                }
                mode.setKitchen(temp + "");
                submitRoomTypeList.get(5).setScannerEdit(temp + "");
            }
        } catch (Exception e) {
            Log.e("double", "" + e.toString());
        }
        try {

            if (!TextUtils.isEmpty(rowsBean.getProperty_price())) {
                if (rowsBean.getProperty_price().contains(",")) {
                    rowsBean.setProperty_price(rowsBean.getProperty_price().replace(",", ""));
                }
                Double price = Double.valueOf(rowsBean.getProperty_price());
                Log.e("price", "" + price);
                String temp = price + "";
                if (temp.contains(".0")) {
                    temp = temp.replace(".0", "");
                }
                mode.setPost_wuyefei(temp + "");
                submitRoomTypeList.get(6).setScannerEdit(temp + "");
            }
        } catch (Exception e) {
            Log.e("double", "" + e.toString());
        }
        try {

            if (!TextUtils.isEmpty(rowsBean.getLiving_sqft())) {
                if (rowsBean.getLiving_sqft().contains(",")) {
                    rowsBean.setLiving_sqft(rowsBean.getLiving_sqft().replace(",", ""));
                    ;
                }
                Double price = Double.valueOf(rowsBean.getLiving_sqft());
                Log.e("price", "" + price);
                String temp = price + "";
                if (temp.contains(".0")) {
                    temp = temp.replace(".0", "");
                }
                mode.setPost_livesqft(temp + "");
                submitRoomTypeList.get(7).setScannerEdit(temp + "");
            }
        } catch (Exception e) {
            Log.e("double", "" + e.toString());
        }

        try {

            mode.setPost_yearbuilder(rowsBean.getYear_build() + "");
//            mode.setLiving_sqft(rowsBean.getYear_build() + "");
//            if(!TextUtils.isEmpty(rowsBean.getYear_build())){
//                if(rowsBean.getLiving_sqft().contains(",")){
//                    rowsBean.getLiving_sqft().replace(",","");
//                }
//                Double price = Double.valueOf(rowsBean.getLiving_sqft());
//
//            }
            submitRoomTypeList.get(8).setScannerEdit(rowsBean.getYear_build() + "");
        } catch (Exception e) {
            Log.e("double", "" + e.toString());
        }
        try {

            if (!TextUtils.isEmpty(rowsBean.getLiving_sqft())) {
                if (rowsBean.getLot_sqft().contains(",")) {
                    rowsBean.setLot_sqft(rowsBean.getLot_sqft().replace(",", ""));
                    ;
                }
                Double price = Double.valueOf(rowsBean.getLot_sqft());
                Log.e("price", "" + price);
                String temp = price + "";
                if (temp.contains(".0")) {
                    temp = temp.replace(".0", "");
                }
                mode.setPost_lotsize(temp + "");
                submitRoomTypeList.get(9).setScannerEdit(temp + "");
            }
        } catch (Exception e) {
            Log.e("double", "" + e.toString());
        }


//        //  desposit
//        submitRoomTypeList.get(1).setScannerEdit(rowsBean.getDeposit() + "");
//        String deposit = "";
//
//        mode.setDeposit(rowsBean.getDeposit() + "");
//        // bedrooms
//        submitRoomTypeList.get(2).setScannerEdit(rowsBean.getBedroom() + "");
//
//        mode.setBedroom(rowsBean.getBedroom() + "");
//        //bathrooms
//        submitRoomTypeList.get(3).setScannerEdit(rowsBean.getBathroom() + "");
//
//        mode.setBathroom(rowsBean.getBathroom() + "");
//
//        // garage
//        submitRoomTypeList.get(4).setScannerEdit(rowsBean.getGarage() + "");
//
//        ;
//        mode.setGarage(rowsBean.getGarage() + "");
//        // kitchen
//        submitRoomTypeList.get(5).setScannerEdit(rowsBean.getKitchen() + "");
//
//        mode.setKitchen(rowsBean.getKitchen() + "");
//        //hoa
//        submitRoomTypeList.get(6).setScannerEdit(rowsBean.getProperty_price() + "");
//        mode.setProperty_price(rowsBean.getProperty_price() + "");
//
//        // living_sqft
//        submitRoomTypeList.get(7).setScannerEdit(rowsBean.getLiving_sqft() + "");
//        mode.setLiving_sqft(rowsBean.getLiving_sqft() + "");
//        // year
//        submitRoomTypeList.get(8).setScannerEdit(rowsBean.getYear_build() + "");
//        mode.setYear_build(rowsBean.getYear_build() + "");
//        //lotsqft
//        submitRoomTypeList.get(9).setScannerEdit(rowsBean.getLot_sqft() + "");
//        mode.setLot_sqft(rowsBean.getLot_sqft() + "");
// otherdetail
        if (!TextUtils.isEmpty(rowsBean.getDescription())) {
            comdetails.setText(rowsBean.getDescription() + "");
        }
        if (!TextUtils.isEmpty(rowsBean.getCustomer_email())) {
            mEmail.setText(rowsBean.getCustomer_email());
        }


        mode.setFk_category_id(rowsBean.getFk_category_id());
        String key = "";
        String value = "";
        // filter 筛选回显
        ArrayList<MoreListModel> listModels = new ArrayList<>();
        ArrayList<MyRentRoomModel.ContentBean.RowsBean.ExtAttrBean> extList = (ArrayList<MyRentRoomModel.ContentBean.RowsBean.ExtAttrBean>) rowsBean.getExtAttr();

        for (int i = 0; i < extList.size(); i++) {
            String name = "";
            if (key.equals("")) {
                key = extList.get(i).getAttr_key_id();
            } else {
                key = key + "-" + extList.get(i).getAttr_key_id();
            }
            if (value.equals("")) {
                value = "";
            } else {
                value = value + "-";
            }
            for (int z = 0; z < extList.get(i).getAttr_value_list().size(); z++) {
                MoreListModel moreListModel = new MoreListModel();
                if (value.equals("")) {
                    name = extList.get(i).getAttr_value_list().get(z).getValue_name();
                    value = extList.get(i).getAttr_value_list().get(z).getValue_id();
                } else {
                    value = value + "," + extList.get(i).getAttr_value_list().get(z).getValue_id();
                    name = name + "," + extList.get(i).getAttr_value_list().get(z).getValue_name();
                }
                if (z == 0) {
                    moreListModel.setTitle(extList.get(i).getAttr_key_name());
                    moreListModel.setItem("■ " + extList.get(i).getAttr_value_list().get(z).getValue_name());
                } else {
                    moreListModel.setTitle("");
                    moreListModel.setItem("■ " + extList.get(i).getAttr_value_list().get(z).getValue_name());
                }
                listModels.add(moreListModel);
            }
            switch (extList.get(i).getAttr_key_name()) {
                case "Flooring":
                    tv_floor_detail.setText(name + "");
                    break;
                case "Internal facilities":
                    tv_internal_detail.setText(name);
                    break;
                case "Features":
                    tv_feature_detail.setText(name);
                    break;
                case "Decoration":
                    tv_decoration_detail.setText(name);
                    break;
                case "Building Amenities":
                    tv_building_detail.setText(name);
                    break;
                case "Community and neighborhood":
                    tv_community_detail.setText(name);
                    break;
                default:

                    break;

            }


        }
        mode.setExt_key(key);
        mode.setExt_value(value);
    }


}
