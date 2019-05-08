package com.gcml.module_hypertension_manager.zhongyi.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2018/6/13.
 */

public class HealthManagementResultBean implements Serializable {

    /**
     * tag : true
     * code : 200
     * data : [{"constitutionName":"阴虚质","score":16,"questionList":[10,21,26,31],"oppositeList":null,"result":"否"},{"constitutionName":"气郁质","score":15,"questionList":[5,6,7,8],"oppositeList":null,"result":"否"},{"constitutionName":"平和质","score":15,"questionList":[1],"oppositeList":[2,4,5,13],"result":"否"},{"constitutionName":"特禀质","score":13,"questionList":[15,17,18,20],"oppositeList":null,"result":"否"},{"constitutionName":"气虚质","score":12,"questionList":[2,3,4,14],"oppositeList":null,"result":"否"},{"constitutionName":"阳虚质","score":12,"questionList":[11,12,13,29],"oppositeList":null,"result":"否"},{"constitutionName":"血瘀质","score":12,"questionList":[19,22,24,33],"oppositeList":null,"result":"否"},{"constitutionName":"痰湿质","score":11,"questionList":[9,16,28,32],"oppositeList":null,"result":"否"},{"constitutionName":"湿热质","score":11,"questionList":[23,25,27,30],"oppositeList":null,"result":"否"}]
     * message : 成功
     */

    public boolean tag;
    public int code;
    public String message;
    public List<DataBean> data;

    public static class DataBean implements Serializable{
        /**
         * constitutionName : 阴虚质
         * score : 16
         * questionList : [10,21,26,31]
         * oppositeList : null
         * result : 否
         */

        public String constitutionName;
        public int score;
        public Object oppositeList;
        public String result;
        public List<Integer> questionList;
    }
}
