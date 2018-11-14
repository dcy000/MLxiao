package com.gzq.lib_core.http.exception;

/**
 * 错误回调实体
 */
public class ErrorBody {
    private boolean tag;
    private  int code;
    private String message;
    private String error;
    public String getMessage() {
        return message;
    }
    public String getError(){
        return error;
    }
}
