package com.yidankeji.cheng.ebuyhouse.utils.permissionsUtils;

import android.Manifest;
import android.app.Activity;
import android.support.v4.content.res.ResourcesCompat;

import com.yidankeji.cheng.ebuyhouse.R;

import java.util.ArrayList;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

/**
 * Created by Administrator on 2018\1\3 0003.
 */

public class MyPermissions {

    private Activity activity;

    public MyPermissions(Activity activity) {
        this.activity = activity;
    }

    /**
     * 读写
     */
    public void getStoragePermissions(){
        ArrayList<PermissionItem> permissionItems = new ArrayList<>();
        permissionItems.add(new PermissionItem(Manifest.permission.RECORD_AUDIO, "Audio", R.mipmap.balck_audio));
        getPermissions(permissionItems);
    }
    /**
     * 读写内存，拍照
     */
    public void getStorageCameraPermissions(){
        ArrayList<PermissionItem> permissionItems = new ArrayList<>();
        permissionItems.add(new PermissionItem(Manifest.permission.CAMERA, "Camera", R.drawable.permission_ic_camera));
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "write", R.drawable.permission_ic_storage));

        getPermissions(permissionItems);
    }

    /**
     * 谷歌定位
     */
    public void getDeviceLocationPermissions(GetPermissionsListening listening){
        ArrayList<PermissionItem> permissionItems = new ArrayList<>();
//        permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, "LOCATION", R.drawable.permission_ic_location));
//        permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_COARSE_LOCATION, "LOCATION", R.drawable.permission_ic_location));
//        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "WRITE", R.drawable.permission_ic_storage));

        permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, "LOCATION", R.mipmap.location_icon));
        permissionItems.add(new PermissionItem(Manifest.permission.READ_PHONE_STATE, "NUMBER", R.mipmap.phone_message_icon));
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "WRITE", R.mipmap.file_icon));

        getPermissions02(permissionItems , listening);
    }

    private void getPermissions(ArrayList<PermissionItem> list){
        HiPermission.create(activity)
                .title("Permission to apply for")
                .filterColor(ResourcesCompat.getColor(activity.getResources(), R.color.colorPrimary, activity.getTheme()))
                .msg("To function properly, please agree to permissions!")
                .permissions(list)
                .style(R.style.PermissionBlueStyle)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                    }
                    @Override
                    public void onFinish() {
                    }
                    @Override
                    public void onDeny(String permission, int position) {
                    }
                    @Override
                    public void onGuarantee(String permission, int position) {
                    }
                });
    }

    private void getPermissions02(ArrayList<PermissionItem> list , final GetPermissionsListening listening){
        HiPermission.create(activity)
                .title("Permission to apply for")
                .filterColor(ResourcesCompat.getColor(activity.getResources(), R.color.so_white, activity.getTheme()))
                .msg("To function properly, please agree to permissions!")
                .permissions(list)
                .style(R.style.PermissionBlueStyle)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        listening.getPermissionsListening("onClose");
                    }
                    @Override
                    public void onFinish() {
                        listening.getPermissionsListening("onFinish");
                    }
                    @Override
                    public void onDeny(String permission, int position) {
                        listening.getPermissionsListening("onDeny");
                    }
                    @Override
                    public void onGuarantee(String permission, int position) {
                        listening.getPermissionsListening("onGuarantee");
                    }
                });
    }

    public interface GetPermissionsListening{
        void getPermissionsListening(String state);
    }
}
