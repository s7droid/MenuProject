package com.usemenu.MenuAndroidApplication.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.usemenu.MenuAndroidApplication.R;
import com.usemenu.MenuAndroidApplication.app.Menu;
import com.usemenu.MenuAndroidApplication.callbacks.OnIBeaconSearchResultCallback;
import com.usemenu.MenuAndroidApplication.dataclasses.DataManager;
import com.usemenu.MenuAndroidApplication.dataclasses.Item;
import com.usemenu.MenuAndroidApplication.dataclasses.Rate;
import com.usemenu.MenuAndroidApplication.utils.Settings;
import com.usemenu.MenuAndroidApplication.utils.Utils;
import com.usemenu.MenuAndroidApplication.views.CircleButtonView;
import com.usemenu.MenuAndroidApplication.volley.VolleySingleton;
import com.usemenu.MenuAndroidApplication.volley.requests.CheckIfPhoneNeededRequest;
import com.usemenu.MenuAndroidApplication.volley.requests.OrderRequest;
import com.usemenu.MenuAndroidApplication.volley.requests.SendReceiptByEmailRequest;
import com.usemenu.MenuAndroidApplication.volley.responses.CheckIfPhoneNeededResponse;
import com.usemenu.MenuAndroidApplication.volley.responses.GsonResponse;
import com.usemenu.MenuAndroidApplication.volley.responses.OrderResponse;
import com.usemenu.MenuAndroidApplication.volley.responses.SendReceiptByEmailResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.usemenu.MenuAndroidApplication.app.Constants.*;

public class CheckoutActivity extends BaseActivity implements OnIBeaconSearchResultCallback {

    private static final String TAG = CheckoutActivity.class.getSimpleName();

    private ArrayList<Item> checkoutList;
    private ArrayList<Item> checkedoutItems;
    private ListView listView;
    private Adapter adapter;
    private CircleButtonView circleButtonAdd;
    private SeekBar seekBar;
    //	private TextView textViewTipPercent;
    private Button buttonCheckout;
    private TextView textViewDesc;
    private LinearLayout layoutAddMore;
    private LinearLayout layoutSeekBar;
    private LinearLayout layoutDiscount;
    private TextView textViewDiscount;
    private View viewDivider;
    private TextView textViewTotalLabel;
    private TextView textViewTotal;
    private TextView textViewTaxPercent;
    private TextView textViewTipTotalPercent;
    private TextView textViewDiscountPercent;
    private TextView textViewSubtotal;
    private TextView textViewTax;
    private TextView textViewTip;
    private Button buttonReceiptListItemSendMessage;
    //	private TextView textViewMinTip;
//	private TextView textViewMaxTip;
    private TextView textviewTipChangableValue;

    private float total;
    private float tax;
    private float minTip;
    private float maxTip;
    private float tip = 0;
    private float discount;
    private String currency;
    private float totalPrice;
    private float disc;
    private float totalTip;
    private float totalTax;
    private boolean isOrderComplete = false;
    private int counter = 0;

    public static boolean isCheckoutClicked = false;

    private DataManager data;

    private OrderResponse orderResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // TODO: comment this
//		Settings.setTableMajor(this, "1");
//		Settings.setTableMinor(this, "1");

        initData();
        initViews();

        // Menu.getInstance().searchForIBeacon(new
        // OnIBeaconSearchResultCallback() {
        //
        // @Override
        // public void onIBeaconSearchResult(int result) {
        //
        // if (!Menu.getInstance().isOrderEnabled() || total == 0) {
        // disableCheckoutButton();
        // } else {
        // enableCheckoutButton();
        // }
        // // dismissProgressDialog();
        // }
        // });

    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshList();

        if (Settings.getAccessToken(CheckoutActivity.this) != null && !Settings.getAccessToken(CheckoutActivity.this).isEmpty() && isCheckoutClicked) {
            showProgressDialogLoading();
            Map<String, String> params = new HashMap<String, String>();
            params.put("accesstoken", Settings.getAccessToken(CheckoutActivity.this));
            params.put("major", Settings.getStoreMajor(CheckoutActivity.this));
            params.put("minor", Settings.getStoreMinor(CheckoutActivity.this));

            CheckIfPhoneNeededRequest request = new CheckIfPhoneNeededRequest(CheckoutActivity.this, params,
                    new Listener<CheckIfPhoneNeededResponse>() {

                        @Override
                        public void onResponse(CheckIfPhoneNeededResponse arg0) {
                            if (arg0.response != null && arg0.response.equals("numberneeded")) {
                                Intent intent = new Intent(CheckoutActivity.this, PickupInfoActivity.class);
                                startActivityForResult(intent, REQUEST_PHONE_NUMBER);
                            } else if (arg0.response != null && arg0.response.equals("notneeded")) {
                                showProgressDialogLoading();
                                checkout();
                            }
                        }
                    });

            VolleySingleton.getInstance(CheckoutActivity.this).addToRequestQueue(request);
        } else {
            isCheckoutClicked = false;
        }

