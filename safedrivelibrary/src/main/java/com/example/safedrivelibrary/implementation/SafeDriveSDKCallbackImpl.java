package com.example.safedrivelibrary.implementation;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zendrive.sdk.ZendriveOperationCallback;
import com.zendrive.sdk.ZendriveOperationResult;

/**
 * Created by sarnab.poddar on 5/21/17.
 */

public class SafeDriveSDKCallbackImpl implements ZendriveOperationCallback
{

    private String TAG = SafeDriveSDKCallbackImpl.class.getName();

    private int mOperation;
    private Handler mEventHandler;

    public SafeDriveSDKCallbackImpl(int operation , Handler eventHandler)
    {
        this.mOperation = operation;
        this.mEventHandler = eventHandler;
    }

    public void updateCallback(Object dataObject)
    {
        Message message = Message.obtain();
        message.arg1 = mOperation;
        message.obj = dataObject;
        mEventHandler.dispatchMessage(message);
    }

    @Override
    public void onCompletion(ZendriveOperationResult zendriveOperationResult) {

        Log.d(TAG, ""+zendriveOperationResult.isSuccess());

        Message message = Message.obtain();
        message.arg1 = mOperation;
        message.obj = zendriveOperationResult.isSuccess();
        mEventHandler.dispatchMessage(message);

    }
}
