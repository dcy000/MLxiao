package com.gcml.auth.face.model;

import com.google.gson.annotations.SerializedName;

public class FaceInfo {
    @SerializedName("groupId")
    public String groupId;
    @SerializedName("userId")
    public String userId;
    @SerializedName("xunfeiId")
    public String faceId;
}
