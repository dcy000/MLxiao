package com.zane.androidupnpdemo.net;

/**
 * Created by gzq on 2018/4/12.
 */

public class HYKD_API {
    public static final String DOMAIN_NAME = "http://61.161.70.27:9020/resident";
    public static final String RESIDENT_NAME="罗正碧";
    public static final String RESIDENT_IDCARD="510229193402151985";
    /**
     * 居民签约文件
     */
    public static final String RESIDENT_SIGN_FILE=DOMAIN_NAME+"/app/huayi/signFile.html";//正常
    /**
     * 我的签约基本信息
     */
    public static final String MY_SIGN_MESSAGE=DOMAIN_NAME+"/app/huayi/mySignInfo.html";//正常
    /**
     * 查询居民服务历史
     */
    public static final String RESIDENT_SERVER_HISTORY=DOMAIN_NAME+"/app/huayi/getServiceInfo.html";//正常
    /**
     * 获取居民服务详情
     */
    public static final String RESIDENT_SERVER_DETAILS=DOMAIN_NAME+"/app/huayi/getServiceDetails.html";//正常
    /**
     * 首页数据
     */
    public static final String HOME_INFORMATION=DOMAIN_NAME+"/app/huayi/getHomepageData.html";//正常
    /**
     * 获取居民信息
     */
    public static final String GET_RESIDENT_INFORMATION=DOMAIN_NAME+"/app/huayi/getResidentRecords.html";//正常
    /**
     * 签约时间团队及服务次数
     */
    public static final String SIGN_TEAM_WITH_SERVER=DOMAIN_NAME+"/app/huayi/getSignInfo.html";
}
