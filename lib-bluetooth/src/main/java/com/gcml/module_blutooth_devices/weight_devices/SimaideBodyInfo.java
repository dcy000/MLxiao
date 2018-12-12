package com.gcml.module_blutooth_devices.weight_devices;

public class SimaideBodyInfo {
    public static final int TYPE_WEIGHT_NORMAL = 200; // 体重动态数据
    public static final int TYPE_NETWORK_EXECTION = 201; // 网络错误

    public static final int TYPE_NORMAL = 0; // 正常测量

    public static final int TYPE_INTERNAL = 3001; // 内部错误
    public static final int TYPE_RECORD_FAIL = 3002; // 创建访问记录失败

    public static final int TYPE_VENDOR_NOT_EXIST = 4001; // 厂商不存在
    public static final int TYPE_VENDOR_BANNED = 4002; // 厂商被禁用
    public static final int TYPE_SOURCE_UNKNOWN = 4003; // 数据来源不明确
    public static final int TYPE_USER_MISSING = 4004; // 用户信息缺失
    public static final int TYPE_PROTOCOL_UNKNOWN = 4005; // 协议类型未识别
    public static final int TYPE_USERINFO_INVALID = 4006; // 用户信息出错
    public static final int TYPE_BMI_OUT_RANGE = 4007; // BMI不在合理区间
    public static final int TYPE_RVALUE_INVALID = 4008; // RValue无效，即没测到阻值(比如穿着鞋上称)
    public static final int TYPE_RVALUE_OUT_RANGE = 4009; // RValue不在合理区间，如：Rvalue<300或RValue>4000
    public static final int TYPE_UNAUTHORIZED = 4010; // 未登录
    public static final int TYPE_KEYWORD_MISSING = 4011;//请传入keyword参数


    /**
     * code : 0
     * details : {"ageOfBody":35,"bmi":19.9,"bmr":1455,"bodyShape":5,"desirableWeight":51.8,"fatFreeBodyWeight":39.7,"fatToControl":0,"idealWeight":56,"levelOfVisceralFat":7,"muscleToControl":3.6,"obesityLevel":0,"rateOfBurnFat":{"max":148,"min":111},"ratioOfFat":22.9,"ratioOfMuscle":27,"ratioOfProtein":17.6,"ratioOfSubcutaneousFat":18.6,"ratioOfWater":56,"score":84.3,"stateOfNutrition":4,"weight":51.5,"weightOfBone":2.3,"weightOfFat":11.8,"weightOfMuscle":13.9,"weightOfProtein":-10000,"weightOfSkeletalMuscle":9.9,"weightOfWater":28.8,"weightToControl":-0.3}
     * msg : success
     */

    private int code;
    private DetailsBean details;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DetailsBean getDetails() {
        return details;
    }

