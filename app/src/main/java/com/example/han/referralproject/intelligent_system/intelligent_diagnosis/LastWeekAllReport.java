package com.example.han.referralproject.intelligent_system.intelligent_diagnosis;

/**
 * Created by Administrator on 2018/5/15.
 */

public class LastWeekAllReport {

    /**
     * user : {"bid":100001,"categoryid":10001,"doct":null,"eq":null,"bname":"用户一","sex":"男","dz":"浙江省杭州市","age":-2932,"sfz":"330814495021548745","tel":"18888888888","mh":"冠心病 糖尿病 高血压    ","eqid":"100002","state":1,"qyzt":null,"height":100,"weight":25,"blood_type":"A","eating_habits":"荤素搭配","smoke":"经常吸烟","drink":"经常喝酒","exercise_habits":"每周几次","user_photo":"http://oyptcv2pb.bkt.clouddn.com/2017111010174991772816.jpg","smokeCode":"1","drinkCode":"1","eatingHabitsCode":"1","exerciseHabitsCode":"2"}
     * temperature : 38.062791
     * highPressure : 119
     * highPressureTarget : null
     * lowPressure : 79
     * lowPressureTarget : null
     * bloodSugar : 7.166667
     * bloodSugarTarget : null
     * bloodSugarOne : 8.773333
     * bloodSugarOneTarget : null
     * bloodSugarTwo : 6.874615
     * bloodSugarTwoTarget : null
     * weight : 53.860465
     * weightTarget : null
     * hypertensionFrequency : null
     * diabetesFrequency : null
     */

    private UserBean user;
    private Double temperature;
    private int highPressure;
    private Object highPressureTarget;
    private int lowPressure;
    private Object lowPressureTarget;
    private double bloodSugar;
    private Object bloodSugarTarget;
    private double bloodSugarOne;
    private Object bloodSugarOneTarget;
    private double bloodSugarTwo;
    private Object bloodSugarTwoTarget;
    private double weight;
    private Object weightTarget;
    private Object hypertensionFrequency;
    private Object diabetesFrequency;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public int getHighPressure() {
        return highPressure;
    }

    public void setHighPressure(int highPressure) {
        this.highPressure = highPressure;
    }

    public Object getHighPressureTarget() {
        return highPressureTarget;
    }

    public void setHighPressureTarget(Object highPressureTarget) {
        this.highPressureTarget = highPressureTarget;
    }

    public int getLowPressure() {
        return lowPressure;
    }

    public void setLowPressure(int lowPressure) {
        this.lowPressure = lowPressure;
    }

    public Object getLowPressureTarget() {
        return lowPressureTarget;
    }

    public void setLowPressureTarget(Object lowPressureTarget) {
        this.lowPressureTarget = lowPressureTarget;
    }

