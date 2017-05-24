package com.example.safedrive.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.safedrivelibrary.models.DriverPersona;
import com.google.gson.Gson;

/**
 * Created by sarnab.poddar on 5/21/17.
 */

public class SharedPrefHelper {

    Context appContext;
    private Gson gson;

    public SharedPrefHelper(Context appContext)
    {
        this.appContext = appContext;
    }

    private SharedPreferences getDefaultSharePreference() {
        return appContext.getSharedPreferences(UtilityConstants.APP_PREFERENCE, Context.MODE_PRIVATE);
    }

    private Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }


    /**
     * Save user logged in status
     */
    public void saveUserLoggedStatus(DriverPersona driverPersona) {
        if (driverPersona != null && !TextUtils.isEmpty(driverPersona.getDriverID())) {
            SharedPreferences.Editor editor = getDefaultSharePreference().edit();
            String driverData = getGson().toJson(driverPersona);
            editor.putString(UtilityConstants.DRIVER_ID, driverData);
            editor.apply();
        }
    }

    @Nullable
    public DriverPersona getUserLoggedState() {
        String driverData = getDefaultSharePreference().getString(UtilityConstants.DRIVER_ID, null);

        if(!TextUtils.isEmpty(driverData))
        {
            DriverPersona driverPersona = getGson().fromJson(driverData,DriverPersona.class);
            return driverPersona;
        }
        return null;
    }

    public void setSessionID(String sessionID)
    {
        if(!TextUtils.isEmpty(sessionID))
        {
            SharedPreferences.Editor editor = getDefaultSharePreference().edit();
            editor.putString(UtilityConstants.SESSION_ID, sessionID);
            editor.apply();
            clearTrackingID();
        }
    }

    public String getSessionID()
    {
        return getDefaultSharePreference().getString(UtilityConstants.SESSION_ID, null);
    }

    public void clearSessionID()
    {
        SharedPreferences.Editor editor = getDefaultSharePreference().edit();
        editor.remove(UtilityConstants.SESSION_ID);
        editor.apply();
    }


    public void setTrackingID(String trackingID)
    {
        if(!TextUtils.isEmpty(trackingID))
        {
            SharedPreferences.Editor editor = getDefaultSharePreference().edit();
            editor.putString(UtilityConstants.TRIP_ID, trackingID);
            editor.apply();
            clearSessionID();
        }
    }

    public String getTrackingID()
    {
        return getDefaultSharePreference().getString(UtilityConstants.TRIP_ID, null);
    }

    public void clearTrackingID()
    {
        SharedPreferences.Editor editor = getDefaultSharePreference().edit();
        editor.remove(UtilityConstants.TRIP_ID);
        editor.apply();
    }


    public void setClearLoggedState()
    {
        SharedPreferences.Editor editor = getDefaultSharePreference().edit();
        editor.remove(UtilityConstants.DRIVER_ID);
        editor.apply();

        clearSessionID();
    }
}
