package com.gcml.module_health_profile.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2019/2/28.
 */

public class TiZhiBean implements Serializable {

    /**
     * constitutionList : [{"constitutionName":"string","oppositeList":[0],"questionList":[0],"result":"string","score":0}]
     * createdBy : string
     * createdOn : 2019-02-28T06:06:32.962Z
     * deletionState : string
     * description : string
     * hiConstitutionResultId : string
     * modifiedBy : string
     * modifiedOn : 2019-02-28T06:06:32.962Z
     * questionnaire : {"answerList":[{"answerName":"string","hmAnswerId":"string","hmQuestionId":"string","questionName":"string","questionSeq":0,"score":0}],"equipmentId":"string","hmQuestionnaireId":"string","hmQuestionnaireName":"string","score":0,"userId":0}
     * seq : 0
     */

    public String createdBy;
    public String createdOn;
    public String deletionState;
    public String description;
    public String hiConstitutionResultId;
    public String modifiedBy;
    public String modifiedOn;
    public QuestionnaireBean questionnaire;
    public Integer seq;
    public List<ConstitutionListBean> constitutionList;

    public static class QuestionnaireBean {
        /**
         * answerList : [{"answerName":"string","hmAnswerId":"string","hmQuestionId":"string","questionName":"string","questionSeq":0,"score":0}]
         * equipmentId : string
         * hmQuestionnaireId : string
         * hmQuestionnaireName : string
         * score : 0
         * userId : 0
         */

        public String equipmentId;
        public String hmQuestionnaireId;
        public String hmQuestionnaireName;
        public Integer score;
        public Integer userId;
        public List<AnswerListBean> answerList;

        public static class AnswerListBean {
            /**
             * answerName : string
             * hmAnswerId : string
             * hmQuestionId : string
             * questionName : string
             * questionSeq : 0
             * score : 0
             */

            public String answerName;
            public String hmAnswerId;
            public String hmQuestionId;
            public String questionName;
            public Integer questionSeq;
            public Integer score;
        }
    }

    public static class ConstitutionListBean implements Serializable {
        /**
         * constitutionName : string
         * oppositeList : [0]
         * questionList : [0]
         * result : string
         * score : 0
         */

        public String constitutionName;
        public String result;
        public Integer score;
        public List<Integer> oppositeList;
        public List<Integer> questionList;
    }
}
