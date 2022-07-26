package com.regin.reginald.Service;

import android.util.Log;
import android.widget.Toast;

//import com.google.firebase.iid.FirebaseInstanceId;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseInstanceService extends FirebaseMessagingService { //FirebaseInstanceIdService

    private static final String TAG = "MyFirebaseInstanceServi";

    public void onTokenRefresh() {
        // Get updated InstanceID token.
        //String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(MyFirebaseInstanceService.this, "Unable to read token", Toast.LENGTH_SHORT).show();
                    return;
                }
                String refreshedToken = task.getResult();
                FirebaseMessaging.getInstance().subscribeToTopic("all");
                Log.d(TAG, "Refreshed token: " + refreshedToken);

                /* If you want to send messages to this application instance or manage this apps subscriptions on the server side, send the Instance ID token to your app server.*/

                sendRegistrationToServer(refreshedToken);
            }
        });
//        String refreshedToken = FirebaseMessaging.getInstance().getToken();;
//        FirebaseMessaging.getInstance().subscribeToTopic("all");
//        Log.d(TAG, "Refreshed token: " + refreshedToken);

        /* If you want to send messages to this application instance or manage this apps subscriptions on the server side, send the Instance ID token to your app server.*/

        //sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        Log.d("TOKEN ", refreshedToken.toString());
    }
}