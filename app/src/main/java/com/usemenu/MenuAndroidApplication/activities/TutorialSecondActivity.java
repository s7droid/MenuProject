package com.usemenu.MenuAndroidApplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.usemenu.MenuAndroidApplication.R;
import com.usemenu.MenuAndroidApplication.app.Menu;
import com.usemenu.MenuAndroidApplication.utils.Settings;

/**
 * Activity for showing second screen of the two screens tutorial. <br>
 * This activity is response for completing tutorial, and for transfering user to
 * 
 * @author s7Design
 *
 */
public class TutorialSecondActivity extends BaseActivity {

	// VIEWS
	private Button mMakeOrder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial_screen_second);
		hideActionBar();
		initViews();
	}

	/**
	 * Method for initializing all views in {@link TutorialSecondActivity}
	 */
	private void initViews() {
		mMakeOrder = (Button) findViewById(R.id.buttonuTutorialScreenSecondContinueButton);

		if (!Menu.getInstance().isInARestaurant())
			mMakeOrder.setText(R.string.menu_tutorial_screen_button_awesome);

		mMakeOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!Settings.getStoreMajor(TutorialSecondActivity.this).isEmpty() && !Settings.getStoreMinor(TutorialSecondActivity.this).isEmpty()) {
					Intent intent = new Intent(getApplicationContext(), CategoryMealsActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);

				}
				finish();
			}
		});
	}
}
