package com.example.han.referralproject.network;

import android.text.TextUtils;

import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.AllDoctor;
import com.example.han.referralproject.bean.AlreadyYuyue;
import com.example.han.referralproject.bean.BUA;
import com.example.han.referralproject.bean.BloodOxygenHistory;
import com.example.han.referralproject.bean.BloodPressureHistory;
import com.example.han.referralproject.bean.BloodSugarHistory;
import com.example.han.referralproject.bean.CholesterolHistory;
import com.example.han.referralproject.bean.ClueInfoBean;
import com.example.han.referralproject.bean.ContractInfo;
import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.bean.DiseaseResult;
import com.example.han.referralproject.bean.Doctor;
import com.example.han.referralproject.bean.ECGHistory;
import com.example.han.referralproject.bean.HeartRateHistory;
import com.example.han.referralproject.bean.PulseHistory;
import com.example.han.referralproject.bean.RobotAmount;
import com.example.han.referralproject.bean.SymptomBean;
import com.example.han.referralproject.bean.SymptomResultBean;
import com.example.han.referralproject.bean.TemperatureHistory;
import com.example.han.referralproject.bean.UserInfo;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.bean.VersionInfoBean;
import com.example.han.referralproject.bean.WeightHistory;
import com.example.han.referralproject.bean.YuYueInfo;
import com.example.han.referralproject.bean.YzInfoBean;
import com.example.han.referralproject.recyclerview.Docter;
import com.example.han.referralproject.recyclerview.OnlineTime;
import com.example.han.referralproject.shopping.Goods;
import com.example.han.referralproject.shopping.Order;
import com.example.han.referralproject.shopping.Orders;
import com.example.han.referralproject.util.Utils;
import com.example.han.referralproject.video.VideoEntity;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkApi {


    public static final String BasicUrl = "http://116.62.36.12:8080";
//        public static final String BasicUrl = "http://116.62.36.12:8080";
//    public static final String BasicUrl = "http://118.31.238.207:8080";

//    public static final String BasicUrl = "http://192.168.200.119:8080";//孙高峰
//    public static final String BasicUrl="http://192.168.200.116:8080";//韩琦本地
//    public static final String BasicUrl = "http://192.168.200.114:8080";//文博本地

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
    public static final String GET_CODE = BasicUrl + "/ZZB/docter/GainCode";

    public static void getCode(
            String phone,
            NetworkManager.SuccessCallback<String> successCallback,
            NetworkManager.FailedCallback failedCallback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        NetworkManager.getInstance().getResultString(GET_CODE, params, successCallback, failedCallback);
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


    public static void checkNotContract(String bid, NetworkManager.SuccessCallback<Object> successCallback, NetworkManager.FailedCallback failedCallback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("bid", bid);
        NetworkManager.getInstance().postResultClass(CHECK_CONTRACT, params, Object.class, successCallback, failedCallback);
    }

    public static void getEqPreAmount(NetworkManager.SuccessCallback<RobotAmount> successCallback, NetworkManager.FailedCallback failedCallback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("eqid", Utils.getDeviceId());
        NetworkManager.getInstance().postResultClass(EQ_PRE_AMOUNT, params, RobotAmount.class, successCallback, failedCallback);
    }

    public static void canRegister(
            String phone,
            String state,
            NetworkManager.SuccessCallback<Object> successCallback,
            NetworkManager.FailedCallback failedCallback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("tel", phone);
        params.put("state", state);
        NetworkManager.getInstance().postResultClass(IS_PHONE_REGISTERED, params, Object.class, successCallback, failedCallback);
    }


    public static final String GOODS_LIST = BasicUrl + "/ZZB/order/OneType_state";


    public static void login(String phoneNum, String pwd, NetworkManager.SuccessCallback<UserInfoBean> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("username", phoneNum);
        paramsMap.put("password", pwd);
        NetworkManager.getInstance().postResultClass(LoginUrl, paramsMap, UserInfoBean.class, listener, failedCallback);
    }


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


    public static void YuYue_history(String userid, String docterid, int state, int page, int limit, NetworkManager.SuccessCallback<ArrayList<YuYueInfo>> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userid", userid);
        paramsMap.put("docterid", docterid);
        paramsMap.put("state", state + "");
        paramsMap.put("page", page + "");
        paramsMap.put("limit", limit + "");

        NetworkManager.getInstance().postResultClass(YUYUE_URL_HISTORY, paramsMap, new TypeToken<ArrayList<YuYueInfo>>() {
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


    public static void get_token(NetworkManager.SuccessCallback<String> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();

        NetworkManager.getInstance().postResultString(TOKEN_URL, paramsMap, listener, failedCallback);
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

    public static void insertGoods(String goodsname, String goodsimage, double goodsprice, int goodstate, NetworkManager.SuccessCallback<String> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();

        paramsMap.put("goodsname", goodsname);
        paramsMap.put("goodsimage", goodsimage);
        paramsMap.put("goodsprice", goodsprice + "");
        paramsMap.put("goodstate", goodstate + "");

        NetworkManager.getInstance().postResultString(GOODS_INSERT, paramsMap, listener, failedCallback);
    }


    public static void return_imageUrl(String user_photo, String bid, String xfid, NetworkManager.SuccessCallback<Object> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("user_photo", user_photo);
        paramsMap.put("bid", bid);
        paramsMap.put("xfid", xfid);
        NetworkManager.getInstance().postResultString(RETURN_IMAGE_URL, paramsMap, listener, failedCallback);
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


    public static void order_info(String userid, String eqid, String articles, String number, String price, String photo, String time, NetworkManager.SuccessCallback<Order> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userid", userid);
        paramsMap.put("eqid", eqid);
        paramsMap.put("articles", articles);
        paramsMap.put("number", number);
        paramsMap.put("price", price);
        paramsMap.put("photo", photo);
        paramsMap.put("time", time);

        NetworkManager.getInstance().postResultClass(ORDER_INFO, paramsMap, Order.class, listener, failedCallback);
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


    public static void registerUser(
            String name,
            String sex,
            String address,
            String sfz,
            String telephone,
            String pwd,
            float height,
            float weight,
            String bloodType,
            String eat,
            String smoke,
            String drink,
            String sports,
            NetworkManager.SuccessCallback<UserInfoBean> listener,
            NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("age", "50");
        paramsMap.put("bname", name);
        paramsMap.put("sex", sex);
        paramsMap.put("eqid", Utils.getDeviceId());
        paramsMap.put("tel", telephone);
        paramsMap.put("pwd", pwd);
        paramsMap.put("dz", address);
        paramsMap.put("sfz", sfz);
        paramsMap.put("height", String.valueOf(height));
        paramsMap.put("weight", String.valueOf(weight));
        paramsMap.put("blood_type", bloodType);
        paramsMap.put("eating_habits", eat);
        paramsMap.put("smoke", smoke);
        paramsMap.put("drink", drink);
        paramsMap.put("exercise_habits", sports);
        NetworkManager.getInstance().postResultClass(RegisterUrl, paramsMap, UserInfoBean.class, listener, failedCallback);
    }

    /**
     * 获取所有症状
     *
     * @param callback
     */
    public static void getAllSym(NetworkManager.SuccessCallback<ArrayList<SymptomBean>> callback) {
        NetworkManager.getInstance().getResultClass(GetAllSymUrl, null, new TypeToken<ArrayList<SymptomBean>>() {
        }.getType(), callback);
    }

    public static void getAllUsers(String accounts, NetworkManager.SuccessCallback<ArrayList<UserInfoBean>> callback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("p", accounts);
        NetworkManager.getInstance().getResultClass(GetInfo_URL, paramsMap, new TypeToken<ArrayList<UserInfoBean>>() {
        }.getType(), callback);
    }

    /**
     * 获取下一层病症和结果
     *
     * @param params
     * @param callback
     */
    public static void analyseSym(String params, NetworkManager.SuccessCallback<SymptomResultBean> callback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("parameter", params);
        NetworkManager.getInstance().getResultClass(AnalyseUrl, paramsMap, SymptomResultBean.class, callback);
    }

    public static void clueNotify(NetworkManager.SuccessCallback<ArrayList<ClueInfoBean>> callback) {
        if (TextUtils.isEmpty(MyApplication.getInstance().userId)) {
            return;
        }
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("bid", MyApplication.getInstance().userId);
        NetworkManager.getInstance().getResultClass(ClueUrl, paramsMap, new TypeToken<ArrayList<ClueInfoBean>>() {
        }.getType(), callback);
    }

    public static void getYzList(NetworkManager.SuccessCallback<ArrayList<YzInfoBean>> callback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userid", MyApplication.getInstance().userId);
        NetworkManager.getInstance().getResultClass(GetYZUrl, paramsMap, new TypeToken<ArrayList<YzInfoBean>>() {
        }.getType(), callback);
    }

    public static void setUserMh(String mh, NetworkManager.SuccessCallback<String> callback, NetworkManager.FailedCallback failedCallback) {
        if (TextUtils.isEmpty(MyApplication.getInstance().userId)) {
            return;
        }
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("bid", MyApplication.getInstance().userId);
        paramsMap.put("mh", mh);
        NetworkManager.getInstance().postResultString(AddMhUrl, paramsMap, callback, failedCallback);
    }

    public static void bindDoctor(String bid, int doctorId, NetworkManager.SuccessCallback<String> callback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("bid", bid);
        paramsMap.put("doid", String.valueOf(doctorId));
        NetworkManager.getInstance().postResultString(BindDocUrl, paramsMap, callback);
    }

    public static void getVersionInfo(NetworkManager.SuccessCallback<VersionInfoBean> callback, NetworkManager.FailedCallback failedCallback) {
        NetworkManager.getInstance().getResultClass(GetVersionUrl, null, VersionInfoBean.class, callback, failedCallback);
    }

    public static void postData(DataInfoBean info, NetworkManager.SuccessCallback<String> successCallback) {
        if (info == null) {
            return;
        }
        NetworkManager.getInstance().postResultString(UploadDataUrl, info.getParamsMap(), successCallback);
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

    public static void getContractInfo(
            String doctorId,
            NetworkManager.SuccessCallback<ContractInfo> successCallback,
            NetworkManager.FailedCallback failedCallback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("userid", MyApplication.getInstance().userId);
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
        params.put("bid", MyApplication.getInstance().userId);
        NetworkManager.getInstance().getResultClass(GET_MY_BASE_DATA, params, UserInfoBean.class, successCallback, failedCallback);
    }

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

    /**
     * 获取所有医生
     *
     * @param successCallback
     */
    public static void getAllDoctor(String doctorname, String start, String limit, NetworkManager.SuccessCallback<ArrayList<AllDoctor>> successCallback
    ) {
        HashMap<String, String> params = new HashMap<>();
        if (null != doctorname) {
            params.put("doctorname", doctorname);
        }
        params.put("start", start);
        params.put("limit", limit);
        NetworkManager.getInstance().getResultClass(Get_AllDotor, params, new TypeToken<ArrayList<AllDoctor>>() {
                }.getType(),
                successCallback);
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
     * 修改个人基本信息
     *
     * @param bid
     * @param height
     * @param weight
     * @param eating_habits
     * @param smoke
     * @param drink
     * @param exercise_habits
     * @param successCallback
     * @param failedCallback
     */
    public static void alertBasedata(String bid, String height, String weight, String eating_habits, String smoke, String drink, String exercise_habits, String mh, String dz,
                                     NetworkManager.SuccessCallback<Object> successCallback,
                                     NetworkManager.FailedCallback failedCallback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("bid", bid);
        params.put("height", height);
        params.put("weight", weight);
        params.put("eating_habits", eating_habits);
        params.put("smoke", smoke);
        params.put("drink", drink);
        params.put("exercise_habits", exercise_habits);
        params.put("mh", mh);
        params.put("dz", dz);
        NetworkManager.getInstance().postResultClass(Alert_Basedata, params, Object.class, successCallback, failedCallback);
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
}
