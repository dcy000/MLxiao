package com.gzq.test_all_devices.net;

import com.google.gson.reflect.TypeToken;
import com.gzq.test_all_devices.MyApplication;
import com.gzq.test_all_devices.health_record_bean.BUA;
import com.gzq.test_all_devices.health_record_bean.BloodOxygenHistory;
import com.gzq.test_all_devices.health_record_bean.BloodPressureHistory;
import com.gzq.test_all_devices.health_record_bean.BloodSugarHistory;
import com.gzq.test_all_devices.health_record_bean.CholesterolHistory;
import com.gzq.test_all_devices.health_record_bean.ECGHistory;
import com.gzq.test_all_devices.health_record_bean.HeartRateHistory;
import com.gzq.test_all_devices.health_record_bean.PulseHistory;
import com.gzq.test_all_devices.health_record_bean.TemperatureHistory;
import com.gzq.test_all_devices.health_record_bean.WeightHistory;

import java.util.ArrayList;
import java.util.HashMap;

public class NetworkApi {
//            public static final String BasicUrl = "http://116.62.36.12:8080";//备用服务器
//    public static final String BasicUrl = "http://118.31.238.207:8080";//正式服务器
//    public static final String BasicUrl = "http://192.168.200.103:8080";//孙高峰
//    public static final String BasicUrl = "http://192.168.200.157:8080";//文博本地
    //    public static final String BasicUrl = "http://192.168.200.157:8080";//文博本地
    public static final String BasicUrl = "http://192.168.200.117:8080";//林天聪
//    public static final String BasicUrl = "http://47.96.98.60:8080";//测试服务器


    //生活疗法
    public static final String Life_Therapy = BasicUrl + "/ZZB/api/healthMonitor/report/lifeTherapy/";
    //运动计划推荐
    public static final String SportHealthPlan = BasicUrl + "/ZZB/api/healthMonitor/lifeTherapy/sport/";
    //一周健康计划推荐
    public static final String WeekHealthDietPlan = BasicUrl + "/ZZB/api/healthMonitor/lifeTherapy/food/cookbook/";
    //每日食材推荐
    public static final String Daily_Food_Recommendation = BasicUrl + "/ZZB/api/healthMonitor/lifeTherapy/food/";
    //每日建议摄入食盐、油等
    public static final String Daily_Recommended_Intake = BasicUrl + "/ZZB/api/healthMonitor/lifeTherapy/food/dailyIntake/";
    //上周检测结果
    public static final String LastWeekAllReport = BasicUrl + "/ZZB/api/healthMonitor/lifeTherapy/weekReport/";
    //本周计划
    public static final String ThisWeekPlan = BasicUrl + "/ZZB/api/healthMonitor/lifeTherapy/detectionReport/";
    //血糖周报告、月报告
    public static final String WeeklyOrMonthlyBloodsugar = BasicUrl + "/ZZB/api/healthMonitor/report/diabetes/week/";
    //血压周报告、月报告接口
    public static final String WeeklyOrMonthlyReport = BasicUrl + "/ZZB/api/healthMonitor/report/hypertension/week/";
    //高血压风险评估
    public static final String Hypertension_Detection = BasicUrl + "/ZZB/api/healthMonitor/hypertension/analysis/";
    //糖尿病风险评估
    public static final String Bloodsugar_Detection = BasicUrl + "/ZZB/api/healthMonitor/diabetes/questionnaire/";
    //健康方案中的药物方案
    public static final String Medicine_Program=BasicUrl+"/ZZB/api/healthMonitor/medicine/hypertension/{userId}/";

