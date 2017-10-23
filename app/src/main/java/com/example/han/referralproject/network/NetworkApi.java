package com.example.han.referralproject.network;

import android.text.TextUtils;

import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.bean.Doctors;
import com.example.han.referralproject.bean.SymptomBean;
import com.example.han.referralproject.bean.SymptomResultBean;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.bean.VersionInfoBean;
import com.example.han.referralproject.bean.YzInfoBean;
import com.example.han.referralproject.util.Utils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NetworkApi {
    public static final String BasicUrl = "http://192.168.200.103:8080";
//        public static final String BasicUrl = "http://116.62.36.12:8080";
    public static final String LoginUrl = BasicUrl + "/ZZB/login/applogin";
    public static final String RegisterUrl = BasicUrl + "/ZZB/br/appadd";
    public static final String AddMhUrl = BasicUrl + "/ZZB/br/mhrecord";
    public static final String BindDocUrl = BasicUrl + "/ZZB/br/qianyue";
    public static final String GetAllSymUrl = BasicUrl + "/ZZB/bl/selAllSym";
    public static final String AnalyseUrl = BasicUrl + "/ZZB/bl/selcon";
    public static final String GetYZUrl = BasicUrl + "/ZZB/bl/selYzAndTime";
    public static final String GetVersionUrl = BasicUrl + "/ZZB/vc/selone";
    public static final String UploadDataUrl = BasicUrl + "/ZZB/bl/doaddbl";
    public static final String CHARGE_URL = BasicUrl + "/ZZB/eq/koufei";
    public static final String PAY_URL = BasicUrl + "/ZZB/br/chongzhi";
    public static final String DOCTOR_URL = BasicUrl + "/ZZB/docter/search_OneDocter";


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
        NetworkManager.getInstance().postResultClass(PAY_URL, paramsMap, UserInfoBean.class, listener, failedCallback);
    }

    public static void DoctorInfo(String bid, NetworkManager.SuccessCallback<Doctors> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("bid", bid);

        NetworkManager.getInstance().postResultClass(DOCTOR_URL, paramsMap, Doctors.class, listener, failedCallback);
    }


    public static void registerUser(String name, String sex, String address, String telephone, String pwd, String sfz, NetworkManager.SuccessCallback<UserInfoBean> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("bname", name);
        paramsMap.put("age", "50");
        paramsMap.put("eqid", Utils.getDeviceId());
        paramsMap.put("tel", telephone);
        paramsMap.put("pwd", pwd);
        paramsMap.put("dz", address);
        paramsMap.put("sex", sex);
        paramsMap.put("sfz", sfz);
        NetworkManager.getInstance().postResultClass(RegisterUrl, paramsMap, UserInfoBean.class, listener, failedCallback);
    }

    public static void getAllSym(NetworkManager.SuccessCallback<ArrayList<SymptomBean>> callback) {
        NetworkManager.getInstance().getResultClass(GetAllSymUrl, null, new TypeToken<ArrayList<SymptomBean>>() {
        }.getType(), callback);
    }

    public static void analyseSym(String params, NetworkManager.SuccessCallback<ArrayList<SymptomResultBean>> callback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("parameter", params);
        NetworkManager.getInstance().getResultClass(AnalyseUrl, paramsMap, new TypeToken<ArrayList<SymptomResultBean>>() {
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


    public static void charge(int minute, NetworkManager.SuccessCallback<Object> successCallback, NetworkManager.FailedCallback failedCallback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("eqid", Utils.getDeviceId());
        params.put("time", String.valueOf(minute));
        NetworkManager.getInstance().postResultClass(CHARGE_URL, params, Object.class, successCallback, failedCallback);
    }


}
