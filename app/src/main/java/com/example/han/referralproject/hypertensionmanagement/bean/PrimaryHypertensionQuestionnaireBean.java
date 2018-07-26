package com.example.han.referralproject.hypertensionmanagement.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 原发性高血压获取问卷
 * Created by lenovo on 2018/7/26.
 */

public class PrimaryHypertensionQuestionnaireBean implements Serializable {

    /**
     * tag : true
     * code : 200
     * data : {"questionList":[{"seq":null,"answerList":[{"seq":0,"hmAnswerId":"3e449f08-4c99-4845-b6b1-f46d823571cd","hmQuestionId":"81c49977-b06c-403e-b0b6-1d0fb09b5453","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","answerInfo":"颈项板紧","answerScore":0},{"seq":0,"hmAnswerId":"a1e2eb42-970b-4f76-b916-bfe2701975e2","hmQuestionId":"81c49977-b06c-403e-b0b6-1d0fb09b5453","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","answerInfo":"疲劳","answerScore":0},{"seq":0,"hmAnswerId":"132b2efa-d176-4a53-bb96-5668863034f5","hmQuestionId":"81c49977-b06c-403e-b0b6-1d0fb09b5453","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","answerInfo":"记忆力下降","answerScore":0},{"seq":0,"hmAnswerId":"11095710-7273-4309-8925-ee6a68efa090","hmQuestionId":"81c49977-b06c-403e-b0b6-1d0fb09b5453","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","answerInfo":"失眠","answerScore":0},{"seq":0,"hmAnswerId":"c9c0d858-1021-49dc-9a77-762db37b62fe","hmQuestionId":"81c49977-b06c-403e-b0b6-1d0fb09b5453","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","answerInfo":"头痛","answerScore":0},{"seq":0,"hmAnswerId":"bc8a1cc5-35d2-4a54-8e2f-659a0b485025","hmQuestionId":"81c49977-b06c-403e-b0b6-1d0fb09b5453","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","answerInfo":"无","answerScore":0}],"hmQuestionId":"81c49977-b06c-403e-b0b6-1d0fb09b5453","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","questionName":"是否有以下症状","questionInfo":null,"questionType":"0"},{"seq":null,"answerList":[{"seq":0,"hmAnswerId":"907f7a87-997b-40a5-a32e-c368d21b4ed7","hmQuestionId":"49a26d60-1592-471a-8f5b-44dbb9b3ff57","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","answerInfo":"否","answerScore":0}],"hmQuestionId":"49a26d60-1592-471a-8f5b-44dbb9b3ff57","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","questionName":"是否有靶器官损害的临床症状","questionInfo":null,"questionType":"0"},{"seq":null,"answerList":[{"seq":0,"hmAnswerId":"f0a9d8d2-14bc-4d6e-9825-772f4128cffd","hmQuestionId":"99080752-bef1-4c8e-b3a5-4ecd1c96d153","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","answerInfo":"否","answerScore":0}],"hmQuestionId":"99080752-bef1-4c8e-b3a5-4ecd1c96d153","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","questionName":"是否有药物不良反应","questionInfo":null,"questionType":"0"},{"seq":null,"answerList":[{"seq":0,"hmAnswerId":"4e61f5e7-4a78-46e9-88a3-f73d6d571944","hmQuestionId":"caf46c8f-0fbc-4516-9e6c-ac72e24e5ab1","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","answerInfo":"偶尔服用","answerScore":0},{"seq":0,"hmAnswerId":"4b000358-b82c-40b0-9148-b094ac11b46f","hmQuestionId":"caf46c8f-0fbc-4516-9e6c-ac72e24e5ab1","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","answerInfo":"从不服用","answerScore":0}],"hmQuestionId":"caf46c8f-0fbc-4516-9e6c-ac72e24e5ab1","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","questionName":"服药依从性","questionInfo":null,"questionType":"0"}],"hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","questionnaireName":"原发性高血压患者调查","questionnaireScore":0,"questionnaireInfo":null}
     * message : 成功
     */

    public boolean tag;
    public int code;
    public DataBean data;
    public String message;

