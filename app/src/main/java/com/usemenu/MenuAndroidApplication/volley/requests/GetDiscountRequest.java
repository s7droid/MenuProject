package com.usemenu.MenuAndroidApplication.volley.requests;

import java.util.Map;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.usemenu.MenuAndroidApplication.volley.responses.GetDiscountResponse;

public class GetDiscountRequest extends GsonRequest<GetDiscountResponse> {

	public GetDiscountRequest(Activity context, Map<String, String> params, Listener<GetDiscountResponse> listener) {
		super(context, Request.Method.GET, "getdiscount", params, GetDiscountResponse.class, listener, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				System.out.println("GetDiscountRequest.GetDiscountRequest(...).new ErrorListener() {...}.onErrorResponse()");
			}
		});
	}

}
