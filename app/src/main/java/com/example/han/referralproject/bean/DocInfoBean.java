package com.example.han.referralproject.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/11/1.
 */

public class DocInfoBean implements Serializable {

    public boolean tag;
    public Object message;
    public DataBean data;

    public static class DataBean {
        /**
         * doctername : 崔博
         * amount : 209.0
         * documents : http://oyptcv2pb.bkt.clouddn.com/doc3.jpg
         * online_status : 2
         * count : 2
         * pro : 擅长肝胆胰外科疾病的临床诊断和治疗，特别是在肝胆胰外科微创手术方面积累了丰富的经验，开展了腹腔镜下左右肝叶切除、腹腔镜下胰体尾和保留脾脏胰体尾切除、腹腔镜下巨脾切除和贲门周围血管离断术以及胰十二指肠切除术、腹腔镜下胆总管囊肿切除术等手术。
         * hosname : 杭州中医院
         * docterid : 10003
         * evaluation : 70
         * service_amount : 1.0
         * zige : 已通过资格审核
         * rankid : 10003
         * docter_photo : http://oyptcv2pb.bkt.clouddn.com/doc3.jpg
         * rankname : 科室医师
         * duty : fqwf
         * tel : 15558089587
         * gat : qwe
         * department : wefqw
         * card : http://oyptcv2pb.bkt.clouddn.com/doc3.jpg
         * adds : qwe
         * apply_amount : 20.0
         * pend : 1
         */

        public String doctername;
        public double amount;
        public String documents;
        public int online_status;
        public int count;
        public String pro;
        public String hosname;
        public int docterid;
        public int evaluation;
        public double service_amount;
        public String zige;
        public int rankid;
        public String docter_photo;
        public String rankname;
        public String duty;
        public String tel;
        public String gat;
        public String department;
        public String card;
        public String adds;
        public double apply_amount;
        public int pend;
    }
}
