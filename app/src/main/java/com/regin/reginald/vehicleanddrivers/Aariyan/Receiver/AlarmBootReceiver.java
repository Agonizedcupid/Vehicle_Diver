package com.regin.reginald.vehicleanddrivers.Aariyan.Receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.regin.reginald.vehicleanddrivers.Aariyan.Service.AlarmTriggerService;

public class AlarmBootReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmBootReceiver";

    private static final int PERIOD = 60000; // 1 minutes
    private static final int INITIAL_DELAY = 5000; // 5 seconds



    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == null) {
            AlarmTriggerService.enqueueWork(context);
        } else {
            scheduleAlarms(context);
        }

        Log.e(getClass().getSimpleName(), "onReceive: I'm still immortal");
    }

    public static void scheduleAlarms(Context context) {
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, AlarmBootReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_IMMUTABLE);

        mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + INITIAL_DELAY,
                PERIOD, pi);

    }
}