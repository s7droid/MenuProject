package com.usemenu.MenuAndroidApplication.dataclasses;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.usemenu.MenuAndroidApplication.utils.Settings;
import com.usemenu.MenuAndroidApplication.volley.responses.GetRestaurantInfoResponse;

public class DataManager {

	private static final String TAG = DataManager.class.getSimpleName();

	private ArrayList<Item> checkoutList;
	private GetRestaurantInfoResponse restaurantInfo;
	private ArrayList<Category> categoriesList;
	private ArrayList<Item> itemsList;
	private Rate rate;
	private float discount;
	private String currency;
	private String clientBraintreeToken;
	private String language = "en";

	public String getStoreMajor(Context context) {
		return Settings.getStoreMajor(context);
	}

	public void setStoreMajor(Context context, String major) {
		Settings.setStoreMajor(context, major);
	}

	public String getStoreMinor(Context context) {
		return Settings.getStoreMinor(context);
	}

	public void setStoreMinor(Context context, String minor) {
		Settings.setStoreMinor(context, minor);
	}

	public String getTableMajor(Context context) {
		return Settings.getTableMajor(context);
	}

	public void setTableMajor(Context context, String major) {
		Settings.setTableMajor(context, major);
	}

	public String getTableMinor(Context context) {
		return Settings.getTableMinor(context);
	}

	public void setTableMinor(Context context, String minor) {
		Settings.setTableMinor(context, minor);
	}

	public boolean getIsInRangeOfTableBeacon(Context context) {
		return Settings.getIsInRangeOfTableBeacon(context);
	}

	public void setIsInRangeOfTableBeacon(Context context, boolean isInRange) {
		Settings.setIsInRangeOfTableBeacon(context, isInRange);
	}

	public String getClientBraintreeToken(Context context) {
		return Settings.getBraintreeToken(context);
	}

	public void setClientBraintreeToken(Context context, String clientBraintreeToken) {
		Settings.setBraintreeToken(context, clientBraintreeToken);
	}

	public float getDiscount(Context context) {
		return Settings.getDiscount(context);
	}

	public void setDiscount(Context context, float discount) {
		Settings.setDiscount(context, discount);
	}

	public String getCurrency(Context context) {
		return Settings.getCurrency(context);
	}

	public void setCurrency(Context context, String currency) {
		Settings.setCurrency(context, currency);
	}

	public ArrayList<Item> getItemsList() {
		return itemsList;
	}

	public void setItemsList(ArrayList<Item> itemsList) {
		this.itemsList = itemsList;
	}

	public ArrayList<Item> getCheckoutList() {

		if (checkoutList == null)
			checkoutList = new ArrayList<Item>();

//		for (Item item : checkoutList) {
//			Log.w(TAG, "item qty small " + item.quantitySmall);
//			Log.w(TAG, "item qty large " + item.quantityLarge);
//		}

		return checkoutList;
	}

	public void addCheckoutListItem(Item item) {

		if (checkoutList == null)
			checkoutList = new ArrayList<Item>();

		Log.d(TAG, "item large quantity " + item.quantityLarge);
		Log.d(TAG, "item small quantity " + item.quantitySmall);

		if (getItemByTag(item.largetag) == null)
			checkoutList.add(item);

		// Item it = getItemByTag(item.largetag);
		//
		//
		//
		// if (it != null) {
		//
		// Log.d(TAG, "it large quantity "+it.quantityLarge);
		// Log.d(TAG, "it small quantity "+it.quantitySmall);
		//
		// it.quantityLarge += item.quantityLarge;
		// it.quantitySmall += item.quantitySmall;
		//
		// Log.d(TAG, "it large quantity after "+it.quantityLarge);
		// Log.d(TAG, "it small quantity after "+it.quantitySmall);
		// } else {
		// checkoutList.add(item);
		// }

	}

	public void removeCheckoutListItem(int tag) {

		try {
			for (Item item : checkoutList) {
				if (item.largetag == tag) {
					if (--item.quantityLarge == 0 && item.quantitySmall == 0) {
						checkoutList.remove(item);
					}
				} else if (item.smalltag == tag) {
					if (--item.quantitySmall == 0 && item.quantityLarge == 0) {
						checkoutList.remove(item);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addCheckoutListItems(ArrayList<Item> items) {
		if (checkoutList == null)
			checkoutList = new ArrayList<Item>();
		checkoutList.addAll(items);
	}

	public boolean isCheckoutListEmpty() {

		if (checkoutList == null)
			checkoutList = new ArrayList<Item>();

		return checkoutList.size() == 0;
	}

	public void clearCheckoutList() {
		checkoutList.clear();
	}

	public void setRestaurantInfo(Context context, GetRestaurantInfoResponse restaurantInfo) {
		Settings.setRestaurantInfo(context, restaurantInfo);
	}

	public GetRestaurantInfoResponse getRestaurantInfo(Context context) {
		return Settings.getRestaurantInfo(context);
	}

	public ArrayList<Category> getCategoriesList() {
		return categoriesList;
	}

	public void setCategoriesList(ArrayList<Category> categoriesList) {
		this.categoriesList = categoriesList;
	}

	public void setTaxRate(Context context, float tax) {
		new Rate(context, tax);
	}

	public void setTipRate(Context context, float min, float max) {
		new Rate(context, min, max);
	}

	public Rate getRate(Context context) {
		if (rate == null)
			rate = new Rate(context);
		return rate;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Item getItemByTag(int tag) {

		for (Item item : checkoutList) {
			if (item.largetag == tag || item.smalltag == tag)
				return item;
		}

		return null;
	}

}
