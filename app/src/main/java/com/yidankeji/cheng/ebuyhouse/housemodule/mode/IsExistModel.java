package com.yidankeji.cheng.ebuyhouse.housemodule.mode;

import java.util.List;

/**
 * Created by ${syj} on 2018/2/2.
 */

public class IsExistModel {


    /**
     * content : {"data":{"isExist":false},"rows":[]}
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
         * data : {"isExist":false}
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
             * isExist : false
             */

            private boolean isExist;

            public boolean isIsExist() {
                return isExist;
            }

            public void setIsExist(boolean isExist) {
                this.isExist = isExist;
            }
        }
    }
}
