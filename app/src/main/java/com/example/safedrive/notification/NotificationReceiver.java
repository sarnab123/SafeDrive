package com.example.safedrive.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by sarnab.poddar on 5/22/17.
 */

public class NotificationReceiver extends BroadcastReceiver
{
    public static String NOTIFICATION_FILTER_ONCLICK = "NOTIFICATION_FILTER_ONCLICK";
    public static String NOTIFICATION = "notification";

    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_FILTER_ONCLICK, 0);
        notificationManager.notify(id, notification);

    }
}
