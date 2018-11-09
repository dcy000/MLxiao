package com.example.han.referralproject.single_measure.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/29 14:24
 * created by:gzq
 * description:完善个人信息之后的健康调查
 */
public class HealthInquiryBean implements Parcelable{

    /**
     * questionList : [{"seq":1,"answerList":[{"seq":1,"hmAnswerId":"27cc94b1-2079-4b15-8a74-eab92e1cdd9d","hmQuestionId":"ebb8529d-e987-461f-8331-3c18935e3e41","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"否","answerScore":0},{"seq":2,"hmAnswerId":"58e08e61-9be6-4dff-82f2-a6796fb5d082","hmQuestionId":"ebb8529d-e987-461f-8331-3c18935e3e41","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"是","answerScore":2}],"hmQuestionId":"ebb8529d-e987-461f-8331-3c18935e3e41","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","questionName":"您是否吸烟？","questionType":"0"},{"seq":2,"answerList":[{"seq":1,"hmAnswerId":"643326fa-ab08-463c-af18-76d495b517e4","hmQuestionId":"0c16fd9d-4c58-4443-8aeb-065b99011979","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"否","answerScore":0},{"seq":2,"hmAnswerId":"86ebb633-f822-4f85-8dac-d79e6356ccf1","hmQuestionId":"0c16fd9d-4c58-4443-8aeb-065b99011979","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"适量","answerScore":0},{"seq":3,"hmAnswerId":"19bf87a2-ed02-4724-8b42-310facebde0c","hmQuestionId":"0c16fd9d-4c58-4443-8aeb-065b99011979","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"过量","answerScore":2}],"hmQuestionId":"0c16fd9d-4c58-4443-8aeb-065b99011979","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","questionName":"您是否饮酒？","questionType":"0"},{"seq":3,"answerList":[{"seq":1,"hmAnswerId":"315222ec-2406-4f21-92b3-3ea7a07b2133","hmQuestionId":"b9fad748-8650-46b4-9f80-77f181a2fc0a","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"杂粮摄入偏少","answerScore":1},{"seq":2,"hmAnswerId":"fe744394-53a6-4f20-bbd5-310cc13730d9","hmQuestionId":"b9fad748-8650-46b4-9f80-77f181a2fc0a","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"高脂类摄入过多","answerScore":1},{"seq":3,"hmAnswerId":"156d3d7f-457f-402c-a540-d872542b9ad2","hmQuestionId":"b9fad748-8650-46b4-9f80-77f181a2fc0a","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"蔬菜水果摄入偏少","answerScore":1},{"seq":4,"hmAnswerId":"b63f35db-01aa-4e61-933a-5540c05d1760","hmQuestionId":"b9fad748-8650-46b4-9f80-77f181a2fc0a","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"饮食偏咸","answerScore":2},{"seq":5,"hmAnswerId":"9753ae61-f00a-46e0-89c5-af256d63230a","hmQuestionId":"b9fad748-8650-46b4-9f80-77f181a2fc0a","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"在外就餐多","answerScore":2},{"seq":6,"hmAnswerId":"e5cc0e95-8361-4421-bfc8-a5ae7bede76a","hmQuestionId":"b9fad748-8650-46b4-9f80-77f181a2fc0a","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"饮食均衡","answerScore":0}],"hmQuestionId":"b9fad748-8650-46b4-9f80-77f181a2fc0a","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","questionName":"您的饮食习惯如何？","questionType":"1"},{"seq":4,"answerList":[{"seq":1,"hmAnswerId":"082a9c4b-83ff-47b3-a76b-463ba81a9cdf","hmQuestionId":"5f60f636-53c3-4623-a7d3-d7ca5ec1da45","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"每周＜5天且每次＜30分钟","answerScore":2},{"seq":2,"hmAnswerId":"b1aea10f-33ed-4ead-903f-58a64e4dbc47","hmQuestionId":"5f60f636-53c3-4623-a7d3-d7ca5ec1da45","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"每周＜5天或每次＜30分钟","answerScore":1},{"seq":3,"hmAnswerId":"2ef584d6-a539-4123-a97e-a87109f33099","hmQuestionId":"5f60f636-53c3-4623-a7d3-d7ca5ec1da45","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"每周＞5天且每次＞30分钟","answerScore":0}],"hmQuestionId":"5f60f636-53c3-4623-a7d3-d7ca5ec1da45","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","questionName":"您每周的运动频率以及每次的运动时间如何？","questionType":"0"},{"seq":5,"answerList":[{"seq":1,"hmAnswerId":"c13f7532-f735-4803-89de-9aa697448e46","hmQuestionId":"1086df46-9880-4db9-8ad1-7fe32ebe2e71","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"没有","answerScore":0},{"seq":2,"hmAnswerId":"9cf2e116-b3d5-4191-93d4-f477196fde5a","hmQuestionId":"1086df46-9880-4db9-8ad1-7fe32ebe2e71","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"糖尿病","answerScore":2},{"seq":3,"hmAnswerId":"807a9015-d0e2-4806-b0d7-5222ba5c7ac0","hmQuestionId":"1086df46-9880-4db9-8ad1-7fe32ebe2e71","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"高血压","answerScore":2},{"seq":4,"hmAnswerId":"79253259-8ae4-45ee-8e32-8739eb1c1c32","hmQuestionId":"1086df46-9880-4db9-8ad1-7fe32ebe2e71","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"血脂异常","answerScore":2},{"seq":5,"hmAnswerId":"74fd6417-8acc-4070-8fbd-7dd5890253e1","hmQuestionId":"1086df46-9880-4db9-8ad1-7fe32ebe2e71","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"痛风/高尿酸血症","answerScore":2},{"seq":6,"hmAnswerId":"a7905f6f-48e4-4c7b-8eef-842002fcc044","hmQuestionId":"1086df46-9880-4db9-8ad1-7fe32ebe2e71","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"慢性支气管炎","answerScore":2},{"seq":7,"hmAnswerId":"c5f3b442-dc9e-42d9-b4f8-2201f00ae0de","hmQuestionId":"1086df46-9880-4db9-8ad1-7fe32ebe2e71","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"肺癌","answerScore":2}],"hmQuestionId":"1086df46-9880-4db9-8ad1-7fe32ebe2e71","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","questionName":"您有没有以下病史？","questionType":"1"},{"seq":6,"answerList":[{"seq":1,"hmAnswerId":"a49ce117-2ed3-484f-8d79-c795326e2d8c","hmQuestionId":"a65d9731-9377-4e19-806e-a4e1d5a40420","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"没有","answerScore":0},{"seq":2,"hmAnswerId":"9e18e683-f0b5-4db9-be6d-31def053b619","hmQuestionId":"a65d9731-9377-4e19-806e-a4e1d5a40420","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"糖尿病","answerScore":2},{"seq":3,"hmAnswerId":"65af62c4-27a8-45bb-bd9c-f67bd6219211","hmQuestionId":"a65d9731-9377-4e19-806e-a4e1d5a40420","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"高血压","answerScore":2},{"seq":4,"hmAnswerId":"1330813d-3c59-4202-80db-46b0acf75ee4","hmQuestionId":"a65d9731-9377-4e19-806e-a4e1d5a40420","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"痛风/高尿酸血症","answerScore":2},{"seq":5,"hmAnswerId":"445d4b59-af7d-4553-8c4d-c1dd8962a56a","hmQuestionId":"a65d9731-9377-4e19-806e-a4e1d5a40420","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"肥胖","answerScore":2},{"seq":6,"hmAnswerId":"1042b735-c10b-4bd2-b326-11a75b4838f7","hmQuestionId":"a65d9731-9377-4e19-806e-a4e1d5a40420","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"肺癌","answerScore":2}],"hmQuestionId":"a65d9731-9377-4e19-806e-a4e1d5a40420","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","questionName":"您有没有以下的家族病史？","questionType":"1"}]
     * hmQuestionnaireId : a7983797-5e09-4a08-9204-9fb686378a35
     * questionnaireName : 健康调查问卷
     * questionnaireScore : 36
     */

