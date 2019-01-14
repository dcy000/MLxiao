package com.gcml.module_blutooth_devices.ecg;

import java.util.List;

public class BoShengResultBean {


    /**
     * avgbeats : [{"BeatData":[-7,-9,-10,-10,-9,-8,-6,-6,-5,-5,-5,-4,-6,-7,-7,-6,-6,-5,-5,-5,-5,-4,-4,-5,-4,-3,-2,-1,-3,-1,-3,0,-1,0,-1,-3,-3,-3,-1,0,0,-1,-1,-1,0,1,0,0,3,3,7,9,10,10,12,11,9,5,0,0,-2,-2,-4,-5,-6,-5,-10,-16,-24,-23,-1,37,96,161,214,245,240,203,143,86,46,21,7,1,-9,-15,-17,-19,-19,-18,-17,-17,-17,-14,-15,-12,-9,-10,-6,-6,-3,-7,-5,-2,2,5,3,6,7,10,10,13,18,18,19,21,21,23,21,20,19,20,20,20,17,14,14,10,5,2,-2,-6,-8,-10,-13,-14,-15,-14,-11,-10,-10,-11,-13,-12,-11,-10,-6,-9,-10,-10,-8,-4,-4,-5,-6,-6,-7,-8,-6,-8,-9,-10,-8,-8,-8,-9,-7,-6,-6,-7,-7,-7,-5,-7,-6,-5,-5,-5,-2,-2,-2,-2,-2,-4,-2,-3,-1,-3,-3,-4,-3,-4,-3,-5,-6,-5,-4,-3,0,-2],"HR":76,"Joff":84,"PR":129,"Poff":54,"QRS":94,"QT":209,"QTC":239,"Qoff":65,"RR":770,"Roff":80,"ST":85,"TEoff":107,"TPoff":0,"hz":200}]
     * findings : 1.心率:76 bpm, 正常范围.
     2.心律失常(也可能是由于信号干扰导致误判).
     3.QRS宽度正常.
     4.QTC正常.
     (以上诊断为自动分析结果,请联系医生)

     * qrs : [{"RR":155,"flag":1,"offset":113},{"RR":167,"flag":1,"offset":280},{"RR":148,"flag":1,"offset":428},{"RR":138,"flag":1,"offset":566},{"RR":139,"flag":1,"offset":705},{"RR":145,"flag":1,"offset":850},{"RR":180,"flag":1,"offset":1030},{"RR":167,"flag":1,"offset":1197},{"RR":175,"flag":1,"offset":1372},{"RR":164,"flag":1,"offset":1536},{"RR":152,"flag":1,"offset":1688},{"RR":141,"flag":1,"offset":1829},{"RR":145,"flag":1,"offset":1974},{"RR":154,"flag":1,"offset":2128},{"RR":165,"flag":1,"offset":2293},{"RR":174,"flag":1,"offset":2467},{"RR":176,"flag":1,"offset":2643},{"RR":173,"flag":1,"offset":2816},{"RR":169,"flag":1,"offset":2985},{"RR":178,"flag":1,"offset":3163},{"RR":164,"flag":1,"offset":3327},{"RR":150,"flag":1,"offset":3477},{"RR":134,"flag":1,"offset":3611},{"RR":128,"flag":1,"offset":3739},{"RR":130,"flag":1,"offset":3869},{"RR":142,"flag":1,"offset":4011},{"RR":225,"flag":1,"offset":4236},{"RR":191,"flag":1,"offset":4427},{"RR":164,"flag":1,"offset":4591},{"RR":164,"flag":1,"offset":4755},{"RR":149,"flag":1,"offset":4904},{"RR":148,"flag":1,"offset":5052},{"RR":141,"flag":1,"offset":5193},{"RR":133,"flag":1,"offset":5326},{"RR":135,"flag":1,"offset":5461},{"RR":158,"flag":1,"offset":5619},{"RR":170,"flag":1,"offset":5789}]
     * stop_light : 1
     */

    private String findings;
    private int stop_light;
    private List<AvgbeatsBean> avgbeats;
    private List<QrsBean> qrs;

    public String getFindings() {
        return findings;
    }

    public void setFindings(String findings) {
        this.findings = findings;
    }

    public int getStop_light() {
        return stop_light;
    }

    public void setStop_light(int stop_light) {
        this.stop_light = stop_light;
    }

    public List<AvgbeatsBean> getAvgbeats() {
        return avgbeats;
    }

