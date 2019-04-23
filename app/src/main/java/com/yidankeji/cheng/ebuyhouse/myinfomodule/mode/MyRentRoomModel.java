package com.yidankeji.cheng.ebuyhouse.myinfomodule.mode;

import java.util.List;

/**
 * Created by ${syj} on 2018/3/14.
 */

public class MyRentRoomModel {

    /**
     * content : {"data":{},"rows":[{"id":"c054b185e3e3424a9616935e6af53574","fk_customer_id":"6a3d9b8a64ae4987ab2bec67ffc61773","fk_city_id":"15053","fk_state_id":"LA","fk_category_id":"feb4586f2e054dd9a1612590f2f0d24f","price":"46.00","property_price":"14.00","cap_rate":"","mls":"132584","apn":"163794958","street":"LA-675, New Iberia, LA 70560","zip":"70560","bedroom":13,"bathroom":"14","garage":16,"kitchen":17,"lot_sqft":1,"living_sqft":15,"latitude":29.99391,"longitude":-91.918226,"year_build":1994,"video_url":"","img_url":"http://ebuyhouse.oss-us-west-1.aliyuncs.com/image/house/e3e2573ac09709bb170dfa334701d5f5.jpeg","img_code":["http://ebuyhouse.oss-us-west-1.aliyuncs.com/image/house/e3e2573ac09709bb170dfa334701d5f5.jpeg","http://ebuyhouse.oss-us-west-1.aliyuncs.com/image/house/621f5d6b3a510a5b18e2960ce1436757.jpeg"],"remark":"","is_enable":1,"check_msg":"","check_status":1,"update_time":1521010451,"origin":"app","add_time":1521010451,"is_sale":0,"release_type":"rent","video_first_pic":"null","release_method":"","rent_payment":"Half year","deposit":13,"is_collect":false,"city_name":"New Iberia","state_name":"Louisiana","simple_price":"46","category_name":"House","description":"Jfnnf","contact":[{"contact_id":"c8496fe6b5c24dfab2a1b3f3dfdf8215","name":"lizhun@yidankeji.com","phone_number":"16794382541"}],"extAttr":[{"attr_key_id":"1","attr_key_name":"Flooring","attr_value_list":[{"value_id":"1","value_name":"Carpet"},{"value_id":"4","value_name":"Marble"}]}],"customer_phone_number":"","customer_nick_name":"","joinTime":"","customer_email":"","customer_head_url":""}]}
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
         * data : {}
         * rows : [{"id":"c054b185e3e3424a9616935e6af53574","fk_customer_id":"6a3d9b8a64ae4987ab2bec67ffc61773","fk_city_id":"15053","fk_state_id":"LA","fk_category_id":"feb4586f2e054dd9a1612590f2f0d24f","price":"46.00","property_price":"14.00","cap_rate":"","mls":"132584","apn":"163794958","street":"LA-675, New Iberia, LA 70560","zip":"70560","bedroom":13,"bathroom":"14","garage":16,"kitchen":17,"lot_sqft":1,"living_sqft":15,"latitude":29.99391,"longitude":-91.918226,"year_build":1994,"video_url":"","img_url":"http://ebuyhouse.oss-us-west-1.aliyuncs.com/image/house/e3e2573ac09709bb170dfa334701d5f5.jpeg","img_code":["http://ebuyhouse.oss-us-west-1.aliyuncs.com/image/house/e3e2573ac09709bb170dfa334701d5f5.jpeg","http://ebuyhouse.oss-us-west-1.aliyuncs.com/image/house/621f5d6b3a510a5b18e2960ce1436757.jpeg"],"remark":"","is_enable":1,"check_msg":"","check_status":1,"update_time":1521010451,"origin":"app","add_time":1521010451,"is_sale":0,"release_type":"rent","video_first_pic":"null","release_method":"","rent_payment":"Half year","deposit":13,"is_collect":false,"city_name":"New Iberia","state_name":"Louisiana","simple_price":"46","category_name":"House","description":"Jfnnf","contact":[{"contact_id":"c8496fe6b5c24dfab2a1b3f3dfdf8215","name":"lizhun@yidankeji.com","phone_number":"16794382541"}],"extAttr":[{"attr_key_id":"1","attr_key_name":"Flooring","attr_value_list":[{"value_id":"1","value_name":"Carpet"},{"value_id":"4","value_name":"Marble"}]}],"customer_phone_number":"","customer_nick_name":"","joinTime":"","customer_email":"","customer_head_url":""}]
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
             * id : c054b185e3e3424a9616935e6af53574
             * fk_customer_id : 6a3d9b8a64ae4987ab2bec67ffc61773
             * fk_city_id : 15053
             * fk_state_id : LA
             * fk_category_id : feb4586f2e054dd9a1612590f2f0d24f
             * price : 46.00
             * property_price : 14.00
             * cap_rate :
             * mls : 132584
             * apn : 163794958
             * street : LA-675, New Iberia, LA 70560
             * zip : 70560
             * bedroom : 13
             * bathroom : 14
             * garage : 16
             * kitchen : 17
             * lot_sqft : 1
             * living_sqft : 15
             * latitude : 29.99391
             * longitude : -91.918226
             * year_build : 1994
             * video_url :
             * img_url : http://ebuyhouse.oss-us-west-1.aliyuncs.com/image/house/e3e2573ac09709bb170dfa334701d5f5.jpeg
             * img_code : ["http://ebuyhouse.oss-us-west-1.aliyuncs.com/image/house/e3e2573ac09709bb170dfa334701d5f5.jpeg","http://ebuyhouse.oss-us-west-1.aliyuncs.com/image/house/621f5d6b3a510a5b18e2960ce1436757.jpeg"]
             * remark :
             * is_enable : 1
             * check_msg :
             * check_status : 1
             * update_time : 1521010451
             * origin : app
             * add_time : 1521010451
             * is_sale : 0
             * release_type : rent
             * video_first_pic : null
             * release_method :
             * rent_payment : Half year
             * deposit : 13
             * is_collect : false
             * city_name : New Iberia
             * state_name : Louisiana
             * simple_price : 46
             * category_name : House
             * description : Jfnnf
             * contact : [{"contact_id":"c8496fe6b5c24dfab2a1b3f3dfdf8215","name":"lizhun@yidankeji.com","phone_number":"16794382541"}]
             * extAttr : [{"attr_key_id":"1","attr_key_name":"Flooring","attr_value_list":[{"value_id":"1","value_name":"Carpet"},{"value_id":"4","value_name":"Marble"}]}]
             * customer_phone_number :
             * customer_nick_name :
             * joinTime :
             * customer_email :
             * customer_head_url :
             */

