package com.yidankeji.cheng.ebuyhouse.utils.popwindow;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.wevey.selector.dialog.DialogOnItemClickListener;
import com.wevey.selector.dialog.NormalSelectionDialog;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.FileProviderUtils.FileUtils;
import com.yidankeji.cheng.ebuyhouse.utils.JumpUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.google.GooglePlus;
import cn.sharesdk.twitter.Twitter;

/**
 * 选取图片的对话框
 */

public class ImageLogoUtils {

    private Activity context;
    private NormalSelectionDialog imageLogoDialog;
    private String TAG = "selectImage";

    public ImageLogoUtils(Activity context) {
        this.context = context;
    }

    /**
     * 相册相机对话框
     *
     * @param num 最大选取张数
     */
    public void getImageLogoDialog(final int num) {
        int choseNum = 8;
        if (num >= 8) {
            choseNum = 8;
        } else {
            choseNum = num;
        }
        final int finalChoseNum = choseNum;
        imageLogoDialog = new NormalSelectionDialog.Builder(context)
                .setlTitleVisible(true)
                .setTitleHeight(65)
                .setTitleText("please select").setTitleTextSize(16)
                .setTitleTextColor(R.color.zhutise)
                .setItemHeight(50)
                .setItemWidth(0.9f)
                .setItemTextColor(R.color.text_heise)
                .setItemTextSize(14)
                .setCancleButtonText("Cancle")
                .setCanceledOnTouchOutside(true)
                .setOnItemListener(new DialogOnItemClickListener() {
                    @Override
                    public void onItemClick(Button button, int position) {
                        getImage01(position, finalChoseNum);
                        imageLogoDialog.dismiss();
                    }
                })
                .build();
        ArrayList<String> itemList = new ArrayList<>();
        itemList.add("Camera");
        itemList.add("Album");
        imageLogoDialog.setDataList(itemList);
        imageLogoDialog.show();
    }

    public void getImageLogoDialog02(final int num) {
        imageLogoDialog = new NormalSelectionDialog.Builder(context)
                .setlTitleVisible(true)
                .setTitleHeight(65)
                .setTitleText("please select").setTitleTextSize(16)
                .setTitleTextColor(R.color.zhutise)
                .setItemHeight(50)
                .setItemWidth(0.9f)
                .setItemTextColor(R.color.text_heise)
                .setItemTextSize(14)
                .setCancleButtonText("Cancle")
                .setCanceledOnTouchOutside(true)
                .setOnItemListener(new DialogOnItemClickListener() {
                    @Override
                    public void onItemClick(Button button, int position) {
                        getImage(position, num);
                        imageLogoDialog.dismiss();
                    }
                })
                .build();
        ArrayList<String> itemList = new ArrayList<>();
        itemList.add("Camera");
        itemList.add("Album");
        imageLogoDialog.setDataList(itemList);
        imageLogoDialog.show();
    }

    private void getImage(int position, int num) {
        File picturefile = new File(FileUtils.picturePath);
        if (!picturefile.exists()) {
            picturefile.mkdirs();
        }
        File compressfile = new File(FileUtils.compressPath);
        if (!compressfile.exists()) {
            compressfile.mkdirs();
        }
        if (position == 0) {
            Log.i(TAG + "选择的相机还是相册", "相机");
            PictureSelector.create(context)
                    .openCamera(PictureMimeType.ofImage())
                    .maxSelectNum(num)
                    .setOutputCameraPath(FileUtils.picturePath)
                    .compress(true)
                    .compressSavePath(FileUtils.compressPath)
                    .cropCompressQuality(90)
                    .minimumCompressSize(100)
                    .forResult(PictureConfig.CHOOSE_REQUEST);
        } else if (position == 1) {
            Log.i(TAG + "选择的相机还是相册", "相册");
            PictureSelector.create(context)
                    .openGallery(PictureMimeType.ofImage())
                    .maxSelectNum(num)
                    .setOutputCameraPath(FileUtils.picturePath)
                    .compress(true)
                    .enableCrop(true)
                    .freeStyleCropEnabled(true)
                    .circleDimmedLayer(true)
                    .compressSavePath(FileUtils.compressPath)
                    .cropCompressQuality(90)
                    .minimumCompressSize(100)
                    .forResult(PictureConfig.CHOOSE_REQUEST);
        }
    }


