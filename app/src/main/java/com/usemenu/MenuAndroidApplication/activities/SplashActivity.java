package com.usemenu.MenuAndroidApplication.activities;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.usemenu.MenuAndroidApplication.R;
import com.usemenu.MenuAndroidApplication.app.Menu;
import com.usemenu.MenuAndroidApplication.callbacks.OnIBeaconSearchResultCallback;
import com.usemenu.MenuAndroidApplication.utils.Utils;
import com.usemenu.MenuAndroidApplication.volley.VolleySingleton;
import com.usemenu.MenuAndroidApplication.volley.requests.GetBraintreeTokenRequest;
import com.usemenu.MenuAndroidApplication.volley.requests.GetCurrencyRequest;
import com.usemenu.MenuAndroidApplication.volley.requests.GetDiscountRequest;
import com.usemenu.MenuAndroidApplication.volley.requests.GetRestaurantInfoRequest;
import com.usemenu.MenuAndroidApplication.volley.requests.GetTaxRateRequest;
import com.usemenu.MenuAndroidApplication.volley.requests.GetTipRequest;
import com.usemenu.MenuAndroidApplication.volley.responses.GetBraintreeTokenResponse;
import com.usemenu.MenuAndroidApplication.volley.responses.GetCurrencyResponse;
import com.usemenu.MenuAndroidApplication.volley.responses.GetDiscountResponse;
import com.usemenu.MenuAndroidApplication.volley.responses.GetRestaurantInfoResponse;
import com.usemenu.MenuAndroidApplication.volley.responses.GetTaxRateResponse;
import com.usemenu.MenuAndroidApplication.volley.responses.GetTipResponse;

/**
 * Splash screen activity used for getting data from server, such are connection
 * to Bluetooth device, and gathering all other data needed for application to
 * work. <br>
 * Also, within this activity, connection on Internet is checked.
 * 
 * @author s7Design
 *
 */
public class SplashActivity extends BaseActivity {

	private static final String TAG = SplashActivity.class.getSimpleName();

	public static final int REQUEST_CODE_LOCATION = 1;
	public static final int REQUEST_CODE_BLUETOOTH = 2;

