package com.gcml.module_blutooth_devices.base;

import java.util.HashMap;

public class DeviceBrand {
    public static final HashMap<String, String> BLOODPRESSURE = new HashMap<String, String>() {
        {
            put("iChoice", "血压计·超思");
            put("KN-550BT", "血压计·九安");
            put("eBlood-Pressure", "血压计·自家");
            put("LD", "血压计·西恩4");
            put("Dual-SPP", "血压计·西恩2");
            put("Yuwell", "血压计·鱼跃");
        }
    };
    public static final HashMap<String, String> BLOODOXYGEN = new HashMap<String, String>() {
        {
            put("iChoice", "血氧仪·超思");
            put("SpO2080971", "血氧仪·康泰");
            put("POD", "血氧仪·自家");
        }
    };
    public static final HashMap<String, String> BLOODSUGAR = new HashMap<String, String>() {
        {
            put("BLE-Glucowell", "血糖仪·好糖");
            put("BLE-BDE_WEIXIN_TTM", "血糖仪·三诺");
            put("Bioland-BGM", "血糖仪·自家");
        }
    };
    public static final HashMap<String, String> ECG = new HashMap<String, String>() {
        {
            put("WeCardio", "心电仪·博声");
            put("A12-B", "心电仪·超思");
            put("PC80B-BLE", "心电仪·自家");
        }
    };
    public static final HashMap<String, String> TEMPERATURE = new HashMap<String, String>() {
        {
            put("AET-WD", "耳温枪·爱立康");
            put("ClinkBlood", "耳温枪·福达康");
            put("MEDXING-IRT", "耳温枪·美的连");
            put("FSRKB-EWQ01", "耳温枪·自家");
        }
    };
    public static final HashMap<String, String> WEIGHT = new HashMap<String, String>() {
        {
            put("VScale", "体重秤·同方");
            put("iChoice", "体重秤·超思");
            put("000FatScale01", "体重秤·自家");
            put("dr01", "体重秤·思麦德");
            put("SHHC-60F1", "体重秤·怡可");
            put("SENSSUN", "体重秤·香山");
            put("IF_B2A", "体重秤·香山");
        }
    };
    public static final HashMap<String, String> THREE_IN_ONE = new HashMap<String, String>() {
        {
            put("BeneCheck", "三合一·自家");
        }
    };
}
