package com.example.safedrive;

import android.app.Application;

import com.example.safedrive.utils.SharedPrefHelper;
import com.example.safedrive.controller.SafeDriveController;
import com.example.safedrivelibrary.SafeDriveManager;

/**
 * Created by sarnab.poddar on 5/21/17.
 */

public class SafeDriveApplication extends Application
{

    private SharedPrefHelper sharedPrefHelper;

    private SafeDriveManager safeDriveManager;

    private static SafeDriveApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        sharedPrefHelper = new SharedPrefHelper(this);
        initializeSafeDriveManager();
    }

    private void initializeSafeDriveManager() {
        if(safeDriveManager == null)
        {
            safeDriveManager = SafeDriveController.initializeSafeDriveManager(this);
        }
    }

    public SharedPrefHelper getSharedPrefHelper() {
        return sharedPrefHelper;
    }

    public static SafeDriveApplication getInstance() {
        return mInstance;
    }


    public SafeDriveManager getSafeDriveManager() {
        return safeDriveManager;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }


}