    private void getImage01(int position, int num) {
        File picturefile = new File(FileUtils.picturePath);
        if (!picturefile.exists()) {
            picturefile.mkdirs();
        }
        File compressfile = new File(FileUtils.compressPath);
        if (!compressfile.exists()) {
            compressfile.mkdirs();
        }
        if (position == 0) {
            Log.i(TAG + "选择的相机还是相册", "相机");
            PictureSelector.create(context)
                    .openCamera(PictureMimeType.ofImage())
                    .maxSelectNum(num)
                    .setOutputCameraPath(FileUtils.picturePath)
                    .compress(true)
                    .compressSavePath(FileUtils.compressPath)
                    .cropCompressQuality(350)
                    .minimumCompressSize(100)
                    .forResult(PictureConfig.CHOOSE_REQUEST);
        } else if (position == 1) {
            Log.i(TAG + "选择的相机还是相册", "相册");
            PictureSelector.create(context)
                    .openGallery(PictureMimeType.ofImage())
                    .maxSelectNum(num)
                    .setOutputCameraPath(FileUtils.picturePath)
                    .compress(true)
                    .compressSavePath(FileUtils.compressPath)
                    .cropCompressQuality(350)
                    .minimumCompressSize(100)
                    .forResult(PictureConfig.CHOOSE_REQUEST);
        }
    }

    /**
     * 视频
     */
    public void getVideoLogoDialog(final String videopath) {
        imageLogoDialog = new NormalSelectionDialog.Builder(context)
                .setlTitleVisible(true)
                .setTitleHeight(65)
                .setTitleText("please select").setTitleTextSize(16)
                .setTitleTextColor(R.color.zhutise)
                .setItemHeight(50)
                .setItemWidth(0.9f)
                .setItemTextColor(R.color.text_heise)
                .setItemTextSize(14)
                .setCancleButtonText("Cancle")
                .setCanceledOnTouchOutside(true)
                .setOnItemListener(new DialogOnItemClickListener() {
                    @Override
                    public void onItemClick(Button button, int position) {
                        getVideo(position, videopath);
                        imageLogoDialog.dismiss();
                    }
                })
                .build();
        ArrayList<String> itemList = new ArrayList<>();
        itemList.add("Shoot or video");
        itemList.add("Preview video");
        imageLogoDialog.setDataList(itemList);
        imageLogoDialog.show();
    }