    public static final String LoginUrl = BasicUrl + "/ZZB/login/applogin";
    public static final String RegisterUrl = BasicUrl + "/ZZB/br/appadd";
    public static final String AddMhUrl = BasicUrl + "/ZZB/br/mhrecord";
    public static final String ClueUrl = BasicUrl + "/ZZB/br/selOneUserClueAll";
    public static final String BindDocUrl = BasicUrl + "/ZZB/br/qianyue";
    public static final String GetAllSymUrl = BasicUrl + "/ZZB/bl/selAllSym";
    //次层病症和首层诊断结果
    public static final String AnalyseUrl = BasicUrl + "/ZZB/bl/selcon";
    public static final String GetYZUrl = BasicUrl + "/ZZB/bl/selYzAndTime";
    public static final String GetVersionUrl = BasicUrl + "/ZZB/vc/selone";
    public static final String UploadDataUrl = BasicUrl + "/ZZB/bl/doaddbl";
    public static final String CHARGE_URL = BasicUrl + "/ZZB/eq/koufei";
    public static final String PAY_URL = BasicUrl + "/ZZB/br/chongzhi";
    public static final String DOCTOR_URL = BasicUrl + "/ZZB/docter/search_OneDocter";
    public static final String PERSON_URL = BasicUrl + "/ZZB/br/selOneUser_con";
    public static final String GetInfo_URL = BasicUrl + "/ZZB/br/selMoreUser";
    public static final String PERSON_AMOUNT = BasicUrl + "/ZZB/eq/eq_amount";
    public static final String YUYUE_URL = BasicUrl + "/ZZB/bl/insertReserve";
    public static final String YUYUE_URL_INFO = BasicUrl + "/ZZB/bl/selAllreserveByDoidAndUserid";
    public static final String YUYUE_URL_HISTORY = BasicUrl + "/ZZB/bl/selReserveByDoidAndUseridAndState";


    public static final String YUYUE_ALREADY = BasicUrl + "/ZZB/bl/selReserveStart_time";

    public static final String YUYUE_URL_CANCEL = BasicUrl + "/ZZB/bl/delReserveByRid";
    public static final String TOKEN_URL = BasicUrl + "/ZZB/br/seltoken";
    public static final String RETURN_IMAGE_URL = BasicUrl + "/ZZB/br/upUser_photo";
    public static final String ADD_EAT_MEDICAL_URL = BasicUrl + "/ZZB/br/addeatmod";
    public static final String GET_CONTRACT_INFO = BasicUrl + "/ZZB/docter/docter_user";

    public static final String GET_MY_BASE_DATA = BasicUrl + "/ZZB/br/selOneUserEverything";

    public static final String DOCTER_APPRAISER = BasicUrl + "/ZZB/pj/insert";

    public static final String GOODS_INSERT = BasicUrl + "/ZZB/order/insert_one";

    public static final String UPDATE_STATUS = BasicUrl + "/ZZB/bl/app_update_reserve_state";


    public static final String DOCTER_LIST = BasicUrl + "/ZZB/docter/seldoctors";

    public static final String ONLINE_DOCTER_LIST = BasicUrl + "/ZZB/docter/search_online_status";

    public static final String ONLINE_DOCTER_ZIXUN = BasicUrl + "/ZZB/docter/online_consulting";


    public static final String ORDER_INFO = BasicUrl + "/ZZB/order/panding_pay";

    public static final String PAY_STATUS = BasicUrl + "/ZZB/order/pay_pro";

    public static final String PAY_CANCEL = BasicUrl + "/ZZB/order/delivery_del";

    public static final String ORDER_LIST = BasicUrl + "/ZZB/order/one_more_orders";

    public static final String Get_HealthRecord = BasicUrl + "/ZZB/br/cl_data";//正式服务器
    //    public static final String Get_HealthRecord=BasicUrl+"/ZZB/br/cl";
    //全部医生
    public static final String Get_AllDotor = BasicUrl + "/ZZB/docter/seldoctors";
    public static final String FIND_ACCOUNT = BasicUrl + "/ZZB/acc/sel_account";
    public static final String SET_PASSWORD = BasicUrl + "/ZZB/acc/update_account_pwd";


    //修改个人基本信息
    public static final String Alert_Basedata = BasicUrl + "/ZZB/br/update_user_onecon";
    public static final String Get_jibing = BasicUrl + "/ZZB/bl/selSugByBname";
    public static final String IS_PHONE_REGISTERED = BasicUrl + "/ZZB/login/tel_isClod";

    public static final String EQ_PRE_AMOUNT = BasicUrl + "/ZZB/eq/selPaidAmountByEqid";
    public static final String CANCEL_CONTRACT = BasicUrl + "/ZZB/br/updateUserState";
    public static final String CHECK_CONTRACT = BasicUrl + "/ZZB/eq/selCountPaidAmountByEqid";

    public static final String GET_VIDEO_LIST = BasicUrl + "/ZZB/vc/selAllUpload";
    public static final String GET_CODE = BasicUrl + "/ZZB/br/GainCode";

