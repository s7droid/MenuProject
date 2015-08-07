package com.usemenu.MenuAndroidApplication.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;

import com.usemenu.MenuAndroidApplication.R;
import com.usemenu.MenuAndroidApplication.app.Menu;
import com.usemenu.MenuAndroidApplication.callbacks.OnIBeaconSearchResultCallback;
import com.usemenu.MenuAndroidApplication.utils.Settings;
import com.usemenu.MenuAndroidApplication.utils.Utils;
import com.usemenu.MenuAndroidApplication.views.MenuButton;

/**
 * Activity for presenting main menu of the <i><b>Menu</b></i> application. <br>
 * In <code>MainMenuActivity</code> you can take actions such are: <br>
 * 
 * <pre>
 * 1)`e menu
 * </pre>
 * 
 * <pre>
 * 2)View Tutorials
 * </pre>
 * 
 * <pre>
 * 3)View past receipts
 * </pre>
 * 
 * <pre>
 * 4)Manage your account
 * </pre>
 * 
 * <pre>
 * 5)Review current order
 * </pre>
 * 
 * @author s7Design
 *
 */
public class MainMenuActivity extends BaseActivity implements OnIBeaconSearchResultCallback {

	// VIEWS
	private MenuButton mVenueMenuButton;
	private Button mTutorialsButton;
	private Button mMenageYourProfileButton;
	private Button mViewPastReceiptsButton;
	private Button mReviewCurrentOrderButton;
	private Button mAboutTheAppButton;

	private View mSeparatorPastReceipts;
	private View mSeparatorManageProfile;

	private boolean isLoggedIn;
	private boolean isOrderListEmpty;
	private boolean isOutsideRestaurant;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		initViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
		isLoggedIn = !Settings.getAccessToken(getApplicationContext()).equals("") ? true : false;
		isOrderListEmpty = Menu.getInstance().getDataManager().getCheckoutList().size() == 0 ? true : false;
		isOutsideRestaurant = !Menu.getInstance().isInARestaurant();

