package com.yidankeji.cheng.ebuyhouse.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.content.res.ResourcesCompat;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yidankeji.cheng.ebuyhouse.R;

import java.util.ArrayList;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

/**
 * Created by ${syj} on 2018/3/30.
 */

public class BaseViewUtil {


    private static volatile BaseViewUtil instance = null;

    public static BaseViewUtil getInstance() {
        synchronized (BaseViewUtil.class) {
            if (instance == null) {
                instance = new BaseViewUtil();
            }

        }
        return instance;
    }

    private BaseViewUtil() {

    }

    // 按鈕倒計時
    public void CountDown(TextView view, Activity activity, Object object) {
        MyTimeCount myTimeCount = new MyTimeCount(60000, 1000);//倒计时
        myTimeCount.setActivity(activity);
        myTimeCount.setTextView(view);

    }

    // 按钮倒計時类
    public class MyTimeCount extends CountDownTimer {

        private Activity activity;
        private TextView textView;

        public MyTimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            String show = "Resend after " + millisUntilFinished / 1000 + " seconds";
            textView.setText(show);
            textView.setClickable(false);
            textView.setTextColor(activity.getResources().getColor(R.color.baise));
            textView.setBackgroundResource(R.drawable.shape_bg_huiradio);
        }

        @Override
        public void onFinish() {
            textView.setText("Try Again");
            textView.setClickable(true);
            textView.setTextColor(activity.getResources().getColor(R.color.zhutise));
            textView.setBackgroundResource(R.drawable.shape_layout_zhutisebiankuang);
        }

        public void setActivity(Activity activity) {
            this.activity = activity;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }
    }

    //  跳转系统邮箱 发送邮件
    public static void sendEmail(Context context, String email) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        try {
            if (!(context instanceof Activity)) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "未安装邮箱应用！", Toast.LENGTH_SHORT).show();
        }
    }


    private void getP(final Activity activity, final String phonebunber) {
        ArrayList<PermissionItem> permissionItems = new ArrayList<PermissionItem>();
        permissionItems.add(new PermissionItem(Manifest.permission.CALL_PHONE, "CALL_PHONE", R.drawable.permission_ic_phone));
        HiPermission.create(activity)
                .title("Permission to apply for")
                .filterColor(ResourcesCompat.getColor(activity.getResources(), R.color.colorPrimary, activity.getTheme()))
                .permissions(permissionItems)
                .msg("To function properly, please agree to permissions!")
                .style(R.style.PermissionBlueStyle)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {

                    }

                    @Override
                    public void onFinish() {

                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phonebunber));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    }

                    @Override
                    public void onDeny(String permission, int position) {
                        ToastUtils.showToast(activity, "onDeny");
                    }

                    @Override
                    public void onGuarantee(String permission, int position) {
                        ToastUtils.showToast(activity, "onGuarantee");
                    }
                });
    }



    //  edittext抖动效果
    public class EditTextShakeHelper {

        private Animation shakeAnimation;

        private CycleInterpolator cycleInterpolator;

        private Vibrator shakeVibrator;

        public EditTextShakeHelper(Context context) {
            shakeVibrator = (Vibrator) context
                    .getSystemService(Service.VIBRATOR_SERVICE);
            shakeAnimation = new TranslateAnimation(0, 10, 0, 0);
            shakeAnimation.setDuration(300);
            cycleInterpolator = new CycleInterpolator(8);
            shakeAnimation.setInterpolator(cycleInterpolator);

        }

        public void shake(EditText... editTexts) {
            for (EditText editText : editTexts) {
                editText.startAnimation(shakeAnimation);
            }
            shakeVibrator.vibrate(new long[]{0, 500}, -1);
        }

    }
}
