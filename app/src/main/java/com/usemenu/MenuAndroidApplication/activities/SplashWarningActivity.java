package com.usemenu.MenuAndroidApplication.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.usemenu.MenuAndroidApplication.R;
import com.usemenu.MenuAndroidApplication.utils.Utils;

import static com.usemenu.MenuAndroidApplication.app.Constants.*;

public class SplashWarningActivity extends BaseActivity {

    private ImageView imageView;
    private TextView textViewBody;
    private TextView textViewDesc;
    private LinearLayout layoutContainer;
    private Button buttonEnableLocationServices;

    private boolean isBluetoothEnabled = false;
    private boolean isLocationEnabled = false;

    private GestureDetector gestureDetector;
    private View.OnTouchListener gestureListener;

    private final BroadcastReceiver bluetoothStateChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
//	            case BluetoothAdapter.STATE_OFF:
//	                setButtonText("Bluetooth off");
//	                break;
//	            case BluetoothAdapter.STATE_TURNING_OFF:
//	                setButtonText("Turning Bluetooth off...");
//	                break;
                    case BluetoothAdapter.STATE_ON:
//	                setButtonText("Bluetooth on");
                        System.err.println("Bluetooth is ON");
                        isBluetoothEnabled = true;
                        finish();
                        break;
//	            case BluetoothAdapter.STATE_TURNING_ON:
//	                setButtonText("Turning Bluetooth on...");
//	                break;
                }
            }
        }
    };

    private final BroadcastReceiver locationStateChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals("android.location.PROVIDERS_CHANGED")) {
                if (Utils.isLocationEnabled(SplashWarningActivity.this)) {
                    finish();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_warning);
        initViews();
        IntentFilter bluetoothFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bluetoothStateChangeReceiver, bluetoothFilter);
        IntentFilter locationFilter = new IntentFilter("android.location.PROVIDERS_CHANGED");
        registerReceiver(locationStateChangeReceiver, locationFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bluetoothStateChangeReceiver);
        unregisterReceiver(locationStateChangeReceiver);
    }

    private void initViews() {

        imageView = (ImageView) findViewById(R.id.imageView);
        textViewBody = (TextView) findViewById(R.id.textViewBody);
        textViewDesc = (TextView) findViewById(R.id.textViewDesc);
        layoutContainer = (LinearLayout) findViewById(R.id.layoutContainer);
        buttonEnableLocationServices = (Button) findViewById(R.id.buttonEnableLocationServices);

        int start = getIntent().getIntExtra(INTENT_EXTRA_TAG_START, 0);

        setActionBarForwardButtonvisibility(View.INVISIBLE);
        setActionBarBackButtonOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });

        if (start == INTENT_EXTRA_START_LOCATION) {

            imageView.setImageResource(R.drawable.notification_location_icon);

            String bold1 = getString(R.string.splash_screen_location_disabled_body_1);
            SpannableStringBuilder ssb1 = new SpannableStringBuilder(bold1 + " " + getString(R.string.splash_screen_location_disabled_body_2));

            final StyleSpan bss1 = new StyleSpan(android.graphics.Typeface.BOLD);
            ssb1.setSpan(bss1, 0, bold1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            textViewBody.setText(ssb1);

            String normal = getString(R.string.splash_screen_location_disabled_desc_1);
            String bold2 = getString(R.string.splash_screen_location_disabled_desc_2);
            SpannableStringBuilder ssb2 = new SpannableStringBuilder(normal + " " + bold2 + " " + getString(R.string.splash_screen_location_disabled_desc_3));

            final StyleSpan bss2 = new StyleSpan(android.graphics.Typeface.BOLD);
            ssb2.setSpan(bss2, normal.length(), normal.length() + bold2.length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            textViewDesc.setText(ssb2);

            buttonEnableLocationServices.setVisibility(View.VISIBLE);
            buttonEnableLocationServices.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });

        } else if (start == INTENT_EXTRA_START_BLUETOOTH) {

            imageView.setImageResource(R.drawable.notification_bluetooth_icon);

            String bold1 = getString(R.string.splash_screen_bluetooth_disabled_body_1);
            SpannableStringBuilder ssb1 = new SpannableStringBuilder(bold1 + " " + getString(R.string.splash_screen_bluetooth_disabled_body_2));

            final StyleSpan bss1 = new StyleSpan(android.graphics.Typeface.BOLD);
            ssb1.setSpan(bss1, 0, bold1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            textViewBody.setText(ssb1);

            String normal = getString(R.string.splash_screen_bluetooth_disabled_desc_1);
            String bold2 = getString(R.string.splash_screen_bluetooth_disabled_desc_2);
            SpannableStringBuilder ssb2 = new SpannableStringBuilder(normal + " " + bold2);

            final StyleSpan bss2 = new StyleSpan(android.graphics.Typeface.BOLD);
            ssb2.setSpan(bss2, normal.length(), normal.length() + bold2.length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            textViewDesc.setText(ssb2);

            gestureDetector = new GestureDetector(this, new MenuGestureDetector());
            gestureListener = new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            };

            layoutContainer.setOnTouchListener(gestureListener);
        }
    }

    class MenuGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    Intent settingsIntent = new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                    startActivity(settingsIntent);
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }

}