    public static final String GET_FM = BasicUrl + "/ZZB/rep/selSomeImitate";
    public static final String Add_Group = BasicUrl + "/ZZB/xf/insert_group_record";
    public static final String Change_Group_Status = BasicUrl + "/ZZB/xf/update_group_record";

    //    public static final String Query_Group=BasicUrl+"/ZZB/xf/select_group_record";
    public static final String Query_Group_118 = "http://118.31.238.207:8080/ZZB/xf/select_group_record";
    public static final String Query_Group_116 = "http://116.62.36.12:8080/ZZB/xf/select_group_record";
    public static final String Get_Week_or_Month_Report = BasicUrl + "/ZZB/ai/sel";
    public static final String Get_Eat_And_Sport = BasicUrl + "/ZZB/ai//EatAndSport";

    public static final String Query_Group = BasicUrl + "/ZZB/xf/select_group_record";

    public static final String GET_SHEET_LIST = BasicUrl + "/ZZB/rep/sel_music_danforapp";

    private static final String GET_SONG_LIST = BasicUrl + "/ZZB/rep/selSomeImitate";

    public static final String Get_Message = BasicUrl + "/ZZB/xf/select_tuisong";
    public static final String Get_Week_Report = BasicUrl + "/AI/ai/selmap";

    public static final String POST_HEAlTH_DIARY = BasicUrl + "/ZZB/ai/insert_influence";
    public static final String POST_TEL_MESSAGE = "ZZB/br/br_teltixing";

    /**
     * 获取体温历史数据
     *
     * @param successCallback
     */


    public static void getTemperatureHistory(String start, String end, String temp, NetworkManager.SuccessCallback<ArrayList<TemperatureHistory>> successCallback, NetworkManager.FailedCallback failedCallback
    ) {
        HashMap<String, String> params = new HashMap<>();

        params.put("bid", MyApplication.getInstance().userId);
//        params.put("bid","100001");
        params.put("temp", temp);
        params.put("starttime", start);
        params.put("endtime", end);
        NetworkManager.getInstance().getResultClass(Get_HealthRecord, params, new TypeToken<ArrayList<TemperatureHistory>>() {
                }.getType(),
                successCallback, failedCallback);
    }

    /**
     * 获取血压的历史数据
     *
     * @param temp
     * @param successCallback
     */
    public static void getBloodpressureHistory(String start, String end, String temp, NetworkManager.SuccessCallback<ArrayList<BloodPressureHistory>> successCallback, NetworkManager.FailedCallback failedCallback
    ) {
        HashMap<String, String> params = new HashMap<>();
        params.put("bid", MyApplication.getInstance().userId);
//        params.put("bid","100001");
        params.put("temp", temp);
        params.put("starttime", start);
        params.put("endtime", end);
        NetworkManager.getInstance().getResultClass(Get_HealthRecord, params, new TypeToken<ArrayList<BloodPressureHistory>>() {
                }.getType(),
                successCallback, failedCallback);
    }

    /**
     * 血糖
     *
     * @param temp
     * @param successCallback
     */

    public static void getBloodSugarHistory(String start, String end, String temp, NetworkManager.SuccessCallback<ArrayList<BloodSugarHistory>> successCallback, NetworkManager.FailedCallback failedCallback
    ) {
        HashMap<String, String> params = new HashMap<>();
        params.put("bid", MyApplication.getInstance().userId);
        params.put("temp", temp);
        params.put("starttime", start);
        params.put("endtime", end);
        NetworkManager.getInstance().getResultClass(Get_HealthRecord, params, new TypeToken<ArrayList<BloodSugarHistory>>() {
                }.getType(),
                successCallback, failedCallback);
    }

    /**
     * 血氧
     *
     * @param temp
     * @param successCallback
     */
    public static void getBloodOxygenHistory(String start, String end, String temp, NetworkManager.SuccessCallback<ArrayList<BloodOxygenHistory>> successCallback, NetworkManager.FailedCallback failedCallback
    ) {
        HashMap<String, String> params = new HashMap<>();
        params.put("bid", MyApplication.getInstance().userId);
        params.put("temp", temp);
        params.put("starttime", start);
        params.put("endtime", end);
        NetworkManager.getInstance().getResultClass(Get_HealthRecord, params, new TypeToken<ArrayList<BloodOxygenHistory>>() {
                }.getType(),
                successCallback, failedCallback);
    }

