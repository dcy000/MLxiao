package com.gcml.auth.face.debug.model.exception;

public class FaceBdError extends RuntimeException {
    private int code = -1;

    public FaceBdError(Throwable throwable) {
        super("服务器繁忙", throwable);
    }

    public FaceBdError(int code, String message) {
        super(message);
        this.code = code;
    }

    public FaceBdError(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
