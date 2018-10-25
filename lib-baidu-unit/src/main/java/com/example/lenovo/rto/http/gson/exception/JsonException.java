package com.example.lenovo.rto.http.gson.exception;

public class JsonException extends DecodeResponseException {
    private static final long serialVersionUID = -6550266490155489403L;

    public JsonException() {
    }

    public JsonException(String paramString) {
        super(paramString);
    }

    public JsonException(String paramString, Throwable paramThrowable) {
        super(paramString, paramThrowable);
    }

    public JsonException(Throwable paramThrowable) {
        super(paramThrowable);
    }
}