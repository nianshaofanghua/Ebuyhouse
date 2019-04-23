package com.yidankeji.cheng.ebuyhouse.mainmodule.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.adapter.MoreIconAdapter;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.community.activity.PersonalInformationActivity;
import com.yidankeji.cheng.ebuyhouse.housemodule.activity.PostRoom02Activity;
import com.yidankeji.cheng.ebuyhouse.loginmodule.activity.LoginActivity;
import com.yidankeji.cheng.ebuyhouse.myinfomodule.activity.HouseHistoryLogActivity;
import com.yidankeji.cheng.ebuyhouse.myinfomodule.activity.NewMyRenttalRomActivity;
import com.yidankeji.cheng.ebuyhouse.myinfomodule.activity.SaveListActivity;
import com.yidankeji.cheng.ebuyhouse.myinfomodule.activity.SetActivity;
import com.yidankeji.cheng.ebuyhouse.offermodule.ContractActivity;
import com.yidankeji.cheng.ebuyhouse.offermodule.MyZiJinDataActivity;
import com.yidankeji.cheng.ebuyhouse.offermodule.SubmitZiJinActivity;
import com.yidankeji.cheng.ebuyhouse.offermodule.httputils.OfferHttpUtils;
import com.yidankeji.cheng.ebuyhouse.servicemodule.mode.ServiceTabMode;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.TokenLifeUtils;
import com.yidankeji.cheng.ebuyhouse.utils.interfaceUtils.InterfaceUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 */
public class MoreFragment extends Fragment implements MoreIconAdapter.OnItemClickListening
        , View.OnClickListener {

    private String[] maintitle = {"Post rental", "Post for sale", "Saved homes"
            , "History record", "My listing", "Contract", "Funding and loan verification", "Settings"};
    private int[] mainicon = {R.mipmap.more_chuzu, R.mipmap.more_maifang
            , R.mipmap.more_shoucang, R.mipmap.more_history_log, R.mipmap.more_fangzi
            , R.mipmap.more_hetong, R.mipmap.more_zijin, R.mipmap.more_shezhi};
    private ArrayList<ServiceTabMode> iconList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RoundedImageView imageLogo;
    private TextView tv_name;
    private String TAG = "MoreFragment";
    private MoreIconAdapter adapter;
    int mMessageNum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    private void initView(View view) {
        Log.e("more", "initView");
        recyclerView = (RecyclerView) view.findViewById(R.id.morefragment_recycler);
        imageLogo = (RoundedImageView) view.findViewById(R.id.morefragment_touxiang);
        imageLogo.setOnClickListener(this);
        tv_name = (TextView) view.findViewById(R.id.morefragment_name);
        tv_name.setOnClickListener(this);
        initData();
    }

    public void initData() {
        for (int i = 0; i < maintitle.length; i++) {
            ServiceTabMode mode = new ServiceTabMode();

            mode.setTitle(maintitle[i]);
            mode.setLogo(mainicon[i]);
            if (i != 6) {
                iconList.add(mode);
            }

        }
        if (mMessageNum != 0) {
            iconList.get(5).setNum(mMessageNum);
        }

        adapter = new MoreIconAdapter(getActivity(), iconList);
        adapter.setOnItemClickListening(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void OnItemClickListening(View view, int position) {
        switch (position) {
            case 0:
                if (SharedPreferencesUtils.isLogin(getActivity())) {
                    Intent intent = new Intent(getActivity(), PostRoom02Activity.class);
                    String firstName = (String) SharedPreferencesUtils.getParam(getActivity(), "firstname", "");
                    String lastName = (String) SharedPreferencesUtils.getParam(getActivity(), "lastname", "");
                    if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
                        //       ToastUtils.showToast(getActivity(), "Please do real name authentication");
                        new LanRenDialog((Activity) getActivity()).isHaveName();
                        return;
                    }
                    if (firstName.equals("null") || lastName.equals("null")) {
                        //   ToastUtils.showToast(getActivity(), "Please do real name authentication");
                        new LanRenDialog((Activity) getActivity()).isHaveName();
                        return;
                    }
                    intent.putExtra("tag", "rent");
                    startActivity(intent);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case 1:
                if (SharedPreferencesUtils.isLogin(getActivity())) {
                    Intent intent = new Intent(getActivity(), PostRoom02Activity.class);
                    String firstName = (String) SharedPreferencesUtils.getParam(getActivity(), "firstname", "");
                    String lastName = (String) SharedPreferencesUtils.getParam(getActivity(), "lastname", "");
                    if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
                        ToastUtils.showToast(getActivity(), "Please fill in the first and last name.");
                        return;
                    }
                    if (firstName.equals("null") || lastName.equals("null")) {
                        ToastUtils.showToast(getActivity(), "Please fill in the first and last name.");
                        return;
                    }

                    intent.putExtra("tag", "sale");
                    startActivity(intent);
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case 2:
                if (SharedPreferencesUtils.isLogin(getActivity())) {
                    startActivity(new Intent(getActivity(), SaveListActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;

            case 3:

                if (SharedPreferencesUtils.isLogin(getActivity())) {
                    startActivity(new Intent(getActivity(), HouseHistoryLogActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }


                break;
            case 4:

                if (SharedPreferencesUtils.isLogin(getActivity())) {
                    startActivity(new Intent(getActivity(), NewMyRenttalRomActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }


                break;
            case 5:


                if (SharedPreferencesUtils.isLogin(getActivity())) {
                    startActivity(new Intent(getActivity(), ContractActivity.class));

                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }


                break;
            case 6:
                Intent intent = new Intent(getActivity(), SetActivity.class);
                startActivityForResult(intent, 100);
//                if (SharedPreferencesUtils.isLogin(getActivity())) {
//                    getZiJinData();
//                } else {
//                    startActivity(new Intent(getActivity(), LoginActivity.class));
//                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.morefragment_touxiang:
                setIntent();
                break;
            case R.id.morefragment_name:
                setIntent();
                break;
            default:
                break;
        }
    }

    /**
     * 是否登录——赋值
     */
    public void setData() {
        if (SharedPreferencesUtils.isLogin(getActivity())) {
            String myinfo_youxiang = (String) SharedPreferencesUtils.getParam(getActivity(), "myinfo_youxiang", "");
            String myinfo_name = (String) SharedPreferencesUtils.getParam(getActivity(), "myinfo_name", "");
            Glide.with(getActivity()).load(myinfo_youxiang).apply(MyApplication.getOptions_touxiang())
                    .into(imageLogo);
            tv_name.setText(myinfo_name);
        } else {
            imageLogo.setImageResource(R.mipmap.touxiang);
            tv_name.setText("Click login");
        }
    }

    /**
     * 是否登录——跳页面
     */
    public void setIntent() {
        if (SharedPreferencesUtils.isLogin(getActivity())) {
            Intent intent01 = new Intent(getActivity(), PersonalInformationActivity.class);
            startActivityForResult(intent01, 100);
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, 100);
        }
    }

    /**
     * 资金验证
     */
    private void getZiJinData() {
        LoadingUtils.showDialog(getActivity());
        new OfferHttpUtils(getActivity()).getZinJiCallBack(new InterfaceUtils.MyOkHttpCallBack() {
            @Override
            public void getHttpResultListening(String state, String json) {
                Log.i(state, json);
                LoadingUtils.dismiss();
                boolean token = TokenLifeUtils.getToken(getActivity(), json);
                if (token) {
                    getJsonDataZiJin(json);
                } else {
                    SharedPreferencesUtils.setExit(getActivity());
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }

            @Override
            public void onFailure(String state, String json) {
                LoadingUtils.dismiss();
                ToastUtils.showToast(getActivity(), getString(R.string.net_erro));
            }
        });
    }

    private void getJsonDataZiJin(String json) {
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONObject data = content.getJSONObject("data");
                    boolean isExist = data.getBoolean("isExist");
                    if (isExist) {
                        JSONArray rows = content.getJSONArray("rows");
                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject object = rows.getJSONObject(i);
                            String amount = object.getString("amount");
                            String url = object.getString("url");
                            String check_status = object.getString("check_status");
                            String add_time = object.getString("add_time");
                            String check_msg = object.getString("check_msg");

                            if (check_status == null || check_status.equals("")) {
                                startActivity(new Intent(getActivity(), SubmitZiJinActivity.class));
                            } else if (check_status.equals("3")) {
                                new LanRenDialog(getActivity()).getSystemHintDialog("Your voucher has been rejected and uploaded again"
                                        , "OK", new LanRenDialog.DialogDismisListening() {
                                            @Override
                                            public void getListening() {
                                                startActivity(new Intent(getActivity(), SubmitZiJinActivity.class));
                                            }
                                        });
                            } else if (check_status.equals("1") || check_status.equals("2")) {
                                Intent intent = new Intent(getActivity(), MyZiJinDataActivity.class);
                                intent.putExtra("amount", amount);
                                intent.putExtra("url", url);
                                intent.putExtra("check_status", check_status);
                                intent.putExtra("add_time", add_time);
                                intent.putExtra("check_msg", check_msg);
                                startActivity(intent);
                            }
                        }
                    } else {
                        startActivity(new Intent(getActivity(), SubmitZiJinActivity.class));
                    }
                } else if (state == 703) {
                    new LanRenDialog((Activity) getActivity()).onlyLogin();

                } else {
                    ToastUtils.showToast(getActivity(), jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refesh(int num) {
        mMessageNum = num;


        if (iconList.size() != 0) {


            iconList.get(5).setNum(num);
            adapter.setIconList(iconList);
        }

    }

    public void exit() {
        iconList.get(4).setNum(0);
        adapter.setIconList(iconList);
    }

}
