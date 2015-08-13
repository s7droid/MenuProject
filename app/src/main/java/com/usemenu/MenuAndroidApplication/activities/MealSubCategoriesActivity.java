package com.usemenu.MenuAndroidApplication.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.usemenu.MenuAndroidApplication.R;
import com.usemenu.MenuAndroidApplication.app.Constants;
import com.usemenu.MenuAndroidApplication.dataclasses.Item;
import com.usemenu.MenuAndroidApplication.views.CategoryMealItemView;
import com.usemenu.MenuAndroidApplication.volley.VolleySingleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milos on 8/11/15.
 */
public class MealSubCategoriesActivity extends BaseActivity {

    private ViewPager viewPager;
    private SubCategoriesAdapter adapter;
    private ArrayList<Item> allCategoryItems;
    private ArrayList<MealSubCategory> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPager = new ViewPager(this);
        setContentView(viewPager);
        initData();
    }

    private void initData() {
        CategoryMealItemView.BundleItem it = new Gson().fromJson(getIntent().getStringExtra(Constants.KEY_ITEMS), CategoryMealItemView.BundleItem.class);
        allCategoryItems = it.items;

        items = getListOfMealSubCategories();

        adapter = new SubCategoriesAdapter(this, items);
        viewPager.setAdapter(adapter);

        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    private ArrayList<MealSubCategory> getListOfMealSubCategories() {
        ArrayList<MealSubCategory> list = new ArrayList<MealSubCategory>();

        for (int i = 0; i < allCategoryItems.size(); ++i) {
            if (i == 0) {
                MealSubCategory mealSub = new MealSubCategory();
                list.add(mealSub);
            }

            if (i != 0) {
                if (!allCategoryItems.get(i).image.equalsIgnoreCase("subcat")) {
                    List<Item> itemsList = list.get(list.size() - 1).items;
                    itemsList.add(allCategoryItems.get(i));
                } else if (allCategoryItems.get(i).image.equalsIgnoreCase("subcat")) {
                    MealSubCategory mealSub = new MealSubCategory();
                    list.add(mealSub);
                }
            }
        }

        return list;
    }

    private class SubCategoriesAdapter extends PagerAdapter {

        private Context context;
        private LayoutInflater inflater;
        private List<MealSubCategory> subCategoryItems;

        public SubCategoriesAdapter(Context context, List<MealSubCategory> subCategoryItems) {
            this.context = context;
            this.subCategoryItems = subCategoryItems;
            inflater = LayoutInflater.from(context);

        }

        @Override
        public int getCount() {
            return subCategoryItems.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LinearLayout layout = new LinearLayout(context);
            final ListView content = new ListView(context);

            content.setDivider(null);
            content.setDividerHeight(0);
            content.setEnabled(false);
            content.setSelector(android.R.color.transparent);
            content.setBackgroundColor(getResources().getColor(android.R.color.white));
            content.setAdapter(new MealsListAdapter(context, subCategoryItems.get(position)));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            content.setLayoutParams(params);

            layout.addView(content);
            container.addView(layout);

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

    private class MealsListAdapter extends BaseAdapter {

        private Context context;
        private MealSubCategory subCat;
        private LayoutInflater inflater;
        private ImageLoader imageLoader;

        public MealsListAdapter(Context context, MealSubCategory subCat) {
            this.context = context;
            this.subCat = subCat;
            imageLoader = VolleySingleton.getInstance(context).getImageLoader();
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (subCat != null && subCat.items != null)
                return subCat.items.size();
            return 0;
        }

        @Override
        public Item getItem(int i) {
            if (subCat != null && subCat.items != null && subCat.items.get(i) != null)
                return subCat.items.get(i);
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            if (convertView == null || convertView.getTag() == null) {

                ViewHolder holder = new ViewHolder();

                convertView = inflater.inflate(R.layout.row_subcategory_item, viewGroup, false);

                holder.separatorTop = convertView.findViewById(R.id.separatorTop);
                holder.separatorBottom = convertView.findViewById(R.id.separatorBottom);
                holder.image = (NetworkImageView) convertView.findViewById(R.id.networkImageViewMealImage);
                holder.addItem = (TextView) convertView.findViewById(R.id.textviewAdd);
                holder.details = (TextView) convertView.findViewById(R.id.textviewDetails);
                holder.name = (TextView) convertView.findViewById(R.id.textviewMealName);
                holder.price = (TextView) convertView.findViewById(R.id.textviewMealPrices);
                holder.container = (RelativeLayout) convertView.findViewById(R.id.container);

                convertView.setTag(holder);
            }

            ViewHolder holder = (ViewHolder) convertView.getTag();

            Item item = getItem(position);

            if (position % 2 == 0)
                holder.container.setVisibility(View.VISIBLE);
            else
                holder.container.setVisibility(View.GONE);

            if (position == 0) {
                holder.separatorTop.setVisibility(View.GONE);
                holder.separatorBottom.setVisibility(View.GONE);
            } else if (position == (subCat.items.size() - 1)) {
                holder.separatorBottom.setVisibility(View.VISIBLE);
                holder.separatorTop.setVisibility(View.GONE);
            } else {
                holder.separatorTop.setVisibility(View.VISIBLE);
                holder.separatorBottom.setVisibility(View.GONE);
            }

            holder.image.setImageUrl(item.image, imageLoader);
            holder.image.setErrorImageResId(R.drawable.no_image);

            holder.name.setText(item.name);

            holder.price.setText(item.currency + " " + item.largeprice + " / " + item.smallprice);
            return convertView;
        }


        private class ViewHolder {
            View separatorTop;
            View separatorBottom;
            NetworkImageView image;
            TextView name;
            TextView price;
            TextView addItem;
            TextView details;
            RelativeLayout container;
        }

    }

    private class MealSubCategory {
        public List<Item> items;

        public MealSubCategory() {
            items = new ArrayList<Item>();
        }

    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

}
