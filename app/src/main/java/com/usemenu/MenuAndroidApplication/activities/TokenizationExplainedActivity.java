package com.usemenu.MenuAndroidApplication.activities;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.usemenu.MenuAndroidApplication.R;

public class TokenizationExplainedActivity extends BaseActivity {

	// VIEWS
	private TextView mTextViewTitle;
	private TextView mTextViewSubtitle;
	private TextView mTextViewFirstStep;
	private TextView mTextViewSecondStep;
	private TextView mTextViewThirdStep;

	// DATA

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_tokenization_explain);
		initActionBar();
		initViews();
	}

	private void initActionBar() {
		setActionBarForwardArrowVisibility(null);
		setActionBarForwardButtonOnClickListener(null);
		setActionBarForwardButtonTextColor(getResources().getColor(R.color.menu_main_orange));
		setActionBarForwardButtonText(getResources().getString(R.string.tokenization_explanation_action_bar_title));

		setActionBarBackButtonOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initViews() {
		mTextViewTitle = (TextView) findViewById(R.id.textviewTokenizationActivityTitle);
		mTextViewSubtitle = (TextView) findViewById(R.id.textviewTokenizationActivitySubtitle);
		mTextViewFirstStep = (TextView) findViewById(R.id.textviewTokenizationActivityFirstStep);
		mTextViewSecondStep = (TextView) findViewById(R.id.textviewTokenizationActivitySecondStep);
		mTextViewThirdStep = (TextView) findViewById(R.id.textviewTokenizationActivityThirdStep);

		mTextViewFirstStep.setText(getSpannedTextForTokenizationExplainStep(getResources().getString(R.string.tokenization_explanation_first_step)));
		mTextViewSecondStep.setText(getSpannedTextForTokenizationExplainStep(getResources().getString(R.string.tokenization_explanation_second_step)));
		mTextViewThirdStep.setText(getSpannedTextForTokenizationExplainStep(getResources().getString(R.string.tokenization_explanation_third_step)));
		
		
	}

	private Spannable getSpannedTextForTokenizationExplainStep(String string) {
		Spannable spann = new SpannableString(string);

		spann.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.menu_main_orange)), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spann.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		return spann;
	}

}