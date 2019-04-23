package com.yidankeji.cheng.ebuyhouse.community.mode;

import java.util.List;

/**
 * Created by ${syj} on 2018/4/13.
 */

public class SocialDetailModel {


    /**
     * content : {"data":{"community_id":"d6cdf9ee124f4b3fac67b8858f79fb0b","id":"d6cdf9ee124f4b3fac67b8858f79fb0b","name":"第一社区","head_url":"http://192.168.0.248:8080/oss/image/community/f8966d130b20cb5f5b36bbbe3e5497a8.jpeg","source_name":"admin","source_id":"7cbd6f8d277345c396866e126b7449de","latitude":36.297675,"longitude":-119.143106,"address":"200-240 N F St, Exeter, CA 93221,Wood Lake","fk_city_id":"","fk_state_id":"","img_code":"efb47f23107d47de9541ac0f2566355d","notice":"第一社区","other_attr":"[{\"key\":\"交通\",\"value\":\"便利\"},{\"key\":\"条件\",\"value\":\"优越\"}]","add_time":1523349958,"imgs":[{"id":"81206da1779e4277b37b4e8aa22e2303","img_url":"http://192.168.0.248:8080/oss/image/community/f8966d130b20cb5f5b36bbbe3e5497a8.jpeg","img_code":"efb47f23107d47de9541ac0f2566355d","sort":1,"add_time":1523349958}],"isJoin":true,"isApply":false},"rows":[]}
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
         * data : {"community_id":"d6cdf9ee124f4b3fac67b8858f79fb0b","id":"d6cdf9ee124f4b3fac67b8858f79fb0b","name":"第一社区","head_url":"http://192.168.0.248:8080/oss/image/community/f8966d130b20cb5f5b36bbbe3e5497a8.jpeg","source_name":"admin","source_id":"7cbd6f8d277345c396866e126b7449de","latitude":36.297675,"longitude":-119.143106,"address":"200-240 N F St, Exeter, CA 93221,Wood Lake","fk_city_id":"","fk_state_id":"","img_code":"efb47f23107d47de9541ac0f2566355d","notice":"第一社区","other_attr":"[{\"key\":\"交通\",\"value\":\"便利\"},{\"key\":\"条件\",\"value\":\"优越\"}]","add_time":1523349958,"imgs":[{"id":"81206da1779e4277b37b4e8aa22e2303","img_url":"http://192.168.0.248:8080/oss/image/community/f8966d130b20cb5f5b36bbbe3e5497a8.jpeg","img_code":"efb47f23107d47de9541ac0f2566355d","sort":1,"add_time":1523349958}],"isJoin":true,"isApply":false}
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
             * community_id : d6cdf9ee124f4b3fac67b8858f79fb0b
             * id : d6cdf9ee124f4b3fac67b8858f79fb0b
             * name : 第一社区
             * head_url : http://192.168.0.248:8080/oss/image/community/f8966d130b20cb5f5b36bbbe3e5497a8.jpeg
             * source_name : admin
             * source_id : 7cbd6f8d277345c396866e126b7449de
             * latitude : 36.297675
             * longitude : -119.143106
             * address : 200-240 N F St, Exeter, CA 93221,Wood Lake
             * fk_city_id :
             * fk_state_id :
             * img_code : efb47f23107d47de9541ac0f2566355d
             * notice : 第一社区
             * other_attr : [{"key":"交通","value":"便利"},{"key":"条件","value":"优越"}]
             * add_time : 1523349958
             * imgs : [{"id":"81206da1779e4277b37b4e8aa22e2303","img_url":"http://192.168.0.248:8080/oss/image/community/f8966d130b20cb5f5b36bbbe3e5497a8.jpeg","img_code":"efb47f23107d47de9541ac0f2566355d","sort":1,"add_time":1523349958}]
             * isJoin : true
             * isApply : false
             */

            private String community_id;
            private String id;
            private String name;
            private String head_url;
            private String source_name;
            private String source_id;
            private double latitude;
            private double longitude;
            private String address;
            private String fk_city_id;
            private String fk_state_id;
            private String img_code;
            private String notice;
            private String other_attr;
            private int add_time;
            private boolean isJoin;
            private boolean isApply;
            private List<ImgsBean> imgs;
            private int customerCount;
            private int interestCount;

            public Object getCustomerCount() {
                return customerCount;
            }



            public Object getInterestCount() {
                return interestCount;
            }

            public void setCustomerCount(int customerCount) {
                this.customerCount = customerCount;
            }

            public void setInterestCount(int interestCount) {
                this.interestCount = interestCount;
            }

            public String getCommunity_id() {
                return community_id;
            }

            public void setCommunity_id(String community_id) {
                this.community_id = community_id;
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

            public String getHead_url() {
                return head_url;
            }

            public void setHead_url(String head_url) {
                this.head_url = head_url;
            }

            public String getSource_name() {
                return source_name;
            }

            public void setSource_name(String source_name) {
                this.source_name = source_name;
            }

            public String getSource_id() {
                return source_id;
            }

            public void setSource_id(String source_id) {
                this.source_id = source_id;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getFk_city_id() {
                return fk_city_id;
            }

            public void setFk_city_id(String fk_city_id) {
                this.fk_city_id = fk_city_id;
            }

            public String getFk_state_id() {
                return fk_state_id;
            }

            public void setFk_state_id(String fk_state_id) {
                this.fk_state_id = fk_state_id;
            }

            public String getImg_code() {
                return img_code;
            }

            public void setImg_code(String img_code) {
                this.img_code = img_code;
            }

            public String getNotice() {
                return notice;
            }

            public void setNotice(String notice) {
                this.notice = notice;
            }

            public String getOther_attr() {
                return other_attr;
            }

            public void setOther_attr(String other_attr) {
                this.other_attr = other_attr;
            }

            public int getAdd_time() {
                return add_time;
            }

            public void setAdd_time(int add_time) {
                this.add_time = add_time;
            }

            public boolean isIsJoin() {
                return isJoin;
            }

            public void setIsJoin(boolean isJoin) {
                this.isJoin = isJoin;
            }

            public boolean isIsApply() {
                return isApply;
            }

            public void setIsApply(boolean isApply) {
                this.isApply = isApply;
            }

            public List<ImgsBean> getImgs() {
                return imgs;
            }

            public void setImgs(List<ImgsBean> imgs) {
                this.imgs = imgs;
            }

            public static class ImgsBean {
                /**
                 * id : 81206da1779e4277b37b4e8aa22e2303
                 * img_url : http://192.168.0.248:8080/oss/image/community/f8966d130b20cb5f5b36bbbe3e5497a8.jpeg
                 * img_code : efb47f23107d47de9541ac0f2566355d
                 * sort : 1
                 * add_time : 1523349958
                 */

                private String id;
                private String img_url;
                private String img_code;
                private int sort;
                private int add_time;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getImg_url() {
                    return img_url;
                }

                public void setImg_url(String img_url) {
                    this.img_url = img_url;
                }

                public String getImg_code() {
                    return img_code;
                }

                public void setImg_code(String img_code) {
                    this.img_code = img_code;
                }

                public int getSort() {
                    return sort;
                }

                public void setSort(int sort) {
                    this.sort = sort;
                }

                public int getAdd_time() {
                    return add_time;
                }

                public void setAdd_time(int add_time) {
                    this.add_time = add_time;
                }
            }
        }
    }
}
