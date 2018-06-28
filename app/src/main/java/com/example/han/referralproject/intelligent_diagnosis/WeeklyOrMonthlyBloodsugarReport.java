package com.example.han.referralproject.intelligent_diagnosis;

import java.util.List;

/**
 * Created by Administrator on 2018/5/15.
 */

public class WeeklyOrMonthlyBloodsugarReport {


    /**
     * userId : 100001
     * startTime : 1524326400000
     * endTime : 1526140800000
     * bloodSugarAvg : 6.514286
     * bloodSugarMark : 0
     * bloodSugarOneAvg : 8.3
     * bloodSugarOneMark : 0
     * bloodSugarTwoAvg : 6.874615
     * bloodSugarTwoMark : 0
     * control : 还需努力
     * trend : 下降趋势
     * healthScore : 0
     * completion : 33
     * diabetesLevel : null
     * weekDateList : [{"startTime":"1523808000000","endTime":"1524326400000","bloodSugarAvg":4.3,"bloodSugarTarget":7,"bloodSugarOffset":null,"bloodSugarOffsetType":null,"bloodSugarOneAvg":6.88,"bloodSugarOneTarget":11.1,"bloodSugarOneOffset":null,"bloodSugarOneOffsetType":null,"bloodSugarTwoAvg":6.874615,"bloodSugarTwoTarget":7.8,"bloodSugarTwoOffset":null,"bloodSugarTwoOffsetType":null,"detectionList":[{"userId":100001,"highPressure":172,"lowPressure":106,"bloodSugar":4.7,"ecg":"2","weight":49,"sugarTime":0,"timeStamp":"1523808000000"},{"userId":100001,"highPressure":154,"lowPressure":97,"bloodSugar":3.9,"ecg":"4","weight":50,"sugarTime":0,"timeStamp":"1524240000000"}]},{"startTime":"1524412800000","endTime":"1524931200000","bloodSugarAvg":5.166667,"bloodSugarTarget":7,"bloodSugarOffset":null,"bloodSugarOffsetType":null,"bloodSugarOneAvg":5.68,"bloodSugarOneTarget":11.1,"bloodSugarOneOffset":null,"bloodSugarOneOffsetType":null,"bloodSugarTwoAvg":6.874615,"bloodSugarTwoTarget":7.8,"bloodSugarTwoOffset":null,"bloodSugarTwoOffsetType":null,"detectionList":[{"userId":100001,"highPressure":120,"lowPressure":80,"bloodSugar":6,"ecg":"10","weight":42,"sugarTime":0,"timeStamp":"1524412800000"},{"userId":100001,"highPressure":176,"lowPressure":108,"bloodSugar":4.6,"ecg":"5","weight":59,"sugarTime":0,"timeStamp":"1524758400000"},{"userId":100001,"highPressure":144,"lowPressure":92,"bloodSugar":4.9,"ecg":"6","weight":66,"sugarTime":0,"timeStamp":"1524844800000"}]},{"startTime":"1525017600000","endTime":"1525536000000","bloodSugarAvg":8.6,"bloodSugarTarget":7,"bloodSugarOffset":null,"bloodSugarOffsetType":null,"bloodSugarOneAvg":8.6,"bloodSugarOneTarget":11.1,"bloodSugarOneOffset":null,"bloodSugarOneOffsetType":null,"bloodSugarTwoAvg":6.874615,"bloodSugarTwoTarget":7.8,"bloodSugarTwoOffset":null,"bloodSugarTwoOffsetType":null,"detectionList":[{"userId":100001,"highPressure":192,"lowPressure":116,"bloodSugar":8.6,"ecg":"3","weight":43,"sugarTime":0,"timeStamp":"1525363200000"}]},{"startTime":"1525622400000","endTime":"1526140800000","bloodSugarAvg":7.166667,"bloodSugarTarget":7,"bloodSugarOffset":null,"bloodSugarOffsetType":null,"bloodSugarOneAvg":8.773333,"bloodSugarOneTarget":11.1,"bloodSugarOneOffset":null,"bloodSugarOneOffsetType":null,"bloodSugarTwoAvg":6.874615,"bloodSugarTwoTarget":7.8,"bloodSugarTwoOffset":null,"bloodSugarTwoOffsetType":null,"detectionList":[{"userId":100001,"highPressure":102,"lowPressure":71,"bloodSugar":6,"ecg":"9","weight":63,"sugarTime":0,"timeStamp":"1525708800000"},{"userId":100001,"highPressure":152,"lowPressure":96,"bloodSugar":9,"ecg":"8","weight":61,"sugarTime":0,"timeStamp":"1525795200000"},{"userId":100001,"highPressure":112,"lowPressure":76,"bloodSugar":6.5,"ecg":"4","weight":52,"sugarTime":0,"timeStamp":"1525968000000"}]}]
     */

