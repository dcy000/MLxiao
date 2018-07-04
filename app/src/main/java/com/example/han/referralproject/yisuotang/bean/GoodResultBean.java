package com.example.han.referralproject.yisuotang.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2018/7/4.
 */

public class GoodResultBean implements Serializable {

    /**
     * tag : true
     * code : 200
     * data : [{"mallProductTypeId":1,"fatherId":null,"name":"试纸类","showStatus":0},{"mallProductTypeId":2,"fatherId":null,"name":"药物类","showStatus":0},{"mallProductTypeId":3,"fatherId":null,"name":"保健类","showStatus":0},{"mallProductTypeId":4,"fatherId":null,"name":"瘦身类","showStatus":0},{"mallProductTypeId":5,"fatherId":null,"name":"仪器类","showStatus":0},{"mallProductTypeId":6,"fatherId":null,"name":"设备类","showStatus":0},{"mallProductTypeId":13,"fatherId":0,"name":"器械","showStatus":0}]
     * message : 成功
     */

    public boolean tag;
    public int code;
    public String message;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * mallProductTypeId : 1
         * fatherId : null
         * name : 试纸类
         * showStatus : 0
         */

        public int mallProductTypeId;
        public Object fatherId;
        public String name;
        public int showStatus;
    }
}
