package com.usemenu.MenuAndroidApplication.volley.requests;

import java.util.Map;

import android.app.Activity;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.usemenu.MenuAndroidApplication.volley.responses.CheckIfPhoneNeededResponse;

/**
 *  !!!! Params mandatory - "accesstoken" and "phonenumber" (e.g +41791234567) 
 * @author S7 Design
 *
 */
public class CheckIfPhoneNeededRequest extends GsonRequest<CheckIfPhoneNeededResponse> {

	public CheckIfPhoneNeededRequest(Activity context, Map<String, String> params, Listener<CheckIfPhoneNeededResponse> listener) {
		super(context, Method.POST, "needsphonenumber", params, CheckIfPhoneNeededResponse.class, listener, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				
			}
		});
	}

}
