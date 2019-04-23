package com.yidankeji.cheng.ebuyhouse.community.chat.ui.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.yidankeji.cheng.ebuyhouse.R;
import com.yidankeji.cheng.ebuyhouse.application.MyApplication;
import com.yidankeji.cheng.ebuyhouse.community.activity.InterestCircleMessageActivity;
import com.yidankeji.cheng.ebuyhouse.community.activity.OtherInformationActivity;
import com.yidankeji.cheng.ebuyhouse.community.activity.PersonalInformationActivity;
import com.yidankeji.cheng.ebuyhouse.community.activity.SocailDetailActivity;
import com.yidankeji.cheng.ebuyhouse.community.chat.adapter.ChatAdapter;
import com.yidankeji.cheng.ebuyhouse.community.chat.adapter.CommonFragmentPagerAdapter;
import com.yidankeji.cheng.ebuyhouse.community.chat.enity.CreateRoomModel;
import com.yidankeji.cheng.ebuyhouse.community.chat.enity.FullImageInfo;
import com.yidankeji.cheng.ebuyhouse.community.chat.ui.fragment.ChatEmotionFragment;
import com.yidankeji.cheng.ebuyhouse.community.chat.ui.fragment.ChatFunctionFragment;
import com.yidankeji.cheng.ebuyhouse.community.chat.util.Constants;
import com.yidankeji.cheng.ebuyhouse.community.chat.util.DBApi;
import com.yidankeji.cheng.ebuyhouse.community.chat.util.GlobalOnItemClickManagerUtils;
import com.yidankeji.cheng.ebuyhouse.community.chat.util.MediaManager;
import com.yidankeji.cheng.ebuyhouse.community.chat.util.SocketUtils;
import com.yidankeji.cheng.ebuyhouse.community.chat.util.Utils;
import com.yidankeji.cheng.ebuyhouse.community.chat.widget.EmotionInputDetector;
import com.yidankeji.cheng.ebuyhouse.community.chat.widget.NoScrollViewPager;
import com.yidankeji.cheng.ebuyhouse.community.chat.widget.StateButton;
import com.yidankeji.cheng.ebuyhouse.community.db.MessageInfo;
import com.yidankeji.cheng.ebuyhouse.community.mode.ChatCallBackModel;
import com.yidankeji.cheng.ebuyhouse.community.mode.UpdateVideoModel;
import com.yidankeji.cheng.ebuyhouse.community.mode.UpdateVoiceModel;
import com.yidankeji.cheng.ebuyhouse.housemodule.httputils.SubmitRoomHttpUtils;
import com.yidankeji.cheng.ebuyhouse.utils.BaseActivity;
import com.yidankeji.cheng.ebuyhouse.utils.FileProviderUtils.FileUtils;
import com.yidankeji.cheng.ebuyhouse.utils.JumpUtils;
import com.yidankeji.cheng.ebuyhouse.utils.SharedPreferencesUtils;
import com.yidankeji.cheng.ebuyhouse.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.rockerhieu.emojicon.EmojiconEditText;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * 作者：Rance on 2016/11/29 10:47
 * 邮箱：rance935@163.com
 */
public class ChatActivity extends BaseActivity implements SubmitRoomHttpUtils.SubmitVoiceListening, View.OnClickListener {


    private EasyRecyclerView chatList;

    ImageView emotionVoice;


    TextView voiceText;

    ImageView emotionButton;

    ImageView emotionAdd;

    StateButton emotionSend;

    NoScrollViewPager viewpager;

