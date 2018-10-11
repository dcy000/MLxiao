package com.gcml.common.data;

import java.util.HashMap;
import java.util.Map;

public class HealthInfo {
    public static final Map<String, String> EAT_MAP = new HashMap<>();

    static {
        EAT_MAP.put("1", "荤素搭配");
        EAT_MAP.put("2", "偏好吃荤");
        EAT_MAP.put("3", "偏好吃素");
        EAT_MAP.put("4", "偏好吃咸");
        EAT_MAP.put("5", "偏好油腻");
        EAT_MAP.put("6", "偏好甜食");
        EAT_MAP.put("荤素搭配", "1");
        EAT_MAP.put("偏好吃荤", "2");
        EAT_MAP.put("偏好吃素", "3");
        EAT_MAP.put("偏好吃咸", "4");
        EAT_MAP.put("偏好油腻", "5");
        EAT_MAP.put("偏好甜食", "6");
    }

    public static final Map<String, String> SMOKE_MAP = new HashMap<>();

    static {
        SMOKE_MAP.put("1", "经常抽烟");
        SMOKE_MAP.put("2", "偶尔抽烟");
        SMOKE_MAP.put("3", "从不抽烟");
        SMOKE_MAP.put("经常抽烟", "1");
        SMOKE_MAP.put("偏好油腻", "2");
        SMOKE_MAP.put("偏好甜食", "3");
    }

    public static final Map<String, String> SPORTS_MAP = new HashMap<>();

    static {
        SPORTS_MAP.put("1", "每天一次");
        SPORTS_MAP.put("2", "每周几次");
        SPORTS_MAP.put("3", "偶尔运动");
        SPORTS_MAP.put("4", "从不运动");
        SPORTS_MAP.put("每天一次", "1");
        SPORTS_MAP.put("每周几次", "2");
        SPORTS_MAP.put("偶尔运动", "3");
        SPORTS_MAP.put("从不运动", "4");
    }

    public static final Map<String, String> DRINK_MAP = new HashMap<>();

    static {
        DRINK_MAP.put("1", "经常喝酒");
        DRINK_MAP.put("2", "偶尔喝酒");
        DRINK_MAP.put("3", "从不喝酒");
        DRINK_MAP.put("经常喝酒", "1");
        DRINK_MAP.put("偶尔喝酒", "2");
        DRINK_MAP.put("从不喝酒", "3");
    }


    public static final Map<String, String> DESEASE_HISTORY_MAP = new HashMap<>();

    static {
        DESEASE_HISTORY_MAP.put("1", "高血压");
        DESEASE_HISTORY_MAP.put("2", "糖尿病");
        DESEASE_HISTORY_MAP.put("3", "冠心病");
        DESEASE_HISTORY_MAP.put("4", "慢阻肺");
        DESEASE_HISTORY_MAP.put("5", "孕产妇");
        DESEASE_HISTORY_MAP.put("6", "痛风");
        DESEASE_HISTORY_MAP.put("7", "甲亢");
        DESEASE_HISTORY_MAP.put("8", "高血脂");
        DESEASE_HISTORY_MAP.put("9", "其他");
        DESEASE_HISTORY_MAP.put("高血压", "1");
        DESEASE_HISTORY_MAP.put("糖尿病", "2");
        DESEASE_HISTORY_MAP.put("冠心病", "3");
        DESEASE_HISTORY_MAP.put("慢阻肺", "4");
        DESEASE_HISTORY_MAP.put("孕产妇", "5");
        DESEASE_HISTORY_MAP.put("痛风", "6");
        DESEASE_HISTORY_MAP.put("甲亢", "7");
        DESEASE_HISTORY_MAP.put("高血脂", "8");
        DESEASE_HISTORY_MAP.put("其他", "9");
    }

    public static String getDeseaseHistory(String mh) {
        String[] mhs = mh.split(",");
        if (mhs == null || mhs.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < mhs.length; i++) {
            if (mhs[i].equals("1")) {
                builder.append("高血压");
            } else if (mhs[i].equals("2"))
                builder.append("糖尿病");
            else if (mhs[i].equals("3"))
                builder.append("冠心病");
            else if (mhs[i].equals("4"))
                builder.append("慢阻肺");
            else if (mhs[i].equals("5"))
                builder.append("孕产妇");
            else if (mhs[i].equals("6"))
                builder.append("痛风");
            else if (mhs[i].equals("7"))
                builder.append("甲亢");
            else if (mhs[i].equals("8"))
                builder.append("高血脂");
            else if (mhs[i].equals("9"))
                builder.append("其他");
            else if (mhs[i].equals("11"))
                builder.append("无");
            if (mhs.length - 1 != i) {
                builder.append(",");
            }
        }
        return builder.toString();
    }

}
