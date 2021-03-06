package com.example.han.referralproject.physicalexamination.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2018/5/10.
 */

public class QuestionnaireBean implements Serializable {


    /**
     * questionList : [{"seq":null,"answerList":[{"seq":0,"hmAnswerId":"03957ecb-b6c1-444e-a207-be31e2b317b7","hmQuestionId":"d99786d0-3269-469b-ab34-4133e6eca7be","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":"<45岁","score":0},{"seq":0,"hmAnswerId":"050f3af2-9006-4357-a538-cd342063d89a","hmQuestionId":"d99786d0-3269-469b-ab34-4133e6eca7be","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":"45--54岁","score":2},{"seq":0,"hmAnswerId":"a1e8c537-2efb-4afd-aa0e-8aa6bf031e90","hmQuestionId":"d99786d0-3269-469b-ab34-4133e6eca7be","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":"54--64岁","score":3},{"seq":0,"hmAnswerId":"bf774ae8-ae0c-4437-b802-976df0e0fff4","hmQuestionId":"d99786d0-3269-469b-ab34-4133e6eca7be","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":">64岁","score":4}],"hmQuestionId":"d99786d0-3269-469b-ab34-4133e6eca7be","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","questionName":"年龄","questionInfo":null,"questionType":"0"},{"seq":null,"answerList":[{"seq":0,"hmAnswerId":"e68fd155-31f2-48d5-ae1e-baa1d8b23cb0","hmQuestionId":"06ff15c1-e9fe-4e0c-931e-def479b43470","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":"<25kg/m2","score":0},{"seq":0,"hmAnswerId":"11df1a36-4750-4b45-b55e-a768d79b8ab5","hmQuestionId":"06ff15c1-e9fe-4e0c-931e-def479b43470","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":"25--30kg/m2","score":1},{"seq":0,"hmAnswerId":"afade8f0-32bc-43c0-bc11-2b3808d91ca2","hmQuestionId":"06ff15c1-e9fe-4e0c-931e-def479b43470","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":">30kg/m2","score":3}],"hmQuestionId":"06ff15c1-e9fe-4e0c-931e-def479b43470","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","questionName":"体重","questionInfo":null,"questionType":"0"},{"seq":null,"answerList":[{"seq":0,"hmAnswerId":"f820155e-9559-4a29-a884-b32490de2feb","hmQuestionId":"0dfb2e78-5f9d-4d56-b540-20e0c0d3f4af","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":"<94cm","score":0},{"seq":0,"hmAnswerId":"cfac68fb-5edd-41df-a060-3a941871650e","hmQuestionId":"0dfb2e78-5f9d-4d56-b540-20e0c0d3f4af","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":"94--102cm","score":3},{"seq":0,"hmAnswerId":"b54c5f14-9a00-41fa-b3f3-59dc34188133","hmQuestionId":"0dfb2e78-5f9d-4d56-b540-20e0c0d3f4af","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":">102cm","score":4}],"hmQuestionId":"0dfb2e78-5f9d-4d56-b540-20e0c0d3f4af","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","questionName":"肋骨下方腰围（一般为平脐水平）","questionInfo":null,"questionType":"0"},{"seq":null,"answerList":[{"seq":0,"hmAnswerId":"b2530ee2-49da-4ab4-92ec-c4f05b0864f9","hmQuestionId":"deb8dd00-0651-4377-87d7-88db25c7f2e8","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":"是","score":0},{"seq":0,"hmAnswerId":"7c38687a-7749-47d1-a18c-4ddae22c6c2d","hmQuestionId":"deb8dd00-0651-4377-87d7-88db25c7f2e8","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":"否","score":2}],"hmQuestionId":"deb8dd00-0651-4377-87d7-88db25c7f2e8","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","questionName":"您每天在工作中和/或休闲时间至少进行30分钟的体力活动（包括正常的日常活动）吗？","questionInfo":null,"questionType":"0"},{"seq":null,"answerList":[{"seq":0,"hmAnswerId":"9b9fe2fa-bebc-4427-9740-9e14e87b6ec4","hmQuestionId":"6cfa423d-94f0-4dec-bf17-e2c43bd8af2d","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":"每天摄入","score":0},{"seq":0,"hmAnswerId":"8b0ffe64-7ad8-4c2e-aa09-faa7c6340506","hmQuestionId":"6cfa423d-94f0-4dec-bf17-e2c43bd8af2d","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":"不是每天摄入","score":2}],"hmQuestionId":"6cfa423d-94f0-4dec-bf17-e2c43bd8af2d","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","questionName":"您摄入蔬菜、水果或莓类的频率是？","questionInfo":null,"questionType":"0"},{"seq":null,"answerList":[{"seq":0,"hmAnswerId":"e4f44756-0959-462b-ae07-3bbb8d8dbf17","hmQuestionId":"7afa2040-1db2-48a6-83d4-bf7cf3b3baed","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":"从未","score":0},{"seq":0,"hmAnswerId":"2d8a501e-3bf5-41f0-9000-319777c5a258","hmQuestionId":"7afa2040-1db2-48a6-83d4-bf7cf3b3baed","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":"是","score":2}],"hmQuestionId":"7afa2040-1db2-48a6-83d4-bf7cf3b3baed","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","questionName":"您曾常规服用降压药吗？","questionInfo":null,"questionType":"0"},{"seq":null,"answerList":[{"seq":0,"hmAnswerId":"42e735ab-193c-4c47-b6e4-89989da9f1a7","hmQuestionId":"0fb15de8-1ca9-41c0-b13e-5af98cfb1da3","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":"从未","score":0},{"seq":0,"hmAnswerId":"d49d47f8-9325-4eec-98ee-e61c11c613dc","hmQuestionId":"0fb15de8-1ca9-41c0-b13e-5af98cfb1da3","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":"是","score":2}],"hmQuestionId":"0fb15de8-1ca9-41c0-b13e-5af98cfb1da3","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","questionName":"您曾发现过高血糖吗（如体检、患病或妊娠过程中）？","questionInfo":null,"questionType":"0"},{"seq":null,"answerList":[{"seq":0,"hmAnswerId":"4f61b9a8-7a76-4c8f-b5e3-57f5497098dd","hmQuestionId":"bbea9cd4-1fa2-46d9-a1e1-71445b551e4a","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":"无","score":0},{"seq":0,"hmAnswerId":"17dff16e-6737-4abc-8c33-3e2f01f382cd","hmQuestionId":"bbea9cd4-1fa2-46d9-a1e1-71445b551e4a","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":"是 祖父母/外祖父母、姑妈/姨母、叔父/舅父，或一级堂表亲（但父母、兄弟、姐妹和子女没有）","score":2},{"seq":0,"hmAnswerId":"572baf08-c5aa-49dd-beb7-a232d23a4f9c","hmQuestionId":"bbea9cd4-1fa2-46d9-a1e1-71445b551e4a","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":"是 父母、兄弟、姐妹或子女","score":5}],"hmQuestionId":"bbea9cd4-1fa2-46d9-a1e1-71445b551e4a","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","questionName":"您的直系亲属或其他亲属中有人被诊断为（1型或2型）糖尿病吗？","questionInfo":null,"questionType":"0"}]
     * hmQuestionnaireId : 64e3f45b-f093-440f-9667-3253bf0afb2c
     * questionnaireName : 男性糖尿病风险评估
     * questionnaireInfo : null
     */

