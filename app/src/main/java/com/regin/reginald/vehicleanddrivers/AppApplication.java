package com.regin.reginald.vehicleanddrivers;

/**
 * Created by Reginald on 31/08/2018.
 */

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AppApplication extends Application {

    private static AppApplication appInstance;
    public static final String CHANNEL_ID = "exampleServiceChannel";

    private static Context sContext;
    public static final String KEY_SERVICE = "service";
    private static boolean success = false;
    public static boolean hasInternet = false;
    @Override
    public void onCreate() {
        super.onCreate();

        appInstance = this;
        sContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return appInstance;
    }
    public static boolean isInternetWorking() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://google.com");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(10000);
                    connection.connect();
                    success = connection.getResponseCode() == 200;
                    hasInternet = success;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        return success;
    }
    public static String getCurrenDatetime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh.mm.ss");
        return sdf.format(c.getTime());
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();

    }
    public static boolean isServiceRunning() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getBoolean(KEY_SERVICE, false);
    }

    public static void serviceRunning(boolean type) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putBoolean(KEY_SERVICE, type).apply();
    }


    public static Context getContext() {
        return sContext;
    }

    public static SharedPreferences getPreferenceManager() {
        return getContext().getSharedPreferences("shared_prefs", MODE_PRIVATE);
    }

    public static void clearSettings() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().clear().commit();
    }
}