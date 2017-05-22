package com.example.safedrive.controller;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.safedrive.BuildConfig;
import com.example.safedrive.R;
import com.example.safedrive.SafeDriveApplication;
import com.example.safedrive.notification.NotificationReceiver;
import com.example.safedrivelibrary.SafeDriveManager;
import com.example.safedrivelibrary.models.ActiveDriveModel;
import com.example.safedrivelibrary.models.DriverPersona;

/**
 * Created by sarnab.poddar on 5/21/17.
 */

public class SafeDriveController
{

    @NonNull
    public static SafeDriveManager initializeSafeDriveManager(Context appContext) {
        return new SafeDriveManager(appContext, BuildConfig.DEBUG);
    }

    @Nullable
    public ActiveDriveModel getCurrentDriveState(@NonNull DriverPersona driverPersona)
    {
        return SafeDriveApplication.getInstance().getSafeDriveManager().getCurrentState(driverPersona);
    }

    public void bootupSafeDriveSDK(@NonNull DriverPersona driverPersona)
    {
        SafeDriveApplication.getInstance().getSafeDriveManager().initializeSDK(driverPersona);
    }

    public void startDriveSession(@NonNull DriverPersona driverPersona)
    {
        //// TODO: 5/22/17 placeholder sessionid
        String sessionID = System.currentTimeMillis()+driverPersona.getDriverID();
        try {
            SafeDriveApplication.getInstance().getSafeDriveManager().startSession(sessionID);
        } catch (IllegalAccessException e) {
            bootupSafeDriveSDK(driverPersona);
        }
    }

    public void setUpFutureSession(long futureMillis, Activity alarmAct)
    {
        Notification.Builder builder = new Notification.Builder(alarmAct);
        builder.setContentTitle("Time for a trip");
        builder.setContentText(alarmAct.getText(R.string.notification_content));
        builder.setSmallIcon(R.drawable.drivesafe_logo);


        Intent notificationIntent = new Intent(NotificationReceiver.NOTIFICATION_FILTER_ONCLICK);
        notificationIntent.setAction(NotificationReceiver.NOTIFICATION_FILTER_ONCLICK);
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION, builder.build());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(alarmAct.getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)alarmAct.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 30000, pendingIntent);
    }

    public void cleanAndLogout()
    {
        SafeDriveApplication.getInstance().getSafeDriveManager().cleanAndStop();
        SafeDriveApplication.getInstance().getSharedPrefHelper().setClearLoggedState();
    }


}
