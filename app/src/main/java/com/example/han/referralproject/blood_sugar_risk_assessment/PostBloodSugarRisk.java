package com.example.han.referralproject.blood_sugar_risk_assessment;

import java.util.List;

/**
 * Created by Administrator on 2018/5/10.
 */

public class PostBloodSugarRisk {

    /**
     * answerList : [{"hmAnswerId":"string","hmQuestionId":"string"}]
     * equipmentId : string
     * hmQuestionnaireAssessId : string
     * hmQuestionnaireId : string
     * score : 0
     * userId : 0
     */

    private String equipmentId;
    private String hmQuestionnaireAssessId;
    private String hmQuestionnaireId;
    private int score;
    private int userId;
    private List<AnswerListBean> answerList;

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getHmQuestionnaireAssessId() {
        return hmQuestionnaireAssessId;
    }

    public void setHmQuestionnaireAssessId(String hmQuestionnaireAssessId) {
        this.hmQuestionnaireAssessId = hmQuestionnaireAssessId;
    }

    public String getHmQuestionnaireId() {
        return hmQuestionnaireId;
    }

    public void setHmQuestionnaireId(String hmQuestionnaireId) {
        this.hmQuestionnaireId = hmQuestionnaireId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
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
         * hmAnswerId : string
         * hmQuestionId : string
         */

        private String hmAnswerId;
        private String hmQuestionId;

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
    }
}
