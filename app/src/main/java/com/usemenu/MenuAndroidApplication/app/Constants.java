package com.usemenu.MenuAndroidApplication.app;

/**
 * Created by milos on 8/12/15.
 * <p/>
 * Class for storing or static constants
 */
public class Constants {

    //Splash screen constants
    public static final int REQUEST_CODE_LOCATION = 1;
    public static final int REQUEST_CODE_BLUETOOTH = 2;

    //Splash warning activity constants
    public static final String INTENT_EXTRA_TAG_START = "start";
    public static final int INTENT_EXTRA_START_LOCATION = 1;
    public static final int INTENT_EXTRA_START_BLUETOOTH = 2;
    public static final int SWIPE_MIN_DISTANCE = 120;
    public static final int SWIPE_MAX_OFF_PATH = 250;
    public static final int SWIPE_THRESHOLD_VELOCITY = 200;
    public static final int REQUEST_BLUETOOTH_STATE_CHANGE = 123456;
    public static final int REQUEST_LOCATION_STATE_CHANGE = 1234567;

    //Meals category activity constants
    public static final String KEY_CATEGORIES = "key_categories";
    public static final String KEY_ITEMS = "key_items";
    public static final String KEY_SUBCATEGORY_SELECTED = "key_sub_category_selected";

    //Checkout activity constants
    public static final int REQUEST_LOGIN = 123;
    public static final int REQUEST_PHONE_NUMBER = 122;

    //Meal details activity constants
    public static final String INTENT_EXTRA_TAG = "tag";
    public static final String INTENT_EXTRA_NAME = "name";

    //Receipt list activity constants
    public static final String RECEIPT_LIST_ITEM_SELECTED = "receipt_selected";

    //Sign in activity constants
    public static int REQUEST_SIGN_UP = 54321;

    //SignUp activity constants
    public static int MY_SCAN_REQUEST_CODE = 123; // arbitrary int
}
