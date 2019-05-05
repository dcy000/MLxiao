package com.gcml.old;

import java.io.Serializable;

public class Orders implements Serializable{

	public String orderid;
	public String bname;
	public String articles;
	public String number;
	public String photo;
	public String price;
	public String time;
	public String address;
	public String tel;
	public String pay_state;
	public String delivery_state;
	public String display_state;
	
	
	public Orders() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Orders(String orderid, String bname, String articles, String number, String photo, String price, String time,
			String address, String tel, String pay_state, String delivery_state, String display_state) {
		super();
		this.orderid = orderid;
		this.bname = bname;
		this.articles = articles;
		this.number = number;
		this.photo = photo;
		this.price = price;
		this.time = time;
		this.address = address;
		this.tel = tel;
		this.pay_state = pay_state;
		this.delivery_state = delivery_state;
		this.display_state = display_state;
	}


	public String getOrderid() {
		return orderid;
	}


	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}


	public String getBname() {
		return bname;
	}


	public void setBname(String bname) {
		this.bname = bname;
	}


	public String getArticles() {
		return articles;
	}


	public void setArticles(String articles) {
		this.articles = articles;
	}


	public String getNumber() {
		return number;
	}


	public void setNumber(String number) {
		this.number = number;
	}


	public String getPhoto() {
		return photo;
	}


	public void setPhoto(String photo) {
		this.photo = photo;
	}


	public String getPrice() {
		return price;
	}


	public void setPrice(String price) {
		this.price = price;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getTel() {
		return tel;
	}


	public void setTel(String tel) {
		this.tel = tel;
	}


	public String getPay_state() {
		return pay_state;
	}


	public void setPay_state(String pay_state) {
		this.pay_state = pay_state;
	}


	public String getDelivery_state() {
		return delivery_state;
	}


	public void setDelivery_state(String delivery_state) {
		this.delivery_state = delivery_state;
	}


	public String getDisplay_state() {
		return display_state;
	}


	public void setDisplay_state(String display_state) {
		this.display_state = display_state;
	}


	@Override
	public String toString() {
		return "Orders [orderid=" + orderid + ", bname=" + bname + ", articles=" + articles + ", number=" + number
				+ ", photo=" + photo + ", price=" + price + ", time=" + time + ", address=" + address + ", tel=" + tel
				+ ", pay_state=" + pay_state + ", delivery_state=" + delivery_state + ", display_state=" + display_state
				+ "]";
	}
	
	
	
	
	

}
