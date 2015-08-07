package com.usemenu.MenuAndroidApplication.volley.requests;

import java.util.Map;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.usemenu.MenuAndroidApplication.volley.responses.GetReceiptsResponse;

public class GetReceiptsRequest extends GsonRequest<GetReceiptsResponse> {

	public GetReceiptsRequest(Activity context, Map<String, String> params, Listener<GetReceiptsResponse> listener) {
		super(context, Request.Method.POST, "getreceipts", params, GetReceiptsResponse.class, listener, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

}
