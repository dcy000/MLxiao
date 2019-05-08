package com.example.han.referralproject.network;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.example.han.referralproject.BuildConfig;
import com.example.han.referralproject.application.MyApplication;
import com.medlink.danbogh.alarm.AlreadyYuyue;
import com.medlink.danbogh.alarm.ClueInfoBean;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.recommend.bean.get.Doctor;
import com.gcml.common.recommend.bean.get.RobotAmount;
import com.gcml.common.recommend.bean.get.VersionInfoBean;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NetworkApi {
    public static final String BasicUrl = BuildConfig.SERVER_ADDRESS;

    public static final String GetVersionUrl = BasicUrl + "ZZB/vc/selone";
    public static final String PAY_URL = BasicUrl + "ZZB/br/chongzhi";
    public static final String DOCTOR_URL = BasicUrl + "ZZB/docter/search_OneDocter";
    public static final String PERSON_URL = BasicUrl + "ZZB/br/selOneUser_con";
    public static final String PERSON_AMOUNT = BasicUrl + "ZZB/eq/eq_amount";



    public static void DoctorInfo(String bid, NetworkManager.SuccessCallback<Doctor> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("bid", bid);

        NetworkManager.getInstance().postResultClass(DOCTOR_URL, paramsMap, Doctor.class, listener, failedCallback);
    }

    public static void Person_Amount(String eqid, NetworkManager.SuccessCallback<RobotAmount> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("eqid", eqid);

        NetworkManager.getInstance().postResultClass(PERSON_AMOUNT, paramsMap, RobotAmount.class, listener, failedCallback);
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

}
