package com.example.safedrive.utils;

/**
 *
 * Constants file which will be used to share bundle keys/ shared constants between two classes
 * Created by sarnab.poddar on 5/21/17.
 */

public class UtilityConstants
{

    // TODO : placeholder for auth scope byte which would ideally be sent from server
    public static final byte NO_SCOPE = 0x000000f;
    public static final byte AUTH_AUTO = NO_SCOPE >> 1;
    public static final byte AUTH_MANUAL = NO_SCOPE >> 2;
    public static final byte AUTH_SESSION = NO_SCOPE >> 3;


    // Shared Preferences constants
    public static final String APP_PREFERENCE = "safedriveprefs";
    public static final String DRIVER_ID = "driverID";

    public static final String no_auth_error = "401";
    public static final String already_logged_in_code = "5001";
}
