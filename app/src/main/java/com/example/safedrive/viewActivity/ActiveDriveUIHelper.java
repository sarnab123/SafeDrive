package com.example.safedrive.viewActivity;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.safedrive.R;
import com.example.safedrive.controller.SafeDriveController;
import com.example.safedrive.controller.listener.IActivityUpdateListener;
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
    private ImageView mRefreshData;

    private TextView mNoTripView;

    private Button mMapTrip;
    private Button mEndTrip;

    private IActivityUpdateListener mActivityUpdateListener;



    /**
     * The view containing layout_active_drive
     * @param activeDriveUI
     * @param dataUpdateListener
     */

    public ActiveDriveUIHelper(Activity parentActivity, View activeDriveUI, IActivityUpdateListener dataUpdateListener)
    {
        activeDriveView = activeDriveUI;
        this.parentActivity = parentActivity;
        this.mActivityUpdateListener = dataUpdateListener;

        mTripID = (TextView)activeDriveView.findViewById(R.id.id_activedrive_tripid);
        mSessionID = (TextView)activeDriveView.findViewById(R.id.id_activedrive_sessionid);
        mTripStartTime = (TextView)activeDriveView.findViewById(R.id.id_activedrive_starttime);
        mCurrentSpeed = (TextView)activeDriveView.findViewById(R.id.id_activedrive_speed);
        mDistanceTravelled = (TextView)activeDriveView.findViewById(R.id.id_activedrive_distance);
        mIsAutoTracked = (TextView)activeDriveView.findViewById(R.id.id_activedrive_autoboolean);
        mNoTripView = (TextView)activeDriveView.findViewById(R.id.id_activedrive_notstarted);
        mRefreshData = (ImageView) activeDriveView.findViewById(R.id.id_activedrive_refresh);

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

        mRefreshData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRefreshClick();
            }
        });

    }

    private void handleRefreshClick()
    {
        if(mActivityUpdateListener != null)
        {
            mActivityUpdateListener.updateUIonEvent(driverPersona);
        }
    }

    private void handleTripSessionClick() {
        if(mEndTrip.getText().equals(parentActivity.getText(R.string.active_session_start)))
        {
            SafeDriveController safeDriveController = new SafeDriveController();
            safeDriveController.startDriveSession(driverPersona);
        } else if(mEndTrip.getText().equals(parentActivity.getText(R.string.active_trip_start)))
        {
            SafeDriveController safeDriveController = new SafeDriveController();
            safeDriveController.startTrip(driverPersona);
        } else if(mEndTrip.getText().equals(parentActivity.getText(R.string.active_trip_end)))
        {
            SafeDriveController safeDriveController = new SafeDriveController();
            safeDriveController.stopDrive(driverPersona);
        } else {
            SafeDriveController safeDriveController = new SafeDriveController();
            safeDriveController.stopSession(driverPersona);
        }
    }

    private void handleMapClick() {
        Toast.makeText(parentActivity,parentActivity.getText(R.string.maps_not_implemented),Toast.LENGTH_LONG).show();
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
            //noinspection StringConcatenationMissingWhitespace
            mTripStartTime.setText(parentActivity.getText(R.string.trip_start_time) + activeDriveModel.getmStartDateTime());
            //noinspection StringConcatenationMissingWhitespace
            mCurrentSpeed.setText(parentActivity.getText(R.string.current_speed) + activeDriveModel.getmCurrentSpeed());
            //noinspection StringConcatenationMissingWhitespace
            mDistanceTravelled.setText(parentActivity.getText(R.string.trip_distance) + activeDriveModel.getDistanceInTrip());
            //noinspection StringConcatenationMissingWhitespace
            mIsAutoTracked.setText(parentActivity.getText(R.string.auto_tracking) + Boolean.toString(activeDriveModel.isAutoTracking()));

            if(!TextUtils.isEmpty(activeDriveModel.getmSessionID()))
            {
                //noinspection StringConcatenationMissingWhitespace
                mSessionID.setText(parentActivity.getText(R.string.session_id) + activeDriveModel.getmSessionID());
                mEndTrip.setText(parentActivity.getText(R.string.active_session_end));
            }
            else if(!TextUtils.isEmpty(activeDriveModel.getmTripID()))
            {
                //noinspection StringConcatenationMissingWhitespace
                mTripID.setText(parentActivity.getText(R.string.trip_id) + activeDriveModel.getmTripID());
                mEndTrip.setText(parentActivity.getText(R.string.active_trip_end));
            }

            mMapTrip.setVisibility(View.VISIBLE);
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
                mEndTrip.setEnabled(true);
                mEndTrip.setText(parentActivity.getText(R.string.active_trip_start));
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
        mEndTrip.setText(parentActivity.getText(R.string.active_session_start));

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
        mEndTrip.setText(parentActivity.getText(R.string.active_trip_start));

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
