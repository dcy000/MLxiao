package com.example.han.referralproject.network;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.example.han.referralproject.BuildConfig;
import com.example.han.referralproject.bean.AlreadyYuyue;
import com.example.han.referralproject.bean.ClueInfoBean;
import com.example.han.referralproject.bean.DiseaseResult;
import com.example.han.referralproject.bean.Doctor;
import com.example.han.referralproject.bean.MonthlyReport;
import com.example.han.referralproject.bean.RobotAmount;
import com.example.han.referralproject.bean.UserInfo;
import com.example.han.referralproject.bean.VersionInfoBean;
import com.example.han.referralproject.bean.YuYueInfo;
import com.example.han.referralproject.bean.YzInfoBean;
import com.example.han.referralproject.health.model.WeekReportModel;
import com.example.han.referralproject.recyclerview.Docter;
import com.example.han.referralproject.recyclerview.OnlineTime;
import com.example.han.referralproject.shopping.Goods;
import com.example.han.referralproject.shopping.Orders;
import com.example.han.referralproject.util.Utils;
import com.google.gson.reflect.TypeToken;
import com.gzq.lib_core.base.Box;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NetworkApi {

    public static final String BasicUrl = BuildConfig.SERVER_ADDRESS;
    public static final String ClueUrl = BasicUrl + "/ZZB/br/selOneUserClueAll";
    public static final String GetYZUrl = BasicUrl + "/ZZB/bl/selYzAndTime";
    public static final String GetVersionUrl = BasicUrl + "/ZZB/vc/selone";
    public static final String CHARGE_URL = BasicUrl + "/ZZB/eq/koufei";
    public static final String PAY_URL = BasicUrl + "/ZZB/br/chongzhi";
    public static final String DOCTOR_URL = BasicUrl + "/ZZB/docter/search_OneDocter";
    public static final String PERSON_URL = BasicUrl + "/ZZB/br/selOneUser_con";
    public static final String PERSON_AMOUNT = BasicUrl + "/ZZB/eq/eq_amount";
    public static final String YUYUE_URL = BasicUrl + "/ZZB/bl/insertReserve";
    public static final String YUYUE_URL_INFO = BasicUrl + "/ZZB/bl/selAllreserveByDoidAndUserid";
    public static final String YUYUE_ALREADY = BasicUrl + "/ZZB/bl/selReserveStart_time";
    public static final String YUYUE_URL_CANCEL = BasicUrl + "/ZZB/bl/delReserveByRid";
    public static final String ADD_EAT_MEDICAL_URL = BasicUrl + "/ZZB/br/addeatmod";
    public static final String DOCTER_APPRAISER = BasicUrl + "/ZZB/pj/insert";
    public static final String UPDATE_STATUS = BasicUrl + "/ZZB/bl/app_update_reserve_state";
    public static final String DOCTER_LIST = BasicUrl + "/ZZB/docter/seldoctors";
    public static final String ONLINE_DOCTER_LIST = BasicUrl + "/ZZB/docter/search_online_status";
    public static final String ONLINE_DOCTER_ZIXUN = BasicUrl + "/ZZB/docter/online_consulting";
    public static final String PAY_CANCEL = BasicUrl + "/ZZB/order/delivery_del";
    public static final String ORDER_LIST = BasicUrl + "/ZZB/order/one_more_orders";
    public static final String FIND_ACCOUNT = BasicUrl + "/ZZB/acc/sel_account";
    public static final String SET_PASSWORD = BasicUrl + "/ZZB/acc/update_account_pwd";
    public static final String Get_jibing = BasicUrl + "/ZZB/bl/selSugByBname";
    public static final String Get_Week_or_Month_Report = BasicUrl + "/ZZB/ai/sel";

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

    public static void YuYue(String start_time, String end_time, String userid, String docterid, NetworkManager.SuccessCallback<String> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("start_time", start_time);
        paramsMap.put("end_time", end_time);
        paramsMap.put("userid", userid + "");
        paramsMap.put("docterid", docterid + "");
        NetworkManager.getInstance().postResultClass(YUYUE_URL, paramsMap, String.class, listener, failedCallback);
    }


    public static void YuYue_info(String userid, String docterid, NetworkManager.SuccessCallback<ArrayList<YuYueInfo>> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userid", userid);
        paramsMap.put("docterid", docterid);
        NetworkManager.getInstance().postResultClass(YUYUE_URL_INFO, paramsMap, new TypeToken<ArrayList<YuYueInfo>>() {
        }.getType(), listener, failedCallback);
    }


    public static void YuYue_cancel(String rid, NetworkManager.SuccessCallback<String> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("rid", rid);
        NetworkManager.getInstance().postResultClass(YUYUE_URL_CANCEL, paramsMap, String.class, listener, failedCallback);
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
    public static void appraise(String docterid, String bid, String content, int score, String time, int doid,
                                NetworkManager.SuccessCallback<String> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();

        paramsMap.put("docterid", docterid + "");
        paramsMap.put("bid", bid + "");
        paramsMap.put("content", content);
        paramsMap.put("score", score + "");
        paramsMap.put("time", time);
        paramsMap.put("daid", String.valueOf(doid));

        NetworkManager.getInstance().postResultString(DOCTER_APPRAISER, paramsMap, listener, failedCallback);
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

    public static void goods_list(int state, NetworkManager.SuccessCallback<ArrayList<Goods>> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("state", state + "");

        NetworkManager.getInstance().postResultClass(GOODS_LIST, paramsMap, new TypeToken<ArrayList<Goods>>() {
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
        if (TextUtils.isEmpty(Box.getUserId())) {
            return;
        }
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("bid", Box.getUserId());
        NetworkManager.getInstance().getResultClass(ClueUrl, paramsMap, new TypeToken<ArrayList<ClueInfoBean>>() {
        }.getType(), callback);
    }

    public static void getYzList(NetworkManager.SuccessCallback<ArrayList<YzInfoBean>> callback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userid",Box.getUserId());
        NetworkManager.getInstance().getResultClass(GetYZUrl, paramsMap, new TypeToken<ArrayList<YzInfoBean>>() {
        }.getType(), callback);
    }



    public static void getVersionInfo(NetworkManager.SuccessCallback<VersionInfoBean> callback, NetworkManager.FailedCallback failedCallback) {
        ApplicationInfo appInfo = null;
        String msg = "";
        try {
            appInfo = Box.getApp().getPackageManager()
                    .getApplicationInfo(Box.getApp().getPackageName(), PackageManager.GET_META_DATA);
            msg = appInfo.metaData.getString("com.gcml.version");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("vname", msg);
        NetworkManager.getInstance().getResultClass(GetVersionUrl, paramsMap, VersionInfoBean.class, callback, failedCallback);
    }




    public static void charge(int minute, int doctorId, String bId,
                              NetworkManager.SuccessCallback<String> successCallback,
                              NetworkManager.FailedCallback failedCallback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("docterid", String.valueOf(doctorId));
        params.put("eqid", Utils.getDeviceId());
        params.put("time", String.valueOf(minute));
        params.put("bid", bId);
        NetworkManager.getInstance().postResultString(CHARGE_URL, params, successCallback, failedCallback);
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

    public static void findAccount(
            String cate,
            String account,
            NetworkManager.SuccessCallback<String> successCallback,
            NetworkManager.FailedCallback failedCallback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("cate", cate);
        params.put("account", account);
        NetworkManager.getInstance().postResultClass(FIND_ACCOUNT, params, Object.class, successCallback, failedCallback);
    }

    public static void setPassword(
            String account,
            String pwd,
            NetworkManager.SuccessCallback<String> successCallback,
            NetworkManager.FailedCallback failedCallback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("account", account);
        params.put("pwd", pwd);
        NetworkManager.getInstance().postResultClass(SET_PASSWORD, params, Object.class, successCallback, failedCallback);
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
     * 获得周生活报告
     *
     * @param successCallback
     * @param failedCallback
     */
    public static void getWeekReport(String userId, NetworkManager.SuccessCallback<WeekReportModel> successCallback,
                                     NetworkManager.FailedCallback failedCallback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("userid", userId);
        params.put("state", "1");
        NetworkManager.getInstance().getResultClass(Get_Week_or_Month_Report, params, WeekReportModel.class, successCallback, failedCallback);
    }

    /**
     * 或得月报告
     *
     * @param userId
     * @param successCallback
     * @param failedCallback
     */
    public static void getMonthReport(String userId, NetworkManager.SuccessCallback<MonthlyReport> successCallback,
                                      NetworkManager.FailedCallback failedCallback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("userid", userId);
        params.put("state", "2");
        NetworkManager.getInstance().getResultClass(Get_Week_or_Month_Report, params, MonthlyReport.class, successCallback, failedCallback);
    }
}