		if (isLoggedIn) {
			mMenageYourProfileButton.setEnabled(true);
			mMenageYourProfileButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.main_menu_manage_account_orange), null,
					null, null);
			mMenageYourProfileButton.setCompoundDrawablePadding((int) Utils.convertDpToPixel(15, MainMenuActivity.this));
			mMenageYourProfileButton.setTextColor(getResources().getColor(R.color.menu_main_gray));

			mViewPastReceiptsButton.setEnabled(true);
			mViewPastReceiptsButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.main_menu_view_receipts_orange), null, null,
					null);
			mViewPastReceiptsButton.setCompoundDrawablePadding((int) Utils.convertDpToPixel(15, MainMenuActivity.this));
			mViewPastReceiptsButton.setTextColor(getResources().getColor(R.color.menu_main_gray));
		} else {
			mMenageYourProfileButton.setEnabled(false);
			mMenageYourProfileButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.main_menu_manage_account_gray), null, null,
					null);
			mMenageYourProfileButton.setCompoundDrawablePadding((int) Utils.convertDpToPixel(15, MainMenuActivity.this));
			mMenageYourProfileButton.setTextColor(Color.parseColor("#999999"));

			mViewPastReceiptsButton.setEnabled(false);
			mViewPastReceiptsButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.main_menu_view_receipts_gray), null, null,
					null);
			mViewPastReceiptsButton.setCompoundDrawablePadding((int) Utils.convertDpToPixel(15, MainMenuActivity.this));
			mViewPastReceiptsButton.setTextColor(Color.parseColor("#999999"));

		}

		if (isOutsideRestaurant) {
			if (Settings.getAccessToken(getApplicationContext()).isEmpty()) {
				mReviewCurrentOrderButton.setText(getResources().getString(R.string.main_menu_log_in_or_sign_up));
				mReviewCurrentOrderButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.main_menu_log_in_sign_up_orange), null,
						null, null);
				mReviewCurrentOrderButton.setCompoundDrawablePadding((int) Utils.convertDpToPixel(15, MainMenuActivity.this));
			} else {
				mReviewCurrentOrderButton.setText(getResources().getString(R.string.main_menu_logout));
				mReviewCurrentOrderButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.main_menu_log_in_sign_up_gray), null,
						null, null);
				mReviewCurrentOrderButton.setCompoundDrawablePadding((int) Utils.convertDpToPixel(15, MainMenuActivity.this));
			}
		} else {
			mReviewCurrentOrderButton.setText(getResources().getString(R.string.main_menu_current_order));
			mReviewCurrentOrderButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.main_menu_view_current_order_gray), null,
					null, null);
			mReviewCurrentOrderButton.setCompoundDrawablePadding((int) Utils.convertDpToPixel(15, MainMenuActivity.this));
		}

		if (isOrderListEmpty) {
			mReviewCurrentOrderButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.main_menu_view_current_order_gray), null,
					null, null);
			mReviewCurrentOrderButton.setCompoundDrawablePadding((int) Utils.convertDpToPixel(15, MainMenuActivity.this));
		} else {
			mReviewCurrentOrderButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.main_menu_view_current_order_orange), null,
					null, null);
			mReviewCurrentOrderButton.setCompoundDrawablePadding((int) Utils.convertDpToPixel(15, MainMenuActivity.this));
		}

		if (Settings.getStoreMajor(MainMenuActivity.this).isEmpty() && Settings.getStoreMinor(MainMenuActivity.this).isEmpty()) {
			mVenueMenuButton.setTextColor(getResources().getColor(R.color.menu_main_gray_light));
			mVenueMenuButton.setText(getResources().getString(R.string.main_menu_category_not_available));
			mVenueMenuButton.setFont(false);
			mVenueMenuButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		} else {
			mVenueMenuButton.setTextColor(getResources().getColor(R.color.menu_main_gray));
			mVenueMenuButton.setText(getResources().getString(R.string.main_menu_view_venue_menu));
			mVenueMenuButton.setFont(true);
			mVenueMenuButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.main_menu_view_menu_orange), null, null, null);
			mVenueMenuButton.setCompoundDrawablePadding((int) Utils.convertDpToPixel(15, MainMenuActivity.this));
		}

		if (isOrderListEmpty) {
			if (Settings.getAccessToken(getApplicationContext()).isEmpty()) {
				mReviewCurrentOrderButton.setText(getResources().getString(R.string.main_menu_log_in_or_sign_up));
				mReviewCurrentOrderButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.main_menu_log_in_sign_up_orange), null,
						null, null);
				mReviewCurrentOrderButton.setCompoundDrawablePadding((int) Utils.convertDpToPixel(15, MainMenuActivity.this));
			} else {
				mReviewCurrentOrderButton.setText(getResources().getString(R.string.main_menu_logout));
				mReviewCurrentOrderButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.main_menu_log_in_sign_up_gray), null,
						null, null);
				mReviewCurrentOrderButton.setCompoundDrawablePadding((int) Utils.convertDpToPixel(15, MainMenuActivity.this));
			}
		}

		Menu.getInstance().subscribeIBeaconSearchResultCallback(this);

	}

	@Override
	protected void onPause() {
		super.onPause();

		Menu.getInstance().unsubscribeIBeaconSearchResultCallback();
	}

	/**
	 * Method for initalizing all views in class.
	 */
	private void initViews() {

		isLoggedIn = !Settings.getAccessToken(getApplicationContext()).equals("") ? true : false;
		isOrderListEmpty = Menu.getInstance().getDataManager().getCheckoutList().size() == 0 ? true : false;
		isOutsideRestaurant = !Menu.getInstance().isInARestaurant();

		mVenueMenuButton = (MenuButton) findViewById(R.id.buttonMainMenuActivityViewVenue);
		mTutorialsButton = (Button) findViewById(R.id.buttonMainMenuActivityViewTutorial);
		mMenageYourProfileButton = (Button) findViewById(R.id.buttonMainMenuActivityManageYourAccount);
		mViewPastReceiptsButton = (Button) findViewById(R.id.buttonMainMenuActivityViewPastReceipts);
		mReviewCurrentOrderButton = (Button) findViewById(R.id.buttonMainMenuActivityCurrentOrderPreview);
		mAboutTheAppButton = (Button) findViewById(R.id.buttonMainMenuActivityAboutTheApp);

		mSeparatorManageProfile = (View) findViewById(R.id.separator4);
		mSeparatorPastReceipts = (View) findViewById(R.id.separator5);

		setActionBarForwardButtonvisibility(View.VISIBLE);
		setActionBarForwardArrowVisibility(null);
		setActionBarForwardButtonText(getResources().getString(R.string.action_bar_main_menu));
		setActionBarForwardButtonTextColor(getResources().getColor(R.color.menu_main_orange));

		setActionBarMenuButtonVisibility(View.INVISIBLE);

		setActionBarBackButtonText(R.string.action_bar_back);
		setActionBarBackButtonOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		setActionBarMenuButtonOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		mVenueMenuButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				venuMenuButtonAction();
			}

		});

		mTutorialsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tutorialsButtonAction();
			}

		});

		mMenageYourProfileButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				manageYourProfileButtonAction();
			}

		});

		mViewPastReceiptsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewPastReceiptsButtonAction();
			}

		});

		mReviewCurrentOrderButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isOrderListEmpty && !isOutsideRestaurant)
					reviewCurrentOrderButtonAction();

				if (isOutsideRestaurant) {
					if (Settings.getAccessToken(getApplicationContext()).isEmpty()) {
						loginOrSignUp();
						return;
					} else {
						logout();
						return;
					}
				}

				if (isOrderListEmpty) {
					if (Settings.getAccessToken(getApplicationContext()).isEmpty()) {
						loginOrSignUp();
						return;
					} else {
						logout();
						return;
					}
				}
			}

		});

		mAboutTheAppButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				aboutTheAppButtonAction();
			}
		});

		mReviewCurrentOrderButton.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				Settings.setAccessToken(MainMenuActivity.this, "asdfkjuhgasdifgsadkfghksjfhgskjhgksfj");

				return false;
			}
		});
	}

	private void venuMenuButtonAction() {
		if (!Settings.getStoreMajor(MainMenuActivity.this).isEmpty() && !Settings.getStoreMinor(MainMenuActivity.this).isEmpty())
			startActivity(new Intent(getApplicationContext(), CategoryMealsActivity.class));

	}

	private void tutorialsButtonAction() {
		startActivity(new Intent(getApplicationContext(), TutorialFirstActivity.class));
	}

	private void manageYourProfileButtonAction() {
		startActivity(new Intent(getApplicationContext(), ManageAccountActivity.class));
	}

	private void viewPastReceiptsButtonAction() {
		startActivity(new Intent(getApplicationContext(), ReceiptListActivity.class));
	}

	private void reviewCurrentOrderButtonAction() {
		if (Menu.getInstance().getDataManager().getCheckoutList() != null)
			startActivity(new Intent(getApplicationContext(), CheckoutActivity.class));
		else
			showAlertDialog("Alert", "Your checkout list is empty. Add some things to Your chart.");
	}

	private void loginOrSignUp() {
		startActivity(new Intent(getApplicationContext(), SignInActivity.class));
	}

	private void logout() {
		Settings.setAccessToken(getApplicationContext(), null);
		startActivity(new Intent(getApplicationContext(), SplashActivity.class));
	}

	private void aboutTheAppButtonAction() {
		startActivity(new Intent(getApplicationContext(), AboutTheAppActivity.class));
	}

	@Override
	public void onIBeaconSearchResult(int result) {

		if (Settings.getStoreMajor(MainMenuActivity.this).isEmpty() && Settings.getStoreMinor(MainMenuActivity.this).isEmpty()) {
			mVenueMenuButton.setTextColor(getResources().getColor(R.color.menu_main_gray_light));
			mVenueMenuButton.setText(getResources().getString(R.string.main_menu_category_not_available));
			mVenueMenuButton.setFont(false);
			mVenueMenuButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		} else {
			mVenueMenuButton.setTextColor(getResources().getColor(R.color.menu_main_gray));
			mVenueMenuButton.setText(getResources().getString(R.string.main_menu_view_venue_menu));
			mVenueMenuButton.setFont(true);
			mVenueMenuButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.main_menu_view_menu_orange), null, null, null);
			mVenueMenuButton.setCompoundDrawablePadding((int) Utils.convertDpToPixel(15, MainMenuActivity.this));
		}
	}

}