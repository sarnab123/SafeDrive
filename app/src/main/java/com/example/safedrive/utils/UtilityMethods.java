package com.example.safedrive.utils;

import android.support.annotation.NonNull;

/**
 * Created by sarnab.poddar on 5/21/17.
 */

public class UtilityMethods {

    public static byte getScopeforEmail(@NonNull String userName)
    {
        if(userName.contains("auto"))
        {
            return UtilityConstants.AUTH_AUTO;
        }
        else if(userName.contains("manual"))
        {
            return UtilityConstants.AUTH_MANUAL;
        }
        else if(userName.contains("session"))
        {
            return UtilityConstants.AUTH_SESSION;
        }

        return UtilityConstants.NO_SCOPE;
    }
}
