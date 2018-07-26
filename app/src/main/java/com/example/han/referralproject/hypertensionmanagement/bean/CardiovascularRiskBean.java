package com.example.han.referralproject.hypertensionmanagement.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 原发性高血压获取问卷
 * Created by lenovo on 2018/7/26.
 */

public class CardiovascularRiskBean implements Serializable {


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
    public int score;
    public int userId;
    public List<AnswerListBean> answerList;

    public static class AnswerListBean {
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
    }
}
