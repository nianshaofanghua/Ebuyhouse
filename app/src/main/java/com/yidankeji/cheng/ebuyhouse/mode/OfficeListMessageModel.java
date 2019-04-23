package com.yidankeji.cheng.ebuyhouse.mode;

import java.util.List;

/**
 * Created by ${syj} on 2018/2/27.
 */

public class OfficeListMessageModel {

    /**
     * content : {"data":{"unreadNum":0},"rows":[]}
     * message : 操作成功
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
         * data : {"unreadNum":0}
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
             * unreadNum : 0
             */

            private int unreadNum;

            public int getUnreadNum() {
                return unreadNum;
            }

            public void setUnreadNum(int unreadNum) {
                this.unreadNum = unreadNum;
            }
        }
    }
}
