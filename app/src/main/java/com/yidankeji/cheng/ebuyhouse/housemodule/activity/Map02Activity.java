package com.yidankeji.cheng.ebuyhouse.housemodule.activity;

import android.app.Activity;
import android.content.Context;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.geojson.GeoJsonGeometryCollection;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

public class Map02Activity extends Activity implements OnMapReadyCallback , GoogleMap.OnCameraIdleListener {

    private String longitude;
    private String latitude;
private GoogleMap mGoogleMap;
    private FrameLayout mFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map02);

        longitude = getIntent().getStringExtra("longitude");
        latitude = getIntent().getStringExtra("latitude");
        if (longitude == null || latitude == null){
            finish();
        }
        initView();
    }

    private void initView() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.showmap02_map);
        mFrameLayout  = (FrameLayout) findViewById(R.id.frame);
        mapFragment.getMapAsync(this);
        ImageView back = (ImageView) findViewById(R.id.showmap02_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setAddress(googleMap);
    }

    //定位
    public void setAddress(GoogleMap googleMap) {
        double lat = Double.parseDouble(latitude);
        double lng = Double.parseDouble(longitude);
        LatLng appointLoc = new LatLng(lat, lng);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(appointLoc));
        googleMap.addMarker(new MarkerOptions().position(appointLoc).title("current location"));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);
        googleMap.animateCamera(zoom);
        mGoogleMap = googleMap;
        mGoogleMap.setOnCameraIdleListener(this);
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.e("onCameraMoveCanceled","onCameraChange"+ cameraPosition.target.latitude+"---"+cameraPosition.target.longitude);
//                CameraPosition cameraPosition1 = mGoogleMap.getCameraPosition();
//                LatLng latLng = cameraPosition1.target;
//                Log.e("onCameraMoveCanceled",latLng.latitude+"----"+latLng.longitude);
            }
        });
    }

    @Override
    public void onCameraIdle() {
        //移动地图，地图静止时，获取中心点的经纬度
        CameraPosition cameraPosition = mGoogleMap.getCameraPosition();
        LatLng latLng = cameraPosition.target;
        Log.e("onCameraMoveCanceled",latLng.latitude+"----"+latLng.longitude);

    }


    public  boolean isGooglePlayServiceAvailable (Context context) {
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


}
