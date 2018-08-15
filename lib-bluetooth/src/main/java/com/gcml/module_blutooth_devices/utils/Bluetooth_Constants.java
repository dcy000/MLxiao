package com.gcml.module_blutooth_devices.utils;

public class Bluetooth_Constants {
    public interface SP {
        /**
         * 保存sp中血氧蓝牙、血压蓝牙、血糖蓝牙、心电蓝牙、指纹蓝牙、耳温蓝牙、体重、三合一蓝牙key
         */
        String SP_SAVE_BLOODOXYGEN = "sp_save_bloodoxygen";
        String SP_SAVE_BLOODPRESSURE = "sp_save_bloodpressure";
        String SP_SAVE_BLOODSUGAR = "sp_save_bloodsugar";
        String SP_SAVE_ECG = "sp_save_ecg";
        String SP_SAVE_FINGERPRINT = "sp_save_fingerprint";
        String SP_SAVE_TEMPERATURE = "sp_save_temperature";
        String SP_SAVE_WEIGHT = "sp_save_weight";
        String SP_SAVE_THREE_IN_ONE="sp_save_three_in_one";


        /**
         * 存在sp中用户名的key
         */
        String KEY_USER_NAME="user_name";
        /**
         * 存在sp中用户的sex
         */
        String KEY_USER_SEX="user_sex";
        /**
         * 存在SP中用户的年龄
         */
        String KEY_USER_AGE="user_age";
        /**
         * 存在SP中用户的电话号码
         */
        String KEY_USER_PHONE="user_phone_num";
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
