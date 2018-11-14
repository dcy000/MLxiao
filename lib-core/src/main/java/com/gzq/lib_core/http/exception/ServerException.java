package com.gzq.lib_core.http.exception;


public class ServerException extends RuntimeException{
    // 异常处理，为速度，不必要设置getter和setter
    public int code;
    public String message;

    public ServerException(String message, int code) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
