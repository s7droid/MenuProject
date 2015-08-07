package com.usemenu.MenuAndroidApplication.dataclasses;

import java.io.Serializable;

public class Receipt implements Serializable{


	private static final long serialVersionUID = 8549055767470018716L;
	
	public String date;
	public String restaurantname;
	public String amount;
	public String receiptid;
	public String currency;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRestaurantName() {
		return restaurantname;
	}
	public void setRestaurantName(String restaurantName) {
		this.restaurantname = restaurantName;
	}
	public String getAmmount() {
		return amount;
	}
	public void setAmmount(String ammount) {
		this.amount = ammount;
	}
	public String getReceiptId() {
		return receiptid;
	}
	public void setReceiptId(String receiptId) {
		this.receiptid = receiptId;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
}