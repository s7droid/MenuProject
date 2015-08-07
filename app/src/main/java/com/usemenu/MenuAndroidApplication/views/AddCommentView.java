package com.usemenu.MenuAndroidApplication.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.usemenu.MenuAndroidApplication.R;

public class AddCommentView extends RelativeLayout {

	public EditText edittextBody;
	private Button buttonOk;
	
	public AddCommentView(Context context) {
		super(context);
		init();
	}

	public AddCommentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AddCommentView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void requestEditTextFocus(){
		edittextBody.requestFocus();
	}
	
	public void setOnClick(OnClickListener onClickListener){
		buttonOk.setOnClickListener(onClickListener);
	}
	
	private void init() {

		View view = inflate(getContext(), R.layout.dialog_edit_text, this);

		edittextBody = (EditText) view.findViewById(R.id.editetextBodyAlertEditTextDialog);
		final TextView characatersLeft = (TextView) view.findViewById(R.id.textviewCharactersLeft);
		buttonOk = (Button) view.findViewById(R.id.buttonEditTextDialogOk);

		characatersLeft.setText(edittextBody.length() + "/10");

		edittextBody.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				characatersLeft.setText(edittextBody.length() + "/10");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	public String getComment(){
		return edittextBody.getText().toString().trim();
	}
	
	public void clearComment(){
		edittextBody.setText("");
	}
	
}
