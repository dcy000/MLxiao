package com.example.han.referralproject.bean;

public class User {
	private int id;
	private String user_name;
	private String user_pwd;
	private String user_phone;
	private String user_pic;

	public User() {
		super();
	}

	public User(int id, String user_name, String user_pwd, String user_phone, String user_pic) {
		super();
		this.id = id;
		this.user_name = user_name;
		this.user_pwd = user_pwd;
		this.user_phone = user_phone;
		this.user_pic = user_pic;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_pwd() {
		return user_pwd;
	}

	public void setUser_pwd(String user_pwd) {
		this.user_pwd = user_pwd;
	}

	public String getUser_phone() {
		return user_phone;
	}

	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}

	public String getUser_pic() {
		return user_pic;
	}

	public void setUser_pic(String user_pic) {
		this.user_pic = user_pic;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", user_name=" + user_name + ", user_pwd=" + user_pwd + ", user_phone=" + user_phone
				+ ", user_pic=" + user_pic + "]";
	}

}
