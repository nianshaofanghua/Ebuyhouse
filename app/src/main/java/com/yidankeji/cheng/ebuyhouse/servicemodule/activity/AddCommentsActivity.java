package com.yidankeji.cheng.ebuyhouse.servicemodule.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;
import com.wevey.selector.dialog.NormalAlertDialog;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.housemodule.httputils.SubmitRoomHttpUtils;
import com.yidankeji.cheng.ebuyhouse.loginmodule.activity.LoginActivity;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.adapter.XiangCeAdapter;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.myinfomodule.activity.MyInfoActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.PhotoView.PhotoViewUtils;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.TokenLifeUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.ImageLogoUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils.dismiss;

public class AddCommentsActivity extends Activity implements View.OnClickListener
        , XiangCeAdapter.OnItemClickListening {

    private String serviceID;
    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<String> submitImageList = new ArrayList<>();
    private EditText editText;
    private Activity activity;
    private String TAG = "AddComments";
    private GridView gridView;
    private XiangCeAdapter adapter;
    private TextView submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comments);
        activity = AddCommentsActivity.this;
        serviceID = getIntent().getStringExtra("serviceID");
        if (serviceID == null) {
            finish();
        }
        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.addcomment_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("Reservation");
        editText = (EditText) findViewById(R.id.addcomment_content);
        /**/
        gridView = (GridView) findViewById(R.id.addcomment_gridview);
        adapter = new XiangCeAdapter(this, list);
        adapter.setListening(this);
        gridView.setAdapter(adapter);
        /**/
        submit = (TextView) findViewById(R.id.addcomment_submit);
        submit.setOnClickListener(this);
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
        switch (v.getId()) {
            case R.id.action_bar_back:
                finish();
                break;
            case R.id.addcomment_submit:
                getSubmitHttp();
                break;
        }
    }

    @Override
    public void onitemClickListening(View view, int position) {
        if (list.size() == position) {
            if (list.size() <= 8) {
                int num = 8 - list.size();
                Log.i(TAG + "相册选择", num + "");
                new ImageLogoUtils(AddCommentsActivity.this).getImageLogoDialog(num);
            }
        } else {
            if (view.getId() == R.id.item_xiangce_delete) {
                list.remove(position);
                adapter.notifyDataSetChanged();
            } else {
                PhotoViewUtils.getPhotoView(activity, list, 0);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    for (int i = 0; i < selectList.size(); i++) {
                        String path = selectList.get(i).getPath();
                        list.add(path);
                    }
                    WindowUtils.setGridViewHeightBasedOnChildren(gridView, 4);
                    adapter.notifyDataSetChanged();
                    postImageToService(selectList);
                    break;
            }
        }
    }

    /**
     * 网络上传图片
     */
    private void postImageToService(final List<LocalMedia> selectList) {
        setSubmitText(false);
        new SubmitRoomHttpUtils(activity).compressedImage(selectList, new SubmitRoomHttpUtils.SubmitRoomImageListening() {
            @Override
            public void getSubmitRoomImageListening(String state, String json) {
                Log.i(TAG + "上传图片", json);
                LoadingUtils.dismiss();
                if (state.equals("onSuccess")) {
                    getJSONData(json);
                } else if (state.equals("onFinished")) {
                    setSubmitText(true);
                }
            }
        });
    }

    private void getJSONData(String json) {
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONObject data = content.getJSONObject("data");
                    String img_url = data.getString("img_url");
                    submitImageList.add(img_url);
                } else {
                    ToastUtils.showToast(activity, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSubmitText(boolean tag) {
        if (tag) {
            submit.setText("Submit");
            submit.setClickable(true);
            submit.setBackgroundResource(R.drawable.shape_text_bg_zhutise);
        } else {
            submit.setClickable(false);
            submit.setBackgroundColor(getResources().getColor(R.color.text_hei));
            submit.setText("Submission File");
        }
    }

    /**
     * 提交
     */
    private void getSubmitHttp() {
        String content = WindowUtils.getEditTextContent(editText);
        if (content.isEmpty()) {
            ToastUtils.showToast(activity, "Please enter your message");
            return;
        }
        if (submitImageList.size() == 0) {
            ToastUtils.showToast(activity, "Please select your Image");
            return;
        }

        String urls = "";
        for (int i = 0; i < submitImageList.size(); i++) {
            if (urls.endsWith("")) {
                urls = submitImageList.get(i);
            } else {
                urls = urls + "," + submitImageList.get(i);
            }
        }

        LoadingUtils.showDialog(activity);
        String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.addevaluate)
                .addHeader("Authorization", "Bearer " + token)
                .addParam("partner_id", serviceID)
                .addParam("evalutae", content)
                .addParam("urls", urls)
                .enqueue(new NewRawResponseHandler(AddCommentsActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG + "上传评价", response);
                        LoadingUtils.dismiss();
                        boolean token1 = TokenLifeUtils.getToken(activity, response);
                        if (token1) {
                            getJSON(response);
                        } else {
                            SharedPreferencesUtils.setExit(activity);
                            startActivity(new Intent(activity, LoginActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        Log.i(TAG + "上传评价", error_msg);
                        LoadingUtils.dismiss();
                        ToastUtils.showToast(activity, getString(R.string.net_erro));
                    }
                });
    }

    private void getJSON(String json) {
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    new LanRenDialog(activity).getSystemHintDialog("Uploaded successfully, Please wait for background check on your comments."
                            , "OK", new LanRenDialog.DialogDismisListening() {
                                @Override
                                public void getListening() {
                                    setResult(1008, new Intent(activity, ServiceDetailActivity.class));
                                    finish();
                                }
                            });
                } else {
                    ToastUtils.showToast(activity, jsonObject.getString("message"));
                }
                if(state==703){
                    new LanRenDialog(activity).onlyLogin();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
