package com.usemenu.MenuAndroidApplication.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.usemenu.MenuAndroidApplication.R;

public class CircleButtonView extends RelativeLayout {

	private TextView textView;
	private ImageView imageView;

	public CircleButtonView(Context context) {
		super(context);

		init();
	}

	public CircleButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	private void init() {

		inflate(getContext(), R.layout.circle_button_view, this);

		textView = (TextView) findViewById(R.id.textView);
		imageView = (ImageView) findViewById(R.id.imageView);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		textView.setTextSize(heightMeasureSpec * 2 / 3);
	}

	public void setAsQty(int qty) {
		textView.setText(String.valueOf(qty));
		setBackgroundResource(R.drawable.border_round_gray_light);
	}

	public void setAsDel() {
		imageView.setImageResource(R.drawable.button_x_grey);
	}

	public void setAsRemoveOrange() {
		imageView.setImageResource(R.drawable.button_minus_orange);
	}

	public void setAsRemoveGrey() {
		imageView.setImageResource(R.drawable.button_minus_grey);
	}

	public void setAsAddOrange() {
		imageView.setImageResource(R.drawable.button_plus_orange);
	}

	public void setAsAddGrey() {
		imageView.setImageResource(R.drawable.button_plus_grey);
	}

	// public void setAsLight() {
	// textView.setTextColor(getResources().getColor(R.color.menu_main_gray_light));
	// setBackgroundResource(R.drawable.border_round_gray_light);
	// }
	//
	// public void setAsOrange() {
	// textView.setTextColor(getResources().getColor(R.color.menu_main_orange));
	// setBackgroundResource(R.drawable.border_round_orange);
	// }

}
