package com.example.han.referralproject.single_measure.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/1 14:00
 * created by:gzq
 * description:TODO
 */
public class FirstReportBean implements Parcelable{
    private RiskBean dm;
    private RiskBean fat;
    private RiskBean htn;
    private RiskBean icvd;
    private List<FactorListBean> factorList;

    protected FirstReportBean(Parcel in) {
        dm = in.readParcelable(RiskBean.class.getClassLoader());
        fat = in.readParcelable(RiskBean.class.getClassLoader());
        htn = in.readParcelable(RiskBean.class.getClassLoader());
        icvd = in.readParcelable(RiskBean.class.getClassLoader());
        factorList = in.createTypedArrayList(FactorListBean.CREATOR);
    }

    public static final Creator<FirstReportBean> CREATOR = new Creator<FirstReportBean>() {
        @Override
        public FirstReportBean createFromParcel(Parcel in) {
            return new FirstReportBean(in);
        }

        @Override
        public FirstReportBean[] newArray(int size) {
            return new FirstReportBean[size];
        }
    };

    public RiskBean getDm() {
        return dm;
    }

    public void setDm(RiskBean dm) {
        this.dm = dm;
    }

    public RiskBean getFat() {
        return fat;
    }

    public void setFat(RiskBean fat) {
        this.fat = fat;
    }

    public RiskBean getHtn() {
        return htn;
    }

    public void setHtn(RiskBean htn) {
        this.htn = htn;
    }

    public RiskBean getIcvd() {
        return icvd;
    }

    public void setIcvd(RiskBean icvd) {
        this.icvd = icvd;
    }

    public List<FactorListBean> getFactorList() {
        return factorList;
    }

    public void setFactorList(List<FactorListBean> factorList) {
        this.factorList = factorList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(dm, flags);
        dest.writeParcelable(fat, flags);
        dest.writeParcelable(htn, flags);
        dest.writeParcelable(icvd, flags);
        dest.writeTypedList(factorList);
    }

    public static class RiskBean implements Parcelable{

        private String controlStatus;
        private String illnessCode;
        private String illnessName;
        private String illnessStatus;
        private String morbidity;
        private int riskLevel;
        private String advice;
        private String result;
        private List<FactorListBean> factorList;

        protected RiskBean(Parcel in) {
            controlStatus = in.readString();
            illnessCode = in.readString();
            illnessName = in.readString();
            illnessStatus = in.readString();
            morbidity = in.readString();
            riskLevel = in.readInt();
            advice = in.readString();
            result = in.readString();
            factorList = in.createTypedArrayList(FactorListBean.CREATOR);
        }

        public static final Creator<RiskBean> CREATOR = new Creator<RiskBean>() {
            @Override
            public RiskBean createFromParcel(Parcel in) {
                return new RiskBean(in);
            }

            @Override
            public RiskBean[] newArray(int size) {
                return new RiskBean[size];
            }
        };

        public String getControlStatus() {
            return controlStatus;
        }

        public void setControlStatus(String controlStatus) {
            this.controlStatus = controlStatus;
        }

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

        public String getIllnessStatus() {
            return illnessStatus;
        }

        public void setIllnessStatus(String illnessStatus) {
            this.illnessStatus = illnessStatus;
        }

        public String getMorbidity() {
            return morbidity;
        }

        public void setMorbidity(String morbidity) {
            this.morbidity = morbidity;
        }

        public int getRiskLevel() {
            return riskLevel;
        }

        public void setRiskLevel(int riskLevel) {
            this.riskLevel = riskLevel;
        }

        public String getAdvice() {
            return advice;
        }

        public void setAdvice(String advice) {
            this.advice = advice;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public List<FactorListBean> getFactorList() {
            return factorList;
        }

        public void setFactorList(List<FactorListBean> factorList) {
            this.factorList = factorList;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(controlStatus);
            dest.writeString(illnessCode);
            dest.writeString(illnessName);
            dest.writeString(illnessStatus);
            dest.writeString(morbidity);
            dest.writeInt(riskLevel);
            dest.writeString(advice);
            dest.writeString(result);
            dest.writeTypedList(factorList);
        }
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

    public static class ListBean implements Parcelable{
        private String anomalyStatus;
        private String factorCode;
        private String fatherCode;
        private String hdFactorRecordId;
        private String hdHealthSurveyId;
        private int score;
        private int userId;
        private String value;

        protected ListBean(Parcel in) {
            anomalyStatus = in.readString();
            factorCode = in.readString();
            fatherCode = in.readString();
            hdFactorRecordId = in.readString();
            hdHealthSurveyId = in.readString();
            score = in.readInt();
            userId = in.readInt();
            value = in.readString();
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

        public String getAnomalyStatus() {
            return anomalyStatus;
        }

        public void setAnomalyStatus(String anomalyStatus) {
            this.anomalyStatus = anomalyStatus;
        }

        public String getFactorCode() {
            return factorCode;
        }

        public void setFactorCode(String factorCode) {
            this.factorCode = factorCode;
        }

        public String getFatherCode() {
            return fatherCode;
        }

        public void setFatherCode(String fatherCode) {
            this.fatherCode = fatherCode;
        }

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

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(anomalyStatus);
            dest.writeString(factorCode);
            dest.writeString(fatherCode);
            dest.writeString(hdFactorRecordId);
            dest.writeString(hdHealthSurveyId);
            dest.writeInt(score);
            dest.writeInt(userId);
            dest.writeString(value);
        }
    }
}
