package com.yidankeji.cheng.ebuyhouse.housemodule.mode;

import java.util.List;

/**
 * Created by Administrator on 2018\1\2 0002.
 */

public class HouseDetailsMode {

    /**
     * message : 操作成功
     * state : 1
     * content : {"data":{"customer_phone_number":"","category_name":"主卧","is_enable":2,"latitude":34.10329,"origin":"app","fk_city_id":"8329","description":"uk saidjmk andqj anddmjqm","remark":null,"fk_state_id":"NY","fk_customer_id":"7fdd30b2e05c407f8cabdd4edc3af64f","is_collect":true,"update_time":1514514603,"city_name":"New York","state_name":"NewYork","price":"5,774.00","street":"234 N D St, San Bernardino, CA 92401","contact":[],"id":"000c6d6a4a4345e8b11957eb52aadb68","kitchen":4,"apn":"cd7577b5e7c944f6aaeb58919e7b2a12","bathroom":1,"longitude":-117.2923,"property_price":"150.00","zip":"92401","living_sqft":500,"joinTime":"23","img_code":["https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2950854151,876158563&fm=200&gp=0.jpg"],"customer_head_url":"http://img3.imgtn.bdimg.com/it/u=2954056395,1419608032&amp;fm=27&amp;gp=0.jpg","release_type":"sale","year_build":600,"bedroom":4,"check_status":2,"lot_sqft":500,"cap_rate":null,"fk_category_id":"1","img_url":"http://imgdev.yidankeji.com/ebuyhouse/house/ed30945b8d9c42639e0758fc173b4967.jpg","customer_nick_name":"lizhun@yidankeji.com","customer_email":"lizhun@yidankeji.com","add_time":1514514603},"rows":[]}
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
         * data : {"customer_phone_number":"","category_name":"主卧","is_enable":2,"latitude":34.10329,"origin":"app","fk_city_id":"8329","description":"uk saidjmk andqj anddmjqm","remark":null,"fk_state_id":"NY","fk_customer_id":"7fdd30b2e05c407f8cabdd4edc3af64f","is_collect":true,"update_time":1514514603,"city_name":"New York","state_name":"NewYork","price":"5,774.00","street":"234 N D St, San Bernardino, CA 92401","contact":[],"id":"000c6d6a4a4345e8b11957eb52aadb68","kitchen":4,"apn":"cd7577b5e7c944f6aaeb58919e7b2a12","bathroom":1,"longitude":-117.2923,"property_price":"150.00","zip":"92401","living_sqft":500,"joinTime":"23","img_code":["https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2950854151,876158563&fm=200&gp=0.jpg"],"customer_head_url":"http://img3.imgtn.bdimg.com/it/u=2954056395,1419608032&amp;fm=27&amp;gp=0.jpg","release_type":"sale","year_build":600,"bedroom":4,"check_status":2,"lot_sqft":500,"cap_rate":null,"fk_category_id":"1","img_url":"http://imgdev.yidankeji.com/ebuyhouse/house/ed30945b8d9c42639e0758fc173b4967.jpg","customer_nick_name":"lizhun@yidankeji.com","customer_email":"lizhun@yidankeji.com","add_time":1514514603}
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
             * customer_phone_number :
             * category_name : 主卧
             * is_enable : 2
             * latitude : 34.10329
             * origin : app
             * fk_city_id : 8329
             * description : uk saidjmk andqj anddmjqm
             * remark : null
             * fk_state_id : NY
             * fk_customer_id : 7fdd30b2e05c407f8cabdd4edc3af64f
             * is_collect : true
             * update_time : 1514514603
             * city_name : New York
             * state_name : NewYork
             * price : 5,774.00
             * street : 234 N D St, San Bernardino, CA 92401
             * contact : []
             * id : 000c6d6a4a4345e8b11957eb52aadb68
             * kitchen : 4
             * apn : cd7577b5e7c944f6aaeb58919e7b2a12
             * bathroom : 1
             * longitude : -117.2923
             * property_price : 150.00
             * zip : 92401
             * living_sqft : 500
             * joinTime : 23
             * img_code : ["https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2950854151,876158563&fm=200&gp=0.jpg"]
             * customer_head_url : http://img3.imgtn.bdimg.com/it/u=2954056395,1419608032&amp;fm=27&amp;gp=0.jpg
             * release_type : sale
             * year_build : 600
             * bedroom : 4
             * check_status : 2
             * lot_sqft : 500
             * cap_rate : null
             * fk_category_id : 1
             * img_url : http://imgdev.yidankeji.com/ebuyhouse/house/ed30945b8d9c42639e0758fc173b4967.jpg
             * customer_nick_name : lizhun@yidankeji.com
             * customer_email : lizhun@yidankeji.com
             * add_time : 1514514603
             */

