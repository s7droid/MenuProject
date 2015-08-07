package com.usemenu.MenuAndroidApplication.volley.requests;

import java.util.Map;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.usemenu.MenuAndroidApplication.volley.responses.GetCurrencyResponse;
import com.usemenu.MenuAndroidApplication.volley.responses.LoginResponse;

public class LoginRequest extends GsonRequest<LoginResponse> {

	public LoginRequest(Activity context, Map<String, String> params, Listener<LoginResponse> listener) {
		super(context, Request.Method.POST, "login", params, LoginResponse.class, listener, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

}
