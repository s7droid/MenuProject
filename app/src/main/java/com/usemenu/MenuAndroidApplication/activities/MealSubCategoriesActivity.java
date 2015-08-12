package com.usemenu.MenuAndroidApplication.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.usemenu.MenuAndroidApplication.R;
import com.usemenu.MenuAndroidApplication.dataclasses.Category;
import com.usemenu.MenuAndroidApplication.dataclasses.Item;

import java.util.ArrayList;
import java.util.List;

import static com.usemenu.MenuAndroidApplication.app.Constants.KEY_CATEGORIES;
import static com.usemenu.MenuAndroidApplication.app.Constants.KEY_ITEMS;

/**
 * Created by milos on 8/11/15.
 */
public class MealSubCategoriesActivity extends BaseActivity {

    private ViewPager viewPager;
    private SubCategoriesAdapter adapter;
    private List<Category> categories;
    private List<Item> allCategoryItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPager = new ViewPager(this);
        setContentView(viewPager);
        initData();
    }

    private void initData() {
        categories = (List<Category>) getIntent().getSerializableExtra(KEY_CATEGORIES);
        allCategoryItems = (List<Item>) getIntent().getSerializableExtra(KEY_ITEMS);

        adapter = new SubCategoriesAdapter(this, categories, allCategoryItems);
        viewPager.setAdapter(adapter);
    }

    private class SubCategoriesAdapter extends PagerAdapter {

        private Context context;
        private LayoutInflater inflater;
        private List<Category> categories;
        private List<Item> allItems;
        private List<MealSubCategory> subCategoryItems;
        private int counter = 0;

        public SubCategoriesAdapter(Context context, List<Category> categories, List<Item> allItems) {
            this.context = context;
            this.categories = categories;
            this.allItems = allItems;
            inflater = LayoutInflater.from(context);

            subCategoryItems = new ArrayList<MealSubCategory>();

            for (Item items : allItems) {

                if (items.image.equals("subcat"))
                    counter++;

//                for (Item item : CategoryMealsActivityNew.this.items) {
//                    if (category.name.equalsIgnoreCase(item.category) && item.image.equalsIgnoreCase("subcat"))
//                        categoryItems.add(item);
//                }
//
//                CategoryMealItemView v = new CategoryMealItemView(this, category, categoryItems, CategoryMealsActivityNew.this.items);
//                categoriesContainer.addView(v);
            }

        }

        @Override
        public int getCount() {
            return counter;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LinearLayout layout = new LinearLayout(context);
            final RelativeLayout content = (RelativeLayout) inflater.inflate(R.layout.tutorial_pager_item, null);


            return layout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    private class MealSubCategory {
        public List<Item> items;
    }

}
