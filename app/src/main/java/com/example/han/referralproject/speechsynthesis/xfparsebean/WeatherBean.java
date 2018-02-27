package com.example.han.referralproject.speechsynthesis.xfparsebean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/2/27.
 */

public class WeatherBean implements Serializable{

    /**
     * date : 2018-02-27
     * airQuality : 中度污染
     * weatherType : 2
     * temp : 3
     * airData : 218
     * city : 北京
     * windLevel : 0
     * pm25 : 168
     * weather : 阴
     * humidity : 62%
     * tempRange : 0℃ ~ 6℃
     * exp : {"ct":{"expName":"穿衣指数","level":"冷","prompt":"天气冷，建议着棉服、羽绒服、皮夹克加羊毛衫等冬季服装。年老体弱者宜着厚棉衣、冬大衣或厚羽绒服。"}}
     * dateLong : 1519660800
     * lastUpdateTime : 2018-02-27 11:00
     * wind : 东南风微风
     */

    public String date;
    public String airQuality;
    public int weatherType;
    public int temp;
    public int airData;
    public String city;
    public int windLevel;
    public String pm25;
    public String weather;
    public String humidity;
    public String tempRange;
    public ExpBean exp;
    public int dateLong;
    public String lastUpdateTime;
    public String wind;

    public static class ExpBean {
        /**
         * ct : {"expName":"穿衣指数","level":"冷","prompt":"天气冷，建议着棉服、羽绒服、皮夹克加羊毛衫等冬季服装。年老体弱者宜着厚棉衣、冬大衣或厚羽绒服。"}
         */

        public CtBean ct;

        public static class CtBean {
            /**
             * expName : 穿衣指数
             * level : 冷
             * prompt : 天气冷，建议着棉服、羽绒服、皮夹克加羊毛衫等冬季服装。年老体弱者宜着厚棉衣、冬大衣或厚羽绒服。
             */

            public String expName;
            public String level;
            public String prompt;
        }
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("WeatherBean{");
        sb.append("date='").append(date).append('\'');
        sb.append(", airQuality='").append(airQuality).append('\'');
        sb.append(", weatherType=").append(weatherType);
        sb.append(", temp=").append(temp);
        sb.append(", airData=").append(airData);
        sb.append(", city='").append(city).append('\'');
        sb.append(", windLevel=").append(windLevel);
        sb.append(", pm25='").append(pm25).append('\'');
        sb.append(", weather='").append(weather).append('\'');
        sb.append(", humidity='").append(humidity).append('\'');
        sb.append(", tempRange='").append(tempRange).append('\'');
        sb.append(", exp=").append(exp);
        sb.append(", dateLong=").append(dateLong);
        sb.append(", lastUpdateTime='").append(lastUpdateTime).append('\'');
        sb.append(", wind='").append(wind).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
