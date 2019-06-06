package com.gcml.auth.face3.model.entity;

import com.google.gson.annotations.SerializedName;

public class FaceBdVerifyParam {

    /**
     * image : {{imgData1}}
     * image_type : BASE64
     * face_field : age,beauty,expression
     */

    @SerializedName("image")
    private String image;
    @SerializedName("image_type")
    private String imageType;
    @SerializedName("face_field")
    private String faceField;

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
}