    public void setDetails(DetailsBean details) {
        this.details = details;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DetailsBean {
        public DetailsBean() {
            statusCode = 100;
            weight = 0f;
            bmi = 0f;
            ratioOfFat = 0f;
            weightOfFat = 0f;
            ratioOfMuscle = 0f;
            ratioOfSkeletalMuscle = -10000;
            weightOfMuscle = 0f;
            ratioOfWater = 0f;
            weightOfWater = 0f;
            ratioOfProtein = 0f;
            weightOfProtein = 0f;
            bmr = 0;
            ageOfBody = 0;
            levelOfVisceralFat = 0;
            obesityLevel = -10000;
            ratioOfSubcutaneousFat = 0f;
            desirableWeight = 0f;
            idealWeight = 0f;
            weightOfBone = 0f;
            fatFreeBodyWeight = 0f;
            weightOfSkeletalMuscle = 0f;
            weightToControl = 0f;
            fatToControl = 0f;
            muscleToControl = 0f;
            score = 0;

            bodyShape = -10000;
            stateOfNutrition = -10000;
            rateOfBurnFat = new RateOfBurnFatBean(0, 0);

            leftLegImp = -10000f;
            leftLegRatioOfFat = -10000f;
            leftLegRatioOfMuscle = -10000f;
            rightLegImp = -10000f;
            rightLegRatioOfFat = -10000f;
            rightLegRatioOfMuscle = -10000f;

            leftArmImp = -10000f;
            leftArmRatioOfFat = -10000f;
            leftArmRatioOfMuscle = -10000f;
            rightArmImp = -10000f;
            rightArmRatioOfFat = -10000f;
            rightArmRatioOfMuscle = -10000f;
        }

        /**
         * ageOfBody : 35
         * bmi : 19.9
         * bmr : 1455
         * bodyShape : 5
         * desirableWeight : 51.8
         * fatFreeBodyWeight : 39.7
         * fatToControl : 0.0
         * idealWeight : 56.0
         * levelOfVisceralFat : 7
         * muscleToControl : 3.6
         * obesityLevel : 0
         * rateOfBurnFat : {"max":148,"min":111}
         * ratioOfFat : 22.9
         * ratioOfMuscle : 27.0
         * ratioOfProtein : 17.6
         * ratioOfSubcutaneousFat : 18.6
         * ratioOfWater : 56.0
         * score : 84.3
         * stateOfNutrition : 4
         * weight : 51.5
         * weightOfBone : 2.3
         * weightOfFat : 11.8
         * weightOfMuscle : 13.9
         * weightOfProtein : -10000
         * weightOfSkeletalMuscle : 9.9
         * weightOfWater : 28.8
         * weightToControl : -0.3
         * sn : "12:30:00:B7:E4:33"
         */


        private int statusCode;
        private int code;
        /**
         * weight, Unit is Kg
         */
        private float weight;
        private float[] weightRange;

        /**
         * bmi, return 0 means error
         */
        private float bmi;
        private float[] bmiRange;

        private float rValue;

        /**
         * ratio of Fat, from 2.0 to 50.0, return 0 means error
         */
        private float ratioOfFat;
        private float[] ratioOfFatRange;

        /**
         * weight of Fat, return 0 means error
         */
        private float weightOfFat;
        private float[] weightOfFatRange;

        /**
         * ratio of Muscle, return 0 means error
         */
        private float ratioOfMuscle;
        private float[] ratioOfMuscleRange;

        private float ratioOfSkeletalMuscle;
        private float[] ratioOfSkeletalMuscleRange;

        /**
         * weight of Muscle, return 0 means error
         */
        private float weightOfMuscle;
        private float[] weightOfMuscleRange;

        /**
         * ratio of Water
         */
        private float ratioOfWater;
        private float[] ratioOfWaterRange;

        /**
         * weight of Water
         */
        private float weightOfWater;
        private float[] weightOfWaterRange;

        /**
         * ratio of Protein
         */
        private float ratioOfProtein;
        private float[] ratioOfProteinRange;

        /**
         * weight of Protein
         */
        private float weightOfProtein;
        private float[] weightOfProteinRange;

        /**
         * basal metabolic rate
         */
        private int bmr;
        private int[] bmrRange;

        /**
         * age of body
         */
        private int ageOfBody;

        /**
         * level of visceral fat
         */
        private int levelOfVisceralFat;
        private int[] levelOfVisceralFatRange;

        /**
         * level of Obesityt
         */
        private int obesityLevel;

        /**
         * ratio of subcutaneous fat
         */
        private float ratioOfSubcutaneousFat;
        private float[] ratioOfSubcutaneousFatRange;

        /**
         * desirable Weight
         */
        private float desirableWeight;

        /**
         * ideal Weight
         */
        private float idealWeight;

        /**
         * weight of Bone
         */
        private float weightOfBone;
        private float[] weightOfBoneRange;

        /**
         * 去脂体重
         */
        private float fatFreeBodyWeight;

        /**
         * 骨骼肌重量
         */
        private float weightOfSkeletalMuscle;

        /**
         * weight to control, positive number means to reduce, negtive number means to increase
         */
        private float weightToControl;

        /**
         * weight to control, positive number means to reduce, the value is more or equal to 0.
         */
        private float fatToControl;

        /**
         * muscle to control, positive number means to reduce, the value is more or equal to 0.
         */
        private float muscleToControl;

        /**
         * body shape according to BMI
         */
        private int bodyShape;

        /**
         * nutrition state of body
         */
        private int stateOfNutrition;

        /**
         * score of the body, 0 means error
         */
        private int score;
        /**
         * 燃脂心率
         */
        private RateOfBurnFatBean rateOfBurnFat;
        private String sn;
        private float[] ratioOfHipRange;

        private float leftLegImp;
        private float leftLegRatioOfFat;
        private float leftLegRatioOfMuscle;
        private float rightLegImp;
        private float rightLegRatioOfFat;
        private float rightLegRatioOfMuscle;

        private float leftArmImp;
        private float leftArmRatioOfFat;
        private float leftArmRatioOfMuscle;
        private float rightArmImp;
        private float rightArmRatioOfFat;
        private float rightArmRatioOfMuscle;

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public float getWeight() {
            return weight;
        }

        public void setWeight(float weight) {
            this.weight = weight;
        }

        public float[] getWeightRange() {
            return weightRange;
        }

        public void setWeightRange(float[] weightRange) {
            this.weightRange = weightRange;
        }

        public float getBmi() {
            return bmi;
        }

        public void setBmi(float bmi) {
            this.bmi = bmi;
        }

        public float[] getBmiRange() {
            return bmiRange;
        }

        public void setBmiRange(float[] bmiRange) {
            this.bmiRange = bmiRange;
        }

        public float getrValue() {
            return rValue;
        }

        public void setrValue(float rValue) {
            this.rValue = rValue;
        }

        public float getRatioOfFat() {
            return ratioOfFat;
        }

        public void setRatioOfFat(float ratioOfFat) {
            this.ratioOfFat = ratioOfFat;
        }

        public float[] getRatioOfFatRange() {
            return ratioOfFatRange;
        }

        public void setRatioOfFatRange(float[] ratioOfFatRange) {
            this.ratioOfFatRange = ratioOfFatRange;
        }

        public float getWeightOfFat() {
            return weightOfFat;
        }

        public void setWeightOfFat(float weightOfFat) {
            this.weightOfFat = weightOfFat;
        }

        public float[] getWeightOfFatRange() {
            return weightOfFatRange;
        }

        public void setWeightOfFatRange(float[] weightOfFatRange) {
            this.weightOfFatRange = weightOfFatRange;
        }

        public float getRatioOfMuscle() {
            return ratioOfMuscle;
        }

        public void setRatioOfMuscle(float ratioOfMuscle) {
            this.ratioOfMuscle = ratioOfMuscle;
        }

        public float[] getRatioOfMuscleRange() {
            return ratioOfMuscleRange;
        }

        public void setRatioOfMuscleRange(float[] ratioOfMuscleRange) {
            this.ratioOfMuscleRange = ratioOfMuscleRange;
        }

        public float getRatioOfSkeletalMuscle() {
            return ratioOfSkeletalMuscle;
        }

        public void setRatioOfSkeletalMuscle(float ratioOfSkeletalMuscle) {
            this.ratioOfSkeletalMuscle = ratioOfSkeletalMuscle;
        }

        public float[] getRatioOfSkeletalMuscleRange() {
            return ratioOfSkeletalMuscleRange;
        }

        public void setRatioOfSkeletalMuscleRange(float[] ratioOfSkeletalMuscleRange) {
            this.ratioOfSkeletalMuscleRange = ratioOfSkeletalMuscleRange;
        }

        public float getWeightOfMuscle() {
            return weightOfMuscle;
        }

        public void setWeightOfMuscle(float weightOfMuscle) {
            this.weightOfMuscle = weightOfMuscle;
        }

        public float[] getWeightOfMuscleRange() {
            return weightOfMuscleRange;
        }

        public void setWeightOfMuscleRange(float[] weightOfMuscleRange) {
            this.weightOfMuscleRange = weightOfMuscleRange;
        }

        public float getRatioOfWater() {
            return ratioOfWater;
        }

        public void setRatioOfWater(float ratioOfWater) {
            this.ratioOfWater = ratioOfWater;
        }

        public float[] getRatioOfWaterRange() {
            return ratioOfWaterRange;
        }

        public void setRatioOfWaterRange(float[] ratioOfWaterRange) {
            this.ratioOfWaterRange = ratioOfWaterRange;
        }

        public float getWeightOfWater() {
            return weightOfWater;
        }

        public void setWeightOfWater(float weightOfWater) {
            this.weightOfWater = weightOfWater;
        }

        public float[] getWeightOfWaterRange() {
            return weightOfWaterRange;
        }

        public void setWeightOfWaterRange(float[] weightOfWaterRange) {
            this.weightOfWaterRange = weightOfWaterRange;
        }

        public float getRatioOfProtein() {
            return ratioOfProtein;
        }

        public void setRatioOfProtein(float ratioOfProtein) {
            this.ratioOfProtein = ratioOfProtein;
        }

        public float[] getRatioOfProteinRange() {
            return ratioOfProteinRange;
        }

        public void setRatioOfProteinRange(float[] ratioOfProteinRange) {
            this.ratioOfProteinRange = ratioOfProteinRange;
        }

        public float getWeightOfProtein() {
            return weightOfProtein;
        }

        public void setWeightOfProtein(float weightOfProtein) {
            this.weightOfProtein = weightOfProtein;
        }

        public float[] getWeightOfProteinRange() {
            return weightOfProteinRange;
        }

        public void setWeightOfProteinRange(float[] weightOfProteinRange) {
            this.weightOfProteinRange = weightOfProteinRange;
        }

        public int getBmr() {
            return bmr;
        }

        public void setBmr(int bmr) {
            this.bmr = bmr;
        }

        public int[] getBmrRange() {
            return bmrRange;
        }

        public void setBmrRange(int[] bmrRange) {
            this.bmrRange = bmrRange;
        }

        public int getAgeOfBody() {
            return ageOfBody;
        }

        public void setAgeOfBody(int ageOfBody) {
            this.ageOfBody = ageOfBody;
        }

        public int getLevelOfVisceralFat() {
            return levelOfVisceralFat;
        }

        public void setLevelOfVisceralFat(int levelOfVisceralFat) {
            this.levelOfVisceralFat = levelOfVisceralFat;
        }

        public int[] getLevelOfVisceralFatRange() {
            return levelOfVisceralFatRange;
        }

        public void setLevelOfVisceralFatRange(int[] levelOfVisceralFatRange) {
            this.levelOfVisceralFatRange = levelOfVisceralFatRange;
        }

        public int getObesityLevel() {
            return obesityLevel;
        }

        public void setObesityLevel(int obesityLevel) {
            this.obesityLevel = obesityLevel;
        }

        public float getRatioOfSubcutaneousFat() {
            return ratioOfSubcutaneousFat;
        }

        public void setRatioOfSubcutaneousFat(float ratioOfSubcutaneousFat) {
            this.ratioOfSubcutaneousFat = ratioOfSubcutaneousFat;
        }

        public float[] getRatioOfSubcutaneousFatRange() {
            return ratioOfSubcutaneousFatRange;
        }

        public void setRatioOfSubcutaneousFatRange(float[] ratioOfSubcutaneousFatRange) {
            this.ratioOfSubcutaneousFatRange = ratioOfSubcutaneousFatRange;
        }

        public float getDesirableWeight() {
            return desirableWeight;
        }

        public void setDesirableWeight(float desirableWeight) {
            this.desirableWeight = desirableWeight;
        }

        public float getIdealWeight() {
            return idealWeight;
        }

        public void setIdealWeight(float idealWeight) {
            this.idealWeight = idealWeight;
        }

        public float getWeightOfBone() {
            return weightOfBone;
        }

        public void setWeightOfBone(float weightOfBone) {
            this.weightOfBone = weightOfBone;
        }

        public float[] getWeightOfBoneRange() {
            return weightOfBoneRange;
        }

        public void setWeightOfBoneRange(float[] weightOfBoneRange) {
            this.weightOfBoneRange = weightOfBoneRange;
        }

        public float getFatFreeBodyWeight() {
            return fatFreeBodyWeight;
        }

        public void setFatFreeBodyWeight(float fatFreeBodyWeight) {
            this.fatFreeBodyWeight = fatFreeBodyWeight;
        }

        public float getWeightOfSkeletalMuscle() {
            return weightOfSkeletalMuscle;
        }

        public void setWeightOfSkeletalMuscle(float weightOfSkeletalMuscle) {
            this.weightOfSkeletalMuscle = weightOfSkeletalMuscle;
        }

        public float getWeightToControl() {
            return weightToControl;
        }

        public void setWeightToControl(float weightToControl) {
            this.weightToControl = weightToControl;
        }

        public float getFatToControl() {
            return fatToControl;
        }

        public void setFatToControl(float fatToControl) {
            this.fatToControl = fatToControl;
        }

        public float getMuscleToControl() {
            return muscleToControl;
        }

        public void setMuscleToControl(float muscleToControl) {
            this.muscleToControl = muscleToControl;
        }

        public int getBodyShape() {
            return bodyShape;
        }

        public void setBodyShape(int bodyShape) {
            this.bodyShape = bodyShape;
        }

        public int getStateOfNutrition() {
            return stateOfNutrition;
        }

        public void setStateOfNutrition(int stateOfNutrition) {
            this.stateOfNutrition = stateOfNutrition;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public RateOfBurnFatBean getRateOfBurnFat() {
            return rateOfBurnFat;
        }

        public void setRateOfBurnFat(RateOfBurnFatBean rateOfBurnFat) {
            this.rateOfBurnFat = rateOfBurnFat;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public float[] getRatioOfHipRange() {
            return ratioOfHipRange;
        }

        public void setRatioOfHipRange(float[] ratioOfHipRange) {
            this.ratioOfHipRange = ratioOfHipRange;
        }

        public float getLeftLegImp() {
            return leftLegImp;
        }

        public void setLeftLegImp(float leftLegImp) {
            this.leftLegImp = leftLegImp;
        }

        public float getLeftLegRatioOfFat() {
            return leftLegRatioOfFat;
        }

        public void setLeftLegRatioOfFat(float leftLegRatioOfFat) {
            this.leftLegRatioOfFat = leftLegRatioOfFat;
        }

        public float getLeftLegRatioOfMuscle() {
            return leftLegRatioOfMuscle;
        }

        public void setLeftLegRatioOfMuscle(float leftLegRatioOfMuscle) {
            this.leftLegRatioOfMuscle = leftLegRatioOfMuscle;
        }

        public float getRightLegImp() {
            return rightLegImp;
        }

        public void setRightLegImp(float rightLegImp) {
            this.rightLegImp = rightLegImp;
        }

        public float getRightLegRatioOfFat() {
            return rightLegRatioOfFat;
        }

        public void setRightLegRatioOfFat(float rightLegRatioOfFat) {
            this.rightLegRatioOfFat = rightLegRatioOfFat;
        }

        public float getRightLegRatioOfMuscle() {
            return rightLegRatioOfMuscle;
        }

        public void setRightLegRatioOfMuscle(float rightLegRatioOfMuscle) {
            this.rightLegRatioOfMuscle = rightLegRatioOfMuscle;
        }

        public float getLeftArmImp() {
            return leftArmImp;
        }

        public void setLeftArmImp(float leftArmImp) {
            this.leftArmImp = leftArmImp;
        }

        public float getLeftArmRatioOfFat() {
            return leftArmRatioOfFat;
        }

        public void setLeftArmRatioOfFat(float leftArmRatioOfFat) {
            this.leftArmRatioOfFat = leftArmRatioOfFat;
        }

        public float getLeftArmRatioOfMuscle() {
            return leftArmRatioOfMuscle;
        }

        public void setLeftArmRatioOfMuscle(float leftArmRatioOfMuscle) {
            this.leftArmRatioOfMuscle = leftArmRatioOfMuscle;
        }

        public float getRightArmImp() {
            return rightArmImp;
        }

        public void setRightArmImp(float rightArmImp) {
            this.rightArmImp = rightArmImp;
        }

        public float getRightArmRatioOfFat() {
            return rightArmRatioOfFat;
        }

        public void setRightArmRatioOfFat(float rightArmRatioOfFat) {
            this.rightArmRatioOfFat = rightArmRatioOfFat;
        }

        public float getRightArmRatioOfMuscle() {
            return rightArmRatioOfMuscle;
        }

        public void setRightArmRatioOfMuscle(float rightArmRatioOfMuscle) {
            this.rightArmRatioOfMuscle = rightArmRatioOfMuscle;
        }

        public static class RateOfBurnFatBean {
            /**
             * max : 148
             * min : 111
             */

            private int max;
            private int min;

            public RateOfBurnFatBean(int max, int min) {
                this.min = min;
                this.max = max;
            }

            public int getMax() {
                return max;
            }

            public void setMax(int max) {
                this.max = max;
            }

            public int getMin() {
                return min;
            }

            public void setMin(int min) {
                this.min = min;
            }
        }
    }
}
