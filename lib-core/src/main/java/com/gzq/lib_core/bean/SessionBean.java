package com.gzq.lib_core.bean;

public class SessionBean {


    /**
     * loginTime : 1542184402773
     * accountId : a60802f5-ffa5-4ab5-ac90-18bc88d8a75a
     * cate : 3
     * token : e378add0-9c47-458a-bbd8-87ed571ca321
     * userId : 130338
     * equipmentId : 861622010000056
     * account : 15181438908
     * refreshToken : e4190560-f2d7-4182-8387-5766f1999c27
     * ip : 60.177.17.253
     */

    private long loginTime;
    private String accountId;
    private int cate;
    private String token;
    private int userId;
    private String equipmentId;
    private String account;
    private String refreshToken;
    private String ip;

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public int getCate() {
        return cate;
    }

    public void setCate(int cate) {
        this.cate = cate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
