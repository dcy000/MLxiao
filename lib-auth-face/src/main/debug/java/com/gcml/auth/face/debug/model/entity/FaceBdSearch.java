package com.gcml.auth.face.debug.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FaceBdSearch {

    /**
     * face_token : fid
     * user_list : [{"group_id":"test1","user_id":"u333333","user_info":"Test User","score":99.3}]
     */

    @SerializedName("face_token")
    private String faceToken;
    @SerializedName("user_list")
    private List<FaceBdUser> userList;

    public String getFaceToken() {
        return faceToken;
    }

    public void setFaceToken(String faceToken) {
        this.faceToken = faceToken;
    }

    public List<FaceBdUser> getUserList() {
        return userList;
    }

    public void setUserList(List<FaceBdUser> userList) {
        this.userList = userList;
    }

}
