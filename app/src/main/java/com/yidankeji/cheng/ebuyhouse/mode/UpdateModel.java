package com.yidankeji.cheng.ebuyhouse.mode;

import java.util.List;

/**
 * Created by ${syj} on 2018/3/28.
 */

public class UpdateModel {

    /**
     * content : {"data":{"res_url":"","is_forced_update":0,"res_name":"","res_code":"954892","update_log":"暂无版本更新"},"rows":[]}
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
         * data : {"res_url":"","is_forced_update":0,"res_name":"","res_code":"954892","update_log":"暂无版本更新"}
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
             * res_url :
             * is_forced_update : 0
             * res_name :
             * res_code : 954892
             * update_log : 暂无版本更新
             */

            private String res_url;
            private int is_forced_update;
            private String res_name;
            private String res_code;
            private String update_log;

            public String getRes_url() {
                return res_url;
            }

            public void setRes_url(String res_url) {
                this.res_url = res_url;
            }

            public int getIs_forced_update() {
                return is_forced_update;
            }

            public void setIs_forced_update(int is_forced_update) {
                this.is_forced_update = is_forced_update;
            }

            public String getRes_name() {
                return res_name;
            }

            public void setRes_name(String res_name) {
                this.res_name = res_name;
            }

            public String getRes_code() {
                return res_code;
            }

            public void setRes_code(String res_code) {
                this.res_code = res_code;
            }

            public String getUpdate_log() {
                return update_log;
            }

            public void setUpdate_log(String update_log) {
                this.update_log = update_log;
            }
        }
    }
}
