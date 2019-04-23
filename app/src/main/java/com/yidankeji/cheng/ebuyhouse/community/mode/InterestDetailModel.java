package com.yidankeji.cheng.ebuyhouse.community.mode;

import java.util.List;

/**
 * Created by ${syj} on 2018/4/16.
 */

public class InterestDetailModel {


    /**
     * content : {"data":{"interest_id":"d1f5fa1700e94afdadb03356771baff4","id":"d1f5fa1700e94afdadb03356771baff4","name":"aaaaa","fk_customer_id":"bb6346e3f2ad4ecebb24f323d79fb6df","fk_leader_id":"691ae86af0e740f4a6329fd774eb5594","fk_community_id":"d6cdf9ee124f4b3fac67b8858f79fb0b","enter_mode":"free","check_msg":"","check_state":2,"head_url":"http://127.0.0.1:8080/oss/image/customer/43d5eee627d2a586f79816f0f68a37a6.jpeg","notice":"yyyyy","add_time":1523600662,"update_time":1523613382,"isJoin":true,"interestLabelList":[{"fk_label_id":"aa5e9ab2d24a4732a1f95ac144de080c","label_name":"jumpss"}]},"rows":[]}
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
         * data : {"interest_id":"d1f5fa1700e94afdadb03356771baff4","id":"d1f5fa1700e94afdadb03356771baff4","name":"aaaaa","fk_customer_id":"bb6346e3f2ad4ecebb24f323d79fb6df","fk_leader_id":"691ae86af0e740f4a6329fd774eb5594","fk_community_id":"d6cdf9ee124f4b3fac67b8858f79fb0b","enter_mode":"free","check_msg":"","check_state":2,"head_url":"http://127.0.0.1:8080/oss/image/customer/43d5eee627d2a586f79816f0f68a37a6.jpeg","notice":"yyyyy","add_time":1523600662,"update_time":1523613382,"isJoin":true,"interestLabelList":[{"fk_label_id":"aa5e9ab2d24a4732a1f95ac144de080c","label_name":"jumpss"}]}
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
             * interest_id : d1f5fa1700e94afdadb03356771baff4
             * id : d1f5fa1700e94afdadb03356771baff4
             * name : aaaaa
             * fk_customer_id : bb6346e3f2ad4ecebb24f323d79fb6df
             * fk_leader_id : 691ae86af0e740f4a6329fd774eb5594
             * fk_community_id : d6cdf9ee124f4b3fac67b8858f79fb0b
             * enter_mode : free
             * check_msg :
             * check_state : 2
             * head_url : http://127.0.0.1:8080/oss/image/customer/43d5eee627d2a586f79816f0f68a37a6.jpeg
             * notice : yyyyy
             * add_time : 1523600662
             * update_time : 1523613382
             * isJoin : true
             * interestLabelList : [{"fk_label_id":"aa5e9ab2d24a4732a1f95ac144de080c","label_name":"jumpss"}]
             */

            private String interest_id;
            private String id;
            private String name;
            private String fk_customer_id;
            private String fk_leader_id;
            private String fk_community_id;
            private String enter_mode;
            private String check_msg;
            private int check_state;
            private String head_url;
            private String notice;
            private int add_time;
            private int update_time;
            private boolean isJoin;
            private List<InterestLabelListBean> interestLabelList;

            public String getInterest_id() {
                return interest_id;
            }

            public void setInterest_id(String interest_id) {
                this.interest_id = interest_id;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getFk_customer_id() {
                return fk_customer_id;
            }

            public void setFk_customer_id(String fk_customer_id) {
                this.fk_customer_id = fk_customer_id;
            }

            public String getFk_leader_id() {
                return fk_leader_id;
            }

            public void setFk_leader_id(String fk_leader_id) {
                this.fk_leader_id = fk_leader_id;
            }

            public String getFk_community_id() {
                return fk_community_id;
            }

            public void setFk_community_id(String fk_community_id) {
                this.fk_community_id = fk_community_id;
            }

            public String getEnter_mode() {
                return enter_mode;
            }

            public void setEnter_mode(String enter_mode) {
                this.enter_mode = enter_mode;
            }

            public String getCheck_msg() {
                return check_msg;
            }

            public void setCheck_msg(String check_msg) {
                this.check_msg = check_msg;
            }

            public int getCheck_state() {
                return check_state;
            }

            public void setCheck_state(int check_state) {
                this.check_state = check_state;
            }

            public String getHead_url() {
                return head_url;
            }

            public void setHead_url(String head_url) {
                this.head_url = head_url;
            }

            public String getNotice() {
                return notice;
            }

            public void setNotice(String notice) {
                this.notice = notice;
            }

            public int getAdd_time() {
                return add_time;
            }

            public void setAdd_time(int add_time) {
                this.add_time = add_time;
            }

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
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
private boolean isChose;

                public boolean isChose() {
                    return isChose;
                }

                public void setChose(boolean chose) {
                    isChose = chose;
                }

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