            private String id;
            private String fk_customer_id;
            private String fk_city_id;
            private String fk_state_id;
            private String fk_category_id;
            private String price;
            private String property_price;
            private String cap_rate;
            private String mls;
            private String apn;
            private String street;
            private String zip;
            private String bedroom;
            private String bathroom;
            private String garage; //11
            private String kitchen;//11
            private String lot_sqft;  //11
            private String living_sqft;  //22
            private Double latitude;
            private Double longitude;
            private int year_build;
            private String video_url;
            private String img_url;
            private String remark;
            private Double is_enable;
            private String check_msg;
            private Object check_status;
            private int update_time;
            private String origin;
            private Object add_time;
            private Object is_sale;
            private String release_type;
            private String video_first_pic;
            private String release_method;
            private String rent_payment;
            private String deposit;  //22
            private boolean is_collect;
            private String city_name;
            private String state_name;
            private String simple_price;
            private String category_name;
            private String description;
            private String customer_phone_number;
            private String customer_nick_name;
            private String joinTime;
            private String customer_email;
            private String customer_head_url;
            private List<String> img_code;
            private List<ContactBean> contact;
            private List<ExtAttrBean> extAttr;
            private boolean isOpen;

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

            public String getFk_category_id() {
                return fk_category_id;
            }

