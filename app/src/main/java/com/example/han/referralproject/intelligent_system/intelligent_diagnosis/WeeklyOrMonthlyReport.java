package com.example.han.referralproject.intelligent_system.intelligent_diagnosis;

import java.util.List;

/**
 * Created by Administrator on 2018/5/11.
 */

public class WeeklyOrMonthlyReport {

    /**
     * control : 很好
     * heartDanger : 中危
     * trend : 下降趋势
     * lowPressureMark : 0
     * lowPressureAvg : 79
     * highPressureMark : 0
     * weekDateList : [{"detectionList":[{"weight":53,"timeStamp":"1525622400000","lowPressure":74,"ecg":"11","userId":100001,"highPressure":108,"sugarTime":1,"bloodSugar":7.12},{"weight":63,"timeStamp":"1525708800000","lowPressure":71,"ecg":"9","userId":100001,"highPressure":102,"sugarTime":0,"bloodSugar":6},{"weight":61,"timeStamp":"1525795200000","lowPressure":96,"ecg":"8","userId":100001,"highPressure":152,"sugarTime":0,"bloodSugar":9},{"weight":60,"timeStamp":"1525881600000","lowPressure":86,"ecg":"18","userId":100001,"highPressure":132,"sugarTime":2,"bloodSugar":5.47},{"weight":52,"timeStamp":"1525968000000","lowPressure":76,"ecg":"4","userId":100001,"highPressure":112,"sugarTime":0,"bloodSugar":6.5},{"weight":59,"timeStamp":"1526054400000","lowPressure":91,"ecg":"5","userId":100001,"highPressure":142,"sugarTime":1,"bloodSugar":9.52},{"weight":48,"timeStamp":"1526140800000","lowPressure":74,"ecg":"14","userId":100001,"highPressure":108,"sugarTime":1,"bloodSugar":9.68}],"startTime":"1525622400000","highPressureAvg":"119","lowOffset":"-21","userId":null,"highOffsetType":"0","lowTarget":"100","lowPressureAvg":"79","highOffset":"-41","highTarget":"160","endTime":"1526140800000","lowOffsetType":"0"}]
     * endTime : 1526227200000
     * completion : 100
     * startTime : 1525622400000
     * highPressureAvg : 119
     * hypertensionLevel : 亚健康
     * userId : 100001
     * healthScore : 88
     */

    private String control;
    private String heartDanger;
    private String trend;
    private String lowPressureMark;
    private String lowPressureAvg;
    private String highPressureMark;
    private String endTime;
    private String completion;
    private String startTime;
    private String highPressureAvg;
    private String hypertensionLevel;
    private String userId;
    private String healthScore;
    private List<WeekDateListBean> weekDateList;

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public String getHeartDanger() {
        return heartDanger;
    }

    public void setHeartDanger(String heartDanger) {
        this.heartDanger = heartDanger;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }

    public String getLowPressureMark() {
        return lowPressureMark;
    }

    public void setLowPressureMark(String lowPressureMark) {
        this.lowPressureMark = lowPressureMark;
    }

    public String getLowPressureAvg() {
        return lowPressureAvg;
    }

    public void setLowPressureAvg(String lowPressureAvg) {
        this.lowPressureAvg = lowPressureAvg;
    }

    public String getHighPressureMark() {
        return highPressureMark;
    }

