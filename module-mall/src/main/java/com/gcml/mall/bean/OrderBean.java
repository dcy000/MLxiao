package com.gcml.mall.bean;

import java.io.Serializable;

public class OrderBean implements Serializable {

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

	public OrderBean(String orderid, String bname, String articles, String number, String photo, String price, String time,
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

	@Override
	public String toString() {
		return "Orders [orderid=" + orderid + ", bname=" + bname + ", articles=" + articles + ", number=" + number
				+ ", photo=" + photo + ", price=" + price + ", time=" + time + ", address=" + address + ", tel=" + tel
				+ ", pay_state=" + pay_state + ", delivery_state=" + delivery_state + ", display_state=" + display_state
				+ "]";
	}
}
