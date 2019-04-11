package com.gcml.module_factory_test.bean;

/**
 * Created by afirez on 18-2-6.
 */

public class ApiException extends RuntimeException {

    private int code;

    public ApiException() {
    }

    public ApiException(String message) {
        super(message);
    }


    public ApiException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int code() {
        return code;
    }
}
