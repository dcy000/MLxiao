package com.example.han.referralproject.yiyuan.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/6/29.
 */

public class ExaminationReportBean implements Serializable {

    /**
     * tag : true
     * code : 200
     * data : {"hiHealthExaminationId":"string","healthExaminationType":"null","equipmentId":"string","userId":0,"weight":0,"bloodOxygen":0,"leftHypertension":{"highPressure":0,"lowPressure":0},"rightHypertension":{"highPressure":0,"lowPressure":0},"temperAture":0,"heartRate":0,"bloodSugar":0,"sugarTime":0,"pulse":0,"ecg":"string","cholesterol":0,"uricAcid":0,"healthSymptom":"null","hypertensionSymptom":"null","diabetesSymptom":"null","saltIntake":"string","smoke":"string","wineDrink":"string","sportFrequency":"string","sportIntension":"string","sportCost":"string","watchState":"string","doctorAdvice":"string"}
     * message : 成功
     * error : null
     */

    public boolean tag;
    public int code;
    public DataBean data;
    public String message;
    public Object error;

    public static class DataBean {
        /**
         * hiHealthExaminationId : string
         * healthExaminationType : null
         * equipmentId : string
         * userId : 0
         * weight : 0
         * bloodOxygen : 0
         * leftHypertension : {"highPressure":0,"lowPressure":0}
         * rightHypertension : {"highPressure":0,"lowPressure":0}
         * temperAture : 0
         * heartRate : 0
         * bloodSugar : 0
         * sugarTime : 0
         * pulse : 0
         * ecg : string
         * cholesterol : 0
         * uricAcid : 0
         * healthSymptom : null
         * hypertensionSymptom : null
         * diabetesSymptom : null
         * saltIntake : string
         * smoke : string
         * wineDrink : string
         * sportFrequency : string
         * sportIntension : string
         * sportCost : string
         * watchState : string
         * doctorAdvice : string
         */

        public String hiHealthExaminationId;
        public String healthExaminationType;
        public String equipmentId;
        public int userId;
        public int weight;
        public int bloodOxygen;
        public LeftHypertensionBean leftHypertension;
        public RightHypertensionBean rightHypertension;
        public int temperAture;
        public int heartRate;
        public int bloodSugar;
        public int sugarTime;
        public int pulse;
        public String ecg;
        public int cholesterol;
        public int uricAcid;
        public String healthSymptom;
        public String hypertensionSymptom;
        public String diabetesSymptom;
        public String saltIntake;
        public String smoke;
        public String wineDrink;
        public String sportFrequency;
        public String sportIntension;
        public String sportCost;
        public String watchState;
        public String doctorAdvice;

        public static class LeftHypertensionBean {
            /**
             * highPressure : 0
             * lowPressure : 0
             */

            public int highPressure;
            public int lowPressure;
        }

        public static class RightHypertensionBean {
            /**
             * highPressure : 0
             * lowPressure : 0
             */

            public int highPressure;
            public int lowPressure;
        }
    }
}
