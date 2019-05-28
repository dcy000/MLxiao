package com.gcml.auth.face3.model.entity;

import com.google.gson.annotations.SerializedName;

public class FaceUser {

    /**
     * bid : 0
     * faceId : string
     * groupId : string
     * imageUrl : string
     */

    @SerializedName("bid")
    private String userId;
    @SerializedName("faceId")
    private String faceId;
    @SerializedName("groupId")
    private String groupId;
    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("imageKey")
    private String imageKey;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }
}
