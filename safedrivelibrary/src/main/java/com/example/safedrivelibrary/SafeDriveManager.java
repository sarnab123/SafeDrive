package com.example.safedrivelibrary;

import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.safedrivelibrary.implementation.SafeDriveSDKCallbackImpl;
import com.example.safedrivelibrary.implementation.ZenIntentService;
import com.example.safedrivelibrary.models.ActiveDriveModel;
import com.example.safedrivelibrary.models.DriveScope;
import com.example.safedrivelibrary.models.DriverPersona;
import com.example.safedrivelibrary.models.EventOperations;
import com.example.zendrivelibrary.BuildConfig;
import com.example.zendrivelibrary.R;
import com.zendrive.sdk.AccidentInfo;
import com.zendrive.sdk.ActiveDriveInfo;
import com.zendrive.sdk.AnalyzedDriveInfo;
import com.zendrive.sdk.DriveResumeInfo;
import com.zendrive.sdk.DriveStartInfo;
import com.zendrive.sdk.EstimatedDriveInfo;
import com.zendrive.sdk.Zendrive;
import com.zendrive.sdk.ZendriveConfiguration;
import com.zendrive.sdk.ZendriveDriveDetectionMode;
import com.zendrive.sdk.ZendriveDriverAttributes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by sarnab.poddar on 5/21/17.
 */

public class SafeDriveManager
{
    private Handler mainHandler;

    private static SafeDriveManager mInstance;

    private List<ActiveDriveModel> driverDriveInfo;

    public static final int START_DRIVE_FOREGROUND_INTENT = 100;


    private SafeDriveManager()
    {
        driverDriveInfo = new ArrayList<>();
    }

    public static SafeDriveManager getSafeDriveManager()
    {
        if(mInstance == null)
        {
            mInstance = new SafeDriveManager();
        }

        return mInstance;
    }

    public void updateSDKManager(boolean isDebugMode, Handler mainHandler)
    {
        this.mainHandler = mainHandler;
    }

