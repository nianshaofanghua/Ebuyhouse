package com.yidankeji.cheng.ebuyhouse.community.mode;

import java.util.List;

/**
 * Created by ${syj} on 2018/4/17.
 */

public class FriendApplyModel {

    /**
     * content : {"data":{},"rows":[{"id":"8825a67276eb4a8785ed72af6130e3ab","fk_customer_id":"52511ff3ce284b4ba9814d623c324a2f","fk_target_id":"691ae86af0e740f4a6329fd774eb5594","remark":"Jack ","state":2,"add_time":1523865993,"source":"Community","nickname":"17719203237","head_url":"http://res.ebuyhouse.com/oss/image/customer/a1cc184857c9ee3d7f0b960cc3f51a27.jpeg"}]}
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
         * rows : [{"id":"8825a67276eb4a8785ed72af6130e3ab","fk_customer_id":"52511ff3ce284b4ba9814d623c324a2f","fk_target_id":"691ae86af0e740f4a6329fd774eb5594","remark":"Jack ","state":2,"add_time":1523865993,"source":"Community","nickname":"17719203237","head_url":"http://res.ebuyhouse.com/oss/image/customer/a1cc184857c9ee3d7f0b960cc3f51a27.jpeg"}]
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
             * id : 8825a67276eb4a8785ed72af6130e3ab
             * fk_customer_id : 52511ff3ce284b4ba9814d623c324a2f
             * fk_target_id : 691ae86af0e740f4a6329fd774eb5594
             * remark : Jack
             * state : 2
             * add_time : 1523865993
             * source : Community
             * nickname : 17719203237
             * head_url : http://res.ebuyhouse.com/oss/image/customer/a1cc184857c9ee3d7f0b960cc3f51a27.jpeg
             */

            private String id;
            private String fk_customer_id;
            private String fk_target_id;
            private String remark;
            private int state;
            private int add_time;
            private String source;
            private String nickname;
            private String head_url;
private boolean isOpen;

            public boolean isOpen() {
                return isOpen;
            }

            public void setOpen(boolean open) {
                isOpen = open;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getFk_customer_id() {
                return fk_customer_id;
            }

            public void setFk_customer_id(String fk_customer_id) {
                this.fk_customer_id = fk_customer_id;
            }

            public String getFk_target_id() {
                return fk_target_id;
            }

            public void setFk_target_id(String fk_target_id) {
                this.fk_target_id = fk_target_id;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public int getAdd_time() {
                return add_time;
            }

            public void setAdd_time(int add_time) {
                this.add_time = add_time;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getHead_url() {
                return head_url;
            }

            public void setHead_url(String head_url) {
                this.head_url = head_url;
            }
        }
    }
}
