package com.yidankeji.cheng.ebuyhouse.housemodule.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.mode.PostRoom.PostRoomMode;
import com.yidankeji.cheng.ebuyhouse.utils.WindowUtils;
import com.yidankeji.cheng.ebuyhouse.utils.popwindow.LanRenDialog;

import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

/**
 * 上传房屋后，验证码地址
 *      进入地图页面
 */
public class PostRoomMapActivity extends Activity implements OnMapReadyCallback
        ,View.OnClickListener,GoogleMap.OnCameraIdleListener{

    private PostRoomMode postRoomMode;
    private String TAG = "PostRoomMap";
    private Activity activity;
    private GoogleMap mGoogleMap;
    private FrameLayout mFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postroom_map);
        activity = PostRoomMapActivity.this;

        postRoomMode = (PostRoomMode) getIntent().getSerializableExtra("postRoomMode");
        if (postRoomMode == null){
            finish();
        }
        initView();
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            FrameLayout yincang = (FrameLayout) findViewById(R.id.postroom_map_yincang);
            mFrameLayout = (FrameLayout) findViewById(R.id.frame);
            yincang.setVisibility(View.VISIBLE);
            int statusBarHeight = WindowUtils.getStatusBarHeight(this);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)yincang.getLayoutParams();
            params.height = statusBarHeight;
            yincang.setLayoutParams(params);
        }

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.postroom_map);
        mapFragment.getMapAsync(this);
        ImageView back = (ImageView) findViewById(R.id.postroom_map_back);
        back.setOnClickListener(this);
        TextView submit = (TextView) findViewById(R.id.postroom_map_submit);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.postroom_map_back:
                finish();
                break;
            case R.id.postroom_map_submit:
                String release_type = postRoomMode.getRelease_type();
                Log.i(TAG+"出租还是出售" , release_type);
                Bundle bundle = new Bundle();
                bundle.putSerializable("postroomMode" , postRoomMode);
                if(release_type.equals("sale")){
                    Intent intent = new Intent(PostRoomMapActivity.this , SubmitRoomForSaleActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                    PostRoom02Activity.activity.finish();
                }else {
                    Intent intent = new Intent(PostRoomMapActivity.this , SubmitRoomAllTypeActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                    PostRoom02Activity.activity.finish();
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        initPermission(googleMap);
    }

    private void initPermission(final GoogleMap googleMap) {
        List<PermissionItem> permissionItems = new ArrayList<>();
        permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, "Camera", R.drawable.permission_ic_location));
        permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_COARSE_LOCATION, "write", R.drawable.permission_ic_location));
        HiPermission.create(activity)
                .title("Permission to apply for")
                .filterColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, getTheme()))
                .msg("To function properly, please agree to permissions!")
                .permissions(permissionItems)
                .style(R.style.PermissionBlueStyle)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                    }
                    @Override
                    public void onFinish() {
                        setAddress(googleMap);
                    }
                    @Override
                    public void onDeny(String permission, int position) {
                    }
                    @Override
                    public void onGuarantee(String permission, int position) {
                    }
                });
    }
    //定位
    public void setAddress(GoogleMap googleMap) {
        double lat = postRoomMode.getLat();
        double lng = postRoomMode.getLon();
        LatLng appointLoc = new LatLng(lat, lng);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(appointLoc));
       // googleMap.addMarker(new MarkerOptions().position(appointLoc).title("当前位置"));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        googleMap.animateCamera(zoom);
        mGoogleMap = googleMap;
        mGoogleMap.setOnCameraIdleListener(this);
    }


    @Override
    public void onCameraIdle() {
        //移动地图，地图静止时，获取中心点的经纬度
        CameraPosition cameraPosition = mGoogleMap.getCameraPosition();
        LatLng latLng = cameraPosition.target;
        Log.e("onCameraMoveCanceled",latLng.latitude+"----"+latLng.longitude);
        postRoomMode.setLat(latLng.latitude);
        postRoomMode.setLon(latLng.longitude);
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