    public void initializeSDK(@NonNull DriverPersona driverPersona, Context mContext)
    {
        if(!isInitialized()) {

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
                    new SafeDriveSDKCallbackImpl(EventOperations.SETUP_OPERATION,mainHandler)
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
        if(isInitialized() && Zendrive.getZendriveState() != null)
        {
            if(driverPersona.getDriverID() != null && driverPersona.getDriverID().equals(Zendrive.getZendriveState().zendriveConfiguration.getDriverId()))
            {
                ActiveDriveInfo activeDriveInfo = Zendrive.getActiveDriveInfo();

                if(activeDriveInfo != null) {
                    ActiveDriveModel activeDriveModel = new ActiveDriveModel();

                    activeDriveModel.setmTripID(activeDriveInfo.trackingId);
                    activeDriveModel.setmSessionID(activeDriveInfo.sessionId);
                    activeDriveModel.setDistanceInTrip(localeDistance(activeDriveInfo.distanceMeters));
                    activeDriveModel.setmCurrentSpeed(localeSpeed(activeDriveInfo.currentSpeed));
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
        Date localeDate = new Date(timestamp);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm z", Locale.US);
        return dateFormat.format(localeDate);
    }

    private String localeDistance(double distanceMeters)
    {
        double distanceValue = (distanceMeters * 0.000621371);
        String distance = String.format(Locale.US, "%.2f", distanceValue);
        return distance;
    }

    private String localeSpeed(double meterspersecond)
    {
        double metersperhour = meterspersecond * 3600;
        double kmsPerhour = metersperhour / 1000;
        return Double.toString(kmsPerhour);
    }



    public void changeAutoSetup(boolean needAutoTrack)
    {
        if(isInitialized())
        {
            Zendrive.setZendriveDriveDetectionMode(needAutoTrack? ZendriveDriveDetectionMode.AUTO_ON:ZendriveDriveDetectionMode.AUTO_OFF,
                    new SafeDriveSDKCallbackImpl(needAutoTrack? EventOperations.START_AUTO_OPERATION:EventOperations.END_AUTO_OPERATION, mainHandler));
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

    public void setState(boolean isForeground, Notification notification)
    {
        if(isForeground)
        {
            Zendrive.startForeground(START_DRIVE_FOREGROUND_INTENT, notification);
        }
        else {
            Zendrive.stopForeground(true);
        }
    }

    public void startDrive(String trackingID) throws IllegalAccessException {
        if(isInitialized())
        {
            Zendrive.startDrive(assertSDKText(trackingID)?trackingID:"sampletracking",new SafeDriveSDKCallbackImpl(EventOperations.START_TRACKED_DRIVE,mainHandler));
        }
        else{
            throw new IllegalAccessException();
        }
    }

    public void stopDrive(String trackingID) throws IllegalAccessException {
        if(isInitialized())
        {
            Zendrive.stopDrive(assertSDKText(trackingID)?trackingID:"sampletracking",new SafeDriveSDKCallbackImpl(EventOperations.STOP_TRACKED_DRIVE,mainHandler));
        }
        else{
            throw new IllegalAccessException();
        }
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

    public void stopSession(String sessionID) throws IllegalAccessException {
        if(isInitialized())
        {
            Zendrive.stopSession();
        }
        else{
            throw new IllegalAccessException();
        }
    }

    public void cleanAndStop()
    {
        if(BuildConfig.DEBUG)
        {
            Zendrive.uploadAllDebugDataAndLogs();
        }

        Zendrive.teardown(new SafeDriveSDKCallbackImpl(EventOperations.TEARDOWN_SDK, mainHandler));
    }

    public void mapMyTrip()
    {

    }

    public List<ActiveDriveModel> showAllActivity(DriverPersona driverPersona)
    {
        return driverDriveInfo;
    }


    public void updateDriveStart(DriveStartInfo driveStartInfo) {

        SafeDriveSDKCallbackImpl safeSDKCallback =  new SafeDriveSDKCallbackImpl(EventOperations.DRIVE_START_DETECTED,mainHandler);
        safeSDKCallback.updateCallback(driveStartInfo);
    }

    public void updateDriveEnd(EstimatedDriveInfo estimatedDriveInfo) {
        SafeDriveSDKCallbackImpl safeSDKCallback =  new SafeDriveSDKCallbackImpl(EventOperations.DRIVE_END_DETECTED,mainHandler);
        safeSDKCallback.updateCallback(estimatedDriveInfo);
    }

    public void updateDriveAnalyzed(AnalyzedDriveInfo analyzedDriveInfo) {
        setState(false,null);
        if(driverDriveInfo != null)
        {
            ActiveDriveModel activeDriveModel = new ActiveDriveModel();
            activeDriveModel.setDriveScore(analyzedDriveInfo.score.zendriveScore);
            activeDriveModel.setmStartDateTime(UTCtoLocaltime(analyzedDriveInfo.startTimeMillis));
            activeDriveModel.setmEndDateTime(UTCtoLocaltime(analyzedDriveInfo.endTimeMillis));
            activeDriveModel.setDistanceInTrip(localeDistance(analyzedDriveInfo.distanceMeters));
            activeDriveModel.setmTripID(analyzedDriveInfo.trackingId);
            activeDriveModel.setmSessionID(analyzedDriveInfo.sessionId);
            activeDriveModel.setmAverageSpeed(localeSpeed(analyzedDriveInfo.averageSpeed));
            activeDriveModel.setmMaxSpeed(localeSpeed(analyzedDriveInfo.maxSpeed));
            driverDriveInfo.add(activeDriveModel);
        }
    }

    public void updateDriveResumed(DriveResumeInfo driveResumeInfo) {
        SafeDriveSDKCallbackImpl safeSDKCallback =  new SafeDriveSDKCallbackImpl(EventOperations.DRIVE_START_DETECTED,mainHandler);
        safeSDKCallback.updateCallback(driveResumeInfo);
    }

    public void updateOnAccident(AccidentInfo accidentInfo) {
    }

    public void updateLocationPermissionDenied() {
    }
}
