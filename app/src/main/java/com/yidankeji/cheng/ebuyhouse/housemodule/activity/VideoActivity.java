package com.yidankeji.cheng.ebuyhouse.housemodule.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yidankeji.cheng.ebuyhouse.R;

//import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
//import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class
VideoActivity extends AppCompatActivity {

    private String videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        getSupportActionBar().setTitle("VideoPlayer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        videoUrl = getIntent().getStringExtra("videoUrl");
        if (videoUrl == null){
            finish();
        }
        initView();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void initView() {
//        JCVideoPlayerStandard video = (JCVideoPlayerStandard) findViewById(R.id.jc_video);
//        video.setUp(videoUrl, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");

    }

    @Override
    public void onBackPressed() {
//        if (JCVideoPlayer.backPress()) {
//            return;
//        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
   //     JCVideoPlayer.releaseAllVideos();
    }
}