        // TODO: comment this
//		enableCheckoutButton();
//
//		// TODO: comment this
//		if (true)
//			return;
        Menu.getInstance().searchForIBeaconCheckout(this);

    }

    @Override
    protected void onPause() {
        super.onPause();

        Menu.getInstance().unsubscribeIBeaconSearchResultCallback();
    }

    private void refreshList() {
        total = 0;

        checkoutList.clear();
        for (Item item : data.getCheckoutList()) {

            Log.w(TAG, "item name " + item.name);
            Log.w(TAG, "item qty large " + item.quantityLarge);
            Log.w(TAG, "item qty small " + item.quantitySmall);

            if (item.quantityLarge > 0) {
                checkoutList.add(item);
                total += item.quantityLarge * item.largeprice;
            }
            if (item.quantitySmall > 0) {
                checkoutList.add(item);
                total += item.quantitySmall * item.smallprice;
            }
        }

        if (listView.getAdapter().getCount() == 0) {
            finish();
        }

        if (isOrderComplete) {
            for (Item item : checkedoutItems) {
                if (item.quantityLarge > 0) {
                    checkoutList.add(item);
                    total += item.quantityLarge * item.largeprice;
                }
                if (item.quantitySmall > 0) {
                    checkoutList.add(item);
                    total += item.quantitySmall * item.smallprice;
                }
            }
        }

        setData();

        if (!Menu.getInstance().isOrderEnabled() || total == 0) {
            disableCheckoutButton();
        } else {
            enableCheckoutButton();
        }

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        if (checkoutList != null && checkoutList.size() < 6) {
            params.weight = 0;
            params.topMargin = (int) Utils.convertDpToPixel(20, this);
            params.rightMargin = (int) Utils.convertDpToPixel(20, this);
            params.leftMargin = (int) Utils.convertDpToPixel(20, this);
            params.bottomMargin = (int) Utils.convertDpToPixel(10, this);

            params.height = (int) (checkoutList.size() * Utils.convertDpToPixel(40, this));
        } else {
            params.weight = 1;
            params.height = 0;
            params.topMargin = (int) Utils.convertDpToPixel(20, this);
            params.rightMargin = (int) Utils.convertDpToPixel(20, this);
            params.leftMargin = (int) Utils.convertDpToPixel(20, this);
            params.bottomMargin = (int) Utils.convertDpToPixel(10, this);
        }

        listView.setLayoutParams(params);

    }

    private void initData() {

        data = Menu.getInstance().getDataManager();
        checkoutList = new ArrayList<Item>();

        Rate rate = data.getRate(this);
        tax = Utils.round(rate.tax, 2);
        minTip = Utils.round(rate.mintip, 2);
        maxTip = Utils.round(rate.maxtip, 2);
        discount = Utils.round(data.getDiscount(CheckoutActivity.this), 2);
        currency = data.getCurrency(this);
        // tip = Utils.round((maxTip + minTip) / 2.f, 1);
        tip = (int) ((maxTip + minTip) / 2);

    }

    private void initViews() {

        setActionBarForwardArrowVisibility(null);
        setActionBarForwardButtonTextColor(getResources().getColor(R.color.menu_main_orange));
        setActionBarForwardButtonText(R.string.action_bar_order_summary);

        setActionBarBackButtonOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.listView);
        circleButtonAdd = (CircleButtonView) findViewById(R.id.circleButtonAdd);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