    public static class DataBean {
        /**
         * questionList : [{"seq":null,"answerList":[{"seq":0,"hmAnswerId":"3e449f08-4c99-4845-b6b1-f46d823571cd","hmQuestionId":"81c49977-b06c-403e-b0b6-1d0fb09b5453","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","answerInfo":"颈项板紧","answerScore":0},{"seq":0,"hmAnswerId":"a1e2eb42-970b-4f76-b916-bfe2701975e2","hmQuestionId":"81c49977-b06c-403e-b0b6-1d0fb09b5453","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","answerInfo":"疲劳","answerScore":0},{"seq":0,"hmAnswerId":"132b2efa-d176-4a53-bb96-5668863034f5","hmQuestionId":"81c49977-b06c-403e-b0b6-1d0fb09b5453","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","answerInfo":"记忆力下降","answerScore":0},{"seq":0,"hmAnswerId":"11095710-7273-4309-8925-ee6a68efa090","hmQuestionId":"81c49977-b06c-403e-b0b6-1d0fb09b5453","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","answerInfo":"失眠","answerScore":0},{"seq":0,"hmAnswerId":"c9c0d858-1021-49dc-9a77-762db37b62fe","hmQuestionId":"81c49977-b06c-403e-b0b6-1d0fb09b5453","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","answerInfo":"头痛","answerScore":0},{"seq":0,"hmAnswerId":"bc8a1cc5-35d2-4a54-8e2f-659a0b485025","hmQuestionId":"81c49977-b06c-403e-b0b6-1d0fb09b5453","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","answerInfo":"无","answerScore":0}],"hmQuestionId":"81c49977-b06c-403e-b0b6-1d0fb09b5453","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","questionName":"是否有以下症状","questionInfo":null,"questionType":"0"},{"seq":null,"answerList":[{"seq":0,"hmAnswerId":"907f7a87-997b-40a5-a32e-c368d21b4ed7","hmQuestionId":"49a26d60-1592-471a-8f5b-44dbb9b3ff57","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","answerInfo":"否","answerScore":0}],"hmQuestionId":"49a26d60-1592-471a-8f5b-44dbb9b3ff57","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","questionName":"是否有靶器官损害的临床症状","questionInfo":null,"questionType":"0"},{"seq":null,"answerList":[{"seq":0,"hmAnswerId":"f0a9d8d2-14bc-4d6e-9825-772f4128cffd","hmQuestionId":"99080752-bef1-4c8e-b3a5-4ecd1c96d153","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","answerInfo":"否","answerScore":0}],"hmQuestionId":"99080752-bef1-4c8e-b3a5-4ecd1c96d153","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","questionName":"是否有药物不良反应","questionInfo":null,"questionType":"0"},{"seq":null,"answerList":[{"seq":0,"hmAnswerId":"4e61f5e7-4a78-46e9-88a3-f73d6d571944","hmQuestionId":"caf46c8f-0fbc-4516-9e6c-ac72e24e5ab1","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","answerInfo":"偶尔服用","answerScore":0},{"seq":0,"hmAnswerId":"4b000358-b82c-40b0-9148-b094ac11b46f","hmQuestionId":"caf46c8f-0fbc-4516-9e6c-ac72e24e5ab1","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","answerInfo":"从不服用","answerScore":0}],"hmQuestionId":"caf46c8f-0fbc-4516-9e6c-ac72e24e5ab1","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","questionName":"服药依从性","questionInfo":null,"questionType":"0"}]
         * hmQuestionnaireId : 1e7179b5-6a50-4290-abbe-941a63297a63
         * questionnaireName : 原发性高血压患者调查
         * questionnaireScore : 0
         * questionnaireInfo : null
         */

        public String hmQuestionnaireId;
        public String questionnaireName;
        public int questionnaireScore;
        public Object questionnaireInfo;
        public List<QuestionListBean> questionList;

        public static class QuestionListBean {
            /**
             * seq : null
             * answerList : [{"seq":0,"hmAnswerId":"3e449f08-4c99-4845-b6b1-f46d823571cd","hmQuestionId":"81c49977-b06c-403e-b0b6-1d0fb09b5453","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","answerInfo":"颈项板紧","answerScore":0},{"seq":0,"hmAnswerId":"a1e2eb42-970b-4f76-b916-bfe2701975e2","hmQuestionId":"81c49977-b06c-403e-b0b6-1d0fb09b5453","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","answerInfo":"疲劳","answerScore":0},{"seq":0,"hmAnswerId":"132b2efa-d176-4a53-bb96-5668863034f5","hmQuestionId":"81c49977-b06c-403e-b0b6-1d0fb09b5453","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","answerInfo":"记忆力下降","answerScore":0},{"seq":0,"hmAnswerId":"11095710-7273-4309-8925-ee6a68efa090","hmQuestionId":"81c49977-b06c-403e-b0b6-1d0fb09b5453","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","answerInfo":"失眠","answerScore":0},{"seq":0,"hmAnswerId":"c9c0d858-1021-49dc-9a77-762db37b62fe","hmQuestionId":"81c49977-b06c-403e-b0b6-1d0fb09b5453","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","answerInfo":"头痛","answerScore":0},{"seq":0,"hmAnswerId":"bc8a1cc5-35d2-4a54-8e2f-659a0b485025","hmQuestionId":"81c49977-b06c-403e-b0b6-1d0fb09b5453","hmQuestionnaireId":"1e7179b5-6a50-4290-abbe-941a63297a63","answerInfo":"无","answerScore":0}]
             * hmQuestionId : 81c49977-b06c-403e-b0b6-1d0fb09b5453
             * hmQuestionnaireId : 1e7179b5-6a50-4290-abbe-941a63297a63
             * questionName : 是否有以下症状
             * questionInfo : null
             * questionType : 0
             */

            public Object seq;
            public String hmQuestionId;
            public String hmQuestionnaireId;
            public String questionName;
            public Object questionInfo;
            public String questionType;
            public List<AnswerListBean> answerList;

            public static class AnswerListBean {
                /**
                 * seq : 0
                 * hmAnswerId : 3e449f08-4c99-4845-b6b1-f46d823571cd
                 * hmQuestionId : 81c49977-b06c-403e-b0b6-1d0fb09b5453
                 * hmQuestionnaireId : 1e7179b5-6a50-4290-abbe-941a63297a63
                 * answerInfo : 颈项板紧
                 * answerScore : 0
                 */

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
