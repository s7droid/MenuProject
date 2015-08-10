package com.usemenu.MenuAndroidApplication.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.usemenu.MenuAndroidApplication.R;

/**
 * Created by milos on 8/7/15.
 */
public class TutorialPageIndicatorView extends LinearLayout {
    private ImageView selectedPage;

    public TutorialPageIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View view = inflate(context, R.layout.view_page_indicator, this);
        selectedPage = (ImageView) view.findViewById(R.id.imageviewSelectedPage);
    }

    public void setSelectedPage(float translation) {
            selectedPage.setTranslationX(translation);
    }
}
