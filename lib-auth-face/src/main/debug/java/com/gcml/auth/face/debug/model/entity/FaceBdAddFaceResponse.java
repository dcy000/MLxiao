package com.gcml.auth.face.debug.model.entity;

import com.google.gson.annotations.SerializedName;

public class FaceBdAddFaceResponse {

    /**
     * face_token : 2fa64a88a9d5118916f9a303782a97d3
     * location : {"left":117,"top":131,"width":172,"height":170,"rotation":4}
     */

    @SerializedName("face_token")
    private String faceToken;
    @SerializedName("location")
    private FaceBdLocation location;

    public String getFaceToken() {
        return faceToken;
    }

    public void setFaceToken(String faceToken) {
        this.faceToken = faceToken;
    }

    public FaceBdLocation getLocation() {
        return location;
    }

    public void setLocation(FaceBdLocation location) {
        this.location = location;
    }

}