    public void setHighPressureMark(String highPressureMark) {
        this.highPressureMark = highPressureMark;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCompletion() {
        return completion;
    }

    public void setCompletion(String completion) {
        this.completion = completion;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getHighPressureAvg() {
        return highPressureAvg;
    }

    public void setHighPressureAvg(String highPressureAvg) {
        this.highPressureAvg = highPressureAvg;
    }

    public String getHypertensionLevel() {
        return hypertensionLevel;
    }

    public void setHypertensionLevel(String hypertensionLevel) {
        this.hypertensionLevel = hypertensionLevel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(String healthScore) {
        this.healthScore = healthScore;
    }

    public List<WeekDateListBean> getWeekDateList() {
        return weekDateList;
    }

    public void setWeekDateList(List<WeekDateListBean> weekDateList) {
        this.weekDateList = weekDateList;
    }

    public static class WeekDateListBean {
        /**
         * detectionList : [{"weight":53,"timeStamp":"1525622400000","lowPressure":74,"ecg":"11","userId":100001,"highPressure":108,"sugarTime":1,"bloodSugar":7.12},{"weight":63,"timeStamp":"1525708800000","lowPressure":71,"ecg":"9","userId":100001,"highPressure":102,"sugarTime":0,"bloodSugar":6},{"weight":61,"timeStamp":"1525795200000","lowPressure":96,"ecg":"8","userId":100001,"highPressure":152,"sugarTime":0,"bloodSugar":9},{"weight":60,"timeStamp":"1525881600000","lowPressure":86,"ecg":"18","userId":100001,"highPressure":132,"sugarTime":2,"bloodSugar":5.47},{"weight":52,"timeStamp":"1525968000000","lowPressure":76,"ecg":"4","userId":100001,"highPressure":112,"sugarTime":0,"bloodSugar":6.5},{"weight":59,"timeStamp":"1526054400000","lowPressure":91,"ecg":"5","userId":100001,"highPressure":142,"sugarTime":1,"bloodSugar":9.52},{"weight":48,"timeStamp":"1526140800000","lowPressure":74,"ecg":"14","userId":100001,"highPressure":108,"sugarTime":1,"bloodSugar":9.68}]
         * startTime : 1525622400000
         * highPressureAvg : 119
         * lowOffset : -21
         * userId : null
         * highOffsetType : 0
         * lowTarget : 100
         * lowPressureAvg : 79
         * highOffset : -41
         * highTarget : 160
         * endTime : 1526140800000
         * lowOffsetType : 0
         */

        private String startTime;
        private String highPressureAvg;
        private String lowOffset;
        private Object userId;
        private String highOffsetType;
        private String lowTarget;
        private String lowPressureAvg;
        private String highOffset;
        private String highTarget;
        private String endTime;
        private String lowOffsetType;
        private List<DetectionListBean> detectionList;
        private String completion;

        public String getCompletion() {
            return completion;
        }

        public void setCompletion(String completion) {
            this.completion = completion;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getHighPressureAvg() {
            return highPressureAvg;
        }

        public void setHighPressureAvg(String highPressureAvg) {
            this.highPressureAvg = highPressureAvg;
        }

        public String getLowOffset() {
            return lowOffset;
        }

        public void setLowOffset(String lowOffset) {
            this.lowOffset = lowOffset;
        }

        public Object getUserId() {
            return userId;
        }

        public void setUserId(Object userId) {
            this.userId = userId;
        }

        public String getHighOffsetType() {
            return highOffsetType;
        }

        public void setHighOffsetType(String highOffsetType) {
            this.highOffsetType = highOffsetType;
        }

        public String getLowTarget() {
            return lowTarget;
        }

        public void setLowTarget(String lowTarget) {
            this.lowTarget = lowTarget;
        }

        public String getLowPressureAvg() {
            return lowPressureAvg;
        }

        public void setLowPressureAvg(String lowPressureAvg) {
            this.lowPressureAvg = lowPressureAvg;
        }

        public String getHighOffset() {
            return highOffset;
        }

        public void setHighOffset(String highOffset) {
            this.highOffset = highOffset;
        }

        public String getHighTarget() {
            return highTarget;
        }

        public void setHighTarget(String highTarget) {
            this.highTarget = highTarget;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getLowOffsetType() {
            return lowOffsetType;
        }

        public void setLowOffsetType(String lowOffsetType) {
            this.lowOffsetType = lowOffsetType;
        }

        public List<DetectionListBean> getDetectionList() {
            return detectionList;
        }

        public void setDetectionList(List<DetectionListBean> detectionList) {
            this.detectionList = detectionList;
        }

        public static class DetectionListBean {
            /**
             * weight : 53
             * timeStamp : 1525622400000
             * lowPressure : 74
             * ecg : 11
             * userId : 100001
             * highPressure : 108
             * sugarTime : 1
             * bloodSugar : 7.12
             */

            private int weight;
            private String timeStamp;
            private int lowPressure;
            private String ecg;
            private int userId;
            private int highPressure;
            private int sugarTime;
            private double bloodSugar;

            public int getWeight() {
                return weight;
            }

            public void setWeight(int weight) {
                this.weight = weight;
            }

            public String getTimeStamp() {
                return timeStamp;
            }

            public void setTimeStamp(String timeStamp) {
                this.timeStamp = timeStamp;
            }

            public int getLowPressure() {
                return lowPressure;
            }

            public void setLowPressure(int lowPressure) {
                this.lowPressure = lowPressure;
            }

            public String getEcg() {
                return ecg;
            }

            public void setEcg(String ecg) {
                this.ecg = ecg;
            }

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

            public int getSugarTime() {
                return sugarTime;
            }

            public void setSugarTime(int sugarTime) {
                this.sugarTime = sugarTime;
            }

            public double getBloodSugar() {
                return bloodSugar;
            }

            public void setBloodSugar(double bloodSugar) {
                this.bloodSugar = bloodSugar;
            }
        }
    }
}