    EmojiconEditText mEditText;
    RelativeLayout emotionLayout;
    private EmotionInputDetector mDetector;
    private ArrayList<Fragment> fragments;
    private ChatEmotionFragment chatEmotionFragment;
    private ChatFunctionFragment chatFunctionFragment;
    private CommonFragmentPagerAdapter adapter;
    private ChatAdapter chatAdapter;
    private LinearLayoutManager layoutManager;
    private List<MessageInfo> messageInfos;
    //录音相关
    int animationRes = 0;
    int res = 0;
    AnimationDrawable animationDrawable = null;
    private ImageView animView;
    Activity mActivity;
    private Socket socket;
    private String roomId;
    private String roomjson;
    private SubmitRoomHttpUtils roomHttpUtils;
    private String friendId;
    private ImageView iv_back, iv_right;
    private TextView tv_title;
    private int chatType;
    private String community_id;
    private String userHead;
    private List<MessageInfo> isExistList = new ArrayList<>();
    private String userId;
    private MessageInfo tempMessageInfo;
    private String tempString;
    private List<MessageInfo> tempList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mActivity = this;
        initView();
        EventBus.getDefault().register(this);
        initWidget();
        socket();
    }

    // 初始化界面控件
    public void initView() {
        iv_back = (ImageView) findViewById(R.id.actionbar_back);
        iv_right = (ImageView) findViewById(R.id.iv_actionbar_right);
        tv_title = (TextView) findViewById(R.id.actionbar_title);
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_title.setOnClickListener(this);

        chatList = (EasyRecyclerView) findViewById(R.id.chat_list);
        emotionVoice = (ImageView) findViewById(R.id.emotion_voice);
        emotionAdd = (ImageView) findViewById(R.id.emotion_add);
        voiceText = (TextView) findViewById(R.id.voice_text);
        emotionButton = (ImageView) findViewById(R.id.emotion_button);
        emotionSend = (StateButton) findViewById(R.id.emotion_send);
        viewpager = (NoScrollViewPager) findViewById(R.id.viewpager);
        emotionLayout = (RelativeLayout) findViewById(R.id.emotion_layout);
        mEditText = (EmojiconEditText) findViewById(R.id.edit_text);
        Intent intent = getIntent();
        roomId = intent.getStringExtra("roomid");
        friendId = intent.getStringExtra("id");
        tv_title.setText(intent.getStringExtra("name"));
        chatType = intent.getIntExtra("type", -1);
        community_id = intent.getStringExtra("socialid");
        if (chatType != 3) {
            iv_right.setImageResource(R.mipmap.peoples_icon);
        } else {
            iv_right.setImageResource(R.mipmap.one_people);
        }

        userHead = (String) SharedPreferencesUtils.getParam(ChatActivity.this, "myinfo_youxiang", "");
        roomHttpUtils = new SubmitRoomHttpUtils(ChatActivity.this);
    }


    // 初始化聊天界面各个控件
    private void initWidget() {
        fragments = new ArrayList<>();
        chatEmotionFragment = new ChatEmotionFragment();
        fragments.add(chatEmotionFragment);
        chatFunctionFragment = new ChatFunctionFragment();
        fragments.add(chatFunctionFragment);
        adapter = new CommonFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);
        emotionLayout.setVisibility(View.GONE);
        mDetector = EmotionInputDetector.with(this)
                .setEmotionView(emotionLayout)
                .setViewPager(viewpager)
                .bindToContent(chatList)
                .bindToEditText(mEditText)
                .bindToEmotionButton(emotionButton)
                .bindToAddButton(emotionAdd)
                .bindToSendButton(emotionSend)
                .bindToVoiceButton(emotionVoice)
                .bindToVoiceText(voiceText)
                .build();

        GlobalOnItemClickManagerUtils globalOnItemClickListener = GlobalOnItemClickManagerUtils.getInstance(this);
        globalOnItemClickListener.attachToEditText(mEditText);
        int screenWidth = MyApplication.screenWidth;
        int spacing = Utils.dp2px(this, 12);
        int itemWidth = (screenWidth - spacing * 8) / 7;

        mEditText.setEmojiconSize(itemWidth / 4 * 3);
        chatAdapter = new ChatAdapter(this);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chatList.setLayoutManager(layoutManager);
        chatList.setAdapter(chatAdapter);
        chatList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        chatAdapter.handler.removeCallbacksAndMessages(null);
                        chatAdapter.notifyDataSetChanged();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        chatAdapter.handler.removeCallbacksAndMessages(null);
                        mDetector.hideEmotionLayout(false);
                        mDetector.hideSoftInput();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        chatAdapter.addItemClickListener(itemClickListener);
        LoadData();
    }

    /**
     * item点击事件
     */
    private ChatAdapter.onItemClickListener itemClickListener = new ChatAdapter.onItemClickListener() {

        /// 头像监听
        @Override
        public void onHeaderClick(int position) {
            Intent intent;
            if (messageInfos.get(position).getType() == Constants.CHAT_ITEM_TYPE_RIGHT) {
                intent = new Intent(ChatActivity.this, PersonalInformationActivity.class);
                startActivity(intent);
            } else {
                intent = new Intent(ChatActivity.this, OtherInformationActivity.class);
                intent.putExtra("id", messageInfos.get(position).getUser_id());
                if (messageInfos.get(position).getTarget_type() == 3) {
                    intent.putExtra("source", "Friend");
                    intent.putExtra("isFriend", 0);
                    intent.putExtra("sourceid", messageInfos.get(position).getTarget_id());
                } else if (messageInfos.get(position).getTarget_type() == 2) {
                    intent.putExtra("source", "Interest");
                    intent.putExtra("isFriend", 0);
                    intent.putExtra("sourceid", messageInfos.get(position).getTarget_id());
                } else if (messageInfos.get(position).getTarget_type() == 1) {
                    intent.putExtra("source", "Community");
                    intent.putExtra("isFriend", 0);
                    intent.putExtra("sourceid", messageInfos.get(position).getTarget_id());
                }
                startActivity(intent);
            }
        }

        //  adapter 图片监听
        @Override
        public void onImageClick(View view, int position) {
            if (messageInfos.get(position).getContentType() == 3) {
                int location[] = new int[2];
                view.getLocationOnScreen(location);
                FullImageInfo fullImageInfo = new FullImageInfo();
                fullImageInfo.setLocationX(location[0]);
                fullImageInfo.setLocationY(location[1]);
                fullImageInfo.setWidth(view.getWidth());
                fullImageInfo.setHeight(view.getHeight());
                fullImageInfo.setImageUrl(messageInfos.get(position).getImageUrl());
                EventBus.getDefault().postSticky(fullImageInfo);
                startActivity(new Intent(mActivity, FullImageActivity.class));
                overridePendingTransition(0, 0);
            } else {
                JumpUtils.goToVideoPlayer(ChatActivity.this, view, messageInfos.get(position).getContent());
            }

        }


        //  音频监听
        @Override
        public void onVoiceClick(final ImageView imageView, final int position) {


            if (messageInfos.get(position).getFilepath() == null) {
                return;
            }
            if (animView != null) {
                animView.setImageResource(res);
                animView = null;
            }
            switch (messageInfos.get(position).getType()) {
                case 1:
                    animationRes = R.drawable.voice_left;
                    res = R.mipmap.icon_voice_left3;
                    break;
                case 2:
                    animationRes = R.drawable.voice_right;
                    res = R.mipmap.icon_voice_right3;
                    break;
                default:
                    break;
            }
            animView = imageView;
            animView.setImageResource(animationRes);
            animationDrawable = (AnimationDrawable) imageView.getDrawable();
            animationDrawable.start();
            MediaManager.playSound(messageInfos.get(position).getFilepath(), new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    animView.setImageResource(res);
                }
            });
        }

        @Override
        public void onMessageDeal(int x, int y, int position, View view) {
            initPopup(x, y, view, messageInfos.get(position));
        }
    };

    /**
     * 构造聊天数据
     */
    private void LoadData() {
        userId = (String) SharedPreferencesUtils.getParam(ChatActivity.this, "userID", "");
        messageInfos = new ArrayList<>();
        messageInfos.addAll(DBApi.queryAllMessageInfo(roomId, chatType));
        chatAdapter.addAll(messageInfos);
        chatList.scrollToPosition(chatAdapter.getCount() - 1);
    }


    // eventbus在EmotionInputDetector 发送事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEventBus(final MessageInfo messageInfo) {
        messageInfo.setHeader(userHead);
        messageInfo.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
        messageInfo.setSendState(Constants.CHAT_ITEM_SENDING);
        messageInfo.setTarget_type(chatType);
        messageInfo.setShouldShowTime(1);
        messageInfo.setHouseID(roomId);
        messageInfo.setIsMine(0);
        messageInfo.setMessage_type(1);
        messageInfo.setUser_id(userId);
        messageInfo.setTarget_id(friendId);
        messageInfo.setMyId(userId);
        messageInfo.setSend_time(System.currentTimeMillis() / 1000);
        messageInfo.setVoiceLength(0);
        messageInfo.setFriendid(friendId);
        messageInfo.setReceive_time(0);

        switch (messageInfo.getContentType()) {
            case 1:
// 发送文字
                String json = new Gson().toJson(messageInfo);
                SocketUtils.mSocket.emit("chatevent", json);
                break;
            case 2:
//发送音频到服务器
                messageInfo.setVoiceLength(((int) messageInfo.getVoiceTime() / 1000));
                roomHttpUtils.postvoiceToService(messageInfo.getFilepath(), messageInfo.getSend_time(), new SubmitRoomHttpUtils.SubmitVoiceListening() {
                    @Override
                    public void getSubmitRoomImageListening(long state, String json) {
                        if (state != -1) {
                            UpdateVoiceModel model = new Gson().fromJson(json, UpdateVoiceModel.class);
                            if (model.getState() == 1) {
                                messageInfo.setContent(model.getContent().getData().getImg_url());
                                String voicejson = new Gson().toJson(messageInfo);
                                Log.e("voice", "" + json);
                                SocketUtils.mSocket.emit("chatevent", voicejson);
                            }
                        }

                    }
                });

                break;
            case 3:

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;//这个参数设置为true才有效，
                Bitmap bmp = BitmapFactory.decodeFile(messageInfo.getImageUrl(), options);//这里的bitmap是个空
                if (bmp == null) {

                }
                int outHeight = options.outHeight;
                int outWidth = options.outWidth;

                getImageHightWitch(outWidth, outHeight, messageInfo);
                // 发送图片到服务器
                roomHttpUtils.chatToImage(messageInfo.getImageUrl(), messageInfo.getSend_time(), new SubmitRoomHttpUtils.SubmitVoiceListening() {
                    @Override
                    public void getSubmitRoomImageListening(long state, String json) {
                        Log.e("json", "" + json);
                        if (!json.equals("error")) {
                            UpdateVoiceModel model = new Gson().fromJson(json, UpdateVoiceModel.class);
                            if (model.getState() == 1) {
                                messageInfo.setContent(model.getContent().getData().getImg_url());
                                String voicejson = new Gson().toJson(messageInfo);
                                SocketUtils.mSocket.emit("chatevent", voicejson);
                            }
                        } else {
                            ToastUtils.showToast(ChatActivity.this, "send error");
                        }

                    }
                });

                break;
            case 4:
                // 发送视频到服务器
                roomHttpUtils.postVedioToChat(messageInfo.getImageUrl(), messageInfo.getSend_time(), new SubmitRoomHttpUtils.SubmitVoiceListening() {
                    @Override
                    public void getSubmitRoomImageListening(long state, String json) {
                        Log.e("json", "" + json);
                        if (!json.equals("error")) {
                            UpdateVideoModel model = new Gson().fromJson(json, UpdateVideoModel.class);
                            if (model.getState() == 1) {
                                messageInfo.setContent(model.getContent().getData().getImg_url());
                                String voicejson = new Gson().toJson(messageInfo);
                                SocketUtils.mSocket.emit("chatevent", voicejson);
                            }
                        }

                    }
                });
                break;
            default:
                break;
        }


        messageInfos.add(messageInfo);
        chatAdapter.add(messageInfo);
        chatList.scrollToPosition(chatAdapter.getCount() - 1);

    }

    @Override
    public void onBackPressed() {
        if (!mDetector.interceptBackPress()) {
            super.onBackPressed();
        }
    }

    //  activity销毁 下线处理
    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().removeStickyEvent(this);
        EventBus.getDefault().unregister(this);
        SocketUtils.mSocket.emit("leave_room", roomjson);
        SocketUtils.mSocket.off(Socket.EVENT_CONNECT_ERROR);
        SocketUtils.mSocket.off(Socket.EVENT_CONNECT_TIMEOUT);
        SocketUtils.mSocket.off(Socket.EVENT_CONNECT);
        SocketUtils.mSocket.off("onMessage");

    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.e("onPause", "锁屏进入桌面");
        SocketUtils.mSocket.emit("leave_room", roomjson);
        SocketUtils.mSocket.emit("off_line", userId);


    }

    //onResume 灭屏 进入后台监听 重新往服务器发送上线提醒
    @Override
    protected void onResume() {
        super.onResume();

        if (SocketUtils.mSocket.connected()) {
            if (userId == null) {
                userId = (String) SharedPreferencesUtils.getParam(ChatActivity.this, "userID", "");
                SocketUtils.mSocket.emit("current_user_id", userId);
            } else {
                SocketUtils.mSocket.emit("current_user_id", userId);
            }
            if (roomjson != null) {

                SocketUtils.mSocket.emit("create_room", roomjson);

            }
        } else {
            SocketUtils.mSocket.on(Socket.EVENT_CONNECT, onConnect);
        }


    }

    // socket初始化
    public void socket() {
        CreateRoomModel model = new CreateRoomModel();
        model.setRoom_id(roomId);
        model.setUser_id((String) SharedPreferencesUtils.getParam(this, "userID", ""));
        roomjson = new Gson().toJson(model);

        SocketUtils.mSocket.emit("create_room", roomjson);

        SocketUtils.mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        SocketUtils.mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, ontimeOut);


        SocketUtils.mSocket.on("onTalk", onMeesage);
        SocketUtils.mSocket.on("callBack", callBack);
    }

    // 连接成功监听
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... objects) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (userId == null) {
                        userId = (String) SharedPreferencesUtils.getParam(ChatActivity.this, "userID", "");
                        SocketUtils.mSocket.emit("current_user_id", userId);
                    } else {
                        SocketUtils.mSocket.emit("current_user_id", userId);
                    }
                    if (roomjson != null) {

                        SocketUtils.mSocket.emit("create_room", roomjson);

                    }
                }
            });

        }
    };


    //  连接出错监听
    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... objects) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showToast(ChatActivity.this, "link error");
                }
            });


        }
    };
    // 连接时间超时监听
    private Emitter.Listener ontimeOut = new Emitter.Listener() {
        @Override
        public void call(Object... objects) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showToast(ChatActivity.this, "Connection timeout");
                }
            });


        }
    };

    //  监听从服务器发来的消息
    private Emitter.Listener onMeesage = new Emitter.Listener() {
        @Override
        public void call(Object... objects) {
            JSONObject json = (JSONObject) objects[0];
            Log.e("onTalk", "" + json.toString());
            tempList.clear();
            tempList.addAll(messageInfos);

            onTalk(json);
        }
    };
    //  是否发送成功监听
    private Emitter.Listener callBack = new Emitter.Listener() {
        @Override
        public void call(final Object... objects) {


            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject json = (JSONObject) objects[0];
                    ChatCallBackModel model = new Gson().fromJson(json.toString(), ChatCallBackModel.class);
                    Log.e("callback", json.toString());
                    for (int i = 0; i < messageInfos.size(); i++) {
                        if (model.getSend_time() == messageInfos.get(i).getSend_time()) {
                            messageInfos.get(i).setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
                            sqlSave(messageInfos.get(i));
                            chatAdapter.notifyDataSetChanged();

                        }
                    }
                }
            });
        }
    };

    @Override
    public void getSubmitRoomImageListening(long state, String json) {
        Log.e("voice", "" + json);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//  得到图片和视频
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == -1) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setContentType(3);
            messageInfo.setImageUrl(selectList.get(0).getCompressPath());
            EventBus.getDefault().post(messageInfo);
        } else if (requestCode == PictureConfig.TYPE_VIDEO) {
            List<LocalMedia> videoList = PictureSelector.obtainMultipleResult(data);
            if (videoList.size() != 0) {
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(videoList.get(0).getPath());
                String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                if (Integer.valueOf(duration) / 1000 > 30) {
                    Toast.makeText(ChatActivity.this, "Video time is too long", Toast.LENGTH_SHORT).show();
                    return;
                }
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setContentType(4);
                messageInfo.setImageUrl(videoList.get(0).getPath());
                getLocalVideoThumbnail(messageInfo, videoList.get(0).getPath());
                EventBus.getDefault().post(messageInfo);
            }

        }

    }

    //  从网络地址中获取视频第一帧图片
    public void getNetVideoBitmap(MessageInfo messageInfo, String videoUrl) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据url获取缩略图
            retriever.setDataSource(videoUrl, new HashMap());
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        saveFile(messageInfo, bitmap, System.currentTimeMillis() + ".jpg");
    }

    //  从本地地址中获取视频第一帧图片
    public void getLocalVideoThumbnail(MessageInfo messageInfo, String filePath) {
        Bitmap bitmap = null;
        //MediaMetadataRetriever 是android中定义好的一个类，提供了统一
        //的接口，用于从输入的媒体文件中取得帧和元数据；
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据文件路径获取缩略图
            retriever.setDataSource(filePath);
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();


        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        saveFile(messageInfo, bitmap, System.currentTimeMillis() + ".jpg");
    }

    //  把获取的网络第一帧图片转为file
    public void saveFile(final MessageInfo messageInfo, Bitmap bm, String fileName) {
        try {
            File dirFile = new File(FileUtils.picturePath);
            if (!dirFile.exists()) {
                dirFile.mkdir();
            }
            File myCaptureFile = new File(FileUtils.picturePath + "/" + fileName);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
            messageInfo.setVideo_img(myCaptureFile.getPath());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//这个参数设置为true才有效，
            Bitmap bmp = BitmapFactory.decodeFile(messageInfo.getVideo_img(), options);//这里的bitmap是个空
            if (bmp == null) {

            }
            int outHeight = options.outHeight;
            int outWidth = options.outWidth;

            getImageHightWitch(outWidth, outHeight, messageInfo);

            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (messageInfo.getType() == 1) {
                        for (int i = 0; i < messageInfos.size(); i++) {
                            if (messageInfo.getSend_time() == messageInfos.get(i).getSend_time()) {
                                messageInfos.get(i).setVideo_img(messageInfo.getVideo_img());
                                messageInfos.get(i).setContentWidth(messageInfo.getContentWidth());
                                messageInfos.get(i).setContentHeight(messageInfo.getContentHeight());
                                sqlSave(messageInfos.get(i));
                                chatAdapter.notifyDataSetChanged();

                            }
                        }


                    }

                }
            });
        } catch (Exception e) {

        }

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.actionbar_back:
                finish();
                break;
            case R.id.iv_actionbar_right:
                //  右侧根据聊天类型跳转跳转
                switch (chatType) {
                    case 1:
                        intent = new Intent(ChatActivity.this, SocailDetailActivity.class);
                        intent.putExtra("id", friendId);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(ChatActivity.this, InterestCircleMessageActivity.class);
                        intent.putExtra("id", friendId);
                        intent.putExtra("socialid", community_id);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(ChatActivity.this, OtherInformationActivity.class);
                        intent.putExtra("id", friendId);
                        intent.putExtra("isFriend", 1);
                        intent.putExtra("sourceid", friendId);
                        intent.putExtra("source", "friend");
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    // 解析json 存为messageinfo
    public MessageInfo getJson(JSONObject jsonObject) {
        MessageInfo messageInfo = new MessageInfo();
        try {
            messageInfo.setVoiceLength(jsonObject.optInt("voiceLength"));
            messageInfo.setMessage_type(jsonObject.optInt("message_type"));
            messageInfo.setSend_time(jsonObject.optLong("receive_time"));
            messageInfo.setContentHeight(jsonObject.optInt("contentHeight"));
            messageInfo.setContentType(jsonObject.optInt("contentType"));
            messageInfo.setHouseID(jsonObject.optString("houseID"));
            messageInfo.setReceive_time(jsonObject.optLong("receive_time"));
            messageInfo.setUser_id(jsonObject.optString("user_id"));
            messageInfo.setContentWidth(jsonObject.optInt("contentWidth"));
            messageInfo.setTarget_id(jsonObject.optString("target_id"));
            messageInfo.setTarget_type(jsonObject.optInt("target_type"));
            messageInfo.setShouldShowTime(jsonObject.optInt("shouldShowTime"));
            messageInfo.setIsMine(jsonObject.optInt("isMine"));
            messageInfo.setSendState(jsonObject.optInt("sendState"));
            messageInfo.setContent(jsonObject.optString("content"));
            messageInfo.setShow_name(jsonObject.optString("show_name"));
            messageInfo.setShow_head(jsonObject.optString("show_head"));
            messageInfo.setInter_name(jsonObject.optString("inter_name"));
            messageInfo.setUser_head(jsonObject.optString("user_head"));
            messageInfo.setUnread_number(jsonObject.optInt("unread_number"));
            messageInfo.setFriendid(friendId);
            if (messageInfo.getTarget_type() == 1 || messageInfo.getTarget_type() == 2) {
                messageInfo.setMyId(userId);
            }
        } catch (Exception e) {

        }

        return messageInfo;
    }


    // 通过图片本身大小计算出合适的高度
    public void getImageHightWitch(double width, double height, MessageInfo messageInfo) {
        Log.e("计算传入大小", width + "---" + height);
        double widHgtScale = 0.0;
        widHgtScale = width / height;
        //图片高宽比
        double hgtWidScale = 0;
        hgtWidScale = height / width;

        //高大于宽
        if (widHgtScale > 0 && widHgtScale < 1) {

            //极窄极高 (展示固定50宽,不能再窄)
            if (105 * widHgtScale <= 50) {
                Log.e("计算传入大小", width + "---" + height);
                messageInfo.setContentWidth(50);
                messageInfo.setContentHeight(130);
            } else {
                messageInfo.setContentWidth((int) (130 * widHgtScale));
                messageInfo.setContentHeight(130);
            }

            //宽大于高
        } else if (widHgtScale > 1) {

            //极宽极低(展示固定高度50,不能更低)
            if (100 * (hgtWidScale) <= 50) {

                messageInfo.setContentWidth((int) (50 * widHgtScale));
                messageInfo.setContentHeight(50);
            } else {

                messageInfo.setContentWidth(135);
                messageInfo.setContentHeight((int) (135 * hgtWidScale));
            }
            //宽高相等
        } else {
            messageInfo.setContentWidth(120);
            messageInfo.setContentHeight(120);

        }

    }


    //  保存消息并去重 多次判断去重 去重！！！
    public synchronized void onTalk(JSONObject json) {
        boolean isOk = false;
        if (tempString == null) {
            tempString = json.toString();
        } else {
            if (tempString.equals(json.toString())) {
                isOk = true;
            } else {
                tempString = json.toString();
            }
        }

        final MessageInfo messageInfo = getJson(json);


        List<MessageInfo> list = DBApi.queryAll();
        for (int i = 0; i < list.size(); i++) {
            if (messageInfo.getSend_time() == list.get(i).getSend_time()) {
                isOk = true;
            }
        }
        for (int i = 0; i < messageInfos.size(); i++) {
            if (messageInfo.getSend_time() == messageInfos.get(i).getSend_time()) {
                isOk = true;
            }
        }
        List<MessageInfo> list01 = DBApi.queryAll();
        for (int i = 0; i < list01.size(); i++) {
            if (messageInfo.getSend_time() == list01.get(i).getSend_time()) {
                isOk = true;
            }
        }
        if (!isOk) {
            ChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    boolean isExist = true;
                    if (isExist) {
                        if (!messageInfo.getUser_id().equals(SharedPreferencesUtils.getParam(ChatActivity.this, "userID", ""))) {
                            messageInfo.setType(Constants.CHAT_ITEM_TYPE_LEFT);
                            messageInfo.setHeader("http://img4.duitang.com/uploads/item/201504/10/20150410H1256_QAULP.jpeg");
                            switch (messageInfo.getContentType()) {
                                case 1:
                                    messageInfo.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
                                    break;
                                case 2:
                                    messageInfo.setSendState(Constants.CHAT_ITEM_SENDING);
                                    roomHttpUtils.downFile(messageInfo.getContent(), messageInfo.getSend_time(), new SubmitRoomHttpUtils.SubmitVoiceListening() {
                                        @Override
                                        public void getSubmitRoomImageListening(long state, final String json) {
                                            messageInfo.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
                                            messageInfo.setFilepath(json);
                                            ChatActivity.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    for (int i = 0; i < messageInfos.size(); i++) {
                                                        if (messageInfo.getSend_time() == messageInfos.get(i).getSend_time()) {
                                                            if (!DBApi.isExist(messageInfo.getSend_time())) {
                                                                messageInfos.get(i).setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
                                                                messageInfos.get(i).setFilepath(json);
                                                                sqlSave(messageInfos.get(i));
                                                                chatAdapter.notifyDataSetChanged();
                                                                chatList.scrollToPosition(chatAdapter.getCount() - 1);
                                                            }
                                                        }
                                                    }
                                                }
                                            });

                                        }
                                    });
                                    break;
                                case 3:
                                    messageInfo.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
                                    break;
                                case 4:
                                    messageInfo.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
                                    getNetVideoBitmap(messageInfo, messageInfo.getContent());
                                    break;
                                default:
                                    break;
                            }

                            messageInfos.add(messageInfo);
                            for (int i = 0; i < messageInfos.size(); i++) {
                                if (messageInfo.getSend_time() == messageInfos.get(i).getSend_time()) {
                                    if (messageInfos.get(i).getTarget_type() == 3) {
                                        messageInfos.get(i).setUser_id(messageInfos.get(i).getTarget_id());
                                        messageInfos.get(i).setTarget_id(friendId);
                                    }
                                    if (messageInfo.getContentType() != 2) {
                                        sqlSave(messageInfos.get(i));
                                        chatAdapter.clear();
                                        chatAdapter.addAll(messageInfos);
                                        chatAdapter.notifyDataSetChanged();
                                        chatList.scrollToPosition(chatAdapter.getCount() - 1);
                                    }

                                }
                            }


                        }
                    }


                }
            });
        }


    }

    //  保存消息 并去重
    public void sqlSave(MessageInfo messageInfo) {
        boolean isExist = false;
        for (int i = 0; i < tempList.size(); i++) {
            if (messageInfo.getSend_time() == tempList.get(i).getSend_time()) {
                isExist = true;
            }
        }

        List<MessageInfo> list = DBApi.queryAll();
        for (int i = 0; i < list.size(); i++) {
            if (messageInfo.getSend_time() == list.get(i).getSend_time()) {
                isExist = true;
            }
        }
        if (!isExist) {
            messageInfo.save();
            chatAdapter.notifyDataSetChanged();
        }

    }

    private ClipboardManager mClipboardManager;
    private ClipData mClipData;


    // 删除 复制 消息 弹窗
    public void initPopup(int x, int y, View view, final MessageInfo messageInfo) {
        mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        View pop_layout = LayoutInflater.from(ChatActivity.this).inflate(R.layout.chat_message_deail_popup, null);
        TextView copy, delete;
        copy = pop_layout.findViewById(R.id.tv_copy);
        delete = pop_layout.findViewById(R.id.tv_delete);
        if (messageInfo.getContentType() == 2 || messageInfo.getContentType() == 3 || messageInfo.getContentType() == 4) {
            copy.setVisibility(View.GONE);
        }
        final PopupWindow mPopupWindow = new PopupWindow(300, 100);
        mPopupWindow.setContentView(pop_layout);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.update();

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        pop_layout.measure(w, h);
        //获取PopWindow宽和高
        int mHeight = pop_layout.getMeasuredHeight();
        int mWidth = pop_layout.getMeasuredWidth();
        int xoff = x - mWidth / 2;
        int yoff = 0 - (view.getHeight() - y) - mHeight;
        mPopupWindow.showAsDropDown(view, xoff, 2 * yoff);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建一个新的文本clip对象
                if (messageInfo.getContentType() == 1) {
                    mClipData = ClipData.newPlainText("Ebuyhouse", messageInfo.getContent());
                    //把clip对象放在剪贴板中
                    mClipboardManager.setPrimaryClip(mClipData);
                    mPopupWindow.dismiss();
                }

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageInfo.delete();
                messageInfos.remove(messageInfo);
                chatAdapter.clear();
                chatAdapter.addAll(messageInfos);
                chatAdapter.notifyDataSetChanged();
                mPopupWindow.dismiss();

            }
        });
    }
}
