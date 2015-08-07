package com.usemenu.MenuAndroidApplication.volley.requests;

import java.util.Map;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.usemenu.MenuAndroidApplication.volley.responses.GetTipResponse;

public class GetTipRequest extends GsonRequest<GetTipResponse> {

	public GetTipRequest(Activity context, Map<String, String> params, Listener<GetTipResponse> listener) {
		super(context, Request.Method.GET, "gettip", params, GetTipResponse.class, listener, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				System.out.println("GetTipRequest.GetTipRequest(...).new ErrorListener() {...}.onErrorResponse()");
			}
		});
	}

}
