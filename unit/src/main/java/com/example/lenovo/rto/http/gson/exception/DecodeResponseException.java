package com.example.lenovo.rto.http.gson.exception;

public class DecodeResponseException extends ApiException {
	private static final long serialVersionUID = 3342398023064526738L;

	public DecodeResponseException() {
	}

	public DecodeResponseException(String paramString) {
		super(paramString);
	}

	public DecodeResponseException(String paramString, Throwable paramThrowable) {
		super(paramString, paramThrowable);
	}

	public DecodeResponseException(Throwable paramThrowable) {
		super(paramThrowable);
	}
}