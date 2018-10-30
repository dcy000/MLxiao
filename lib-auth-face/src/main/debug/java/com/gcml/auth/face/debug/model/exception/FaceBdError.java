package com.gcml.auth.face.debug.model.exception;

import com.gcml.auth.face.debug.model.FaceBdErrorUtils;

public class FaceBdError extends RuntimeException {
    private int code = FaceBdErrorUtils.ERROR_UNKNOWN;

    public FaceBdError(Throwable throwable) {
        super(throwable);
    }

    public FaceBdError(int code, String message) {
        super(message);
        this.code = code;
    }

    public FaceBdError(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
