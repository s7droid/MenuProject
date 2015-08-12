package com.usemenu.MenuAndroidApplication.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.usemenu.MenuAndroidApplication.R;
import com.usemenu.MenuAndroidApplication.dataclasses.Category;
import com.usemenu.MenuAndroidApplication.dataclasses.Item;
import com.usemenu.MenuAndroidApplication.utils.Utils;
import com.usemenu.MenuAndroidApplication.volley.VolleySingleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milos on 8/10/15.
 */
public class CategoryMealItemView extends LinearLayout implements View.OnClickListener {

    private LinearLayout subCategoriesContainer;
    private NetworkImageView categoryImage;
    private Category category;
    private List<Item> item;
    private Activity context;
    private ImageLoader imageLoader;
    private List<Category> categories;
    private List<Item> allItems; // the list of all items. Here are included subcategories and categories, and onClick, this list will be filtered with only items from certain subcategory

    public CategoryMealItemView(Activity context, Category category, List<Item> item, List<Item> allItems, List<Category> categories) {
        super(context);
        this.category = category;
        this.item = item;
        this.context = context;
        this.allItems = allItems;
        this.categories = categories;
        imageLoader = VolleySingleton.getInstance(context).getImageLoader();
        init();
    }

    public CategoryMealItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CategoryMealItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        View rootView = inflate(getContext(), R.layout.row_category_meal, this);

        subCategoriesContainer = (LinearLayout) rootView.findViewById(R.id.linearlayoutSubCategoryContainer);
        categoryImage = (NetworkImageView) rootView.findViewById(R.id.imageviewCategoryImage);

        subCategoriesContainer.setOnClickListener(this);

        categoryImage.setErrorImageResId(R.drawable.no_image);
        categoryImage.setDefaultImageResId(R.drawable.no_image);
        categoryImage.setImageUrl(category.image, imageLoader);

        View view = LayoutInflater.from(context).inflate(R.layout.row_subcategory_name, null);
        View separatorTitle = (View) view.findViewById(R.id.separator);
        TextView categoryName = (TextView) view.findViewById(R.id.textviewSubCategoryName);
        categoryName.setText(category.name);
        categoryName.setTextColor(getResources().getColor(android.R.color.black));

        subCategoriesContainer.addView(view);

        if (item.size() > 0) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = (int) Utils.convertDpToPixel(13, context);
            categoryName.setLayoutParams(params);
        } else {
            separatorTitle.setVisibility(View.INVISIBLE);
        }

        for (int i = 0; i < item.size(); i++) {
            View v = LayoutInflater.from(context).inflate(R.layout.row_subcategory_name, null);
            View separator = (View) v.findViewById(R.id.separator);
            TextView subCategory = (TextView) v.findViewById(R.id.textviewSubCategoryName);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            subCategory.setTextColor(getResources().getColor(R.color.menu_main_orange));
            subCategory.setTextSize(Utils.convertDpToPixel(5, context));
            subCategory.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.arrow_selection), null);

            if (i == (item.size() - 1)) {
                separator.setVisibility(View.INVISIBLE);
                params.topMargin = (int) Utils.convertDpToPixel(12, context);
            } else {
                params.topMargin = (int) Utils.convertDpToPixel(12, context);
                params.bottomMargin = (int) Utils.convertDpToPixel(11, context);
            }

            subCategory.setLayoutParams(params);

            subCategory.setText(item.get(i).name);
            v.setTag(item.get(i).name);
            v.setOnClickListener(this);
            subCategoriesContainer.addView(v);

        }
    }

    @Override
    public void onClick(View view) {
        if ((String) view.getTag() != null && !((String) view.getTag()).isEmpty()) {
            int position = 0;

            for (int i = 0; i < allItems.size(); ++i) {
                if (allItems.get(i).name.equalsIgnoreCase((String) view.getTag())) {
                    position = i + 1;
                }
            }

            List<Item> subCategoryItems = new ArrayList<Item>();
            for (int i = position; i < allItems.size(); ++i) {
                if (!allItems.get(i).image.equalsIgnoreCase("subcat")) {
                    subCategoryItems.add(allItems.get(i));
                }

                if (allItems.get(i).image.equalsIgnoreCase("subcat"))
                    break;
            }

            String it = "";
            for (Item item : allItems) {
                it.concat("\n" + item.name);
            }
            Toast.makeText(getContext(), "SubCategory list size= " + subCategoryItems.size() + "\n All items list size= " + allItems.size(), Toast.LENGTH_SHORT).show();

        }
    }
}
