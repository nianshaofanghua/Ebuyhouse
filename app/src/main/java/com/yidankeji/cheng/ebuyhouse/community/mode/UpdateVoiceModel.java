package com.yidankeji.cheng.ebuyhouse.community.mode;

import java.util.List;

/**
 * Created by ${syj} on 2018/5/9.
 */

public class UpdateVoiceModel {

    /**
     * content : {"data":{"img_url":"http://192.168.0.211:8080/oss/mp4/chat/d2fd797baa360a7bbd51c120a15871ac.m4a"},"rows":[]}
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
         * data : {"img_url":"http://192.168.0.211:8080/oss/mp4/chat/d2fd797baa360a7bbd51c120a15871ac.m4a"}
         * rows : []
         */

        private DataBean data;
        private List<?> rows;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public List<?> getRows() {
            return rows;
        }

        public void setRows(List<?> rows) {
            this.rows = rows;
        }

        public static class DataBean {
            /**
             * img_url : http://192.168.0.211:8080/oss/mp4/chat/d2fd797baa360a7bbd51c120a15871ac.m4a
             */

            private String img_url;

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }
        }
    }
}