    private void getVideo(int position, final String video_path) {
        File videofile = new File(FileUtils.videoPath);
        if (!videofile.exists()) {
            videofile.mkdirs();
        }
        if (position == 0) {
            PictureSelector.create(context)
                    .openGallery(PictureMimeType.ofVideo())
                    .setOutputCameraPath(FileUtils.videoPath)
                    .maxSelectNum(1)
                    .previewVideo(true)
                    .enablePreviewAudio(true)
                    .videoQuality(8)
                    .videoMaxSecond(60)
                    .recordVideoSecond(60)
                    .forResult(PictureConfig.TYPE_VIDEO);
        } else {
            if (video_path == null || video_path.equals("")) {
                return;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JumpUtils.goToVideoPlayer(context, null, video_path);
//                        String a = HttpJoddHolder.get(video_path);
//                        Intent intent = new Intent(context, VideoActivity.class);
//                        intent.putExtra("videoUrl", a);
//                        context.startActivity(intent);
                    } catch (Exception e) {
                    }
                }
            }).start();

        }
    }

    /**
     * 视频
     */
    public void getToShare(final String id) {
        imageLogoDialog = new NormalSelectionDialog.Builder(context)
                .setlTitleVisible(true)
                .setTitleHeight(65)
                .setTitleText("please select").setTitleTextSize(16)
                .setTitleTextColor(R.color.zhutise)
                .setItemHeight(50)
                .setItemWidth(0.9f)
                .setItemTextColor(R.color.text_heise)
                .setItemTextSize(14)
                .setCancleButtonText("Cancle")
                .setCanceledOnTouchOutside(true)
                .setOnItemListener(new DialogOnItemClickListener() {
                    @Override
                    public void onItemClick(Button button, int position) {
                        imageLogoDialog.dismiss();
                        if (position == 0) {
                            share(id);
                        } else {
                            sharetoGoogle(id);
                        }
                    }
                })
                .build();
        ArrayList<String> itemList = new ArrayList<>();
        itemList.add("FaceBook");
        itemList.add("Google+");
        imageLogoDialog.setDataList(itemList);
        imageLogoDialog.show();
    }

    public void showShare(View view, final String id, final View layout) {

        layout.setVisibility(View.VISIBLE);
        // 用于PopupWindow的View
        View contentView = LayoutInflater.from(context).inflate(R.layout.share_popup, null, false);
        RelativeLayout facebook = contentView.findViewById(R.id.facebook);
        RelativeLayout google = contentView.findViewById(R.id.google);
        TextView cancel = contentView.findViewById(R.id.cancel);


        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        final PopupWindow window = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        // 设置PopupWindow的背景
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        window.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        window.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        window.showAsDropDown(view, 0, 0);
        // 或者也可以调用此方法显示PopupWindow，其中：
        // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
        // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
        // window.showAtLocation(parent, gravity, x, y);


        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                share(id);
                layout.setVisibility(View.GONE);
            }
        });
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.GONE);
                window.dismiss();
                sharetoGoogle(id);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.GONE);
                window.dismiss();

            }
        });
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layout.setVisibility(View.GONE);
            }
        });


    }

    // 分享facebook
    public void share(String prodetail_id) {


        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setUrl(Constant.share.replace("houseId", prodetail_id));
        sp.setShareType(Platform.SHARE_WEBPAGE);
        Platform fb = ShareSDK.getPlatform(Facebook.NAME);
        fb.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.e("share", "成功");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("share", "失败" + throwable.toString());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.e("share", "失败" + platform.toString());
            }
        });
        fb.share(sp);
    }




    // 分享facebook
    public void shareToTwitter(String prodetail_id) {


        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setUrl(Constant.share.replace("houseId", prodetail_id));
        sp.setShareType(Platform.SHARE_WEBPAGE);
        Platform fb = ShareSDK.getPlatform(Twitter.NAME);
        fb.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.e("share", "成功");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("share", "失败" + throwable.toString());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.e("share", "失败" + platform.toString());
            }
        });
        fb.share(sp);
    }

    //分享google
    public void sharetoGoogle(String prodetail_id) {


        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setUrl(Constant.share.replace("houseId", prodetail_id));
        sp.setText("Ebuyhouse");
        sp.setShareType(Platform.SHARE_WEBPAGE);
        Platform fb = ShareSDK.getPlatform(GooglePlus.NAME);
        fb.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.e("share", "成功");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("share", "失败" + throwable.toString());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.e("share", "失败" + platform.toString());
            }
        });
        fb.share(sp);
    }


    public void interestExit(View view, final OnClickListen onClickListen, final View layout) {
        mOnClickListen = onClickListen;
        layout.setVisibility(View.VISIBLE);
        // 用于PopupWindow的View
        View contentView = LayoutInflater.from(context).inflate(R.layout.popup_interest_exit, null, false);
        final TextView exit = contentView.findViewById(R.id.exit);
        final TextView cancel = contentView.findViewById(R.id.cancel);


        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        final PopupWindow window = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        // 设置PopupWindow的背景
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        window.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        window.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        window.showAsDropDown(view, 0, 0);
        // 或者也可以调用此方法显示PopupWindow，其中：
        // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
        // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
        // window.showAtLocation(parent, gravity, x, y);


        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                layout.setVisibility(View.GONE);
                if (mOnClickListen != null) {
                    mOnClickListen.OnPopupClickListen(exit,new Object());
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.GONE);
                window.dismiss();
                if (mOnClickListen != null) {
                    mOnClickListen.OnPopupClickListen(cancel,new Object());
                }

            }
        });
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layout.setVisibility(View.GONE);
            }
        });
    }

    OnClickListen mOnClickListen;

    public interface OnClickListen {
        void OnPopupClickListen(View view, Object object);
    }


}
