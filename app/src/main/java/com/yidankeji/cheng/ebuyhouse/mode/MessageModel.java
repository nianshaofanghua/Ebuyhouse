package com.yidankeji.cheng.ebuyhouse.mode;

import java.util.List;

/**
 * Created by ${syj} on 2018/1/30.
 */

public class MessageModel {


    /**
     * message : 操作成功
     * state : 1
     * content : {"data":{"number2_time":1516875028,"number3_time":0,"number3":0,"number1":"0","number1_time":1517298178,"number2":0},"rows":[]}
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
         * data : {"number2_time":1516875028,"number3_time":0,"number3":0,"number1":"0","number1_time":1517298178,"number2":0}
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
             * number2_time : 1516875028
             * number3_time : 0
             * number3 : 0
             * number1 : 0
             * number1_time : 1517298178
             * number2 : 0
             */

            private Object number2_time;
            private Object number3_time;
            private int number3;
            private int number1;
            private Object number1_time;
            private int number2;

            public Object getNumber2_time() {
                return number2_time;
            }

            public void setNumber2_time(int number2_time) {
                this.number2_time = number2_time;
            }

            public Object getNumber3_time() {
                return number3_time;
            }

            public void setNumber3_time(int number3_time) {
                this.number3_time = number3_time;
            }

            public int getNumber3() {
                return number3;
            }

            public void setNumber3(int number3) {
                this.number3 = number3;
            }

            public int getNumber1() {
                return number1;
            }

            public void setNumber1(int number1) {
                this.number1 = number1;
            }

            public Object getNumber1_time() {
                return number1_time;
            }

            public void setNumber1_time(int number1_time) {
                this.number1_time = number1_time;
            }

            public int getNumber2() {
                return number2;
            }

            public void setNumber2(int number2) {
                this.number2 = number2;
            }
        }
    }
}
