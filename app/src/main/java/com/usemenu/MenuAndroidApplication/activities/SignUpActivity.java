package com.usemenu.MenuAndroidApplication.activities;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.braintreepayments.api.Braintree;
import com.braintreepayments.api.models.CardBuilder;
import com.usemenu.MenuAndroidApplication.R;
import com.usemenu.MenuAndroidApplication.app.Menu;
import com.usemenu.MenuAndroidApplication.utils.Settings;
import com.usemenu.MenuAndroidApplication.utils.Utils;
import com.usemenu.MenuAndroidApplication.views.MenuEditText;
import com.usemenu.MenuAndroidApplication.volley.VolleySingleton;
import com.usemenu.MenuAndroidApplication.volley.requests.SignUpRequest;
import com.usemenu.MenuAndroidApplication.volley.responses.SignUpResponse;

import static com.usemenu.MenuAndroidApplication.app.Constants.*;

public class SignUpActivity extends BaseActivity {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    // VIEWS
    private TextView mShowHidePasswordButton;
    private Button mScanCreditCardButton;
    private Button mSignUpButton;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    // private EditText mNicknameEditText;
    private EditText mRepeatPasswordEditText;
    private EditText mCreditCardNumberEditText;
    private EditText mNameOnCardEditText;
    private EditText mMonthEditText;
    private EditText mYearEditText;
    private EditText mCCVEditText;
    private TextView mTextViewTermsOfUse;

    // DATA
    private boolean isPasswordShowing = false;
    private int counter = 0;
    private int tokenizeCalled = 0;

