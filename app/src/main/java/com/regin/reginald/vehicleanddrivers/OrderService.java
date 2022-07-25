package com.regin.reginald.vehicleanddrivers;

import android.Manifest;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.regin.reginald.model.Orders;
import com.regin.reginald.model.OrderLines;
import com.regin.reginald.model.OtherAttributes;
import com.regin.reginald.model.SettingsModel;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.regin.reginald.vehicleanddrivers.Aariyan.Database.DatabaseAdapter;

import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import android.location.Location;

import pk.codebase.requests.FormData;
import pk.codebase.requests.HttpError;
import pk.codebase.requests.HttpRequest;
import pk.codebase.requests.HttpResponse;

import static com.loopj.android.http.AsyncHttpClient.log;
import static com.regin.reginald.vehicleanddrivers.AppApplication.CHANNEL_ID;
//import android.os.Handler;

public class OrderService extends Service {
    private BroadcastReceiver mNetworkReceiver;
    public static OrderService instance;
    public OrderService(Context applicationContext) {
        super();
        Log.i("HERE", "here I am!");
    }

    public OrderService() {
    }
    private static final String TAG = "DimsDriver";

    private boolean isRunning = false;
    //private SQLiteDatabase db1, db2, db3, db=null;
    private Cursor c, d;
    String strBarCode, strLon, strLat, strQty, strComments, date_created, strDeviceId, strImageName, strSentToServer, returnedBarCode, returnedToken, returnedstrImageName, strStorename;

    final Context myContext = this;
    //final MyRawQueryHelper dbH = new MyRawQueryHelper(AppApplication.getAppContext());
    final DatabaseAdapter dbH = new DatabaseAdapter(AppApplication.getAppContext());
    LandingPage h = new LandingPage();

    String IP;//="http://192.168.0.18:8181/driver/";
    //String IP="http://so-ca.ddns.net:8179/driver/";
    //String IP="http://linxsystems3.dedicated.co.za:8881/driver/";
    int counter = 0;
    private long LocationInterval = 41000;
    private long LocationFastestInterval = 41000;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    public static String latitude;
    public static String longitude;
    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");
        h.setServiceRunning(true);
        isRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        instance = this;
        startForeground(1, showNotification("Driver Service", startId));
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        startLocationUpdates();
        mNetworkReceiver = new NetworkChangedReceiver();
        registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        AppApplication.serviceRunning(true);

        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onDestroy() {

        unregisterNetworkChanges();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
        AppApplication.serviceRunning(false);
        stopSelf();
        // db.close();
    }
    public void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
            ((NetworkChangedReceiver) mNetworkReceiver).stopRepeatingTask();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    private Notification showNotification(String messageBody, int id) {
        Intent i = new Intent();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, i,
                PendingIntent.FLAG_IMMUTABLE);
        String channelId = getPackageName();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setTicker(getString(R.string.app_name))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setChannelId(channelId)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_PROGRESS)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setLargeIcon(bitmap);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = "Getting Location..";
            final int importance = NotificationManager.IMPORTANCE_NONE;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setSound(null, null);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
        return notificationBuilder.build();
    }
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(LocationInterval);
        mLocationRequest.setFastestInterval(LocationFastestInterval);
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        createLocationRequest();
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());
                    LatLng currentLocation = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
                    System.out.println("Latitude: " + currentLocation);
                    String latitudeAndLongitude = latitude + "," + longitude;
                    pushLocationToServer(latitudeAndLongitude, AppApplication.getCurrenDatetime());

                }
            }
        };
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }
    public void pushLocationToServer(String strLatLng, String dteDateTime) {
        HttpRequest request = new HttpRequest();
        Log.e("locations", " " + "----------------------- ");
        request.setOnResponseListener(response -> {
            if (response.code == pk.codebase.requests.HttpResponse.HTTP_OK) {
                Log.e("HTTP_OK", " " + "----------------------- ");

            } else if (response.code == pk.codebase.requests.HttpResponse.HTTP_BAD_REQUEST) {
                Log.e("HTTP_BAD_REQUEST", " " + "----------------------- ");
            } else if (response.code == HttpResponse.HTTP_NOT_FOUND) {
                Log.e("HTTP_NOT_FOUND", " " + "----------------------- ");
            }
        });
        request.setOnErrorListener(error -> {
            switch (error.code) {
                case HttpError.CONNECTION_TIMED_OUT:
                    break;
                case HttpError.NETWORK_UNREACHABLE:
                    break;
                case HttpError.INVALID_URL:
                    break;
                case HttpError.UNKNOWN:
                    break;
            }
        });
        ArrayList<SettingsModel> sett= dbH.getSettings();
        for (SettingsModel orderAttributes: sett){
            IP = orderAttributes.getstrServerIp();

        }
        ArrayList<OtherAttributes> oD = dbH.returnFilters();
        String dbRoute="XXX";
        String dbLateOrder="XXX";
        String dbDateFrom="XXX";
        for (OtherAttributes orderAttributes : oD) {
            dbRoute = orderAttributes.getroute();
            dbLateOrder = orderAttributes.getordertype();
            dbDateFrom = orderAttributes.getdeliverydate();

        }
        FormData formData;
        formData = new FormData();
        Log.e("locations", " " + "----------------------- "+strLatLng);
        Log.e("dteDateTime", " " + "----------------------- "+dteDateTime);
        Log.e("dteDateTime", " " + "----------------------- "+formData);
        formData.put("strLatLng", strLatLng);
        formData.put("dteDateTime", dbDateFrom);
        formData.put("dteDeliveryDate", dbDateFrom);
        formData.put("strRouteName", dbRoute);
        formData.put("strOrdertype", dbLateOrder );
        request.post(IP+"PostCoordinates.php", formData);
    }
}
