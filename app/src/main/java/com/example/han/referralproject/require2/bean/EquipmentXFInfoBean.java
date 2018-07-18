package com.example.han.referralproject.require2.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 通过设备讯飞组Id的bean
 * Created by lenovo on 2018/7/18.
 */
public class EquipmentXFInfoBean implements Serializable {

    /**
     * code : 0
     * data : [{"groupId":"string","num":0}]
     * error : {}
     * message :
     * tag : false
     */

    public int code;
    public Object error;
    public String message;
    public boolean tag;
    public List<DataBean> data;


    public static class DataBean implements Serializable {
        /**
         * groupId : string
         * num : 0
         */

        public String groupId;
        public int num;
    }
}
