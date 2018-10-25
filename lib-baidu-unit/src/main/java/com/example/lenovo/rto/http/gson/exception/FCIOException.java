package com.example.lenovo.rto.http.gson.exception;

import java.io.IOException;

public class FCIOException extends IOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6524805793687658590L;
	private Throwable cause;
	
	public FCIOException(){
		
	}
	
	public FCIOException(String paramString){
		super(paramString);
	}
	
	public FCIOException(String paramString,Throwable paramThrowable){
		this(paramString);
		cause = paramThrowable;
	}
	
	public FCIOException(Throwable paramThrowable){
		this("cause ="+ paramThrowable);
		cause = paramThrowable;
	}
	
	public Throwable getCause(){
		return cause;
	}
	
}