	// VIEWS
	private TextView mTitleText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		hideActionBar();
		initViews();
		doChecks();
	}

	private void getBraintreeToken() {
		GetBraintreeTokenRequest tokenRequest = new GetBraintreeTokenRequest(SplashActivity.this, null, new Listener<GetBraintreeTokenResponse>() {

			@Override
			public void onResponse(GetBraintreeTokenResponse token) {

				Menu.getInstance().getDataManager().setClientBraintreeToken(SplashActivity.this, token.token);

//				 Settings.setStoreMajor(SplashActivity.this, "1");
//				 Settings.setStoreMinor(SplashActivity.this, "1");
//				 Settings.setTableMajor(SplashActivity.this, "1");
//				 Settings.setTableMinor(SplashActivity.this, "1");
//
//				 onBeaconFound();

				scanForIBeacon();
			}
		});
		VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(tokenRequest);
	}

	private void doChecks() {

		if (!Utils.isNetworkAvailable(this)) {
			showAlertDialog(R.string.dialog_no_internet_connection_title, R.string.dialog_no_internet_connection_message, new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();

				}
			});
		} else if (!Utils.isLocationEnabled(this)) {

			Intent intent = new Intent(this, SplashWarningActivity.class);
			intent.putExtra(SplashWarningActivity.INTENT_EXTRA_TAG_START, SplashWarningActivity.INTENT_EXTRA_START_LOCATION);
			startActivityForResult(intent, REQUEST_CODE_LOCATION);

		} else if (!Utils.isBluetoothEnabled()) {

			Intent intent = new Intent(this, SplashWarningActivity.class);
			intent.putExtra(SplashWarningActivity.INTENT_EXTRA_TAG_START, SplashWarningActivity.INTENT_EXTRA_START_BLUETOOTH);
			startActivityForResult(intent, REQUEST_CODE_BLUETOOTH);

			// OkCancelDialogFragment okCancelDialog = new
			// OkCancelDialogFragment();
			// okCancelDialog.showDialog(getFragmentManager(),
			// getString(R.string.dialog_title_error),
			// getString(R.string.dialog_bluetooth_off), new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			//
			// IntentFilter filter = new
			// IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
			// bluetoothReceiver = new BroadcastReceiver() {
			// @Override
			// public void onReceive(Context context, Intent intent) {
			// final String action = intent.getAction();
			//
			// if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
			// final int state =
			// intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
			// BluetoothAdapter.ERROR);
			// switch (state) {
			// case BluetoothAdapter.STATE_OFF:
			// Log.d(TAG, "off");
			// break;
			// case BluetoothAdapter.STATE_TURNING_OFF:
			// Log.d(TAG, "turning off");
			// break;
			// case BluetoothAdapter.STATE_ON:
			// dismissProgressDialog();
			// initViews();
			// initData();
			// Log.d(TAG, "on");
			// break;
			// case BluetoothAdapter.STATE_TURNING_ON:
			// Log.d(TAG, "turning on");
			// break;
			// }
			// }
			// }
			// };
			// registerReceiver(bluetoothReceiver, filter);
			//
			// showProgressDialog(R.string.dialog_please_wait);
			//
			// BluetoothAdapter.getDefaultAdapter().enable();
			// }
			// }, new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// finish();
			// }
			// });
		} else {

			getBraintreeToken();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_CODE_LOCATION) {
			if (!Utils.isLocationEnabled(this)) {
				finish();
			} else {
				if (!Utils.isBluetoothEnabled()) {
					Intent intent = new Intent(this, SplashWarningActivity.class);
					intent.putExtra(SplashWarningActivity.INTENT_EXTRA_TAG_START, SplashWarningActivity.INTENT_EXTRA_START_BLUETOOTH);
					startActivityForResult(intent, REQUEST_CODE_BLUETOOTH);
				} else {
					getBraintreeToken();
				}
			}
		} else if (requestCode == REQUEST_CODE_BLUETOOTH) {
			if (!Utils.isBluetoothEnabled()) {
				finish();
			} else {
				if (!Utils.isLocationEnabled(this)) {
					Intent intent = new Intent(this, SplashWarningActivity.class);
					intent.putExtra(SplashWarningActivity.INTENT_EXTRA_TAG_START, SplashWarningActivity.INTENT_EXTRA_START_LOCATION);
					startActivityForResult(intent, REQUEST_CODE_LOCATION);

				} else {
					getBraintreeToken();
				}
			}
		}
	}

	/**
	 * Method for initializing all views in {@link SplashSActivity}
	 */
	private void initViews() {

		mTitleText = (TextView) findViewById(R.id.textviewSplashActivityMenuTitle);

		String finalString = getResources().getString(R.string.splash_screen_welcome_message);
		Spannable sb = new SpannableString(finalString);
		System.out.println("finalstring.lenght= " + finalString.length());
		System.out.println("lenght - 4=" + (finalString.length() - 4));
		sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), finalString.length() - 4, finalString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // bold

		mTitleText.setText(sb);
	}

	private int searchAtempts = 2;

	private void scanForIBeacon() {

		Menu.getInstance().searchForIBeacon(new OnIBeaconSearchResultCallback() {

			@Override
			public void onIBeaconSearchResult(int result) {

				if (result == OnIBeaconSearchResultCallback.SEARCH_RESULT_STORE_BEACON_FOUND || result == OnIBeaconSearchResultCallback.SEARCH_RESULT_TABLE_BEACON_FOUND) {

					onBeaconFound();

				} else if (result == OnIBeaconSearchResultCallback.SEARCH_RESULT_BEACON_NOT_FOUND) {

					dismissProgressDialog();

					// if (searchAtempts > 0) {
					// --searchAtempts;
					// OkCancelDialogFragment dialog = new
					// OkCancelDialogFragment();
					// dialog.showDialog(getFragmentManager(), "",
					// getString(R.string.dialog_no_restaurants_found), new
					// OnClickListener() {
					//
					// @Override
					// public void onClick(View v) {
					// showProgressDialogLoading();
					// scanForIBeacon();
					//
					// }
					// }, new OnClickListener() {
					//
					// @Override
					// public void onClick(View v) {
					// startActivity(new Intent(getApplicationContext(),
					// MainMenuActivity.class));
					// finish();
					// }
					// });
					// } else {
					startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
					finish();
					// }
				}
			}
		});

	}

	private void onBeaconFound() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... pars) {

				final CountDownLatch countDownLatch = new CountDownLatch(5);

				Map<String, String> params = new HashMap<String, String>();
				params.put("major", Menu.getInstance().getDataManager().getStoreMajor(SplashActivity.this));
				params.put("minor", Menu.getInstance().getDataManager().getStoreMinor(SplashActivity.this));

				GetRestaurantInfoRequest restaurantInfoRequest = new GetRestaurantInfoRequest(SplashActivity.this, params, new Listener<GetRestaurantInfoResponse>() {

					@Override
					public void onResponse(GetRestaurantInfoResponse restaurantInfo) {
						Menu.getInstance().getDataManager().setRestaurantInfo(SplashActivity.this, restaurantInfo);
						countDownLatch.countDown();

						String major = Menu.getInstance().getDataManager().getStoreMajor(SplashActivity.this);
						String minor = Menu.getInstance().getDataManager().getStoreMinor(SplashActivity.this);

						if ((major != null && minor != null)) {
							Intent i = new Intent(SplashActivity.this, RestaurantPreviewActivity.class);
							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(i);
						} else if ((major == null || minor == null)) {
							Intent i = new Intent(SplashActivity.this, MainMenuActivity.class);
							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(i);
						}
					}
				});

				GetTaxRateRequest taxRequest = new GetTaxRateRequest(SplashActivity.this, params, new Listener<GetTaxRateResponse>() {

					@Override
					public void onResponse(GetTaxRateResponse taxRate) {
						Menu.getInstance().getDataManager().setTaxRate(SplashActivity.this, taxRate.rate[0].tax);
						countDownLatch.countDown();
					}
				});

				GetTipRequest tipRequest = new GetTipRequest(SplashActivity.this, params, new Listener<GetTipResponse>() {

					@Override
					public void onResponse(GetTipResponse tipRate) {
						Menu.getInstance().getDataManager().setTipRate(SplashActivity.this, tipRate.rate[0].mintip, tipRate.rate[0].maxtip);
						countDownLatch.countDown();
					}
				});

				GetDiscountRequest discountRequest = new GetDiscountRequest(SplashActivity.this, params, new Listener<GetDiscountResponse>() {

					@Override
					public void onResponse(GetDiscountResponse discount) {
						Menu.getInstance().getDataManager().setDiscount(SplashActivity.this, discount.discount);
						countDownLatch.countDown();
					}
				});

				GetCurrencyRequest currencyRequest = new GetCurrencyRequest(SplashActivity.this, params, new Listener<GetCurrencyResponse>() {

					@Override
					public void onResponse(GetCurrencyResponse currency) {
						Menu.getInstance().getDataManager().setCurrency(SplashActivity.this, currency.currency);
						countDownLatch.countDown();
					}
				});

				VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(restaurantInfoRequest);
				VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(taxRequest);
				VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(tipRequest);
				VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(discountRequest);
				VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(currencyRequest);

				try {
					countDownLatch.await(20000, TimeUnit.MILLISECONDS);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				return null;
			}
		}.execute();
	}

}
