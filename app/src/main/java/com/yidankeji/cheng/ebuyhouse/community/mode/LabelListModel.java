package com.yidankeji.cheng.ebuyhouse.community.mode;

import java.util.List;

/**
 * Created by ${syj} on 2018/4/16.
 */

public class LabelListModel {


    /**
     * content : {"data":{},"rows":[{"id":"9010899dd4dc49b3b088bd26df2360ba","label":"swim","is_public":1},{"id":"aa5e9ab2d24a4732a1f95ac144de080c","label":"jumpss","is_public":1},{"id":"3efe81ab325f412ba9d6e75634fca47e","label":"dddd","is_public":1}]}
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
         * rows : [{"id":"9010899dd4dc49b3b088bd26df2360ba","label":"swim","is_public":1},{"id":"aa5e9ab2d24a4732a1f95ac144de080c","label":"jumpss","is_public":1},{"id":"3efe81ab325f412ba9d6e75634fca47e","label":"dddd","is_public":1}]
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
             * id : 9010899dd4dc49b3b088bd26df2360ba
             * label : swim
             * is_public : 1
             */

            private String id;
            private String label;
            private int is_public;
private boolean isChose;


            public boolean isChose() {
                return isChose;
            }

            public void setChose(boolean chose) {
                isChose = chose;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public int getIs_public() {
                return is_public;
            }

            public void setIs_public(int is_public) {
                this.is_public = is_public;
            }
        }
    }
}
