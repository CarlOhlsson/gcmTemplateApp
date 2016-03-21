/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kits.gcmtemplate.gcmServices;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;


import kits.gcmtemplate.MainActivity;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        System.out.println(data.toString());

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        // If you want to handle these messages differently do so here.
        if (from.startsWith("/topics/")) {
            // Message sent to any topic will go here
        } else {
            // Message sent to this device will go here
        }

        handleData(message);
        showNotification(data);
        sendToUserInterface(message);
    }

    /**
     * Create and show a notification about the received message.
     *
     * @param data GCM bundle received.
     */
    private void showNotification(Bundle data) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setSmallIcon(android.R.drawable.stat_notify_more);
        notificationBuilder.setContentTitle(data.getString("gcm.notification.title"));
        notificationBuilder.setContentText(data.getString("gcm.notification.body"));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    /**
     * Gives you the ability to send the message variable to for example a database.
     * Use if the user is not intended to see the message.
     *
     * @param message GCM message received.
     */
    private void handleData(String message){
        // Do something with the data. (Not related to UI)
    }

    /**
     * Will send the message variable to the UI with broadcast receiver.
     * Use if intention is to show the message directly.
     *
     * @param message GCM message received.
     */
    private void sendToUserInterface(String message){
        Intent intent = new Intent();
        intent.setAction(QuckstartPreferences.MESSAGE_RECEIVED);

        intent.putExtra("Message", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
