package com.example.han.referralproject.olderhealthmanagement.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2018/6/13.
 */

public class HealthManagementAnwserBean implements Serializable {

    /**
     * answerList : [{"answerScore":0,"hmAnswerId":"string","hmQuestionId":"string","questionSeq":0}]
     * equipmentId : string
     * hmQuestionnaireAssessId : string
     * hmQuestionnaireId : string
     * score : 0
     * userId : 0
     */

    public String equipmentId;
    public String hmQuestionnaireAssessId;
    public String hmQuestionnaireId;
    public int score;
    public String userId;
    public List<AnswerListBean> answerList;

    public static class AnswerListBean {
        /**
         * answerScore : 0
         * hmAnswerId : string
         * hmQuestionId : string
         * questionSeq : 0
         */

        public int answerScore;
        public String hmAnswerId;
        public String hmQuestionId;
        public int questionSeq;
    }
}
