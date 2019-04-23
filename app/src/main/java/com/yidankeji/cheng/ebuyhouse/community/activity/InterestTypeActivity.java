package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.community.mode.LabelListModel;
import com.yidankeji.cheng.ebuyhouse.utils.BaseActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.FlowGroupView;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

import java.util.ArrayList;

public class InterestTypeActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_nextType;
    private FlowGroupView mGroupView;
    private TextView mTitleBar;
    private ImageView mBack;
    private TextView mRight;
    private String id;
    private ArrayList<LabelListModel.ContentBean.RowsBean> mLabelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_type);
        setActivity(InterestTypeActivity.class.getName(), InterestTypeActivity.this);
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
        id = getIntent().getStringExtra("id");
        mTitleBar = (TextView) findViewById(R.id.actionbar_title);
        mTitleBar.setText("Interest type");
        mBack = (ImageView) findViewById(R.id.actionbar_back);
        mRight = (TextView) findViewById(R.id.actionbar_right);
        mRight.setText("user-defined");
        mRight.setOnClickListener(this);
        mBack.setOnClickListener(this);
        tv_nextType = (TextView) findViewById(R.id.tv_next_step);
        tv_nextType.setOnClickListener(this);
        mGroupView = (FlowGroupView) findViewById(R.id.flow);
        mLabelList = new ArrayList<>();

        mGroupView.removeAllViews();
        getLabelList();
    }

    public void setFlow() {
        mGroupView.removeAllViews();
        for (int j = 0; j < mLabelList.size();
             j++) {
            final LabelListModel.ContentBean.RowsBean labelModel = mLabelList.get(j);
            View view = LayoutInflater.from(this).inflate(R.layout.label_item, null);
            final TextView tv_label = view.findViewById(R.id.tv_label);
            final RelativeLayout rl = view.findViewById(R.id.rl);
            ImageView delete = view.findViewById(R.id.iv_delete);
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            params.setMargins(10, 20, 10, 10);
            view.setLayoutParams(params);
            tv_label.setText(mLabelList.get(j).getLabel());
            tv_label.setTag(0);
            if (mLabelList.get(j).getIs_public() == 1) {
                delete.setVisibility(View.INVISIBLE);

            }

            if(labelModel.isChose()){
                rl.setBackground(getResources().getDrawable(R.drawable.shape_label_chose));
                tv_label.setTextColor(getResources().getColor(R.color.text_red));
            }else {
                rl.setBackground(getResources().getDrawable(R.drawable.shape_label_nochose));
                tv_label.setTextColor(getResources().getColor(R.color.black));
            }


            tv_label.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!labelModel.isChose()) {

                        labelModel.setChose(true);
                        rl.setBackground(getResources().getDrawable(R.drawable.shape_nosolid_red_circle));
                        tv_label.setTextColor(getResources().getColor(R.color.text_red));
                    } else {

                        labelModel.setChose(false);
                        rl.setBackground(getResources().getDrawable(R.drawable.shape_nosolid_gray));
                        tv_label.setTextColor(getResources().getColor(R.color.black));
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLabelList.remove(labelModel);
                    mGroupView.removeAllViews();
                    setFlow();
                }
            });


            mGroupView.addView(view);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.actionbar_back:
                finish();
                break;
            case R.id.actionbar_right:
                intent = new Intent(InterestTypeActivity.this, AddInterestLabelActivity.class);
                intent.putExtra("id", id);
                startActivityForResult(intent, 1001);
                break;
            case R.id.tv_next_step:
                String label = "";
                for (LabelListModel.ContentBean.RowsBean bean :
                        mLabelList) {
                    if (bean.isChose()) {
                        if (TextUtils.isEmpty(label)) {
                            label = bean.getId();
                        } else {
                            label = label + "," + bean.getId();
                        }
                    }
                }
                intent = new Intent(InterestTypeActivity.this, PetNameActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("label", label);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    public void getLabelList() {
        String token = SharedPreferencesUtils.getToken(InterestTypeActivity.this);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.label_list.replace("communityId", id))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(InterestTypeActivity.this) {
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
Log.e("log",""+error_msg);
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mGroupView.removeAllViews();
        getLabelList();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            mGroupView.removeAllViews();
            getLabelList();
        }
    }

    public static void toFinish() {
        InterestTypeActivity.toFinish();
    }
}
