package com.example.safedrivelibrary.models;

/**
 * following scopes can be pushed based on Driver ID
 * i.   Automatic Drive on mode (always on)
 * ii.  Manual Mode (Rideshare driver - location start and end known)
 * iii. Automatic Drive - Session Based (ability to specify session start and session end)
 *
 *
 * Created by sarnab.poddar on 5/21/17.
 */

public enum DriveScope {

        AUTO_DRIVE(false),
        MANUAL_MODE(true),
        AUTO_SESSION_DRIVE(true),
        NO_SCOPE(false);


        public boolean isDriverInteractionAllowed() {
                return isDriverInteractionAllowed;
        }

        private boolean isDriverInteractionAllowed;

        DriveScope(boolean driverInteraction)
        {
            this.isDriverInteractionAllowed = driverInteraction;
        }

}
