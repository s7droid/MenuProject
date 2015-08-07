package com.usemenu.MenuAndroidApplication.activities;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response.Listener;
import com.usemenu.MenuAndroidApplication.R;
import com.usemenu.MenuAndroidApplication.utils.Settings;
import com.usemenu.MenuAndroidApplication.utils.Utils;
import com.usemenu.MenuAndroidApplication.volley.VolleySingleton;
import com.usemenu.MenuAndroidApplication.volley.requests.AddPhoneNumberRequest;
import com.usemenu.MenuAndroidApplication.volley.responses.AddPhoneNumberResponse;

public class PickupInfoActivity extends BaseActivity {

	// VIEWS
	private Button mButtonConfirm;;
	private EditText mEditTextCountryCode;
	private EditText mEditTextPhoneNumber;

	// DATA

	// CONTROLLERS

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pickup_info);
		initActionBar();
		initViews();
	}

	private void initActionBar() {
		setActionBarForwardArrowVisibility(null);
		setActionBarForwardButtonTextColor(getResources().getColor(R.color.menu_main_orange));
		setActionBarForwardButtonText(getResources().getString(R.string.action_bar_pickup_info));

		setActionBarMenuButtonVisibility(View.GONE);

		setActionBarBackButtonOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private void initViews() {
		mButtonConfirm = (Button) findViewById(R.id.buttonPickupInfoActivityConfirm);
		mEditTextCountryCode = (EditText) findViewById(R.id.edittextPickupInfoActivityCountryCodeValue);
		mEditTextPhoneNumber = (EditText) findViewById(R.id.edittextPickupInfoActivityPhoneNumberValue);

		mEditTextCountryCode.requestFocus();
		Utils.handleOutsideEditTextClick(findViewById(R.id.relativelayoutPickupInfoActivity), this);

		mButtonConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Map<String, String> params = new HashMap<String, String>();
				params.put("accesstoken", Settings.getAccessToken(PickupInfoActivity.this));

				if (mEditTextCountryCode.getText().toString().isEmpty()
						&& (mEditTextPhoneNumber.getText().toString().isEmpty() || mEditTextPhoneNumber.getText().toString().length() < 6)) {
					showAlertDialog("", getResources().getString(R.string.pickup_info_add_phone));
					return;
				}

				String phonenumber = "+" + mEditTextCountryCode.getText().toString() + mEditTextPhoneNumber.getText().toString();

				params.put("phonenumber", phonenumber);
				AddPhoneNumberRequest request = new AddPhoneNumberRequest(PickupInfoActivity.this, params, new Listener<AddPhoneNumberResponse>() {

					@Override
					public void onResponse(AddPhoneNumberResponse arg0) {

						// showAlertDialog("",
						// getResources().getString(R.string.pickup_info_phone_number_added),
						// new OnClickListener() {
						//
						// @Override
						// public void onClick(View v) {
						// Intent intent = new Intent();
						// setResult(RESULT_OK,intent);
						// finish();
						// }
						// });
						Intent intent = new Intent();
						setResult(RESULT_OK, intent);
						finish();
					}
				});

				VolleySingleton.getInstance(PickupInfoActivity.this).addToRequestQueue(request);

			}
		});

	}

}