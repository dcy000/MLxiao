package com.gcml.module_factory_test.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by afirez on 18-2-6.
 */

public class ApiResult<T> {
    @SerializedName("tag")
    private boolean successful;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private T data;

    public boolean isSuccessful() {
        return successful || code == 200;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "successful=" + successful +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
