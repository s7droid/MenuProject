package com.usemenu.MenuAndroidApplication.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

import android.R.menu;
import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v4.util.Pair;
import android.util.Log;

import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.usemenu.MenuAndroidApplication.callbacks.OnIBeaconSearchResultCallback;
import com.usemenu.MenuAndroidApplication.callbacks.OnVolleyErrorCallback;
import com.usemenu.MenuAndroidApplication.dataclasses.Beacon;
import com.usemenu.MenuAndroidApplication.dataclasses.DataManager;
import com.usemenu.MenuAndroidApplication.volley.VolleySingleton;
import com.usemenu.MenuAndroidApplication.volley.requests.TableBeaconRequest;
import com.usemenu.MenuAndroidApplication.volley.responses.GsonResponse;
import com.usemenu.MenuAndroidApplication.volley.responses.TableBeaconResponse;

public class Menu extends Application implements BeaconConsumer, LeScanCallback, OnIBeaconSearchResultCallback {

	private final String TAG = Menu.class.getSimpleName();

	private static Menu instance;

	private List<Activity> activityStack;
	private Map<String, Pair<String, Pair<String, String>>> responseTimeStatistics;

	private static Context context;

	private DataManager dataManager;

	private OnVolleyErrorCallback onVolleyErrorCallback;

	public static final Integer DISABLING_MINOR_VALUE = 0;

	private static final int SPLASH_SCREEN_TIMEOUT = 5000;

	private static final int SCAN_PERIOD_DEFAULT = 60000;
	private static final int SCAN_PERIOD_CHECKOUT_SCREEN = 10000;
	private static int SCAN_PERIOD = SCAN_PERIOD_DEFAULT;

	private BeaconManager beaconManager;
	private BluetoothManager bluetoothManager;
	private BluetoothAdapter bluetoothAdapter;

	private Timer timer;

	private Map<String, Beacon> beaconMap;

	public static final String storeUuid = "DE5B5C5E-C681-4DF6-9349-0456EDE0EA45";
	public static final String tableUuid = "A5324FA8-BCF1-421B-945E-F83DF4672519";

	private UUID[] uuids;

	public Menu() {
		instance = this;
		activityStack = new ArrayList<Activity>();
		responseTimeStatistics = new HashMap<String, Pair<String, Pair<String, String>>>();

		dataManager = new DataManager();

		timer = new Timer();

		beaconMap = new HashMap<String, Beacon>();

		uuids = new UUID[2];
		uuids[0] = UUID.fromString(storeUuid);
		uuids[1] = UUID.fromString(tableUuid);

	}

	public static synchronized Context getContext() {
		if (instance == null)
			new Menu();
		return instance;
	}

	public static Menu getInstance() {
		if (instance == null)
			new Menu();
		return instance;
	}

	public static Context getMenuApplicationContext() {
		return context;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.context = getApplicationContext();

		beaconManager = BeaconManager.getInstanceForApplication(this);
		beaconManager.bind(this);
		bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		bluetoothAdapter = bluetoothManager.getAdapter();

		// ACRA.init(this);
		MenuTrustManager.init();
	}

