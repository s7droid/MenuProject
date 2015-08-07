package com.usemenu.MenuAndroidApplication.dialogs;

import java.io.IOException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.usemenu.MenuAndroidApplication.R;
import com.usemenu.MenuAndroidApplication.gif.GifDecoderView;

public class ProgressDialogFragment extends DialogFragment {
	private FragmentManager fm;
	private String tag = "alert_dialog";
	private AlertDialog.Builder builder;
	private String body;
	private Context context;
	private TextView bodyTextView;
	public boolean isVisible = false;

	public ProgressDialogFragment() {
	}

	public void setFragmentManager(FragmentManager fm, Context context) {
		this.fm = fm;
		this.context = context;
	}

	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState) {
		isVisible = true;
		LayoutInflater inflater = getActivity().getLayoutInflater();
		RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.dialog_progress, null);
		
		try {
			GifDecoderView gifView = (GifDecoderView) view.findViewById(R.id.decoderView);
			gifView.playGif(context.getAssets().open("loading_icon_384.gif"));
//					new GifDecoderView(context, context.getAssets().open("loading_icon_384.gif"));
//			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
//			params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//			gifView.setLayoutParams(params);
//			view.addView(gifView);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (body != null)
			bodyTextView.setText(body);
		builder = new AlertDialog.Builder(getActivity());
		builder.setView(view);
		setCancelable(false);
		return builder.create();
	}

	// @Override
	// public void onStart() {
	// super.onStart();
	// try {
	// getDialog().getWindow().setLayout(800, LayoutParams.WRAP_CONTENT);
	// } catch (Exception e) {
	// Log.e("ProgressDialog", "onStart error");
	// }
	// }

	public void showDialog(String body) {
		if (isVisible() || isVisible)
			return;
		this.body = body;
		show(fm, tag);
	}

	public void showDialog() {
		if (isVisible() || isVisible)
			return;
		show(fm, tag);
	}

	@Override
	public void show(FragmentManager manager, String tag) {
		if (isVisible)
			return;

		super.show(manager, tag);
		isVisible = true;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		isVisible = false;
	}
}