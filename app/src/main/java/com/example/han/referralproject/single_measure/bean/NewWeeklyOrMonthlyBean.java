package com.example.han.referralproject.single_measure.bean;

import java.util.List;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/7/30 14:15
 * created by:gzq
 * description:TODO
 */
public class NewWeeklyOrMonthlyBean {

    /**
     * userId : 100034
     * startTime : 1532880000000
     * endTime : 1533484800000
     * weekDateList : [{"userId":null,"startTime":"1532880000000","endTime":"1533398400000","highPressureAvg":101,"lowPressureAvg":70,"highTarget":140,"lowTarget":90,"highOffset":"-39","highOffsetType":"0","lowOffset":"-20","lowOffsetType":"0","completion":"100","detectionList":[{"userId":100034,"highPressure":160,"lowPressure":90,"bloodSugar":null,"ecg":null,"weight":null,"sugarTime":null,"timeStamp":"1532918524000"},{"userId":100034,"highPressure":140,"lowPressure":100,"bloodSugar":null,"ecg":null,"weight":null,"sugarTime":null,"timeStamp":"1532918524000"},{"userId":100034,"highPressure":120,"lowPressure":80,"bloodSugar":null,"ecg":null,"weight":null,"sugarTime":null,"timeStamp":"1532918524000"},{"userId":100034,"highPressure":88,"lowPressure":80,"bloodSugar":null,"ecg":null,"weight":null,"sugarTime":null,"timeStamp":"1532918524000"},{"userId":100034,"highPressure":0,"lowPressure":0,"bloodSugar":null,"ecg":null,"weight":null,"sugarTime":null,"timeStamp":"1532918524000"}]}]
     * highPressureAvg : 101
     * highPressureMark : 0
     * lowPressureAvg : 70
     * lowPressureMark : 0
     * hypertensionLevel : null
     * heartDanger : 低危
     * healthScore : 85
     * completion : 100
     * control : 很好
     * trend : 下降趋势
     * targetList : [{"target":-1,"num":1},{"target":0,"num":1},{"target":1,"num":3}]
     */

    private String userId;
    private String startTime;
    private String endTime;
    private int highPressureAvg;
    private String highPressureMark;
    private int lowPressureAvg;
    private String lowPressureMark;
    private Object hypertensionLevel;
    private String heartDanger;
    private String healthScore;
    private String completion;
    private String control;
    private String trend;
    private List<WeekDateListBean> weekDateList;
    private List<TargetListBean> targetList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getHighPressureAvg() {
        return highPressureAvg;
    }

    public void setHighPressureAvg(int highPressureAvg) {
        this.highPressureAvg = highPressureAvg;
    }

    public String getHighPressureMark() {
        return highPressureMark;
    }

    public void setHighPressureMark(String highPressureMark) {
        this.highPressureMark = highPressureMark;
    }

    public int getLowPressureAvg() {
        return lowPressureAvg;
    }

    public void setLowPressureAvg(int lowPressureAvg) {
        this.lowPressureAvg = lowPressureAvg;
    }

    public String getLowPressureMark() {
        return lowPressureMark;
    }

    public void setLowPressureMark(String lowPressureMark) {
        this.lowPressureMark = lowPressureMark;
    }

    public Object getHypertensionLevel() {
        return hypertensionLevel;
    }

    public void setHypertensionLevel(Object hypertensionLevel) {
        this.hypertensionLevel = hypertensionLevel;
    }

    public String getHeartDanger() {
        return heartDanger;
    }

    public void setHeartDanger(String heartDanger) {
        this.heartDanger = heartDanger;
    }

    public String getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(String healthScore) {
        this.healthScore = healthScore;
    }

    public String getCompletion() {
        return completion;
    }

    public void setCompletion(String completion) {
        this.completion = completion;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }

    public List<WeekDateListBean> getWeekDateList() {
        return weekDateList;
    }

    public void setWeekDateList(List<WeekDateListBean> weekDateList) {
        this.weekDateList = weekDateList;
    }

    public List<TargetListBean> getTargetList() {
        return targetList;
    }

    public void setTargetList(List<TargetListBean> targetList) {
        this.targetList = targetList;
    }

