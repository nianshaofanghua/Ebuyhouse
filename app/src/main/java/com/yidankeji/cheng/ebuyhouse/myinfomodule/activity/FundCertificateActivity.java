package com.yidankeji.cheng.ebuyhouse.myinfomodule.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.housemodule.adapter.FundCerificationAdapter;
import com.yidankeji.cheng.ebuyhouse.housemodule.httputils.SubmitRoomHttpUtils;
import com.yidankeji.cheng.ebuyhouse.housemodule.mode.FundCerificateModel;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.PhotoView.PhotoViewUtils;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.ImageLogoUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.makeramen.roundedimageview.RoundedImageView.TAG;

public class FundCertificateActivity extends AppCompatActivity implements View.OnClickListener, FundCerificationAdapter.XiangCeOnItemClickListeming {
    private ImageView mImg01, mImg02, mImg03;
    private Intent mIntent;
    private String code;
    private ArrayList<String> mList;
    private ImageView mDeleteImg01, mDeleteImg02, mDeleteImg03;
    private String mStringImg01, mStringImg02, mStringImg03;
    private ArrayList<String> mPicList;
    private int mPicPos;
    private ArrayList<String> mSubmitPic;
    private Activity activity;
    private TextView mSubmit;
    private RecyclerView xiangceRecycler;
    private FundCerificationAdapter xiangCeRecyclerAdapter;
    private ArrayList<String> pictureList = new ArrayList<>();
    private ArrayList<String> submitImageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_certificate);
        activity = this;
        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.myzijindata_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        mList = new ArrayList<>();
        mIntent = getIntent();
        mPicList = new ArrayList<>();
        mSubmitPic = new ArrayList<>();
        code = mIntent.getStringExtra("code");


        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("Funding Verification");
        TextView right = (TextView) findViewById(R.id.action_bar_right);
        xiangceRecycler = (RecyclerView) findViewById(R.id.item_submitroom_xiangce);
        xiangCeRecyclerAdapter = new FundCerificationAdapter(activity, pictureList, true);
        xiangCeRecyclerAdapter.setListeming(this);
        xiangceRecycler.setAdapter(xiangCeRecyclerAdapter);
        xiangceRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        mSubmit = (TextView) findViewById(R.id.submit);
        mSubmit.setOnClickListener(this);

        mPicList = new ArrayList<>();


        Log.e("logzz", "" + code);
        getImg();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.action_bar_back:
                finish();
                break;
            case R.id.submit:
                ArrayList<LocalMedia> picList = new ArrayList<>();
                for (String pic :
                        pictureList) {
                    if(!pic.contains("http")){
                        LocalMedia local = new LocalMedia();
                        local.setCompressPath(pic);
                        picList.add(local);
                    }

                }
                if (picList.size() != 0) {
                    LoadingUtils.showDialog(activity);
                    submitImagr(picList);
                } else {
                    submit();
                }

                break;
            default:
                break;
        }
    }


    public void getImg() {


        String token = SharedPreferencesUtils.getToken(FundCertificateActivity.this);

        MyApplication.getmMyOkhttp().get()
                .url(Constant.moneypic.replace("message_code", code))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(FundCertificateActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("logzz", "" + response);
                        FundCerificateModel model = new Gson().fromJson(response, FundCerificateModel.class);


                        if (model.getState() == 1) {
                            pictureList.addAll(model.getContent().getData().getCapitals());
                            if (model.getContent().getData().getIs_update() == 0) {
                                xiangCeRecyclerAdapter.setBoo(false);
                                mSubmit.setVisibility(View.GONE);
                            } else {
                                xiangCeRecyclerAdapter.setBoo(true);
                                mSubmit.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Handler handler = new Handler();


        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia local :
                            selectList) {
                        pictureList.add(local.getCompressPath());

                    }
                    xiangCeRecyclerAdapter.setList(pictureList);
                    switch (selectList.size()) {
                        case 0:
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
                case PictureConfig.TYPE_VIDEO:


                    break;
                default:
                    break;
            }
        }

    }

    private void submitImagr(final List<LocalMedia> selectList) {

        new SubmitRoomHttpUtils(activity).compressedImage(selectList, new SubmitRoomHttpUtils.SubmitRoomImageListening() {
            @Override
            public void getSubmitRoomImageListening(String state, String json) {

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
                    submit();
                }
            }
        });
    }

    public void submit() {

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
        String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.moneypic.replace("message_code", code))
                .addParam("capitals", picPath)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(FundCertificateActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("logzz", "" + response);
                        LoadingUtils.dismiss();
                        FundCerificateModel model = new Gson().fromJson(response, FundCerificateModel.class);


                        if (model.getState() == 1) {
                            finish();


                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        LoadingUtils.dismiss();
                    }
                });

    }

    @Override
    public void onXiangCeOnItemClickListeming(View view, int position) {
        switch (view.getId()) {
            case R.id.item_selectxiangce_image:
                if (pictureList.size() == position) {
                    if (pictureList.size() < 3) {
                        int num = 3 - pictureList.size();
                        Log.i(TAG + "相册选择", num + "");
                        new ImageLogoUtils(activity).getImageLogoDialog(num);
                    }
                } else {
                    PhotoViewUtils.getPhotoView(activity, pictureList, 0);
                }
                break;
            case R.id.item_selectxiangce_delete:
                if (pictureList.size() != 0) {
                    pictureList.remove(position);
                    xiangCeRecyclerAdapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }
}
