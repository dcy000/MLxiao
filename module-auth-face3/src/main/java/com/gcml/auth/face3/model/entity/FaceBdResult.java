package com.gcml.auth.face3.model.entity;

import com.google.gson.annotations.SerializedName;

public class FaceBdResult<T> {

    /**
     * timestamp : 1540453911
     * result : {"location":{"left":207.3682251,"height":326,"rotation":-1,"width":315,"top":246.5291443},"face_token":"65ad9cf447e54d91efe203ca9cd93c85"}
     * cached : 0
     * error_code : 0
     * log_id : 7520189552545
     * error_msg : SUCCESS
     */

    @SerializedName("timestamp")
    private int timestamp;
    @SerializedName("result")
    private T data;
    @SerializedName("cached")
    private int cached;
    @SerializedName("error_code")
    private int errorCode;  // 222202 : no face
    @SerializedName("log_id")
    private long logId;
    @SerializedName("error_msg")
    private String errorMsg;

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T result) {
        this.data = result;
    }

    public int getCached() {
        return cached;
    }

    public void setCached(int cached) {
        this.cached = cached;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return errorCode == 0;
    }
}
