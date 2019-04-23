package com.yidankeji.cheng.ebuyhouse.community.mode;

import java.util.List;

/**
 * Created by ${syj} on 2018/4/17.
 */

public class FriendMessageModel {

    /**
     * content : {"data":{"id":"52511ff3ce284b4ba9814d623c324a2f","head_url":"http://res.ebuyhouse.com/oss/image/customer/a1cc184857c9ee3d7f0b960cc3f51a27.jpeg","nickname":"17719203237","firstname":"fufv","middlename":"xggg","lastname":"hhfg","gender":"","constellation":"","isFriend":0},"rows":[]}
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
         * data : {"id":"52511ff3ce284b4ba9814d623c324a2f","head_url":"http://res.ebuyhouse.com/oss/image/customer/a1cc184857c9ee3d7f0b960cc3f51a27.jpeg","nickname":"17719203237","firstname":"fufv","middlename":"xggg","lastname":"hhfg","gender":"","constellation":"","isFriend":0}
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
             * id : 52511ff3ce284b4ba9814d623c324a2f
             * head_url : http://res.ebuyhouse.com/oss/image/customer/a1cc184857c9ee3d7f0b960cc3f51a27.jpeg
             * nickname : 17719203237
             * firstname : fufv
             * middlename : xggg
             * lastname : hhfg
             * gender :
             * constellation :
             * isFriend : 0
             */

            private String id;
            private String head_url;
            private String nickname;
            private String firstname;
            private String middlename;
            private String lastname;
            private String gender;
            private String constellation;
            private int isFriend;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getHead_url() {
                return head_url;
            }

            public void setHead_url(String head_url) {
                this.head_url = head_url;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getFirstname() {
                return firstname;
            }

            public void setFirstname(String firstname) {
                this.firstname = firstname;
            }

            public String getMiddlename() {
                return middlename;
            }

            public void setMiddlename(String middlename) {
                this.middlename = middlename;
            }

            public String getLastname() {
                return lastname;
            }

            public void setLastname(String lastname) {
                this.lastname = lastname;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getConstellation() {
                return constellation;
            }

            public void setConstellation(String constellation) {
                this.constellation = constellation;
            }

            public int getIsFriend() {
                return isFriend;
            }

            public void setIsFriend(int isFriend) {
                this.isFriend = isFriend;
            }
        }
    }
}
