package com.example.han.referralproject.network;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.example.han.referralproject.BuildConfig;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.AlreadyYuyue;
import com.example.han.referralproject.bean.ClueInfoBean;
import com.example.han.referralproject.bean.ContractInfo;
import com.example.han.referralproject.bean.DiseaseResult;
import com.example.han.referralproject.bean.Doctor;
import com.example.han.referralproject.bean.RobotAmount;
import com.example.han.referralproject.bean.UserInfo;
import com.example.han.referralproject.bean.VersionInfoBean;
import com.example.han.referralproject.bean.YzInfoBean;
import com.example.han.referralproject.children.model.SheetModel;
import com.example.han.referralproject.children.model.SongModel;
import com.example.han.referralproject.physicalexamination.bean.QuestionnaireBean;
import com.example.han.referralproject.radio.RadioEntity;
import com.example.han.referralproject.recyclerview.Docter;
import com.example.han.referralproject.recyclerview.OnlineTime;
import com.example.han.referralproject.shopping.Orders;
import com.example.han.referralproject.util.Utils;
import com.example.han.referralproject.video.VideoEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.recommend.bean.get.GoodBean;
import com.gcml.old.auth.entity.UserInfoBean;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkApi {
    public static final String BasicUrl = BuildConfig.SERVER_ADDRESS;
    //运动计划推荐
    public static final String SportHealthPlan = BasicUrl + "ZZB/api/healthMonitor/lifeTherapy/sport/";
    //一周健康计划推荐
    public static final String WeekHealthDietPlan = BasicUrl + "ZZB/api/healthMonitor/lifeTherapy/food/cookbook/";
    //每日食材推荐
    public static final String Daily_Food_Recommendation = BasicUrl + "ZZB/api/healthMonitor/lifeTherapy/food/";
    //每日建议摄入食盐、油等
    public static final String Daily_Recommended_Intake = BasicUrl + "ZZB/api/healthMonitor/lifeTherapy/food/dailyIntake/";
    //上周检测结果
    public static final String LastWeekAllReport = BasicUrl + "ZZB/api/healthMonitor/lifeTherapy/weekReport/";
    //本周计划
    public static final String ThisWeekPlan = BasicUrl + "ZZB/api/healthMonitor/lifeTherapy/detectionReport/";
    //高血压风险评估
    public static final String Hypertension_Detection = BasicUrl + "ZZB/api/healthMonitor/hypertension/analysis/";
    //糖尿病风险评估
    public static final String Bloodsugar_Detection = BasicUrl + "ZZB/api/healthMonitor/diabetes/questionnaire/";
    //健康方案中的药物方案
    public static final String Medicine_Program = BasicUrl + "ZZB/api/healthMonitor/medicine/hypertension/";

    public static final String ClueUrl = BasicUrl + "ZZB/br/selOneUserClueAll";
    public static final String BindDocUrl = BasicUrl + "ZZB/br/qianyue";
    //次层病症和首层诊断结果
    public static final String GetYZUrl = BasicUrl + "ZZB/bl/selYzAndTime";
    public static final String GetVersionUrl = BasicUrl + "ZZB/vc/selone";
    public static final String PAY_URL = BasicUrl + "ZZB/br/chongzhi";
    public static final String DOCTOR_URL = BasicUrl + "ZZB/docter/search_OneDocter";
    public static final String PERSON_URL = BasicUrl + "ZZB/br/selOneUser_con";
    public static final String PERSON_AMOUNT = BasicUrl + "ZZB/eq/eq_amount";


    public static final String YUYUE_ALREADY = BasicUrl + "ZZB/bl/selReserveStart_time";
    public static final String ADD_EAT_MEDICAL_URL = BasicUrl + "ZZB/br/addeatmod";
    public static final String GET_CONTRACT_INFO = BasicUrl + "ZZB/docter/docter_user";

    public static final String GET_MY_BASE_DATA = BasicUrl + "ZZB/br/selOneUserEverything";



    public static final String UPDATE_STATUS = BasicUrl + "ZZB/bl/app_update_reserve_state";


    public static final String DOCTER_LIST = BasicUrl + "ZZB/docter/seldoctors";

    public static final String ONLINE_DOCTER_LIST = BasicUrl + "ZZB/docter/search_online_status";

    public static final String ONLINE_DOCTER_ZIXUN = BasicUrl + "ZZB/docter/online_consulting";


    public static final String ORDER_INFO = BasicUrl + "ZZB/order/panding_pay";

    public static final String PAY_STATUS = BasicUrl + "ZZB/order/pay_pro";

    public static final String PAY_CANCEL = BasicUrl + "ZZB/order/delivery_del";

    public static final String ORDER_LIST = BasicUrl + "ZZB/order/one_more_orders";

    //修改个人基本信息
    public static final String Get_jibing = BasicUrl + "ZZB/bl/selSugByBname";

    public static final String CANCEL_CONTRACT = BasicUrl + "ZZB/br/updateUserState";

    public static final String GET_VIDEO_LIST = BasicUrl + "ZZB/vc/selAllUpload";

    public static final String GET_FM = BasicUrl + "ZZB/rep/selSomeImitate";

    public static final String GET_SHEET_LIST = BasicUrl + "ZZB/rep/sel_music_danforapp";

    private static final String GET_SONG_LIST = BasicUrl + "ZZB/rep/selSomeImitate";

    public static void getChildEduSheetList(
            int page,
            int limit,
            NetworkManager.SuccessCallback<List<SheetModel>> successCallback,
            NetworkManager.FailedCallback failedCallback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(limit));
        NetworkManager.getInstance().getResultClass(
                GET_SHEET_LIST,
                params,
                new TypeToken<List<SheetModel>>() {
                }.getType(),
                successCallback,
                failedCallback
        );
    }

    public static void getChildEduSongListBySheetId(
            int page,
            int limit,
            int sheetId,
            int type,
            String singer,
            NetworkManager.SuccessCallback<List<SongModel>> successCallback,
            NetworkManager.FailedCallback failedCallback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(limit));
        params.put("mid", String.valueOf(sheetId));
        params.put("type", String.valueOf(type));
        params.put("wr", singer);
        NetworkManager.getInstance().getResultClass(
                GET_SONG_LIST,
                params,
                new TypeToken<List<SongModel>>() {
                }.getType(),
                successCallback,
                failedCallback);
    }


    public static void getFM(
            String type,
            String page,
            String limit,
            NetworkManager.SuccessCallback<List<RadioEntity>> successCallback,
            NetworkManager.FailedCallback failedCallback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("type", type);
        params.put("page", page);
        params.put("limit", limit);
        params.put("mid", "0");
        NetworkManager.getInstance().getResultClass(GET_FM, params, new TypeToken<List<RadioEntity>>() {
        }.getType(), successCallback, failedCallback);
    }


    public static void getVideoList(
            int category,
            String version,
            String flag,
            int page,
            int pageSize,
            NetworkManager.SuccessCallback<List<VideoEntity>> successCallback,
            NetworkManager.FailedCallback failedCallback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("tag1", String.valueOf(category));
        params.put("tag2", version);
        params.put("flag", flag);
        params.put("page", String.valueOf(page));
        params.put("pagesize", String.valueOf(pageSize));
        NetworkManager.getInstance().postResultClass(GET_VIDEO_LIST, params, new TypeToken<List<VideoEntity>>() {
        }.getType(), successCallback, failedCallback);
    }

    public static void cancelContract(
            String bid,
            NetworkManager.SuccessCallback<Object> successCallback,
            NetworkManager.FailedCallback failedCallback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("bid", bid);
        NetworkManager.getInstance().postResultClass(CANCEL_CONTRACT, params, Object.class, successCallback, failedCallback);
    }
    public static final String GOODS_LIST = BasicUrl + "/ZZB/order/OneType_state";



    public static void PayInfo(String eqid, String bba, String time, String bid, NetworkManager.SuccessCallback<String> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("eqid", eqid);
        paramsMap.put("bba", bba);
        paramsMap.put("time", time);
        paramsMap.put("bid", bid);
        NetworkManager.getInstance().postResultClass(PAY_URL, paramsMap, String.class, listener, failedCallback);
    }

    public static void DoctorInfo(String bid, NetworkManager.SuccessCallback<Doctor> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("bid", bid);

        NetworkManager.getInstance().postResultClass(DOCTOR_URL, paramsMap, Doctor.class, listener, failedCallback);
    }

    public static void PersonInfo(String bid, NetworkManager.SuccessCallback<UserInfo> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("bid", bid);

        NetworkManager.getInstance().postResultClass(PERSON_URL, paramsMap, UserInfo.class, listener, failedCallback);
    }

    public static void Person_Amount(String eqid, NetworkManager.SuccessCallback<RobotAmount> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("eqid", eqid);

        NetworkManager.getInstance().postResultClass(PERSON_AMOUNT, paramsMap, RobotAmount.class, listener, failedCallback);
    }

    public static void YuYue_already(String docterid, NetworkManager.SuccessCallback<ArrayList<AlreadyYuyue>> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("docterid", docterid);

        NetworkManager.getInstance().postResultClass(YUYUE_ALREADY, paramsMap, new TypeToken<ArrayList<AlreadyYuyue>>() {
        }.getType(), listener, failedCallback);
    }


    public static void update_status(String rid, String state, NetworkManager.SuccessCallback<String> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();

        paramsMap.put("rid", rid);
        paramsMap.put("state", state);

        NetworkManager.getInstance().postResultString(UPDATE_STATUS, paramsMap, listener, failedCallback);
    }
    public static void doctor_list(int start, int limit, NetworkManager.SuccessCallback<ArrayList<Docter>> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("start", start + "");
        paramsMap.put("limit", limit + "");
        NetworkManager.getInstance().postResultClass(DOCTER_LIST, paramsMap, new TypeToken<ArrayList<Docter>>() {
        }.getType(), listener, failedCallback);
    }

    public static void onlinedoctor_list(int online_status, String doctername, int page, int pagesize, NetworkManager.SuccessCallback<ArrayList<Docter>> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("online_status", online_status + "");
        paramsMap.put("doctername", doctername);
        paramsMap.put("page", page + "");
        paramsMap.put("pagesize", pagesize + "");

        NetworkManager.getInstance().postResultClass(ONLINE_DOCTER_LIST, paramsMap, new TypeToken<ArrayList<Docter>>() {
        }.getType(), listener, failedCallback);
    }

    public static void goods_list(int state, NetworkManager.SuccessCallback<ArrayList<GoodBean>> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("state", state + "");

        NetworkManager.getInstance().postResultClass(GOODS_LIST, paramsMap, new TypeToken<ArrayList<GoodBean>>() {
        }.getType(), listener, failedCallback);
    }


    public static void onlinedoctor_zixun(String docterid, String userid, int state, NetworkManager.SuccessCallback<OnlineTime> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("docterid", docterid + "");
        paramsMap.put("userid", userid + "");
        paramsMap.put("state", state + "");

        NetworkManager.getInstance().postResultClass(ONLINE_DOCTER_ZIXUN, paramsMap, new TypeToken<OnlineTime>() {
        }.getType(), listener, failedCallback);
    }

    public static void preparingPay(String userid, String eqid, String articles, String number, String price, String photo, String time, NetworkManager.SuccessCallback<String> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userid", userid);
        paramsMap.put("eqid", eqid);
        paramsMap.put("articles", articles);
        paramsMap.put("number", number);
        paramsMap.put("price", price);
        paramsMap.put("photo", photo);
        paramsMap.put("time", time);

        NetworkManager.getInstance().postResultString(ORDER_INFO, paramsMap, listener, failedCallback);
    }

    public static void pay_status(String userid, String eqid, String orderid, NetworkManager.SuccessCallback<String> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userid", userid);
        paramsMap.put("eqid", eqid);
        paramsMap.put("orderid", orderid);
        NetworkManager.getInstance().postResultString(PAY_STATUS, paramsMap, listener, failedCallback);
    }

    public static void pay_cancel(String pay_state, String delivery_state, String display_state, String orderid, NetworkManager.SuccessCallback<String> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("pay_state", pay_state);
        paramsMap.put("delivery_state", delivery_state);
        paramsMap.put("display_state", display_state);
        paramsMap.put("orderid", orderid);
        NetworkManager.getInstance().postResultString(PAY_CANCEL, paramsMap, listener, failedCallback);
    }


    public static void order_list(String pay_state, String delivery_state, String display_state, String bname, String page, String limit, NetworkManager.SuccessCallback<ArrayList<Orders>> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("pay_state", pay_state);
        paramsMap.put("delivery_state", delivery_state);
        paramsMap.put("display_state", display_state);
        paramsMap.put("bname", bname);
        paramsMap.put("page", page);
        paramsMap.put("limit", limit);

        NetworkManager.getInstance().postResultClass(ORDER_LIST, paramsMap, new TypeToken<ArrayList<Orders>>() {
        }.getType(), listener, failedCallback);
    }

    public static void clueNotify(NetworkManager.SuccessCallback<ArrayList<ClueInfoBean>> callback) {
        if (TextUtils.isEmpty(UserSpHelper.getUserId())) {
            return;
        }
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("bid", UserSpHelper.getUserId());
        NetworkManager.getInstance().getResultClass(ClueUrl, paramsMap, new TypeToken<ArrayList<ClueInfoBean>>() {
        }.getType(), callback);
    }

    public static void getYzList(NetworkManager.SuccessCallback<ArrayList<YzInfoBean>> callback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userid", UserSpHelper.getUserId());
        NetworkManager.getInstance().getResultClass(GetYZUrl, paramsMap, new TypeToken<ArrayList<YzInfoBean>>() {
        }.getType(), callback);
    }

    public static void bindDoctor(String bid, int doctorId, NetworkManager.SuccessCallback<String> callback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("bid", bid);
        paramsMap.put("doid", String.valueOf(doctorId));
        NetworkManager.getInstance().postResultString(BindDocUrl, paramsMap, callback);
    }

    public static void getVersionInfo(NetworkManager.SuccessCallback<VersionInfoBean> callback, NetworkManager.FailedCallback failedCallback) {
        ApplicationInfo appInfo = null;
        String msg = "";
        try {
            appInfo = MyApplication.getInstance().getPackageManager()
                    .getApplicationInfo(MyApplication.getInstance().getPackageName(), PackageManager.GET_META_DATA);
            msg = appInfo.metaData.getString("com.gcml.version");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("vname", msg);
        NetworkManager.getInstance().getResultClass(GetVersionUrl, paramsMap, VersionInfoBean.class, callback, failedCallback);
    }

    public static void addEatMedicalRecord(
            String userName,
            String content,
            String state,
            NetworkManager.SuccessCallback<Object> successCallback,
            NetworkManager.FailedCallback failedCallback) {
        HashMap<String, String> params = new HashMap<>();
        if (TextUtils.isEmpty(userName)) {
            if (failedCallback != null) {
                failedCallback.onFailed("请重新登录");
            }
            return;
        }
        params.put("username", userName);
        params.put("jl", content);
        params.put("time", String.valueOf(Calendar.getInstance().getTimeInMillis()));
        params.put("state", state);
        NetworkManager.getInstance().postResultClass(ADD_EAT_MEDICAL_URL, params, Object.class, successCallback, failedCallback);
    }

    public static void getContractInfo(
            String doctorId,
            NetworkManager.SuccessCallback<ContractInfo> successCallback,
            NetworkManager.FailedCallback failedCallback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("userid", UserSpHelper.getUserId());
        params.put("docterid", doctorId);
        NetworkManager.getInstance().getResultClass(GET_CONTRACT_INFO, params, ContractInfo.class, successCallback, failedCallback);

    }

    /**
     * 获取个人的基本信息
     *
     * @param successCallback
     * @param failedCallback
     */
    public static void getMyBaseData(
            NetworkManager.SuccessCallback<UserInfoBean> successCallback, NetworkManager.FailedCallback failedCallback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("bid", UserSpHelper.getUserId());
        NetworkManager.getInstance().getResultClass(GET_MY_BASE_DATA, params, UserInfoBean.class, successCallback, failedCallback);
    }


    /**
     * 更具语音获取疾病结果
     *
     * @param bname
     * @param successCallback
     * @param failedCallback
     */
    public static void getJibing(String bname, NetworkManager.SuccessCallback<DiseaseResult> successCallback,
                                 NetworkManager.FailedCallback failedCallback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("bname", bname);
        NetworkManager.getInstance().getResultClass(Get_jibing, params, DiseaseResult.class, successCallback, failedCallback);
    }

    /**
     * 获取中医体质检测的题目
     * type 0：男性，1 女性
     */
    public static final String GET_QUESTIONNAIRE = BasicUrl + "/ZZB/api/healthMonitor/diabetes/questionnaire/";

    public static void getQuestionnaire(String type,
                                        NetworkManager.SuccessCallback<QuestionnaireBean> successCallback,
                                        NetworkManager.FailedCallback failedCallback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("type", type);
        NetworkManager.getInstance().getResultClass(GET_QUESTIONNAIRE,
                params,
                QuestionnaireBean.class,
                successCallback,
                failedCallback);
    }

    /**
     * 获取原发性高血压问卷
     */
    public static final String PRIMARY_HYPERTENSION_URL = BasicUrl + "/ZZB/api/healthMonitor/questionnaire/hypertension/primary/";

    public static void getPrimaryHypertensionQuestion(StringCallback callback) {
        OkGo.<String>get(PRIMARY_HYPERTENSION_URL).
                execute(callback);
    }

    /**
     * 提交原发性高血压问卷
     */
    public static final String POST_PRIMARY_HYPERTENSION_URL = BasicUrl + "/ZZB/api/healthMonitor/questionnaire/hypertension/primary/";

    public static void postPrimaryHypertensionQuestion(String postJson, String userId, StringCallback callback) {
        OkGo.<String>post(POST_PRIMARY_HYPERTENSION_URL + userId + "/")
                .params("userId", userId)
                .upJson(postJson)
                .execute(callback);
    }


    /**
     * 高血压-->获取心血管问卷
     */
    public static final String HYPERTENSION_URL = BasicUrl + "/ZZB/api/healthMonitor/questionnaire/hypertension/heart/";

    public static void getHypertensionQuestion(StringCallback callback) {
        OkGo.<String>get(HYPERTENSION_URL).
                execute(callback);
    }


    /**
     * 高血压-->提交心血管问卷
     */
    public static void postHypertensionQuestion(String postJson, String userId, StringCallback callback) {
        OkGo.<String>post(HYPERTENSION_URL + userId + "/")
                .params("userId", userId)
                .upJson(postJson)
                .execute(callback);
    }


    /**
     * 正常高值-->高血压风险评估
     */
    public static final String NORMALHIGHT_URL = BasicUrl + "/ZZB/api/healthMonitor/questionnaire/hypertension/risk/";

    public static void getNormalHightQuestion(StringCallback callback) {
        OkGo.<String>get(NORMALHIGHT_URL).
                execute(callback);
    }


    /**
     * 正常高值-->高血压风险评估
     */
    public static void postNormalHightQuestion(String postJson, String userId, StringCallback callback) {
        OkGo.<String>post(NORMALHIGHT_URL + userId + "/")
                .params("userId", userId)
                .upJson(postJson)
                .execute(callback);
    }


    /**
     * 原发性高血压 修改
     */
    public static final String POST_ORIGIN_HYPERTENTION = BasicUrl + "/ZZB/api/healthMonitor/hypertension/diagnose/primary/";

    public static void postOriginHypertension(String hypertensionPrimaryState, String userId, StringCallback callback) {
        OkGo.<String>post(POST_ORIGIN_HYPERTENTION + userId + "/")
                .params("userId", userId)
                .params("hypertensionPrimaryState", hypertensionPrimaryState)
                .execute(callback);
    }

    /**
     * 靶器官判定
     */
    public static final String POST_TARGET_HYPERTENTION = BasicUrl + "/ZZB/api/healthMonitor/hypertension/diagnose/target/";

    public static void postTargetHypertension(String hypertensionTarget, String userId, StringCallback callback) {
        OkGo.<String>post(POST_TARGET_HYPERTENTION + userId + "/")
                .params("userId", userId)
                .params("hypertensionTarget", hypertensionTarget)
                .execute(callback);
    }

    /**
     * 获取诊断信息
     */
    public static final String GET_DIAGNOSE_INFO = BasicUrl + "/ZZB/api/healthMonitor/hypertension/diagnose/";

    public static void getDiagnoseInfo(String userId, StringCallback callback) {
        OkGo.<String>get(GET_DIAGNOSE_INFO + userId + "/")
                .params("userId", userId)
                .execute(callback);
    }


    /**
     * 获取诊断信息-->重新生成方案
     */
    public static void getDiagnoseInfoNew(String userId, StringCallback callback) {
        OkGo.<String>get(GET_DIAGNOSE_INFO + userId + "/new/")
                .params("userId", userId)
                .execute(callback);
    }


    /**
     * 老年人中医药健康管理服务记录表
     */

    public static String TCM_HEALTH_MANAGER_FOR_OLDER = BasicUrl + "/ZZB/api/health/inquiry/constitution/questionnaire/";

    public static void getHealthManagementForOlder(StringCallback stringCallback) {
        OkGo.<String>get(TCM_HEALTH_MANAGER_FOR_OLDER).headers("equipmentId", Utils.getDeviceId()).execute(stringCallback);
    }


    /**
     * 老年人中医药健康管理服务记录表  提交答案
     */
    public static String POST_HEALTH_MANAGEMENT_ANWSER_URL = BasicUrl + "/ZZB/api/health/inquiry/constitution/questionnaire/";

    public static void postHealthManagementAnwser(String anwserJson, StringCallback callback) {
        OkGo.<String>post(POST_HEALTH_MANAGEMENT_ANWSER_URL)
                .headers("equipmentId", Utils.getDeviceId())
                .upJson(anwserJson)
                .execute(callback);
    }

}
