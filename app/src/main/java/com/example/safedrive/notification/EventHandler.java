package com.example.safedrive.notification;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.example.safedrive.SafeDriveApplication;
import com.example.safedrive.controller.SafeDriveController;
import com.example.safedrive.utils.UtilityConstants;
import com.example.safedrivelibrary.models.EventOperations;

/**
 *
 * Handler for all events being pushed to main thread
 * Created by sarnab.poddar on 5/22/17.
 */

public class EventHandler extends Handler
{

    private static EventHandler mInstance;

    private EventHandler()
    {
        super(Looper.getMainLooper());
    }

    public static EventHandler getInstance()
    {
        if(mInstance == null)
        {
            mInstance = new EventHandler();
        }

        return mInstance;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(msg != null)
        {

            switch(msg.arg1)
            {
                case EventOperations.DRIVE_START_DETECTED:
                    Intent driveStarted =new Intent(UtilityConstants.UPDATE_UI_ACTION);
                    driveStarted.putExtra(UtilityConstants.INTENT_DRIVE_STARTED, true);
                    new SafeDriveController().startForegroundService();
                    LocalBroadcastManager.getInstance(SafeDriveApplication.getInstance()).sendBroadcast(driveStarted);
                break;

                case EventOperations.DRIVE_END_DETECTED:
                    Intent driveEnded=new Intent(UtilityConstants.UPDATE_UI_ACTION);
                    driveEnded.putExtra(UtilityConstants.INTENT_DRIVE_STARTED, false);
                    LocalBroadcastManager.getInstance(SafeDriveApplication.getInstance()).sendBroadcast(driveEnded);
                break;

                case EventOperations.FUTURE_DRIVE_OPERATION:
                    if(msg.obj != null && msg.obj instanceof String) {
                        new SafeDriveController().startTripWithTripID((String) msg.obj);
                    }
                    break;
                case EventOperations.START_AUTO_OPERATION:

                    Toast.makeText(SafeDriveApplication.getInstance(),"Operation has started successfully", Toast.LENGTH_LONG).show();
                    break;
                case EventOperations.START_TRACKED_DRIVE:
                    Toast.makeText(SafeDriveApplication.getInstance(),"Trip has started, please wait for drive detection", Toast.LENGTH_LONG).show();
                    break;


            }
        }
    }


}