//		textViewTipPercent = (TextView) findViewById(R.id.textViewTipPercent);
        buttonCheckout = (Button) findViewById(R.id.buttonCheckout);
        textViewDesc = (TextView) findViewById(R.id.textViewDesc);
        layoutAddMore = (LinearLayout) findViewById(R.id.layoutAddMore);
        layoutSeekBar = (LinearLayout) findViewById(R.id.layoutSeekBar);
        layoutDiscount = (LinearLayout) findViewById(R.id.layoutDiscount);
        textViewDiscount = (TextView) findViewById(R.id.textViewDiscount);
        viewDivider = (View) findViewById(R.id.viewDivider);
        textViewTotalLabel = (TextView) findViewById(R.id.textViewTotalLabel);
        textViewTotal = (TextView) findViewById(R.id.textViewTotal);

        textViewTaxPercent = (TextView) findViewById(R.id.textViewTaxPercent);
        textViewTipTotalPercent = (TextView) findViewById(R.id.textViewTipTotalPercent);
        textViewDiscountPercent = (TextView) findViewById(R.id.textViewDiscountPercent);
        textViewSubtotal = (TextView) findViewById(R.id.textViewSubtotal);
        textViewTax = (TextView) findViewById(R.id.textViewTax);
        textViewTip = (TextView) findViewById(R.id.textViewTip);
        buttonReceiptListItemSendMessage = (Button) findViewById(R.id.buttonReceiptListItemSendMessage);
        textviewTipChangableValue = (TextView) findViewById(R.id.textviewTipChangebleValue);
//		textViewMinTip = (TextView) findViewById(R.id.textViewMinTip);
//		textViewMaxTip = (TextView) findViewById(R.id.textViewMaxTip);

        circleButtonAdd.setAsAddOrange();

        adapter = new Adapter(this, checkoutList);
        listView.setAdapter(adapter);

        int tipRange = (int) ((maxTip - minTip) * 10);

        seekBar.setMax(tipRange);
        seekBar.setProgress((int) ((tip - minTip) * 10));

        textViewTaxPercent.setText(String.valueOf(tax) + "%");
        textViewDiscountPercent.setText(String.valueOf(discount) + "%");
//		textViewMinTip.setText(minTip + "%");
//		textViewMaxTip.setText(maxTip + "%");

        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // tip = ((float) progress) / 10.f + minTip;
                tip = ((int) progress) / 10 + minTip;
                setData();
            }
        });

        buttonCheckout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                disableCheckoutButton();
                showProgressDialogLoading();

                // TODO: comment this block
