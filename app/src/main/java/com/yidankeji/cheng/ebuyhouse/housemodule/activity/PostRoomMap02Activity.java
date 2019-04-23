package com.yidankeji.cheng.ebuyhouse.housemodule.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.housemodule.mode.MyItem;
import com.yidankeji.cheng.ebuyhouse.housemodule.mode.MyItemReader;
import com.yidankeji.cheng.ebuyhouse.mainmodule.activity.BaseGoogleMapActivity;

import org.json.JSONException;

import java.io.InputStream;
import java.util.List;

public class PostRoomMap02Activity extends BaseGoogleMapActivity {

    private ClusterManager<MyItem> mClusterManager;

    @Override
    protected void startDemo() {
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        mClusterManager = new ClusterManager<MyItem>(this, getMap());
        getMap().setOnCameraIdleListener(mClusterManager);

        try {
            readItems();
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of markers.", Toast.LENGTH_LONG).show();
        }
    }

    private void readItems() throws JSONException {
        InputStream inputStream = getResources().openRawResource(R.raw.radar_search);
        List<MyItem> items = new MyItemReader().read(inputStream);
        mClusterManager.addItems(items);
    }
}
