package com.example.safedrivelibrary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateUtils;

import com.example.safedrivelibrary.implementation.ZenIntentService;
import com.example.safedrivelibrary.implementation.SafeDriveSDKCallbackImpl;
import com.example.safedrivelibrary.models.ActiveDriveModel;
import com.example.safedrivelibrary.models.DriveScope;
import com.example.safedrivelibrary.models.DriverPersona;
import com.example.zendrivelibrary.BuildConfig;
import com.example.zendrivelibrary.R;
import com.zendrive.sdk.ActiveDriveInfo;
import com.zendrive.sdk.Zendrive;
import com.zendrive.sdk.ZendriveConfiguration;
import com.zendrive.sdk.ZendriveDriveDetectionMode;
import com.zendrive.sdk.ZendriveDriverAttributes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by sarnab.poddar on 5/21/17.
 */

public class SafeDriveManager
{
    private final Context mContext;
    private SafeDriveSDKCallbackImpl zenSDKCallback;

    public SafeDriveManager(Context appContext, boolean isDebugMode)
    {
        this.mContext = appContext;
        this.zenSDKCallback = new SafeDriveSDKCallbackImpl();
    }

    public void initializeSDK(@NonNull DriverPersona driverPersona)
    {
        if(getCurrentState(driverPersona) == null) {

            ZendriveDriverAttributes driverAttributes = new ZendriveDriverAttributes();
            driverAttributes.setFirstName(assertSDKText(driverPersona.getFirstName())?driverPersona.getFirstName():"John");
            driverAttributes.setLastName(assertSDKText(driverPersona.getFirstName())?driverPersona.getFirstName():"Doe");
            driverAttributes.setEmail(assertSDKText(driverPersona.getUserEmail())?driverPersona.getUserEmail():"john@doe.com");
            driverAttributes.setPhoneNumber(assertSDKText(driverPersona.getUserPhoneNumber())?driverPersona.getUserPhoneNumber():"1234567890");

            ZendriveConfiguration zendriveConfiguration = new ZendriveConfiguration(
                    assertSDKText(mContext.getString(R.string.api_key))?mContext.getString(R.string.api_key):"INVALIDKEY", assertSDKText(driverPersona.getDriverID()) ? driverPersona.getDriverID()
                    : "johndoe", ZendriveDriveDetectionMode.AUTO_OFF);
            zendriveConfiguration.setDriverAttributes(driverAttributes);

            Zendrive.setup(
                    mContext,
                    zendriveConfiguration,
                    ZenIntentService.class,
                    zenSDKCallback
            );

            if(driverPersona.getDriveScope() == DriveScope.AUTO_DRIVE)
            {
                changeAutoSetup(true);
            }
        }
    }

    public boolean isInitialized()
    {
        return Zendrive.isSDKSetup();
    }

    @Nullable
    public ActiveDriveModel getCurrentState(@NonNull DriverPersona driverPersona)
    {
        if(isInitialized() && Zendrive.getZendriveState() != null && Zendrive.getZendriveState().isDriveInProgress)
        {
            if(driverPersona.getDriverID() != null && driverPersona.getDriverID().equals(Zendrive.getZendriveState().zendriveConfiguration.getDriverId()))
            {
                ActiveDriveInfo activeDriveInfo = Zendrive.getActiveDriveInfo();

                if(activeDriveInfo != null) {
                    ActiveDriveModel activeDriveModel = new ActiveDriveModel();

                    activeDriveModel.setmTripID(activeDriveInfo.trackingId);
                    activeDriveModel.setmSessionID(activeDriveInfo.sessionId);
                    activeDriveModel.setDistanceInTrip(Double.toString(activeDriveInfo.distanceMeters));
                    activeDriveModel.setmCurrentSpeed(Double.toString(activeDriveInfo.currentSpeed));
                    activeDriveModel.setTrackingActive(true);
                    activeDriveModel.setmStartDateTime(UTCtoLocaltime(activeDriveInfo.startTimeMillis));
                    activeDriveModel.setAutoTracking(TextUtils.isEmpty(activeDriveInfo.trackingId) && TextUtils.isEmpty(activeDriveInfo.sessionId));
                    return activeDriveModel;
                }

            }

        }

        return null;
    }

    private String UTCtoLocaltime(long timestamp)
    {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sdf.setTimeZone(tz);

        CharSequence realTime = DateUtils.getRelativeTimeSpanString(
                timestamp * 1000,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS);


        return realTime.toString();
    }

    public void changeAutoSetup(boolean needAutoTrack)
    {
        if(isInitialized())
        {
            Zendrive.setZendriveDriveDetectionMode(needAutoTrack? ZendriveDriveDetectionMode.AUTO_ON:ZendriveDriveDetectionMode.AUTO_OFF,zenSDKCallback);
        }
    }

    private boolean assertSDKText(String textToCheck)
    {
        if(Zendrive.isValidInputParameter(textToCheck))
        {
            return true;
        }

        return false;
    }

    public void startDrive(String trackingID) {

    }

    public void stopDrive(String trackingID)
    {

    }

    public void startSession(String sessionID) throws IllegalAccessException {
        if(isInitialized())
        {
            Zendrive.startSession(assertSDKText(sessionID)? sessionID:"samplesession");
        }
        else{
            throw new IllegalAccessException();
        }
    }

    public void stopSession(String sessionID)
    {

    }

    public void cleanAndStop()
    {
        if(BuildConfig.DEBUG)
        {
            Zendrive.uploadAllDebugDataAndLogs();
        }

        Zendrive.teardown(zenSDKCallback);
    }




}
