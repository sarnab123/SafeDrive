package com.example.safedrive.viewActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.safedrive.R;
import com.example.safedrive.SafeDriveApplication;
import com.example.safedrive.controller.SafeDriveController;
import com.example.safedrive.controller.listener.IActivityUpdateListener;
import com.example.safedrive.utils.UtilityConstants;
import com.example.safedrivelibrary.models.DriverPersona;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SafeDriveActivity extends AppCompatActivity implements IActivityUpdateListener{


    private String TAG = SafeDriveActivity.class.getName();

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();

    private final int REQUEST_LOCATION_SETTINGS = 1;



    @Nullable
    private View mContentView = null;

    /**
     * All view data related to the main screen
     */
    @Nullable
    private TextView userTextView = null;
    @Nullable
    private Button signOut = null;

    @Nullable
    private ActiveDriveUIHelper activeDriveUIHelper = null;

    @Nullable
    FutureDriveUIHelper futureDriveUIHelper = null;
    @Nullable
    private SafeDriveController safeDriveController = null;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    @Nullable
    private View mControlsView = null;
    @Nullable
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible = false;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_safe_drive);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        activeDriveUIHelper = new ActiveDriveUIHelper(this,findViewById(R.id.id_active_drive_section), this);
        futureDriveUIHelper = new FutureDriveUIHelper(this, findViewById(R.id.id_future_tripsection));

        safeDriveController = new SafeDriveController();

        userTextView = (TextView) findViewById(R.id.id_user_id);
        signOut = (Button) findViewById(R.id.id_sign_out);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignOutClick();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        Button userHistory = (Button)findViewById(R.id.id_user_history);
        userHistory.setOnTouchListener(mDelayHideTouchListener);
        userHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUserHistoryClick();
            }
        });

        bootupSDK(SafeDriveApplication.getInstance().getSharedPrefHelper().getUserLoggedState());

        updateUIonEvent(SafeDriveApplication.getInstance().getSharedPrefHelper().getUserLoggedState());

    }

    private void handleUserHistoryClick() {
        if(safeDriveController.getUserDriveHistory() != null && safeDriveController.getUserDriveHistory().size() > 0) {
            Intent historyIntent = new Intent(getApplicationContext(), HistoryActivity.class);
            startActivity(historyIntent);
        }
        else{
            Toast.makeText(getApplicationContext(),getText(R.string.string_driveNoHistory),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleLocationSettings();
        IntentFilter iff= new IntentFilter(UtilityConstants.UPDATE_UI_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(onDriveStarted, iff);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onDriveStarted);
    }

    @NonNull
    private BroadcastReceiver onDriveStarted = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent != null && intent.hasExtra(UtilityConstants.INTENT_DRIVE_STARTED))
            {
                Log.d(TAG,"DRIVE STARTED RECEIVED IN UI"+intent.getBooleanExtra(UtilityConstants.INTENT_DRIVE_STARTED,false));
            }

            updateUIonEvent(SafeDriveApplication.getInstance().getSharedPrefHelper().getUserLoggedState());
        }
    };

    @Override
    public void handleLocationSettings()
    {
        mayRequestLocation();
    }


    private boolean mayRequestLocation() {

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager == null || !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                displayLocationSettingsRequest(getApplicationContext());
                return false;
            }
            return true;
    }


    private void displayLocationSettingsRequest(@NonNull Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        bootupSDK(SafeDriveApplication.getInstance().getSharedPrefHelper().getUserLoggedState());
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(SafeDriveActivity.this, REQUEST_LOCATION_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_LOCATION_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        bootupSDK(SafeDriveApplication.getInstance().getSharedPrefHelper().getUserLoggedState());
                        break;
                }
                break;
        }
    }

    private void handleSignOutClick()
    {
        if(SafeDriveApplication.getInstance().getSharedPrefHelper().getUserLoggedState() != null)
        {
            if(safeDriveController != null) {
                safeDriveController.cleanAndLogout();
            }
            finish();
        }

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }


    private void bootupSDK(@Nullable DriverPersona userLoggedState)
    {
        if(userLoggedState != null) {
            if(safeDriveController != null) {
                safeDriveController.bootupSafeDriveSDK(userLoggedState);
            }
        }
        else{
            if(mContentView != null) {
                Snackbar.make(mContentView, getText(R.string.error_no_loggedIn), Snackbar.LENGTH_INDEFINITE);
            }
            if(signOut != null) {
                signOut.setText(getText(R.string.action_sign_in_short));
            }
        }
    }


    @Override
    public void updateUIonEvent(@Nullable DriverPersona driverPersona) {

        if(driverPersona == null)
        {
            if(mContentView != null) {
                Snackbar.make(mContentView, getText(R.string.error_no_loggedIn), Snackbar.LENGTH_INDEFINITE);
            }
            if(signOut != null) {
                signOut.setText(getText(R.string.action_sign_in_short));
            }
        }
        else{
            if(signOut != null) {
                signOut.setText(getText(R.string.action_sign_out));
            }
            if(userTextView != null) {
                userTextView.setText(driverPersona.getFirstName() + " " + driverPersona.getLastName() + " " + driverPersona.getDriveScope());
            }

            if(safeDriveController != null) {
                if (activeDriveUIHelper != null) {
                    activeDriveUIHelper.updateUIWithModel(safeDriveController.getCurrentDriveState(driverPersona), driverPersona);
                }

                if (futureDriveUIHelper != null) {
                    futureDriveUIHelper.updateUIWithModel(safeDriveController.getCurrentDriveState(driverPersona), driverPersona);
                }
            }
        }
    }
}
