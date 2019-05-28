package com.gcml.auth.face3.model.entity;

import com.google.gson.annotations.SerializedName;

public class FaceBdAddFaceParam {

    /**
     * image : {{imgData1}}
     * image_type : BASE64
     * group_id : {{groupId}}
     * user_id : {{userId}}
     * user_info : sss
     * quality_control : LOW
     * liveness_control : NONE
     */

    @SerializedName("image")
    private String image;
    @SerializedName("image_type")
    private String imageType;
    @SerializedName("group_id")
    private String groupId;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("user_info")
    private String userInfo;
    @SerializedName("quality_control")
    private String qualityControl;
    @SerializedName("liveness_control")
    private String livenessControl;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

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

    public String getQualityControl() {
        return qualityControl;
    }

    public void setQualityControl(String qualityControl) {
        this.qualityControl = qualityControl;
    }

    public String getLivenessControl() {
        return livenessControl;
    }

    public void setLivenessControl(String livenessControl) {
        this.livenessControl = livenessControl;
    }
}
