package com.yidankeji.cheng.ebuyhouse.mode;

import java.util.List;

/**
 * Created by ${syj} on 2018/1/22.
 */

public class DefaultAddressModel {

    /**
     * message : 操作成功
     * state : 1
     * content : {"data":{},"rows":[{"k":"map_center","v":"38.90719,-77.036870"},{"k":"house_state","v":"NY"},{"k":"house_state_value","v":"NewYork"}]}
     */

    private String message;
    private int state;
    private ContentBean content;

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

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * data : {}
         * rows : [{"k":"map_center","v":"38.90719,-77.036870"},{"k":"house_state","v":"NY"},{"k":"house_state_value","v":"NewYork"}]
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
             * k : map_center
             * v : 38.90719,-77.036870
             */

            private String k;
            private String v;

            public String getK() {
                return k;
            }

            public void setK(String k) {
                this.k = k;
            }

            public String getV() {
                return v;
            }

            public void setV(String v) {
                this.v = v;
            }
        }
    }
}
