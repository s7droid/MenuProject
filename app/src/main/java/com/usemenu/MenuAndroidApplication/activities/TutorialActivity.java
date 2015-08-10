package com.usemenu.MenuAndroidApplication.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.usemenu.MenuAndroidApplication.R;
import com.usemenu.MenuAndroidApplication.utils.Settings;
import com.usemenu.MenuAndroidApplication.utils.Utils;
import com.usemenu.MenuAndroidApplication.views.TutorialPageIndicatorView;

/**
 * Created by milos on 8/7/15.
 */
public class TutorialActivity extends BaseActivity {

    private TutorialPagerAdapter adapter;
    private ViewPager viewPager;
    private TutorialPageIndicatorView indicatorView;
    private TextView skipOrContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        initActionBar();
        initViews();
    }

    private void initActionBar() {
        hideActionBar();
    }

    private void initViews() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        indicatorView = (TutorialPageIndicatorView) findViewById(R.id.tutorialPageIndicator);
        skipOrContinue = (TextView) findViewById(R.id.textviewContinue);
        adapter = new TutorialPagerAdapter(this);
        viewPager.setAdapter(adapter);

        SpannableString ssSkip = new SpannableString(getResources().getString(R.string.tutorial_skip));
        ssSkip.setSpan(new UnderlineSpan(), 0, ssSkip.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        skipOrContinue.setText(ssSkip);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 2 && (positionOffset > 0 && positionOffset <= 0.5)) {
                    skipOrContinue.setAlpha(1 - (2 * positionOffset));
                    SpannableString ssSkip = new SpannableString(getResources().getString(R.string.tutorial_skip));
                    ssSkip.setSpan(new UnderlineSpan(), 0, ssSkip.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    skipOrContinue.setText(ssSkip);
                }

                if (position == 2 && (positionOffset > 0.5)) {
                    skipOrContinue.setAlpha(positionOffset - (1 - positionOffset));
                    SpannableString ssSkip = new SpannableString(getResources().getString(R.string.tutorial_continue));
                    ssSkip.setSpan(new UnderlineSpan(), 0, ssSkip.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    skipOrContinue.setText(ssSkip);
                }

                for (int i = 0; i <= 20; i++) {
                    float translation = Utils.convertDpToPixel((int) (i * (position + positionOffset)), TutorialActivity.this);
                    indicatorView.setSelectedPage(translation);
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        skipOrContinue.setOnClickListener(onSkipClicked);
    }

    View.OnClickListener onSkipClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!Settings.getStoreMajor(TutorialActivity.this).isEmpty() && !Settings.getStoreMinor(TutorialActivity.this).isEmpty()) {
                Intent intent = new Intent(getApplicationContext(), CategoryMealsActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
            finish();
        }
    };

    View.OnClickListener onBackClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    private class TutorialPagerAdapter extends PagerAdapter {

        private Context context;
        private LayoutInflater inflater;

        public TutorialPagerAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LinearLayout layout = new LinearLayout(context);
            final RelativeLayout content = (RelativeLayout) inflater.inflate(R.layout.tutorial_pager_item, null);

            content.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            //     content.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
            final TextView message = (TextView) content.findViewById(R.id.tutorialMessage);
            final ImageView image = (ImageView) content.findViewById(R.id.tutorialImage);
            final RelativeLayout imageContainer = (RelativeLayout) content.findViewById(R.id.relativeLayout);

//            image.setBackgroundColor(getResources().getColor(R.color.menu_main_gray_light));

            if (position == 0) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.leftMargin = (int) Utils.convertDpToPixel(30, context);
                params.rightMargin = (int) Utils.convertDpToPixel(30, context);
                params.bottomMargin = (int) Utils.convertDpToPixel(40, context);
                params.topMargin = (int) Utils.convertDpToPixel(20, context);
                params.addRule(RelativeLayout.BELOW, R.id.tutorialMessage);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                image.setLayoutParams(params);

                message.setText(context.getResources().getString(R.string.tutorial_explanation_1));
                image.setImageResource(R.drawable.tutorial_step_1);
            } else if (position == 1) {
//                RelativeLayout.LayoutParams paramsImage = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                paramsImage.addRule(RelativeLayout.CENTER_IN_PARENT);
//                image.setLayoutParams(paramsImage);

//                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//                imageContainer.setLayoutParams(params);
                message.setText(context.getResources().getString(R.string.tutorial_explanation_2));
                message.setBackgroundColor(Color.parseColor("#fcfcfc"));

                int screenWidth = Utils.getScreenWidth(context);
                Drawable drawable = getResources().getDrawable(R.drawable.tutorial_step_2);
                int imageWidth = drawable.getIntrinsicWidth();
                int imageHeight = drawable.getIntrinsicHeight();

                RelativeLayout.LayoutParams paramsImage = new RelativeLayout.LayoutParams(screenWidth, (int) (screenWidth * 2 * ((float) (imageHeight / imageWidth))));
                paramsImage.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                image.setLayoutParams(paramsImage);

                image.setBackground(drawable);
            } else if (position == 2) {
//                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//                imageContainer.setLayoutParams(params);
                message.setText(context.getResources().getString(R.string.tutorial_explanation_3));
                message.setBackgroundColor(Color.parseColor("#fcfcfc"));

                int screenWidth = Utils.getScreenWidth(context);
                Drawable drawable = getResources().getDrawable(R.drawable.tutorial_step_3);
                int imageWidth = drawable.getIntrinsicWidth();
                int imageHeight = drawable.getIntrinsicHeight();

                RelativeLayout.LayoutParams paramsImage = new RelativeLayout.LayoutParams(screenWidth, (int) (screenWidth * 2 * ((float) (imageHeight / imageWidth))));
                paramsImage.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                image.setLayoutParams(paramsImage);

                image.setBackground(drawable);
            } else if (position == 3) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.leftMargin = (int) Utils.convertDpToPixel(30, context);
                params.rightMargin = (int) Utils.convertDpToPixel(30, context);
                params.bottomMargin = (int) Utils.convertDpToPixel(40, context);
                params.topMargin = (int) Utils.convertDpToPixel(20, context);
                params.addRule(RelativeLayout.BELOW, R.id.tutorialMessage);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                image.setLayoutParams(params);

                message.setText(context.getResources().getString(R.string.tutorial_explanation_4));
                image.setImageResource(R.drawable.tutorial_step_4);
            }

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
}
