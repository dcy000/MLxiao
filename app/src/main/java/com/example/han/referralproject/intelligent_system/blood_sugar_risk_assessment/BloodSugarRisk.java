package com.example.han.referralproject.intelligent_system.blood_sugar_risk_assessment;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/10.
 */

public class BloodSugarRisk implements MultiItemEntity{
    public static final int FOUR_BUTTON = 100;
    public static final int THREE_BUTTON_HORIZONTAL = 200;
    public static final int THREE_BUTTON_VERTICAL = 300;
    public static final int TWO_BUTTON = 400;
    /**
     * seq : null
     * answerList : [{"seq":0,"hmAnswerId":"03957ecb-b6c1-444e-a207-be31e2b317b7","hmQuestionId":"d99786d0-3269-469b-ab34-4133e6eca7be","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":"<45岁","answerScore":0},{"seq":0,"hmAnswerId":"050f3af2-9006-4357-a538-cd342063d89a","hmQuestionId":"d99786d0-3269-469b-ab34-4133e6eca7be","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":"45--54岁","answerScore":2},{"seq":0,"hmAnswerId":"a1e8c537-2efb-4afd-aa0e-8aa6bf031e90","hmQuestionId":"d99786d0-3269-469b-ab34-4133e6eca7be","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":"54--64岁","answerScore":3},{"seq":0,"hmAnswerId":"bf774ae8-ae0c-4437-b802-976df0e0fff4","hmQuestionId":"d99786d0-3269-469b-ab34-4133e6eca7be","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":">64岁","answerScore":4}]
     * hmQuestionId : d99786d0-3269-469b-ab34-4133e6eca7be
     * hmQuestionnaireId : 64e3f45b-f093-440f-9667-3253bf0afb2c
     * questionName : 年龄
     * questionInfo : null
     * questionType : 0
     */

    private Object seq;
    private String hmQuestionId;
    private String hmQuestionnaireId;
    private String questionName;
    private Object questionInfo;
    private String questionType;
    private List<AnswerListBean> answerList;
    private int itemType;
    private int  choosedPosition;
    private boolean isChoosed=false;

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public int getChoosedPosition() {
        return choosedPosition;
    }

    public void setChoosedPosition(int choosedPosition) {
        this.choosedPosition = choosedPosition;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public Object getSeq() {
        return seq;
    }

    public void setSeq(Object seq) {
        this.seq = seq;
    }

    public String getHmQuestionId() {
        return hmQuestionId;
    }

    public void setHmQuestionId(String hmQuestionId) {
        this.hmQuestionId = hmQuestionId;
    }

    public String getHmQuestionnaireId() {
        return hmQuestionnaireId;
    }

    public void setHmQuestionnaireId(String hmQuestionnaireId) {
        this.hmQuestionnaireId = hmQuestionnaireId;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public Object getQuestionInfo() {
        return questionInfo;
    }

    public void setQuestionInfo(Object questionInfo) {
        this.questionInfo = questionInfo;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public List<AnswerListBean> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<AnswerListBean> answerList) {
        this.answerList = answerList;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public static class AnswerListBean {
        /**
         * seq : 0
         * hmAnswerId : 03957ecb-b6c1-444e-a207-be31e2b317b7
         * hmQuestionId : d99786d0-3269-469b-ab34-4133e6eca7be
         * hmQuestionnaireId : 64e3f45b-f093-440f-9667-3253bf0afb2c
         * answerInfo : <45岁
         * answerScore : 0
         */

        private int seq;
        private String hmAnswerId;
        private String hmQuestionId;
        private String hmQuestionnaireId;
        private String answerInfo;
        private int answerScore;

        public int getSeq() {
            return seq;
        }

        public void setSeq(int seq) {
            this.seq = seq;
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

        public String getHmQuestionnaireId() {
            return hmQuestionnaireId;
        }

        public void setHmQuestionnaireId(String hmQuestionnaireId) {
            this.hmQuestionnaireId = hmQuestionnaireId;
        }

        public String getAnswerInfo() {
            return answerInfo;
        }

        public void setAnswerInfo(String answerInfo) {
            this.answerInfo = answerInfo;
        }

        public int getAnswerScore() {
            return answerScore;
        }

        public void setAnswerScore(int answerScore) {
            this.answerScore = answerScore;
        }
    }
}
