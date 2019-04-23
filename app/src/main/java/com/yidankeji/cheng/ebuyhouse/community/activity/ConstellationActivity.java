package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.community.adapter.ConstellAdapter;
import com.yidankeji.cheng.ebuyhouse.utils.BaseActivity;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

import java.util.ArrayList;

public class ConstellationActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ArrayList<String> mList;
    private ListView mListView;
    private ConstellAdapter mAdapter;
    private TextView mTitleBar;
    private ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constellation);
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
        mTitleBar = (TextView) findViewById(R.id.actionbar_title);

        mBack = (ImageView) findViewById(R.id.actionbar_back);
        mBack.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.list);
        String name = getIntent().getStringExtra("name");
        if (name.equals("Gender")) {
            mTitleBar.setText("Gender");
        } else {
            mTitleBar.setText("Constellation");
        }
        setData();
    }


    public void setData() {
        mList = new ArrayList<>();
        if (mTitleBar.getText().toString().equals("Gender")) {
          getSexList();
        } else {
            getConstellList();
        }
        mAdapter = new ConstellAdapter(mList, ConstellationActivity.this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    public void getConstellList() {

        mList.add("Capricorn");
        mList.add("Aquarius");
        mList.add("Pisces");
        mList.add("Aries");
        mList.add("Taurus");
        mList.add("Gemini");
        mList.add("Cancer");
        mList.add("Leo");
        mList.add("Virgo");
        mList.add("Libra");
        mList.add("Scorpio");
        mList.add("Sagittarius");
    }

    public void getSexList() {
        mList.add("Woman");
        mList.add("Man");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = getIntent();
        intent.putExtra("key", mList.get(position));
        setResult(1, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionbar_back:
                finish();
                break;
            default:
                break;
        }
    }
}
