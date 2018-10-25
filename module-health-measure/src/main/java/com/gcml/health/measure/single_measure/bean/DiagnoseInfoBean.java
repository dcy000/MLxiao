package com.gcml.health.measure.single_measure.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2018/7/27.
 */

public class DiagnoseInfoBean implements Serializable {

    /**
     * tag : true
     * code : 200
     * data : {"userId":null,"detectionDayCount":0,"dagerCount":null,"highPressure":null,"lowPressure":null,"primary":{"answerList":[{"hmQuestionId":"f7cbd6d2-e7fd-48ac-a27f-654a01055ba8","questionName":"是否有以下症状","hmAnswerId":"4ddcab28-6c03-46ab-ac8b-35d83bb4d0f4","answerName":"无","score":null},{"hmQuestionId":"f70fb6bd-3c03-4cad-9ed5-11e9c41596fe","questionName":"是否有靶器官损害的临床症状","hmAnswerId":"4b290927-1610-4080-a4a4-108993c909d8","answerName":"否","score":null},{"hmQuestionId":"c529fd11-6f44-4e6a-8a15-1e48f9ab8373","questionName":"是否有药物不良反应","hmAnswerId":"dfa7ae90-a7ce-4457-906a-bd1eaed31dc9","answerName":"否","score":null},{"hmQuestionId":"1b6a1b40-6f39-4bf7-ba16-8b8f89026ee6","questionName":"服药依从性","hmAnswerId":"ebff88fe-8377-4bd8-ad95-e0644b8b6e63","answerName":"偶尔服用","score":null}],"hmQuestionnaireId":"f3ad5781-3e6a-48e8-aef3-ffd1ea46df00","hmQuestionnaireName":"原发性高血压患者调查","hmQuestionnaireAssessId":null,"equipmentId":"1bca3d0263b30932","userId":100034,"score":0},"risk":null,"heart":null,"age":50,"sex":"男","weight":null,"height":170,"waistline":null,"bloodSugar":null,"hypertensionPrimaryState":"0","hypertensionTarget":null,"hypertensionLevel":null,"result":null}
     * message : 成功
     */

    public boolean tag;
    public int code;
    public DataBean data;
    public String message;

    public static class DataBean {
        /**
         * userId : null
         * detectionDayCount : 0
         * dagerCount : null
         * highPressure : null
         * lowPressure : null
         * primary : {"answerList":[{"hmQuestionId":"f7cbd6d2-e7fd-48ac-a27f-654a01055ba8","questionName":"是否有以下症状","hmAnswerId":"4ddcab28-6c03-46ab-ac8b-35d83bb4d0f4","answerName":"无","score":null},{"hmQuestionId":"f70fb6bd-3c03-4cad-9ed5-11e9c41596fe","questionName":"是否有靶器官损害的临床症状","hmAnswerId":"4b290927-1610-4080-a4a4-108993c909d8","answerName":"否","score":null},{"hmQuestionId":"c529fd11-6f44-4e6a-8a15-1e48f9ab8373","questionName":"是否有药物不良反应","hmAnswerId":"dfa7ae90-a7ce-4457-906a-bd1eaed31dc9","answerName":"否","score":null},{"hmQuestionId":"1b6a1b40-6f39-4bf7-ba16-8b8f89026ee6","questionName":"服药依从性","hmAnswerId":"ebff88fe-8377-4bd8-ad95-e0644b8b6e63","answerName":"偶尔服用","score":null}],"hmQuestionnaireId":"f3ad5781-3e6a-48e8-aef3-ffd1ea46df00","hmQuestionnaireName":"原发性高血压患者调查","hmQuestionnaireAssessId":null,"equipmentId":"1bca3d0263b30932","userId":100034,"score":0}
         * risk : null
         * heart : null
         * age : 50
         * sex : 男
         * weight : null
         * height : 170
         * waistline : null
         * bloodSugar : null
         * hypertensionPrimaryState : 0
         * hypertensionTarget : null
         * hypertensionLevel : null
         * result : null
         */

        public Object userId;
        public Integer detectionDayCount;
        public Integer dagerCount;
        public Integer highPressure;
        public Integer lowPressure;
        public PrimaryBean primary;
        public PrimaryBean risk;
        public PrimaryBean heart;
        public int age;
        public String sex;
        public Double weight;
        public int height;
        public Double waistline;
        public String bloodSugar;
        public String hypertensionPrimaryState;
        public String hypertensionTarget;
        public String hypertensionLevel;
        public String result;

        public static class PrimaryBean {
            /**
             * answerList : [{"hmQuestionId":"f7cbd6d2-e7fd-48ac-a27f-654a01055ba8","questionName":"是否有以下症状","hmAnswerId":"4ddcab28-6c03-46ab-ac8b-35d83bb4d0f4","answerName":"无","score":null},{"hmQuestionId":"f70fb6bd-3c03-4cad-9ed5-11e9c41596fe","questionName":"是否有靶器官损害的临床症状","hmAnswerId":"4b290927-1610-4080-a4a4-108993c909d8","answerName":"否","score":null},{"hmQuestionId":"c529fd11-6f44-4e6a-8a15-1e48f9ab8373","questionName":"是否有药物不良反应","hmAnswerId":"dfa7ae90-a7ce-4457-906a-bd1eaed31dc9","answerName":"否","score":null},{"hmQuestionId":"1b6a1b40-6f39-4bf7-ba16-8b8f89026ee6","questionName":"服药依从性","hmAnswerId":"ebff88fe-8377-4bd8-ad95-e0644b8b6e63","answerName":"偶尔服用","score":null}]
             * hmQuestionnaireId : f3ad5781-3e6a-48e8-aef3-ffd1ea46df00
             * hmQuestionnaireName : 原发性高血压患者调查
             * hmQuestionnaireAssessId : null
             * equipmentId : 1bca3d0263b30932
             * userId : 100034
             * score : 0
             */

            public String hmQuestionnaireId;
            public String hmQuestionnaireName;
            public Object hmQuestionnaireAssessId;
            public String equipmentId;
            public int userId;
            public int score;
            public List<AnswerListBean> answerList;

            public static class AnswerListBean {
                /**
                 * hmQuestionId : f7cbd6d2-e7fd-48ac-a27f-654a01055ba8
                 * questionName : 是否有以下症状
                 * hmAnswerId : 4ddcab28-6c03-46ab-ac8b-35d83bb4d0f4
                 * answerName : 无
                 * score : null
                 */

                public String hmQuestionId;
                public String questionName;
                public String hmAnswerId;
                public String answerName;
                public Object score;
            }
        }
    }
}