    private String hmQuestionnaireId;
    private String questionnaireName;
    private int questionnaireScore;
    private List<QuestionListBean> questionList;

    protected HealthInquiryBean(Parcel in) {
        hmQuestionnaireId = in.readString();
        questionnaireName = in.readString();
        questionnaireScore = in.readInt();
        questionList = in.createTypedArrayList(QuestionListBean.CREATOR);
    }

    public static final Creator<HealthInquiryBean> CREATOR = new Creator<HealthInquiryBean>() {
        @Override
        public HealthInquiryBean createFromParcel(Parcel in) {
            return new HealthInquiryBean(in);
        }

        @Override
        public HealthInquiryBean[] newArray(int size) {
            return new HealthInquiryBean[size];
        }
    };

    public String getHmQuestionnaireId() {
        return hmQuestionnaireId;
    }

    public void setHmQuestionnaireId(String hmQuestionnaireId) {
        this.hmQuestionnaireId = hmQuestionnaireId;
    }

    public String getQuestionnaireName() {
        return questionnaireName;
    }

    public void setQuestionnaireName(String questionnaireName) {
        this.questionnaireName = questionnaireName;
    }

    public int getQuestionnaireScore() {
        return questionnaireScore;
    }

    public void setQuestionnaireScore(int questionnaireScore) {
        this.questionnaireScore = questionnaireScore;
    }

