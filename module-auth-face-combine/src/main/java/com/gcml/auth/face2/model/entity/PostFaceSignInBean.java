package com.gcml.auth.face2.model.entity;

public class PostFaceSignInBean {

    /**
     * category :
     * faceId :
     * groupId :
     */

    private String category = "3";
    private String faceId;
    private String groupId;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
}
