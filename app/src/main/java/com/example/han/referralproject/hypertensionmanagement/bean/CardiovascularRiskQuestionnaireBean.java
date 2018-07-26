package com.example.han.referralproject.hypertensionmanagement.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 原发性高血压获取问卷
 * Created by lenovo on 2018/7/26.
 */

public class CardiovascularRiskQuestionnaireBean implements Serializable {


    /**
     * code : 0
     * data : {"hmQuestionnaireId":"string","questionList":[{"answerList":[{"answerInfo":"string","answerScore":0,"hmAnswerId":"string","hmQuestionId":"string","hmQuestionnaireId":"string","seq":0}],"hmQuestionId":"string","hmQuestionnaireId":"string","questionInfo":"string","questionName":"string","questionType":"string","seq":0}],"questionnaireInfo":"string","questionnaireName":"string","questionnaireScore":0}
     * message : {}
     * tag : false
     */

    public int code;
    public DataBean data;
    public MessageBean message;
    public boolean tag;

    public static class DataBean {
        /**
         * hmQuestionnaireId : string
         * questionList : [{"answerList":[{"answerInfo":"string","answerScore":0,"hmAnswerId":"string","hmQuestionId":"string","hmQuestionnaireId":"string","seq":0}],"hmQuestionId":"string","hmQuestionnaireId":"string","questionInfo":"string","questionName":"string","questionType":"string","seq":0}]
         * questionnaireInfo : string
         * questionnaireName : string
         * questionnaireScore : 0
         */

        public String hmQuestionnaireId;
        public String questionnaireInfo;
        public String questionnaireName;
        public int questionnaireScore;
        public List<QuestionListBean> questionList;

        public static class QuestionListBean {
            /**
             * answerList : [{"answerInfo":"string","answerScore":0,"hmAnswerId":"string","hmQuestionId":"string","hmQuestionnaireId":"string","seq":0}]
             * hmQuestionId : string
             * hmQuestionnaireId : string
             * questionInfo : string
             * questionName : string
             * questionType : string
             * seq : 0
             */

            public String hmQuestionId;
            public String hmQuestionnaireId;

            public static class AnswerListBean {
            }
        }
    }

    public static class MessageBean {
    }
}
