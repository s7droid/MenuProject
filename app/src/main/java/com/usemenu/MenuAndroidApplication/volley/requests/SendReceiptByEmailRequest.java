package com.usemenu.MenuAndroidApplication.volley.requests;

import java.util.Map;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.usemenu.MenuAndroidApplication.volley.responses.SendReceiptByEmailResponse;

public class SendReceiptByEmailRequest extends GsonRequest<SendReceiptByEmailResponse>{

	public SendReceiptByEmailRequest(Activity context, Map<String, String> params, Listener<SendReceiptByEmailResponse> listener) {
		super(context, Request.Method.POST, "sendreceiptemail", params, SendReceiptByEmailResponse.class, listener, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				
			}
		});
	}

}