    private String userId;
    private String startTime;
    private String endTime;
    private Double bloodSugarAvg;
    private String bloodSugarMark;
    private Double bloodSugarOneAvg;
    private String bloodSugarOneMark;
    private Double bloodSugarTwoAvg;
    private String bloodSugarTwoMark;
    private String control;
    private String trend;
    private String healthScore;
    private String completion;
    private String diabetesLevel;
    private List<WeekDateListBean> weekDateList;

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

    public Double getBloodSugarAvg() {
        return bloodSugarAvg;
    }

    public void setBloodSugarAvg(Double bloodSugarAvg) {
        this.bloodSugarAvg = bloodSugarAvg;
    }

    public String getBloodSugarMark() {
        return bloodSugarMark;
    }

    public void setBloodSugarMark(String bloodSugarMark) {
        this.bloodSugarMark = bloodSugarMark;
    }

    public Double getBloodSugarOneAvg() {
        return bloodSugarOneAvg;
    }

    public void setBloodSugarOneAvg(Double bloodSugarOneAvg) {
        this.bloodSugarOneAvg = bloodSugarOneAvg;
    }

    public String getBloodSugarOneMark() {
        return bloodSugarOneMark;
    }

    public void setBloodSugarOneMark(String bloodSugarOneMark) {
        this.bloodSugarOneMark = bloodSugarOneMark;
    }

    public Double getBloodSugarTwoAvg() {
        return bloodSugarTwoAvg;
    }

    public void setBloodSugarTwoAvg(Double bloodSugarTwoAvg) {
        this.bloodSugarTwoAvg = bloodSugarTwoAvg;
    }

    public String getBloodSugarTwoMark() {
        return bloodSugarTwoMark;
    }

