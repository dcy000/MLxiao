package com.example.han.referralproject.bean;

public class Receive1 {
	private String msg;
	private boolean success;
	private Receive data;
	
	
	
	public Receive1(String msg, boolean success, Receive data) {
		super();
		this.msg = msg;
		this.success = success;
		this.data = data;
	}



	public String getMsg() {
		return msg;
	}



	public void setMsg(String msg) {
		this.msg = msg;
	}



	public boolean getSuccess() {
		return success;
	}



	public void setSuccess(boolean success) {
		this.success = success;
	}



	public Receive getReceive() {
		return data;
	}



	public void setReceive(Receive data) {
		this.data = data;
	}



	@Override
	public String toString() {
		return "Receive1 [msg=" + msg + ", success=" + success + ", receive=" + data + "]";
	}
	
	
	
	
	

}
