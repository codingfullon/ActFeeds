package com.example.myapp.actfeeds.fcm;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by KavitaPC on 11/2/2017.
 */

public class ActFeedsFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static String LOG_TAG = ActFeedsFirebaseInstanceIDService.class.getSimpleName();
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
       // Log.d(LOG_TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
    }

}
