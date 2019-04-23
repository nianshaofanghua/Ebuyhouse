package com.yidankeji.cheng.ebuyhouse.application;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.mob.MobSDK;
import com.pgyersdk.crash.PgyCrashManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.community.chat.enity.CreateRoomModel;
import com.yidankeji.cheng.ebuyhouse.community.chat.util.DBApi;
import com.yidankeji.cheng.ebuyhouse.community.chat.util.SocketUtils;
import com.yidankeji.cheng.ebuyhouse.community.db.AlertModel;
import com.yidankeji.cheng.ebuyhouse.community.suger.SugarContext;
import com.yidankeji.cheng.ebuyhouse.mainmodule.activity.MainActivity;
import com.yidankeji.cheng.ebuyhouse.utils.FileProviderUtils.FileUtils;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;

import org.json.JSONObject;
import org.xutils.x;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.OkHttpClient;

/**
 * 全局
 */

public class MyApplication extends Application {

    private static MyOkHttp mMyOkhttp;//网络请求变量
    private static RequestOptions options_square, options_rectangles, options_touxiang;
    public static int Equipment_width, Equipment_height;//屏幕宽度变量
    public static String androidId;//设备编号
    static Context mContext;
    private HashMap<String, Activity> mHashMap;
    private String userId;

    /**
     * 屏幕宽度
     */
    public static int screenWidth;
    /**
     * 屏幕高度
     */
    public static int screenHeight;
    /**
     * 屏幕密度
     */
    public static float screenDensity;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("main","handleMessage");
            if (msg.what == 1) {
                userId = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), "userID", "");
                SocketUtils.mSocket.emit("current_user_id", userId);
              //  SocketUtils.mSocket.on("chat_record", chatRecord);
            } else {

            }

        }
    };


    public void sortAlertModel(AlertModel model) {
        List<AlertModel> list = DBApi.queryAllAlertModel();
        int temp = -1;
        int unreadnumber = 0;

        String targetid = "";
        String callBackId = "";
        if (model.getUser_id().equals(userId)) {
            callBackId = model.getTarget_id();
        } else {
            callBackId = model.getUser_id();
        }


        if (list.size() != 0) {

            for (int i = 0; i < list.size(); i++) {
                if (userId.equals(list.get(i).getUser_id())) {
                    targetid = list.get(i).getTarget_id();
                } else {
                    targetid = list.get(i).getUser_id();
                }
                if (model.getTarget_type() == 1 || model.getTarget_type() == 2) {
                    if (list.get(i).getTarget_type() == 1 || list.get(i).getTarget_type() == 2) {
                        if (targetid.equals(callBackId)) {
                            temp = i;
                            unreadnumber = list.get(i).getUnread_number();
                        }
                    }
                } else {
                    if (callBackId.equals(targetid)) {
                        temp = i;
                        unreadnumber = list.get(i).getUnread_number();
                    }
                }
            }

            if (temp != -1) {
                list.get(temp).delete();
            }
            list.clear();
            model.setUnread_number(model.getUnread_number() + unreadnumber);
            model.save();
        } else {
            model.save();


        }
    }

    static {
        initRefreshLayout();
        ClassicsHeader.REFRESH_HEADER_PULLDOWN = "Pull to refresh results";
        ClassicsHeader.REFRESH_HEADER_REFRESHING = "Refreshing results...";
        ClassicsHeader.REFRESH_HEADER_LOADING = "Loading...";
        ClassicsHeader.REFRESH_HEADER_RELEASE = "Release to refresh results";
        ClassicsHeader.REFRESH_HEADER_FINISH = "Results refreshed";
        ClassicsHeader.REFRESH_HEADER_FAILED = "Faild to refresh results";
        ClassicsHeader.REFRESH_HEADER_LASTTIME = " M-d HH:mm";

        ClassicsFooter.REFRESH_FOOTER_PULLUP = "Pull up more";
        ClassicsFooter.REFRESH_FOOTER_RELEASE = "Release immediately load";
        ClassicsFooter.REFRESH_FOOTER_REFRESHING = "It's refreshing...";
        ClassicsFooter.REFRESH_FOOTER_LOADING = "Loading...";
        ClassicsFooter.REFRESH_FOOTER_FINISH = "Load completion";
        ClassicsFooter.REFRESH_FOOTER_FAILED = "Load failure";
        ClassicsFooter.REFRESH_FOOTER_ALLLOADED = "Complete loading completion";

    }

    /**
     * 初始化当前设备屏幕宽高
     */
    private void initScreenSize() {
        DisplayMetrics curMetrics = getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = curMetrics.widthPixels;
        screenHeight = curMetrics.heightPixels;
        screenDensity = curMetrics.density;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());
        mHashMap = new HashMap<>();
        MobSDK.init(this);
        PgyCrashManager.register(this);
        initMyOkHttp();
        initRefreshLayout();
        initEquipmentParameter();
        initJiGuang();
        initGlide();
        initXUtils();
        initFile();
        initScreenSize();
        SugarContext.init(this);


        if (SharedPreferencesUtils.isLogin(this)) {
            userId = (String) SharedPreferencesUtils.getParam(this, "userID", "");
            SocketUtils.getSocket((String) SharedPreferencesUtils.getParam(this, "userID", ""));
            SocketUtils.mSocket.connect();
            SocketUtils.mSocket.on(Socket.EVENT_CONNECT, onConnect);

        }
        ;

    }

    private void initXUtils() {
        x.Ext.init(this);
        x.Ext.setDebug(false);
    }


    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... objects) {
            try {

                CreateRoomModel model = new CreateRoomModel();
                String json = new Gson().toJson(model);
                Message message = new Message();
                message.obj = json;
                handler.sendEmptyMessage(1);


            } catch (Exception e) {
                Log.e("EVENT_CONNECT", "连接成功" + e.toString());
            }
        }
    };


    private void initGlide() {
        options_touxiang = new RequestOptions();
        options_touxiang.error(R.mipmap.touxiang);
        options_touxiang.placeholder(R.mipmap.touxiang);
        options_square = new RequestOptions();
        options_square.error(R.mipmap.erro_iamge_fang);
        options_square.placeholder(R.mipmap.erro_iamge_fang);
        options_rectangles = new RequestOptions();
        options_rectangles.error(R.mipmap.erro_image_chang);
        options_rectangles.placeholder(R.mipmap.erro_image_chang);
    }

    //初始化极光
    private void initJiGuang() {
        JAnalyticsInterface.init(this);
        JAnalyticsInterface.setDebugMode(true);
        JAnalyticsInterface.initCrashHandler(this);
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);


