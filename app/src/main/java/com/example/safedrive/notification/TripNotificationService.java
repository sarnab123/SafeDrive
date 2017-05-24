package com.example.safedrive.notification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.example.safedrive.SafeDriveApplication;
import com.example.safedrive.utils.UtilityConstants;
import com.example.safedrivelibrary.models.DriverPersona;
import com.example.safedrivelibrary.models.EventOperations;

/**
 * Created by sarnab.poddar on 5/23/17.
 */

public class TripNotificationService extends Service {

    private String TAG = TripNotificationService.class.getName();


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service got created");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG,"Here is in TRIPNOTIFCATION SERVICE");
        if(intent != null && intent.hasExtra(UtilityConstants.FUTURE_DRIVE_TRIPID) && intent.hasExtra(UtilityConstants.FUTURE_DRIVE_DRIVEID))
        {
            Log.d(TAG,"has both intents");
            DriverPersona driverPersona = SafeDriveApplication.getInstance().getSharedPrefHelper().getUserLoggedState();
            if(driverPersona != null && !TextUtils.isEmpty(driverPersona.getDriverID()) && driverPersona.getDriverID().equalsIgnoreCase(intent.getStringExtra(UtilityConstants.FUTURE_DRIVE_DRIVEID))) {
                Log.d(TAG,"starting trip");
                Message message = Message.obtain();
                message.arg1 = EventOperations.FUTURE_DRIVE_OPERATION;
                message.obj = intent.getStringExtra(UtilityConstants.FUTURE_DRIVE_TRIPID);
                EventHandler.getInstance().dispatchMessage(message);
            }

        }
    }

}
