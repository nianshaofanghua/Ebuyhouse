package com.yidankeji.cheng.ebuyhouse.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yidankeji.cheng.ebuyhouse.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
        , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onConnected(Bundle bundle) {
        getDeviceLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //开始定位
    public void getDeviceLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastLocation != null){
            LatLng sydney = new LatLng(lastLocation.getLatitude() ,lastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(sydney).title("当前位置"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney , 14.0f));
        }
    }

//    //开始定位
//    public void getDeviceLocation() {
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        Location lastLocation = null;
////        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if (lastLocation == null){
//            ToastUtils.showToast(getActivity() , "定位失败");
//        }else{
//            LatLng sydney = new LatLng(lastLocation.getLatitude() ,lastLocation.getLongitude());
////            mMap.setMyLocationEnabled(false);
////            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14.0f));
////            mMap.addMarker(new MarkerOptions().title("当前位置").position(sydney));
//        }
//    }
//
//    //根据城市名称请求城市边缘的经纬度
//    public void getCityEdgeLatitudeAndLongitude(String cityName){
//        LoadingUtils.showDialog(getActivity());
//        MyApplication.getmMyOkhttp().get()
//                .url(Constant.shape+"city="+cityName)
//                .tag(this)
//                .enqueue(new RawResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, String response) {
//                        Log.i(TAG+"经纬度" ,response);
//                        LoadingUtils.dismiss();
//                        try {
//                            if (response != null){
//                                JSONObject jsonObject = new JSONObject(response);
//                                JSONObject content = jsonObject.getJSONObject("content");
//                                JSONArray rows = content.getJSONArray("rows");
//                                for (int i = 0; i < rows.length() ; i++) {
//                                    MapMode mapMode = new MapMode();
//                                    JSONObject object = rows.getJSONObject(i);
//
//                                    ArrayList<MapMode> mapModeArrayList = new ArrayList<MapMode>();
//                                    JSONArray shape = object.getJSONArray("shape");
//                                    for (int j = 0; j < shape.length() ; j++) {
//                                        JSONObject object1 = shape.getJSONObject(j);
//                                        MapMode mode = new MapMode();
//                                        mode.setId(String.valueOf(object1.getInt("id")));
//                                        mode.setType(object1.getString("type"));
//                                        mode.setLat(object1.getDouble("lat"));
//                                        mode.setLon(object1.getDouble("lon"));
//                                        mapModeArrayList.add(mode);
//                                    }
//
//                                    mapMode.set_id(object.getString("_id"));
//                                    mapMode.setCity_id(String.valueOf(object.getInt("city_id")));
//                                    mapMode.setMapModeArrayList(mapModeArrayList);
//
//                                    cityAddList.add(mapMode);
//                                }
//
//                                drawCityBorder();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    @Override
//                    public void onFailure(int statusCode, String error_msg) {
//                        Log.i(TAG+"经纬度" ,error_msg);
//                        LoadingUtils.dismiss();
//                        ToastUtils.showToast(getActivity() , getString(R.string.net_erro));
//                    }
//                });
//    }
//
//    //画边界
//    public void drawCityBorder(){
//        ToastUtils.showToast(getActivity() , "开始画图");
//        PolylineOptions polylineOptions = new PolylineOptions();
//        polylineOptions.geodesic(true);
//        polylineOptions.color(getResources().getColor(R.color.zhutise));
//        for (int i = 0; i < cityAddList.size() ; i++) {
//            MapMode mapMode = cityAddList.get(i);
//            double lat = mapMode.getLat();//纬度
//            double lon = mapMode.getLon();//经度
//            LatLng latLng = new LatLng(lat , lon);
//            polylineOptions.add(latLng);
//        }
////        mMap.addPolyline(polylineOptions);
//    }

}
