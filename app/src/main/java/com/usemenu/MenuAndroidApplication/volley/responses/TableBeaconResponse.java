package com.usemenu.MenuAndroidApplication.volley.responses;

public class TableBeaconResponse extends GsonResponse {

	public int rssi;
	public int localRssi = 64;

	public boolean isInRange() {
		return localRssi >= rssi;
	}
}
