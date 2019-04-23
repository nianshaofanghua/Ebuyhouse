package com.yidankeji.cheng.ebuyhouse.community.mode;

import java.util.List;

/**
 * Created by ${syj} on 2018/4/16.
 */

public class InterestFriendModel {


    /**
     * content : {"data":{},"rows":[{"interest_id":"d1f5fa1700e94afdadb03356771baff4","head_url":"http://127.0.0.1:8080/oss/image/customer/43d5eee627d2a586f79816f0f68a37a6.jpeg","name":"aaaaa","fk_leader_id":"691ae86af0e740f4a6329fd774eb5594","isJoin":true,"interestLabelList":[{"fk_label_id":"aa5e9ab2d24a4732a1f95ac144de080c","label_name":"jumpss"}]}]}
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
         * rows : [{"interest_id":"d1f5fa1700e94afdadb03356771baff4","head_url":"http://127.0.0.1:8080/oss/image/customer/43d5eee627d2a586f79816f0f68a37a6.jpeg","name":"aaaaa","fk_leader_id":"691ae86af0e740f4a6329fd774eb5594","isJoin":true,"interestLabelList":[{"fk_label_id":"aa5e9ab2d24a4732a1f95ac144de080c","label_name":"jumpss"}]}]
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
             * interest_id : d1f5fa1700e94afdadb03356771baff4
             * head_url : http://127.0.0.1:8080/oss/image/customer/43d5eee627d2a586f79816f0f68a37a6.jpeg
             * name : aaaaa
             * fk_leader_id : 691ae86af0e740f4a6329fd774eb5594
             * isJoin : true
             * interestLabelList : [{"fk_label_id":"aa5e9ab2d24a4732a1f95ac144de080c","label_name":"jumpss"}]
             */

            private String interest_id;
            private String head_url;
            private String name;
            private String fk_leader_id;
            private boolean isJoin;
            private String firstWord;

            public String getFirstWord() {
                return firstWord;
            }

            public void setFirstWord(String firstWord) {
                this.firstWord = firstWord;
            }

            private List<InterestLabelListBean> interestLabelList;

            public String getInterest_id() {
                return interest_id;
            }

            public void setInterest_id(String interest_id) {
                this.interest_id = interest_id;
            }

            public String getHead_url() {
                return head_url;
            }

            public void setHead_url(String head_url) {
                this.head_url = head_url;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getFk_leader_id() {
                return fk_leader_id;
            }

            public void setFk_leader_id(String fk_leader_id) {
                this.fk_leader_id = fk_leader_id;
            }

            public boolean isIsJoin() {
                return isJoin;
            }

            public void setIsJoin(boolean isJoin) {
                this.isJoin = isJoin;
            }

            public List<InterestLabelListBean> getInterestLabelList() {
                return interestLabelList;
            }

            public void setInterestLabelList(List<InterestLabelListBean> interestLabelList) {
                this.interestLabelList = interestLabelList;
            }

            public static class InterestLabelListBean {
                /**
                 * fk_label_id : aa5e9ab2d24a4732a1f95ac144de080c
                 * label_name : jumpss
                 */

                private String fk_label_id;
                private String label_name;

                public String getFk_label_id() {
                    return fk_label_id;
                }

                public void setFk_label_id(String fk_label_id) {
                    this.fk_label_id = fk_label_id;
                }

                public String getLabel_name() {
                    return label_name;
                }

                public void setLabel_name(String label_name) {
                    this.label_name = label_name;
                }
            }
        }
    }
}
