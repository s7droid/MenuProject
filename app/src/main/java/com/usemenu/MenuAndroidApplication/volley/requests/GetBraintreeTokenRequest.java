package com.usemenu.MenuAndroidApplication.volley.requests;

import java.util.Map;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.usemenu.MenuAndroidApplication.volley.responses.GetBraintreeTokenResponse;
import com.usemenu.MenuAndroidApplication.volley.responses.GetCurrencyResponse;
import com.usemenu.MenuAndroidApplication.volley.responses.LoginResponse;

public class GetBraintreeTokenRequest extends GsonRequest<GetBraintreeTokenResponse> {

	public GetBraintreeTokenRequest(Activity context, Map<String, String> params, Listener<GetBraintreeTokenResponse> listener) {
		super(context, Request.Method.GET, "clienttoken", params, GetBraintreeTokenResponse.class, listener, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

}