    private String creditCardNumber;
    private String nameOnCard;
    private String expiryDate;
    private String cardCCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initActionBar();
        initViews();

    }

    private void initActionBar() {
        setActionBarMenuButtonVisibility(View.INVISIBLE);

        setActionBarForwardArrowVisibility(null);
        setActionBarForwardButtonText(getResources().getString(R.string.action_bar_sign_up));
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
        mSignUpButton = (Button) findViewById(R.id.buttonSignUpActivitySignUp);
        mShowHidePasswordButton = (TextView) findViewById(R.id.buttonSignUpActivityHide);
        mScanCreditCardButton = (Button) findViewById(R.id.buttonSignUpScanCreditCard);
        mEmailEditText = (EditText) findViewById(R.id.edittextSignUpActivityEmail);
        mPasswordEditText = (EditText) findViewById(R.id.edittextSignUpActivityPassword);
        // mNicknameEditText = (EditText)
        // findViewById(R.id.edittextSignUpActivityNickname);
        mRepeatPasswordEditText = (EditText) findViewById(R.id.edittextSignUpActivityRepeatPassword);
        mCreditCardNumberEditText = (EditText) findViewById(R.id.edittextSignUpActivityCardNumber);
        mNameOnCardEditText = (EditText) findViewById(R.id.edittextSignUpActivityNameOnCard);
        mMonthEditText = (EditText) findViewById(R.id.edittextSignUpActivityMonth);
        mYearEditText = (EditText) findViewById(R.id.edittextSignUpActivityYear);
        mCCVEditText = (EditText) findViewById(R.id.edittextSignUpActivityCCV);
        mTextViewTermsOfUse = (TextView) findViewById(R.id.textviewSignUpScanTermsOfUse);

        mNameOnCardEditText.requestFocus();
        Utils.handleOutsideEditTextClick(findViewById(R.id.relativelayoutSignUpActivityContainer), this);

        initClicksInTermsText();

        mSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tokenizeCalled = 0;
                if (mNameOnCardEditText.getText().toString().isEmpty()) {
                    showAlertDialog(getResources().getString(R.string.dialog_first_name_empty_title), getResources().getString(R.string.dialog_first_name_empty_content));
                    return;
                } else if (mEmailEditText.getText().toString().isEmpty()) {
                    showAlertDialog(getResources().getString(R.string.dialog_email_address_empty_title), getResources().getString(R.string.dialog_email_address_empty_content));
                    return;
                } else if (mPasswordEditText.getText().toString().isEmpty()) {
                    showAlertDialog(getResources().getString(R.string.dialog_password_empty_title), getResources().getString(R.string.dialog_password_empty_content));
                    return;
                } else if (mRepeatPasswordEditText.getText().toString().isEmpty()) {
                    showAlertDialog(getResources().getString(R.string.dialog_repeat_password_empty_title), getResources().getString(R.string.dialog_repeat_password_empty_content));
                    return;
                } else if (mCreditCardNumberEditText.getText().toString().isEmpty()) {
                    showAlertDialog(getResources().getString(R.string.dialog_credit_card_number_empty_title), getResources().getString(R.string.dialog_credit_card_number_empty_content));
                    return;
                } else if (!Utils.isValidExpiryDate(mMonthEditText.getText().toString(), mYearEditText.getText().toString())) {
                    showAlertDialog(getResources().getString(R.string.dialog_invalid_expiry_date_error_title), getResources().getString(R.string.dialog_invalid_expiry_date_error_content));
                    return;
                } else if (mCCVEditText.getText().toString().isEmpty()) {
                    showAlertDialog(getResources().getString(R.string.dialog_cvv_empty_title), getResources().getString(R.string.dialog_cvv_empty_content));
                    return;
                } else if (!mPasswordEditText.getText().toString().equals(mRepeatPasswordEditText.getText().toString())) {
                    showAlertDialog(getResources().getString(R.string.dialog_passwords_dont_match_title), getResources().getString(R.string.dialog_passwords_dont_match_content));
                    return;
                }

                Braintree braintree = Braintree.getInstance(SignUpActivity.this, Menu.getInstance().getDataManager().getClientBraintreeToken(SignUpActivity.this));
                braintree.addListener(new Braintree.PaymentMethodNonceListener() {
                    public void onPaymentMethodNonce(String paymentMethodNonce) {
                        tokenizeCalled++;
                        showProgressDialogLoading();

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("email", mEmailEditText.getText().toString());
                        params.put("password", mPasswordEditText.getText().toString());
                        params.put("nonce", paymentMethodNonce);
                        params.put("name", mNameOnCardEditText.getText().toString());

                        SignUpRequest signUpRequest = new SignUpRequest(SignUpActivity.this, params, new Listener<SignUpResponse>() {

                            @Override
                            public void onResponse(SignUpResponse signUpResponse) {
                                Log.e("dialog", "onSignUpResponse");
                                if (signUpResponse.response.equals("success")) {
                                    Settings.setAccessToken(SignUpActivity.this, signUpResponse.accesstoken);
                                    if (!CheckoutActivity.isCheckoutClicked) {
                                        Intent i = new Intent(SignUpActivity.this, RestaurantPreviewActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                        dismissProgressDialog();
                                    } else {
                                        Intent i = new Intent(SignUpActivity.this, CheckoutActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                        dismissProgressDialog();

                                    }

                                } else if (signUpResponse.response.equals("emailinuse")) {
                                    showAlertDialog(R.string.dialog_title_error, R.string.dialog_email_in_use);
                                } else if (signUpResponse.response.equals("ccdeclined")) {
                                    showAlertDialog(R.string.dialog_title_error, R.string.dialog_card_declined);
                                }

                                dismissProgressDialog();
                            }
                        });
                        if (tokenizeCalled < 2) {
                            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(signUpRequest);
                        }
                    }
                });

                CardBuilder cardBuilder = new CardBuilder().cardNumber(mCreditCardNumberEditText.getText().toString()).expirationDate(
                        mMonthEditText.getText().toString() + "/" + mYearEditText.getText().toString());
                braintree.tokenize(cardBuilder);
            }
        });

        mShowHidePasswordButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isPasswordShowing) {
                    mShowHidePasswordButton.setText(R.string.sign_up_show);
                    mPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ((MenuEditText) mPasswordEditText).setFont(true);
                } else {
                    mShowHidePasswordButton.setText(R.string.sign_up_hide);
                    mPasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ((MenuEditText) mPasswordEditText).setFont(true);

                }

                isPasswordShowing = !isPasswordShowing;

            }
        });

        mScanCreditCardButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("SCAN BUTTON CLICKED");
                // This method is set up as an onClick handler in the layout xml
                // e.g. android:onClick="onScanPress"

                Intent scanIntent = new Intent(SignUpActivity.this, CardIOActivity.class);

                // customize these values to suit your needs.
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default:
                // true
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default:
                // false
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default:
                // false
                // hides the manual entry button
                // if set, developers should provide their own manual entry
                // mechanism in the app
                scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false); // default:
                // false

                // MY_SCAN_REQUEST_CODE is arbitrary and is only used within
                // this activity.
                startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);

            }
        });

        // mCCVEditText.setOnTouchListener(new OnTouchListener() {
        //
        // @Override
        // public boolean onTouch(View v, MotionEvent event) {
        // mCCVEditText.requestLayout();
        // SignUpActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        // return false;
        // }
        // });
        //
        // mCreditCardNumberEditText.setOnTouchListener(new OnTouchListener() {
        //
        // @Override
        // public boolean onTouch(View v, MotionEvent event) {
        // mCreditCardNumberEditText.requestLayout();
        // SignUpActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        // return false;
        // }
        // });
        //
        // mMonthEditText.setOnTouchListener(new OnTouchListener() {
        //
        // @Override
        // public boolean onTouch(View v, MotionEvent event) {
        // mMonthEditText.requestLayout();
        // SignUpActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        // return false;
        // }
        // });
        //
        // mYearEditText.setOnTouchListener(new OnTouchListener() {
        //
        // @Override
        // public boolean onTouch(View v, MotionEvent event) {
        // mYearEditText.requestLayout();
        // SignUpActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        // return false;
        // }
        // });

    }

    private void initClicksInTermsText() {
        String terms_intro = getResources().getString(R.string.sign_up_privacy_terms_intro);
        String terms_privacy_clickable = " " + getResources().getString(R.string.sign_up_privacy_terms_clickable);
        String terms_and = " " + getResources().getString(R.string.sign_up_privacy_policy_and);
        String terms_privacy_policy_clickable = " " + getResources().getString(R.string.sign_up_privacy_policy_clickable);
        String terms_tokenizer = ". " + getResources().getString(R.string.sign_up_privacy_tokenizer);
        String terms_tokenizer_clickable = " " + getResources().getString(R.string.sign_up_privacy_tokenizer_clickable) + ".";

        SpannableString ss = new SpannableString(terms_intro + terms_privacy_clickable + terms_and + terms_privacy_policy_clickable + terms_tokenizer + terms_tokenizer_clickable);
        ClickableSpan clickableSpanTermsOfService = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(SignUpActivity.this, AboutTheAppActivity.class));
            }
        };
        ss.setSpan(clickableSpanTermsOfService, terms_intro.length() + 1, (terms_intro.length() + 1 + terms_privacy_clickable.length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.menu_main_orange)), terms_intro.length() + 1, (terms_intro.length() + 1 + terms_privacy_clickable.length() - 1),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ClickableSpan clickableSpanPrivacyPolicy = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(SignUpActivity.this, AboutTheAppActivity.class));
            }
        };
        ss.setSpan(clickableSpanPrivacyPolicy, (terms_intro.length() + terms_privacy_clickable.length() + terms_and.length() + 1),
                (terms_intro.length() + terms_privacy_clickable.length() + terms_and.length() + terms_privacy_policy_clickable.length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.menu_main_orange)), (terms_intro.length() + terms_privacy_clickable.length() + terms_and.length() + 1),
                (terms_intro.length() + terms_privacy_clickable.length() + terms_and.length() + terms_privacy_policy_clickable.length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ClickableSpan clickableSpanTokenizerSeeHow = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(SignUpActivity.this, TokenizationExplainedActivity.class));
            }
        };
        ss.setSpan(clickableSpanTokenizerSeeHow,
                (terms_intro.length() + terms_privacy_clickable.length() + terms_and.length() + terms_privacy_policy_clickable.length() + terms_tokenizer.length() + 1), (terms_intro.length()
                        + terms_privacy_clickable.length() + terms_and.length() + terms_privacy_policy_clickable.length() + terms_tokenizer.length() + terms_tokenizer_clickable.length() - 1),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.menu_main_orange)), (terms_intro.length() + terms_privacy_clickable.length() + terms_and.length()
                + terms_privacy_policy_clickable.length() + terms_tokenizer.length() + 1), (terms_intro.length() + terms_privacy_clickable.length() + terms_and.length()
                + terms_privacy_policy_clickable.length() + terms_tokenizer.length() + terms_tokenizer_clickable.length() - 1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTextViewTermsOfUse.setText(ss);
        mTextViewTermsOfUse.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

            // Never log a raw card number. Avoid displaying it, but if
            // necessary use getFormattedCardNumber()

            creditCardNumber = scanResult.getRedactedCardNumber();
            // mCreditCardNumberEditText.setText(creditCardNumber);
            mCreditCardNumberEditText.setText(scanResult.getFormattedCardNumber());

            if (scanResult.isExpiryValid()) {
                Log.d("dialog", "Month= " + (scanResult.expiryMonth < 10 ? ("0" + String.valueOf(scanResult.expiryMonth)) : String.valueOf(scanResult.expiryMonth)));
                Log.d("dialog", "Year= " + String.valueOf(scanResult.expiryYear));
                String year = String.valueOf(scanResult.expiryYear).substring(2, 4);
                mYearEditText.setText(year);
                mMonthEditText.setText(scanResult.expiryMonth < 10 ? ("0" + String.valueOf(scanResult.expiryMonth)) : String.valueOf(scanResult.expiryMonth));
            }

            if (scanResult.cvv != null) {
                mCCVEditText.setText(scanResult.cvv);
            }

            mCreditCardNumberEditText.setTextColor(getResources().getColor(R.color.menu_main_gray));
            mMonthEditText.setTextColor(getResources().getColor(R.color.menu_main_gray));
            mYearEditText.setTextColor(getResources().getColor(R.color.menu_main_gray));
            mCCVEditText.setTextColor(getResources().getColor(R.color.menu_main_gray));

        }
    }

}