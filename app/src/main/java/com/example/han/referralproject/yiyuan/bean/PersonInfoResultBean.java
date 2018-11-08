package com.example.han.referralproject.yiyuan.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/5/30.
 */

public class PersonInfoResultBean  implements Serializable{

    /**
     * tag : true
     * message : 成功
     * data : {"exercise_habits":"偶尔运动","sfz":"340321199112256551","sex":"男","eqid":"1bca3d0263b30932","smoke":"从不吸烟","weight":65,"smokeCode":"3","drinkCode":"2","drink":"偶尔喝酒","eatingHabitsCode":"1","bname":"张辉","dz":"安徽省蚌埠市怀远县双桥集镇湾东村张西组４９号","blood_type":"A","record":{"hiHealthRecordId":"cea982c9-12dd-4818-b9d0-f18413700bea","equipmentId":"1bca3d0263b30932","userId":100096,"doctorId":10008,"address":"浙江杭州萧山区建设一路14号","height":"163cm","weight":"61kg","bloodType":"不详","rhBlood":"不详","educationalLevel":"大学本科","maritalStatus":null,"medicalPayments":"新型农村合作医疗","medicationAllergy":"无","exposureHistory":"无","diseasesHistory":"无","diseasesKinsfolk":"无","kinsfolkDiseasesType":null,"geneticHistory":"否","disabilitySituation":"无残疾","kitchenExhaust":"烟囱","kitchenFuel":"柴火","waterEnvironment":"井水","toiletPosition":"卫生厕所","livestockBar":"室内","professionType":"专业技术人员"},"tel":"18655212892","eating_habits":"荤素搭配","mh":"尚未填写","state":0,"bid":100096,"exerciseHabitsCode":"3","user_photo":"http://oyptcv2pb.bkt.clouddn.com/2018053007412807012011.jpg","age":27,"categoryid":0,"height":180}
     */

    public boolean tag;
    public String message;
    public DataBean data;

    public static class DataBean implements Serializable {
        /**
         * exercise_habits : 偶尔运动
         * sfz : 340321199112256551
         * sex : 男
         * eqid : 1bca3d0263b30932
         * smoke : 从不吸烟
         * weight : 65
         * smokeCode : 3
         * drinkCode : 2
         * drink : 偶尔喝酒
         * eatingHabitsCode : 1
         * bname : 张辉
         * dz : 安徽省蚌埠市怀远县双桥集镇湾东村张西组４９号
         * blood_type : A
         * record : {"hiHealthRecordId":"cea982c9-12dd-4818-b9d0-f18413700bea","equipmentId":"1bca3d0263b30932","userId":100096,"doctorId":10008,"address":"浙江杭州萧山区建设一路14号","height":"163cm","weight":"61kg","bloodType":"不详","rhBlood":"不详","educationalLevel":"大学本科","maritalStatus":null,"medicalPayments":"新型农村合作医疗","medicationAllergy":"无","exposureHistory":"无","diseasesHistory":"无","diseasesKinsfolk":"无","kinsfolkDiseasesType":null,"geneticHistory":"否","disabilitySituation":"无残疾","kitchenExhaust":"烟囱","kitchenFuel":"柴火","waterEnvironment":"井水","toiletPosition":"卫生厕所","livestockBar":"室内","professionType":"专业技术人员"}
         * tel : 18655212892
         * eating_habits : 荤素搭配
         * mh : 尚未填写
         * state : 0
         * bid : 100096
         * exerciseHabitsCode : 3
         * user_photo : http://oyptcv2pb.bkt.clouddn.com/2018053007412807012011.jpg
         * age : 27
         * categoryid : 0
         * height : 180
         */

        public String exercise_habits;
        public String sfz;
        public String sex;
        public String eqid;
        public String smoke;
        public String smokeCode;
        public String drinkCode;
        public String drink;
        public String eatingHabitsCode;
        public String bname;
        public String dz;
        public String blood_type;
        public RecordBean record;
        public String tel;
        public String eating_habits;
        public String mh;
        public int state;
        public int doid;
        public int bid;
        public String exerciseHabitsCode;
        public String userPhoto;
        public String user_photo;
        public int age;
        public int categoryid;
        public String address;
        public String height;
        public String weight;

        public static class RecordBean {
            /**
             * hiHealthRecordId : cea982c9-12dd-4818-b9d0-f18413700bea
             * equipmentId : 1bca3d0263b30932
             * userId : 100096
             * doctorId : 10008
             * address : 浙江杭州萧山区建设一路14号
             * height : 163cm
             * weight : 61kg
             * bloodType : 不详
             * rhBlood : 不详
             * educationalLevel : 大学本科
             * maritalStatus : null
             * medicalPayments : 新型农村合作医疗
             * medicationAllergy : 无
             * exposureHistory : 无
             * diseasesHistory : 无
             * diseasesKinsfolk : 无
             * kinsfolkDiseasesType : null
             * geneticHistory : 否
             * disabilitySituation : 无残疾
             * kitchenExhaust : 烟囱
             * kitchenFuel : 柴火
             * waterEnvironment : 井水
             * toiletPosition : 卫生厕所
             * livestockBar : 室内
             * professionType : 专业技术人员
             */

            public String hiHealthRecordId;
            public String equipmentId;
            public int userId;
            public int doctorId;
            public String address;
            public String height;
            public String weight;
            public String bloodType;
            public String rhBlood;
            public String educationalLevel;
            public String maritalStatus;
            public String medicalPayments;
            public String medicationAllergy;
            public String exposureHistory;
            public String diseasesHistory;
            public String diseasesKinsfolk;
            public String kinsfolkDiseasesType;
            public String geneticHistory;
            public String disabilitySituation;
            public String kitchenExhaust;
            public String kitchenFuel;
            public String waterEnvironment;
            public String toiletPosition;
            public String livestockBar;
            public String professionType;
        }
    }
}
