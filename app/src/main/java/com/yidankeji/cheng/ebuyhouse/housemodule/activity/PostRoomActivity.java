package com.yidankeji.cheng.ebuyhouse.housemodule.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wevey.selector.dialog.MDEditDialog;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.adapter.PostRoomAdapter;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.loginmodule.activity.LoginActivity;
import com.yidankeji.cheng.ebuyhouse.mode.PostRoom.PostRoomMode;
import com.yidankeji.cheng.ebuyhouse.mode.PostRoom.PostRoomSelectAddressMode;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.TokenLifeUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class PostRoomActivity extends Activity implements View.OnClickListener
            , PostRoomAdapter.OnItemClickListening{

    private ArrayList<PostRoomMode> list = new ArrayList<>();
    private PostRoomMode mode = null;
    private RecyclerView recyclerView;
    private EditText ed_content;
    private TextView tv_selectCity , tv_selectState ,tv_selectCode;
    private String TAG = "PostRoom";
    private boolean isFist = true;
    private PostRoomAdapter adapter;
    private MDEditDialog nameDialog;

    private boolean aaa = true;
    private int bb = 1;
    private int cc = 5;
    private String tag;

    public static Activity postromActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_room);
        postromActivity = this;

        tag = getIntent().getStringExtra("tag");
        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.postroom_yincang);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }
        ImageView back = (ImageView) findViewById(R.id.action_bar_back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.action_bar_title);
        title.setText("Post a Room");
        ed_content = (EditText) findViewById(R.id.postroom_content);
        ed_content.addTextChangedListener(textWatcher);
        TextView submit = (TextView) findViewById(R.id.postroom_submit);
        submit.setOnClickListener(this);
        /**/
        recyclerView = (RecyclerView) findViewById(R.id.postroom_recyclerview);
        adapter = new PostRoomAdapter(PostRoomActivity.this , list);
        adapter.setListening(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(PostRoomActivity.this , LinearLayoutManager.VERTICAL , false));
        /**/
        tv_selectCity = (TextView) findViewById(R.id.postroom_filter_city);
        tv_selectCity.setOnClickListener(this);
        tv_selectState = (TextView) findViewById(R.id.postroom_filter_state);
        tv_selectState.setOnClickListener(this);
        tv_selectCode = (TextView) findViewById(R.id.postroom_filter_code);
        tv_selectCode.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.action_bar_back:
                finish();
                break;
            case R.id.postroom_filter_city:
                Intent intent1 = new Intent(PostRoomActivity.this , PostRoomSelectAddressActivity.class);
                intent1.putExtra("tag" , "city");
                startActivityForResult(intent1 , 100);
                break;
            case R.id.postroom_filter_state:
                Intent intent2 = new Intent(PostRoomActivity.this , PostRoomSelectAddressActivity.class);
                intent2.putExtra("tag" , "state");
                startActivityForResult(intent2 , 100);
                break;
            case R.id.postroom_filter_code:
                getNameDialog();
                break;
            case R.id.postroom_submit:
                getSubmitHttp();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == 1006){
            if (data != null){
                PostRoomSelectAddressMode selectAddressMode = (PostRoomSelectAddressMode) data.getSerializableExtra("selectAddressMode");
                tv_selectCity.setText(selectAddressMode.getCity());
            }
        }
        if (requestCode == 100 && resultCode == 1007){
            if (data != null){
                PostRoomSelectAddressMode selectAddressMode = (PostRoomSelectAddressMode) data.getSerializableExtra("selectAddressMode");
                tv_selectState.setText(selectAddressMode.getState());
            }
        }
    }

    /**
     * 点击其他区域  键盘消失
     */
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
     * 输入框监听
     */
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.e("logzzz",s+"----"+aaa);
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.e("logzzz",s+"----"+aaa);
        }
        @Override
        public void afterTextChanged(Editable s) {
            Log.e("logzzz",s+"----"+aaa);
            if (aaa){
                String content = WindowUtils.getEditTextContent(ed_content);
                if (content == null){
                    recyclerView.setVisibility(View.GONE);
                }else{
                    if (content.length() < 3){
                        recyclerView.setVisibility(View.GONE);
                    }else{


                        getAddressFormHttp(content);
                    }
                }
            }else{
                recyclerView.setVisibility(View.GONE);
                ++bb;
                if (bb >= cc){
                    aaa = true;
                }
            }
        }
    };
    /**
     * 输入邮编对话框
     */
    public void getNameDialog(){
        nameDialog = new MDEditDialog.Builder(PostRoomActivity.this)
                .setTitleVisible(true)
                .setTitleText("Please enter zip code").setTitleTextSize(20)
                .setTitleTextColor(R.color.text_heise)
                .setContentText("")
                .setContentTextSize(18)
                .setMaxLength(7)
                .setHintText("")
                .setMaxLines(1)
                .setContentTextColor(R.color.line_huise)
                .setButtonTextSize(14)
                .setLeftButtonTextColor(R.color.text_hui)
                .setLeftButtonText("Cancle")
                .setRightButtonTextColor(R.color.text_heise)
                .setRightButtonText("OK")
                .setInputTpye(InputType.TYPE_CLASS_NUMBER)
                .setLineColor(R.color.text_heise)
                .setOnclickListener(new MDEditDialog.OnClickEditDialogListener() {
                    @Override
                    public void clickLeftButton(View view, String editText) {
                        nameDialog.dismiss();
                    }
                    @Override
                    public void clickRightButton(View view, String editText) {
                        nameDialog.dismiss();
                        tv_selectCode.setText(editText);
                    }
                }).setMinHeight(0.3f).setWidth(0.8f).build();
        nameDialog.show();
    }
    /**
     * 获取联想地址
     */
    private void getAddressFormHttp(String keyword){
        MyApplication.getmMyOkhttp().get()
                .url(Constant.address+"str="+keyword)
                .enqueue(new NewRawResponseHandler(PostRoomActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.i(TAG+"联想词" , response);
                        Log.e("logzzz",response);
                        list.clear();
                        boolean token = TokenLifeUtils.getToken(postromActivity, response);
                        if (token){
                            getJSONData(response);
                        }else{
                            SharedPreferencesUtils.setExit(postromActivity);
                            startActivity(new Intent(postromActivity , LoginActivity.class));
                        }
                    }
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        Log.i(TAG+"联想词" , error_msg);
                    }
                });
    }
    /**
     * 对联想词进项解析
     */
    private void getJSONData(String json){
        try {
            if(json != null){
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1){
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONArray rows = content.getJSONArray("rows");
                    for (int i = 0; i < rows.length() ; i++) {
                        JSONObject object = rows.getJSONObject(i);
                        PostRoomMode mode = new PostRoomMode();
                        mode.setId(object.getString("id"));
                        mode.setName(object.getString("name"));
                        mode.setLat(object.getDouble("lat"));
                        mode.setLon(object.getDouble("lon"));
                        mode.setFk_city_id(object.getString("fk_city_id"));
                        mode.setFk_state_id(object.getString("fk_state_id"));
                        mode.setState(object.getString("state"));
                        mode.setZip(object.getString("zip"));
                        mode.setCity(object.getString("city"));
                        list.add(mode);
                    }

                    if (list.size() > 0){
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                    }else{
                        recyclerView.setVisibility(View.GONE);
                    }
                }else if(state==703){
                    new LanRenDialog((Activity) PostRoomActivity.this).onlyLogin();

                }else {
                    ToastUtils.showToast( PostRoomActivity.this , jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 联想词item点击事件
     */
    @Override
    public void onItemClickListening(View view, int position) {
        recyclerView.setVisibility(View.GONE);
        aaa = false;
        bb = 1;
        mode = list.get(position);
        ed_content.setText(mode.getName());

        Log.i(TAG+"值" , mode.getName());
        tv_selectState.setText(mode.getState());
        tv_selectCode.setText(mode.getZip());
        tv_selectCity.setText(mode.getCity());
    }
    /**
     * 点击提交
     */
    private void getSubmitHttp(){
        String content = WindowUtils.getEditTextContent(ed_content);
        if (content.isEmpty()){
            ToastUtils.showToast(PostRoomActivity.this , "Please enter your house address");
            return;
        }
        if (mode == null){
            ToastUtils.showToast(PostRoomActivity.this , "Please select your house address");
            return;
        }
        if (tag.equals("01")){
            mode.setRelease_type("rent");
        }else{
            mode.setRelease_type("sale");
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("postRoomMode" , mode);
        Intent intent = new Intent(PostRoomActivity.this ,PostRoomMapActivity.class );
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
