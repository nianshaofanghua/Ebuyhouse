package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.community.mode.InterestDetailModel;
import com.yidankeji.cheng.ebuyhouse.community.mode.LabelListModel;
import com.yidankeji.cheng.ebuyhouse.mode.ErrorModel;
import com.yidankeji.cheng.ebuyhouse.utils.BaseActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.FlowGroupView;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

import java.util.ArrayList;

public class AgainChoseLabelActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_close, tv_submit;
    private FlowGroupView mGroupView;

    private TextView mTitleBar;
    private ImageView mBack;
    private TextView mRight;
    private InterestDetailModel mModel;
    private Activity mActivity;
    private ArrayList<LabelListModel.ContentBean.RowsBean> mLabelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_again_chose_label);
        mActivity = this;
        initView();
    }

    public void initView() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.housetype_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        String json = getIntent().getStringExtra("json");

        mModel = new Gson().fromJson(json, InterestDetailModel.class);

        mTitleBar = (TextView) findViewById(R.id.actionbar_title);
        mTitleBar.setText("Interest type");
        mBack = (ImageView) findViewById(R.id.actionbar_back);
        mRight = (TextView) findViewById(R.id.actionbar_right);
        mRight.setText("user-defined");
        mRight.setOnClickListener(this);
        mBack.setOnClickListener(this);
        tv_close = (TextView) findViewById(R.id.tv_close);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        tv_close.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
        mGroupView = (FlowGroupView) findViewById(R.id.flow);
        mLabelList = new ArrayList<>();
        getLabelList();



    }

    public void setFlow() {


        for (int j = 0; j < mLabelList.size();
             j++) {
            final TextView text = new TextView(AgainChoseLabelActivity.this);
            ViewGroup.MarginLayoutParams parm = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            parm.setMargins(10, 20, 10, 10);
            text.setLayoutParams(parm);
            text.setBackground(getResources().getDrawable(R.drawable.shape_nosolid_gray));
            text.setText(mLabelList.get(j).getLabel());
            text.setPadding(30, 20, 30, 20);
            text.setTextSize(14);
            text.setTextColor(getResources().getColor(R.color.black));
            text.setTag(0);
            final int finalJ = j;
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((int) (text.getTag()) == 0) {
                        text.setTag(1);
                        mLabelList.get(finalJ).setChose(true);
                        text.setBackground(getResources().getDrawable(R.drawable.shape_label_chose));
                        text.setTextColor(getResources().getColor(R.color.text_red));
                    } else {
                        text.setTag(0);
                        mLabelList.get(finalJ).setChose(false);
                        text.setBackground(getResources().getDrawable(R.drawable.shape_label_nochose));
                        text.setTextColor(getResources().getColor(R.color.black));
                    }
                }
            });


            mGroupView.addView(text);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back:
                finish();
                break;
            case R.id.actionbar_right:
                Intent intent = new Intent(AgainChoseLabelActivity.this, AddInterestLabelActivity.class);
                intent.putExtra("id", mModel.getContent().getData().getFk_community_id());
                startActivity(intent);
                break;
            case R.id.tv_close:
                finish();

                break;
            case R.id.tv_submit:
                String labelId = "";
                for (LabelListModel.ContentBean.RowsBean bean :
                        mLabelList) {
                    if (bean.isChose()) {
                        if (TextUtils.isEmpty(labelId)) {
                            labelId = bean.getId();
                        } else {
                            labelId = labelId + "," + bean.getId();
                        }
                    }
                }

                if (!TextUtils.isEmpty(labelId)) {
                    reviseLabel(labelId);
                } else {
                    ToastUtils.showToast(mActivity, "Please chose label");
                }

                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGroupView.removeAllViews();
        getLabelList();

    }

    public void reviseLabel(String label) {
        String token = SharedPreferencesUtils.getToken(mActivity);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.revise_label.replace("integerId",mModel.getContent().getData().getInterest_id()))
                .addHeader("Authorization", "Bearer " + token)
                .addParam("label", label)

                .enqueue(new NewRawResponseHandler(mActivity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("label",""+response);
                        ErrorModel model = new Gson().fromJson(response, ErrorModel.class);
                        if (model.getState() == 1) {
                          toActivityFinish(WatchLabelActivity.class.getName());
                          finish();
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });
    }
    public void getLabelList() {
        String token = SharedPreferencesUtils.getToken(mActivity);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.label_list.replace("communityId", mModel.getContent().getData().getFk_community_id()))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(mActivity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("label", "" + response);
                        LabelListModel model = new Gson().fromJson(response, LabelListModel.class);

                        if (model.getState() == 1) {
                            mLabelList = (ArrayList<LabelListModel.ContentBean.RowsBean>) model.getContent().getRows();
                            setFlow();
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });
    }

}
