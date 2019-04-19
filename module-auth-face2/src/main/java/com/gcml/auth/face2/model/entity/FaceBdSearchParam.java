package com.gcml.auth.face2.model.entity;

import com.google.gson.annotations.SerializedName;

public class FaceBdSearchParam {

    /**
     * image : {{imgData1}}
     * image_type : BASE64
     * group_id_list : {{groupId}}
     * user_id : qs
     * max_user_num : 1
     * quality_control : LOW
     * liveness_control : NONE
     */

    @SerializedName("image")
    private String image;
    @SerializedName("image_type")
    private String imageType;
    @SerializedName("group_id_list") // "group_repeat,group_233"
    private String groupIdList;
    @SerializedName("user_id")
    private String userId;           // 可选， 有 userId 就是认证识别
    @SerializedName("max_user_num")
    private int maxUserNum = 1;
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

    public String getGroupIdList() {
        return groupIdList;
    }

    public void setGroupIdList(String groupIdList) {
        this.groupIdList = groupIdList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getMaxUserNum() {
        return maxUserNum;
    }

    public void setMaxUserNum(int maxUserNum) {
        this.maxUserNum = maxUserNum;
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