    public String hmQuestionnaireId;
    public String questionnaireName;
    public Object questionnaireInfo;
    public List<QuestionListBean> questionList;

    public static class QuestionListBean implements Serializable {
        /**
         * seq : null
         * answerList : [{"seq":0,"hmAnswerId":"03957ecb-b6c1-444e-a207-be31e2b317b7","hmQuestionId":"d99786d0-3269-469b-ab34-4133e6eca7be","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":"<45岁","score":0},{"seq":0,"hmAnswerId":"050f3af2-9006-4357-a538-cd342063d89a","hmQuestionId":"d99786d0-3269-469b-ab34-4133e6eca7be","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":"45--54岁","score":2},{"seq":0,"hmAnswerId":"a1e8c537-2efb-4afd-aa0e-8aa6bf031e90","hmQuestionId":"d99786d0-3269-469b-ab34-4133e6eca7be","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":"54--64岁","score":3},{"seq":0,"hmAnswerId":"bf774ae8-ae0c-4437-b802-976df0e0fff4","hmQuestionId":"d99786d0-3269-469b-ab34-4133e6eca7be","hmQuestionnaireId":"64e3f45b-f093-440f-9667-3253bf0afb2c","answerInfo":">64岁","score":4}]
         * hmQuestionId : d99786d0-3269-469b-ab34-4133e6eca7be
         * hmQuestionnaireId : 64e3f45b-f093-440f-9667-3253bf0afb2c
         * questionName : 年龄
         * questionInfo : null
         * questionType : 0
         */

        public Object seq;
        public String hmQuestionId;
        public String hmQuestionnaireId;
        public String questionName;
        public String questionInfo = "";
        public List<AnswerListBean> answerList;
        public boolean isSelected;
        public int score;

        public static class AnswerListBean implements Serializable {
            /**
             * seq : 0
             * hmAnswerId : 03957ecb-b6c1-444e-a207-be31e2b317b7
             * hmQuestionId : d99786d0-3269-469b-ab34-4133e6eca7be
             * hmQuestionnaireId : 64e3f45b-f093-440f-9667-3253bf0afb2c
             * answerInfo : <45岁
             * score : 0
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
