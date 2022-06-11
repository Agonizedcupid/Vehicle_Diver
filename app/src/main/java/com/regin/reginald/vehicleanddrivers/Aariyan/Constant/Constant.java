package com.regin.reginald.vehicleanddrivers.Aariyan.Constant;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Constant {

    public static final String[] title = {"Home", "Credit Request", "Set Up"};
    public static final String CHECK_URL = "http://102.37.0.48/driversapp/";

    //It will return the today's date using Java Default DateTime APIs:
    public static String getTodayDate(){
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
