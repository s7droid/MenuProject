package com.usemenu.MenuAndroidApplication.volley.requests;

import java.util.Map;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.usemenu.MenuAndroidApplication.volley.responses.GetItemInfoResponse;

public class GetItemInfoRequest extends GsonRequest<GetItemInfoResponse> {

	public GetItemInfoRequest(Activity context, Map<String, String> params, Listener<GetItemInfoResponse> listener) {
		super(context, Request.Method.GET, "getiteminfo", params, GetItemInfoResponse.class, listener, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

}
