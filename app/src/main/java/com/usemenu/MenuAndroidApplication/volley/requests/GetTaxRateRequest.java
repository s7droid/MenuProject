package com.usemenu.MenuAndroidApplication.volley.requests;

import java.util.Map;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.usemenu.MenuAndroidApplication.volley.responses.GetTaxRateResponse;

public class GetTaxRateRequest extends GsonRequest<GetTaxRateResponse> {

	public GetTaxRateRequest(Activity context, Map<String, String> params, Listener<GetTaxRateResponse> listener) {
		super(context, Request.Method.GET, "gettaxrate", params, GetTaxRateResponse.class, listener, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				System.out.println("GetTaxRateRequest.GetTaxRateRequest(...).new ErrorListener() {...}.onErrorResponse()");
			}
		});
	}

}
