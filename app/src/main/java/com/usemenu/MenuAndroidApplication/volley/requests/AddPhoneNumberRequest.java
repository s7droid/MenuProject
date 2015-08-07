package com.usemenu.MenuAndroidApplication.volley.requests;

import java.util.Map;

import android.app.Activity;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.usemenu.MenuAndroidApplication.volley.responses.AddPhoneNumberResponse;

public class AddPhoneNumberRequest extends GsonRequest<AddPhoneNumberResponse> {

	public AddPhoneNumberRequest(Activity context, Map<String, String> params, Listener<AddPhoneNumberResponse> listener) {
		super(context, Method.POST, "addphonenumber", params, AddPhoneNumberResponse.class, listener, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				
			}
		});
	}

}
