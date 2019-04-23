package com.yidankeji.cheng.ebuyhouse.utils;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.yidankeji.cheng.ebuyhouse.R;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by cheng on 2017\11\25 0025.
 */

public class WindowUtils {

    /**
     * 获取EditText文本
     */
    public static String getEditTextContent(EditText editText){
        String trim = editText.getText().toString().trim();
        return trim;
    }

    /**
     * 通过反射的方式获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return ((Activity)context).getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 点击edittext外部   消失
     * @param v
     * @param event
     * @return
     */
    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取屏幕宽度
     * @param context
     */
    public static int getWeight(Context context){
        Activity activity = (Activity)context;
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        return width;
    }

    // 谷歌地图 添加小水滴
    public static Bitmap createDrawableFromView(final Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
       final ImageView numBg = (ImageView) view.findViewById(R.id.num_bg);
        numBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(context,"喘吁吁程序瑕疵v");
                numBg.setImageResource(R.mipmap.marker_gray);
            }
        });

        view.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));


        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    /**
     * 动态设置gridview高度
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setGridViewHeightBasedOnChildren(GridView gridview , int a){
        ListAdapter listadapter = gridview.getAdapter();
        if (listadapter == null) {
            return;
        }
        int col = a;
        int totalHeight = 0;
        for (int i = 0; i < listadapter.getCount(); i+= col) {
            View listitem = listadapter.getView(i, null, gridview);
            listitem.measure(0, 0);
            totalHeight += listitem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = gridview.getLayoutParams();
        params.height = 50+totalHeight+ (gridview.getVerticalSpacing() * (gridview.getCount()/a));
        gridview.setLayoutParams(params);
    }

    /**
     * 动态设置listview高度
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight   + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 动态设置listview高度
     */
    public static int setListViewHeightBasedOnChildren01(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight   + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        Log.e("listview",""+totalHeight   + (listView.getDividerHeight() * (listAdapter.getCount() - 1)));
        return  params.height;
    }

    /**
     * 动态设置listview高度
     */
    public static int setGridViewHeightBasedOnChildren01(GridView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount()/6; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight   + (listView.getVerticalSpacing() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        Log.e("listview",""+totalHeight   + (listView.getVerticalSpacing() * (listAdapter.getCount() - 1)));
        return  params.height;
    }




    /**
     * 设置背景
     * @param activity
     * @param bgAlpha
     */
    public static void setBg(Activity activity , float bgAlpha){
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    public static void hideKeyBoard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 获取获得本地视频缩略图
     */
    public static Bitmap getVideoThumbnail(String videoPath) {
        MediaMetadataRetriever media =new MediaMetadataRetriever();
        media.setDataSource(videoPath);
        Bitmap bitmap = media.getFrameAtTime();
        return bitmap;
    }

    /**
     *  服务器返回url，通过url去获取视频的第一帧
     *  Android 原生给我们提供了一个MediaMetadataRetriever类
     *  提供了获取url视频第一帧的方法,返回Bitmap对象
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static Bitmap getNetVideoBitmap(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }
}