    /**
     * 心率
     *
     * @param temp
     * @param successCallback
     */
    public static void getHeartRateHistory(String start, String end, String temp, NetworkManager.SuccessCallback<ArrayList<HeartRateHistory>> successCallback, NetworkManager.FailedCallback failedCallback
    ) {
        HashMap<String, String> params = new HashMap<>();
        params.put("bid", MyApplication.getInstance().userId);
        params.put("temp", temp);
        params.put("starttime", start);
        params.put("endtime", end);
        NetworkManager.getInstance().getResultClass(Get_HealthRecord, params, new TypeToken<ArrayList<HeartRateHistory>>() {
                }.getType(),
                successCallback, failedCallback);
    }

    /**
     * 脉搏
     *
     * @param temp
     * @param successCallback
     */
    public static void getPulseHistory(String start, String end, String temp, NetworkManager.SuccessCallback<ArrayList<PulseHistory>> successCallback, NetworkManager.FailedCallback failedCallback
    ) {
        HashMap<String, String> params = new HashMap<>();
        params.put("bid", MyApplication.getInstance().userId);
        params.put("temp", temp);
        params.put("starttime", start);
        params.put("endtime", end);
        NetworkManager.getInstance().getResultClass(Get_HealthRecord, params, new TypeToken<ArrayList<PulseHistory>>() {
                }.getType(),
                successCallback, failedCallback);
    }

    /**
     * 胆固醇
     *
     * @param temp
     * @param successCallback
     */
    public static void getCholesterolHistory(String start, String end, String temp, NetworkManager.SuccessCallback<ArrayList<CholesterolHistory>> successCallback, NetworkManager.FailedCallback failedCallback
    ) {
        HashMap<String, String> params = new HashMap<>();
        params.put("bid", MyApplication.getInstance().userId);
//        params.put("bid","100001");
        params.put("temp", temp);
        params.put("starttime", start);
        params.put("endtime", end);
        NetworkManager.getInstance().getResultClass(Get_HealthRecord, params, new TypeToken<ArrayList<CholesterolHistory>>() {
                }.getType(),
                successCallback, failedCallback);
    }

    /**
     * 血尿酸
     *
     * @param temp
     * @param successCallback
     */
    public static void getBUAHistory(String start, String end, String temp, NetworkManager.SuccessCallback<ArrayList<BUA>> successCallback, NetworkManager.FailedCallback failedCallback
    ) {
        HashMap<String, String> params = new HashMap<>();
        params.put("bid", MyApplication.getInstance().userId);
//        params.put("bid","100001");
        params.put("temp", temp);
        params.put("starttime", start);
        params.put("endtime", end);
        NetworkManager.getInstance().getResultClass(Get_HealthRecord, params, new TypeToken<ArrayList<BUA>>() {
                }.getType(),
                successCallback, failedCallback);
    }

    /**
     * 心电
     *
     * @param temp
     * @param successCallback
     */
    public static void getECGHistory(String start, String end, String temp, NetworkManager.SuccessCallback<ArrayList<ECGHistory>> successCallback, NetworkManager.FailedCallback failedCallback
    ) {
        HashMap<String, String> params = new HashMap<>();
        params.put("bid", MyApplication.getInstance().userId);
//        params.put("bid", "100001");
        params.put("temp", temp);
        params.put("starttime", start);
        params.put("endtime", end);
        NetworkManager.getInstance().getResultClass(Get_HealthRecord, params, new TypeToken<ArrayList<ECGHistory>>() {
                }.getType(),
                successCallback, failedCallback);
    }

    /**
     * 体重
     *
     * @param start
     * @param end
     * @param temp
     * @param successCallback
     * @param failedCallback
     */
    public static void getWeight(String start, String end, String temp, NetworkManager.SuccessCallback<ArrayList<WeightHistory>> successCallback, NetworkManager.FailedCallback failedCallback
    ) {
        HashMap<String, String> params = new HashMap<>();

        params.put("bid", MyApplication.getInstance().userId);
//        params.put("bid","100001");
        params.put("temp", temp);
        params.put("starttime", start);
        params.put("endtime", end);
        NetworkManager.getInstance().getResultClass(Get_HealthRecord, params, new TypeToken<ArrayList<WeightHistory>>() {
                }.getType(),
                successCallback, failedCallback);
    }

}
