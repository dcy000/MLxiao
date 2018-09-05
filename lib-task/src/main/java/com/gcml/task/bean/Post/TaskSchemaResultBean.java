package com.gcml.task.bean.Post;

import java.io.Serializable;
import java.util.List;

public class TaskSchemaResultBean implements Serializable {

    /**
     * attitude : string
     * awareness : string
     * compliance : string
     * detectionPlan : {"GLU":"string","HTN":"string","glu":"string","htn":"string","weight":"string"}
     * initiative : string
     * intake : {"drink":"string","grease":"string","naSalt":"string","smoke":"string"}
     * result : string
     * sportRecommend : {"sportLevel":"string","sportList":[{"aerobicType":"string","alias":"string","consumption":"string","diabetesEffect":"string","hmSportId":"string","hypertensionEffect":"string","imgUrl":"string","indoorType":"string","introduce":"string","name":"string","sportEffect":"string","sportLevel":"string"}],"sportRate":"string","sportStr":"string","sportTime":"string","sportWeekTime":0,"timeCost":0,"weekCount":0}
     */

    public String attitude;
    public String awareness;
    public String compliance;
    public DetectionPlanBean detectionPlan;
    public String initiative;
    public IntakeBean intake;
    public String result;
    public SportRecommendBean sportRecommend;

    public static class DetectionPlanBean implements Serializable {
        /**
         * GLU : string
         * HTN : string
         * glu : string
         * htn : string
         * weight : string
         */

        public String GLU;
        public String HTN;
        public String glu;
        public String htn;
        public String weight;

    }

    public static class IntakeBean implements Serializable {
        /**
         * drink : string
         * grease : string
         * naSalt : string
         * smoke : string
         */

        public String drink;
        public String grease;
        public String naSalt;
        public String smoke;

    }

    public static class SportRecommendBean implements Serializable {
        /**
         * sportLevel : string
         * sportList : [{"aerobicType":"string","alias":"string","consumption":"string","diabetesEffect":"string","hmSportId":"string","hypertensionEffect":"string","imgUrl":"string","indoorType":"string","introduce":"string","name":"string","sportEffect":"string","sportLevel":"string"}]
         * sportRate : string
         * sportStr : string
         * sportTime : string
         * sportWeekTime : 0
         * timeCost : 0
         * weekCount : 0
         * weightTarget : 0.00
         */

        public String sportLevel;
        public String sportRate;
        public String sportStr;
        public String sportTime;
        public int sportWeekTime;
        public int timeCost;
        public int weekCount;
        public double weightTarget;
        public List<SportListBean> sportList;

        public static class SportListBean implements Serializable {
            /**
             * aerobicType : string
             * alias : string
             * consumption : string
             * diabetesEffect : string
             * hmSportId : string
             * hypertensionEffect : string
             * imgUrl : string
             * indoorType : string
             * introduce : string
             * name : string
             * sportEffect : string
             * sportLevel : string
             */

            public String aerobicType;
            public String alias;
            public String consumption;
            public String diabetesEffect;
            public String hmSportId;
            public String hypertensionEffect;
            public String imgUrl;
            public String indoorType;
            public String introduce;
            public String name;
            public String sportEffect;
            public String sportLevel;

        }
    }
}
