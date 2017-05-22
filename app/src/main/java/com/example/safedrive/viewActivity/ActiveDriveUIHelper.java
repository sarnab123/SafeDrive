package com.example.safedrive.viewActivity;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.safedrive.R;
import com.example.safedrive.controller.SafeDriveController;
import com.example.safedrivelibrary.models.ActiveDriveModel;
import com.example.safedrivelibrary.models.DriveScope;
import com.example.safedrivelibrary.models.DriverPersona;

/**
 * Created by sarnab.poddar on 5/21/17.
 */

public class ActiveDriveUIHelper
{

    private View activeDriveView;
    private Activity parentActivity;
    private DriverPersona driverPersona;


    /**
     * All views defined
     */
    private TextView mTripID;
    private TextView mSessionID;
    private TextView mTripStartTime;
    private TextView mCurrentSpeed;
    private TextView mDistanceTravelled;
    private TextView mIsAutoTracked;

    private TextView mNoTripView;

    private Button mMapTrip;
    private Button mEndTrip;



    /**
     * The view containing layout_active_drive
     * @param activeDriveUI
     */

    public ActiveDriveUIHelper(Activity parentActivity, View activeDriveUI)
    {
        activeDriveView = activeDriveUI;
        this.parentActivity = parentActivity;

        mTripID = (TextView)activeDriveView.findViewById(R.id.id_activedrive_tripid);
        mSessionID = (TextView)activeDriveView.findViewById(R.id.id_activedrive_sessionid);
        mTripStartTime = (TextView)activeDriveView.findViewById(R.id.id_activedrive_starttime);
        mCurrentSpeed = (TextView)activeDriveView.findViewById(R.id.id_activedrive_speed);
        mDistanceTravelled = (TextView)activeDriveView.findViewById(R.id.id_activedrive_distance);
        mIsAutoTracked = (TextView)activeDriveView.findViewById(R.id.id_activedrive_autoboolean);
        mNoTripView = (TextView)activeDriveView.findViewById(R.id.id_activedrive_notstarted);

        mMapTrip = (Button)activeDriveView.findViewById(R.id.id_map_active);
        mEndTrip = (Button)activeDriveView.findViewById(R.id.id_end_active);
        mMapTrip.setEnabled(false);
        mEndTrip.setEnabled(false);

        mMapTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleMapClick();
            }
        });

        mEndTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTripSessionClick();
            }
        });

    }

    private void handleTripSessionClick() {
        if(mEndTrip.getText().equals(parentActivity.getText(R.string.active_session_start)))
        {
            SafeDriveController safeDriveController = new SafeDriveController();
            safeDriveController.startDriveSession(driverPersona);
        }
    }

    private void handleMapClick() {

    }

    /**
     * @param activeDriveModel
     * @param driverPersona
     */
    public void updateUIWithModel(@Nullable ActiveDriveModel activeDriveModel , @NonNull DriverPersona driverPersona)
    {
        this.driverPersona = driverPersona;

        activateUIbasedOnPersona(driverPersona);

        if(activeDriveModel != null)
        {
            mTripStartTime.setText(parentActivity.getText(R.string.trip_start_time) + activeDriveModel.getmStartDateTime());
            mCurrentSpeed.setText(parentActivity.getText(R.string.current_speed) + activeDriveModel.getmCurrentSpeed());
            mDistanceTravelled.setText(parentActivity.getText(R.string.trip_distance) + activeDriveModel.getDistanceInTrip());
            mIsAutoTracked.setText(parentActivity.getText(R.string.auto_tracking) + Boolean.toString(activeDriveModel.isAutoTracking()));

            if(!TextUtils.isEmpty(activeDriveModel.getmSessionID()))
            {
                mSessionID.setText(parentActivity.getText(R.string.session_id) + activeDriveModel.getmSessionID());
            }
            else if(!TextUtils.isEmpty(activeDriveModel.getmTripID()))
            {
                mSessionID.setText(parentActivity.getText(R.string.trip_id) + activeDriveModel.getmTripID());
            }

            mMapTrip.setEnabled(true);
            if(!activeDriveModel.isAutoTracking()) {
                mEndTrip.setEnabled(true);
            }
        }
        else{
            if(driverPersona.getDriveScope() == DriveScope.AUTO_SESSION_DRIVE)
            {
                mMapTrip.setVisibility(View.GONE);
                mEndTrip.setEnabled(true);
                mEndTrip.setText(parentActivity.getText(R.string.active_session_start));

            } else if(driverPersona.getDriveScope() == DriveScope.MANUAL_MODE)
            {
                mMapTrip.setVisibility(View.GONE);
                mEndTrip.setVisibility(View.GONE);
            }
            disableUI();
        }
    }

    private void disableUI()
    {
        mNoTripView.setVisibility(View.VISIBLE);

        mTripID.setVisibility(View.GONE);
        mSessionID.setVisibility(View.GONE);
        mTripStartTime.setVisibility(View.GONE);
        mCurrentSpeed.setVisibility(View.GONE);
        mDistanceTravelled.setVisibility(View.GONE);
        mIsAutoTracked.setVisibility(View.GONE);

    }

    private void setUpForNoAccess()
    {
        mNoTripView.setVisibility(View.VISIBLE);

        mTripID.setVisibility(View.GONE);
        mSessionID.setVisibility(View.GONE);
        mTripStartTime.setVisibility(View.GONE);
        mCurrentSpeed.setVisibility(View.GONE);
        mDistanceTravelled.setVisibility(View.GONE);
        mIsAutoTracked.setVisibility(View.GONE);
        mMapTrip.setEnabled(false);
        mEndTrip.setVisibility(View.GONE);

    }

    private void setUpUIForAutoTracking()
    {
        mNoTripView.setVisibility(View.INVISIBLE);

        mTripID.setVisibility(View.GONE);
        mSessionID.setVisibility(View.GONE);
        mTripStartTime.setVisibility(View.VISIBLE);
        mCurrentSpeed.setVisibility(View.VISIBLE);
        mDistanceTravelled.setVisibility(View.VISIBLE);
        mIsAutoTracked.setVisibility(View.VISIBLE);
        mMapTrip.setEnabled(true);
        mEndTrip.setEnabled(false);

    }

    private void setUpUIForSessionTracking()
    {
        mNoTripView.setVisibility(View.INVISIBLE);

        mTripID.setVisibility(View.GONE);
        mSessionID.setVisibility(View.VISIBLE);
        mTripStartTime.setVisibility(View.VISIBLE);
        mCurrentSpeed.setVisibility(View.VISIBLE);
        mDistanceTravelled.setVisibility(View.VISIBLE);
        mIsAutoTracked.setVisibility(View.VISIBLE);
        mMapTrip.setEnabled(true);
        mEndTrip.setEnabled(true);
        mEndTrip.setText(parentActivity.getText(R.string.active_session_end));

    }


    private void setUpUIForManualTripTracking()
    {
        mNoTripView.setVisibility(View.INVISIBLE);

        mTripID.setVisibility(View.VISIBLE);
        mSessionID.setVisibility(View.GONE);
        mTripStartTime.setVisibility(View.VISIBLE);
        mCurrentSpeed.setVisibility(View.VISIBLE);
        mDistanceTravelled.setVisibility(View.VISIBLE);
        mIsAutoTracked.setVisibility(View.VISIBLE);
        mMapTrip.setEnabled(true);
        mEndTrip.setEnabled(true);
        mEndTrip.setText(parentActivity.getText(R.string.active_trip_end));

    }

    private void activateUIbasedOnPersona(@Nullable DriverPersona driverPersona)
    {
        if(driverPersona != null && driverPersona.getDriveScope() != null)
        {
            switch(driverPersona.getDriveScope())
            {
                case NO_SCOPE:
                    setUpForNoAccess();
                    break;
                case AUTO_DRIVE:
                    setUpUIForAutoTracking();
                    break;
                case AUTO_SESSION_DRIVE:
                    setUpUIForSessionTracking();
                    break;
                case MANUAL_MODE:
                    setUpUIForManualTripTracking();
                    break;
            }
        }
    }
}
