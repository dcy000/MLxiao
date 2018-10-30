package com.gcml.auth.face.debug.model.entity;

import com.google.gson.annotations.SerializedName;

public class FaceBdFaceInfo {
    /**
     * expression : {"type":"none","probability":0.9999980927}
     * face_probability : 1
     * location : {"left":261.6818848,"height":326,"rotation":2,"width":305,"top":197.5403595}
     * age : 23
     * face_token : fef33454b6d467966a8231b0b629f5c6
     * beauty : 62.88520813
     * angle : {"pitch":-1.240876436,"yaw":0.2984000444,"roll":2.373161793}
     * liveness : {"livemapscore":0.08331970848}
     */

    @SerializedName("expression")
    private FaceBdExpression expression;
    @SerializedName("face_probability")
    private double faceProbability;
    @SerializedName("location")
    private FaceBdLocation location;
    @SerializedName("age")
    private int age;
    @SerializedName("face_token")
    private String faceToken;
    @SerializedName("beauty")
    private double beauty;
    @SerializedName("angle")
    private FaceBdAngle angle;
    @SerializedName("liveness")
    private FaceBdLiveness liveness;

    public FaceBdExpression getExpression() {
        return expression;
    }

    public void setExpression(FaceBdExpression expression) {
        this.expression = expression;
    }

    public double getFaceProbability() {
        return faceProbability;
    }

    public void setFaceProbability(double faceProbability) {
        this.faceProbability = faceProbability;
    }

    public FaceBdLocation getLocation() {
        return location;
    }

    public void setLocation(FaceBdLocation location) {
        this.location = location;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFaceToken() {
        return faceToken;
    }

    public void setFaceToken(String faceToken) {
        this.faceToken = faceToken;
    }

    public double getBeauty() {
        return beauty;
    }

    public void setBeauty(double beauty) {
        this.beauty = beauty;
    }

    public FaceBdAngle getAngle() {
        return angle;
    }

    public void setAngle(FaceBdAngle angle) {
        this.angle = angle;
    }

    public FaceBdLiveness getLiveness() {
        return liveness;
    }

    public void setLiveness(FaceBdLiveness liveness) {
        this.liveness = liveness;
    }
}
