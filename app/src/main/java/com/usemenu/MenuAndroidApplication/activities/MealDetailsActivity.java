package com.usemenu.MenuAndroidApplication.activities;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.android.volley.toolbox.NetworkImageView;
import com.usemenu.MenuAndroidApplication.R;
import com.usemenu.MenuAndroidApplication.app.Menu;
import com.usemenu.MenuAndroidApplication.dataclasses.Item;
import com.usemenu.MenuAndroidApplication.utils.Settings;
import com.usemenu.MenuAndroidApplication.utils.Utils;
import com.usemenu.MenuAndroidApplication.views.AddCommentView;
import com.usemenu.MenuAndroidApplication.views.CircleButtonView;
import com.usemenu.MenuAndroidApplication.volley.VolleySingleton;
import com.usemenu.MenuAndroidApplication.volley.requests.GetItemInfoRequest;
import com.usemenu.MenuAndroidApplication.volley.responses.GetItemInfoResponse;

public class MealDetailsActivity extends BaseActivity {

	private static final String TAG = MealDetailsActivity.class.getSimpleName();

	public static final String INTENT_EXTRA_TAG = "tag";
	public static final String INTENT_EXTRA_NAME = "name";

	// VIEWS
	private AddCommentView addcommentview;
	private NetworkImageView mMealImageImageView;
	private TextView mMealDescriptionTextView;
	private TextView mMealReceiptTextView;
	private TextView mMealIngridientsTextView;
	private TextView mOrderLargePriceTextView;
	private TextView mOrderSmallPriceTextView;
	private TextView mOrderLargeQuantityTextView;
	private TextView mOrderSmallQuantityTextView;
	private TextView mReceiptTextView;
	private ImageButton mGetReceiptImageButton;
	private CircleButtonView circleButtonViewMinusLarge;
	private CircleButtonView circleButtonViewPlusLarge;
	private CircleButtonView circleButtonViewMinusSmall;
	private CircleButtonView circleButtonViewPlusSmall;
	private TextView textViewLabelLarge;
	private TextView textViewLabelSmall;
	private View viewSeparator;
	private RelativeLayout layoutSmall;
	private ScrollView mScrollViewGlobalContainer;

	private Item item;

	private String currency;

	private int itemTag;
	private String name;

	// DATA

	View.OnClickListener onAddCommentCLicked = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (addcommentview.getComment() != null && !addcommentview.getComment().isEmpty()) {
				item.comment = addcommentview.getComment();
				addcommentview.setVisibility(View.GONE);
				try {
					Utils.hideSoftKeyboard(v, MealDetailsActivity.this);
				} catch (Exception e) {
					e.printStackTrace();
				}
				addcommentview.clearComment();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meal_details);

		itemTag = getIntent().getIntExtra(INTENT_EXTRA_TAG, 0);
		name = getIntent().getStringExtra(INTENT_EXTRA_NAME);

		initActionBar();
		initViews();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (item != null) {
			setData();
		}

