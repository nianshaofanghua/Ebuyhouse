package com.yidankeji.cheng.ebuyhouse.utils.popwindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.community.chat.ui.activity.ChatActivity;
import com.yidankeji.cheng.ebuyhouse.housemodule.activity.ProductDetailsActivity;
import com.yidankeji.cheng.ebuyhouse.loginmodule.activity.LoginActivity;
import com.yidankeji.cheng.ebuyhouse.mainmodule.activity.MainFragment;
import com.yidankeji.cheng.ebuyhouse.mode.ShowListMode;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.TokenLifeUtils;

import org.json.JSONObject;

/**
 * 点击地图上坐标
 * 弹出地点详情图
 */

public class MapPopwindowAddress implements View.OnClickListener {

    private Context context;
    private ShowListMode mode;
    private PopupWindow pop;
    private String TAG = "MapPopwindow";
    private ImageView collect;
    private String json;

    public MapPopwindowAddress(Context context, ShowListMode mode, String json) {
        this.context = context;
        this.mode = mode;
        this.json = json;
    }

    public void getDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.popwindows_mapaddress, null);
        pop = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pop.setAnimationStyle(R.style.dialogWindowAnim);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        initData(view);
    }

    private void initData(View view) {
        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.popwindow_mapaddress_layout01);
        layout.setOnClickListener(this);
        TextView textView = (TextView) view.findViewById(R.id.popwindow_mapaddress_check);
        textView.setOnClickListener(this);
        ImageView imageView = (ImageView) view.findViewById(R.id.popwindow_mapaddress_iamge);
        imageView.setOnClickListener(this);
        setHeight(imageView);
        collect = (ImageView) view.findViewById(R.id.popwindow_mapaddress_collect);
        collect.setOnClickListener(this);
        TextView address = (TextView) view.findViewById(R.id.popwindow_mapaddress_adddress);
        TextView price = (TextView) view.findViewById(R.id.popwindow_mapaddress_price);
        TextView address02 = (TextView) view.findViewById(R.id.popwindow_mapaddress_address02);
        TextView jishi = (TextView) view.findViewById(R.id.popwindow_mapaddress_jishi);
        TextView mViewNum = (TextView) view.findViewById(R.id.watch_num);
        TextView mSaveNum = (TextView) view.findViewById(R.id.collect_num);


        mViewNum.setText(mode.getViewnum()+"");
        mSaveNum.setText(mode.getSavenum()+"");
        jishi.setText(mode.getBedroom() + "beds " + mode.getBathroom() + "baths " + mode.getLiving_sqft() + "sqft");
        address.setText(mode.getCity_name() + " " + mode.getState_name());
        price.setText("$" + mode.getPrice());
        address02.setText(mode.getStreet());
        Glide.with(context).load(mode.getImg_url()).apply(MyApplication.getOptions_rectangles()).into(imageView);

        getHouseMessageByID(mode.getId());//获取是否收藏状态
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popwindow_mapaddress_iamge:
                pop.dismiss();
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("prodetail_id", mode.getId());
                intent.putExtra("json", json);
                context.startActivity(intent);
                break;
            case R.id.popwindow_mapaddress_check:
                boolean login1 = SharedPreferencesUtils.isLogin(context);
                if (login1) {
                    pop.dismiss();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("mode", mode);
                    getRoomId(mode.getFk_customer_id(),mode.getCustomer_nick_name());
//                    Intent intent1 = new Intent(context, CheckAvailabilityActivity.class);
//
//                    context.startActivity(intent1);
                } else {
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
                break;
            case R.id.popwindow_mapaddress_collect://是否收藏
                boolean login = SharedPreferencesUtils.isLogin(context);
                if (login) {
                    MainFragment.setMapListRefresh(true);
                    setCollectForProduct();
                } else {
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
                break;
        }
    }

    /**
     * 动态设置图片高度
     *
     * @param ima
     */
    public void setHeight(ImageView ima) {
        ViewGroup.LayoutParams params = ima.getLayoutParams();
        params.height = MyApplication.Equipment_width / 2;
        params.width = MyApplication.Equipment_width;
        ima.setLayoutParams(params);
    }
    public void getRoomId(final String id, final String name) {
        String token = SharedPreferencesUtils.getToken(context);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.get_room + "/" + id)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler((Activity) context) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.e("room", "" + response);
                        String userId = (String) SharedPreferencesUtils.getParam(context, "userID", "");
                        try {

                            if (response != null) {
                                JSONObject jsonObject = new JSONObject(response);
                                int state = jsonObject.getInt("state");
                                if (state == 1) {
                                    JSONObject content = jsonObject.getJSONObject("content");
                                    JSONObject data = content.getJSONObject("data");
                                    String roomId = data.optString("roomId");
                                    Intent intent = new Intent(context, ChatActivity.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("sourceid", userId);
                                    intent.putExtra("name", name);
                                    intent.putExtra("type", 3);
                                    intent.putExtra("roomid", roomId);
                                    context.startActivity(intent);
                                }
                            }
                        } catch (Exception e) {


                        }


                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {

                    }
                });
    }
    /**
     * 按主键查询房屋所有信息
     */
    private void getHouseMessageByID(String id) {
        collect.setClickable(false);
        String token = SharedPreferencesUtils.getToken(context);
        MyApplication.getmMyOkhttp().post()
                .url(Constant.fid + "house_id=" + id)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler((Activity) context) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "房屋信息", response);
                        collect.setClickable(true);
                        getJSONHouse(response);
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "房屋信息", error_msg);
                        collect.setClickable(true);
                    }
                });
    }

    /**
     * 对房屋信息的数据进行解析
     */
    private void getJSONHouse(String json) {
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONObject data = content.getJSONObject("data");
                    boolean is_collect = data.getBoolean("is_collect");
                    Log.i(TAG + "收藏", is_collect + "");
                    if (is_collect) {
                        collect.setImageResource(R.mipmap.pro_shoucang_ed);
                    } else {
                        collect.setImageResource(R.mipmap.pro_shoucang_e);
                    }
                } else if (state == 703) {
                    new LanRenDialog((Activity) context).onlyLogin();

                } else {
                    ToastUtils.showToast(context, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否收藏
     */
    private void setCollectForProduct() {
        collect.setClickable(false);
        String token = SharedPreferencesUtils.getToken(context);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.collect + "target_id=" + mode.getId())
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler((Activity) context) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "收藏", response);
                        collect.setClickable(true);
                        boolean token1 = TokenLifeUtils.getToken((Activity) context, response);
                        if (token1) {
                            getJSONCollectData(response);
                        } else {
                            SharedPreferencesUtils.setExit(context);
                            context.startActivity(new Intent(context, LoginActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "收藏", error_msg);
                        collect.setClickable(true);
                    }
                });
    }

    /**
     * 解析收藏json数据
     */
    private void getJSONCollectData(String json) {
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 200) {
                    String message = jsonObject.getString("message");
                    if (message.equals("success")) {
                        collect.setImageResource(R.mipmap.pro_shoucang_ed);
                    } else {
                        collect.setImageResource(R.mipmap.pro_shoucang_e);
                    }
                } else if (state == 703) {
                    new LanRenDialog((Activity) context).onlyLogin();

                } else {
                    ToastUtils.showToast(context, jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
