package com.yidankeji.cheng.ebuyhouse.community.mode;

import java.util.List;

/**
 * Created by ${syj} on 2018/4/16.
 */

public class AddLabelIdModel {


    /**
     * content : {"data":{"labelId":"a7f0603a752549b1af2f66f83e3d7e42"},"rows":[]}
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
         * data : {"labelId":"a7f0603a752549b1af2f66f83e3d7e42"}
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
             * labelId : a7f0603a752549b1af2f66f83e3d7e42
             */

            private String labelId;

            public String getLabelId() {
                return labelId;
            }

            public void setLabelId(String labelId) {
                this.labelId = labelId;
            }
        }
    }
}
