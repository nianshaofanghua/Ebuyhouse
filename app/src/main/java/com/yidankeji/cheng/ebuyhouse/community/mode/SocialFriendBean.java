package com.yidankeji.cheng.ebuyhouse.community.mode;

import java.util.List;

/**
 * Created by ${syj} on 2018/4/16.
 */

public class SocialFriendBean {

    /**
     * content : {"data":{},"rows":[{"fk_customer_id":"691ae86af0e740f4a6329fd774eb5594","head_url":"http://192.168.0.248:8080/oss/image/house/1e0ef9512c1d3a07a07430dbd1aa346d.jpeg","customer_name":"lizhun@yidankeji.com","isFriend":0},{"fk_customer_id":"5cc8338168f6417badf071dd6b411564","head_url":"http://192.168.0.248:8080/oss/image/house/1e0ef9512c1d3a07a07430dbd1aa346d.jpeg","customer_name":"15518879996","isFriend":0},{"fk_customer_id":"bb6346e3f2ad4ecebb24f323d79fb6df","head_url":"","customer_name":"294242841@qq.com","isFriend":0}]}
     * message :
     * state : 1
     */

    private ContentBean content;
    private String message;
    private int state;

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public static class ContentBean {
        /**
         * data : {}
         * rows : [{"fk_customer_id":"691ae86af0e740f4a6329fd774eb5594","head_url":"http://192.168.0.248:8080/oss/image/house/1e0ef9512c1d3a07a07430dbd1aa346d.jpeg","customer_name":"lizhun@yidankeji.com","isFriend":0},{"fk_customer_id":"5cc8338168f6417badf071dd6b411564","head_url":"http://192.168.0.248:8080/oss/image/house/1e0ef9512c1d3a07a07430dbd1aa346d.jpeg","customer_name":"15518879996","isFriend":0},{"fk_customer_id":"bb6346e3f2ad4ecebb24f323d79fb6df","head_url":"","customer_name":"294242841@qq.com","isFriend":0}]
         */

        private DataBean data;
        private List<RowsBean> rows;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public List<RowsBean> getRows() {
            return rows;
        }

        public void setRows(List<RowsBean> rows) {
            this.rows = rows;
        }

        public static class DataBean {
            int new_friend;

            public int getNew_friend() {
                return new_friend;
            }

            public void setNew_friend(int new_friend) {
                this.new_friend = new_friend;
            }
        }

        public static class RowsBean {
            /**
             * fk_customer_id : 691ae86af0e740f4a6329fd774eb5594
             * head_url : http://192.168.0.248:8080/oss/image/house/1e0ef9512c1d3a07a07430dbd1aa346d.jpeg
             * customer_name : lizhun@yidankeji.com
             * isFriend : 0
             */

            private String fk_customer_id;
            private String head_url;
            private String customer_name;
            private int isFriend;
            private int message;
            private String firstWord;
            private boolean isChose;
            private long add_time;
            private String nick_name;
            private String room_id;

            public String getRoom_id() {
                return room_id;
            }

            public void setRoom_id(String room_id) {
                this.room_id = room_id;
            }

            public long getAdd_time() {
                return add_time;
            }

            public void setAdd_time(long add_time) {
                this.add_time = add_time;
            }

            public String getNick_name() {
                return nick_name;
            }

            public void setNick_name(String nick_name) {
                this.nick_name = nick_name;
            }

            public int getMessage() {
                return message;
            }

            public void setMessage(int message) {
                this.message = message;
            }

            public boolean isChose() {
                return isChose;
            }

            public void setChose(boolean chose) {
                isChose = chose;
            }

            public String getFirstWord() {
                return firstWord;
            }

            public void setFirstWord(String firstWord) {
                this.firstWord = firstWord;
            }

            public String getFk_customer_id() {
                return fk_customer_id;
            }

            public void setFk_customer_id(String fk_customer_id) {
                this.fk_customer_id = fk_customer_id;
            }

            public String getHead_url() {
                return head_url;
            }

            public void setHead_url(String head_url) {
                this.head_url = head_url;
            }

            public String getCustomer_name() {
                return customer_name;
            }

            public void setCustomer_name(String customer_name) {
                this.customer_name = customer_name;
            }

            public int getIsFriend() {
                return isFriend;
            }

            public void setIsFriend(int isFriend) {
                this.isFriend = isFriend;
            }
        }
    }
}
