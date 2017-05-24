package com.example.safedrive.controller;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.safedrive.BuildConfig;
import com.example.safedrive.R;
import com.example.safedrive.SafeDriveApplication;
import com.example.safedrive.notification.EventHandler;
import com.example.safedrive.notification.TripNotificationService;
import com.example.safedrive.utils.UtilityConstants;
import com.example.safedrive.viewActivity.SafeDriveActivity;
import com.example.safedrivelibrary.SafeDriveManager;
import com.example.safedrivelibrary.models.ActiveDriveModel;
import com.example.safedrivelibrary.models.DriverPersona;

import java.util.List;

/**
 * Created by sarnab.poddar on 5/21/17.
 */

public class SafeDriveController
{

    @NonNull
    public static SafeDriveManager initializeSafeDriveManager(Context appContext) {
        SafeDriveManager safeDriveManager =  SafeDriveManager.getSafeDriveManager();
        safeDriveManager.updateSDKManager(BuildConfig.DEBUG, EventHandler.getInstance());
        return safeDriveManager;
    }

    @Nullable
    public ActiveDriveModel getCurrentDriveState(@NonNull DriverPersona driverPersona)
    {
        return SafeDriveApplication.getInstance().getSafeDriveManager().getCurrentState(driverPersona);
    }

    public void bootupSafeDriveSDK(@NonNull DriverPersona driverPersona)
    {
        SafeDriveApplication.getInstance().getSafeDriveManager().initializeSDK(driverPersona,SafeDriveApplication.getInstance() );
    }

    public void startDriveSession(@NonNull DriverPersona driverPersona)
    {
        //// TODO: 5/22/17 placeholder sessionid
        //noinspection StringConcatenationMissingWhitespace
        String sessionID = System.currentTimeMillis()+driverPersona.getDriverID();
        SafeDriveApplication.getInstance().getSharedPrefHelper().setSessionID(sessionID);
        try {
            SafeDriveApplication.getInstance().getSafeDriveManager().startSession(sessionID);
        } catch (IllegalAccessException e) {
            bootupSafeDriveSDK(driverPersona);
        }
    }

    public void startTrip(@NonNull DriverPersona driverPersona)
    {
        //noinspection StringConcatenationMissingWhitespace
        String tripID = System.currentTimeMillis()+driverPersona.getDriverID();
        SafeDriveApplication.getInstance().getSharedPrefHelper().setTrackingID(tripID);
        try {
            SafeDriveApplication.getInstance().getSafeDriveManager().startDrive(tripID);
        } catch (IllegalAccessException e) {
            bootupSafeDriveSDK(driverPersona);
        }
    }

    public void startTripWithTripID(String tripID)
    {
        SafeDriveApplication.getInstance().getSharedPrefHelper().setTrackingID(tripID);
        try {
            SafeDriveApplication.getInstance().getSafeDriveManager().startDrive(tripID);
        } catch (IllegalAccessException e) {
            if(SafeDriveApplication.getInstance().getSharedPrefHelper().getUserLoggedState() != null) {
                bootupSafeDriveSDK(SafeDriveApplication.getInstance().getSharedPrefHelper().getUserLoggedState());
            }
        }
    }

    public void stopDrive(@NonNull DriverPersona driverPersona)
    {
        try {
            SafeDriveApplication.getInstance().getSafeDriveManager().stopDrive(SafeDriveApplication.getInstance().getSharedPrefHelper().getTrackingID());
            SafeDriveApplication.getInstance().getSharedPrefHelper().clearTrackingID();
        } catch (IllegalAccessException e) {
            bootupSafeDriveSDK(driverPersona);
        }
    }


    public void stopSession(@NonNull DriverPersona driverPersona)
    {
        try {
            SafeDriveApplication.getInstance().getSafeDriveManager().stopSession(SafeDriveApplication.getInstance().getSharedPrefHelper().getSessionID());
            SafeDriveApplication.getInstance().getSharedPrefHelper().clearSessionID();
        } catch (IllegalAccessException e) {
            bootupSafeDriveSDK(driverPersona);
        }
    }

    public void setUpFutureSession(long futureMillis, Activity alarmAct, String tripID, String driverID)
    {

        AlarmManager alarmMgr = (AlarmManager)SafeDriveApplication.getInstance().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(SafeDriveApplication.getInstance(), TripNotificationService.class);
        intent.putExtra(UtilityConstants.FUTURE_DRIVE_TRIPID,tripID);
        intent.putExtra(UtilityConstants.FUTURE_DRIVE_DRIVEID,driverID);
        PendingIntent alarmIntent = PendingIntent.getService(SafeDriveApplication.getInstance(), UtilityConstants.FUTURE_DRIVE_REQUESTCODE, intent, 0);

        alarmMgr.set(AlarmManager.RTC_WAKEUP,
                futureMillis, alarmIntent);

        Toast.makeText(alarmAct,"Trip has been scheduled", Toast.LENGTH_LONG).show();
    }

    public List<ActiveDriveModel> getUserDriveHistory()
    {
        return SafeDriveApplication.getInstance().getSafeDriveManager().showAllActivity(SafeDriveApplication.getInstance().getSharedPrefHelper().getUserLoggedState());
    }

    public void cleanAndLogout()
    {
        SafeDriveApplication.getInstance().getSafeDriveManager().cleanAndStop();
        SafeDriveApplication.getInstance().getSharedPrefHelper().setClearLoggedState();
    }


    public void startForegroundService() {


            Intent notificationIntent = new Intent(SafeDriveApplication.getInstance(), SafeDriveActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(SafeDriveApplication.getInstance(), 0,
                    notificationIntent, 0);

            Notification foregroundNotification = new NotificationCompat.Builder(SafeDriveApplication.getInstance())
                    .setContentTitle(SafeDriveApplication.getInstance().getResources().getString(R.string.app_name))
                    .setContentText(SafeDriveApplication.getInstance().getResources().getString(R.string.drive_started))
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setSmallIcon(R.drawable.drive_safe_logo)
                    .setContentIntent(pendingIntent)
                    .build();
            foregroundNotification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;

            SafeDriveApplication.getInstance().getSafeDriveManager().setState(true,foregroundNotification);
    }

    public void endForegroundService()
    {
        SafeDriveApplication.getInstance().getSafeDriveManager().setState(false,null);
    }
}
