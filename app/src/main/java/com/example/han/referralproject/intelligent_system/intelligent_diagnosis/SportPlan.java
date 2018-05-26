package com.example.han.referralproject.intelligent_system.intelligent_diagnosis;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2018/5/16.
 */

public class SportPlan implements Parcelable{

    /**
     * sportList : [{"hmSportId":"1f6d506f-8a84-47d1-9d28-57312d233ef7","name":"篮球","alias":null,"imgUrl":"http://oyptcv2pb.bkt.clouddn.com/%E7%AF%AE%E7%90%83.png","sportEffect":"健康价值：增强心肺功能","consumption":"442千卡/60分钟","sportLevel":"3","introduce":null,"indoorType":null,"aerobicType":null,"hypertensionEffect":null,"diabetesEffect":"可以降低血糖，因为运动改善了胰岛素的敏感程度，也提高了身体使用葡萄糖的效率，当然也可以减轻胰岛素分泌的负担。"},{"hmSportId":"fd9434f4-6047-44b1-a948-b2adcf72f012","name":"踢毽子","alias":null,"imgUrl":"http://oyptcv2pb.bkt.clouddn.com/%E8%B8%A2%E6%AF%BD%E5%AD%90.png","sportEffect":"健康价值：提高反应判断力，发展平衡能力","consumption":"468千卡/60分钟","sportLevel":"3","introduce":null,"indoorType":null,"aerobicType":null,"hypertensionEffect":null,"diabetesEffect":"糖尿病患者不适合较长时间的运动，而踢毽子运动量不大，却能使得全身得到活动。同时，踢毽子不仅使下肢的关节、肌肉、韧带得到锻炼，同时也能充分活动腰部。"},{"hmSportId":"90006f22-9d1f-4736-8a80-31e5afee5f58","name":"足球","alias":null,"imgUrl":"http://oyptcv2pb.bkt.clouddn.com/%E8%B6%B3%E7%90%83.png","sportEffect":"健康价值：增强心肺功能","consumption":"482千卡/60分钟","sportLevel":"3","introduce":null,"indoorType":null,"aerobicType":null,"hypertensionEffect":null,"diabetesEffect":"可以降低血糖，因为运动改善了胰岛素的敏感程度，也提高了身体使用葡萄糖的效率，当然也可以减轻胰岛素分泌的负担。"},{"hmSportId":"f05295c1-4d79-48c9-a8de-04668aa9e6be","name":"羽毛球","alias":null,"imgUrl":"http://oyptcv2pb.bkt.clouddn.com/%E7%BE%BD%E6%AF%9B%E7%90%83.png","sportEffect":"健康价值：增强心肺功能","consumption":"361千卡/60分钟","sportLevel":"3","introduce":null,"indoorType":null,"aerobicType":null,"hypertensionEffect":null,"diabetesEffect":"打羽毛球可以降低血糖，因为运动改善了胰岛素的敏感程度，也提高了身体使用葡萄糖的效率，当然也可以减轻胰岛素分泌的负担。在运动过程中，身体会消耗肝脏所储存的肝糖原。"}]
     * sportStr : 篮球、踢毽子、足球
     * sportLevel : 高
     * sportRate : 165
     * sportTime : 每周3次,每次30分钟
     * sportWeekTime : 90
     */

    private String sportStr;
    private String sportLevel;
    private String sportRate;
    private String sportTime;
    private int sportWeekTime;
    private List<SportListBean> sportList;

    protected SportPlan(Parcel in) {
        sportStr = in.readString();
        sportLevel = in.readString();
        sportRate = in.readString();
        sportTime = in.readString();
        sportWeekTime = in.readInt();
        sportList = in.createTypedArrayList(SportListBean.CREATOR);
    }

    public static final Creator<SportPlan> CREATOR = new Creator<SportPlan>() {
        @Override
        public SportPlan createFromParcel(Parcel in) {
            return new SportPlan(in);
        }

        @Override
        public SportPlan[] newArray(int size) {
            return new SportPlan[size];
        }
    };

    public String getSportStr() {
        return sportStr;
    }

