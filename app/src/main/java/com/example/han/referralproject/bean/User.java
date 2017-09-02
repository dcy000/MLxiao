package com.example.han.referralproject.bean;

public class User {
	private String bid;
	private String eqid;
	private String bname;
	private String sex;
	private String dz;
	private String age;
	private String sfz;
	private String tel;
	private String mh;

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(String bid, String eqid, String bname, String sex, String dz, String age, String sfz, String tel,
			String mh) {
		super();
		this.bid = bid;
		this.eqid = eqid;
		this.bname = bname;
		this.sex = sex;
		this.dz = dz;
		this.age = age;
		this.sfz = sfz;
		this.tel = tel;
		this.mh = mh;
	}
	
	
	

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getEqid() {
		return eqid;
	}

	public void setEqid(String eqid) {
		this.eqid = eqid;
	}

	public String getBname() {
		return bname;
	}

	public void setBname(String bname) {
		this.bname = bname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getDz() {
		return dz;
	}

	public void setDz(String dz) {
		this.dz = dz;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSfz() {
		return sfz;
	}

	public void setSfz(String sfz) {
		this.sfz = sfz;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getMh() {
		return mh;
	}

	public void setMh(String mh) {
		this.mh = mh;
	}

	@Override
	public String toString() {
		return "User [bid=" + bid + ", eqid=" + eqid + ", bname=" + bname + ", sex=" + sex + ", dz=" + dz + ", age="
				+ age + ", sfz=" + sfz + ", tel=" + tel + ", mh=" + mh + "]";
	}

	

	

}
