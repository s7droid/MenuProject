package com.usemenu.MenuAndroidApplication.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.volley.VolleyError;
import com.usemenu.MenuAndroidApplication.R;
import com.usemenu.MenuAndroidApplication.app.Menu;
import com.usemenu.MenuAndroidApplication.callbacks.OnVolleyErrorCallback;
import com.usemenu.MenuAndroidApplication.dialogs.AlertDialogEditTextFragment;
import com.usemenu.MenuAndroidApplication.dialogs.AlertDialogFragment;
import com.usemenu.MenuAndroidApplication.dialogs.OkCancelDialogFragment;
import com.usemenu.MenuAndroidApplication.dialogs.ProgressDialog;
import com.usemenu.MenuAndroidApplication.utils.Utils;
import com.usemenu.MenuAndroidApplication.volley.responses.GsonResponse;

public class BaseActivity extends ActionBarActivity implements OnVolleyErrorCallback {

	private static final String TAG = BaseActivity.class.getSimpleName();

	private ActionBar actionBar;

	private ProgressDialog progressDialog;
	private AlertDialogEditTextFragment edittextDialog;
	private AlertDialogFragment alertDialog;
	// private ProgressDialogFragment progressDialog;
	private Button buttonActionBarBack;
	private Button buttonActionBarForward;
	private ImageButton imageButtonActionBarMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));


		LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.action_bar, null);

		actionBar.setCustomView(v);

		Toolbar parent = (Toolbar) v.getParent();
		parent.setContentInsetsAbsolute(0, 0);


		// Uncomment if action bar right margin appears!

//		ViewGroup.LayoutParams lp = v.getLayoutParams();
//		lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
//		v.setLayoutParams(lp);

		alertDialog = new AlertDialogFragment();
		alertDialog.setFragmentManager(getFragmentManager(), this);

		// progressDialog = new ProgressDialogFragment();
		// progressDialog.setFragmentManager(getFragmentManager(), this);

		progressDialog = new ProgressDialog(this);

		edittextDialog = new AlertDialogEditTextFragment();
		edittextDialog.setFragmentManager(getFragmentManager(), this);

		buttonActionBarBack = (Button) findViewById(R.id.buttonActionBarBack);
		buttonActionBarForward = (Button) findViewById(R.id.buttonActionBarForward);
		imageButtonActionBarMenu = (ImageButton) findViewById(R.id.imageButtonActionBarMenu);

		imageButtonActionBarMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();

		Menu.getInstance().registerOnVolleyErrorCallback(this);
	}

	@Override
	protected void onPause() {
		super.onPause();

		Menu.getInstance().unregisterOnVolleyErrorCallback();
	}

	public void hideActionBar() {
		actionBar.hide();
	}

	public void setActionBarMenuButtonOnClickListener(OnClickListener listener) {
		if (listener != null)
			imageButtonActionBarMenu.setOnClickListener(listener);
	}

	public void setActionBarBackButtonOnClickListener(OnClickListener listener) {
		if (listener != null)
			buttonActionBarBack.setOnClickListener(listener);
	}

	public void setActionBarForwardButtonOnClickListener(OnClickListener listener) {
		if (listener != null)
			buttonActionBarForward.setOnClickListener(listener);
	}

	public void setActionBarBackButtonText(String text) {
		buttonActionBarBack.setText(text);
	}

	public void setActionBarBackButtonText(int text) {
		buttonActionBarBack.setText(text);
	}

	public void setActionBarForwardButtonText(String text) {
		buttonActionBarForward.setText(text);
	}

	public void setActionBarForwardButtonText(int text) {
		buttonActionBarForward.setText(text);
	}

	public void setActionBarForwardButtonvisibility(int visibility) {
		buttonActionBarForward.setVisibility(visibility);
	}

	public void setActionBarBackButtonVisibility(int visibility) {
		buttonActionBarBack.setVisibility(visibility);
	}

	public void setActionBarMenuButtonVisibility(int visibility) {
		imageButtonActionBarMenu.setVisibility(visibility);
	}

	public void setActionBarForwardButtonTextColor(int color) {
		buttonActionBarForward.setTextColor(color);
	}

	/**
	 * Setting visibility for right arrow
	 * 
	 * @param drawable
	 *            - resource for arrow (fi <b>null</b>, arrow becomes invisible)
	 */
	public void setActionBarForwardArrowVisibility(Drawable drawable) {
		buttonActionBarForward.setCompoundDrawables(null, null, drawable, null);
		buttonActionBarForward.setPadding(buttonActionBarForward.getPaddingLeft(), buttonActionBarForward.getPaddingTop(), 30, buttonActionBarForward.getPaddingBottom());
		if (drawable == null)
			buttonActionBarForward.setPadding(buttonActionBarForward.getPaddingLeft(), buttonActionBarForward.getPaddingTop(), (int) Utils.convertDpToPixel(20, BaseActivity.this),
					buttonActionBarForward.getPaddingBottom());
	}

	public void showAlertDialog(int title, int body) {
		alertDialog.showDialog(getString(title), getString(body), null);
	}

	public void showAlertDialog(String title, String body) {
		alertDialog.showDialog(title, body, null);
	}

	public void showAlertDialog(int title, int body, OnClickListener onClickListener) {
		alertDialog.showDialog(getString(title), getString(body), onClickListener);
	}

	public void showAlertDialog(String title, String body, OnClickListener onClickListener) {
		alertDialog.showDialog(title, body, onClickListener);
	}

	public void showProgressDialog(int body) {
		// progressDialog.showDialog(getString(body));
		progressDialog.show();
	}

	public void showProgressDialog(String body) {
		// progressDialog.showDialog(body);
		progressDialog.show();
	}

	public void showProgressDialogLoading() {
		// if (!progressDialog.isVisible) {
		// progressDialog.show(getFragmentManager(),
		// BaseActivity.class.getSimpleName());
		// ;
		// }
		progressDialog.show();
	}

	public void dismissProgressDialog() {
		// try {
		// progressDialog.dismiss();
		// progressDialog.dismissAllowingStateLoss();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		progressDialog.dismiss();
	}

	public void showEditTextDialog(String title, OnClickListener listener) {
		edittextDialog.showDialog(title, listener);
	}

	@Override
	public void onResponseError(GsonResponse response) {

		dismissProgressDialog();

		if (response.response != null && response.response.equals("wronglogindetails")) {

			OkCancelDialogFragment dialog = new OkCancelDialogFragment();
			dialog.showDialog(getFragmentManager(), "", getString(R.string.dialog_please_login_again), new OnClickListener() {

				@Override
				public void onClick(View v) {
					startActivity(new Intent(BaseActivity.this, SignInActivity.class));
					finish();
				}
			}, null);

			return;
		}
		
		if(response.response != null && response.response.equals("ccdeclined")){
			showAlertDialog(R.string.dialog_title_error, R.string.dialog_card_declined);
			return;
		}
		
		showAlertDialog(getString(R.string.dialog_body_default_error_title), getString(R.string.dialog_body_default_error_message));
	}

	@Override
	public void onVolleyError(VolleyError volleyError) {
		dismissProgressDialog();
		showAlertDialog(getString(R.string.dialog_body_default_error_title), getString(R.string.dialog_body_default_error_message));
	}

	public void hideRightActionBarButton() {
		buttonActionBarForward.setVisibility(View.INVISIBLE);
	}

	public void showRightActionBarButton() {
		buttonActionBarForward.setVisibility(View.VISIBLE);
	}

}
