package com.usemenu.MenuAndroidApplication.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.usemenu.MenuAndroidApplication.R;

public class OkCancelDialogFragment extends DialogFragment {
	private static final String TAG = OkCancelDialogFragment.class.getSimpleName();
	private String tag = "alert_dialog";
	private AlertDialog.Builder builder;
	private String title;
	private String body;
	private OnClickListener onClickListenerOk;
	private OnClickListener onClickListenerCancel;
	private TextView textViewTitle;
	private TextView textViewBody;
	private Button buttonOk;
	private Button buttonCancel;
	private boolean isVisible = false;

	public OkCancelDialogFragment() {
	}

	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState) {
		isVisible = true;
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_ok_cancel, null);
		textViewTitle = (TextView) view.findViewById(R.id.textViewTitleAlertDialog);
		textViewBody = (TextView) view.findViewById(R.id.textViewbodyAlertDialog);
		buttonOk = (Button) view.findViewById(R.id.buttonOk);
		buttonCancel = (Button) view.findViewById(R.id.buttonCancel);
		textViewTitle.setText(title);
		textViewBody.setText(body);
		buttonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onClickListenerOk != null)
					onClickListenerOk.onClick(v);
				dismiss();
			}
		});
		buttonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onClickListenerCancel != null)
					onClickListenerCancel.onClick(v);
				dismiss();
			}
		});
		builder = new AlertDialog.Builder(getActivity());
		builder.setView(view);
		setCancelable(false);
		return builder.create();
	}

	public void showDialog(FragmentManager fm, String title, String body, OnClickListener listenerOk, OnClickListener listenerCancel) {
		if (isVisible() || isVisible)
			return;

		this.onClickListenerOk = listenerOk;
		this.onClickListenerCancel = listenerCancel;
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