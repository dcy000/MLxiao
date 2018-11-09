package com.example.han.referralproject.single_measure.bean;

import java.util.List;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/30 9:35
 * created by:gzq
 * description:TODO
 */
public class HealthInquiryPostBean {


    /**
     * answerList : [{"answerName":"string","hmAnswerId":"string","hmQuestionId":"string","questionName":"string","questionSeq":0,"score":0}]
     * equipmentId : string
     * hmQuestionnaireId : string
     * hmQuestionnaireName : string
     * score : 0
     * userId : 0
     */

    private String equipmentId;
    private String hmQuestionnaireId;
    private String hmQuestionnaireName;
    private int score;
    private String userId;
    private List<AnswerListBean> answerList;

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getHmQuestionnaireId() {
        return hmQuestionnaireId;
    }

    public void setHmQuestionnaireId(String hmQuestionnaireId) {
        this.hmQuestionnaireId = hmQuestionnaireId;
    }

    public String getHmQuestionnaireName() {
        return hmQuestionnaireName;
    }

    public void setHmQuestionnaireName(String hmQuestionnaireName) {
        this.hmQuestionnaireName = hmQuestionnaireName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<AnswerListBean> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<AnswerListBean> answerList) {
        this.answerList = answerList;
    }

    public static class AnswerListBean {
        /**
         * answerName : string
         * hmAnswerId : string
         * hmQuestionId : string
         * questionName : string
         * questionSeq : 0
         * score : 0
         */

        private String answerName;
        private String hmAnswerId;
        private String hmQuestionId;
        private String questionName;
        private int questionSeq;
        private int score;

        public String getAnswerName() {
            return answerName;
        }

        public void setAnswerName(String answerName) {
            this.answerName = answerName;
        }

        public String getHmAnswerId() {
            return hmAnswerId;
        }

        public void setHmAnswerId(String hmAnswerId) {
            this.hmAnswerId = hmAnswerId;
        }

        public String getHmQuestionId() {
            return hmQuestionId;
        }

        public void setHmQuestionId(String hmQuestionId) {
            this.hmQuestionId = hmQuestionId;
        }

        public String getQuestionName() {
            return questionName;
        }

        public void setQuestionName(String questionName) {
            this.questionName = questionName;
        }

        public int getQuestionSeq() {
            return questionSeq;
        }

        public void setQuestionSeq(int questionSeq) {
            this.questionSeq = questionSeq;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }
}
