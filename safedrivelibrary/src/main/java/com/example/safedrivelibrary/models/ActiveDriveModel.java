package com.example.safedrivelibrary.models;

/**
 * This is the UI model for the Main screen Active session data.
 *
 * Created by sarnab.poddar on 5/21/17.
 */

public class ActiveDriveModel
{

    public boolean isTrackingActive() {
        return isTrackingActive;
    }

    public void setTrackingActive(boolean trackingActive) {
        isTrackingActive = trackingActive;
    }



    public String getmTripID() {
        return mTripID;
    }

    public void setmTripID(String mTripID) {
        this.mTripID = mTripID;
    }



    public String getmStartDateTime() {
        return mStartDateTime;
    }

    public void setmStartDateTime(String mStartDateTime) {
        this.mStartDateTime = mStartDateTime;
    }

    public String getmCurrentSpeed() {
        return mCurrentSpeed;
    }

    public void setmCurrentSpeed(String mCurrentSpeed) {
        this.mCurrentSpeed = mCurrentSpeed;
    }

    public String getDistanceInTrip() {
        return distanceInTrip;
    }

    public void setDistanceInTrip(String distanceInTrip) {
        this.distanceInTrip = distanceInTrip;
    }

    public String getmSessionID() {
        return mSessionID;
    }

    public void setmSessionID(String mSessionID) {
        this.mSessionID = mSessionID;
    }


    public boolean isAutoTracking() {
        return isAutoTracking;
    }

    public void setAutoTracking(boolean autoTracking) {
        isAutoTracking = autoTracking;
    }

    private String mSessionID;
    private String mStartDateTime;
    private String mCurrentSpeed;
    private String distanceInTrip;
    private boolean isTrackingActive;
    private String mTripID;
   private boolean isAutoTracking;


}
