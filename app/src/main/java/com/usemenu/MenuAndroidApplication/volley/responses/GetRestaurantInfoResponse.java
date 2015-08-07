package com.usemenu.MenuAndroidApplication.volley.responses;

public class GetRestaurantInfoResponse extends GsonResponse {

	public String restaurantname;
	public String imageurl;

	@Override
	public String toString() {
		return "GetRestaurantInfoResponse [restaurantname=" + restaurantname + ", imageurl=" + imageurl + ", errordata=" + errordata + ", errorlog=" + errorlog + "]";
	}

}
