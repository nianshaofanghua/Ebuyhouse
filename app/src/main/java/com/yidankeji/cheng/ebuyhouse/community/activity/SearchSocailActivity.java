package com.yidankeji.cheng.ebuyhouse.community.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.community.adapter.SearchListAdapter;
import com.yidankeji.cheng.ebuyhouse.community.mode.SearchSocailModel;
import com.yidankeji.cheng.ebuyhouse.utils.BaseActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;

import java.util.ArrayList;

public class SearchSocailActivity extends BaseActivity implements TextView.OnEditorActionListener, AdapterView.OnItemClickListener, View.OnClickListener, TextWatcher {
    private TextView tv_hint;
    private EditText edit_search;
    private ListView mSearchListView;
    private SearchListAdapter mListAdapter;
    private ArrayList<SearchSocailModel.ContentBean.RowsBean> mSearchList;
    private ImageView iv_back;
private ImageView iv_delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_socail);
        initView();
    }

    public void initView() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            FrameLayout yincang = (FrameLayout) findViewById(R.id.alterfragment_yincang);
//            yincang.setVisibility(View.VISIBLE);
//            int statusBarHeight = WindowUtils.getStatusBarHeight(SearchSocailActivity.this);
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) yincang.getLayoutParams();
//            params.height = statusBarHeight;
//            yincang.setLayoutParams(params);
//        }
        tv_hint = (TextView) findViewById(R.id.tv_hint);
        edit_search = (EditText) findViewById(R.id.seach_edit);
        iv_back = (ImageView) findViewById(R.id.search_back);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        iv_delete.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        mSearchListView = (ListView) findViewById(R.id.search_list);
        edit_search.setOnEditorActionListener(this);
        edit_search.addTextChangedListener(this);
        setData();
    }


    public void setData() {
        mSearchList = new ArrayList<>();
        mListAdapter = new SearchListAdapter(mSearchList, SearchSocailActivity.this);
        mSearchListView.setAdapter(mListAdapter);
        mSearchListView.setOnItemClickListener(this);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            //完成自己的事件
            if (TextUtils.isEmpty(edit_search.getText().toString().trim())) {

            }else {
                searchCircle(edit_search.getText().toString());
                edit_search.setText("");
            }

        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(SearchSocailActivity.this, SocailDetailActivity.class);
        intent.putExtra("id", mSearchList.get(position).getCommunity_id());
        startActivity(intent);
//        switch (position) {
//            case 0:
//                intent = new Intent(SearchSocailActivity.this, SocailDetailActivity.class);
//                startActivity(intent);
//                break;
//            case 1:
//                intent = new Intent(SearchSocailActivity.this, SocailDetailedActivity.class);
//                startActivity(intent);
//                break;
//            default:
//                break;
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_back:
                finish();
                break;
            case R.id.iv_delete:
                edit_search.setText("");
                mSearchList.clear();
                mListAdapter.setList(mSearchList);
                break;
            default:
                break;
        }
    }


    //  查询
    public void searchCircle(String keyword) {
        String token = SharedPreferencesUtils.getToken(SearchSocailActivity.this);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.search_social)
                .addParam("keyword", keyword)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(SearchSocailActivity.this) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("search", "" + response);

                        try {
                            SearchSocailModel model = new Gson().fromJson(response, SearchSocailModel.class);
                            if (model.getState() == 1) {
                                mSearchList = (ArrayList<SearchSocailModel.ContentBean.RowsBean>) model.getContent().getRows();
                                if (mSearchList.size() != 0) {
                                    tv_hint.setVisibility(View.GONE);
                                } else {
                                    tv_hint.setVisibility(View.VISIBLE);
                                    tv_hint.setText("Did not find the community you want");
                                }
                                mListAdapter.setList(mSearchList);
                            }
                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.e("search", "" + error_msg);
                    }
                });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        searchCircle(edit_search.getText().toString());
    }
}
