package com.usemenu.MenuAndroidApplication.activities;

import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.usemenu.MenuAndroidApplication.R;
import com.usemenu.MenuAndroidApplication.app.Menu;
import com.usemenu.MenuAndroidApplication.dataclasses.Category;
import com.usemenu.MenuAndroidApplication.dataclasses.Item;
import com.usemenu.MenuAndroidApplication.utils.Settings;
import com.usemenu.MenuAndroidApplication.utils.Utils;
import com.usemenu.MenuAndroidApplication.views.CategoryMealItemView;
import com.usemenu.MenuAndroidApplication.volley.VolleySingleton;
import com.usemenu.MenuAndroidApplication.volley.requests.GetAllItemsInCategoryRequest;
import com.usemenu.MenuAndroidApplication.volley.requests.GetCategoriesRequest;
import com.usemenu.MenuAndroidApplication.volley.responses.GetAllItemsInCategoryResponse;
import com.usemenu.MenuAndroidApplication.volley.responses.GetCategoriesResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by milos on 8/10/15.
 */
public class CategoryMealsActivityNew extends BaseActivity {


    public static final String INTENT_EXTRA_CATEGORY_TAG = "category_tag";

    // VIEWS
    private GridView mCategoryGridView;
    private LinearLayout categoriesContainer;
    // DATA
    private ArrayList<Category> categories;
    private ArrayList<Item> items;
    private int width = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_meals_new);

        initActionBar();

        Map<String, String> params = new HashMap<String, String>();
        params.put("major", Settings.getStoreMajor(CategoryMealsActivityNew.this));
        params.put("minor", Settings.getStoreMinor(CategoryMealsActivityNew.this));
        params.put("lang", "en");

        GetCategoriesRequest request = new GetCategoriesRequest(this, params, new Response.Listener<GetCategoriesResponse>() {

            @Override
            public void onResponse(GetCategoriesResponse categories) {
                CategoryMealsActivityNew.this.categories = new ArrayList<Category>(Arrays.asList(categories.categories));
                Menu.getInstance().getDataManager().setCategoriesList(CategoryMealsActivityNew.this.categories);
                callSubCategoryByCatId();
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
        categoriesContainer = (LinearLayout) findViewById(R.id.categoriesContainer);
        for (Category category : categories) {
            List<Item> categoryItems = new ArrayList<Item>();

            for (Item item : CategoryMealsActivityNew.this.items) {
                if (category.name.equalsIgnoreCase(item.category) && item.image.equalsIgnoreCase("subcat"))
                    categoryItems.add(item);
            }

            CategoryMealItemView v = new CategoryMealItemView(this, category, categoryItems,CategoryMealsActivityNew.this.items,categories);
            categoriesContainer.addView(v);
        }

    }

    private void initActionBar() {
        setActionBarBackButtonVisibility(View.INVISIBLE);
        setActionBarForwardButtonText(R.string.action_bar_checkout);

        setActionBarForwardButtonOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Menu.getInstance().getDataManager().getCheckoutList() != null)
                    startActivity(new Intent(getApplicationContext(), CheckoutActivity.class));
                else
                    showAlertDialog("Alert", "Your checkout list is empty. Add some things to Your chart.");
            }
        });

        setActionBarBackButtonOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
    }

    private void callSubCategoryByCatId() {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... parameters) {
                final CountDownLatch countDownLatch = new CountDownLatch(categories.size());

                for (Category category : categories) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("major", Settings.getStoreMajor(CategoryMealsActivityNew.this));
                    params.put("minor", Settings.getStoreMinor(CategoryMealsActivityNew.this));
                    params.put("lang", "en");
                    params.put("cat", String.valueOf(category.tag));

                    GetAllItemsInCategoryRequest request = new GetAllItemsInCategoryRequest(CategoryMealsActivityNew.this, params, new Response.Listener<GetAllItemsInCategoryResponse>() {

                        @Override
                        public void onResponse(GetAllItemsInCategoryResponse items) {
                            boolean isLetched = false;
                            if (items.errordata != null && items.errordata.equals("none")) {
                                Log.e("onResponse", "response of meal sub item");
                                if (CategoryMealsActivityNew.this.items == null)
                                    CategoryMealsActivityNew.this.items = new ArrayList<Item>();
                                if (items != null && items.items != null)
                                    CategoryMealsActivityNew.this.items.addAll(new ArrayList<Item>(Arrays.asList(items.items)));
                                countDownLatch.countDown();
                                isLetched = true;
                            }
                            if (!isLetched)
                                countDownLatch.countDown();
                        }
                    });

                    VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
                }

                boolean success = false;
                try {
                    success = countDownLatch.await(20000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return success;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    initViews();
                    dismissProgressDialog();
                } else {
                    dismissProgressDialog();
                    showAlertDialog(getString(R.string.dialog_body_default_error_title),
                            getString(R.string.dialog_body_default_error_message));
                }
            }
        }.execute();

    }
}