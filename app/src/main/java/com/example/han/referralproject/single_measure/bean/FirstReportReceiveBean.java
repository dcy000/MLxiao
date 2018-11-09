package com.example.han.referralproject.single_measure.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/7 15:26
 * created by:gzq
 * description:TODO
 */
public class FirstReportReceiveBean implements Parcelable{

    private List<FactorListBean> factorList;
    private List<ReportListBean> reportList;

    protected FirstReportReceiveBean(Parcel in) {
        factorList = in.createTypedArrayList(FactorListBean.CREATOR);
        reportList = in.createTypedArrayList(ReportListBean.CREATOR);
    }

    public static final Creator<FirstReportReceiveBean> CREATOR = new Creator<FirstReportReceiveBean>() {
        @Override
        public FirstReportReceiveBean createFromParcel(Parcel in) {
            return new FirstReportReceiveBean(in);
        }

        @Override
        public FirstReportReceiveBean[] newArray(int size) {
            return new FirstReportReceiveBean[size];
        }
    };

    public List<FactorListBean> getFactorList() {
        return factorList;
    }

    public void setFactorList(List<FactorListBean> factorList) {
        this.factorList = factorList;
    }

    public List<ReportListBean> getReportList() {
        return reportList;
    }

    public void setReportList(List<ReportListBean> reportList) {
        this.reportList = reportList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(factorList);
        dest.writeTypedList(reportList);
    }

    public static class FactorListBean implements Parcelable{

        private String factorName;
        private String reference;
        private List<ListBean> list;

        protected FactorListBean(Parcel in) {
            factorName = in.readString();
            reference = in.readString();
            list = in.createTypedArrayList(ListBean.CREATOR);
        }

        public static final Creator<FactorListBean> CREATOR = new Creator<FactorListBean>() {
            @Override
            public FactorListBean createFromParcel(Parcel in) {
                return new FactorListBean(in);
            }

            @Override
            public FactorListBean[] newArray(int size) {
                return new FactorListBean[size];
            }
        };

        public String getFactorName() {
            return factorName;
        }

        public void setFactorName(String factorName) {
            this.factorName = factorName;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(factorName);
            dest.writeString(reference);
            dest.writeTypedList(list);
        }
    }
    public static class ReportListBean implements Parcelable{
        private String illnessCode;
        private String illnessName;
        private int riskLevel;
        private String illnessStatus;
        private String controlStatus;
        private String morbidity;
        private String result;
        private String advice;
        private List<FactorListBeanX> factorList;

        protected ReportListBean(Parcel in) {
            illnessCode = in.readString();
            illnessName = in.readString();
            riskLevel = in.readInt();
            illnessStatus = in.readString();
            controlStatus = in.readString();
            morbidity = in.readString();
            result = in.readString();
            advice = in.readString();
            factorList = in.createTypedArrayList(FactorListBeanX.CREATOR);
        }

        public static final Creator<ReportListBean> CREATOR = new Creator<ReportListBean>() {
            @Override
            public ReportListBean createFromParcel(Parcel in) {
                return new ReportListBean(in);
            }

            @Override
            public ReportListBean[] newArray(int size) {
                return new ReportListBean[size];
            }
        };

        public String getIllnessCode() {
            return illnessCode;
        }

        public void setIllnessCode(String illnessCode) {
            this.illnessCode = illnessCode;
        }

        public String getIllnessName() {
            return illnessName;
        }

        public void setIllnessName(String illnessName) {
            this.illnessName = illnessName;
        }

        public int getRiskLevel() {
            return riskLevel;
        }

        public void setRiskLevel(int riskLevel) {
            this.riskLevel = riskLevel;
        }

        public String getIllnessStatus() {
            return illnessStatus;
        }

        public void setIllnessStatus(String illnessStatus) {
            this.illnessStatus = illnessStatus;
        }

        public String getControlStatus() {
            return controlStatus;
        }

        public void setControlStatus(String controlStatus) {
            this.controlStatus = controlStatus;
        }

        public String getMorbidity() {
            return morbidity;
        }

        public void setMorbidity(String morbidity) {
            this.morbidity = morbidity;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getAdvice() {
            return advice;
        }

        public void setAdvice(String advice) {
            this.advice = advice;
        }

        public List<FactorListBeanX> getFactorList() {
            return factorList;
        }

        public void setFactorList(List<FactorListBeanX> factorList) {
            this.factorList = factorList;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(illnessCode);
            dest.writeString(illnessName);
            dest.writeInt(riskLevel);
            dest.writeString(illnessStatus);
            dest.writeString(controlStatus);
            dest.writeString(morbidity);
            dest.writeString(result);
            dest.writeString(advice);
            dest.writeTypedList(factorList);
        }
    }

    public static class FactorListBeanX implements Parcelable{

        private String factorName;
        private String reference;
        private List<ListBean> list;

        protected FactorListBeanX(Parcel in) {
            factorName = in.readString();
            reference = in.readString();
            list = in.createTypedArrayList(ListBean.CREATOR);
        }

        public static final Creator<FactorListBeanX> CREATOR = new Creator<FactorListBeanX>() {
            @Override
            public FactorListBeanX createFromParcel(Parcel in) {
                return new FactorListBeanX(in);
            }

            @Override
            public FactorListBeanX[] newArray(int size) {
                return new FactorListBeanX[size];
            }
        };

        public String getFactorName() {
            return factorName;
        }

        public void setFactorName(String factorName) {
            this.factorName = factorName;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(factorName);
            dest.writeString(reference);
            dest.writeTypedList(list);
        }
    }

    public static class ListBean implements Parcelable{

        private String hdFactorRecordId;
        private String hdHealthSurveyId;
        private int userId;
        private String fatherCode;
        private String factorCode;
        private String value;
        private String anomalyStatus;
        private int score;

        protected ListBean(Parcel in) {
            hdFactorRecordId = in.readString();
            hdHealthSurveyId = in.readString();
            userId = in.readInt();
            fatherCode = in.readString();
            factorCode = in.readString();
            value = in.readString();
            anomalyStatus = in.readString();
            score = in.readInt();
        }

        public static final Creator<ListBean> CREATOR = new Creator<ListBean>() {
            @Override
            public ListBean createFromParcel(Parcel in) {
                return new ListBean(in);
            }

            @Override
            public ListBean[] newArray(int size) {
                return new ListBean[size];
            }
        };

        public String getHdFactorRecordId() {
            return hdFactorRecordId;
        }

        public void setHdFactorRecordId(String hdFactorRecordId) {
            this.hdFactorRecordId = hdFactorRecordId;
        }

        public String getHdHealthSurveyId() {
            return hdHealthSurveyId;
        }

        public void setHdHealthSurveyId(String hdHealthSurveyId) {
            this.hdHealthSurveyId = hdHealthSurveyId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getFatherCode() {
            return fatherCode;
        }

        public void setFatherCode(String fatherCode) {
            this.fatherCode = fatherCode;
        }

        public String getFactorCode() {
            return factorCode;
        }

        public void setFactorCode(String factorCode) {
            this.factorCode = factorCode;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getAnomalyStatus() {
            return anomalyStatus;
        }

        public void setAnomalyStatus(String anomalyStatus) {
            this.anomalyStatus = anomalyStatus;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(hdFactorRecordId);
            dest.writeString(hdHealthSurveyId);
            dest.writeInt(userId);
            dest.writeString(fatherCode);
            dest.writeString(factorCode);
            dest.writeString(value);
            dest.writeString(anomalyStatus);
            dest.writeInt(score);
        }
    }
}
