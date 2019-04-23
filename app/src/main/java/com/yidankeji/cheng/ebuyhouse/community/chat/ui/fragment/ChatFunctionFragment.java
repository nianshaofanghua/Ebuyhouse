package com.yidankeji.cheng.ebuyhouse.community.chat.ui.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.community.chat.base.BaseFragment;
import com.yidankeji.cheng.ebuyhouse.utils.FileProviderUtils.FileUtils;

import java.io.File;


/**
 * 作者：Rance on 2016/12/13 16:01
 * 邮箱：rance935@163.com
 */
public class ChatFunctionFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private static final int CROP_PHOTO = 2;
    private static final int REQUEST_CODE_PICK_IMAGE = 3;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    private File output;
    private Uri imageUri;
    private TextView camera, album;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_chat_function, container, false);

        }
        initView(rootView);
        return rootView;
    }

    public void initView(View view) {
        camera = view.findViewById(R.id.chat_function_photo);
        album = view.findViewById(R.id.chat_function_photograph);
        camera.setOnClickListener(this);
        album.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chat_function_photo:
//                if (ContextCompat.checkSelfPermission(getActivity(),
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(getActivity(),
//                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                            MY_PERMISSIONS_REQUEST_CALL_PHONE2);
//
//                } else {
//                    takePhoto();
//                }

                PictureSelector.create(getActivity())
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(1)
                        .setOutputCameraPath(FileUtils.picturePath)
                        .compress(true)
                        .enableCrop(true)
                        .freeStyleCropEnabled(true)
                        .compressSavePath(FileUtils.compressPath)
                        .cropCompressQuality(90)
                        .minimumCompressSize(100)
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            case R.id.chat_function_photograph:
//                if (ContextCompat.checkSelfPermission(getActivity(),
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(getActivity(),
//                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                            MY_PERMISSIONS_REQUEST_CALL_PHONE2);
//
//                } else {
//                    choosePhoto();
//                }
                PictureSelector.create(getActivity())
                        .openGallery(PictureMimeType.ofVideo())
                        .setOutputCameraPath(FileUtils.videoPath)
                        .maxSelectNum(1)
                        .previewVideo(true)
                        .enablePreviewAudio(true)
                        .videoQuality(8)
                        .videoMaxSecond(60)
                        .recordVideoSecond(60)
                        .forResult(PictureConfig.TYPE_VIDEO);
                break;
            default:
                break;
        }
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        /**
         * 最后一个参数是文件夹的名称，可以随便起
         */
        File file = new File(Environment.getExternalStorageDirectory(), "拍照");
        if (!file.exists()) {
            file.mkdir();
        }
        /**
         * 这里将时间作为不同照片的名称
         */
        output = new File(file, System.currentTimeMillis() + ".jpg");

        /**
         * 如果该文件夹已经存在，则删除它，否则创建一个
         */
        try {
            if (output.exists()) {
                output.delete();
            }
            output.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * 隐式打开拍照的Activity，并且传入CROP_PHOTO常量作为拍照结束后回调的标志
         */
        imageUri = Uri.fromFile(output);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CROP_PHOTO);

    }

    /**
     * 从相册选取图片
     */
    private void choosePhoto() {
        /**
         * 打开选择图片的界面
         */
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);

    }

    @Override
    public void onActivityResult(int req, int res, Intent data) {





//        switch (req) {
//            case CROP_PHOTO:
//                if (res == Activity.RESULT_OK) {
//                    try {
//                        MessageInfo messageInfo = new MessageInfo();
//                        messageInfo.setImageUrl(imageUri.getPath());
//                        EventBus.getDefault().post(messageInfo);
//                    } catch (Exception e) {
//                    }
//                } else {
//                    Log.d(Constants.TAG, "失败");
//                }
//
//                break;
//            case REQUEST_CODE_PICK_IMAGE:
//                if (res == Activity.RESULT_OK) {
//                    try {
//                        Uri uri = data.getData();
//                        MessageInfo messageInfo = new MessageInfo();
//                        messageInfo.setImageUrl(getRealPathFromURI(uri));
//                        EventBus.getDefault().post(messageInfo);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Log.d(Constants.TAG, e.getMessage());
//                    }
//                } else {
//                    Log.d(Constants.TAG, "失败");
//                }
//
//                break;
//
//            default:
//                break;
//        }


}

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                toastShow("请同意系统权限后继续");
            }
        }

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choosePhoto();
            } else {
                toastShow("请同意系统权限后继续");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}