		if (Menu.getInstance().getDataManager().isCheckoutListEmpty())
			hideRightActionBarButton();
		else
			showRightActionBarButton();
	}

	@Override
	protected void onPause() {
		super.onPause();
		try {
			Utils.hideSoftKeyboard(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initActionBar() {
		setActionBarForwardButtonText(getResources().getString(R.string.action_bar_checkout));
		setActionBarBackButtonText(getResources().getString(R.string.action_bar_back));

		setActionBarBackButtonOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		setActionBarForwardButtonOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (Item item : Menu.getInstance().getDataManager().getCheckoutList()) {
					Log.e("InitData", "Item= " + item.toString());
				}
				startActivity(new Intent(getApplicationContext(), CheckoutActivity.class));
			}
		});

	}

	private void initViews() {

		mScrollViewGlobalContainer = (ScrollView) findViewById(R.id.scrollViewMealDetailsContainer);
		addcommentview = (AddCommentView) findViewById(R.id.addCommentView);
		mMealImageImageView = (NetworkImageView) findViewById(R.id.imageviewMealsDetailsActivity);
		mMealDescriptionTextView = (TextView) findViewById(R.id.textviewMealDetailsActivityDescription);
		mMealReceiptTextView = (TextView) findViewById(R.id.textviewMealDetailsActivityReceiptText);
		mMealIngridientsTextView = (TextView) findViewById(R.id.textviewMealDetailsActivityIngridients);
		mOrderLargePriceTextView = (TextView) findViewById(R.id.textviewMealDetailsActivityOrderLargePrice);
		mOrderSmallPriceTextView = (TextView) findViewById(R.id.textviewMealDetailsActivityOrderSmallPrice);
		mOrderLargeQuantityTextView = (TextView) findViewById(R.id.textviewMealDetailsActivityOrderLargeQuantity);
		mOrderSmallQuantityTextView = (TextView) findViewById(R.id.textviewMealDetailsActivityOrderSmallQuantity);
		circleButtonViewMinusLarge = (CircleButtonView) findViewById(R.id.circleButtonViewMinusLarge);
		circleButtonViewPlusLarge = (CircleButtonView) findViewById(R.id.circleButtonViewPlusLarge);
		circleButtonViewMinusSmall = (CircleButtonView) findViewById(R.id.circleButtonViewMinusSmall);
		circleButtonViewPlusSmall = (CircleButtonView) findViewById(R.id.circleButtonViewPlusSmall);
		mGetReceiptImageButton = (ImageButton) findViewById(R.id.imagebuttonMealDetailsActivityGetReceipt);
		textViewLabelLarge = (TextView) findViewById(R.id.tv1);
		textViewLabelSmall = (TextView) findViewById(R.id.tv2);
		viewSeparator = (View) findViewById(R.id.viewSeparator);
		layoutSmall = (RelativeLayout) findViewById(R.id.layoutSmall);

		circleButtonViewMinusLarge.setAsRemoveOrange();
		circleButtonViewPlusLarge.setAsAddOrange();
		circleButtonViewMinusSmall.setAsRemoveGrey();
		circleButtonViewPlusSmall.setAsAddGrey();

		addcommentview.setOnClick(onAddCommentCLicked);

		mGetReceiptImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (addcommentview.getVisibility() == View.GONE) {
					addcommentview.setVisibility(View.VISIBLE);
					addcommentview.requestEditTextFocus();
					Utils.showSoftKeyboard(addcommentview.edittextBody, MealDetailsActivity.this);
				} else {
					addcommentview.setVisibility(View.GONE);
					try {
						Utils.hideSoftKeyboard(v, MealDetailsActivity.this);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = (int) ((float) size.x * 2.f / 3.f);

		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mMealImageImageView.getLayoutParams();
		params.height = width;
		mMealImageImageView.setLayoutParams(params);

		Utils.handleOutsideCommentDialogClick(addcommentview, mScrollViewGlobalContainer, this);
	}

	private void initData() {

		Map<String, String> params = new HashMap<String, String>();
		params.put("major", Settings.getStoreMajor(MealDetailsActivity.this));
		params.put("minor", Settings.getStoreMinor(MealDetailsActivity.this));
		params.put("itemtag", String.valueOf(getIntent().getIntExtra(INTENT_EXTRA_TAG, 0)));
		params.put("lang", "en");

		GetItemInfoRequest request = new GetItemInfoRequest(this, params, new Listener<GetItemInfoResponse>() {

			@Override
			public void onResponse(GetItemInfoResponse response) {

				if (response != null && response.item != null && response.item.length > 0) {
					item = response.item[0];
					mScrollViewGlobalContainer.setVisibility(View.VISIBLE);
					setData();
				}

				dismissProgressDialog();
			}
		});

		showProgressDialogLoading();
		VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

		currency = Menu.getInstance().getDataManager().getCurrency(this);
	}

	private void setData() {

		Item tempItem = Menu.getInstance().getDataManager().getItemByTag(item.largetag);
		if (tempItem != null) {
			// item.quantityLarge = tempItem.quantityLarge;
			// item.quantitySmall = tempItem.quantitySmall;
			tempItem.description = item.description;
			tempItem.ingredients = item.ingredients;
			item = tempItem;
		} else {
			item.quantityLarge = 0;
			item.quantitySmall = 0;
		}

		// "item":[{"category":"BEVERAGES","image":"http:\/\/usemenu.com\/images\/image.png","name":"Mineral
		// Water without Gas","description":"Fresh mineral water from the swiss
		// alps.","ingredients":"SWISS ALPINE
		// WATER","smalltag":"3","largetag":"4","smallprice":"3.50","largeprice":"7.00","smalllabel":"GLASS","largelabel":"BOTTLE","currency":"\u20ac"}],"errordata":"none"}

		Log.e(TAG, "desc " + item.description);
		Log.e(TAG, "ingr " + item.ingredients);

		mMealImageImageView.setDefaultImageResId(R.drawable.no_image);
		mMealImageImageView.setErrorImageResId(R.drawable.no_image);
		mMealImageImageView.setImageUrl(item.image, VolleySingleton.getInstance(getApplicationContext()).getImageLoader());
		mMealDescriptionTextView.setText(name);
		mMealReceiptTextView.setText(item.description);
		mMealIngridientsTextView.setText(item.ingredients);
		mOrderLargePriceTextView.setText(currency + String.format("%.2f", item.largeprice));
		circleButtonViewPlusLarge.setVisibility(View.VISIBLE);
		circleButtonViewPlusSmall.setVisibility(View.VISIBLE);

		Item itemCOL = Menu.getInstance().getDataManager().getItemByTag(itemTag);

		if (itemCOL != null) {

			item.quantityLarge = itemCOL.quantityLarge;
			item.quantitySmall = itemCOL.quantitySmall;

			if (item.quantityLarge > 0) {
				circleButtonViewMinusLarge.setVisibility(View.VISIBLE);
				mOrderLargeQuantityTextView.setVisibility(View.VISIBLE);
				mOrderLargeQuantityTextView.setText(String.valueOf(item.quantityLarge));
			}
			if (item.quantitySmall > 0) {
				circleButtonViewMinusSmall.setVisibility(View.VISIBLE);
				mOrderSmallQuantityTextView.setVisibility(View.VISIBLE);
				mOrderSmallQuantityTextView.setText(String.valueOf(item.quantitySmall));
			}
		} else {
			circleButtonViewMinusLarge.setVisibility(View.INVISIBLE);
			mOrderLargeQuantityTextView.setVisibility(View.INVISIBLE);
			circleButtonViewMinusSmall.setVisibility(View.INVISIBLE);
			mOrderSmallQuantityTextView.setVisibility(View.INVISIBLE);
		}

		textViewLabelLarge.setText(/*
									 * getString(R.string.meal_details_order) +
									 * " " +
									 */item.largelabel);

		circleButtonViewPlusLarge.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (Menu.getInstance().getDataManager().isCheckoutListEmpty())
					showRightActionBarButton();

				if (item.quantityLarge == 0) {
					circleButtonViewMinusLarge.setVisibility(View.VISIBLE);
					mOrderLargeQuantityTextView.setVisibility(View.VISIBLE);
				}
				mOrderLargeQuantityTextView.setText(String.valueOf((item.quantityLarge) + 1));

				Log.e("InitData", "Before= " + item.toString());
				Menu.getInstance().getDataManager().addCheckoutListItem(item.getLarge());
				Log.e("InitData", "After= " + item.toString());

				if (!Menu.getInstance().isOrderEnabled() && (item.quantityLarge + item.quantitySmall < 2))
					showAlertDialog(R.string.dialog_title_warning, R.string.dialog_not_at_menu);
			}
		});

		circleButtonViewMinusLarge.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (item.quantityLarge == 1) {
					circleButtonViewMinusLarge.setVisibility(View.INVISIBLE);
					mOrderLargeQuantityTextView.setVisibility(View.INVISIBLE);
				}
				Menu.getInstance().getDataManager().removeCheckoutListItem(item.largetag);
				mOrderLargeQuantityTextView.setText(String.valueOf((item.quantityLarge)));

				if (Menu.getInstance().getDataManager().isCheckoutListEmpty())
					hideRightActionBarButton();
			}
		});

		if (item.smalllabel.length() > 0) {

			textViewLabelSmall.setText(/*
										 * getString(R.string.meal_details_order)
										 * + " " +
										 */item.smalllabel);
			mOrderSmallPriceTextView.setText(currency + String.format("%.2f", item.smallprice));

			circleButtonViewPlusSmall.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (Menu.getInstance().getDataManager().isCheckoutListEmpty())
						showRightActionBarButton();

					if (item.quantitySmall == 0) {
						circleButtonViewMinusSmall.setVisibility(View.VISIBLE);
						mOrderSmallQuantityTextView.setVisibility(View.VISIBLE);
					}
					mOrderSmallQuantityTextView.setText(String.valueOf((item.quantitySmall) + 1));

					Menu.getInstance().getDataManager().addCheckoutListItem(item.getSmall());

					if (!Menu.getInstance().isOrderEnabled() && (item.quantityLarge + item.quantitySmall < 2))
						showAlertDialog(R.string.dialog_title_warning, R.string.dialog_not_at_menu);
				}
			});

			circleButtonViewMinusSmall.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (item.quantitySmall == 1) {
						circleButtonViewMinusSmall.setVisibility(View.INVISIBLE);
						mOrderSmallQuantityTextView.setVisibility(View.INVISIBLE);
					}
					Menu.getInstance().getDataManager().removeCheckoutListItem(item.smalltag);
					mOrderSmallQuantityTextView.setText(String.valueOf((item.quantitySmall)));

					if (Menu.getInstance().getDataManager().isCheckoutListEmpty())
						hideRightActionBarButton();

				}
			});
		} else {
			layoutSmall.setVisibility(View.GONE);
			viewSeparator.setVisibility(View.GONE);
		}

	}

}