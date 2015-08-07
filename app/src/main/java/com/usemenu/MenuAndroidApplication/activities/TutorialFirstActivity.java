package com.usemenu.MenuAndroidApplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.usemenu.MenuAndroidApplication.R;

/**
 * Activity for showing first screen of the two screens tutorial. <br>
 * This activity doesn't have too much things to handle, except translation to
 * other tutorial screen activity, which is {@linkplain TutorialSecondActivity}.
 * 
 * @author s7Design
 *
 */
public class TutorialFirstActivity extends BaseActivity {

	private static final String TAG = TutorialFirstActivity.class.getSimpleName();

	// VIEWS
	private Button mContinue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial_screen_first);
		hideActionBar();
		initViews();
	}

	/**
	 * Method for initializing all views in {@link TutorialFirstActivity}
	 */
	private void initViews() {
		mContinue = (Button) findViewById(R.id.buttonuTutorialScreenFirstContinueButton);

		mContinue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				startActivity(new Intent(getApplicationContext(), TutorialSecondActivity.class));
				finish();
				
			}
		});
	}

}