    public void setSportStr(String sportStr) {
        this.sportStr = sportStr;
    }

    public String getSportLevel() {
        return sportLevel;
    }

    public void setSportLevel(String sportLevel) {
        this.sportLevel = sportLevel;
    }

    public String getSportRate() {
        return sportRate;
    }

    public void setSportRate(String sportRate) {
        this.sportRate = sportRate;
    }

    public String getSportTime() {
        return sportTime;
    }

    public void setSportTime(String sportTime) {
        this.sportTime = sportTime;
    }

    public int getSportWeekTime() {
        return sportWeekTime;
    }

    public void setSportWeekTime(int sportWeekTime) {
        this.sportWeekTime = sportWeekTime;
    }

    public List<SportListBean> getSportList() {
        return sportList;
    }

    public void setSportList(List<SportListBean> sportList) {
        this.sportList = sportList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sportStr);
        dest.writeString(sportLevel);
        dest.writeString(sportRate);
        dest.writeString(sportTime);
        dest.writeInt(sportWeekTime);
        dest.writeTypedList(sportList);
    }

    public static class SportListBean implements Parcelable{
        /**
         * hmSportId : 1f6d506f-8a84-47d1-9d28-57312d233ef7
         * name : 篮球
         * alias : null
         * imgUrl : http://oyptcv2pb.bkt.clouddn.com/%E7%AF%AE%E7%90%83.png
         * sportEffect : 健康价值：增强心肺功能
         * consumption : 442千卡/60分钟
         * sportLevel : 3
         * introduce : null
         * indoorType : null
         * aerobicType : null
         * hypertensionEffect : null
         * diabetesEffect : 可以降低血糖，因为运动改善了胰岛素的敏感程度，也提高了身体使用葡萄糖的效率，当然也可以减轻胰岛素分泌的负担。
         */

        private String hmSportId;
        private String name;
        private String alias;
        private String imgUrl;
        private String sportEffect;
        private String consumption;
        private String sportLevel;
        private String introduce;
        private String hypertensionEffect;
        private String diabetesEffect;

        protected SportListBean(Parcel in) {
            hmSportId = in.readString();
            name = in.readString();
            alias = in.readString();
            imgUrl = in.readString();
            sportEffect = in.readString();
            consumption = in.readString();
            sportLevel = in.readString();
            introduce = in.readString();
            hypertensionEffect = in.readString();
            diabetesEffect = in.readString();
        }

        public static final Creator<SportListBean> CREATOR = new Creator<SportListBean>() {
            @Override
            public SportListBean createFromParcel(Parcel in) {
                return new SportListBean(in);
            }

            @Override
            public SportListBean[] newArray(int size) {
                return new SportListBean[size];
            }
        };

        public String getHmSportId() {
            return hmSportId;
        }

        public void setHmSportId(String hmSportId) {
            this.hmSportId = hmSportId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getSportEffect() {
            return sportEffect;
        }

        public void setSportEffect(String sportEffect) {
            this.sportEffect = sportEffect;
        }

        public String getConsumption() {
            return consumption;
        }

        public void setConsumption(String consumption) {
            this.consumption = consumption;
        }

        public String getSportLevel() {
            return sportLevel;
        }

        public void setSportLevel(String sportLevel) {
            this.sportLevel = sportLevel;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }
        public String getHypertensionEffect() {
            return hypertensionEffect;
        }

        public void setHypertensionEffect(String hypertensionEffect) {
            this.hypertensionEffect = hypertensionEffect;
        }

        public String getDiabetesEffect() {
            return diabetesEffect;
        }

        public void setDiabetesEffect(String diabetesEffect) {
            this.diabetesEffect = diabetesEffect;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(hmSportId);
            dest.writeString(name);
            dest.writeString(alias);
            dest.writeString(imgUrl);
            dest.writeString(sportEffect);
            dest.writeString(consumption);
            dest.writeString(sportLevel);
            dest.writeString(introduce);
            dest.writeString(hypertensionEffect);
            dest.writeString(diabetesEffect);
        }
    }
}
