package com.yidankeji.cheng.ebuyhouse.utils.PhotoView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.yidankeji.cheng.ebuyhouse.housemodule.activity.ProRentDetailActivity;

import java.util.ArrayList;

/**
 * Created by 刘灿成 on 2017\12\29 0029.
 */

public class PhotoViewUtils {

    public static void getPhotoView(Activity activity , ArrayList<String> list, int position){
        Bundle bundle = new Bundle();
        bundle.putSerializable("ImageList" , list);
        Intent intent = new Intent(activity , PhotoViewActivity.class);
        intent.putExtras(bundle);
        intent.putExtra("state" , position);
        activity.startActivity(intent);
    }
}
