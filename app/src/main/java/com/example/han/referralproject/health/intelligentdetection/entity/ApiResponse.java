package com.example.han.referralproject.health.intelligentdetection.entity;

import com.google.gson.annotations.SerializedName;

public class ApiResponse<T> {
    @SerializedName("tag")
    private boolean successful;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private T data;

    public boolean isSuccessful() {
        return successful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "successful=" + successful +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
