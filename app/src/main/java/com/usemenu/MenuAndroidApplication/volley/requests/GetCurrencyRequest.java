package com.usemenu.MenuAndroidApplication.volley.requests;

import java.util.Map;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.usemenu.MenuAndroidApplication.volley.responses.GetCurrencyResponse;

public class GetCurrencyRequest extends GsonRequest<GetCurrencyResponse> {

	public GetCurrencyRequest(Activity context, Map<String, String> params, Listener<GetCurrencyResponse> listener) {
		super(context, Request.Method.GET, "getcurrency", params, GetCurrencyResponse.class, listener, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				System.out.println("GetCurrencyRequest.GetCurrencyRequest(...).new ErrorListener() {...}.onErrorResponse()");
			}
		});
	}

}