    public static class WeekDateListBean {
        /**
         * userId : null
         * startTime : 1532880000000
         * endTime : 1533398400000
         * highPressureAvg : 101
         * lowPressureAvg : 70
         * highTarget : 140.0
         * lowTarget : 90.0
         * highOffset : -39
         * highOffsetType : 0
         * lowOffset : -20
         * lowOffsetType : 0
         * completion : 100
         * detectionList : [{"userId":100034,"highPressure":160,"lowPressure":90,"bloodSugar":null,"ecg":null,"weight":null,"sugarTime":null,"timeStamp":"1532918524000"},{"userId":100034,"highPressure":140,"lowPressure":100,"bloodSugar":null,"ecg":null,"weight":null,"sugarTime":null,"timeStamp":"1532918524000"},{"userId":100034,"highPressure":120,"lowPressure":80,"bloodSugar":null,"ecg":null,"weight":null,"sugarTime":null,"timeStamp":"1532918524000"},{"userId":100034,"highPressure":88,"lowPressure":80,"bloodSugar":null,"ecg":null,"weight":null,"sugarTime":null,"timeStamp":"1532918524000"},{"userId":100034,"highPressure":0,"lowPressure":0,"bloodSugar":null,"ecg":null,"weight":null,"sugarTime":null,"timeStamp":"1532918524000"}]
         */

        private Object userId;
        private String startTime;
        private String endTime;
        private int highPressureAvg;
        private int lowPressureAvg;
        private double highTarget;
        private double lowTarget;
        private String highOffset;
        private String highOffsetType;
        private String lowOffset;
        private String lowOffsetType;
        private String completion;
        private List<DetectionListBean> detectionList;

        public Object getUserId() {
            return userId;
        }

        public void setUserId(Object userId) {
            this.userId = userId;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getHighPressureAvg() {
            return highPressureAvg;
        }

        public void setHighPressureAvg(int highPressureAvg) {
            this.highPressureAvg = highPressureAvg;
        }

        public int getLowPressureAvg() {
            return lowPressureAvg;
        }

        public void setLowPressureAvg(int lowPressureAvg) {
            this.lowPressureAvg = lowPressureAvg;
        }

        public double getHighTarget() {
            return highTarget;
        }

        public void setHighTarget(double highTarget) {
            this.highTarget = highTarget;
        }

        public double getLowTarget() {
            return lowTarget;
        }

        public void setLowTarget(double lowTarget) {
            this.lowTarget = lowTarget;
        }

        public String getHighOffset() {
            return highOffset;
        }

        public void setHighOffset(String highOffset) {
            this.highOffset = highOffset;
        }

        public String getHighOffsetType() {
            return highOffsetType;
        }

        public void setHighOffsetType(String highOffsetType) {
            this.highOffsetType = highOffsetType;
        }

        public String getLowOffset() {
            return lowOffset;
        }

        public void setLowOffset(String lowOffset) {
            this.lowOffset = lowOffset;
        }

        public String getLowOffsetType() {
            return lowOffsetType;
        }

        public void setLowOffsetType(String lowOffsetType) {
            this.lowOffsetType = lowOffsetType;
        }

        public String getCompletion() {
            return completion;
        }

        public void setCompletion(String completion) {
            this.completion = completion;
        }

        public List<DetectionListBean> getDetectionList() {
            return detectionList;
        }

        public void setDetectionList(List<DetectionListBean> detectionList) {
            this.detectionList = detectionList;
        }

        public static class DetectionListBean {
            /**
             * userId : 100034
             * highPressure : 160
             * lowPressure : 90
             * bloodSugar : null
             * ecg : null
             * weight : null
             * sugarTime : null
             * timeStamp : 1532918524000
             */

            private int userId;
            private int highPressure;
            private int lowPressure;
            private Object bloodSugar;
            private Object ecg;
            private Object weight;
            private Object sugarTime;
            private String timeStamp;

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getHighPressure() {
                return highPressure;
            }

            public void setHighPressure(int highPressure) {
                this.highPressure = highPressure;
            }

            public int getLowPressure() {
                return lowPressure;
            }

            public void setLowPressure(int lowPressure) {
                this.lowPressure = lowPressure;
            }

            public Object getBloodSugar() {
                return bloodSugar;
            }

            public void setBloodSugar(Object bloodSugar) {
                this.bloodSugar = bloodSugar;
            }

            public Object getEcg() {
                return ecg;
            }

            public void setEcg(Object ecg) {
                this.ecg = ecg;
            }

            public Object getWeight() {
                return weight;
            }

            public void setWeight(Object weight) {
                this.weight = weight;
            }

            public Object getSugarTime() {
                return sugarTime;
            }

            public void setSugarTime(Object sugarTime) {
                this.sugarTime = sugarTime;
            }

            public String getTimeStamp() {
                return timeStamp;
            }

            public void setTimeStamp(String timeStamp) {
                this.timeStamp = timeStamp;
            }
        }
    }

    public static class TargetListBean {
        /**
         * target : -1
         * num : 1
         */

        private int target;
        private int num;

        public int getTarget() {
            return target;
        }

        public void setTarget(int target) {
            this.target = target;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}
