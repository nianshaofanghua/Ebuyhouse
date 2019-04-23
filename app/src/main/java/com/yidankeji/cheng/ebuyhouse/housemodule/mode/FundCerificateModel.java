package com.yidankeji.cheng.ebuyhouse.housemodule.mode;

import java.util.List;

/**
 * Created by ${syj} on 2018/3/26.
 */

public class FundCerificateModel {


    /**
     * content : {"data":{"capitals":["http://192.168.0.252:8080/oss/image/house/3ee12d3362cba1fd238921aa5600accf.jpeg"],"is_update":1},"rows":[]}
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
         * data : {"capitals":["http://192.168.0.252:8080/oss/image/house/3ee12d3362cba1fd238921aa5600accf.jpeg"],"is_update":1}
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
             * capitals : ["http://192.168.0.252:8080/oss/image/house/3ee12d3362cba1fd238921aa5600accf.jpeg"]
             * is_update : 1
             */

            private int is_update;
            private List<String> capitals;

            public int getIs_update() {
                return is_update;
            }

            public void setIs_update(int is_update) {
                this.is_update = is_update;
            }

            public List<String> getCapitals() {
                return capitals;
            }

            public void setCapitals(List<String> capitals) {
                this.capitals = capitals;
            }
        }
    }
}
