package com.example.safedrivelibrary.models;

/**
 * Created by sarnab.poddar on 5/22/17.
 */

public class EventOperations
{

    public static final int SETUP_OPERATION = 0;
    public static final int START_AUTO_OPERATION = SETUP_OPERATION + 1;
    public static final int END_AUTO_OPERATION = START_AUTO_OPERATION + 1;
    public static final int TEARDOWN_SDK = END_AUTO_OPERATION + 1;
    public static final int START_TRACKED_DRIVE = TEARDOWN_SDK + 1;
    public static final int STOP_TRACKED_DRIVE = START_TRACKED_DRIVE + 1;
    public static final int DRIVE_START_DETECTED = STOP_TRACKED_DRIVE + 1;
    public static final int DRIVE_END_DETECTED = DRIVE_START_DETECTED + 1;
    public static final int DRIVE_ANALYZED = DRIVE_END_DETECTED + 1;
    public static final int ACCIDENT_DETECTED = DRIVE_ANALYZED + 1;
    public static final int LOCATION_PERMISSION_CHANGED = ACCIDENT_DETECTED + 1;
    public static final int LOCATION_STATUS_CHANGED = LOCATION_PERMISSION_CHANGED + 1;
    public static final int FUTURE_DRIVE_OPERATION = LOCATION_PERMISSION_CHANGED + 1;
}