            private String customer_phone_number;
            private String category_name;
            private String is_enable;
            private String latitude;
            private String origin;
            private String fk_city_id;
            private String description;
            private Object remark;
            private String fk_state_id;
            private String fk_customer_id;
            private boolean is_collect;
            private String update_time;
            private String city_name;
            private String state_name;
            private String price;
            private String street;
            private String id;
            private String kitchen;
            private String apn;
            private String bathroom;
            private String longitude;
            private String property_price;
            private String zip;
            private String living_sqft;
            private String joinTime;
            private String customer_head_url;
            private String release_type;
            private String year_build;
            private String bedroom;
            private String check_status;
            private String lot_sqft;
            private Object cap_rate;
            private String fk_category_id;
            private String img_url;
            private String customer_nick_name;
            private String customer_email;
            private String add_time;
            private List<?> contact;
            private List<String> img_code;

            public String getCustomer_phone_number() {
                return customer_phone_number;
            }

            public void setCustomer_phone_number(String customer_phone_number) {
                this.customer_phone_number = customer_phone_number;
            }

            public String getCategory_name() {
                return category_name;
            }

            public void setCategory_name(String category_name) {
                this.category_name = category_name;
            }

            public String getIs_enable() {
                return is_enable;
            }

            public void setIs_enable(String is_enable) {
                this.is_enable = is_enable;
            }

            public String getLatitude() {
                return latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            public String getOrigin() {
                return origin;
            }

            public void setOrigin(String origin) {
                this.origin = origin;
            }

            public String getFk_city_id() {
                return fk_city_id;
            }

            public void setFk_city_id(String fk_city_id) {
                this.fk_city_id = fk_city_id;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public Object getRemark() {
                return remark;
            }

            public void setRemark(Object remark) {
                this.remark = remark;
            }

            public String getFk_state_id() {
                return fk_state_id;
            }

            public void setFk_state_id(String fk_state_id) {
                this.fk_state_id = fk_state_id;
            }

            public String getFk_customer_id() {
                return fk_customer_id;
            }

            public void setFk_customer_id(String fk_customer_id) {
                this.fk_customer_id = fk_customer_id;
            }

            public boolean isIs_collect() {
                return is_collect;
            }

            public void setIs_collect(boolean is_collect) {
                this.is_collect = is_collect;
            }

            public String getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(String update_time) {
                this.update_time = update_time;
            }

            public String getCity_name() {
                return city_name;
            }

            public void setCity_name(String city_name) {
                this.city_name = city_name;
            }

            public String getState_name() {
                return state_name;
            }

            public void setState_name(String state_name) {
                this.state_name = state_name;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getStreet() {
                return street;
            }

            public void setStreet(String street) {
                this.street = street;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getKitchen() {
                return kitchen;
            }

            public void setKitchen(String kitchen) {
                this.kitchen = kitchen;
            }

            public String getApn() {
                return apn;
            }

            public void setApn(String apn) {
                this.apn = apn;
            }

            public String getBathroom() {
                return bathroom;
            }

            public void setBathroom(String bathroom) {
                this.bathroom = bathroom;
            }

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            public String getProperty_price() {
                return property_price;
            }

            public void setProperty_price(String property_price) {
                this.property_price = property_price;
            }

            public String getZip() {
                return zip;
            }

            public void setZip(String zip) {
                this.zip = zip;
            }

            public String getLiving_sqft() {
                return living_sqft;
            }

            public void setLiving_sqft(String living_sqft) {
                this.living_sqft = living_sqft;
            }

            public String getJoinTime() {
                return joinTime;
            }

            public void setJoinTime(String joinTime) {
                this.joinTime = joinTime;
            }

            public String getCustomer_head_url() {
                return customer_head_url;
            }

            public void setCustomer_head_url(String customer_head_url) {
                this.customer_head_url = customer_head_url;
            }

            public String getRelease_type() {
                return release_type;
            }

            public void setRelease_type(String release_type) {
                this.release_type = release_type;
            }

            public String getYear_build() {
                return year_build;
            }

            public void setYear_build(String year_build) {
                this.year_build = year_build;
            }

            public String getBedroom() {
                return bedroom;
            }

            public void setBedroom(String bedroom) {
                this.bedroom = bedroom;
            }

            public String getCheck_status() {
                return check_status;
            }

            public void setCheck_status(String check_status) {
                this.check_status = check_status;
            }

            public String getLot_sqft() {
                return lot_sqft;
            }

            public void setLot_sqft(String lot_sqft) {
                this.lot_sqft = lot_sqft;
            }

            public Object getCap_rate() {
                return cap_rate;
            }

            public void setCap_rate(Object cap_rate) {
                this.cap_rate = cap_rate;
            }

            public String getFk_category_id() {
                return fk_category_id;
            }

            public void setFk_category_id(String fk_category_id) {
                this.fk_category_id = fk_category_id;
            }

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public String getCustomer_nick_name() {
                return customer_nick_name;
            }

            public void setCustomer_nick_name(String customer_nick_name) {
                this.customer_nick_name = customer_nick_name;
            }

            public String getCustomer_email() {
                return customer_email;
            }

            public void setCustomer_email(String customer_email) {
                this.customer_email = customer_email;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }

            public List<?> getContact() {
                return contact;
            }

            public void setContact(List<?> contact) {
                this.contact = contact;
            }

            public List<String> getImg_code() {
                return img_code;
            }

            public void setImg_code(List<String> img_code) {
                this.img_code = img_code;
            }
        }
    }
}
