package com.usemenu.MenuAndroidApplication.dialogs;

import java.io.IOException;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.usemenu.MenuAndroidApplication.R;
import com.usemenu.MenuAndroidApplication.gif.GifDecoderView;

public class ProgressDialog extends Dialog {

	public ProgressDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.dialog_progress);

		LayoutParams params = getWindow().getAttributes();
		params.width = LayoutParams.MATCH_PARENT;
		params.height = LayoutParams.MATCH_PARENT;
		getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

		getWindow().setBackgroundDrawable(new ColorDrawable(0x99000000));

		initViews();

		setCanceledOnTouchOutside(Boolean.FALSE);
		setCancelable(Boolean.TRUE);

	}

	private void initViews() {

		GifDecoderView gifView = (GifDecoderView) findViewById(R.id.decoderView);

		try {
			gifView.playGif(getContext().getAssets().open("loading_icon_256.gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