//				{
//
//					if (!Menu.getInstance().isOrderEnabled() || !Menu.getInstance().isInARestaurant()) {
//						showAlertDialog(R.string.dialog_title_warning, R.string.dialog_cannot_order);
//						enableCheckoutButton();
//						dismissProgressDialog();
//						return;
//					}
//
//					if (!Settings.getAccessToken(getApplicationContext()).equals("")) {
//
//						showProgressDialogLoading();
//
//						System.out.println("accesstoken= " + Settings.getAccessToken(getApplicationContext()));
//
//						Map<String, String> params = new HashMap<String, String>();
//						params.put("accesstoken", Settings.getAccessToken(CheckoutActivity.this));
//						params.put("major", Settings.getStoreMajor(CheckoutActivity.this));
//						params.put("minor", Settings.getStoreMinor(CheckoutActivity.this));
//
//						CheckIfPhoneNeededRequest request = new CheckIfPhoneNeededRequest(CheckoutActivity.this, params,
//								new Listener<CheckIfPhoneNeededResponse>() {
//
//									@Override
//									public void onResponse(CheckIfPhoneNeededResponse arg0) {
//
//										enableCheckoutButton();
//
//										if (arg0.response != null && arg0.response.equals("numberneeded")) {
//											dismissProgressDialog();
//											Intent intent = new Intent(CheckoutActivity.this, PickupInfoActivity.class);
//											startActivityForResult(intent, REQUEST_PHONE_NUMBER);
//										} else if (arg0.response != null && arg0.response.equals("notneeded")) {
//											checkout();
//										}
//									}
//								});
//
//						VolleySingleton.getInstance(CheckoutActivity.this).addToRequestQueue(request);
//
//					} else {
//						isCheckoutClicked = true;
//						Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
//						startActivityForResult(intent, REQUEST_LOGIN);
//						dismissProgressDialog();
//						enableCheckoutButton();
//					}
//					if (true)
//						return;
//				}

                Menu.getInstance().searchForIBeacon(new OnIBeaconSearchResultCallback() {

                    @Override
                    public void onIBeaconSearchResult(int result) {

                        if (!Menu.getInstance().isOrderEnabled() || !Menu.getInstance().isInARestaurant()) {
                            showAlertDialog(R.string.dialog_title_warning, R.string.dialog_cannot_order);
                            enableCheckoutButton();
                            dismissProgressDialog();
                            return;
                        }

                        if (!Settings.getAccessToken(getApplicationContext()).equals("")) {

                            showProgressDialogLoading();

                            System.out.println("accesstoken= " + Settings.getAccessToken(getApplicationContext()));

                            Map<String, String> params = new HashMap<String, String>();
                            params.put("accesstoken", Settings.getAccessToken(CheckoutActivity.this));
                            params.put("major", Settings.getStoreMajor(CheckoutActivity.this));
                            params.put("minor", Settings.getStoreMinor(CheckoutActivity.this));

                            CheckIfPhoneNeededRequest request = new CheckIfPhoneNeededRequest(CheckoutActivity.this, params,
                                    new Listener<CheckIfPhoneNeededResponse>() {

                                        @Override
                                        public void onResponse(CheckIfPhoneNeededResponse arg0) {

                                            enableCheckoutButton();

                                            if (arg0.response != null && arg0.response.equals("numberneeded")) {
                                                dismissProgressDialog();
                                                Intent intent = new Intent(CheckoutActivity.this, PickupInfoActivity.class);
                                                startActivityForResult(intent, REQUEST_PHONE_NUMBER);
                                            } else if (arg0.response != null && arg0.response.equals("notneeded")) {
                                                checkout();
                                            }
                                        }
                                    });

                            VolleySingleton.getInstance(CheckoutActivity.this).addToRequestQueue(request);

                        } else {
                            isCheckoutClicked = true;
                            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                            startActivityForResult(intent, REQUEST_LOGIN);
                            dismissProgressDialog();
                            enableCheckoutButton();
                        }
                    }
                });

            }
        });

        circleButtonAdd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                startActivity(new Intent(CheckoutActivity.this, CategoryMealsActivity.class));
            }
        });

        if (discount == 0) {
            layoutDiscount.setVisibility(View.GONE);
            textViewDiscount.setVisibility(View.GONE);
        }

    }

    @Override
    public void onVolleyError(VolleyError volleyError) {
        super.onVolleyError(volleyError);

        enableCheckoutButton();
    }

    @Override
    public void onResponseError(GsonResponse response) {

        Log.e(TAG, "onResponseError " + response.errordata);

        dismissProgressDialog();

        if (response.response != null && response.response.equals("wronglogindetails")) {
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
            dismissProgressDialog();
            enableCheckoutButton();

            return;
        } else if (response.response != null && response.response.equals("ccdeclined")) {
            if (isOrderComplete)
                return;
            showAlertDialog(R.string.dialog_title_error, R.string.dialog_card_declined);
        } else {
            showAlertDialog(getString(R.string.dialog_body_default_error_title), getString(R.string.dialog_body_default_error_message));
        }

        resetCheckoutListBool();

        enableCheckoutButton();
    }

    private void resetCheckoutListBool() {
        for (Item item : checkoutList) {
            item.smallAdded = false;
        }
    }

    private void setData() {

        disc = Utils.round(total * discount / 100, 2);
        float totalDisc = total - disc;

        totalTip = Utils.round(totalDisc * tip / 100, 2);

        totalPrice = Utils.round(totalDisc + totalTip, 2);

        totalTax = Utils.round((totalPrice / (1 + tax / 100)) * (tax / 100), 2);

        // totalTax = Utils.round(totalDisc * tax / 100, 2);

//		textViewTipPercent.setText(tip + "% - " + currency + String.format("%.2f", totalTip));

        textViewTipTotalPercent.setText(tip + "%");
        textviewTipChangableValue.setText((int) tip + "%");

        textViewSubtotal.setText(String.format("%.2f", total));
        textViewTax.setText(String.format("%.2f", totalTax));
        textViewTip.setText(String.format("%.2f", totalTip));
        textViewDiscount.setText(String.format("%.2f", disc));
        textViewTotalLabel.setText(getString(R.string.checkout_total) + " " + currency);
        textViewTotal.setText(String.format("%.2f", totalPrice));

        adapter.notifyDataSetChanged();

    }

    private void onSuccessfulCheckout() {

        isCheckoutClicked = false;

        textViewDesc.setVisibility(View.VISIBLE);
        buttonReceiptListItemSendMessage.setVisibility(View.VISIBLE);
        layoutAddMore.setVisibility(View.GONE);
        layoutSeekBar.setVisibility(View.GONE);
        layoutDiscount.setVisibility(View.VISIBLE);
        textViewDiscount.setVisibility(View.VISIBLE);
        viewDivider.setBackgroundColor(getResources().getColor(R.color.menu_main_gray_light));
        textViewTotalLabel.setTextColor(getResources().getColor(R.color.menu_main_gray));
        textViewTotal.setTextColor(getResources().getColor(R.color.menu_main_gray));
        setActionBarForwardButtonText(R.string.action_bar_receipt);
        setActionBarBackButtonText(getString(R.string.action_bar_add_items));
        buttonCheckout.setText(R.string.checkout_enjoy);
        buttonCheckout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckoutActivity.this, CategoryMealsActivity.class);
                startActivity(intent);
            }
        });

        setActionBarBackButtonOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckoutActivity.this, CategoryMealsActivity.class);
                startActivity(intent);
            }
        });

        setActionBarBackButtonText(R.string.action_bar_order_again);

        String thankYou = getString(R.string.checkout_thank_you_for_order);
        SpannableStringBuilder ssb = new SpannableStringBuilder(thankYou + " " + getString(R.string.checkout_receipt_sent));

        final StyleSpan bss1 = new StyleSpan(android.graphics.Typeface.BOLD);
        ssb.setSpan(bss1, 0, thankYou.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        textViewDesc.setText(ssb);

        adapter = new Adapter(this, checkedoutItems);
        adapter.disableClicks();
        listView.setAdapter(adapter);
        listView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                checkoutList.clear();
                Menu.getInstance().getDataManager().clearCheckoutList();
            }
        });

        buttonReceiptListItemSendMessage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (buttonReceiptListItemSendMessage.getText().toString().equals(getString(R.string.receipt_send_email_button_sent))) {
                    return;
                }

                showProgressDialogLoading();

                Map<String, String> params = new HashMap<String, String>();
                params.put("accesstoken", Settings.getAccessToken(CheckoutActivity.this));
                params.put("receiptid", orderResponse.receiptid);
                SendReceiptByEmailRequest sendEmailRequest = new SendReceiptByEmailRequest(CheckoutActivity.this, params,
                        new Listener<SendReceiptByEmailResponse>() {

                            @Override
                            public void onResponse(SendReceiptByEmailResponse response) {

                                if (response.response != null && response.response.equals("success")) {
                                    dismissProgressDialog();
                                    buttonReceiptListItemSendMessage.setText(getString(R.string.receipt_send_email_button_sent));
                                    buttonReceiptListItemSendMessage.setEnabled(false);
                                }
                            }
                        });

                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(sendEmailRequest);
            }
        });

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        params.height = 0;
        params.topMargin = (int) Utils.convertDpToPixel(20, this);
        params.rightMargin = (int) Utils.convertDpToPixel(20, this);
        params.leftMargin = (int) Utils.convertDpToPixel(20, this);
        params.bottomMargin = (int) Utils.convertDpToPixel(10, this);

        listView.setLayoutParams(params);

        if (discount == 0) {
            layoutDiscount.setVisibility(View.GONE);
            textViewDiscount.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_LOGIN) {
                // Map<String, String> params = new HashMap<String, String>();
                // params.put("accesstoken",
                // Settings.getAccessToken(CheckoutActivity.this));
                // params.put("major",
                // Settings.getStoreMajor(CheckoutActivity.this));
                // params.put("minor",
                // Settings.getStoreMinor(CheckoutActivity.this));
                //
                // CheckIfPhoneNeededRequest request = new
                // CheckIfPhoneNeededRequest(CheckoutActivity.this, params,
                // new Listener<CheckIfPhoneNeededResponse>() {
                //
                // @Override
                // public void onResponse(CheckIfPhoneNeededResponse arg0) {
                // if (arg0.response != null &&
                // arg0.response.equals("numberneeded")) {
                // Intent intent = new Intent(CheckoutActivity.this,
                // PickupInfoActivity.class);
                // startActivityForResult(intent, REQUEST_PHONE_NUMBER);
                // } else if (arg0.response != null &&
                // arg0.response.equals("notneeded")) {
                // showProgressDialogLoading();
                // checkout();
                // }
                // }
                // });
                //
                // VolleySingleton.getInstance(CheckoutActivity.this).addToRequestQueue(request);
            }

            // if (requestCode == REQUEST_PHONE_NUMBER) {
            // showProgressDialogLoading();
            // checkout();
            // }
        }
    }

    private void checkout() {

        Calendar cal = Calendar.getInstance();

        // TODO: see how will this beneath do
        // checkedoutItems = new ArrayList<Item>();
        // checkedoutItems.addAll(checkoutList);

        // List<Item> copy = new ArrayList<Item>(checkoutList.size());
        //
        // for (Item foo: checkoutList) {
        // copy.add((Item)foo.clone());
        // }

        checkedoutItems = (ArrayList<Item>) cloneList(checkoutList);

        Log.e(TAG, "CHECKOUT BEFORE NEWORDER");

        // /////////////
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "neworder");
        params.put("major", Settings.getTableMajor(CheckoutActivity.this));
        params.put("minor", Settings.getTableMinor(CheckoutActivity.this));
        params.put("language", data.getLanguage());
        params.put("accesstoken", Settings.getAccessToken(this));
        params.put("itemcount", String.valueOf(checkoutList.size()));
        params.put("orderprice", String.format("%.2f", total));
        params.put("discount", String.format("%.2f", disc));
        params.put("tip", String.format("%.2f", totalTip));
        params.put("tax", String.format("%.2f", totalTax));
        params.put("totalprice", String.format("%.2f", totalPrice));
        params.put("day", String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        params.put("month", String.valueOf(cal.get(Calendar.MONTH) + 1));
        params.put("year", String.valueOf(cal.get(Calendar.YEAR)));
        int minute = cal.get(Calendar.MINUTE);
        params.put("time", String.valueOf(cal.get(Calendar.HOUR_OF_DAY) + ":" + (minute > 9 ? minute : "0" + minute)));

        OrderRequest createOrderRequest = new OrderRequest(this, params, new Listener<OrderResponse>() {

            @Override
            public void onResponse(final OrderResponse response) {

                if (response.response != null && response.response.equals("success")) {
                    new AsyncTask<Void, Void, Boolean>() {

                        @Override
                        protected Boolean doInBackground(Void... params) {

                            final CountDownLatch countDownLatch = new CountDownLatch(checkoutList.size());

                            for (Item item : checkoutList) {
                                Map<String, String> itemParams = new HashMap<String, String>();
                                itemParams.put("type", "addtoorder");
                                itemParams.put("major", Settings.getTableMajor(CheckoutActivity.this));
                                itemParams.put("minor", Settings.getTableMinor(CheckoutActivity.this));
                                itemParams.put("language", data.getLanguage());
                                itemParams.put("tag", String.valueOf((item.quantitySmall > 0 && !item.smallAdded) ? item.smalltag : item.largetag));
                                itemParams.put("amount",
                                        String.valueOf((item.quantitySmall > 0 && !item.smallAdded) ? item.quantitySmall : item.quantityLarge));

                                if (item.quantitySmall > 0)
                                    item.smallAdded = true;

                                itemParams.put("orderid", response.orderid);
                                itemParams.put("accesstoken", Settings.getAccessToken(CheckoutActivity.this));

                                if (!(item.quantitySmall > 0 && !item.smallAdded) && (item.comment != null && !item.comment.isEmpty())) {
                                    itemParams.put("comment", item.comment);
                                    item.comment = "";
                                }

                                System.out.println("tag= " + String.valueOf(item.quantitySmall > 0 ? item.smalltag : item.largetag));

                                OrderRequest itemRequest = new OrderRequest(CheckoutActivity.this, itemParams, new Listener<OrderResponse>() {

                                    @Override
                                    public void onResponse(OrderResponse response) {

                                        if (response.response != null && response.response.equals("success"))
                                            countDownLatch.countDown();
                                    }
                                });

                                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(itemRequest);
                            }

                            boolean success = false;
                            try {
                                success = countDownLatch.await(20000, TimeUnit.MILLISECONDS);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            return success;
                        }

                        @Override
                        protected void onPostExecute(Boolean result) {

                            System.err.println("onPostExecute");
                            if (result) {
                                // We had to put this counter, because of the
                                // multiple calls of checkout() method: from
                                // onActivityResult and onResume. This is caused
                                // due to sometimes when user want to checkout,
                                // and he doesn't have an account, and goes to
                                // SignUp, he will be transfered in onResume
                                // method, and everything will be fine, but the
                                // problem happens when user do the login, that
                                // is when onResume and onActivityResult calls,
                                // and in each method, we are calling checkout()
                                // method.
                                counter++;
                                Map<String, String> executeParams = new HashMap<String, String>();
                                executeParams.put("type", "execute");
                                executeParams.put("major", Settings.getTableMajor(CheckoutActivity.this));
                                executeParams.put("minor", Settings.getTableMinor(CheckoutActivity.this));
                                executeParams.put("language", data.getLanguage());
                                executeParams.put("orderid", response.orderid);
                                executeParams.put("accesstoken", Settings.getAccessToken(CheckoutActivity.this));

                                OrderRequest executeRequest = new OrderRequest(CheckoutActivity.this, executeParams, new Listener<OrderResponse>() {

                                    @Override
                                    public void onResponse(OrderResponse response) {
                                        Log.e("afterLogin", "OrderRequestResponse");
                                        if (response.response != null && response.response.equals("success")) {
                                            orderResponse = response;
                                            resetCheckoutListBool();
                                            dismissProgressDialog();
                                            // showAlertDialog(R.string.dialog_title_success,
                                            // R.string.dialog_order_successful);
                                            isOrderComplete = true;
                                            // TODO: commented, moved to the
                                            // start of the method
                                            // checkedoutItems = new
                                            // ArrayList<Item>();
                                            // checkedoutItems.addAll(checkoutList);
                                            onSuccessfulCheckout();
                                        }
                                    }
                                });

                                if (counter == 1)
                                    VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(executeRequest);

                            } else {
                                Log.d(TAG, "latch said false");
                                dismissProgressDialog();
                                showAlertDialog(getString(R.string.dialog_body_default_error_title),
                                        getString(R.string.dialog_body_default_error_message));
                            }
                        }
                    }.execute();
                }

            }
        });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(createOrderRequest);
    }

    class Adapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        private ArrayList<Item> checkoutList;
        private boolean disableClicks = false;

        public Adapter(Context context, ArrayList<Item> checkoutList) {
            this.checkoutList = checkoutList;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return checkoutList.size();
        }

        @Override
        public Item getItem(int position) {
            return checkoutList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.row_checkout_list, parent, false);
                holder = new ViewHolder();

                holder.circleButtonViewQty = (CircleButtonView) convertView.findViewById(R.id.circleButtonViewQty);
                holder.textViewName = (TextView) convertView.findViewById(R.id.textViewName);
                holder.textViewPrice = (TextView) convertView.findViewById(R.id.textViewPrice);
                holder.circleButtonViewDel = (CircleButtonView) convertView.findViewById(R.id.circleButtonViewDel);
                holder.circleButtonViewMinus = (CircleButtonView) convertView.findViewById(R.id.circleButtonViewMinus);
                holder.circleButtonViewPlus = (CircleButtonView) convertView.findViewById(R.id.circleButtonViewPlus);
                holder.textViewQty = (TextView) convertView.findViewById(R.id.textViewQty);
                holder.buttonDone = (Button) convertView.findViewById(R.id.buttonDone);
                holder.layoutName = (LinearLayout) convertView.findViewById(R.id.layoutName);
                holder.layoutMinusPlus = (LinearLayout) convertView.findViewById(R.id.layoutMinusPlus);
                holder.textViewIsSmall = (TextView) convertView.findViewById(R.id.textViewIsSmall);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Item item = getItem(position);

            holder.position = position;

            if (position == 0) {
                if (item.quantityLarge > 0) {
                    holder.isSmall = false;
                } else {
                    holder.isSmall = true;
                }
            } else {
                Item prevItem = getItem(position - 1);
                if (prevItem.largetag == item.largetag) {
                    holder.isSmall = true;
                } else {
                    if (item.quantityLarge > 0)
                        holder.isSmall = false;
                    else
                        holder.isSmall = true;
                }
            }

            holder.circleButtonViewQty.setAsQty(holder.isSmall ? item.quantitySmall : item.quantityLarge);
            holder.textViewName.setText(item.name);
            holder.textViewPrice.setText(String.format("%.2f", holder.isSmall ? (item.smallprice * item.quantitySmall)
                    : (item.largeprice * item.quantityLarge)));
            holder.circleButtonViewDel.setAsDel();
            holder.circleButtonViewMinus.setAsRemoveGrey();
            holder.circleButtonViewPlus.setAsAddGrey();
            holder.textViewQty.setText(String.valueOf(holder.isSmall ? item.quantitySmall : item.quantityLarge));
            // holder.textViewIsSmall.setVisibility(holder.isSmall ?
            // View.VISIBLE : View.GONE);

            if (!disableClicks) {
                convertView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewHolder holder = (ViewHolder) v.getTag();
                        if (holder.layoutName.getVisibility() == View.VISIBLE) {
                            holder.layoutName.setVisibility(View.GONE);
                            holder.layoutMinusPlus.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }

            if (!disableClicks) {
                holder.buttonDone.setTag(holder);
                holder.buttonDone.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewHolder holder = (ViewHolder) v.getTag();
                        holder.layoutMinusPlus.setVisibility(View.GONE);
                        holder.layoutName.setVisibility(View.VISIBLE);
                        holder.circleButtonViewQty.setAsQty(holder.isSmall ? getItem(holder.position).quantitySmall
                                : getItem(holder.position).quantityLarge);
                    }
                });
            }

            if (!disableClicks) {
                holder.circleButtonViewMinus.setTag(holder);
                holder.circleButtonViewMinus.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewHolder holder = (ViewHolder) v.getTag();
                        int qty = holder.isSmall ? getItem(holder.position).quantitySmall : getItem(holder.position).quantityLarge;
                        if (qty > 1) {
                            holder.textViewQty.setText(String.valueOf(holder.isSmall ? --getItem(holder.position).quantitySmall
                                    : --getItem(holder.position).quantityLarge));
                            refreshList();
                        }
                    }
                });
            }

            if (!disableClicks) {
                holder.circleButtonViewPlus.setTag(holder);
                holder.circleButtonViewPlus.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewHolder holder = (ViewHolder) v.getTag();
                        int qty = holder.isSmall ? getItem(holder.position).quantitySmall : getItem(holder.position).quantityLarge;
                        if (qty < 99) {
                            holder.textViewQty.setText(String.valueOf(holder.isSmall ? ++getItem(holder.position).quantitySmall
                                    : ++getItem(holder.position).quantityLarge));
                            refreshList();
                        }

                        Log.d(TAG, "quantity large " + getItem(holder.position).quantityLarge);
                        Log.d(TAG, "quantity small " + getItem(holder.position).quantitySmall);
                    }
                });
            }

            if (!disableClicks) {
                holder.circleButtonViewDel.setTag(holder);
                holder.circleButtonViewDel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ViewHolder holder = (ViewHolder) v.getTag();
                        Item item = getItem(holder.position);
                        if (holder.isSmall) {
                            if (item.quantityLarge > 0) {
                                item.quantitySmall = 0;
                            } else {
                                item.quantitySmall = 1;
                                Menu.getInstance().getDataManager().removeCheckoutListItem(item.smalltag);
                            }
                            checkoutList.remove(holder.position);
                        } else {
                            if (item.quantitySmall > 0) {
                                item.quantityLarge = 0;
                            } else {
                                item.quantityLarge = 1;
                                Menu.getInstance().getDataManager().removeCheckoutListItem(item.largetag);
                            }
                            checkoutList.remove(holder.position);
                        }

                        refreshList();
                    }
                });
            }

            if (disableClicks) {
                holder.circleButtonViewDel.setVisibility(View.GONE);
                LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.rightMargin = 0;
                params.leftMargin = 0;
                holder.textViewPrice.setLayoutParams(params);
            }

            return convertView;
        }

        public void disableClicks() {
            disableClicks = true;
            notifyDataSetChanged();
        }

        class ViewHolder {
            CircleButtonView circleButtonViewQty;
            TextView textViewName;
            TextView textViewPrice;
            CircleButtonView circleButtonViewDel;
            LinearLayout layoutName;
            LinearLayout layoutMinusPlus;
            CircleButtonView circleButtonViewMinus;
            CircleButtonView circleButtonViewPlus;
            TextView textViewQty;
            TextView textViewIsSmall;
            Button buttonDone;
            int position;
            boolean isSmall;
        }

    }

    private void enableCheckoutButton() {
        buttonCheckout.setEnabled(true);
        buttonCheckout.setShadowLayer(5, 5, 5, getResources().getColor(R.color.menu_main_orange_dark));
    }

    private void disableCheckoutButton() {
        buttonCheckout.setEnabled(false);
        buttonCheckout.setShadowLayer(0, 0, 0, getResources().getColor(android.R.color.transparent));
    }

    // Method for copying list
    public static List<Item> cloneList(List<Item> list) {
        List<Item> clone = new ArrayList<Item>(list.size());
        for (Item item : list)
            clone.add((Item) item.clone());
        return clone;
    }

    @Override
    public void onIBeaconSearchResult(int result) {

        if (!Menu.getInstance().isOrderEnabled() || total == 0) {
            disableCheckoutButton();
        } else {
            enableCheckoutButton();
        }
    }

}