//        builder.notificationDefaults = Notification.DEFAULT_SOUND
//                | Notification.DEFAULT_VIBRATE
//                | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
//        JPushInterface.setPushNotificationBuilder(1, builder);
    }


    public void initFile() {
        File picturefile = new File(FileUtils.picturePath);
        if (!picturefile.exists()) {
            picturefile.mkdirs();
        }
        File compressfile = new File(FileUtils.compressPath);
        if (!compressfile.exists()) {
            compressfile.mkdirs();
        }
        File videoFile = new File(FileUtils.videoPath);
        if (!videoFile.exists()) {
            videoFile.mkdirs();
        }
        File pdfFile = new File(FileUtils.pdfPath);
        if (!pdfFile.exists()) {
            pdfFile.mkdirs();
        }
    }

    //初始化设备参数
    private void initEquipmentParameter() {
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Equipment_width = wm.getDefaultDisplay().getWidth();
        Equipment_height = wm.getDefaultDisplay().getHeight();
        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static Context getContext() {
        return mContext;
    }

    //初始化刷新控件
    private static void initRefreshLayout() {

        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.text_hei, R.color.baise);
                layout.setDisableContentWhenRefresh(true);
                layout.setReboundDuration(200);

                return new ClassicsHeader(context);
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.text_hei, R.color.baise);
                layout.setDisableContentWhenLoading(true);
                layout.setReboundDuration(200);

                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    //初始化myOkHttp
    public void initMyOkHttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(50000L, TimeUnit.MILLISECONDS)
                .readTimeout(500000L, TimeUnit.MILLISECONDS)

                .build();

        mMyOkhttp = new NewOkHttp(okHttpClient);
    }
//    private static class MyDns implements Dns {
//
//        @Override
//        public List<InetAddress> lookup(String hostname) throws UnknownHostException {
//            List<String> strIps = HttpDns.getInstance().getIpByHost(hostname);
//            List<InetAddress> ipList;
//            if (strIps != null && strIps.size() > 0) {
//                ipList = new ArrayList<>();
//                for (String ip : strIps) {
//                    ipList.add(InetAddress.getByName(ip));
//                }
//            } else {
//                ipList = Dns.SYSTEM.lookup(hostname);
//            }
//            return ipList;
//        }
//    }


    public static MyOkHttp getmMyOkhttp() {
        return mMyOkhttp;
    }

    //获取正方形的Glide初始值
    public static RequestOptions getOptions_square() {
        return options_square;
    }

    //获取长方形的Glide初始值
    public static RequestOptions getOptions_rectangles() {
        return options_rectangles;
    }

    public static RequestOptions getOptions_touxiang() {
        return options_touxiang;
    }


    /**
     * 添加Activity
     */
    public void addActivity_(String name, Activity activity) {
// 判断当前集合中不存在该Activity
        Log.e("classsname", "" + name);
        mHashMap.put(name, activity);
    }

    /**
     * 销毁单个Activity
     */
    public void removeActivity_(String name) {
//判断当前集合中存在该Activity

        if (mHashMap.containsKey(name)) {
            mHashMap.get(name).finish();
        }
    }

    public void finishRemaveName(String name) {

        if (mHashMap.containsKey(name)) {
            mHashMap.remove("name");
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }

    private Emitter.Listener chatRecord = new Emitter.Listener() {
        @Override
        public void call(Object... objects) {

            JSONObject jsonObject = (JSONObject) objects[0];
            Log.e("chatssssRecord", "" + jsonObject.toString());

            boolean boo =   isForeground(getContext(), MainActivity.class.getName());
            if(!boo){
                AlertModel model = new Gson().fromJson(jsonObject.toString(), AlertModel.class);
                sortAlertModel(model);
            }else {
                Message message = new Message();
                message.obj = jsonObject.toString();
                handler.sendMessage(message);
            }



        }
    };
    /**
     * 判断某个Activity 界面是否在前台
     *
     * @param context
     * @param className 某个界面名称
     * @return
     */
    public boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }

        return false;

    }
}
