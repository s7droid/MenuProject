package com.usemenu.MenuAndroidApplication.callbacks;

public interface OnIBeaconSearchResultCallback {

	public static final int SEARCH_RESULT_DEFAULT = 0;
	public static final int SEARCH_RESULT_STORE_BEACON_FOUND = 1;
	public static final int SEARCH_RESULT_TABLE_BEACON_FOUND = 2;
	public static final int SEARCH_RESULT_BEACON_NOT_FOUND = 3;

	public void onIBeaconSearchResult(int result);

}
