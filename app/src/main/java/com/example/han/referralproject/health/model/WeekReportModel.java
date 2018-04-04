package com.example.han.referralproject.health.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lenovo on 2018/4/4.
 */

public class WeekReportModel {
    @SerializedName("mapm")
    public CurrentWeek currentWeek;

    @SerializedName("mapq")
    public LastWeek lastWeek;

    @Override
    public String toString() {
        return "WeekReportModel{" +
                "currentWeek=" + currentWeek +
                ", lastWeek=" + lastWeek +
                '}';
    }

    public static class CurrentWeek {
        //    na: "20.57",
//    sports: "34.00",
//    weight: -1,
//    drink: "19.14"
        public String na;
        public String sports;
        public String weight;
        public String drink;
        public String bmis;

        @Override
        public String toString() {
            return "CurrentWeek{" +
                    "na='" + na + '\'' +
                    ", sports='" + sports + '\'' +
                    ", weight='" + weight + '\'' +
                    ", drink='" + drink + '\'' +
                    ", bmis='" + bmis + '\'' +
                    '}';
        }
    }

    public static class LastWeek {
        public String bmil;
        public String nas;
        public String bmim;
        public String zongw;
        public String drinkw;
        public String naw;
        public String sportsw;
        public String drinks;
        public String drinkl;
        public String sportss;
        public String drinkm;
        public String health;
        public String sportsl;
        public String dy_avg;
        public String sportsm;
        public String bmiw;
        public String nal;
        public String nam;
        public String bmis;
        public String gy_avg;
        public String p_gy;
        public String p_dy;

        @Override
        public String toString() {
            return "LastWeek{" +
                    "bmil='" + bmil + '\'' +
                    ", nas='" + nas + '\'' +
                    ", bmim='" + bmim + '\'' +
                    ", zongw='" + zongw + '\'' +
                    ", drinkw='" + drinkw + '\'' +
                    ", naw='" + naw + '\'' +
                    ", sportsw='" + sportsw + '\'' +
                    ", drinks='" + drinks + '\'' +
                    ", drinkl='" + drinkl + '\'' +
                    ", sportss='" + sportss + '\'' +
                    ", drinkm='" + drinkm + '\'' +
                    ", health='" + health + '\'' +
                    ", sportsl='" + sportsl + '\'' +
                    ", dy_avg='" + dy_avg + '\'' +
                    ", sportsm='" + sportsm + '\'' +
                    ", bmiw='" + bmiw + '\'' +
                    ", nal='" + nal + '\'' +
                    ", nam='" + nam + '\'' +
                    ", bmis='" + bmis + '\'' +
                    ", gy_avg='" + gy_avg + '\'' +
                    ", p_gy='" + p_gy + '\'' +
                    ", p_dy='" + p_dy + '\'' +
                    '}';
        }
    }
}
