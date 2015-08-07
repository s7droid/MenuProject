package com.usemenu.MenuAndroidApplication.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.usemenu.MenuAndroidApplication.R;

public class AlertDialogEditTextFragment extends DialogFragment {
	private static final String TAG = AlertDialogFragment.class.getSimpleName();
	private FragmentManager fm;
	private String tag = "alert_dialog";
	private AlertDialog.Builder builder;
	private String title;
	private OnClickListener onClickListener;
	private TextView textViewTitle;
	private EditText textViewBody;
	private TextView characatersLeft;
	private Button buttonOk;
	private boolean isVisible = false;

	public AlertDialogEditTextFragment() {
	}

	public void setFragmentManager(FragmentManager fm, Context context) {
		this.fm = fm;
	}

	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState) {
		isVisible = true;
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_edit_text, null);
		textViewTitle = (TextView) view.findViewById(R.id.textViewTitleAlertEditTextDialog);
		textViewBody = (EditText) view.findViewById(R.id.editetextBodyAlertEditTextDialog);
		characatersLeft = (TextView) view.findViewById(R.id.textviewCharactersLeft);
		buttonOk = (Button) view.findViewById(R.id.buttonEditTextDialogOk);
		textViewTitle.setText(title);
		buttonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onClickListener != null)
					onClickListener.onClick(v);
				dismiss();
			}
		});

		characatersLeft.setText(textViewBody.length() + "/100");

		textViewBody.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				characatersLeft.setText(textViewBody.length() + "/100");
				System.out.println("ON TEXT CHANGED");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		builder = new AlertDialog.Builder(getActivity());
		builder.setView(view);
		setCancelable(true);
		return builder.create();
	}

	public void showDialog(String title, OnClickListener listener) {
		if (isVisible() || isVisible)
			return;

		this.onClickListener = listener;
		this.title = title;
		show(fm, tag);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		isVisible = false;
	}
}
