package com.usemenu.MenuAndroidApplication.volley.requests;

import java.util.Map;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.usemenu.MenuAndroidApplication.volley.responses.GetRestaurantInfoResponse;

public class GetRestaurantInfoRequest extends GsonRequest<GetRestaurantInfoResponse> {

	public GetRestaurantInfoRequest(Activity context, Map<String, String> params, Listener<GetRestaurantInfoResponse> listener) {
		super(context, Request.Method.GET, "beaconinfo", params, GetRestaurantInfoResponse.class, listener, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				System.out.println("GetRestaurantInfoRequest.GetRestaurantInfoRequest(...).new ErrorListener() {...}.onErrorResponse()");
			}
		});
	}

}
