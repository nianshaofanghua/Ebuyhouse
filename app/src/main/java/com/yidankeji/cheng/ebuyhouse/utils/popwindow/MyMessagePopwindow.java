package com.yidankeji.cheng.ebuyhouse.utils.popwindow;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.adapter.MyMessage.MyMeaageSonAdapter;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.housemodule.mode.FundCerificateModel;
import com.yidankeji.cheng.ebuyhouse.loginmodule.activity.LoginActivity;
import com.yidankeji.cheng.ebuyhouse.mode.MyMessageMode;
import com.yidankeji.cheng.ebuyhouse.myinfomodule.activity.FundCertificateActivity;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.TimeUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.TokenLifeUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017\12\25 0025.
 */

public class MyMessagePopwindow implements View.OnClickListener {

    private Activity activity;
    private PopupWindow pop;
    private MyMessageMode mode;
    private EditText editText;
    private String TAG = "MyMessagePop";
    private ArrayList<MyMessageMode> list;
    private MyMeaageSonAdapter adapter;
    private int mPosition;
    private ListView listView;
    MyMessageMode firstmyMessageMode;
    private TextView tv_offer;

    public MyMessagePopwindow(Activity activity, MyMessageMode mode, int position) {
        this.activity = activity;
        this.mode = mode;
        this.mPosition = position;


    }

