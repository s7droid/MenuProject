package com.usemenu.MenuAndroidApplication.volley;

public class Constants {

	public static final String PREFIX_UNSECURE = "http://";
	public static final String PREFIX_SECURE = "https://";

	public static final boolean useSecurePrefix = true;

	public static String getPrefix() {
		if (useSecurePrefix)
			return PREFIX_SECURE;
		else
			return PREFIX_UNSECURE;
	}

	//choose between test and production environment
	private static boolean isTest = true;

	private static final String BASE_TEST_URL = "usemenu.com/testapi/v1/";
	private static final String BASE_PRODUCTION_URL = "usemenu.com/api/v1/";

	public static final String BASE_URL = (isTest ? BASE_TEST_URL : BASE_PRODUCTION_URL);

}
