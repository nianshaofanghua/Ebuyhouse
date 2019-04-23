package com.yidankeji.cheng.ebuyhouse.community.chat.util;

import android.util.Log;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by ${syj} on 2018/5/9.
 */

public class SocketUtils {
    public static Socket mSocket;







    public static void getSocket(String userId) {
        Log.e("urser", "" + userId);
//        IO.Options options = new IO.Options();
//        options.transports = new String[]{"websocket"};
//        options.reconnectionAttempts = 50;     // 重连尝试次数
//        options.reconnectionDelay = 1000;     // 失败重连的时间间隔(ms)
//        options.timeout = 20000;              // 连接超时时间(ms)
//        options.forceNew = true;
//        options.query = "userid="+userId;
        try {
           mSocket = IO.socket("http://47.254.45.106:9090/");

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        SocketUtils.mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                Log.e("EVENT_CONNECT", "连接成功");

            }
        });
        mSocket.on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                Log.e("EVENT_CONNECT", "连接失敗");
            }
        });
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
            @Override
            public void call(Object...   objects) {
                Log.e("EVENT_CONNECT", "连接失敗1");
            }
        });
    }


}
