package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.community.adapter.LabelListAdapter;
import com.yidankeji.cheng.ebuyhouse.community.mode.AddLabelIdModel;
import com.yidankeji.cheng.ebuyhouse.community.mode.AddLabelListModel;
import com.yidankeji.cheng.ebuyhouse.mode.ErrorModel;
import com.yidankeji.cheng.ebuyhouse.utils.BaseActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

import java.util.ArrayList;

public class AddInterestLabelActivity extends BaseActivity implements View.OnClickListener, LabelListAdapter.OnClickListen {
    private TextView tv_add;
    private EditText et_label;
    private TextView mTitleBar;
    private ImageView mBack;
    private ListView mListView;
    private ArrayList<AddLabelListModel> mLabelList;
    private LabelListAdapter mListAdapter;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_interest_label);
        setActivity(AddInterestLabelActivity.class.getName(),AddInterestLabelActivity.this);
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
        mListView = (ListView) findViewById(R.id.list);
        mTitleBar = (TextView) findViewById(R.id.actionbar_title);
        mTitleBar.setText("Interest type");
        mBack = (ImageView) findViewById(R.id.actionbar_back);
        mBack.setOnClickListener(this);
        tv_add = (TextView) findViewById(R.id.tv_add);
        et_label = (EditText) findViewById(R.id.et_label);
        tv_add.setOnClickListener(this);
        mLabelList = new ArrayList<>();
        mListAdapter = new LabelListAdapter(mLabelList, AddInterestLabelActivity.this);
        mListView.setAdapter(mListAdapter);
        mListAdapter.setOnClickListen(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                String newLabel = et_label.getText().toString().trim();
                et_label.setText("");
                if (TextUtils.isEmpty(newLabel)) {
                    ToastUtils.showToast(AddInterestLabelActivity.this, "Label format is error");
                    return;
                }
                for (AddLabelListModel label :
                        mLabelList) {
                    if (label.getLabel().equals(newLabel)) {
                        ToastUtils.showToast(AddInterestLabelActivity.this, "Label already have");
                        return;
                    }
                }

                if(newLabel.length()>30){
                    ToastUtils.showToast(AddInterestLabelActivity.this, "Label to long");
                    return;
                }

                addLabel(newLabel);

                break;
            case R.id.actionbar_back:
                finish();
                break;
            default:
                break;
        }
    }


    public void addLabel(final String label) {
        String token = SharedPreferencesUtils.getToken(AddInterestLabelActivity.this);
        MyApplication.getmMyOkhttp().get().url(Constant.add_label)
                .addParam("fk_community_id", id)
                .addParam("label", label)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(AddInterestLabelActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {

                        Log.e("label", "" + response);
                        AddLabelIdModel addLabelIdModel = new Gson().fromJson(response, AddLabelIdModel.class);
                        if (addLabelIdModel.getState() == 1) {
                            AddLabelListModel labelListModel = new AddLabelListModel();
                            labelListModel.setLabel(label);
                            labelListModel.setLabelId(addLabelIdModel.getContent().getData().getLabelId());
                            mLabelList.add(labelListModel);
                            mListAdapter.setList(mLabelList);

                        }

                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.e("label", "" + error_msg);
                    }
                });
    }


    public void remaveLabel(String id, final int position) {
        String token = SharedPreferencesUtils.getToken(AddInterestLabelActivity.this);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.delete_label.replace("labelId", id))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(AddInterestLabelActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("label", response);
                        ErrorModel model = new Gson().fromJson(response, ErrorModel.class);
                        if (model.getState() == 1) {
                            mLabelList.remove(position);
                            mListAdapter.setList(mLabelList);
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.e("label", error_msg);
                    }
                });


    }

    @Override
    public void OnClick(View view, int position) {
        remaveLabel(mLabelList.get(position).getLabelId(), position);
    }


}
