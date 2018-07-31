package com.example.han.referralproject.hypertensionmanagement.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 原发性高血压提交数据
 * Created by lenovo on 2018/7/26.
 */

public class PrimaryHypertensionBean implements Serializable {

    /**
     * answerList : [{"answerName":"string","hmAnswerId":"string","hmQuestionId":"string","questionName":"string"}]
     * equipmentId : string
     * hmQuestionnaireAssessId : string
     * hmQuestionnaireId : string
     * hmQuestionnaireName : string
     * score : 0
     * userId : 0
     */

    public String equipmentId;
    public String hmQuestionnaireAssessId;
    public String hmQuestionnaireId;
    public String hmQuestionnaireName;
    public Integer score=0;
    public String userId;
    public List<AnswerListBean> answerList;

    public static class AnswerListBean implements Serializable {
        /**
         * answerName : string
         * hmAnswerId : string
         * hmQuestionId : string
         * questionName : string
         */

        public String answerName;
        public String hmAnswerId;
        public String hmQuestionId;
        public String questionName;
        public int answerScore;
    }
}
