package com.gcml.common.user;

import com.google.gson.annotations.SerializedName;

/**
 * accountId : f3a08d96-d989-4490-8562-f9eb1d1341bb
 * account : 18868808297
 * cate : 3
 * userId : 130260
 * ip : 192.168.200.125
 * equipmentId : test
 * loginTime : 1537411580041
 * token : a17869ca-688e-4e04-a200-696d5044ea6b
 * refreshToken : 5aea73ec-c4e8-4389-8967-3de3e1b90f2b
 */
public class UserToken {
    @SerializedName("accountId")
    private String accountId;
    @SerializedName("account")
    private String account;
    @SerializedName("cate")
    private String cate;
    @SerializedName("userId")
    private String userId;
    @SerializedName("ip")
    private String ip;
    @SerializedName("equipmentId")
    private String equipmentId;
    @SerializedName("loginTime")
    private String loginTime;
    @SerializedName("token")
    private String token;
    @SerializedName("refreshToken")
    private String refreshToken;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
