package com.yidankeji.cheng.ebuyhouse.community.db;

import com.yidankeji.cheng.ebuyhouse.community.suger.SugarRecord;

/**
 * 作者：Rance on 2016/12/14 14:13
 * 邮箱：rance935@163.com
 */
public class MessageInfo extends SugarRecord {


    private int sendtype;
    private String filepath;
    private String time;
    private String header;
    private String imageUrl;
    private long voiceTime;
    private String msgId;

    private String user_id;
    private int voiceLength;
    private int message_type;
    private long send_time;
    private int contentHeight;
    private int contentType;  // 1 文字 2语音 3图片 4视频
    private String houseid;
    private long receive_time;
    private int contentWidth;
    private String target_id;
    private int target_type; //1 社区 2 兴趣圈 3 好友
    private int shouldShowTime;
    private int isMine;
    private String houseID;
    private int sendState;
    private String content;
    private String video_img;
    private String infoId;
    private String show_name;
    private String inter_name;
    private String show_head;
    private String user_head;
    private int unread_number;
    private String myid;
private String friendid;
    public MessageInfo() {

    }

    public String getFriendid() {
        return friendid;
    }

    public void setFriendid(String friendid) {
        this.friendid = friendid;
    }

    public int getSendtype() {
        return sendtype;
    }

    public void setSendtype(int sendtype) {
        this.sendtype = sendtype;
    }

    public String getMyId() {
        return myid;
    }

    public void setMyId(String myId) {
        this.myid = myId;
    }

    public String getShow_name() {
        return show_name;
    }

    public void setShow_name(String show_name) {
        this.show_name = show_name;
    }

    public String getInter_name() {
        return inter_name;
    }
    public void setInter_name(String inter_name) {
        this.inter_name = inter_name;
    }
    public String getShow_head() {
        return show_head;
    }
    public void setShow_head(String show_head) {
        this.show_head = show_head;
    }

    public String getUser_head() {
        return user_head;
    }

    public void setUser_head(String user_head) {
        this.user_head = user_head;
    }

    public int getUnread_number() {
        return unread_number;
    }

    public void setUnread_number(int unread_number) {
        this.unread_number = unread_number;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getVideo_img() {
        return video_img;
    }

    public void setVideo_img(String video_img) {
        this.video_img = video_img;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getVoiceLength() {
        return voiceLength;
    }

    public void setVoiceLength(int voiceLength) {
        this.voiceLength = voiceLength;
    }

    public int getContentHeight() {
        return contentHeight;
    }

    public void setContentHeight(int contentHeight) {
        this.contentHeight = contentHeight;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getHouseID() {
        return houseid;
    }

    public void setHouseID(String houseID) {
        this.houseID = houseID;
        this.houseid = houseID;
    }

    public long getReceive_time() {
        return receive_time;
    }

    public void setReceive_time(long receive_time) {
        this.receive_time = receive_time;
    }

    public int getContentWidth() {
        return contentWidth;
    }

    public void setContentWidth(int contentWidth) {
        this.contentWidth = contentWidth;
    }

    public int getShouldShowTime() {
        return shouldShowTime;
    }

    public void setShouldShowTime(int shouldShowTime) {
        this.shouldShowTime = shouldShowTime;
    }

    public int getIsMine() {
        return isMine;
    }

    public void setIsMine(int isMine) {
        this.isMine = isMine;
    }


    public String getTarget_id() {
        return target_id;
    }

    public void setTarget_id(String target_id) {
        this.target_id = target_id;
    }

    public int getTarget_type() {
        return target_type;
    }

    public void setTarget_type(int target_type) {
        this.target_type = target_type;
    }

    public long getSend_time() {
        return send_time;
    }

    public void setSend_time(long send_time) {
        this.send_time = send_time;
    }

    public int getMessage_type() {
        return message_type;
    }

    public void setMessage_type(int message_type) {
        this.message_type = message_type;
    }

    public int getType() {
        return sendtype;
    }

    public void setType(int type) {
        this.sendtype = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public int getSendState() {
        return sendState;
    }

    public void setSendState(int sendState) {
        this.sendState = sendState;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getVoiceTime() {
        return voiceTime;
    }

    public void setVoiceTime(long voiceTime) {
        this.voiceTime = voiceTime;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    @Override
    public String toString() {
        return "MessageInfo{" +
                "type=" + sendtype +
                ", content='" + content + '\'' +
                ", filepath='" + filepath + '\'' +
                ", sendState=" + sendState +
                ", time='" + time + '\'' +
                ", header='" + header + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", voiceTime=" + voiceTime +
                ", msgId='" + msgId + '\'' +
                '}';
    }
}
