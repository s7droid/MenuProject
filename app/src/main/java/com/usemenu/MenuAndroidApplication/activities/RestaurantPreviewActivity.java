package com.usemenu.MenuAndroidApplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.usemenu.MenuAndroidApplication.R;
import com.usemenu.MenuAndroidApplication.app.Menu;
import com.usemenu.MenuAndroidApplication.dataclasses.Rate;
import com.usemenu.MenuAndroidApplication.volley.VolleySingleton;
import com.usemenu.MenuAndroidApplication.volley.responses.GetRestaurantInfoResponse;

/**
 * Welcome activity. This activity contains data about current restaurant. From this activity user can go to tutorials, or see menu of the restaurant, and make
 * an order. <br>
 * In order to enter this activity, user first must satisfied several conditions, such are: Internet and bluetooth connection enabled, and some several
 * conditions, which are checked in {@link SplashActivity}.
 * 
 * @author s7Design
 *
 */
public class RestaurantPreviewActivity extends BaseActivity {

	// VIEWS
	private Button mMakeOrderButton;
	private TextView mRestaurantName;
	private NetworkImageView imageViewRestaurant;

	private GetRestaurantInfoResponse restaurantInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_preview);
		restaurantInfo = Menu.getInstance().getDataManager().getRestaurantInfo(this);
		initViews();
	}

	/**
	 * Method for initializing all views in {@link RestaurantPreviewActivity}.
	 */
	private void initViews() {

		mMakeOrderButton = (Button) findViewById(R.id.buttonRestaurantPreviewActivityMakeOrder);
		mRestaurantName = (TextView) findViewById(R.id.textviewRestaurantPreviewActivityRestaurantName);
		imageViewRestaurant = (NetworkImageView) findViewById(R.id.imageViewRestaurantPreviewActivityRestaurantImage);

		imageViewRestaurant.setImageUrl(restaurantInfo.imageurl, VolleySingleton.getInstance(getApplicationContext()).getImageLoader());
		imageViewRestaurant.setDefaultImageResId(R.drawable.no_image);
		imageViewRestaurant.setErrorImageResId(R.drawable.no_image);

		mRestaurantName.setText(restaurantInfo.restaurantname);

		setActionBarBackButtonVisibility(View.INVISIBLE);
		setActionBarForwardButtonText(R.string.action_bar_tutorial);
		mMakeOrderButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(RestaurantPreviewActivity.this, CategoryMealsActivity.class));
			}
		});

		setActionBarForwardButtonOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(RestaurantPreviewActivity.this, TutorialFirstActivity.class));
			}
		});

		Rate rate = new Rate(this);

		System.out.println("Min tip= " + rate.mintip + "\nMax tip= " + rate.maxtip + "\nTax= " + rate.tax);

	}
}
