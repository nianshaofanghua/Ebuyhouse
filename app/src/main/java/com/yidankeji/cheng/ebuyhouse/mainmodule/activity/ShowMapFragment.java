package com.yidankeji.cheng.ebuyhouse.mainmodule.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.mode.MainFilterMode;
import com.yidankeji.cheng.ebuyhouse.mode.MapMode;
import com.yidankeji.cheng.ebuyhouse.mode.MyRentalRoomMode;
import com.yidankeji.cheng.ebuyhouse.mode.ShowListMode;
import com.yidankeji.cheng.ebuyhouse.mode.ShowListModel;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.LoadingUtils;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.permissionsUtils.MyPermissions;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.MapPopwindowAddress;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 首页map
 * 首先进入默认地址
 */
public class ShowMapFragment extends Fragment implements OnMapReadyCallback
        , View.OnClickListener {

    private ArrayList<MapMode> cityAddList = new ArrayList<>();//城市边缘坐标集合
    private ArrayList<ShowListMode> houselist = new ArrayList<>();//房屋信息集合
    private String TAG = "ShowMapFragment";
    private ArrayList<LatLng> latLngsList = new ArrayList<>();
    private MainFilterMode mainFilterMode;
    private GoogleMap googleMap;
    private MapFragment mapFragment;
    private String longitude = "";
    private String latitude = "";
    private FrameLayout mFrameLayout;
    private ShowListModel mShowListModel;
    private ImageView numBg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_map, container, false);

        initView(view);

        EventBus.getDefault().register(this);
        //  isGooglePlayServiceAvailable(getActivity());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i(TAG, "onResume");
        latitude = (String) SharedPreferencesUtils.getParam(getActivity(), "latitude", "");
        longitude = (String) SharedPreferencesUtils.getParam(getActivity(), "longitude", "");
        mainFilterMode = MainFragment.getMainFilterMode();
        boolean refresh_map = mainFilterMode.isRefresh_map();
        if (refresh_map) {
            mapFragment.getMapAsync(this);
        }
    }

    private void initView(View view) {
        mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.fragment_showmap_map);
        ImageView goback = (ImageView) view.findViewById(R.id.fragment_showmap_goback);
        mFrameLayout = (FrameLayout) view.findViewById(R.id.frame);
        goback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_showmap_goback:
                //setRefreshUI();
                if (googleMap == null) {
                    return;
                }
                String latitudes = (String) SharedPreferencesUtils.getParam(getActivity(), "latitude", "");
                String longitudes = (String) SharedPreferencesUtils.getParam(getActivity(), "longitude", "");
                double lat = Double.valueOf(latitudes);
                double lon = Double.valueOf(longitudes);
                getDeviceLocation(lat, lon);
                //setClearData();
                break;
            default:
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap1) {
        Log.i(TAG, "onMapReady");
        googleMap = googleMap1;
        googleMap.clear();


        googleMap.setOnMarkerClickListener(onMarkerClickListener);
        setRefreshUI();
    }

    /**
     * 更新UI
     */
    private void setRefreshUI() {
        houselist.clear();
        MainFragment.setMapRefresh(false);
        getHouseMessage();
        if (mainFilterMode.getType().equals("city")) {
            getCityEdgeLatitudeAndLongitude();
        }


    }

    /**
     * 根据城市名称请求城市边缘的经纬度
     */
    private void getCityEdgeLatitudeAndLongitude() {
        String id = mainFilterMode.getId();
        if (id == null) {
            return;
        }
        LoadingUtils.showDialog(getActivity());
        MyApplication.getmMyOkhttp().get()
                .url(Constant.shape + "city_id=" + id)
                .enqueue(new NewRawResponseHandler(getActivity()) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "城市边缘的经纬度", response);
                        LoadingUtils.dismiss();
                        getJSONCityEdgeLatitudeAndLongitude(response);
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "城市边缘的经纬度", error_msg);
                        LoadingUtils.dismiss();
                        ToastUtils.showToast(getActivity(), getString(R.string.net_erro));
                    }
                });
    }

    /**
     * 解析城市边界json数据
     */
    private void getJSONCityEdgeLatitudeAndLongitude(String json) {

        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");
                if (state == 1) {
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONObject data = content.getJSONObject("data");
                    JSONObject city = data.getJSONObject("city");
                    latitude = city.getDouble("lat") + "";
                    longitude = city.getDouble("lon") + "";
                    String city1 = city.getString("city");

                    getDeviceLocation(Double.parseDouble(latitude), Double.parseDouble(longitude));

                    String rows = data.getString("shape_json");
                    if (rows.equals("JS_ERROR")) {
                        return;
                    }
                    JSONArray jsonArray = new JSONArray(rows);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONArray array = jsonArray.getJSONArray(i);
                        ArrayList<MapMode> list = new ArrayList<>();
                        for (int j = 0; j < array.length(); j++) {
                            JSONObject object = array.getJSONObject(j);
                            MapMode mapMode = new MapMode();
                            mapMode.setLat(object.getDouble("lat"));
                            mapMode.setLon(object.getDouble("lon"));
                            list.add(mapMode);
                        }
                        drawCityBorder(list);
                    }
                } else {
                    if (TextUtils.isEmpty(latitude)) {
                        getDeviceLocation(0, 0);
                    } else {
                        getDeviceLocation(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(getActivity(), getString(R.string.json_erro));
            getDeviceLocation(Double.parseDouble(latitude), Double.parseDouble(longitude));
        }

        SharedPreferencesUtils.setParam(getActivity(), "isFist", false);
    }

    /**
     * 定位到默认位置
     */
    private void getDeviceLocation(final double lat, final double lon) {
        Log.i(TAG + "定位到默认位置", lat + "..." + lon);
        new MyPermissions(getActivity()).getDeviceLocationPermissions(new MyPermissions.GetPermissionsListening() {
            @Override
            public void getPermissionsListening(String state) {
                if (state.equals("onFinish")) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    Log.e(TAG + "权限", lat + "权限" + lon);
                    LatLng sydney = new LatLng(lat, lon);

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 9.0f));
                    googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                    googleMap.setMyLocationEnabled(true);
                    View markerView = LayoutInflater.from(getActivity()).inflate(R.layout.marker_item, null);
                    getAddress(googleMap.getMyLocation());

//                    googleMap.addMarker(new MarkerOptions()
//                            .snippet("")
//                            .title("")
//                            .position(sydney)
//                            .icon(BitmapDescriptorFactory.fromBitmap(
//                                    WindowUtils.createDrawableFromView(getActivity() , markerView)))
//                    );
                    // googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                }
            }
        });
    }

    /**
     * 画边界
     */
    private void drawCityBorder(ArrayList<MapMode> list) {
        Log.i(TAG + "城市边缘的经纬度", "画边界" + list.size());
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.geodesic(true);
        polylineOptions.color(getResources().getColor(R.color.zhutise));
        for (int i = 0; i < list.size(); i++) {
            MapMode mapMode = list.get(i);
            double lat = mapMode.getLat();//纬度
            double lon = mapMode.getLon();//经度
            LatLng latLng = new LatLng(lat, lon);
            polylineOptions.add(latLng);
        }
        googleMap.addPolyline(polylineOptions);
    }

    /**
     * 根据城市id获取房屋信息
     */
    private void getHouseMessage() {
        String id = mainFilterMode.getId();
        if (id == null) {
            return;
        }
        if (TextUtils.isEmpty(mainFilterMode.getRelease_type())) {
            // mainFilterMode.setRelease_type("sale");
        }
        if (TextUtils.isEmpty(mainFilterMode.getPrice()) || mainFilterMode.getPrice().equals("null")) {
            Log.e("信息", "1");
            mainFilterMode.setPrice("");
            Log.e("信息", "getPrice" + mainFilterMode.getPrice());
        }
        if (TextUtils.isEmpty(mainFilterMode.getBedroom()) || mainFilterMode.getBedroom().equals("null")) {
            Log.e("信息", "2");
            mainFilterMode.setBedroom("-1");
            Log.e("信息", "getBedroom" + mainFilterMode.getBedroom());
        }
        if (TextUtils.isEmpty(mainFilterMode.getBathroom()) || mainFilterMode.getBathroom().equals("null")) {
            Log.e("信息", "3");
            mainFilterMode.setBathroom("-1");
            Log.e("信息", "getBathroom" + mainFilterMode.getBathroom());

        }
        if (TextUtils.isEmpty(mainFilterMode.getLot_sqft()) || mainFilterMode.getLot_sqft().equals("null")) {
            Log.e("信息", "4");
            mainFilterMode.setLot_sqft("");
            Log.e("信息", "getLot_sqft" + mainFilterMode.getLot_sqft());
        }
        if (TextUtils.isEmpty(mainFilterMode.getDays()) || mainFilterMode.getDays().equals("null")) {
            Log.e("信息", "5");
            mainFilterMode.setDays("");
            Log.e("信息", "getDays" + mainFilterMode.getDays());

        }
        if (TextUtils.isEmpty(mainFilterMode.getRelease_type()) || mainFilterMode.getRelease_type().equals("null")) {
            Log.e("信息", "6");
            mainFilterMode.setRelease_type("");
            Log.e("信息", "getRelease_type" + mainFilterMode.getRelease_type());
        }
        if (TextUtils.isEmpty(mainFilterMode.getLiving_sqft()) || mainFilterMode.getLiving_sqft().equals("null")) {
            Log.e("信息", "7");
            mainFilterMode.setLiving_sqft("");
            Log.e("信息", "getLiving_sqft" + mainFilterMode.getLiving_sqft());
        }
        if (TextUtils.isEmpty(mainFilterMode.getKitchen()) || mainFilterMode.getKitchen().equals("null")) {
            Log.e("信息", "8");
            mainFilterMode.setKitchen("-1");
            Log.e("信息", "getKitchen" + mainFilterMode.getKitchen());
        }
        if (TextUtils.isEmpty(mainFilterMode.getYear_build()) || mainFilterMode.getYear_build().equals("null")) {
            Log.e("信息", "9");
            mainFilterMode.setYear_build("");
            Log.e("信息", "getYear_build" + mainFilterMode.getYear_build());
        }
        Log.i(TAG + "城市房屋信息", latitude + "____" + longitude + "");
        String token = SharedPreferencesUtils.getToken(getActivity());
        MyApplication.getmMyOkhttp().post()
                .url(Constant.filter)
                .addHeader("Authorization", "Bearer " + token)
                .addParam("latitude", latitude + "")
                .addParam("longitude", longitude + "")
                .addParam("fk_city_id", mainFilterMode.getId())
                .addParam("release_type", mainFilterMode.getRelease_type())
                .addParam("fk_category_id", mainFilterMode.getFk_category_id())
                .addParam("price", mainFilterMode.getPrice())
                .addParam("bedroom", mainFilterMode.getBedroom())
                .addParam("bathroom", mainFilterMode.getBathroom())
                .addParam("garage", mainFilterMode.getKitchen())
                .addParam("property_price", mainFilterMode.getProperty_price())
                .addParam("lot_sqft", mainFilterMode.getLot_sqft())
                .addParam("living_sqft", mainFilterMode.getLiving_sqft())
                .addParam("year_build", mainFilterMode.getYear_build())
                .addParam("days", mainFilterMode.getDays())
                .addParam("pageNumber", "-1")
                .addParam("pageSize", "20")
                .enqueue(new NewRawResponseHandler(getActivity()) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "城市房屋信息", response);
                        Log.e("mUrl", "城市房屋信息" + response);
                        mShowListModel = new Gson().fromJson(response, ShowListModel.class);

                        getJSONHouseMessage(response);
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "城市房屋信息", error_msg);
                        ToastUtils.showToast(getActivity(), getString(R.string.net_erro));
                    }
                });
    }

    /**
     * 解析城市房屋信息
     */
    private void getJSONHouseMessage(String json) {
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                int state = jsonObject.getInt("state");

                if (state == 1) {
                    JSONObject content = jsonObject.getJSONObject("content");
                    JSONArray rows = content.getJSONArray("rows");
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject object = rows.getJSONObject(i);
                        ShowListMode mode = new ShowListMode();
                        mode.setId(object.getString("id"));//房屋主键
                        mode.setFk_customer_id(object.getString("fk_customer_id"));//用户主键
                        mode.setPrice(object.getString("price"));//价格
                        mode.setStreet(object.getString("street"));//街道信息
                        mode.setBedroom(object.getString("bedroom") + "");//卧室数量
                        mode.setBathroom(object.getString("bathroom") + "");//浴室数量
                        mode.setKitchen(object.getString("kitchen") + "");//厨房数量
                        mode.setLot_sqft(object.getString("lot_sqft") + "");//占地面积
                        mode.setLiving_sqft(object.getString("living_sqft") + "");//使用面积
                        mode.setYear_build(object.getString("year_build") + "");//建于哪一年
                        mode.setImg_url(object.getString("img_url"));//主图
                        mode.setImg_code(object.getString("img_code"));//图片编码
                        mode.setRemark(object.getString("remark"));//备注
                        mode.setOrigin(object.getString("origin"));//来源 pc或app
                        mode.setAdd_time(object.getString("add_time"));//添加时间
                        mode.setUpdate_time(object.getString("update_time"));//更新时间
                        mode.setLatitude(object.getString("latitude") + "");//纬度
                        mode.setLongitude(object.getString("longitude") + "");//经度
                        mode.setIs_collect(object.getBoolean("is_collect"));//是否收藏
                        mode.setRelease_type(object.getString("release_type"));//出租rent、出售sale
                        mode.setCity_name(object.getString("city_name"));
                        mode.setState_name(object.getString("state_name"));
                        mode.setSimple_price(object.getString("simple_price"));
                        mode.setViewnum(object.optInt("viewNum"));
                        mode.setSavenum(object.optInt("saveNum"));
                        houselist.add(mode);
                    }
                    drawCityHouseAddress();
                } else if (state == 703) {
                    new LanRenDialog((Activity) getActivity()).onlyLogin();

                } else {
                    ToastUtils.showToast(getActivity(), jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(getActivity(), getString(R.string.json_erro));
        }
    }

    /**
     * 画房屋图标
     */
    private void drawCityHouseAddress() {
        Log.i(TAG + "城市房屋信息", "开始画房屋图标" + houselist.size());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        View markerView = LayoutInflater.from(getActivity()).inflate(R.layout.map_xiaoshuidi, null);
        TextView numTv = (TextView) markerView.findViewById(R.id.num_txt);
        numBg = (ImageView) markerView.findViewById(R.id.num_bg);
        numBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(getActivity(), "小心西藏重新整理仓库");
                numBg.setImageResource(R.mipmap.marker_gray);
            }
        });
        RelativeLayout relativeLayout = (RelativeLayout) markerView.findViewById(R.id.re_bac);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(getActivity(), "小心西藏重新整理仓库");
                numBg.setImageResource(R.mipmap.marker_gray);
            }

        });
        for (int i = 0; i < houselist.size(); i++) {
            ShowListMode showListMode = houselist.get(i);
            String release_type = showListMode.getRelease_type();
            String price = "";
            if (showListMode.getPrice().contains("$")) {
                price = showListMode.getSimple_price();
            } else {
                price = "$" + showListMode.getSimple_price();
            }
            if (release_type.equals("rent")) {
                numBg.setImageResource(R.mipmap.map_icon_ba_lv);

                numTv.setText(price + "/MO");
                numTv.setBackgroundResource(R.mipmap.map_icon_ba_lv);
                //  relativeLayout.setBackgroundResource(R.mipmap.map_icon_ba_lv);
            } else {
                numBg.setImageResource(R.mipmap.map_icon_ba_red);
                numTv.setBackgroundResource(R.mipmap.map_icon_ba_red);
                //  relativeLayout.setBackgroundResource(R.mipmap.map_icon_ba_red);
                numTv.setText(price);
            }

            double latitude = Double.parseDouble(showListMode.getLatitude());
            double longitude = Double.parseDouble(showListMode.getLongitude());
            LatLng sydney = new LatLng(latitude, longitude);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.snippet("" + i);
            markerOptions.title("房屋信息");

            googleMap.addMarker(new MarkerOptions()
                    .snippet("" + i)
                    .title("房屋信息")
                    .position(sydney)
                    .icon(BitmapDescriptorFactory.fromBitmap(
                            WindowUtils.createDrawableFromView(getActivity(), markerView)))
            );


        }
    }

    /**
     * 房屋信息（覆盖物）点击事件
     */
    GoogleMap.OnMarkerClickListener onMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            if (marker != null) {



                String snippet = marker.getSnippet();
                int markerTag = Integer.parseInt(snippet);
                ShowListMode showListMode = houselist.get(markerTag);
                String json = new Gson().toJson(mShowListModel.getContent().getRows().get(markerTag));
                Log.e("mUrl", "mUrlmUrlmUrlmUrl");
                new MapPopwindowAddress(getActivity(), showListMode, json).getDialog();
                marker.remove();
                addMarker(markerTag);
            }
            return true;
        }
    };


    // 获取地址信息
    private List<Address> getAddress(Location location) {
        List<Address> result = null;
        try {
            if (location != null) {
                Geocoder gc = new Geocoder(getActivity(), Locale.getDefault());
                result = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
                Log.e("local", "getFeatureName==" + result.get(0).getFeatureName());
                Log.e("local", "getCountryName==" + result.get(0).getCountryName());
                Log.e("local", "getAdminArea==" + result.get(0).getAdminArea());
                Log.e("local", "getCountryCode==" + result.get(0).getCountryCode());
                Log.e("local", "getLocality==" + result.get(0).getLocality());
                Log.e("local", "getPremises==" + result.get(0).getPremises());
                Log.e("local", "getSubAdminArea==" + result.get(0).getSubAdminArea());
                Log.e("local", "getSubLocality==" + result.get(0).getSubLocality());
                Log.e("local", "getSubThoroughfare==" + result.get(0).getSubThoroughfare());
                Log.e("local", "getSubThoroughfare==" + result.get(0).getSubThoroughfare());
                Log.e("local", result.get(0).getFeatureName() + "----" + result.get(0).getLocality() + "-----" + result.get(0).getCountryName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActivityResult", "onActivityResultonActivityResultonActivityResult");
        if (data == null) {
            data = new Intent();
        }
        try {
            super.onActivityResult(requestCode, resultCode, data);
        } catch (Exception ignored) {
        }

    }

    public boolean isGooglePlayServiceAvailable(Context context) {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (status == ConnectionResult.SUCCESS) {
            Log.e("YXH", "GooglePlayServicesUtil service is available.");
            return true;
        } else {
            Log.e("YXH", "GooglePlayServicesUtil service is NOT available.");
            mFrameLayout.setVisibility(View.GONE);
            new LanRenDialog((Activity) context).isNoGoogleMap();
            return false;
        }
    }


    public void setClearData() {
        mainFilterMode.setRelease_type("");
        /**/

        mainFilterMode.setFk_category_id("");
        mainFilterMode.setHouseName("Any");
        /**/

        mainFilterMode.setBedroom("-1");
        mainFilterMode.setBathroom("-1");
        mainFilterMode.setKitchen("-1");
        /**/

        /**/
        mainFilterMode.setPriceName("Any");
        mainFilterMode.setPrice("");
        mainFilterMode.setDayName("Any");
        mainFilterMode.setDays("");
        mainFilterMode.setProperty_priceName("Any");
        mainFilterMode.setProperty_price("");
        mainFilterMode.setYear_buildName("Any");
        mainFilterMode.setYear_build("");
        mainFilterMode.setLot_sqftName("Any");
        mainFilterMode.setLot_sqft("");
        mainFilterMode.setLiving_sqftName("Any");
        mainFilterMode.setLiving_sqft("");

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setReflesh(MyRentalRoomMode reflesh) {
        //setRefreshUI();

        if (googleMap == null) {
            return;
        }

        String latitudes = (String) SharedPreferencesUtils.getParam(getActivity(), "latitude", "");
        String longitudes = (String) SharedPreferencesUtils.getParam(getActivity(), "longitude", "");
        double lat = Double.valueOf(latitudes);
        double lon = Double.valueOf(longitudes);
        getDeviceLocation(lat, lon);
        mainFilterMode.setId("");
        mainFilterMode.setType("");
        mainFilterMode.setName("");
    }

    public void addMarker(int i){
        View markerView = LayoutInflater.from(getActivity()).inflate(R.layout.map_xiaoshuidi, null);
        TextView numTv = (TextView) markerView.findViewById(R.id.num_txt);
        numBg = (ImageView) markerView.findViewById(R.id.num_bg);
        numBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                numBg.setImageResource(R.mipmap.marker_gray);
            }
        });
        RelativeLayout relativeLayout = (RelativeLayout) markerView.findViewById(R.id.re_bac);

        ShowListMode showListMode = houselist.get(i);
        String release_type = showListMode.getRelease_type();
        String price = "";
        if (showListMode.getPrice().contains("$")) {
            price = showListMode.getSimple_price();
        } else {
            price = "$" + showListMode.getSimple_price();
        }
        if (release_type.equals("rent")) {
            numBg.setImageResource(R.mipmap.map_icon_ba_lv);

            numTv.setText(price + "/MO");
            numTv.setBackgroundResource(R.mipmap.map_icon_ba_lv);
            //  relativeLayout.setBackgroundResource(R.mipmap.map_icon_ba_lv);
        } else {
            numBg.setImageResource(R.mipmap.map_icon_ba_red);
            numTv.setBackgroundResource(R.mipmap.map_icon_ba_red);
            //  relativeLayout.setBackgroundResource(R.mipmap.map_icon_ba_red);
            numTv.setText(price);
        }
        numBg.setImageResource(R.mipmap.marker_gray);
        numTv.setBackgroundResource(R.mipmap.marker_gray);
        double latitude = Double.parseDouble(showListMode.getLatitude());
        double longitude = Double.parseDouble(showListMode.getLongitude());
        LatLng sydney = new LatLng(latitude, longitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.snippet("" + i);
        markerOptions.title("房屋信息");

        googleMap.addMarker(new MarkerOptions()
                .snippet("" + i)
                .title("房屋信息")
                .position(sydney)
                .icon(BitmapDescriptorFactory.fromBitmap(
                        WindowUtils.createDrawableFromView(getActivity(), markerView)))
        );
    }


}
