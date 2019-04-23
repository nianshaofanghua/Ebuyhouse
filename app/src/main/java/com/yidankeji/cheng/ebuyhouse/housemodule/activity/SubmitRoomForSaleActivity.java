package com.yidankeji.cheng.ebuyhouse.housemodule.activity;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.yidankeji.cheng.ebuyhouse.housemodule.mode.SubmitRoomTypeMode;
import com.yidankeji.cheng.ebuyhouse.mode.FilterMode;
import com.yidankeji.cheng.ebuyhouse.mode.PostRoom.PostRoomMode;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SubmitRoomForSaleActivity extends Activity implements View.OnClickListener
        , XiangCeRecyclerAdapter.XiangCeOnItemClickListeming {
    private ArrayList<String> pictureList = new ArrayList<>();
    private ArrayList<String> submitImageList = new ArrayList<>();
    private Activity activity;
    private PostRoomMode mode;
    private ListView typeListView;
    private String TAG = "SubmitRoomAllType";
    private SubmitTypeAapter submitTypeAapter;
    private TextView tv_properytypes, comdetails, submitButton;
    private ImageView im_video;
    private EditText ed_phone, ed_name;
    private RecyclerView xiangceRecycler;
    private TextView mPic_num;
    private XiangCeRecyclerAdapter xiangCeRecyclerAdapter;
    private ProgressDialog mProgressDialog;
    private String currentOutputVideoPath = FileUtils.videoPath + "out" + ".mp4";
    private String currentInputVideoPath = "";
    String cmd = "-y -i " + currentInputVideoPath + " -strict -2 -vcodec libx264 -preset ultrafast " +
            "-crf 24 -acodec aac -ar 44100 -ac 2 -b:a 96k -s 640x480 -aspect 16:9 " + currentOutputVideoPath;

    private Compressor mCompressor;
    private TextView mTvPersonal_sales, mTvEntrustment;
    private CustomScrollow scrollView;
    private TextView tv_floor, tv_internal, tv_feature, tv_decoration, tv_building, tv_community;
    private EditText mEmail;
    private HashMap<String, String> mFilterHashMap = new HashMap<>();
    private TextView tv_floor_detail, tv_internal_detail, tv_feature_detail, tv_decoration_detail, tv_building_detail, tv_community_detail;
    private RelativeLayout rl_floor, rl_internal, rl_feature, rl_decoration, rl_building, rl_community;
    private int mIntentPosition;
    private HashMap<String, String> mFilterNameMap = new HashMap<>();
    private int mPos;
    private int mDetailPos;
    private boolean isSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_room_for_sale);
        activity = this;
        mode = (PostRoomMode) getIntent().getSerializableExtra("postroomMode");
        if (mode == null) {
            ToastUtils.showToast(this, "Sorry, data is lost");
            finish();
        }

        initView();

        initPermission();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.submitroom_alltype_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        isSubmit = true;
        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.action_bar_title);
        mPic_num = (TextView) findViewById(R.id.pic_num);
        title.setText("Post a Room");
        typeListView = (ListView) findViewById(R.id.submitroom_alltype_listview);
        mTvPersonal_sales = (TextView) findViewById(R.id.tv_sales);
        mTvEntrustment = (TextView) findViewById(R.id.tv_entrustment);
        mTvPersonal_sales.setOnClickListener(this);
        mTvEntrustment.setOnClickListener(this);

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
        /**/
        ArrayList<SubmitRoomTypeMode> submitRoomTypeList = new SecondaryUtils(activity).setTypeDataOfListViewForSale();

        submitTypeAapter = new SubmitTypeAapter(activity, submitRoomTypeList, mode.getRelease_type());
        typeListView.setAdapter(submitTypeAapter);
        WindowUtils.setListViewHeightBasedOnChildren(typeListView);
        /**/
        xiangceRecycler = (RecyclerView) findViewById(R.id.item_submitroom_xiangce);
        xiangCeRecyclerAdapter = new XiangCeRecyclerAdapter(activity, pictureList);
        xiangCeRecyclerAdapter.setListeming(this);
        xiangceRecycler.setAdapter(xiangCeRecyclerAdapter);
        xiangceRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        /**/
        tv_properytypes = (TextView) findViewById(R.id.filter_text_properytypes);
        tv_properytypes.setOnClickListener(this);
        comdetails = (TextView) findViewById(R.id.filter_text_comdetails);
        comdetails.setOnClickListener(this);
        /**/
        im_video = (ImageView) findViewById(R.id.item_submitroom_video);
        im_video.setOnClickListener(this);
        ImageView im_deletevideo = (ImageView) findViewById(R.id.item_submitroom_deletevideo);
        im_deletevideo.setOnClickListener(this);
        /**/
        ed_name = (EditText) findViewById(R.id.filter_lianxi_name);
        ed_phone = (EditText) findViewById(R.id.filter_lianxi_phone);
        mEmail = (EditText) findViewById(R.id.filter_lianxi_email);
        ed_name.setText((String) SharedPreferencesUtils.getParam(this, "myinfo_name", ""));
        String phone = (String) SharedPreferencesUtils.getParam(this, "email_phone", "");
        if (!phone.contains("com")) {
            ed_phone.setText((String) SharedPreferencesUtils.getParam(this, "email_phone", ""));
        } else {
            mEmail.setText((String) SharedPreferencesUtils.getParam(this, "email_phone", ""));
        }

        mode.setTranscation_contract("Personal sales");
        mode.setRent_payment("Month");
        /**/
        submitButton = (TextView) findViewById(R.id.submitroom_alltype_submit);
        submitButton.setOnClickListener(this);
        /**/
        scrollView = (CustomScrollow) findViewById(R.id.submitroom_alltype_scrollview);
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
//                Log.e("location",scrollX+"----"+scrollY);
//
//            }
//        });
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
            case R.id.action_bar_back://返回
                GiveupSubmit();
                break;
            case R.id.filter_text_properytypes://房屋类型
                Intent intent = new Intent(activity, FilerHouseTypeActivity.class);
                startActivityForResult(intent, 101);
                break;
            case R.id.filter_text_comdetails://备注信息
                String str = comdetails.getText().toString().trim();
                Intent intent1 = new Intent(activity, EditContentActivity.class);
                intent1.putExtra("content", str);
                startActivityForResult(intent1, 101);
                break;
            case R.id.item_submitroom_video://拍摄视频
                new ImageLogoUtils(activity).getVideoLogoDialog(mode.getPost_videopath());
                break;
            case R.id.submitroom_alltype_submit:
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
//  滚动到之前的位置
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                scrollView.smoothScrollTo(0, mDetailPos);// 改变滚动条的位置
            }
        };
        handler.postDelayed(runnable, 50);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
