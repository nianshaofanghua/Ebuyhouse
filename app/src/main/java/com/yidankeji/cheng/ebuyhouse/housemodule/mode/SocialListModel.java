package com.yidankeji.cheng.ebuyhouse.housemodule.mode;

import java.util.List;

/**
 * Created by ${syj} on 2018/4/18.
 */

public class SocialListModel {

    /**
     * content : {"data":{},"rows":[{"community_id":"d6cdf9ee124f4b3fac67b8858f79fb0b","name":"第一社区","head_url":"http://192.168.0.248:8080/oss/image/community/f8966d130b20cb5f5b36bbbe3e5497a8.jpeg","address":"200-240 N F St, Exeter, CA 93221,Wood Lake"}]}
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
         * rows : [{"community_id":"d6cdf9ee124f4b3fac67b8858f79fb0b","name":"第一社区","head_url":"http://192.168.0.248:8080/oss/image/community/f8966d130b20cb5f5b36bbbe3e5497a8.jpeg","address":"200-240 N F St, Exeter, CA 93221,Wood Lake"}]
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
        }

        public static class RowsBean {
            /**
             * community_id : d6cdf9ee124f4b3fac67b8858f79fb0b
             * name : 第一社区
             * head_url : http://192.168.0.248:8080/oss/image/community/f8966d130b20cb5f5b36bbbe3e5497a8.jpeg
             * address : 200-240 N F St, Exeter, CA 93221,Wood Lake
             */

            private String community_id;
            private String name;
            private String head_url;
            private String address;
            private String firstWord;
            private int state;
            private String room_id;

            public String getRoom_id() {
                return room_id;
            }

            public void setRoom_id(String room_id) {
                this.room_id = room_id;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public String getFirstWord() {
                return firstWord;
            }

            public void setFirstWord(String firstWord) {
                this.firstWord = firstWord;
            }

            public String getCommunity_id() {
                return community_id;
            }

            public void setCommunity_id(String community_id) {
                this.community_id = community_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getHead_url() {
                return head_url;
            }

            public void setHead_url(String head_url) {
                this.head_url = head_url;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }
        }
    }
}