    public void setBloodSugarTwoMark(String bloodSugarTwoMark) {
        this.bloodSugarTwoMark = bloodSugarTwoMark;
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

    public String getDiabetesLevel() {
        return diabetesLevel;
    }

    public void setDiabetesLevel(String diabetesLevel) {
        this.diabetesLevel = diabetesLevel;
    }

    public List<WeekDateListBean> getWeekDateList() {
        return weekDateList;
    }

    public void setWeekDateList(List<WeekDateListBean> weekDateList) {
        this.weekDateList = weekDateList;
    }

    public static class WeekDateListBean {
        /**
         * startTime : 1523808000000
         * endTime : 1524326400000
         * bloodSugarAvg : 4.3
         * bloodSugarTarget : 7
         * bloodSugarOffset : null
         * bloodSugarOffsetType : null
         * bloodSugarOneAvg : 6.88
         * bloodSugarOneTarget : 11.1
         * bloodSugarOneOffset : null
         * bloodSugarOneOffsetType : null
         * bloodSugarTwoAvg : 6.874615
         * bloodSugarTwoTarget : 7.8
         * bloodSugarTwoOffset : null
         * bloodSugarTwoOffsetType : null
         * detectionList : [{"userId":100001,"highPressure":172,"lowPressure":106,"bloodSugar":4.7,"ecg":"2","weight":49,"sugarTime":0,"timeStamp":"1523808000000"},{"userId":100001,"highPressure":154,"lowPressure":97,"bloodSugar":3.9,"ecg":"4","weight":50,"sugarTime":0,"timeStamp":"1524240000000"}]
         */

        private String startTime;
        private String endTime;
        private Double bloodSugarAvg;
        private Double bloodSugarTarget;
        private Double bloodSugarOffset;
        private Object bloodSugarOffsetType;
        private Double bloodSugarOneAvg;
        private Double bloodSugarOneTarget;
        private Double bloodSugarOneOffset;
        private Object bloodSugarOneOffsetType;
        private Double bloodSugarTwoAvg;
        private Double bloodSugarTwoTarget;
        private Double bloodSugarTwoOffset;
        private Object bloodSugarTwoOffsetType;
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

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public Double getBloodSugarAvg() {
            return bloodSugarAvg;
        }

        public void setBloodSugarAvg(Double bloodSugarAvg) {
            this.bloodSugarAvg = bloodSugarAvg;
        }

        public Double getBloodSugarTarget() {
            return bloodSugarTarget;
        }

        public void setBloodSugarTarget(Double bloodSugarTarget) {
            this.bloodSugarTarget = bloodSugarTarget;
        }

        public Double getBloodSugarOffset() {
            return bloodSugarOffset;
        }

        public void setBloodSugarOffset(Double bloodSugarOffset) {
            this.bloodSugarOffset = bloodSugarOffset;
        }

        public Object getBloodSugarOffsetType() {
            return bloodSugarOffsetType;
        }

        public void setBloodSugarOffsetType(Object bloodSugarOffsetType) {
            this.bloodSugarOffsetType = bloodSugarOffsetType;
        }

        public Double getBloodSugarOneAvg() {
            return bloodSugarOneAvg;
        }

        public void setBloodSugarOneAvg(Double bloodSugarOneAvg) {
            this.bloodSugarOneAvg = bloodSugarOneAvg;
        }

        public Double getBloodSugarOneTarget() {
            return bloodSugarOneTarget;
        }

        public void setBloodSugarOneTarget(Double bloodSugarOneTarget) {
            this.bloodSugarOneTarget = bloodSugarOneTarget;
        }

        public Double getBloodSugarOneOffset() {
            return bloodSugarOneOffset;
        }

        public void setBloodSugarOneOffset(Double bloodSugarOneOffset) {
            this.bloodSugarOneOffset = bloodSugarOneOffset;
        }

        public Object getBloodSugarOneOffsetType() {
            return bloodSugarOneOffsetType;
        }

        public void setBloodSugarOneOffsetType(Object bloodSugarOneOffsetType) {
            this.bloodSugarOneOffsetType = bloodSugarOneOffsetType;
        }

        public Double getBloodSugarTwoAvg() {
            return bloodSugarTwoAvg;
        }

        public void setBloodSugarTwoAvg(Double bloodSugarTwoAvg) {
            this.bloodSugarTwoAvg = bloodSugarTwoAvg;
        }

        public Double getBloodSugarTwoTarget() {
            return bloodSugarTwoTarget;
        }

        public void setBloodSugarTwoTarget(Double bloodSugarTwoTarget) {
            this.bloodSugarTwoTarget = bloodSugarTwoTarget;
        }

        public Double getBloodSugarTwoOffset() {
            return bloodSugarTwoOffset;
        }

        public void setBloodSugarTwoOffset(Double bloodSugarTwoOffset) {
            this.bloodSugarTwoOffset = bloodSugarTwoOffset;
        }

        public Object getBloodSugarTwoOffsetType() {
            return bloodSugarTwoOffsetType;
        }

        public void setBloodSugarTwoOffsetType(Object bloodSugarTwoOffsetType) {
            this.bloodSugarTwoOffsetType = bloodSugarTwoOffsetType;
        }

        public List<DetectionListBean> getDetectionList() {
            return detectionList;
        }

        public void setDetectionList(List<DetectionListBean> detectionList) {
            this.detectionList = detectionList;
        }

        public static class DetectionListBean {
            /**
             * userId : 100001
             * highPressure : 172
             * lowPressure : 106
             * bloodSugar : 4.7
             * ecg : 2
             * weight : 49
             * sugarTime : 0
             * timeStamp : 1523808000000
             */

            private int userId;
            private int highPressure;
            private int lowPressure;
            private Double bloodSugar;
            private String ecg;
            private int weight;
            private int sugarTime;
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

            public Double getBloodSugar() {
                return bloodSugar;
            }

            public void setBloodSugar(Double bloodSugar) {
                this.bloodSugar = bloodSugar;
            }

            public String getEcg() {
                return ecg;
            }

            public void setEcg(String ecg) {
                this.ecg = ecg;
            }

            public int getWeight() {
                return weight;
            }

            public void setWeight(int weight) {
                this.weight = weight;
            }

            public int getSugarTime() {
                return sugarTime;
            }

            public void setSugarTime(int sugarTime) {
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
}