    public void setAvgbeats(List<AvgbeatsBean> avgbeats) {
        this.avgbeats = avgbeats;
    }

    public List<QrsBean> getQrs() {
        return qrs;
    }

    public void setQrs(List<QrsBean> qrs) {
        this.qrs = qrs;
    }

    public static class AvgbeatsBean {
        /**
         * BeatData : [-7,-9,-10,-10,-9,-8,-6,-6,-5,-5,-5,-4,-6,-7,-7,-6,-6,-5,-5,-5,-5,-4,-4,-5,-4,-3,-2,-1,-3,-1,-3,0,-1,0,-1,-3,-3,-3,-1,0,0,-1,-1,-1,0,1,0,0,3,3,7,9,10,10,12,11,9,5,0,0,-2,-2,-4,-5,-6,-5,-10,-16,-24,-23,-1,37,96,161,214,245,240,203,143,86,46,21,7,1,-9,-15,-17,-19,-19,-18,-17,-17,-17,-14,-15,-12,-9,-10,-6,-6,-3,-7,-5,-2,2,5,3,6,7,10,10,13,18,18,19,21,21,23,21,20,19,20,20,20,17,14,14,10,5,2,-2,-6,-8,-10,-13,-14,-15,-14,-11,-10,-10,-11,-13,-12,-11,-10,-6,-9,-10,-10,-8,-4,-4,-5,-6,-6,-7,-8,-6,-8,-9,-10,-8,-8,-8,-9,-7,-6,-6,-7,-7,-7,-5,-7,-6,-5,-5,-5,-2,-2,-2,-2,-2,-4,-2,-3,-1,-3,-3,-4,-3,-4,-3,-5,-6,-5,-4,-3,0,-2]
         * HR : 76
         * Joff : 84
         * PR : 129
         * Poff : 54
         * QRS : 94
         * QT : 209
         * QTC : 239
         * Qoff : 65
         * RR : 770
         * Roff : 80
         * ST : 85
         * TEoff : 107
         * TPoff : 0
         * hz : 200
         */

        private int HR;
        private int Joff;
        private int PR;
        private int Poff;
        private int QRS;
        private int QT;
        private int QTC;
        private int Qoff;
        private int RR;
        private int Roff;
        private int ST;
        private int TEoff;
        private int TPoff;
        private int hz;
        private List<Integer> BeatData;

        public int getHR() {
            return HR;
        }

        public void setHR(int HR) {
            this.HR = HR;
        }

        public int getJoff() {
            return Joff;
        }

        public void setJoff(int Joff) {
            this.Joff = Joff;
        }

        public int getPR() {
            return PR;
        }

        public void setPR(int PR) {
            this.PR = PR;
        }

        public int getPoff() {
            return Poff;
        }

        public void setPoff(int Poff) {
            this.Poff = Poff;
        }

        public int getQRS() {
            return QRS;
        }

        public void setQRS(int QRS) {
            this.QRS = QRS;
        }

        public int getQT() {
            return QT;
        }

        public void setQT(int QT) {
            this.QT = QT;
        }

        public int getQTC() {
            return QTC;
        }

        public void setQTC(int QTC) {
            this.QTC = QTC;
        }

        public int getQoff() {
            return Qoff;
        }

        public void setQoff(int Qoff) {
            this.Qoff = Qoff;
        }

        public int getRR() {
            return RR;
        }

        public void setRR(int RR) {
            this.RR = RR;
        }

        public int getRoff() {
            return Roff;
        }

        public void setRoff(int Roff) {
            this.Roff = Roff;
        }

        public int getST() {
            return ST;
        }

        public void setST(int ST) {
            this.ST = ST;
        }

        public int getTEoff() {
            return TEoff;
        }

        public void setTEoff(int TEoff) {
            this.TEoff = TEoff;
        }

        public int getTPoff() {
            return TPoff;
        }

        public void setTPoff(int TPoff) {
            this.TPoff = TPoff;
        }

        public int getHz() {
            return hz;
        }

        public void setHz(int hz) {
            this.hz = hz;
        }

        public List<Integer> getBeatData() {
            return BeatData;
        }

        public void setBeatData(List<Integer> BeatData) {
            this.BeatData = BeatData;
        }
    }

    public static class QrsBean {
        /**
         * RR : 155
         * flag : 1
         * offset : 113
         */

        private int RR;
        private int flag;
        private int offset;

        public int getRR() {
            return RR;
        }

        public void setRR(int RR) {
            this.RR = RR;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }
    }
}
