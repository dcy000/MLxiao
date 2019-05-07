package com.gcml.common.recommend.bean.get;

public class RobotAmount {
	
	public String amount;
	public int count;
	
	public RobotAmount() {
		super();
		// TODO Auto-generated constructor stub
	}



	public RobotAmount(String amount) {
		super();
		this.amount = amount;
	}



	public String getAmount() {
		return amount;
	}



	public void setAmount(String amount) {
		this.amount = amount;
	}



	@Override
	public String toString() {
		return "RobotAmount [amount=" + amount + "]";
	}
	
	
	



	
	

}
