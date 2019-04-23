package com.yidankeji.cheng.ebuyhouse.housemodule.mode;

import java.util.List;

/**
 * Created by ${syj} on 2018/3/14.
 */

public class PostRentListModel {

    /**
     * content : {"data":{},"rows":[{"attr_key_id":"1","attr_key_name":"Flooring","attr_is_multiple":1,"attr_value_list":[{"value_id":"1","value_name":"Carpet"},{"value_id":"4","value_name":"Marble"},{"value_id":"3","value_name":"Tile"},{"value_id":"2","value_name":"Hardwood"}]},{"attr_key_id":"2","attr_key_name":"Internal facilities","attr_is_multiple":1,"attr_value_list":[{"value_id":"5","value_name":"Fireplace"},{"value_id":"6","value_name":"Air-conditioner"},{"value_id":"7","value_name":"Whole house fan"},{"value_id":"8","value_name":"Jetted tub"}]},{"attr_key_id":"3","attr_key_name":"Features","attr_is_multiple":1,"attr_value_list":[{"value_id":"9","value_name":"Big bedroom"},{"value_id":"15","value_name":"Wine cellar"},{"value_id":"10","value_name":"Big window"},{"value_id":"11","value_name":"Big balcony"},{"value_id":"12","value_name":"Sauna room"},{"value_id":"13","value_name":"Study"},{"value_id":"14","value_name":"Home theatre"},{"value_id":"16","value_name":"Betweenmaid dormitory"}]},{"attr_key_id":"4","attr_key_name":"Decoration","attr_is_multiple":1,"attr_value_list":[{"value_id":"18","value_name":"New"},{"value_id":"17","value_name":"Luxury"},{"value_id":"19","value_name":"Simple"}]},{"attr_key_id":"5","attr_key_name":"Building Amenities","attr_is_multiple":1,"attr_value_list":[{"value_id":"25","value_name":"Backyard"},{"value_id":"20","value_name":"Lake/Pond"},{"value_id":"21","value_name":"Green area"},{"value_id":"22","value_name":"Private tennis court"},{"value_id":"24","value_name":"Private garden"},{"value_id":"23","value_name":"Private swimming pool"}]},{"attr_key_id":"6","attr_key_name":"Community and neighborhood","attr_is_multiple":1,"attr_value_list":[{"value_id":"42","value_name":"Public theatre"},{"value_id":"41","value_name":"Amusement park"},{"value_id":"40","value_name":"Yacht"},{"value_id":"38","value_name":"Bar"},{"value_id":"39","value_name":"Public swimming pool"},{"value_id":"37","value_name":"Gym"},{"value_id":"36","value_name":"Golf court"},{"value_id":"27","value_name":"Mall"},{"value_id":"28","value_name":"Supermarket"},{"value_id":"29","value_name":"Park"},{"value_id":"30","value_name":"Hospital"},{"value_id":"31","value_name":"School"},{"value_id":"32","value_name":"Post office"},{"value_id":"33","value_name":"Bus Station"},{"value_id":"34","value_name":"Football court"},{"value_id":"35","value_name":"Tennis court"},{"value_id":"26","value_name":"Parking lot"}]}]}
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
         * rows : [{"attr_key_id":"1","attr_key_name":"Flooring","attr_is_multiple":1,"attr_value_list":[{"value_id":"1","value_name":"Carpet"},{"value_id":"4","value_name":"Marble"},{"value_id":"3","value_name":"Tile"},{"value_id":"2","value_name":"Hardwood"}]},{"attr_key_id":"2","attr_key_name":"Internal facilities","attr_is_multiple":1,"attr_value_list":[{"value_id":"5","value_name":"Fireplace"},{"value_id":"6","value_name":"Air-conditioner"},{"value_id":"7","value_name":"Whole house fan"},{"value_id":"8","value_name":"Jetted tub"}]},{"attr_key_id":"3","attr_key_name":"Features","attr_is_multiple":1,"attr_value_list":[{"value_id":"9","value_name":"Big bedroom"},{"value_id":"15","value_name":"Wine cellar"},{"value_id":"10","value_name":"Big window"},{"value_id":"11","value_name":"Big balcony"},{"value_id":"12","value_name":"Sauna room"},{"value_id":"13","value_name":"Study"},{"value_id":"14","value_name":"Home theatre"},{"value_id":"16","value_name":"Betweenmaid dormitory"}]},{"attr_key_id":"4","attr_key_name":"Decoration","attr_is_multiple":1,"attr_value_list":[{"value_id":"18","value_name":"New"},{"value_id":"17","value_name":"Luxury"},{"value_id":"19","value_name":"Simple"}]},{"attr_key_id":"5","attr_key_name":"Building Amenities","attr_is_multiple":1,"attr_value_list":[{"value_id":"25","value_name":"Backyard"},{"value_id":"20","value_name":"Lake/Pond"},{"value_id":"21","value_name":"Green area"},{"value_id":"22","value_name":"Private tennis court"},{"value_id":"24","value_name":"Private garden"},{"value_id":"23","value_name":"Private swimming pool"}]},{"attr_key_id":"6","attr_key_name":"Community and neighborhood","attr_is_multiple":1,"attr_value_list":[{"value_id":"42","value_name":"Public theatre"},{"value_id":"41","value_name":"Amusement park"},{"value_id":"40","value_name":"Yacht"},{"value_id":"38","value_name":"Bar"},{"value_id":"39","value_name":"Public swimming pool"},{"value_id":"37","value_name":"Gym"},{"value_id":"36","value_name":"Golf court"},{"value_id":"27","value_name":"Mall"},{"value_id":"28","value_name":"Supermarket"},{"value_id":"29","value_name":"Park"},{"value_id":"30","value_name":"Hospital"},{"value_id":"31","value_name":"School"},{"value_id":"32","value_name":"Post office"},{"value_id":"33","value_name":"Bus Station"},{"value_id":"34","value_name":"Football court"},{"value_id":"35","value_name":"Tennis court"},{"value_id":"26","value_name":"Parking lot"}]}]
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
             * attr_key_id : 1
             * attr_key_name : Flooring
             * attr_is_multiple : 1
             * attr_value_list : [{"value_id":"1","value_name":"Carpet"},{"value_id":"4","value_name":"Marble"},{"value_id":"3","value_name":"Tile"},{"value_id":"2","value_name":"Hardwood"}]
             */

            private String attr_key_id;
            private String attr_key_name;
            private int attr_is_multiple;
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

            public int getAttr_is_multiple() {
                return attr_is_multiple;
            }

            public void setAttr_is_multiple(int attr_is_multiple) {
                this.attr_is_multiple = attr_is_multiple;
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
private boolean isChose;

                public boolean isChose() {
                    return isChose;
                }

                public void setChose(boolean chose) {
                    isChose = chose;
                }

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