    public List<QuestionListBean> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<QuestionListBean> questionList) {
        this.questionList = questionList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hmQuestionnaireId);
        dest.writeString(questionnaireName);
        dest.writeInt(questionnaireScore);
        dest.writeTypedList(questionList);
    }

    @Override
    public String toString() {
        return "HealthInquiryBean{" +
                "hmQuestionnaireId='" + hmQuestionnaireId + '\'' +
                ", questionnaireName='" + questionnaireName + '\'' +
                ", questionnaireScore=" + questionnaireScore +
                ", questionList=" + questionList +
                '}';
    }

    public static class QuestionListBean implements Parcelable{
        /**
         * seq : 1
         * answerList : [{"seq":1,"hmAnswerId":"27cc94b1-2079-4b15-8a74-eab92e1cdd9d","hmQuestionId":"ebb8529d-e987-461f-8331-3c18935e3e41","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"否","answerScore":0},{"seq":2,"hmAnswerId":"58e08e61-9be6-4dff-82f2-a6796fb5d082","hmQuestionId":"ebb8529d-e987-461f-8331-3c18935e3e41","hmQuestionnaireId":"a7983797-5e09-4a08-9204-9fb686378a35","answerInfo":"是","answerScore":2}]
         * hmQuestionId : ebb8529d-e987-461f-8331-3c18935e3e41
         * hmQuestionnaireId : a7983797-5e09-4a08-9204-9fb686378a35
         * questionName : 您是否吸烟？
         * questionType : 0
         */

        private int seq;
        private String hmQuestionId;
        private String hmQuestionnaireId;
        private String questionName;
        private String questionType;
        private List<AnswerListBean> answerList;

        protected QuestionListBean(Parcel in) {
            seq = in.readInt();
            hmQuestionId = in.readString();
            hmQuestionnaireId = in.readString();
            questionName = in.readString();
            questionType = in.readString();
            answerList = in.createTypedArrayList(AnswerListBean.CREATOR);
        }

        public static final Creator<QuestionListBean> CREATOR = new Creator<QuestionListBean>() {
            @Override
            public QuestionListBean createFromParcel(Parcel in) {
                return new QuestionListBean(in);
            }

            @Override
            public QuestionListBean[] newArray(int size) {
                return new QuestionListBean[size];
            }
        };

        public int getSeq() {
            return seq;
        }

        public void setSeq(int seq) {
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
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(seq);
            dest.writeString(hmQuestionId);
            dest.writeString(hmQuestionnaireId);
            dest.writeString(questionName);
            dest.writeString(questionType);
            dest.writeTypedList(answerList);
        }

        @Override
        public String toString() {
            return "QuestionListBean{" +
                    "seq=" + seq +
                    ", hmQuestionId='" + hmQuestionId + '\'' +
                    ", hmQuestionnaireId='" + hmQuestionnaireId + '\'' +
                    ", questionName='" + questionName + '\'' +
                    ", questionType='" + questionType + '\'' +
                    ", answerList=" + answerList +
                    '}';
        }

        public static class AnswerListBean implements Parcelable{
            /**
             * seq : 1
             * hmAnswerId : 27cc94b1-2079-4b15-8a74-eab92e1cdd9d
             * hmQuestionId : ebb8529d-e987-461f-8331-3c18935e3e41
             * hmQuestionnaireId : a7983797-5e09-4a08-9204-9fb686378a35
             * answerInfo : 否
             * answerScore : 0
             */

            private int seq;
            private String hmAnswerId;
            private String hmQuestionId;
            private String hmQuestionnaireId;
            private String answerInfo;
            private int answerScore;
            private String exclusiveStatus;
            private boolean isChoosed;

            protected AnswerListBean(Parcel in) {
                seq = in.readInt();
                hmAnswerId = in.readString();
                hmQuestionId = in.readString();
                hmQuestionnaireId = in.readString();
                answerInfo = in.readString();
                answerScore = in.readInt();
                exclusiveStatus = in.readString();
                isChoosed = in.readByte() != 0;
            }

            public static final Creator<AnswerListBean> CREATOR = new Creator<AnswerListBean>() {
                @Override
                public AnswerListBean createFromParcel(Parcel in) {
                    return new AnswerListBean(in);
                }

                @Override
                public AnswerListBean[] newArray(int size) {
                    return new AnswerListBean[size];
                }
            };

            public String getExclusiveStatus() {
                return exclusiveStatus;
            }

            public void setExclusiveStatus(String exclusiveStatus) {
                this.exclusiveStatus = exclusiveStatus;
            }

            public boolean getChoosed() {
                return isChoosed;
            }

            public void setChoosed(boolean choosed) {
                isChoosed = choosed;
            }

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

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(seq);
                dest.writeString(hmAnswerId);
                dest.writeString(hmQuestionId);
                dest.writeString(hmQuestionnaireId);
                dest.writeString(answerInfo);
                dest.writeInt(answerScore);
                dest.writeString(exclusiveStatus);
                dest.writeByte((byte) (isChoosed ? 1 : 0));
            }

            @Override
            public String toString() {
                return "AnswerListBean{" +
                        "seq=" + seq +
                        ", hmAnswerId='" + hmAnswerId + '\'' +
                        ", hmQuestionId='" + hmQuestionId + '\'' +
                        ", hmQuestionnaireId='" + hmQuestionnaireId + '\'' +
                        ", answerInfo='" + answerInfo + '\'' +
                        ", answerScore=" + answerScore +
                        ", exclusiveStatus='" + exclusiveStatus + '\'' +
                        ", isChoosed=" + isChoosed +
                        '}';
            }
        }
    }
}
