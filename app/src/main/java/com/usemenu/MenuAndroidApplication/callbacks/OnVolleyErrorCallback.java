package com.usemenu.MenuAndroidApplication.callbacks;

import com.android.volley.VolleyError;
import com.usemenu.MenuAndroidApplication.volley.responses.GsonResponse;

public interface OnVolleyErrorCallback {

	public void onResponseError(GsonResponse response);
	public void onVolleyError(VolleyError volleyError);

}
