package com.example.han.referralproject.blood_pressure_risk_assessment;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2018/5/7.
 */

public class EssentialHypertension implements Parcelable{

    /**
     * illnessType : 0
     * primary : {"analysis":"根据您的检测结果，您有可能是原发性高血压，并且可能存在心、脑、肾脏、眼的靶器官损害，根据您的体质主要影响血压的因素是：","illnessFactor":["遗传"],"advice":"建议您及时联系顾问，检查靶器官损害情况，同时保持健康饮食，合理运动，根据康复疗程进行生活干预。"}
     * secondary : null
     * targets : ["心","脑","肾脏","眼"]
     */

    private String illnessType;
    private PrimaryBean primary;
    private List<String> targets;


    protected EssentialHypertension(Parcel in) {
        illnessType = in.readString();
        primary = in.readParcelable(PrimaryBean.class.getClassLoader());
        targets = in.createStringArrayList();
    }

    public static final Creator<EssentialHypertension> CREATOR = new Creator<EssentialHypertension>() {
        @Override
        public EssentialHypertension createFromParcel(Parcel in) {
            return new EssentialHypertension(in);
        }

        @Override
        public EssentialHypertension[] newArray(int size) {
            return new EssentialHypertension[size];
        }
    };

    public String getIllnessType() {
        return illnessType;
    }

    public void setIllnessType(String illnessType) {
        this.illnessType = illnessType;
    }

    public PrimaryBean getPrimary() {
        return primary;
    }

    public void setPrimary(PrimaryBean primary) {
        this.primary = primary;
    }

    public List<String> getTargets() {
        return targets;
    }

    public void setTargets(List<String> targets) {
        this.targets = targets;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(illnessType);
        dest.writeParcelable(primary, flags);
        dest.writeStringList(targets);
    }

    public static class PrimaryBean implements Parcelable{
        /**
         * analysis : 根据您的检测结果，您有可能是原发性高血压，并且可能存在心、脑、肾脏、眼的靶器官损害，根据您的体质主要影响血压的因素是：
         * illnessFactor : ["遗传"]
         * advice : 建议您及时联系顾问，检查靶器官损害情况，同时保持健康饮食，合理运动，根据康复疗程进行生活干预。
         */

        private String analysis;
        private String advice;
        private List<String> illnessFactor;

        protected PrimaryBean(Parcel in) {
            analysis = in.readString();
            advice = in.readString();
            illnessFactor = in.createStringArrayList();
        }

        public static final Creator<PrimaryBean> CREATOR = new Creator<PrimaryBean>() {
            @Override
            public PrimaryBean createFromParcel(Parcel in) {
                return new PrimaryBean(in);
            }

            @Override
            public PrimaryBean[] newArray(int size) {
                return new PrimaryBean[size];
            }
        };

        public String getAnalysis() {
            return analysis;
        }

        public void setAnalysis(String analysis) {
            this.analysis = analysis;
        }

        public String getAdvice() {
            return advice;
        }

        public void setAdvice(String advice) {
            this.advice = advice;
        }

        public List<String> getIllnessFactor() {
            return illnessFactor;
        }

        public void setIllnessFactor(List<String> illnessFactor) {
            this.illnessFactor = illnessFactor;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(analysis);
            dest.writeString(advice);
            dest.writeStringList(illnessFactor);
        }
    }
}
