package com.example.han.referralproject.bean;

public class RobotContent {

	private String client_user_id;
	private String user_input;
	private String app_id;
	private String secret;
	
	public RobotContent() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RobotContent(String client_user_id, String user_input, String app_id, String secret) {
		super();
		this.client_user_id = client_user_id;
		this.user_input = user_input;
		this.app_id = app_id;
		this.secret = secret;
	}

	public String getClient_user_id() {
		return client_user_id;
	}

	public void setClient_user_id(String client_user_id) {
		this.client_user_id = client_user_id;
	}

	public String getUser_input() {
		return user_input;
	}

	public void setUser_input(String user_input) {
		this.user_input = user_input;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Override
	public String toString() {
		return "RobotContent [client_user_id=" + client_user_id + ", user_input=" + user_input + ", app_id=" + app_id
				+ ", secret=" + secret + "]";
	}

	
	
	

}
