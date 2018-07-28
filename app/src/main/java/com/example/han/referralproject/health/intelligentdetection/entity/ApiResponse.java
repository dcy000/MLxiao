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
}
