package com.usemenu.MenuAndroidApplication.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.usemenu.MenuAndroidApplication.R;

public class AlertDialogFragment extends DialogFragment {
	private static final String TAG = AlertDialogFragment.class.getSimpleName();
	private FragmentManager fm;
	private String tag = "alert_dialog";
	private AlertDialog.Builder builder;
	private String title;
	private String body;
	private OnClickListener onClickListener;
	private Context context;
	private TextView textViewTitle;
	private TextView textViewBody;
	private Button buttonOk;
	private boolean isVisible = false;

	public AlertDialogFragment() {
	}

	public void setFragmentManager(FragmentManager fm, Context context) {
		this.fm = fm;
		this.context = context;
	}

	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState) {
		isVisible = true;
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_alert, null);
		textViewTitle = (TextView) view.findViewById(R.id.textViewTitleAlertDialog);
		textViewBody = (TextView) view.findViewById(R.id.textViewbodyAlertDialog);
		buttonOk = (Button) view.findViewById(R.id.buttonOk);
		textViewTitle.setText(title);
		textViewBody.setText(body);
		buttonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onClickListener != null)
					onClickListener.onClick(v);
				dismiss();
			}
		});
		builder = new AlertDialog.Builder(getActivity());
		builder.setView(view);
		setCancelable(false);
		return builder.create();
	}

	public void showDialog(String title, String body, OnClickListener listener) {
		if (isVisible() || isVisible)
			return;

		this.onClickListener = listener;
		this.title = title;
		this.body = body;
		show(fm, tag);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		isVisible = false;
	}
}