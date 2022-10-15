package com.regin.reginald.vehicleanddrivers.Aariyan.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

public class AlarmTriggerService extends JobIntentService {

    private static final String TAG = "AlarmTriggerService";

    private static final int UNIQUE_JOB_ID = 9989;

    public static void enqueueWork(Context mContext) {
        enqueueWork(mContext, AlarmTriggerService.class, UNIQUE_JOB_ID,
                new Intent(mContext, AlarmTriggerService.class));
    }

    @Override
    public void onHandleWork(@NonNull Intent intent) {
        Log.e(TAG, "I'm immortal :D");

        if (!UploadOrderService.isServiceRunning) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(this, UploadOrderService.class));
            } else {
                startService(new Intent(this, UploadOrderService.class));
            }
        }

        /*if (!OpenVPNService.isVpnServiceRunning) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(this, OpenVPNService.class));
            } else {
                startService(new Intent(this, OpenVPNService.class));
            }
        }*/
    }
}