//                    for (int i = 0; i < selectList.size(); i++) {
//                        String path = selectList.get(i).getCompressPath();
//                        pictureList.add(path);
//                    }


                    submitImagr(selectList);
                    break;
                case PictureConfig.TYPE_VIDEO:

                    List<LocalMedia> videoList = PictureSelector.obtainMultipleResult(data);
                    if (videoList.size() != 0) {
                        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                        mmr.setDataSource(videoList.get(0).getPath());
                        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                        if (Integer.valueOf(duration) / 1000 > 30) {
                            Toast.makeText(SubmitRoomForSaleActivity.this, "Video time is too long", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    for (int i = 0; i < videoList.size(); i++) {
                        String videoPath = videoList.get(i).getPath();
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
        //城市类型
        if (requestCode == 101 && resultCode == 1005) {
            FilterMode houseTypemode = (FilterMode) data.getSerializableExtra("houseType");

            tv_properytypes.setText(houseTypemode.getType());
            mode.setPost_housetypeid(houseTypemode.getId());
            mode.setPost_housetypename(houseTypemode.getType());

        }
        //详情信息
        if (requestCode == 101 && resultCode == 1006) {
            String content = data.getStringExtra("content");
            if (content != null) {
                comdetails.setText(content);
            } else {
                comdetails.setText("");
                content = "";
            }
            mode.setPost_details(content);

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

    /**
     * 动态申请权限
     */
    private void initPermission() {
        new MyPermissions(activity).getStorageCameraPermissions();
    }

    /**
     * 提示是否要放弃本次上传
     */
    private void GiveupSubmit() {
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
     * 相册选择点击事件
     */
    @Override
    public void onXiangCeOnItemClickListeming(View view, int position) {
        switch (view.getId()) {
            case R.id.item_selectxiangce_image:
                if (pictureList.size() == position) {
                    if (pictureList.size() <= 50) {
                        int num = 50 - pictureList.size();


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
                        xiangCeRecyclerAdapter.setList(pictureList);

                        // xiangCeRecyclerAdapter.notifyDataSetChanged();
                        if (pictureList.size() != 0 || pictureList.size() != 1) {
                            mPic_num.setText(pictureList.size() + "/50photos");
                        } else {
                            mPic_num.setText(pictureList.size() + "/50photo");
                        }
                    }
                } else {
                    ToastUtils.showToast(activity, "Upload, cannot delete");
                }
                if (pictureList.size() != 0 || pictureList.size() != 1) {
                    mPic_num.setText(pictureList.size() + "/50photos");
                } else {
                    mPic_num.setText(pictureList.size() + "/50photo");
                }
                break;
            default:
                break;
        }
    }


    private void execCommand(String cmd) {
        File mFile = new File(currentOutputVideoPath);
        if (mFile.exists()) {
            mFile.delete();
        }

        mCompressor.execCommand(cmd, new CompressListener() {
            @Override
            public void onExecSuccess(String message) {

                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("mCompressor", "runhandler");

                    }
                });


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


    /**
     * 上传视频
     */
    private void submitVideo(String path) {
        setSubmitButtonSatte(false);
        Log.i("videoUrl", "submitVideo");
        currentInputVideoPath = path;
        currentOutputVideoPath = FileUtils.videoPath + System.currentTimeMillis() + ".mp4";
        String cmd = "-y -i " + currentInputVideoPath + " -strict -2 -vcodec libx264 -preset ultrafast " +
                "-crf 24 -acodec aac -ar 44100 -ac 2 -b:a 96k -s 640x480 -aspect 16:9 " + currentOutputVideoPath;
        Log.e("mCompressor", currentInputVideoPath + "----" + cmd);
        LoadingUtils.showDialog(SubmitRoomForSaleActivity.this);
        execCommand(cmd);

//


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
                                pictureList.add(img_url);
                                xiangCeRecyclerAdapter.notifyDataSetChanged();
                                if (pictureList.size() != 0 || pictureList.size() != 1) {
                                    mPic_num.setText(pictureList.size() + "/50photos");
                                } else {
                                    mPic_num.setText(pictureList.size() + "/50photo");
                                }
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
            ToastUtils.showToast(activity, "Please fill in your price");
            return;
        }
        mode.setPost_price(price);

        String bedroom = typeHashMap.get("bedroom");
        if (bedroom == null || bedroom.equals("")) {
            ToastUtils.showToast(activity, "Please fill in your bedroom");
            return;
        }
        mode.setPost_bedsnum(bedroom);

        String bathroom = typeHashMap.get("bathroom");
        if (bathroom == null || bathroom.equals("")) {
            ToastUtils.showToast(activity, "Please fill in your bathroom");
            return;
        }
        mode.setPost_bathsnum(bathroom);


        String garage = typeHashMap.get("garage");
        if (garage == null || garage.equals("")) {

//            return;
            garage = "";
        }
        mode.setPost_garage(garage);

        String kitchen = typeHashMap.get("kitchen");
        if (kitchen == null || kitchen.equals("")) {
            ToastUtils.showToast(activity, "Please fill in your kitchen");
            return;
        }
        mode.setPost_kitchensnum(kitchen);

        String property_price = typeHashMap.get("property_price");
        if (property_price == null || property_price.equals("")) {
            ToastUtils.showToast(activity, "Please fill out your Hoa/master plan/ month");
            return;
        }
        mode.setPost_wuyefei(property_price);

        String living_sqft = typeHashMap.get("living_sqft");
        if (living_sqft == null || living_sqft.equals("")) {
            ToastUtils.showToast(activity, "Please fill in your living_sqft");
            return;
        }
        mode.setPost_livesqft(living_sqft);

        String year_build = typeHashMap.get("year_build");
        if (year_build == null || year_build.equals("")) {
            ToastUtils.showToast(activity, "Please fill in your room year_build");
            return;
        }

        if (year_build.length() != 4 || Integer.valueOf(getCurrentYear()) < Integer.valueOf(year_build)) {
            ToastUtils.showToast(activity, "Year_Built Format is wrong");
            return;
        }
        mode.setPost_yearbuilder(year_build);

        String lot_sqft = typeHashMap.get("lot_sqft");
        if (lot_sqft == null || lot_sqft.equals("")) {
            ToastUtils.showToast(activity, "Please fill in your lot_sqft");
            return;
        }
        mode.setPost_lotsize(lot_sqft);

        String apn = typeHashMap.get("apn");
        if (apn == null || apn.equals("")) {
            ToastUtils.showToast(activity, "Please fill in your Parcel");
            return;
        }

        if (apn.length() < 5) {
            ToastUtils.showToast(activity, "Apn Format is wrong.");
            return;
        }
        mode.setPost_apn(apn);

        String mls = typeHashMap.get("mls");
        if (mls == null || mls.equals("")) {
            mls = "";
        } else {
            if (mls.length() < 5) {
                ToastUtils.showToast(activity, "MLS Format is wrong");
                return;
            }
        }

        mode.setPost_mls(mls);

        String post_details = mode.getPost_details();
//        if (post_details == null || post_details.equals("")) {
//            ToastUtils.showToast(activity, "Please fill in your room details");
//            return;
//        }
        mode.setPost_details(post_details);

        if (submitImageList.size() == 0) {
            ToastUtils.showToast(activity, "Please select several indoor photos");
            return;
        }
//        String video = mode.getPost_videopath();
//        if (TextUtils.isEmpty(video)) {
//            ToastUtils.showToast(activity, "Please upload the video");
//            return;
//        }
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

        mode.setPost_phone(phone);
        Log.i(TAG + "上传", mode.getFk_state_id());

        if (TextUtils.isEmpty(mode.getTranscation_contract())) {
            ToastUtils.showToast(activity, "Please fill in your phone Trancation contract");
            return;
        }
if(isSubmit){
    beginSubmitDataToService();
    isSubmit =false;
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

        Double price = Double.valueOf(mode.getPost_price());
        long longPrice = (long) (price * 100);
        Double wuyefei = Double.valueOf(mode.getPost_wuyefei());
        long longWuyefei = (long) (wuyefei * 100);
        Log.e("videoUrl", "------" + mode.getPost_videopath());
        String token = SharedPreferencesUtils.getToken(activity);


        MyApplication.getmMyOkhttp().post()
                .url(Constant.addHouseMessage)
                .addHeader("Authorization", "Bearer " + token)
                .addParam("id", "")
                .addParam("contact_id", "")
                .addParam("fk_city_id", mode.getFk_city_id())
                .addParam("fk_state_id", mode.getFk_state_id())
                .addParam("price", longPrice + "")
                .addParam("property_price", longWuyefei + "")
                .addParam("fk_category_id", mode.getPost_housetypeid())
                .addParam("street", mode.getName())
                .addParam("zip", mode.getZip())
                .addParam("bedroom", mode.getPost_bedsnum())
                .addParam("bathroom", mode.getPost_bathsnum())
                .addParam("garage", mode.getPost_garage())
                .addParam("kitchen", mode.getPost_kitchensnum())
                .addParam("apn", mode.getPost_apn())
                .addParam("mls", mode.getPost_mls())
                .addParam("lot_sqft", mode.getPost_lotsize())
                .addParam("living_sqft", mode.getPost_livesqft())
                .addParam("latitude", mode.getLat() + "")
                .addParam("longitude", mode.getLon() + "")
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
                .addParam("release_method", mode.getTranscation_contract())
                .enqueue(new NewRawResponseHandler(SubmitRoomForSaleActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "上传", response);
                        isSubmit =true;
                        LoadingUtils.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.getInt("state");
                            if (state == 1) {
                                new LanRenDialog(activity).getSubmitSuccessDialog(new LanRenDialog.DialogDismisListening() {
                                    @Override
                                    public void getListening() {
                                        finish();
                                    }
                                });
                            } else if (state == 703) {
                                new LanRenDialog((Activity) activity).onlyLogin();

                            } else {
                                ToastUtils.showToast(activity, jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.e("logz", "" + error_msg);
                        LoadingUtils.dismiss();
                        isSubmit =true;
                        ToastUtils.showToast(activity, getString(R.string.net_erro));
                    }
                });
    }

    // bitmap 转file
    public String saveBitmapFile(Bitmap bitmap) {
        File picturefile = new File(FileUtils.picturePath);
        if (!picturefile.exists()) {
            picturefile.mkdirs();
        }
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


    public void compressMyHeadImageHttp(String path) {
        ArrayList<LocalMedia> list = new ArrayList<>();
        LocalMedia media = new LocalMedia();
        media.setCompressPath(path);
        list.add(media);

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
                    ToastUtils.showToast(SubmitRoomForSaleActivity.this, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCurrentYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return sdf.format(date);
    }
}
