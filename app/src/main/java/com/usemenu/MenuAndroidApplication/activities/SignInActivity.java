package com.usemenu.MenuAndroidApplication.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.usemenu.MenuAndroidApplication.R;
import com.usemenu.MenuAndroidApplication.utils.Settings;
import com.usemenu.MenuAndroidApplication.utils.Utils;
import com.usemenu.MenuAndroidApplication.volley.VolleySingleton;
import com.usemenu.MenuAndroidApplication.volley.requests.ForgotPasswordRequest;
import com.usemenu.MenuAndroidApplication.volley.requests.LoginRequest;
import com.usemenu.MenuAndroidApplication.volley.responses.ForgotPasswordResponse;
import com.usemenu.MenuAndroidApplication.volley.responses.LoginResponse;

import java.util.HashMap;
import java.util.Map;

import static com.usemenu.MenuAndroidApplication.app.Constants.*;

public class SignInActivity extends BaseActivity {

    // VIEWS
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private TextView mForgottPasswordButton;
    private TextView mRegisterButton;
    private Button mSignInButton;

    // DATA


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initActionBar();
        initViews();
    }

    private void initActionBar() {
        setActionBarMenuButtonVisibility(View.INVISIBLE);

        setActionBarForwardArrowVisibility(null);
        setActionBarForwardButtonText(getResources().getString(R.string.action_bar_sign_in));
        setActionBarForwardButtonTextColor(getResources().getColor(R.color.menu_main_orange));

        setActionBarMenuButtonOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        setActionBarBackButtonOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {

        mUsernameEditText = (EditText) findViewById(R.id.edittextSignInActivityUsername);
        mPasswordEditText = (EditText) findViewById(R.id.edittextSignInActivityPassword);
        mForgottPasswordButton = (TextView) findViewById(R.id.textviewSignInActivityForgotPassword);
        mRegisterButton = (TextView) findViewById(R.id.textviewSignInActivitySignUp);
        mSignInButton = (Button) findViewById(R.id.buttonSignInActivitySignin);

        mUsernameEditText.requestFocus();
        Utils.handleOutsideEditTextClick(findViewById(R.id.relativelayoutSignInActivityContainer), this);

        mRegisterButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), SignUpActivity.class), REQUEST_SIGN_UP);
            }
        });

        mForgottPasswordButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mUsernameEditText.getText().toString().equals("")) {
                    showAlertDialog(null, getResources().getString(R.string.dialog_insert_email_address));
                    return;
                }

                showProgressDialogLoading();
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", mUsernameEditText.getText().toString());

                ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest(SignInActivity.this, params,
                        new Listener<ForgotPasswordResponse>() {

                            @Override
                            public void onResponse(ForgotPasswordResponse forgotPasswordResponse) {

                                dismissProgressDialog();

                                if (forgotPasswordResponse.response != null && forgotPasswordResponse.response.equals("success")) {

                                    showAlertDialog(R.string.dialog_check_inbox_title, R.string.dialog_check_inbox_content);
                                } else if (forgotPasswordResponse.response != null
                                        && forgotPasswordResponse.response.equals(ForgotPasswordResponse.USER_DOESNT_EXIST)) {

                                    showAlertDialog(R.string.dialog_no_user_title, R.string.dialog_no_user);
                                }
                            }
                        });

                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(forgotPasswordRequest);
            }
        });

        mSignInButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (Utils.isNetworkAvailable(getApplicationContext())) {
                    if (mUsernameEditText.getText().toString().equals("")) {
                        showAlertDialog(null, getResources().getString(R.string.dialog_insert_email_address));
                    } else if (mPasswordEditText.getText().toString().equals("")) {
                        showAlertDialog(null, getResources().getString(R.string.dialog_insert_password));
                    } else {

                        showProgressDialogLoading();

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("email", mUsernameEditText.getText().toString());
                        params.put("password", mPasswordEditText.getText().toString());

                        LoginRequest loginRequest = new LoginRequest(SignInActivity.this, params, new Listener<LoginResponse>() {

                            @Override
                            public void onResponse(LoginResponse loginResponse) {

                                if (loginResponse.response != null && loginResponse.response.equals("success")) {
                                    Settings.setAccessToken(SignInActivity.this, loginResponse.accesstoken);
                                    Intent intent = new Intent();
                                    setResult(RESULT_OK, intent);
                                    dismissProgressDialog();
                                    finish();
                                } else {
                                    dismissProgressDialog();

                                    if (loginResponse.response != null && loginResponse.response.equals("nouser")) {
                                        showAlertDialog(R.string.dialog_no_user_title, R.string.dialog_no_user);
                                    } else if (loginResponse.response.equals("passwordfalse")) {
                                        showAlertDialog(R.string.dialog_wrong_password_title, R.string.dialog_wrong_password);
                                    } else if (loginResponse.response.equals("lockedout")) {
                                        showAlertDialog(R.string.dialog_account_lockedout_title, R.string.dialog_account_lockedout_content);
                                    }

                                }
                            }
                        });

                        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(loginRequest);
                    }

                } else {
                    showAlertDialog(R.string.dialog_no_internet_connection_title, R.string.dialog_no_internet_connection_message);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_SIGN_UP) {

            }
        }
    }

}
