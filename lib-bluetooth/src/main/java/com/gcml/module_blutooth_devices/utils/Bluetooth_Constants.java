package com.gcml.module_blutooth_devices.utils;

public class Bluetooth_Constants {
    public interface SP {
        /**
         * 保存sp中血氧蓝牙、血压蓝牙、血糖蓝牙、心电蓝牙、指纹蓝牙、耳温蓝牙、体重、三合一蓝牙、呼吸家key
         */
        String SP_SAVE_BLOODOXYGEN = "sp_save_bloodoxygen";
        String SP_SAVE_BLOODPRESSURE = "sp_save_bloodpressure";
        String SP_SAVE_BLOODSUGAR = "sp_save_bloodsugar";
        String SP_SAVE_ECG = "sp_save_ecg";
        String SP_SAVE_FINGERPRINT = "sp_save_fingerprint";
        String SP_SAVE_TEMPERATURE = "sp_save_temperature";
        String SP_SAVE_WEIGHT = "sp_save_weight";
        String SP_SAVE_THREE_IN_ONE="sp_save_three_in_one";
        String SP_SAVE_BREATH_HOME="sp-save_breath_home";
    }
    public interface BoSheng{
        //app id
        String BoSheng_APP_ID="855623";
        String BoSheng_URL="http://cnapp.wecardio.com:808";
        String BoSheng_PASSWORD="hzml1578";
//        String BoSheng_USERNAME="gcml_company";
        String BoSheng_USERNAME="151****8143";
        String BoSheng_USER_PASSWORD="123456_GCML";
    }
}
