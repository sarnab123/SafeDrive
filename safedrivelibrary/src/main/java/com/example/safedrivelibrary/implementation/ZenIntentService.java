package com.example.safedrivelibrary.implementation;

import android.util.Log;

import com.example.safedrivelibrary.SafeDriveManager;
import com.zendrive.sdk.AccidentInfo;
import com.zendrive.sdk.AnalyzedDriveInfo;
import com.zendrive.sdk.DriveResumeInfo;
import com.zendrive.sdk.DriveStartInfo;
import com.zendrive.sdk.EstimatedDriveInfo;
import com.zendrive.sdk.ZendriveIntentService;
import com.zendrive.sdk.ZendriveLocationSettingsResult;

/**
 * Created by sarnab.poddar on 5/21/17.
 */

public class ZenIntentService extends ZendriveIntentService {

    private String TAG = ZenIntentService.class.getName();

    public ZenIntentService() {
        super("ZenIntentService");
    }

    @Override
    public void onDriveStart(DriveStartInfo driveStartInfo) {
        Log.d(TAG, "drive started "+driveStartInfo.driveId);
        SafeDriveManager.getSafeDriveManager().updateDriveStart(driveStartInfo);

    }

    @Override
    public void onDriveEnd(EstimatedDriveInfo estimatedDriveInfo) {
        Log.d(TAG, "drive ended "+estimatedDriveInfo.distanceMeters);
        SafeDriveManager.getSafeDriveManager().updateDriveEnd(estimatedDriveInfo);
    }

    @Override
    public void onDriveAnalyzed(AnalyzedDriveInfo analyzedDriveInfo) {
        Log.d(TAG, "drive analysed "+analyzedDriveInfo.distanceMeters);
        SafeDriveManager.getSafeDriveManager().updateDriveAnalyzed(analyzedDriveInfo);
    }

    @Override
    public void onDriveResume(DriveResumeInfo driveResumeInfo) {
        Log.d(TAG, "drive resumed "+driveResumeInfo.driveId);
        SafeDriveManager.getSafeDriveManager().updateDriveResumed(driveResumeInfo);

    }

    @Override
    public void onAccident(AccidentInfo accidentInfo) {
        SafeDriveManager.getSafeDriveManager().updateOnAccident(accidentInfo);
    }

    @Override
    public void onLocationPermissionsChange(boolean permission) {
        if(!permission) {
            SafeDriveManager.getSafeDriveManager().updateLocationPermissionDenied();
        }
    }

    @Override
    public void onLocationSettingsChange(ZendriveLocationSettingsResult zendriveLocationSettingsResult) {
        Log.d(TAG, "location settings changed "+zendriveLocationSettingsResult.isSuccess());
        if(!zendriveLocationSettingsResult.isSuccess()) {
            SafeDriveManager.getSafeDriveManager().updateLocationPermissionDenied();
        }
    }
}
