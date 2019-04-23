package com.yidankeji.cheng.ebuyhouse.housemodule.httputils;

import android.app.Activity;
import android.util.Log;

import com.luck.picture.lib.entity.LocalMedia;
import com.tsy.sdk.myokhttp.response.DownloadResponseHandler;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.application.NewRawResponseHandler;
import com.yidankeji.cheng.ebuyhouse.utils.Constant;
import com.yidankeji.cheng.ebuyhouse.utils.CryptAES;
import com.yidankeji.cheng.ebuyhouse.utils.FileProviderUtils.FileUtils;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToCompressImageUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2018\1\4 0004.
 */

public class SubmitRoomHttpUtils {

    private Activity activity;
    private String TAG = "SubmitRoomAllType";
    private SubmitRoomImageListening myListening;
    private SubmitVoiceListening mVoiceListening;

    public SubmitRoomHttpUtils(Activity activity) {
        this.activity = activity;
    }

    /**
     * 上传图片
     *
     * @param selectList
     * @param listening
     */
    public void compressedImage(final List<LocalMedia> selectList, SubmitRoomImageListening listening) {
        myListening = listening;
        final ArrayList<String> imagepathList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < selectList.size(); i++) {
                    String compressPath = selectList.get(i).getCompressPath();
                    File file = new File(compressPath);
                    String substring = file.getName().substring(file.getName().lastIndexOf("."));
                    String filename = UUID.randomUUID().toString().trim().replaceAll("-", "");
                    String imagepath = ToCompressImageUtils.compress_Image(compressPath, FileUtils.compressPath + filename + substring, 100);
                    imagepathList.add(imagepath);
                }
                postImageToService(imagepathList);
            }
        }).start();
    }

    int bb = 0;

    private void postImageToService(final ArrayList<String> list) {
        bb = 0;

        String token = SharedPreferencesUtils.getToken(activity);
        for (int i = 0; i < list.size(); i++) {

            String mUrl = Constant.uhead + "house/image";

            MyApplication.getmMyOkhttp()
                    .upload().
                    url(Constant.uhead + "house/image").
                    addHeader("Authorization", "Bearer " + token)
                    .addFile("file", new File(list.get(i)))
                    .enqueue(new NewRawResponseHandler(activity) {
                        @Override
                        public void onSuccess(Object object, int statusCode, String response) {
                            //                    Log.i(TAG+"上传图片" , result);

                            myListening.getSubmitRoomImageListening("onSuccess", response);


                            int pos = bb + 1;

                            if (pos == list.size()) {
                                myListening.getSubmitRoomImageListening("onFinished", "");
                            }
                            bb++;
                        }

                        @Override
                        public void onFailure(Object object, int statusCode, String error_msg) {

                            myListening.getSubmitRoomImageListening("onError", "");
                            if ((bb + 1) == list.size()) {
                                myListening.getSubmitRoomImageListening("onFinished", "");
                            }
                        }
                    });


        }


    }


    /**
     * 上传视频
     */
    public void postVedioToService(String path, final SubmitRoomImageListening submitRoomImageListening) {
        String token = SharedPreferencesUtils.getToken(activity);
        String mUrl = Constant.uhead + "house/video?";
        MyApplication.getmMyOkhttp().upload()
                .url(Constant.uhead + "house/video?")
                .addHeader("Authorization", "Bearer " + token)
                .addFile("file", new File(path))
                .enqueue(new NewRawResponseHandler(activity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "：s", response);
                        Log.i("videoUrl", response + "");
                        submitRoomImageListening.getSubmitRoomImageListening("onSuccess", response);
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        super.onProgress(currentBytes, totalBytes);
                        int count = (int) ((currentBytes * 1.0 / totalBytes) * 100);
                        Log.i(TAG + "：f", currentBytes + "/" + totalBytes + "=" + count);
                        submitRoomImageListening.getSubmitRoomImageListening("onProgress", count + "");
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "：f", error_msg);
                        Log.i("videoUrl", error_msg + "");
                        submitRoomImageListening.getSubmitRoomImageListening("onSuccess", error_msg);
                    }
                });
    }


    public interface SubmitRoomImageListening {
        void getSubmitRoomImageListening(String state, String json);
    }

    /**
     * 获取验证码
     */
    public void getYZMcode(final CallBack.HttpUtilsListening listening) {
        String androidId = MyApplication.androidId;
        MyApplication.getmMyOkhttp().get()
                .url(Constant.getrandomcode + androidId + "/token?" + "jti=" + androidId)
                .enqueue(new NewRawResponseHandler(activity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        listening.getHttpUtilsListening(statusCode, response);
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        ToastUtils.showToast(activity, activity.getString(R.string.net_erro));
                    }
                });
    }

    public void getPhoneCodeHttp(String token, String phone, final CallBack.HttpUtilsListening listening) {
        String androidId = MyApplication.androidId;
        String aesToken2 = null;
        try {
            aesToken2 = CryptAES.aesEncryptString(token, "0ad0095f18b64004");
        } catch (Exception e) {
            e.printStackTrace();
        }
        MyApplication.getmMyOkhttp().post()
                .url(Constant.getrandomcode + androidId + "/send/edit")
                .addParam("aesToken", aesToken2)
                .addParam("phoneNumber", phone)
                .enqueue(new NewRawResponseHandler(activity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        listening.getHttpUtilsListening(statusCode, response);
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        ToastUtils.showToast(activity, activity.getString(R.string.net_erro));
                    }
                });
    }

    /**
     * 验证验证码
     */
    public void setVerificationCode(String phone, String code, final CallBack.HttpUtilsListening listening) {
        MyApplication.getmMyOkhttp().post()
                .url(Constant.yanzhengCode)
                .addParam("account", phone)
                .addParam("code", code)
                .addParam("model", "edit")
                .enqueue(new NewRawResponseHandler(activity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        listening.getHttpUtilsListening(statusCode, response);
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        ToastUtils.showToast(activity, activity.getString(R.string.net_erro));
                    }
                });
    }


    /**
     * 判断某参数是否在服务器重复
     */
    public void getCanShuDataHttp(String canshu, String values, final CallBack.HttpUtilsListening listening) {
        String token = SharedPreferencesUtils.getToken(activity);
        Log.i("fgh", Constant.isExist + "type=" + canshu + "&data=" + values);
        MyApplication.getmMyOkhttp().get()
                .url(Constant.isExist + "type=" + canshu + "&data=" + values)
                .addHeader("Authorization", "Bearer " + token)
                .enqueue(new NewRawResponseHandler(activity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        listening.getHttpUtilsListening(1, response);
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        listening.getHttpUtilsListening(2, error_msg);
                    }
                });
    }

    /**
     * 上传音频
     */
    public void postvoiceToService(String path, final long time, final SubmitVoiceListening submitVoiceListening) {
        String token = SharedPreferencesUtils.getToken(activity);
        mVoiceListening = submitVoiceListening;
        String mUrl = Constant.update_voice;
        MyApplication.getmMyOkhttp().upload()
                .url(Constant.update_voice)
                .addHeader("Authorization", "Bearer " + token)
                .addFile("file", new File(path))
                .enqueue(new NewRawResponseHandler(activity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "：s", response);
                        Log.e("videoUrl", response + "");
                        mVoiceListening.getSubmitRoomImageListening(time, response);
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        super.onProgress(currentBytes, totalBytes);

                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "：f", error_msg);
                        Log.i("videoUrl", error_msg + "");
                        mVoiceListening.getSubmitRoomImageListening(-1, error_msg);
                    }
                });
    }


    public void downFile(String url, final long time, SubmitVoiceListening voiceListening) {
        mVoiceListening = voiceListening;


        String[] temp = url.split("\\/");
        String name = temp[temp.length - 1];
        String token = SharedPreferencesUtils.getToken(activity);
        MyApplication.getmMyOkhttp().download()
                .addHeader("Authorization", "Bearer " + token)
                .url(url)
                .fileName(name)
                .filePath(FileUtils.videoPath + "/" + name)
                .enqueue(new DownloadResponseHandler() {
                    @Override
                    public void onFinish(File downloadFile) {
                        Log.e("files", "" + downloadFile.getPath());
                        mVoiceListening.getSubmitRoomImageListening(time, downloadFile.getPath());
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {

                    }

                    @Override
                    public void onFailure(String error_msg) {
                        Log.e("files", "" + error_msg);
                        mVoiceListening.getSubmitRoomImageListening(-1, error_msg);
                    }
                });
    }



    /**
     * 上传图片
     *
     * @param url
     * @param listening
     */
    public void chatToImage(final String url, final long time, SubmitVoiceListening listening) {
        mVoiceListening = listening;
        final ArrayList<String> imagepathList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {

                    String compressPath = url;
                    File file = new File(compressPath);
                    String substring = file.getName().substring(file.getName().lastIndexOf("."));
                    String filename = UUID.randomUUID().toString().trim().replaceAll("-", "");
                    String imagepath = ToCompressImageUtils.compress_Image(compressPath, FileUtils.compressPath + filename + substring, 100);
                    imagepathList.add(imagepath);

                postImageToChat(imagepathList,time);
            }
        }).start();
    }



    private void postImageToChat(final ArrayList<String> list, final long time) {
        bb = 0;

        String token = SharedPreferencesUtils.getToken(activity);
        for (int i = 0; i < list.size(); i++) {



            MyApplication.getmMyOkhttp()
                    .upload().
                    url(Constant.uhead + "house/image").
                    addHeader("Authorization", "Bearer " + token)
                    .addFile("file", new File(list.get(i)))
                    .enqueue(new NewRawResponseHandler(activity) {
                        @Override
                        public void onSuccess(Object object, int statusCode, String response) {
                            //                    Log.i(TAG+"上传图片" , result);

                            mVoiceListening.getSubmitRoomImageListening(time, response);


                            int pos = bb + 1;

                            if (pos == list.size()) {
                                mVoiceListening.getSubmitRoomImageListening(time, "");
                            }
                            bb++;
                        }
                        @Override
                        public void onFailure(Object object, int statusCode, String error_msg) {

                            mVoiceListening.getSubmitRoomImageListening(-1, "error");
                            if ((bb + 1) == list.size()) {
                                mVoiceListening.getSubmitRoomImageListening(-1, "error");
                            }
                        }
                    });


        }


    }





    public interface SubmitVoiceListening {
        void getSubmitRoomImageListening(long state, String json);
    }

    /**
     * 上传视频
     */
    public void postVedioToChat(String path, final long time, final SubmitVoiceListening submitRoomImageListening) {
        String token = SharedPreferencesUtils.getToken(activity);
        String mUrl = Constant.uhead + "house/video?";
        MyApplication.getmMyOkhttp().upload()
                .url(Constant.uhead + "house/video?")
                .addHeader("Authorization", "Bearer " + token)
                .addFile("file", new File(path))
                .enqueue(new NewRawResponseHandler(activity) {
                    @Override
                    public void onSuccess(Object object, int statusCode, String response) {
                        Log.i(TAG + "：s", response);
                        Log.i("videoUrl", response + "");
                        submitRoomImageListening.getSubmitRoomImageListening(time, response);
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        super.onProgress(currentBytes, totalBytes);
//                        int count = (int) ((currentBytes * 1.0 / totalBytes) * 100);
//                        Log.i(TAG + "：f", currentBytes + "/" + totalBytes + "=" + count);
//                        submitRoomImageListening.getSubmitRoomImageListening(time, count + "");
                    }

                    @Override
                    public void onFailure(Object object, int statusCode, String error_msg) {
                        Log.i(TAG + "：f", error_msg);
                        Log.i("videoUrl", error_msg + "");
                        submitRoomImageListening.getSubmitRoomImageListening(time, error_msg);
                    }
                });
    }
}
