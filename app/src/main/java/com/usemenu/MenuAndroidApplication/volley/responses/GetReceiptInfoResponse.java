package com.usemenu.MenuAndroidApplication.volley.responses;

import com.usemenu.MenuAndroidApplication.dataclasses.Item;

public class GetReceiptInfoResponse extends GsonResponse {

	public float orderprice;
	public float discount;
	public float tip;
	public float tax;
	public String restaurantname;
	public String tablenumber;
	public long ordernumber;
	public float taxrate;
	public float discountrate;
	public float tiprate;
	public String date;
	public String time;
	public Item[] items;
	public String currency;

}
