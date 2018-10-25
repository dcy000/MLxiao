package com.gcml.auth.face.debug.model.entity;

import com.google.gson.annotations.SerializedName;

public class FaceBdDetectFace {

    /**
     * image : {{imgData1}}
     * image_type : BASE64
     * face_field : faceshape,facetype
     * max_face_num : 1
     * face_type : LIVE
     */

    @SerializedName("image")
    private String image;
    @SerializedName("image_type")
    private String imageType;
    @SerializedName("face_field")
    private String faceField;
    @SerializedName("max_face_num")
    private int maxFaceNum;
    @SerializedName("face_type")
    private String faceType;

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

    public String getFaceField() {
        return faceField;
    }

    public void setFaceField(String faceField) {
        this.faceField = faceField;
    }

    public int getMaxFaceNum() {
        return maxFaceNum;
    }

    public void setMaxFaceNum(int maxFaceNum) {
        this.maxFaceNum = maxFaceNum;
    }

    public String getFaceType() {
        return faceType;
    }

    public void setFaceType(String faceType) {
        this.faceType = faceType;
    }
}