	public void destroyActivity(int position) {
		try {
			activityStack.get(position).finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void destroyActivity(Activity activity) {
		try {
			activityStack.get(activityStack.indexOf(activity)).finish();
			activityStack.remove(activityStack.indexOf(activity));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addActivityToStack(Activity activity) {
		try {
			activityStack.add(activity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void popBackActivityStack() {
		try {
			activityStack.get(activityStack.size() - 1).finish();
			activityStack.remove(activityStack.size() - 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void destroyAllActivitiesFromStack() {
		try {
			for (Activity activity : activityStack) {
				activity.finish();
			}
			activityStack.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<String, Pair<String, Pair<String, String>>> getTimeResponses() {
		return this.responseTimeStatistics;
	}

	public DataManager getDataManager() {
		return dataManager;
	}

	public void registerOnVolleyErrorCallback(OnVolleyErrorCallback onVolleyErrorCallback) {
		this.onVolleyErrorCallback = onVolleyErrorCallback;
	}

	public void unregisterOnVolleyErrorCallback() {
		this.onVolleyErrorCallback = null;
	}

	public void onResponseErrorReceived(GsonResponse response) {

		if (onVolleyErrorCallback != null)
			onVolleyErrorCallback.onResponseError(response);
	}

	public void onVolleyErrorReceived(VolleyError error) {

		if (onVolleyErrorCallback != null)
			onVolleyErrorCallback.onVolleyError(error);
	}

	public boolean isOrderEnabled() {

		if (dataManager.getTableMajor(getApplicationContext()).isEmpty()
				|| (int) Integer.valueOf(dataManager.getTableMinor(getApplicationContext())) == DISABLING_MINOR_VALUE
				|| !dataManager.getIsInRangeOfTableBeacon(getApplicationContext()))
			return false;
		else
			return true;
	}

	public boolean isInARestaurant() {

		return dataManager.getStoreMajor(getApplicationContext()).length() > 0;
	}

	Handler handler = new Handler();
	private Runnable runnable = new Runnable() {
		public void run() {

			bluetoothAdapter.stopLeScan(Menu.this);
			onIBeaconSearchResult(0);
			handler.removeCallbacks(runnable);
		}
	};

	private OnIBeaconSearchResultCallback onIBeaconSearchResultCallback;
	private OnIBeaconSearchResultCallback onSubscribeIBeaconSearchResultCallback;

	public void subscribeIBeaconSearchResultCallback(OnIBeaconSearchResultCallback callback) {
		onSubscribeIBeaconSearchResultCallback = callback;
	}

	public void subscribeIBeaconSearchResultCallbackCheckout(OnIBeaconSearchResultCallback callback) {
		onSubscribeIBeaconSearchResultCallback = callback;
		SCAN_PERIOD = SCAN_PERIOD_CHECKOUT_SCREEN;
	}

	public void unsubscribeIBeaconSearchResultCallback() {
		onSubscribeIBeaconSearchResultCallback = null;
		SCAN_PERIOD = SCAN_PERIOD_DEFAULT;
		searchForIBeacon(null);
	}

	private TimerTask timerTask;

	public void searchForIBeaconCheckout(OnIBeaconSearchResultCallback callback) {
		SCAN_PERIOD = SCAN_PERIOD_CHECKOUT_SCREEN;
		onSubscribeIBeaconSearchResultCallback = callback;
		searchForIBeacon(null);
	}

	public void searchForIBeacon(OnIBeaconSearchResultCallback callback) {
		
		//TODO: comment this
//		if(true)
//			return;
		
		onIBeaconSearchResultCallback = callback;

		beaconMap.clear();
		bluetoothAdapter.startLeScan(this);
		handler.postDelayed(runnable, SPLASH_SCREEN_TIMEOUT);

		if (timerTask != null) {
			timer.cancel();
			timer.purge();
		}
		timer = new Timer();

		if (timerTask != null)
			timerTask.cancel();

		timerTask = new TimerTask() {

			@Override
			public void run() {

				Log.w(TAG, "timer run!!!");

				beaconMap.clear();
				bluetoothAdapter.startLeScan(Menu.this);
				handler.postDelayed(runnable, SPLASH_SCREEN_TIMEOUT);
			}
		};

		try {
			timer.schedule(timerTask, SCAN_PERIOD, SCAN_PERIOD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBeaconServiceConnect() {
		beaconManager.setMonitorNotifier(new MonitorNotifier() {
			@Override
			public void didEnterRegion(Region region) {
				Log.i(TAG, "I just saw a beacon for the first time!");
			}

			@Override
			public void didExitRegion(Region region) {
				Log.i(TAG, "I no longer see a beacon");
			}

			@Override
			public void didDetermineStateForRegion(int state, Region region) {
				Log.i(TAG, "I have just switched from seeing/not seeing beacons: " + state);
			}
		});

		try {
			beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
		} catch (RemoteException e) {
		}
	}

	@Override
	public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

		int startByte = 2;
		boolean patternFound = false;
		while (startByte <= 5) {
			if (((int) scanRecord[startByte + 2] & 0xff) == 0x02 && // Identifies
																	// an
																	// iBeacon
					((int) scanRecord[startByte + 3] & 0xff) == 0x15) { // Identifies
																		// correct
																		// data
																		// length
				patternFound = true;
				break;
			}
			startByte++;
		}

		if (patternFound) {
			// Convert to hex String
			byte[] uuidBytes = new byte[16];
			System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 16);
			String hexString = bytesToHex(uuidBytes);
			// Here is your UUID
			String uuid = hexString.substring(0, 8) + "-" + hexString.substring(8, 12) + "-" + hexString.substring(12, 16) + "-"
					+ hexString.substring(16, 20) + "-" + hexString.substring(20, 32);
			// Here is your Major value
			int major = (scanRecord[startByte + 20] & 0xff) * 0x100 + (scanRecord[startByte + 21] & 0xff);
			// Here is your Minor value
			int minor = (scanRecord[startByte + 22] & 0xff) * 0x100 + (scanRecord[startByte + 23] & 0xff);

			Beacon beacon = new Beacon();
			beacon.uuid = uuid;
			beacon.major = major;
			beacon.minor = minor;
			beacon.rssi = rssi;

			String key = !beacon.uuid.contains(storeUuid) ? (String.valueOf(beacon.uuid) + "|" + String.valueOf(beacon.major) + "|" + String
					.valueOf(beacon.minor)) : String.valueOf(beacon.uuid);

			// TODO: check this with Marlon
			// if (!beaconMap.containsKey(uuid))
			beaconMap.put(key, beacon);
		}
	}

	static final char[] hexArray = "0123456789ABCDEF".toCharArray();

	private static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	@Override
	public void onIBeaconSearchResult(int result) {

		if (result == OnIBeaconSearchResultCallback.SEARCH_RESULT_DEFAULT) {

			if (beaconMap.containsKey(storeUuid)) {
				result = OnIBeaconSearchResultCallback.SEARCH_RESULT_STORE_BEACON_FOUND;

				String tableMajor = "";
				String tableMinor = "";

				Beacon storeBeacon = null;
				boolean isTableBeaconAvailable = false;
				for (Beacon beacon : beaconMap.values()) {
					if (beacon.uuid.contains(storeUuid)) {
						storeBeacon = beacon;
					}

					if (beacon.uuid.contains(tableUuid))
						isTableBeaconAvailable = true;
				}

				// if (beaconMap.containsKey(tableUuid)) {
				if (isTableBeaconAvailable) {
					result = OnIBeaconSearchResultCallback.SEARCH_RESULT_TABLE_BEACON_FOUND;

					Beacon closestBeacon = null;
					int maxRssi = -128;

					final ArrayList<Beacon> beacons = new ArrayList<Beacon>(beaconMap.values());

					for (Beacon beacon : beacons) {
						if (beacon.uuid.contains(tableUuid) && beacon.rssi > maxRssi) {
							maxRssi = beacon.rssi;
							closestBeacon = beacon;
						}
					}

					// Iterator it = beaconMap.entrySet().iterator();
					// while (it.hasNext()) {
					// Map.Entry pair = (Map.Entry) it.next();
					// Beacon beacon = (Beacon) pair.getValue();
					//
					// Log.i(TAG, "beacon uuid " + beacon.uuid);
					// Log.i(TAG, "beacon rssi " + beacon.rssi);
					//
					// if (beacon.uuid.equals(tableUuid) && beacon.rssi >
					// maxRssi) {
					// maxRssi = beacon.rssi;
					// closestBeacon = beacon;
					// }
					// }

					tableMajor = String.valueOf(closestBeacon.major);
					tableMinor = String.valueOf(closestBeacon.minor);

					Map<String, String> params = new HashMap<String, String>();
					params.put("major", tableMajor);
					params.put("minor", tableMinor);

					TableBeaconRequest tableBeaconRequest = new TableBeaconRequest(null, params, closestBeacon.rssi,
							new Listener<TableBeaconResponse>() {

								@Override
								public void onResponse(TableBeaconResponse response) {

									dataManager.setIsInRangeOfTableBeacon(getApplicationContext(), response.isInRange());

									if (onIBeaconSearchResultCallback != null) {
										onIBeaconSearchResultCallback
												.onIBeaconSearchResult(OnIBeaconSearchResultCallback.SEARCH_RESULT_TABLE_BEACON_FOUND);
										onIBeaconSearchResultCallback = null;
									}

									if (onSubscribeIBeaconSearchResultCallback != null)
										onSubscribeIBeaconSearchResultCallback
												.onIBeaconSearchResult(OnIBeaconSearchResultCallback.SEARCH_RESULT_TABLE_BEACON_FOUND);
								}
							});

					VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(tableBeaconRequest);

					dataManager.setStoreMajor(getApplicationContext(), String.valueOf(storeBeacon.major));
					dataManager.setStoreMinor(getApplicationContext(), String.valueOf(storeBeacon.minor));
					dataManager.setTableMajor(getApplicationContext(), tableMajor);
					dataManager.setTableMinor(getApplicationContext(), tableMinor);

					return;

				}

				dataManager.setStoreMajor(getApplicationContext(), String.valueOf(storeBeacon.major));
				dataManager.setStoreMinor(getApplicationContext(), String.valueOf(storeBeacon.minor));
				dataManager.setTableMajor(getApplicationContext(), tableMajor);
				dataManager.setTableMinor(getApplicationContext(), tableMinor);
				dataManager.setIsInRangeOfTableBeacon(getApplicationContext(), false);

			} else {
				result = OnIBeaconSearchResultCallback.SEARCH_RESULT_BEACON_NOT_FOUND;

				dataManager.setStoreMajor(getApplicationContext(), String.valueOf(""));
				dataManager.setStoreMinor(getApplicationContext(), String.valueOf(""));
				dataManager.setTableMajor(getApplicationContext(), String.valueOf(""));
				dataManager.setTableMinor(getApplicationContext(), String.valueOf(""));
				dataManager.setIsInRangeOfTableBeacon(getApplicationContext(), false);
			}
		}

		if (onIBeaconSearchResultCallback != null) {
			onIBeaconSearchResultCallback.onIBeaconSearchResult(result);
			onIBeaconSearchResultCallback = null;
		}

		if (onSubscribeIBeaconSearchResultCallback != null)
			onSubscribeIBeaconSearchResultCallback.onIBeaconSearchResult(result);
	}

}