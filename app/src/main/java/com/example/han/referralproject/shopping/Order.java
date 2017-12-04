package com.example.han.referralproject.shopping;

public class Order {

	public String orderid;

	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Order(String orderid) {
		super();
		this.orderid = orderid;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	@Override
	public String toString() {
		return "Order [orderid=" + orderid + "]";
	}
	
	

}