    public double getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(double bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public Object getBloodSugarTarget() {
        return bloodSugarTarget;
    }

    public void setBloodSugarTarget(Object bloodSugarTarget) {
        this.bloodSugarTarget = bloodSugarTarget;
    }

    public double getBloodSugarOne() {
        return bloodSugarOne;
    }

    public void setBloodSugarOne(double bloodSugarOne) {
        this.bloodSugarOne = bloodSugarOne;
    }

    public Object getBloodSugarOneTarget() {
        return bloodSugarOneTarget;
    }

    public void setBloodSugarOneTarget(Object bloodSugarOneTarget) {
        this.bloodSugarOneTarget = bloodSugarOneTarget;
    }

    public double getBloodSugarTwo() {
        return bloodSugarTwo;
    }

    public void setBloodSugarTwo(double bloodSugarTwo) {
        this.bloodSugarTwo = bloodSugarTwo;
    }

    public Object getBloodSugarTwoTarget() {
        return bloodSugarTwoTarget;
    }

    public void setBloodSugarTwoTarget(Object bloodSugarTwoTarget) {
        this.bloodSugarTwoTarget = bloodSugarTwoTarget;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Object getWeightTarget() {
        return weightTarget;
    }

    public void setWeightTarget(Object weightTarget) {
        this.weightTarget = weightTarget;
    }

    public Object getHypertensionFrequency() {
        return hypertensionFrequency;
    }

    public void setHypertensionFrequency(Object hypertensionFrequency) {
        this.hypertensionFrequency = hypertensionFrequency;
    }

    public Object getDiabetesFrequency() {
        return diabetesFrequency;
    }

    public void setDiabetesFrequency(Object diabetesFrequency) {
        this.diabetesFrequency = diabetesFrequency;
    }

    public static class UserBean {
        /**
         * bid : 100001
         * categoryid : 10001
         * doct : null
         * eq : null
         * bname : 用户一
         * sex : 男
         * dz : 浙江省杭州市
         * age : -2932
         * sfz : 330814495021548745
         * tel : 18888888888
         * mh : 冠心病 糖尿病 高血压
         * eqid : 100002
         * state : 1
         * qyzt : null
         * height : 100.0
         * weight : 25.0
         * blood_type : A
         * eating_habits : 荤素搭配
         * smoke : 经常吸烟
         * drink : 经常喝酒
         * exercise_habits : 每周几次
         * user_photo : http://oyptcv2pb.bkt.clouddn.com/2017111010174991772816.jpg
         * smokeCode : 1
         * drinkCode : 1
         * eatingHabitsCode : 1
         * exerciseHabitsCode : 2
         */

        private int bid;
        private int categoryid;
        private Object doct;
        private Object eq;
        private String bname;
        private String sex;
        private String dz;
        private int age;
        private String sfz;
        private String tel;
        private String mh;
        private String eqid;
        private int state;
        private Object qyzt;
        private double height;
        private double weight;
        private String blood_type;
        private String eating_habits;
        private String smoke;
        private String drink;
        private String exercise_habits;
        private String user_photo;
        private String smokeCode;
        private String drinkCode;
        private String eatingHabitsCode;
        private String exerciseHabitsCode;

        public int getBid() {
            return bid;
        }

        public void setBid(int bid) {
            this.bid = bid;
        }

        public int getCategoryid() {
            return categoryid;
        }

        public void setCategoryid(int categoryid) {
            this.categoryid = categoryid;
        }

        public Object getDoct() {
            return doct;
        }

        public void setDoct(Object doct) {
            this.doct = doct;
        }

        public Object getEq() {
            return eq;
        }

        public void setEq(Object eq) {
            this.eq = eq;
        }

        public String getBname() {
            return bname;
        }

        public void setBname(String bname) {
            this.bname = bname;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getDz() {
            return dz;
        }

        public void setDz(String dz) {
            this.dz = dz;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getSfz() {
            return sfz;
        }

        public void setSfz(String sfz) {
            this.sfz = sfz;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getMh() {
            return mh;
        }

        public void setMh(String mh) {
            this.mh = mh;
        }

        public String getEqid() {
            return eqid;
        }

        public void setEqid(String eqid) {
            this.eqid = eqid;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public Object getQyzt() {
            return qyzt;
        }

        public void setQyzt(Object qyzt) {
            this.qyzt = qyzt;
        }

        public double getHeight() {
            return height;
        }

        public void setHeight(double height) {
            this.height = height;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public String getBlood_type() {
            return blood_type;
        }

        public void setBlood_type(String blood_type) {
            this.blood_type = blood_type;
        }

        public String getEating_habits() {
            return eating_habits;
        }

        public void setEating_habits(String eating_habits) {
            this.eating_habits = eating_habits;
        }

        public String getSmoke() {
            return smoke;
        }

        public void setSmoke(String smoke) {
            this.smoke = smoke;
        }

        public String getDrink() {
            return drink;
        }

        public void setDrink(String drink) {
            this.drink = drink;
        }

        public String getExercise_habits() {
            return exercise_habits;
        }

        public void setExercise_habits(String exercise_habits) {
            this.exercise_habits = exercise_habits;
        }

        public String getUser_photo() {
            return user_photo;
        }

        public void setUser_photo(String user_photo) {
            this.user_photo = user_photo;
        }

        public String getSmokeCode() {
            return smokeCode;
        }

        public void setSmokeCode(String smokeCode) {
            this.smokeCode = smokeCode;
        }

        public String getDrinkCode() {
            return drinkCode;
        }

        public void setDrinkCode(String drinkCode) {
            this.drinkCode = drinkCode;
        }

        public String getEatingHabitsCode() {
            return eatingHabitsCode;
        }

        public void setEatingHabitsCode(String eatingHabitsCode) {
            this.eatingHabitsCode = eatingHabitsCode;
        }

        public String getExerciseHabitsCode() {
            return exerciseHabitsCode;
        }

        public void setExerciseHabitsCode(String exerciseHabitsCode) {
            this.exerciseHabitsCode = exerciseHabitsCode;
        }
    }
}
