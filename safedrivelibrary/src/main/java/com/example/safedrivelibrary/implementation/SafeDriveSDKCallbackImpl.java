package com.example.safedrivelibrary.implementation;

import android.util.Log;

import com.zendrive.sdk.ZendriveOperationCallback;
import com.zendrive.sdk.ZendriveOperationResult;

/**
 * Created by sarnab.poddar on 5/21/17.
 */

public class SafeDriveSDKCallbackImpl implements ZendriveOperationCallback
{

    private String TAG = SafeDriveSDKCallbackImpl.class.getName();
    @Override
    public void onCompletion(ZendriveOperationResult zendriveOperationResult) {
        Log.d(TAG, ""+zendriveOperationResult.isSuccess());
    }
}
