package com.gcml.task.bean.Post;

import java.io.Serializable;
import java.util.List;

public class TaskSchemaBean implements Serializable {

    /**
     * answerList : [{"answerName":"string","hmAnswerId":"string","hmQuestionId":"string","questionName":"string","questionSeq":0}]
     * equipmentId : string
     * hmQuestionnaireId : string
     * hmQuestionnaireName : string
     * score : 0
     * userId : 0
     */

    public String equipmentId;
    public String hmQuestionnaireId;
    public String hmQuestionnaireName;
    public int score = 0;
    public int userId;
    public List<AnswerListBean> answerList;

    public static class AnswerListBean implements Serializable {
        /**
         * answerName : string
         * hmAnswerId : string
         * hmQuestionId : string
         * questionName : string
         * questionSeq : 0
         */

        public String answerName;
        public String hmAnswerId;
        public int score;
        public String hmQuestionId;
        public String questionName;
        public int questionSeq;

    }
}
