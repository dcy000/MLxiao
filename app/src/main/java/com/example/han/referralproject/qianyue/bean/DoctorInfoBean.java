package com.example.han.referralproject.qianyue.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/9/28.
 */

public class DoctorInfoBean implements Serializable {

    /**
     * docterid : 10002
     * doctername : 戴程远
     * tel : 18818131887
     * adds : 辽宁省
     * duty : 门诊主任
     * department : 骨科
     * state : 1
     * priority : 13
     * documents : http://oyptcv2pb.bkt.clouddn.com/doc2.jpg
     * card : http://oyptcv2pb.bkt.clouddn.com/doc2.jpg
     * amount : 704.2
     * gat : 糖尿病，骨科
     * pro : 暨南大学附属第一医院骨科副主任兼运动医学中心主任，医学博士，硕士生导师，副主任顾问。从事临床工作近20年，师从广东省医协会运动医学分会主任委员黄华扬教授，主攻运动医学及关节外科，对肩膝髋肘等关节疾病的微创治疗有丰富经验。曾赴美国学习深造、师从SuperPath微创髋关节置换的发明者，Hedley骨科研究所James Chow教授，完成关节和运动医学的专业培训（ISOKAS Fellow）。同时作为访问顾问，师从国际肩肘关节大师，北美关节镜协会（AANA）前主席、杜兰大学医学院运动医学部主任Felix Savoie教授以及亚利桑那州立大学骨科主任，NBA凤凰城太阳队首席顾问Thomas Carter 教授。
     * pend : 1
     * number : 0
     * evaluation : 75
     * apply_amount : 50
     * service_amount : 222
     * docter_photo : http://oyptcv2pb.bkt.clouddn.com/doc2.jpg
     * r : {"rankid":10002,"rankname":"门诊顾问"}
     * hosname : 树兰医院
     * online_status : 1
     */

    public int docterid;
    public String doctername;
    public String tel;
    public String adds;
    public String duty;
    public String department;
    public int state;
    public int priority;
    public String documents;
    public String card;
    public double amount;
    public String gat;
    public String pro;
    public int pend;
    public int number;
    public int evaluation;
    public int apply_amount;
    public float service_amount;
    public String docter_photo;
    public RBean r;
    public String hosname;
    public int online_status;

    public static class RBean {
        /**
         * rankid : 10002
         * rankname : 门诊顾问
         */

        public int rankid;
        public String rankname;
    }
}
