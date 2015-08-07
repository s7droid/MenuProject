package com.usemenu.MenuAndroidApplication.utils;

import com.usemenu.MenuAndroidApplication.volley.responses.GetRestaurantInfoResponse;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Settings {

	private static final String PREFERENCE_NAME = "Preferences";

	public static final String PREFERENCE_TAG_ACCESS_TOKEN = "access_token";
	public static final String PREFERENCE_TAG_STORE_MAJOR = "preference_access_tag_store_major";
	public static final String PREFERENCE_TAG_STORE_MINOR = "preference_access_tag_store_minor";
	public static final String PREFERENCE_TAG_TABLE_MAJOR = "preference_access_tag_table_major";
	public static final String PREFERENCE_TAG_TABLE_MINOR = "preference_access_tag_table_minor";
	public static final String PREFERENCE_TAG_IS_IN_RANGE = "preference_access_tag_is_in_range";
	public static final String PREFERENCE_TAG_BRAIN_TREE_TOKEN = "preference_brain_tree_token";
	public static final String PREFERENCE_TAG_RESTAURANT_INFO = "preference_tag_restaurant_info";
	public static final String PREFERENCE_TAG_DISCOUNT = "preference_tag_discount";
	public static final String PREFERENCE_TAG_CURRENCY = "preference_tag_currency";
	public static final String PREFERENCE_TAG_MIN_TIP = "preference_tag_min_tip";
	public static final String PREFERENCE_TAG_MAX_TIP = "preference_tag_max_tip";
	public static final String PREFERENCE_TAG_TAX = "preference_tag_tax";

	private static SharedPreferences getSharedPreferences(Context context) {
		return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
	}

	private static Editor getEditor(Context context) {
		return getSharedPreferences(context).edit();
	}

	public static void clear(Context context) {
		Editor editor = getEditor(context);
		editor.clear();
		editor.commit();
	}

	public static String getAccessToken(Context context) {
		return getSharedPreferences(context).getString(PREFERENCE_TAG_ACCESS_TOKEN, "");
	}

	public static void setAccessToken(Context context, String accessToken) {
		Editor editor = getEditor(context);
		editor.putString(PREFERENCE_TAG_ACCESS_TOKEN, accessToken);
		editor.commit();
	}

	public static String getStoreMajor(Context context) {
		return getSharedPreferences(context).getString(PREFERENCE_TAG_STORE_MAJOR, "");
	}

	public static final void setStoreMajor(Context context, String major) {
		Editor editor = getEditor(context);
		editor.putString(PREFERENCE_TAG_STORE_MAJOR, major);
		editor.commit();
	}

	public static String getStoreMinor(Context context) {
		return getSharedPreferences(context).getString(PREFERENCE_TAG_STORE_MINOR, "");
	}

	public static final void setStoreMinor(Context context, String minor) {
		Editor editor = getEditor(context);
		editor.putString(PREFERENCE_TAG_STORE_MINOR, minor);
		editor.commit();
	}

	public static String getTableMajor(Context context) {
		return getSharedPreferences(context).getString(PREFERENCE_TAG_TABLE_MAJOR, "");
	}

	public static final void setTableMajor(Context context, String major) {
		Editor editor = getEditor(context);
		editor.putString(PREFERENCE_TAG_TABLE_MAJOR, major);
		editor.commit();
	}

	public static String getTableMinor(Context context) {
		return getSharedPreferences(context).getString(PREFERENCE_TAG_TABLE_MINOR, "");
	}

	public static final void setTableMinor(Context context, String minor) {
		Editor editor = getEditor(context);
		editor.putString(PREFERENCE_TAG_TABLE_MINOR, minor);
		editor.commit();
	}

	public static boolean getIsInRangeOfTableBeacon(Context context) {
		return getSharedPreferences(context).getBoolean(PREFERENCE_TAG_IS_IN_RANGE, false);
	}

	public static final void setIsInRangeOfTableBeacon(Context context, boolean isInRange) {
		Editor editor = getEditor(context);
		editor.putBoolean(PREFERENCE_TAG_IS_IN_RANGE, isInRange);
		editor.commit();
	}

	public static final void setBraintreeToken(Context context, String token) {
		Editor editor = getEditor(context);
		editor.putString(PREFERENCE_TAG_BRAIN_TREE_TOKEN, token);
		editor.commit();
	}

	public static final String getBraintreeToken(Context context) {
		return getSharedPreferences(context).getString(PREFERENCE_TAG_BRAIN_TREE_TOKEN, "");
	}

	public static final void setRestaurantInfo(Context context, GetRestaurantInfoResponse restaurantInfo) {
		Editor editor = getEditor(context);
		editor.putString(PREFERENCE_TAG_RESTAURANT_INFO, Utils.getGson().toJson(restaurantInfo));
		editor.commit();
	}

	public static final GetRestaurantInfoResponse getRestaurantInfo(Context context) {
		GetRestaurantInfoResponse response = Utils.getGson().fromJson(getSharedPreferences(context).getString(PREFERENCE_TAG_RESTAURANT_INFO, ""),
				GetRestaurantInfoResponse.class);
		return response;
	}

	public static final void setDiscount(Context context, float discount) {
		Editor editor = getEditor(context);
		editor.putFloat(PREFERENCE_TAG_DISCOUNT, discount);
		editor.commit();
	}

	public static final float getDiscount(Context context) {
		return getSharedPreferences(context).getFloat(PREFERENCE_TAG_DISCOUNT, 0);
	}

	public static final void setCurrency(Context context, String currency) {
		Editor editor = getEditor(context);
		editor.putString(PREFERENCE_TAG_CURRENCY, currency);
		editor.commit();
	}

	public static final String getCurrency(Context context) {
		return getSharedPreferences(context).getString(PREFERENCE_TAG_CURRENCY, "");
	}

	public static final void setMinTip(Context context, float mintip) {
		Editor editor = getEditor(context);
		editor.putFloat(PREFERENCE_TAG_MIN_TIP, mintip);
		editor.commit();
	}

	public static final float getMinTip(Context context) {
		return getSharedPreferences(context).getFloat(PREFERENCE_TAG_MIN_TIP, 0);
	}

	public static final void setMaxTip(Context context, float maxtip) {
		Editor editor = getEditor(context);
		editor.putFloat(PREFERENCE_TAG_MAX_TIP, maxtip);
		editor.commit();
	}

	public static final float getMaxTip(Context context) {
		return getSharedPreferences(context).getFloat(PREFERENCE_TAG_MAX_TIP, 0);
	}

	public static final void setTax(Context context, float tax) {
		Editor editor = getEditor(context);
		editor.putFloat(PREFERENCE_TAG_TAX, tax);
		editor.commit();
	}

	public static final float getTax(Context context) {
		return getSharedPreferences(context).getFloat(PREFERENCE_TAG_TAX, 0);
	}

}