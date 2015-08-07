package com.usemenu.MenuAndroidApplication.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.usemenu.MenuAndroidApplication.R;
import com.usemenu.MenuAndroidApplication.app.Menu;
import com.usemenu.MenuAndroidApplication.dataclasses.Category;
import com.usemenu.MenuAndroidApplication.utils.Settings;
import com.usemenu.MenuAndroidApplication.utils.Utils;
import com.usemenu.MenuAndroidApplication.volley.VolleySingleton;
import com.usemenu.MenuAndroidApplication.volley.requests.GetCategoriesRequest;
import com.usemenu.MenuAndroidApplication.volley.responses.GetCategoriesResponse;

/**
 * Activity for presenting all meal categories from one restaurant. <br>
 * Categories are sorted, and implemented into grid view list.
 * 
 * @author s7Design
 *
 */
public class CategoryMealsActivity extends BaseActivity {

	public static final String INTENT_EXTRA_CATEGORY_TAG = "category_tag";

	// VIEWS
	private GridView mCategoryGridView;
	// DATA
	private ArrayList<Category> categories;

	private int width = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_meals);

		initActionBar();

		Map<String, String> params = new HashMap<String, String>();
		params.put("major", Settings.getStoreMajor(CategoryMealsActivity.this));
		params.put("minor", Settings.getStoreMinor(CategoryMealsActivity.this));
		params.put("lang", "en");

		GetCategoriesRequest request = new GetCategoriesRequest(this, params, new Listener<GetCategoriesResponse>() {

			@Override
			public void onResponse(GetCategoriesResponse categories) {

				CategoryMealsActivity.this.categories = new ArrayList<Category>(Arrays.asList(categories.categories));
				Menu.getInstance().getDataManager().setCategoriesList(CategoryMealsActivity.this.categories);

				initViews();
				dismissProgressDialog();

			}
		});

		showProgressDialogLoading();
		VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = (int) (size.x - Utils.convertDpToPixel(45, this)) / 2;

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (Menu.getInstance().getDataManager().isCheckoutListEmpty())
			hideRightActionBarButton();
		else
			showRightActionBarButton();
	}

	/**
	 * Method for initializing all views in {@link CategoryMealsActivity}
	 */
	private void initViews() {

		mCategoryGridView = (GridView) findViewById(R.id.gridviewCategoryMealsActivity);
		mCategoryGridView.setAdapter(new CategoriesAdapter(CategoryMealsActivity.this));

		mCategoryGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(Menu.getContext(), OrderMealsActivity.class);
				intent.putExtra(INTENT_EXTRA_CATEGORY_TAG, categories.get(position).tag);
				startActivity(intent);
			}
		});

	}

	private void initActionBar() {
		setActionBarBackButtonVisibility(View.INVISIBLE);
		setActionBarForwardButtonText(R.string.action_bar_checkout);

		setActionBarForwardButtonOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Menu.getInstance().getDataManager().getCheckoutList() != null)
					startActivity(new Intent(getApplicationContext(), CheckoutActivity.class));
				else
					showAlertDialog("Alert", "Your checkout list is empty. Add some things to Your chart.");
			}
		});

		setActionBarBackButtonOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
	}

	class CategoriesAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private ImageLoader imageLoader;

		public CategoriesAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
			imageLoader = VolleySingleton.getInstance(getApplicationContext()).getImageLoader();
		}

		@Override
		public int getCount() {
			return categories.size();
		}

		@Override
		public Category getItem(int position) {
			return categories.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {

				convertView = mInflater.inflate(R.layout.grid_item_meals_category, null);

				ViewHolder holder = new ViewHolder();

				holder.categoryImage = (NetworkImageView) convertView.findViewById(R.id.imageviewCategoryMealsGridViewItemImage);
				holder.categoryTitle = (TextView) convertView.findViewById(R.id.textviewCategoryMealsGridViewItemTitle);

				convertView.setTag(holder);
			}

			ViewHolder holder = (ViewHolder) convertView.getTag();

			RelativeLayout.LayoutParams params = (LayoutParams) holder.categoryImage.getLayoutParams();
			params.width = width;
			params.height = width;
			holder.categoryImage.setLayoutParams(params);

			holder.categoryImage.setImageUrl(getItem(position).image, imageLoader);
			holder.categoryImage.setDefaultImageResId(R.drawable.no_image);
			holder.categoryImage.setErrorImageResId(R.drawable.no_image);

			holder.categoryTitle.setText(getItem(position).name);

			return convertView;
		}

		class ViewHolder {
			TextView categoryTitle;
			NetworkImageView categoryImage;
		}

	}

}
