package com.usemenu.MenuAndroidApplication.volley.requests;

import java.util.Map;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.usemenu.MenuAndroidApplication.volley.responses.GetItemInfoResponse;
import com.usemenu.MenuAndroidApplication.volley.responses.TableBeaconResponse;

public class TableBeaconRequest extends GsonRequest<TableBeaconResponse> {

	public TableBeaconRequest(Activity context, Map<String, String> params, final int localRssi, final Listener<TableBeaconResponse> listener) {
		super(context, Request.Method.GET, "tablebeacon", params, TableBeaconResponse.class, new Listener<TableBeaconResponse>() {

			@Override
			public void onResponse(TableBeaconResponse response) {
				response.localRssi = localRssi;
				listener.onResponse(response);

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {

			}
		});
	}

}
