package com.yidankeji.cheng.ebuyhouse.utils.FileProviderUtils;

import android.Manifest;
import android.app.Activity;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.wevey.selector.dialog.NormalAlertDialog;
import com.yidankeji.cheng.ebuyhouse.R;

import java.io.File;
import java.util.ArrayList;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

/**
 * Created by 刘灿成 on 2018\1\2 0002.
 *  文件管理类
 */

public class FileUtils {

    public static String filePath = Environment.getExternalStorageDirectory() +"/EBuyHouse";
    public static String picturePath = filePath+"/picture/";
    public static String compressPath = filePath+"/image_compress/";
    public static String videoPath = filePath+"/video/";
    public static String pdfPath = filePath+"/pdf/";
    private Activity activity;
    private NormalAlertDialog nodialog;

    public FileUtils(Activity activity) {
        this.activity = activity;
        initPermissions();
    }

    public void initFile(final PermissionsListening listening){
        ArrayList<PermissionItem> permissionItems = new ArrayList<>();
        permissionItems.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, "Read", R.drawable.permission_ic_storage));
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "WRITE", R.drawable.permission_ic_storage));
        HiPermission.create(activity)
                .title("To apply for permission")
                .permissions(permissionItems)
                .filterColor(R.color.colorPrimary)
                .msg("For the normal operation of the program, please agree")
                .style(R.style.PermissionBlueStyle)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        listening.getPermissionsListening("onClose");
                    }
                    @Override
                    public void onFinish() {
                        File picturefile = new File(picturePath);
                        if (!picturefile.exists()) {
                            picturefile.mkdirs();
                        }
                        File compressfile = new File(compressPath);
                        if (!compressfile.exists()) {
                            compressfile.mkdirs();
                        }
                        File videoFile = new File(videoPath);
                        if (!videoFile.exists()) {
                            videoFile.mkdirs();
                        }
                        File pdfFile = new File(pdfPath);
                        if (!pdfFile.exists()) {
                            pdfFile.mkdirs();
                        }
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

    //权限申请
    private void initPermissions() {
        ArrayList<PermissionItem> permissionItems = new ArrayList<>();
        permissionItems.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, "Read from memory", R.drawable.permission_ic_storage));
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "Read from memory", R.drawable.permission_ic_storage));
        HiPermission.create(activity)
                .title("To apply for permission")
                .permissions(permissionItems)
                .filterColor(R.color.colorPrimary)
                .msg("For the normal operation of the program, please agree")
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

    /**
     * 获取图片压缩的文件地址
     */
    public void getCompressPath(){

    }

    /**
     * 获取APP缓存大小
     */
    public long getClearCache(){
        File file = new File(filePath);
        long size = getFolderSize(file);
        return size;
    }

    /**
     * 删除文件夹下的所有文件
     */
    public void removeAllFile(final boolean state, final TextView textView){
        String str;
        if (state){
            str = "Are you sure you want to clear cache";
        }else{
            str = "There is no cache file yet";
        }
        nodialog = new NormalAlertDialog.Builder(activity)
                .setHeight(0.23f).setWidth(0.65f).setTitleVisible(true)
                .setTitleText("System hint").setTitleTextColor(R.color.colorPrimary)
                .setContentText(str).setContentTextColor(R.color.colorPrimaryDark)
                .setSingleMode(true).setSingleButtonText("OK")
                .setSingleButtonTextColor(R.color.colorAccent).setCanceledOnTouchOutside(true)
                .setSingleListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (state){
                            nodialog.dismiss();
                            File file = new File(filePath);
                            RecursionDeleteFile(file);
                            textView.setText("0k");
                        }else{
                            nodialog.dismiss();
                        }
                    }
                }).build();
        nodialog.show();
    }

    //递归删除文件夹下的所有文件
    private void RecursionDeleteFile(File file){
        if(file.isFile()){
            file.delete();
            return;
        }
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return;
            }
            for(File f : childFile){
                RecursionDeleteFile(f);
            }
        }
    }

    //递归获取文件夹的大小
    private long getFolderSize(File file){
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                }else{
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            size = 0;
        }
        return size;
    }

    public interface PermissionsListening{
        void getPermissionsListening(String state);
    }
}