    public void getPopwinsow(final OnDismissListening listening) {
        View view = LayoutInflater.from(activity).inflate(R.layout.pop_mymessage, null);
        pop = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        pop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pop.setAnimationStyle(R.style.dialogWindowAnim);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                listening.setDismiss();
            }
        });

        initView(view);
    }

    private void initView(View view) {
        ImageView dis = (ImageView) view.findViewById(R.id.pop_mymessagelist_dismiss);
        dis.setOnClickListener(this);
        ImageView touxiang = (ImageView) view.findViewById(R.id.pop_mymessagelist_touxiang);
        TextView time = (TextView) view.findViewById(R.id.pop_mymessagelist_time);
        TextView reply = (TextView) view.findViewById(R.id.pop_mymessagelist_reply);
        reply.setOnClickListener(this);
        TextView name = (TextView) view.findViewById(R.id.pop_mymessagelist_name);
        TextView bianhao = (TextView) view.findViewById(R.id.pop_mymessagelist_bianhao);
        listView = (ListView) view.findViewById(R.id.pop_mymessagelist_listview);
        editText = (EditText) view.findViewById(R.id.pop_mymessagelist_edit);
        TextView submit = (TextView) view.findViewById(R.id.pop_mymessagelist_submit);
        submit.setOnClickListener(this);
        TextView email = (TextView) view.findViewById(R.id.pop_mymessagelist_email);
        tv_offer = (TextView) view.findViewById(R.id.offer);
        tv_offer.setOnClickListener(this);
        name.setText(mode.getNickname());
        String add_time = mode.getAdd_time();
        long aLong = Long.parseLong(add_time);
        String toYears = TimeUtils.getSecondToYears(aLong);
        time.setText(toYears);
        bianhao.setText(mode.getPhone_number());
        email.setText(mode.getEmail());
        Glide.with(activity).load(mode.getHead_url()).apply(MyApplication.getOptions_touxiang()).into(touxiang);
        firstmyMessageMode = new MyMessageMode();
        firstmyMessageMode.setAdd_time(mode.getAdd_time());
        firstmyMessageMode.setMessage(mode.getMessage());
        list = new ArrayList<>();
        list.add(firstmyMessageMode);
//        list = mode.getList();
     //   list.addAll(mode.getList());
        adapter = new MyMeaageSonAdapter(activity, list);
        adapter.setSingleLine(false);
        listView.setAdapter(adapter);
        getMessage(mode.getMessage_code());
        WindowUtils.setListViewHeightBasedOnChildren(listView);
        siVisFunCerificate(mode.getMessage_code());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_mymessagelist_dismiss:
                pop.dismiss();
                break;
            case R.id.pop_mymessagelist_reply:
                pop.dismiss();
                break;
            case R.id.pop_mymessagelist_submit:
                String editTextContent = WindowUtils.getEditTextContent(editText);
                if (editTextContent.isEmpty()) {
                    ToastUtils.showToast(activity, "Please type in what you want to say");
                    return;
                }
                editText.setText("");
                WindowUtils.hideKeyBoard(activity);
                getHttpData(editTextContent);
                break;
            case R.id.offer:
                Intent intent = new Intent(activity, FundCertificateActivity.class);
                intent.putExtra("code", mode.getMessage_code());
                activity.startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 追加消息
     */
    private void getHttpData(String str) {
        final String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.addmymessage)
                .addHeader("Authorization", "Bearer " + token)
                .addParam("message_code", mode.getMessage_code())
                .addParam("message", str)
                .enqueue(new NewRawResponseHandler(activity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "回答", response);

                        boolean token1 = TokenLifeUtils.getToken(activity, response);
                        if (token1) {
                            //  getJSONData(response);

//                            if (mPosition + 1 <= 20) {
//                                getHttpData(1);
//                            } else {
//                                int pageNum = (mPosition + 1) / 20;
//                                if ((mPosition + 1) % 20 == 0) {
//                                    getHttpData(pageNum);
//                                } else {
//                                    getHttpData(pageNum + 1);
//
//                                }
//
//
//                            }


                            getMessage(mode.getMessage_code());
                        } else {
                            SharedPreferencesUtils.setExit(activity);
                            activity.startActivity(new Intent(activity, LoginActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "回答", error_msg);
                        ToastUtils.showToast(activity, activity.getString(R.string.net_erro));
                    }
                });
    }

    /**
     * 解析数据
     */
    private void getJSONData(String json) {
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    MyMessageMode mode1 = new MyMessageMode();
                    String content = WindowUtils.getEditTextContent(editText);
                    String userID = (String) SharedPreferencesUtils.getParam(activity, "userID", "");
                    if (userID.equals(mode.getFk_buyer_id())) {
                        mode1.setMessage(content);
                    } else {
                        mode1.setReply(content);
                    }
                    list.add(0, mode1);
                    adapter.notifyDataSetChanged();
                    editText.setText("");
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


    public interface OnDismissListening {
        void setDismiss();
    }

    /**
     * 网络请求数据
     */
    private void getHttpData(final int pageNumber) {

        String token = SharedPreferencesUtils.getToken(activity);

        MyApplication.getmMyOkhttp().get()
                .url(Constant.mymessagelist)
                .addParam("pageNumber", pageNumber + "")
                .addParam("pageSize", 20 + "")
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(activity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "我的消息列表", response);

                        boolean token1 = TokenLifeUtils.getToken(activity, response);
                        if (token1) {

                            getJSONData01(response, pageNumber);
                        } else {
                            SharedPreferencesUtils.setExit(activity);
                            activity.startActivity(new Intent(activity, LoginActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "我的消息列表", error_msg);

                        ToastUtils.showToast(activity, activity.getString(R.string.net_erro));
                    }
                });
    }

    /**
     * 解析网络数据
     */
    private void getJSONData01(String json, int pageNum) {
        ArrayList<MyMessageMode> myMessageModes = new ArrayList<>();
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONArray rows = content.getJSONArray("rows");
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject object = rows.getJSONObject(i);
                        MyMessageMode mode = new MyMessageMode();
                        mode.setMessage(object.getString("message"));
                        mode.setEmail(object.getString("email"));
                        mode.setPhone_number(object.getString("phone_number"));
                        mode.setMessage_code(object.getString("message_code"));
                        mode.setFk_buyer_id(object.getString("fk_buyer_id"));
                        mode.setConsulte_id(object.getString("consulte_id"));
                        mode.setAdd_time(object.getString("add_time"));
                        mode.setNickname(object.getString("nickname"));
                        mode.setHead_url(object.getString("head_url"));
                        mode.setHouse_type(object.getString("house_type"));

                        ArrayList<MyMessageMode> list = new ArrayList<>();
                        JSONArray replies = object.getJSONArray("replies");
                        for (int j = 0; j < replies.length(); j++) {
                            JSONObject object1 = replies.getJSONObject(j);
                            MyMessageMode mode1 = new MyMessageMode();
                            mode1.setMessage(object1.getString("message"));
                            mode1.setReply(object1.getString("reply"));
                            mode1.setAdd_time(object1.getString("add_time"));
                            Log.e("time", "" + mode1.getAdd_time());
                            list.add(mode1);
                        }

                        mode.setList(list);
                        myMessageModes.add(mode);

                    }
                    if ((mPosition + 1) > 20) {
                        int minPosition = mPosition - (pageNum * 20);
                        mode = myMessageModes.get(minPosition);
                    } else {
                        mode = myMessageModes.get(mPosition);

                    }
                    list.clear();
                    list.add(firstmyMessageMode);
                    list.addAll(mode.getList());


                    adapter.notifyDataSetChanged();
                    adapter.setDatalist(list);
                    listView.setSelection(list.size() - 1);

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


    public void refresh(MyMessageMode myMessageMode) {
        if (pop != null && pop.isShowing()) {
//            if (mPosition + 1 <= 20) {
//                getHttpData(1);
//            } else {
//                int pageNum = (mPosition + 1) / 20;
//                if ((mPosition + 1) % 20 == 0) {
//                    getHttpData(pageNum);
//                } else {
//                    getHttpData(pageNum + 1);
//
//                }
//
//            }

            getMessage(mode.getMessage_code());
        }


    }

    /**
     * 解析网络数据
     */
    private ArrayList<MyMessageMode> getJSONData01(String json) {
        ArrayList<MyMessageMode> list01 = new ArrayList<>();
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    JSONObject object01 = jsonObject.getJSONObject("content");
                    JSONObject object = object01.getJSONObject("data");
                    MyMessageMode mode = new MyMessageMode();
                    mode.setMessage(object.getString("message"));
                    mode.setEmail(object.getString("email"));
                    mode.setPhone_number(object.getString("phone_number"));
                    mode.setMessage_code(object.getString("message_code"));
                    mode.setFk_buyer_id(object.getString("fk_buyer_id"));
                    mode.setConsulte_id(object.getString("consulte_id"));
                    mode.setAdd_time(object.getString("add_time"));
                    mode.setNickname(object.getString("nickname"));
                    mode.setHead_url(object.getString("head_url"));
                    mode.setHouse_type(object.getString("house_type"));

                    ArrayList<MyMessageMode> list = new ArrayList<>();
                    JSONArray replies = object.getJSONArray("replies");
                    for (int j = 0; j < replies.length(); j++) {
                        JSONObject object1 = replies.getJSONObject(j);
                        MyMessageMode mode1 = new MyMessageMode();
                        mode1.setMessage(object1.getString("message"));
                        mode1.setReply(object1.getString("reply"));
                        mode1.setAdd_time(object1.getString("add_time"));
                        Log.e("time", "" + mode1.getAdd_time());
                        list.add(mode1);
                    }

                    mode.setList(list);
                    list01.add(mode);


                } else if (state == 703) {
                    new LanRenDialog((Activity) activity).onlyLogin();

                } else {
                    ToastUtils.showToast(activity, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list01;
    }

    public void getMessage(String code) {
        String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp().get()
                .addHeader("Authorization", "Bearer " + token)
                .url(Constant.jpush_message.replace("message_code", code))

                .enqueue(new NewRawResponseHandler(activity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("logzz", "" + response);
                        ArrayList<MyMessageMode> parentList = new ArrayList<MyMessageMode>();
                        parentList = getJSONData01(response);
                        list.clear();
                        list.add(firstmyMessageMode);
                        list.addAll(parentList.get(0).getList());
                        adapter.notifyDataSetChanged();
                        adapter.setDatalist(list);
                        listView.setSelection(list.size() - 1);

                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });
    }

    public void siVisFunCerificate(String code) {
        String token = SharedPreferencesUtils.getToken(activity);

        MyApplication.getmMyOkhttp().get()
                .url(Constant.moneypic.replace("message_code", code))
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(activity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("logzz", "" + response);
                        FundCerificateModel model = new Gson().fromJson(response, FundCerificateModel.class);
                        if (model.getContent().getData().getIs_update() == 0&&model.getContent().getData().getCapitals().size()==0) {
                            tv_offer.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });

    }


}
