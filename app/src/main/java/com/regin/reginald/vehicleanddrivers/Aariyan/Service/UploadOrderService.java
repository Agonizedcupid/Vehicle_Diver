package com.regin.reginald.vehicleanddrivers.Aariyan.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.regin.reginald.vehicleanddrivers.Aariyan.Receiver.AlarmBootReceiver;
import com.regin.reginald.vehicleanddrivers.OrderNotUploadedActivity;
import com.regin.reginald.vehicleanddrivers.R;

import org.json.JSONArray;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UploadOrderService extends Service {

    //Tag for checking the targeted result on LogCat:
    private static final String TAG = "MainService";

    //ExecutorService API Instance variable(This is basically a concurrency member variable):
    private ExecutorService mExecutorService;

    //For checking is the service is running or not:
    public static boolean isServiceRunning;

    //Channel id will be used for NotificationChannel:
    private String CHANNEL_ID = "com.regin.reginald.vehicleanddrivers.Aariyan";


    //Policy policy;
    private IntentFilter intentFilter = new IntentFilter();

    public UploadOrderService() {
        isServiceRunning = false;
    }

    @Override
    public void onCreate() {
        //Creating a notification channel for latest android version
        createNotificationChannel();
        //notifying the service is running:
        isServiceRunning = true;
        // Triggering the alarm manager
        AlarmBootReceiver.scheduleAlarms(this);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        //Showing the notification as Foreground service:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            showNotification();
        }
        //Running some background thread for Asynchronous task:
        triggerTheMonitor();
        Toast.makeText(getApplicationContext(), "Service Started!!", Toast.LENGTH_SHORT).show();
        //return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    //It will show the notification to aware the user that, Supervisor is running in the Foreground, Chill!
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void showNotification() {
        //This intent will be used as pending intent; means when user will click on notification tab it will open this activity:
        Intent notificationIntent = new Intent(this, OrderNotUploadedActivity.class);
        //Attaching the pending intent:
        //PendingIntent.FLAG_IMMUTABLE is used for >= android 11:
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                //this is the notification title:
                .setContentTitle("Service is Running")
                //Notification sub-title:
                .setContentText("Activity Tracking!")
                //notification icon:
                .setSmallIcon(R.mipmap.ic_launcher)
                //setting the pending intent on the notification:
                .setContentIntent(pendingIntent)
                //set the background color of intent
                .setColor(getResources().getColor(R.color.colorAccent))
                //Finally build the notification to show:
                .build();
        /**
         * A started service can use the startForeground API to put the service in a foreground state,
         * where the system considers it to be something the user is actively aware of and thus not
         * a candidate for killing when low on memory.
         */
        // it will starting show the ForeGround notification:
        startForeground(new Random().nextInt(), notification);
    }

    //Notification channel is only needed for above Oreo:
    private void createNotificationChannel() {
        //Checking the device OS version:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //App name
            String appName = getString(R.string.app_name);
            //creating the notification channel here and adding all the information:
            NotificationChannel serviceChannel = new NotificationChannel(
                    //Channel id. that could be anything but same package name is recommended:
                    CHANNEL_ID,
                    //Putting the app name to show
                    appName,
                    //This is the importance on notification showing:
                    //For now we are setting as Default:
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            //Instantiating the Notification Manager:
            NotificationManager manager = getSystemService(NotificationManager.class);
            //Finally creating the notification channel and passing as parameter of manager:
            manager.createNotificationChannel(serviceChannel);
        }
    }

    //Running background thread for Asynchronously working:
    public void triggerTheMonitor() {
        //Instantiating the concurrency service:
        mExecutorService = Executors.newSingleThreadExecutor();
        //Creating an object of StartMonitoring class for submitting on the background continuous running:
        //monitoring = new StartMonitoring();
        Monitoring monitoring = new Monitoring();
        //monitoring = new Monitoring(this);
        // Finally, submit the monitoring object to the background concurrency:
        mExecutorService.submit(monitoring);
        Log.e(TAG, "onCreateService: Started, service is running");
    }

    @Override
    public void onDestroy() {
        isServiceRunning = false;
        stopForeground(true);

        Toast.makeText(this, "Service Destroyed!", Toast.LENGTH_SHORT).show();
        if (mExecutorService != null) {
            mExecutorService.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}