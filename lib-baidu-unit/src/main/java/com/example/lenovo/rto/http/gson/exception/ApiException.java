package com.example.lenovo.rto.http.gson.exception;

public class ApiException extends FCIOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3916949283237745775L;

	public ApiException(){
		
	}
	
	public ApiException(String paramString){
		super(paramString);
	}
	
	public ApiException(String paramString, Throwable paramThrowable){
		super(paramString, paramThrowable);
	}
	
	public ApiException(Throwable paramThrowable){
		super(paramThrowable);
	}
}
