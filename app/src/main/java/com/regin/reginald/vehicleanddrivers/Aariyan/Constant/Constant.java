package com.regin.reginald.vehicleanddrivers.Aariyan.Constant;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Constant {
    public static final String DEFAULT_SHARED_PREF_VALUE = "N/A";
    public static final String LOGGED_IN = "loggedIn";
    public static final String LOGGED_IN_KEYWORD = "log";
    public static final int DB_VERSION = 25;
    public static final String[] title = {"Home", "Credit Request", "Set Up"};
    //public static final String CHECK_URL = "http://102.37.0.48/driversapp/";
    public static final String CHECK_URL = "http://102.37.0.48/driversappdemo/";

    public static String DELIVERY_DATE = getTodayDate();
    public static String ROUTES_NAME = "ROUTES_NAME";
    public static int ORDER_TYPE = -777;


    public static boolean isSetUpCompleted = false;

    public static final String ROUTES_TABLE = "Routes";

    //It will return the today's date using Java Default DateTime APIs:
    public static String getTodayDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
