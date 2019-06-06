package com.gcml.auth.face2.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FaceBdVerify {


    /**
     * face_liveness : 0.3870637451
     * thresholds : {"frr_1e-2":0.9,"frr_1e-3":0.3,"frr_1e-4":0.05}
     * face_list : [{"expression":{"type":"none","probability":0.9999980927},"face_probability":1,"location":{"left":261.6818848,"height":326,"rotation":2,"width":305,"top":197.5403595},"age":23,"face_token":"fef33454b6d467966a8231b0b629f5c6","beauty":62.88520813,"angle":{"pitch":-1.240876436,"yaw":0.2984000444,"roll":2.373161793},"liveness":{"livemapscore":0.08331970848}},{"expression":{"type":"none","probability":0.9999988079},"face_probability":1,"location":{"left":237.3197937,"height":315,"rotation":1,"width":293,"top":203.805542},"age":22,"face_token":"783b98e2ec9964f3de42b411d99f915b","beauty":64.37795258,"angle":{"pitch":-0.5715998411,"yaw":-1.538130641,"roll":1.552309632},"liveness":{"livemapscore":0.3870637451}},{"expression":{"type":"none","probability":0.9999982119},"face_probability":1,"location":{"left":221.2050323,"height":310,"rotation":1,"width":289,"top":204.1291351},"age":23,"face_token":"bf96d2ac3836c3fc0b34d27b95120a43","beauty":62.44766998,"angle":{"pitch":-0.8590278625,"yaw":-1.099190235,"roll":1.527231216},"liveness":{"livemapscore":0.3787647819}}]
     */

    @SerializedName("face_liveness")
    private double faceLiveness;
    @SerializedName("thresholds")
    private FaceBdThresholds thresholds;
    @SerializedName("face_list")
    private List<FaceBdFaceInfo> faceList;

    public double getFaceLiveness() {
        return faceLiveness;
    }

    public void setFaceLiveness(double faceLiveness) {
        this.faceLiveness = faceLiveness;
    }

    public FaceBdThresholds getThresholds() {
        return thresholds;
    }

    public void setThresholds(FaceBdThresholds thresholds) {
        this.thresholds = thresholds;
    }

    public List<FaceBdFaceInfo> getFaceList() {
        return faceList;
    }

    public void setFaceList(List<FaceBdFaceInfo> faceList) {
        this.faceList = faceList;
    }

    public String image() {
        for (FaceBdFaceInfo info : faceList) {
            if (Math.abs(faceLiveness - info.getLiveness().getLivemapscore()) < 0.001) {
                return "FACE_TOKEN," + info.getFaceToken();
            }
        }
        return "";
    }
}
