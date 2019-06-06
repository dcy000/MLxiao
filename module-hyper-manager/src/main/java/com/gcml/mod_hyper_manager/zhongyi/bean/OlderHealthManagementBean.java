package com.gcml.mod_hyper_manager.zhongyi.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2018/6/12.
 * 老年人中医药健康管理服务记录表
 */

public class OlderHealthManagementBean implements Serializable {
    public boolean tag;
    public int code;
    public DataBean data;
    public String message;

    public static class DataBean implements Serializable {
        public String hmQuestionnaireId;
        public String questionnaireName;
        public Object questionnaireInfo;
        public List<QuestionListBean> questionList;

        public static class QuestionListBean implements Serializable {
            public int seq;
            public Boolean isSelected = false;
            //**序号
            public int questionSeq = -1;
            //**得分
            public int answerScore = -1;
            //**问题id
            public String hmQuestionId = "";
            //**答案id
            public String hmAnswerId = "";
            public String hmQuestionnaireId;
            public String questionName;
            public Object questionInfo;
            public String questionType;
            public List<AnswerListBean> answerList;

            public static class AnswerListBean implements Serializable {
                public int seq;
                public String hmAnswerId;
                public String hmQuestionId;
                public String hmQuestionnaireId;
                public String answerInfo;
                public int answerScore;
            }
        }
    }
}
