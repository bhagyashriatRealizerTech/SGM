/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.realizer.schoolgenie.managment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.realizer.schoolgenie.managment.utils.Config;
import com.realizer.schoolgenie.managment.utils.Singlton;




/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

    @SuppressWarnings("hiding")
    private static final String TAG = "GCMIntentService";


    public GCMIntentService() {
        super(Config.SENDER_ID);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {

        Log.d(TAG, "Device registered: regId = " + registrationId);
        Config.displayMessage(context, getString(R.string.gcm_registered));

        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String getValueBack = sharedpreferences.getString("UidName", "");
        String empID =getValueBack;

        ServerUtilities.register(context, registrationId, empID);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        //Log.i(TAG, "Device unregistered");
        //displayMessage(context, getString(R.string.gcm_unregistered));
        //SharedPreferences Pref =
        if (GCMRegistrar.isRegisteredOnServer(context)) {
            //ServerUtilities.unregister(context, registrationId);  commented today
        } else {
            // This callback results from the call to unregister made on
            // ServerUtilities when the registration to the server failed.
           // Log.i(TAG, "Ignoring unregister callback");
        }
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
       // Log.i(TAG, "Received message");
         String message = intent.getStringExtra("message");
         Log.d("Received message", message);
       // displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
       // Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
       // displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    @Override
    public void onError(Context context, String errorId) {
       // Log.i(TAG, "Received error: " + errorId);
        //displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
       // Log.i(TAG, "Received recoverable error: " + errorId);
        //displayMessage(context, getString(R.string.gcm_recoverable_error,
                //errorId))
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues search_layout notification to inform the user that server has sent search_layout message.
     */
    private static void generateNotification(Context context, String message) {

        int icon = R.mipmap.school_genie_logo;
        String[] msg=message.split("@@@");
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.d("Message=", message);

        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int nid = sharedpreferences.getInt("NID",0);
        nid =nid+1;
        SharedPreferences.Editor edit = sharedpreferences.edit();
        edit.putInt("NID",nid );
        edit.commit();

        Notification notification ;
        Notification.Builder builder = new Notification.Builder(context);

        String title = context.getString(R.string.app_name);
        Intent notificationIntent = new Intent(context, DashboardActivity.class);
        notificationIntent.putExtra("FragName","Communication");
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            /*notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);*/
        PendingIntent intent =
                PendingIntent.getActivity(context, nid, notificationIntent, 0);

        builder.setAutoCancel(true);
        builder.setContentTitle(title);
        builder.setContentText(msg[6]);
        builder.setSmallIcon(icon);
        builder.setContentIntent(intent);
        builder.setOngoing(true);  //API level 16
        builder.setNumber(100);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.build();
        notification = builder.getNotification();
           /* notification.setLatestEventInfo(context, title, msg[6], intent);
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;*/
        notificationManager.notify(nid, notification);
        Singlton obj = Singlton.getInstance();
        if(obj.getResultReceiver() != null)
        {
            obj.getResultReceiver().send(100,null);
        }
    }

}
