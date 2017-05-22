package com.example.safedrive.viewActivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.safedrive.R;
import com.example.safedrive.SafeDriveApplication;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

public class DecisionActivity extends AppCompatActivity {


    private final int REQUEST_LOCATION_SETTINGS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isPermissionGranted = mayRequestLocation();

        if(isPermissionGranted)
        {
            transitionToActivity();
        }

    }

    private void transitionToActivity() {

        Intent intent;
        if(SafeDriveApplication.getInstance().getSharedPrefHelper().getUserLoggedState() == null)
        {
            intent = new Intent(this, LoginActivity.class);
        }
        else
        {
            intent = new Intent(this, SafeDriveActivity.class);
        }

        startActivity(intent);
        finish();
    }



    private boolean mayRequestLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(LOCATION_SERVICE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            if (shouldShowRequestPermissionRationale(LOCATION_SERVICE)) {
                Snackbar.make(getWindow().getDecorView().getRootView(), R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                        .setAction(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            @TargetApi(Build.VERSION_CODES.M)
                            public void onClick(View v) {
                                requestPermissions(new String[]{LOCATION_SERVICE}, REQUEST_LOCATION_SETTINGS);
                            }
                        });
            } else {
                requestPermissions(new String[]{LOCATION_SERVICE}, REQUEST_LOCATION_SETTINGS);
            }
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager == null || !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                displayLocationSettingsRequest(getApplicationContext());
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_SETTINGS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                transitionToActivity();
            }
        }
    }

    private void displayLocationSettingsRequest(Context context) {
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
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        transitionToActivity();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(DecisionActivity.this, REQUEST_LOCATION_SETTINGS);
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
                        transitionToActivity();
                        break;
                }
                break;
        }
    }
}
