package com.yidankeji.cheng.ebuyhouse.mainmodule.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.mode.MapMode;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.SPUtil;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.permissionsUtils.MyPermissions;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StartActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener {

    private Activity activity;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        activity = StartActivity.this;
        initView();
        getHash();
        initData();
        isGooglePlayServiceAvailable(this);
    }

    private void initData() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(activity)
                    .enableAutoManage(StartActivity.this, this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        SharedPreferencesUtils.setParam(activity, "isFist", true);
    }

    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(1);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("test", "onConnected");
        getDeviceLocation();
        // getConfigureHttp();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("test", "onConnectionSuspended");
        getConfigureHttp();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("test", "onConnectionFailed");
        getConfigureHttp();
    }

    public void getDeviceLocation() {
        Log.e("getDeviceLocation", "getDeviceLocationgetDeviceLocationgetDeviceLocation");
        new MyPermissions(activity).getDeviceLocationPermissions(new MyPermissions.GetPermissionsListening() {

            private String longitude;
            private String latitude;

            @Override
            public void getPermissionsListening(String state) {
                Log.e("logzz","getPermissionsListening"+state);

                if (state.equals("onFinish")) {

                    if (ActivityCompat.checkSelfPermission(activity,
                            Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(activity,
                                    Manifest.permission.ACCESS_COARSE_LOCATION) !=
                                    PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    if (lastLocation != null) {

                        latitude = lastLocation.getLatitude() + "";
                        longitude = lastLocation.getLongitude() + "";

                        getAddress(lastLocation);


                    } else {

                    }
                    if (latitude == null || (latitude.equals("") || longitude.equals(""))) {
                        getConfigureHttp();
                    } else {

                        SharedPreferencesUtils.setParam(activity, "latitude", latitude);
                        SharedPreferencesUtils.setParam(activity, "longitude", longitude);
                        if (SharedPreferencesUtils.isLogin(activity)) {
                            isCanKeepLogin(activity);
                            // new MyThread().start();
                        } else {
                            new MyThread().start();
                        }

                    }
                } else {
                    Log.e("logzz","未获得权限");
                    getConfigureHttp();
                }
            }
        });
    }

    private ArrayList<MapMode> configureList = new ArrayList<>();

    private void getConfigureHttp() {
        MyApplication.getmMyOkhttp().get()
                .url(Constant.configure)
                .enqueue(new NewRawResponseHandler(StartActivity.this) {
                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.e("logzz",""+response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.getInt("state");

                            if (state == 1) {
                                JSONObject content = jsonObject.getJSONObject("content");
                                JSONArray rows = content.getJSONArray("rows");
                                for (int i = 0; i < rows.length(); i++) {
                                    JSONObject object1 = rows.getJSONObject(i);
                                    MapMode mode = new MapMode();
                                    mode.setK(object1.getString("k"));
                                    mode.setV(object1.getString("v"));
                                    configureList.add(mode);
                                }

                                String values = configureList.get(0).getV();
                                if (values.contains(",")) {
                                    String[] split = values.split(",");
                                    SharedPreferencesUtils.setParam(activity, "latitude", split[1]);
                                    SharedPreferencesUtils.setParam(activity, "longitude", split[0]);
                                }
                                new MyThread().start();
                            } else if (state == 703) {
                                Log.e("getDeviceLocation", "state==703");
                                new LanRenDialog((Activity) StartActivity.this).onlyLogin();

                            } else {
                                ToastUtils.showToast(StartActivity.this, jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        new MyThread().start();
                    }
                });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    // 获取地址信息
    private List<Address> getAddress(Location location) {
        List<Address> result = null;
        try {
            if (location != null) {
                Geocoder gc = new Geocoder(this, Locale.getDefault());
                result = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);

                SPUtil.put(StartActivity.this, "logincountry", result.get(0).getLocale().getDisplayCountry());
                SPUtil.put(StartActivity.this, "loginarea", result.get(0).getAdminArea());
                SPUtil.put(StartActivity.this, "logincity", result.get(0).getLocality());
                Log.e("local", result.get(0).getFeatureName() + "----" + result.get(0).getLocality() + "-----" + result.get(0).getCountryName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void isCanKeepLogin(final Activity activity) {
        Log.e("test", "onSuccess");
        String token = SharedPreferencesUtils.getToken(this);
        MyApplication.getmMyOkhttp().get()
                .addHeader("Authorization", "Bearer " + token)
                .url(Constant.validateLogin)
                .enqueue(new NewRawResponseHandler(StartActivity.this) {
                    @Override
                    public void onFailure(Object object,int statusCode, String error_msg) {
                        new MyThread().start();
                    }

                    @Override
                    public void onSuccess(Object object,int statusCode, String response) {
                        Log.e("test", "onSuccess" + response);

                        JSONObject content = null;
                        try {
                            Log.e("test", "state =try= 703");
                            JSONObject jsonObject = new JSONObject(response);
                            int state = jsonObject.getInt("state");
                            if (state == 703) {
                                new LanRenDialog(StartActivity.this).onlyLogin();

                            } else {
                                new MyThread().start();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                });
    }

    public static boolean isGooglePlayServiceAvailable(Context context) {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (status == ConnectionResult.SUCCESS) {
            Log.e("YXH", "GooglePlayServicesUtil service is available.");
            return true;
        } else {
            Log.e("YXH", "GooglePlayServicesUtil service is NOT available.");
            new LanRenDialog((Activity) context).isCanGoogleMap();
            return false;
        }
    }

    public void getHash(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo( getPackageName(),  PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String KeyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("KeyHash:", "KeyHash:"+KeyHash);//两次获取的不一样  此处取第一个的值

            }
        } catch (Exception e) {
            Log.e("KeyHash:", "KeyHash:"+e.toString());//两次获取的不一样  此处取第一个的值
        }
    }
}
