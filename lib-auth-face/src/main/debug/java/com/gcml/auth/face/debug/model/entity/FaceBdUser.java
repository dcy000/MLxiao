package com.gcml.auth.face.debug.model.entity;

import com.google.gson.annotations.SerializedName;

public class FaceBdUser {
    /**
     * group_id : test1
     * user_id : u333333
     * user_info : Test User
     * score : 99.3
     */

    @SerializedName("group_id")
    private String groupId;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("user_info")
    private String userInfo;
    @SerializedName("score")
    private double score;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
