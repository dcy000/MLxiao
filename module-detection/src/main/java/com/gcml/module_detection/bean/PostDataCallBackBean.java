package com.gcml.module_detection.bean;

public class PostDataCallBackBean {

    private Result2Bean result2;
    private Result1Bean result1;

    public Result2Bean getResult2() {
        return result2;
    }

    public void setResult2(Result2Bean result2) {
        this.result2 = result2;
    }

    public Result1Bean getResult1() {
        return result1;
    }

    public void setResult1(Result1Bean result1) {
        this.result1 = result1;
    }

    public static class Result2Bean {
        private String deletionState;
        private int resultId;
        private int dataZid;
        private String dataResult;
        private String result;

        public String getDeletionState() {
            return deletionState;
        }

        public void setDeletionState(String deletionState) {
            this.deletionState = deletionState;
        }

        public int getResultId() {
            return resultId;
        }

        public void setResultId(int resultId) {
            this.resultId = resultId;
        }

        public int getDataZid() {
            return dataZid;
        }

        public void setDataZid(int dataZid) {
            this.dataZid = dataZid;
        }

        public String getDataResult() {
            return dataResult;
        }

        public void setDataResult(String dataResult) {
            this.dataResult = dataResult;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }

    public static class Result1Bean {
        private DateBean date;
        private String diagnose;
        private String result;
        private int score;

        public DateBean getDate() {
            return date;
        }

        public void setDate(DateBean date) {
            this.date = date;
        }

        public String getDiagnose() {
            return diagnose;
        }

        public void setDiagnose(String diagnose) {
            this.diagnose = diagnose;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public static class DateBean {

            private String eqid;
            private int userid;
            private String detectionType;
            private String time;
            private int highPressure;
            private int lowPressure;

            public String getEqid() {
                return eqid;
            }

            public void setEqid(String eqid) {
                this.eqid = eqid;
            }

            public int getUserid() {
                return userid;
            }

            public void setUserid(int userid) {
                this.userid = userid;
            }

            public String getDetectionType() {
                return detectionType;
            }

            public void setDetectionType(String detectionType) {
                this.detectionType = detectionType;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
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
        }
    }
}
