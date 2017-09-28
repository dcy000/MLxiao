package com.example.han.referralproject.network;

import android.text.TextUtils;

import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.util.Utils;

import java.util.HashMap;
import java.util.Map;

public class NetworkApi {
        public static final String BasicUrl = "http://192.168.200.108:8080";
//    public static final String BasicUrl = "http://116.62.36.12:8080";
    public static final String LoginUrl = BasicUrl + "/ZZB/login/applogin";
    public static final String RegisterUrl = BasicUrl + "/ZZB/br/appadd";
    public static final String AddMhUrl = BasicUrl + "/ZZB/br/mhrecord";
    public static final String BindDocUrl = BasicUrl + "/ZZB/br/qianyue";
    public static final String UploadDataUrl = BasicUrl + "/ZZB/bl/doaddbl";
    public static final String CHARGE_URL = BasicUrl + "/ZZB/eq/koufei";

    public static void login(String phoneNum, String pwd, NetworkManager.SuccessCallback<UserInfoBean> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("username", phoneNum);
        paramsMap.put("password", pwd);
        NetworkManager.getInstance().postResultClass(LoginUrl, paramsMap, UserInfoBean.class, listener, failedCallback);
    }

    public static void registerUser(String name, String sex, String address, String telephone, String pwd,
                                    NetworkManager.SuccessCallback<UserInfoBean> listener, NetworkManager.FailedCallback failedCallback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("bname", name);
        paramsMap.put("age", "50");
        paramsMap.put("eqid", Utils.getDeviceId());
        paramsMap.put("tel", telephone);
        paramsMap.put("pwd", pwd);
        paramsMap.put("dz", address);
        paramsMap.put("sex", sex);
        NetworkManager.getInstance().postResultClass(RegisterUrl, paramsMap, UserInfoBean.class, listener, failedCallback);
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

    public static void bindDoctor(int doctorId, NetworkManager.SuccessCallback<String> callback) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("docterid", String.valueOf(doctorId));
        paramsMap.put("eqid", Utils.getDeviceId());
        NetworkManager.getInstance().postResultString(BindDocUrl, paramsMap, callback);
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

//    public static final String HomePageUrl = BasicUrl + "/appIndex";
//    public static final String UserInfoUrl = BasicUrl + "/centerInfo";
//    public static final String FavoriteSaveUrl = BasicUrl + "/user/favoriteSave";
//    public static final String FavoriteDeleteUrl = BasicUrl + "/user/favoriteDelete";
//    public static final String GetFavoriteListUrl = BasicUrl + "/user/favoriteList";
//    public static final String GetTraceListUrl = BasicUrl + "/user/traceList";
//    public static final String TraceSaveUrl = BasicUrl + "/user/traceSave";
//    public static final String TraceDeleteUrl = BasicUrl + "/user/traceDelete";

//    public static void loginWithOpenId(String openId, String type, String nickName, String imageUrl,
//                                       NetworkManager.SuccessCallback<LoginInfoBean> listener, NetworkManager.FailedCallback failedCallback){
//        Map<String, String> paramsMap = new HashMap<>();
//        paramsMap.put("nick", nickName);
//        paramsMap.put("portrait", imageUrl);
//        paramsMap.put("sign", Utils.md5HexDigest("#2017" + openId + "dD78hy2!" + nickName + "uK23lp"));
//        paramsMap.put("type", type);
//        paramsMap.put("userName", openId);
//        NetworkManager.getInstance().postResultClass(LoginWithOpenIdUrl, paramsMap, LoginInfoBean.class, listener, failedCallback);
//    }
//
//    public static void getHomePage(NetworkManager.SuccessCallback<HomePageBean> listener, NetworkManager.FailedCallback failedCallback){
//        NetworkManager.getInstance().getResultClass(HomePageUrl, HomePageBean.class, listener);
//    }
//
//    public static void getUserInfo(NetworkManager.SuccessCallback<UserPageBean> listener){
//        NetworkManager.getInstance().getResultClass(UserInfoUrl, UserPageBean.class, listener);
//    }
//
//    public static void favoriteOperation(long imageId, boolean isLike){
//        Map<String, String> paramsMap = new HashMap<>();
//        paramsMap.put("targetId", String.valueOf(imageId));
//        NetworkManager.getInstance().postResultString(isLike ? FavoriteSaveUrl : FavoriteDeleteUrl, paramsMap, null);
//    }
//
//    public static void getFavoriteList(NetworkManager.SuccessCallback<FavoriteListBean> listener){
//        Map<String, String> paramsMap = new HashMap<>();
//        paramsMap.put("pageSize", "1000");
//        NetworkManager.getInstance().getResultClass(GetFavoriteListUrl, paramsMap, FavoriteListBean.class, listener);
//    }
//
//    public static void getTraceList(NetworkManager.SuccessCallback<TraceListBean> listener){
//        Map<String, String> paramsMap = new HashMap<>();
//        paramsMap.put("pageSize", "1000");
//        NetworkManager.getInstance().getResultClass(GetTraceListUrl, paramsMap, TraceListBean.class, listener);
//    }

//    public static void traceAdd(long imageId){
//        Map<String, String> paramsMap = new HashMap<>();
//        paramsMap.put("targetId", String.valueOf(imageId));
//        NetworkManager.getInstance().postResultString(TraceSaveUrl, paramsMap, null);
//    }
//
//    public static void cleanTrace(){
//        NetworkManager.getInstance().postResultString(TraceDeleteUrl, null, null);
//    }
}