            public void setFk_category_id(String fk_category_id) {
                this.fk_category_id = fk_category_id;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getProperty_price() {
                return property_price;
            }

            public void setProperty_price(String property_price) {
                this.property_price = property_price;
            }

            public String getCap_rate() {
                return cap_rate;
            }

            public void setCap_rate(String cap_rate) {
                this.cap_rate = cap_rate;
            }

            public String getMls() {
                return mls;
            }

            public void setMls(String mls) {
                this.mls = mls;
            }

            public String getApn() {
                return apn;
            }

            public void setApn(String apn) {
                this.apn = apn;
            }

            public String getStreet() {
                return street;
            }

            public void setStreet(String street) {
                this.street = street;
            }

            public String getZip() {
                return zip;
            }

            public void setZip(String zip) {
                this.zip = zip;
            }

            public String getBedroom() {
                return bedroom;
            }

            public void setBedroom(String bedroom) {
                this.bedroom = bedroom;
            }

            public String getBathroom() {
                return bathroom;
            }

            public void setBathroom(String bathroom) {
                this.bathroom = bathroom;
            }

            public String getGarage() {
                return garage;
            }

            public void setGarage(String garage) {
                this.garage = garage;
            }

            public String getKitchen() {
                return kitchen;
            }

            public void setKitchen(String kitchen) {
                this.kitchen = kitchen;
            }

            public String getLot_sqft() {
                return lot_sqft;
            }

            public void setLot_sqft(String lot_sqft) {
                this.lot_sqft = lot_sqft;
            }

            public String getLiving_sqft() {
                return living_sqft;
            }

            public void setLiving_sqft(String living_sqft) {
                this.living_sqft = living_sqft;
            }

            public Double getLatitude() {
                return latitude;
            }

            public void setLatitude(Double latitude) {
                this.latitude = latitude;
            }

            public Double getLongitude() {
                return longitude;
            }

            public void setLongitude(Double longitude) {
                this.longitude = longitude;
            }

            public int getYear_build() {
                return year_build;
            }

            public void setYear_build(int year_build) {
                this.year_build = year_build;
            }

            public String getVideo_url() {
                return video_url;
            }

            public void setVideo_url(String video_url) {
                this.video_url = video_url;
            }

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public Double getIs_enable() {
                return is_enable;
            }

            public void setIs_enable(Double is_enable) {
                this.is_enable = is_enable;
            }

            public String getCheck_msg() {
                return check_msg;
            }

            public void setCheck_msg(String check_msg) {
                this.check_msg = check_msg;
            }

            public Object getCheck_status() {
                return check_status;
            }

            public void setCheck_status(Object check_status) {
                this.check_status = check_status;
            }

            public int getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(int update_time) {
                this.update_time = update_time;
            }

            public String getOrigin() {
                return origin;
            }

            public void setOrigin(String origin) {
                this.origin = origin;
            }

            public Object getAdd_time() {
                return add_time;
            }

            public void setAdd_time(Object add_time) {
                this.add_time = add_time;
            }

            public Object getIs_sale() {
                return is_sale;
            }

            public void setIs_sale(Object is_sale) {
                this.is_sale = is_sale;
            }

            public String getRelease_type() {
                return release_type;
            }

            public void setRelease_type(String release_type) {
                this.release_type = release_type;
            }

            public String getVideo_first_pic() {
                return video_first_pic;
            }

            public void setVideo_first_pic(String video_first_pic) {
                this.video_first_pic = video_first_pic;
            }

            public String getRelease_method() {
                return release_method;
            }

            public void setRelease_method(String release_method) {
                this.release_method = release_method;
            }

            public String getRent_payment() {
                return rent_payment;
            }

            public void setRent_payment(String rent_payment) {
                this.rent_payment = rent_payment;
            }

            public String getDeposit() {
                return deposit;
            }

            public void setDeposit(String deposit) {
                this.deposit = deposit;
            }

            public boolean is_collect() {
                return is_collect;
            }

            public void setIs_collect(boolean is_collect) {
                this.is_collect = is_collect;
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

            public String getSimple_price() {
                return simple_price;
            }

            public void setSimple_price(String simple_price) {
                this.simple_price = simple_price;
            }

            public String getCategory_name() {
                return category_name;
            }

            public void setCategory_name(String category_name) {
                this.category_name = category_name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getCustomer_phone_number() {
                return customer_phone_number;
            }

            public void setCustomer_phone_number(String customer_phone_number) {
                this.customer_phone_number = customer_phone_number;
            }

            public String getCustomer_nick_name() {
                return customer_nick_name;
            }

            public void setCustomer_nick_name(String customer_nick_name) {
                this.customer_nick_name = customer_nick_name;
            }

            public String getJoinTime() {
                return joinTime;
            }

            public void setJoinTime(String joinTime) {
                this.joinTime = joinTime;
            }

            public String getCustomer_email() {
                return customer_email;
            }

            public void setCustomer_email(String customer_email) {
                this.customer_email = customer_email;
            }

            public String getCustomer_head_url() {
                return customer_head_url;
            }

            public void setCustomer_head_url(String customer_head_url) {
                this.customer_head_url = customer_head_url;
            }

            public List<String> getImg_code() {
                return img_code;
            }

            public void setImg_code(List<String> img_code) {
                this.img_code = img_code;
            }

            public List<ContactBean> getContact() {
                return contact;
            }

            public void setContact(List<ContactBean> contact) {
                this.contact = contact;
            }

            public List<ExtAttrBean> getExtAttr() {
                return extAttr;
            }

            public void setExtAttr(List<ExtAttrBean> extAttr) {
                this.extAttr = extAttr;
            }

            public boolean isOpen() {
                return isOpen;
            }

            public void setOpen(boolean open) {
                isOpen = open;
            }

            public static class ContactBean {
                /**
                 * contact_id : c8496fe6b5c24dfab2a1b3f3dfdf8215
                 * name : lizhun@yidankeji.com
                 * phone_number : 16794382541
                 */

                private String contact_id;
                private String name;
                private String phone_number;

                public String getContact_id() {
                    return contact_id;
                }

                public void setContact_id(String contact_id) {
                    this.contact_id = contact_id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getPhone_number() {
                    return phone_number;
                }

                public void setPhone_number(String phone_number) {
                    this.phone_number = phone_number;
                }
            }

            public static class ExtAttrBean {
                /**
                 * attr_key_id : 1
                 * attr_key_name : Flooring
                 * attr_value_list : [{"value_id":"1","value_name":"Carpet"},{"value_id":"4","value_name":"Marble"}]
                 */

                private String attr_key_id;
                private String attr_key_name;
                private List<AttrValueListBean> attr_value_list;

                public String getAttr_key_id() {
                    return attr_key_id;
                }

                public void setAttr_key_id(String attr_key_id) {
                    this.attr_key_id = attr_key_id;
                }

                public String getAttr_key_name() {
                    return attr_key_name;
                }

                public void setAttr_key_name(String attr_key_name) {
                    this.attr_key_name = attr_key_name;
                }

                public List<AttrValueListBean> getAttr_value_list() {
                    return attr_value_list;
                }

                public void setAttr_value_list(List<AttrValueListBean> attr_value_list) {
                    this.attr_value_list = attr_value_list;
                }

                public static class AttrValueListBean {
                    /**
                     * value_id : 1
                     * value_name : Carpet
                     */

                    private String value_id;
                    private String value_name;

                    public String getValue_id() {
                        return value_id;
                    }

                    public void setValue_id(String value_id) {
                        this.value_id = value_id;
                    }

                    public String getValue_name() {
                        return value_name;
                    }

                    public void setValue_name(String value_name) {
                        this.value_name = value_name;
                    }
                }
            }
        }
    }
}
